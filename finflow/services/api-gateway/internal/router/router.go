package router

import (
	"github.com/gin-gonic/gin"
	"github.com/troybllo/finflow/api-gateway/internal/config"
	"github.com/troybllo/finflow/api-gateway/internal/handlers"
	"github.com/troybllo/finflow/api-gateway/internal/middleware"
)

// Setup configures and returns the router
func Setup(cfg *config.Config) *gin.Engine {
	// Set Gin mode based on environment
	if cfg.Environment == "production" {
		gin.SetMode(gin.ReleaseMode)
	}

	// Create router
	r := gin.New()

	// Global middleware
	r.Use(gin.Recovery())                      // Recover from panics
	r.Use(middleware.Logger())                 // Custom logger
	r.Use(middleware.CORS(cfg.AllowedOrigins)) // CORS

	// Initialize handlers
	healthHandler := handlers.NewHealthHandler(cfg)
	proxyHandler := handlers.NewProxyHandler(cfg)

	// Health check endpoints (no authentication required)
	r.GET("/health", healthHandler.Health)
	r.GET("/ready", healthHandler.Ready)
	r.GET("/ping", healthHandler.Ping)

	// Root endpoint
	r.GET("/", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"service":     "FinFlow API Gateway",
			"version":     "0.1.0",
			"environment": cfg.Environment,
			"status":      "running",
		})
	})

	// API routes with rate limiting
	api := r.Group("/api/v1")
	if cfg.RateLimitEnabled {
		api.Use(middleware.RateLimiter(cfg.RateLimitRequests, cfg.RateLimitWindow))
	}

	{
		// Analytics service routes
		analytics := api.Group("/analytics")
		{
			analytics.Any("/*proxy", proxyHandler.ProxyToAnalytics)
		}

		// Trading engine routes (to be implemented)
		trading := api.Group("/trading")
		{
			trading.Any("/*proxy", proxyHandler.ProxyToTrading)
		}

		// Portfolio service routes (to be implemented)
		portfolio := api.Group("/portfolio")
		{
			portfolio.Any("/*proxy", proxyHandler.ProxyToPortfolio)
		}
	}

	return r
}
