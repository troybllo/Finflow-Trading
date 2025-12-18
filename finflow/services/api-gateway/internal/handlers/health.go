package handlers

import (
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
)

type HealthHandler struct {
	cfg interface{}
}

type HealthResponse struct {
	Status    string `json:"status"`
	Timestamp int64  `json:"timestamp"`
	Service   string `json:"service"`
	Version   string `json:"version"`
}

type ReadinessResponse struct {
	Status       string            `json:"status"`
	Timestamp    int64             `json:"timestamp"`
	Dependencies map[string]string `json:"dependencies"`
}

func NewHealthHandler(cfg interface{}) *HealthHandler {
	return &HealthHandler{}
}

func ReadinessCheck(c *gin.Context) {
	dependencies := map[string]string{
		"analytics_service":    "healthy",
		"trading_service":      "healthy",
		"portfolio_service":    "healthy",
		"notification_service": "healthy",
	}

	c.JSON(http.StatusOK, ReadinessResponse{
		Status:       "ready",
		Timestamp:    time.Now().Unix(),
		Dependencies: dependencies,
	})
}
