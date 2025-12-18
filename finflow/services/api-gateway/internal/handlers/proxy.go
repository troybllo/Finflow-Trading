package handlers

import (
	"net/http/httputil"
	"time"

	"honnef.co/go/tools/config"
)

type ProxyHandler struct {
	cfg      *config.Config
	services map[string]*httputil.ReverseProxy
	timeout  time.Duration
}

func NewProxyHandler(cfg *config.Config) *ProxyHandler {
	return &ProxyHandler{
		cfg: cfg,
	}
}
