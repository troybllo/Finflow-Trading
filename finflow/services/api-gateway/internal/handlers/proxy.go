package handlers

import (
	"fmt"
	"net/http"
	"net/http/httputil"
	"net/url"

	"github.com/gin-gonic/gin"
)

type ProxyHandler struct {
	services map[string]*httputil.ReverseProxy
}

func NewProxyHandler() *ProxyHandler {
	return &ProxyHandler{
		services: make(map[string]*httputil.ReverseProxy),
	}
}

// AddService registers a backend service
func (p *ProxyHandler) AddService(name, backendURL string) error {
	target, err := url.Parse(backendURL)
	if err != nil {
		return fmt.Errorf("invalid backend URL: %w", err)
	}

	proxy := httputil.NewSingleHostReverseProxy(target)

	// Customize error handling
	proxy.ErrorHandler = func(w http.ResponseWriter, r *http.Request, err error) {
		w.WriteHeader(http.StatusBadGateway)
		w.Write([]byte(fmt.Sprintf(`{"error": "service unavailable: %s"}`, name)))
	}

	// Add custom headers to responses
	proxy.ModifyResponse = func(resp *http.Response) error {
		resp.Header.Set("X-Proxied-By", "FinFlow-Gateway")
		return nil
	}

	p.services[name] = proxy
	return nil
}

// ProxyToService returns a Gin handler that forwards requests to the specified service
func (p *ProxyHandler) ProxyToService(serviceName string) gin.HandlerFunc {
	return func(c *gin.Context) {
		proxy, exists := p.services[serviceName]
		if !exists {
			c.JSON(http.StatusNotFound, gin.H{"error": fmt.Sprintf("service '%s' not configured", serviceName)})
			return
		}

		// For /api/v1/analytics/health, the "proxy" param contains "/health"
		proxyPath := c.Param("proxy")

		// Update the request path to just the backend path
		c.Request.URL.Path = proxyPath

		// Add forwarding headers
		c.Request.Header.Set("X-Forwarded-Host", c.Request.Host)
		c.Request.Header.Set("X-Real-IP", c.ClientIP())
		c.Request.Header.Set("X-Forwarded-Proto", "http")

		// Forward the request to backend service
		proxy.ServeHTTP(c.Writer, c.Request)
	}
}

func (p *ProxyHandler) ProxyToAnalytics() gin.HandlerFunc {
	return p.ProxyToService("analytics")
}

func (p *ProxyHandler) ProxyToTrading() gin.HandlerFunc {
	return p.ProxyToService("trading")
}

func (p *ProxyHandler) ProxyToPortfolio() gin.HandlerFunc {
	return p.ProxyToService("portfolio")
}
