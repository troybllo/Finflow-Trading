package middleware

import (
	"log"
	"time"

	"github.com/gin-gonic/gin"
)

// Logger is a custom logging middleware
func Logger() gin.HandlerFunc {
	return func(c *gin.Context) {
		// Start timer
		start := time.Now()
		path := c.Request.URL.Path
		query := c.Request.URL.RawQuery
		reqSize := c.Request.ContentLength

		// Process request
		c.Next()

		duration := time.Since(start)
		statusCode := c.Writer.Status()

		respSize := c.Writer.Size()

		// Log format: [STATUS] METHOD PATH | DURATION | IP
		log.Printf("[%d] %s %s | %v | %s",
			statusCode,
			c.Request.Method,
			path,
			duration,
			c.ClientIP(),
		)

		// Log query string if present
		if query != "" {
			log.Printf("  Query: %s", query)
		}

		// Log request size if available
		if reqSize > 0 {
			log.Printf("  Request Size: %d bytes | Response: %d bytes", reqSize, respSize)
		}

		// Log errors if any
		if len(c.Errors) > 0 {
			log.Printf("  Errors: %v", c.Errors.String())
		}
	}
}
