package middleware

import (
	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
)

func RequestID() gin.HandlerFunc {
	return func(c *gin.Context) {
		// Generates unique ID
		requestID := uuid.New().String()

		// Adds to Context
		c.Header("X-Request-ID", requestID)

		c.Next()
	}
}
