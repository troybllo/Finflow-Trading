package config

import (
	"fmt"
	"log"
	"os"

	"github.com/joho/godotenv"
)

// Config holds all application configuration
type Config struct {
	Port        string
	Environment string

	// Service URLs
	AnalyticsServiceURL string
	TradingServiceURL   string
	PortfolioServiceURL string
	NotificationURL     string

	// Security
	JWTSecret string

	// Rate Limiting (Day 3)
	RateLimitEnabled  bool
	RateLimitRequests int
	RateLimitWindow   int // seconds

	// CORS
	AllowedOrigins []string
}

// Load reads configuration from environment variables
func Load() *Config {
	// Load .env file if it exists (development)
	_ = godotenv.Load()

	cfg := &Config{
		Port:        getEnv("PORT", "8080"),
		Environment: getEnv("ENVIRONMENT", "development"),

		// Service URLs
		AnalyticsServiceURL: getEnv("ANALYTICS_SERVICE_URL", "http://localhost:8000"),
		TradingServiceURL:   getEnv("TRADING_SERVICE_URL", "http://localhost:8001"),
		PortfolioServiceURL: getEnv("PORTFOLIO_SERVICE_URL", "http://localhost:8002"),
		NotificationURL:     getEnv("NOTIFICATION_SERVICE_URL", "http://localhost:8003"),

		// Security
		JWTSecret: getEnv("JWT_SECRET", "dev-secret-change-in-production"),

		// Rate Limiting
		RateLimitEnabled:  getEnv("RATE_LIMIT_ENABLED", "true") == "true",
		RateLimitRequests: getEnvInt("RATE_LIMIT_REQUESTS", 100),
		RateLimitWindow:   getEnvInt("RATE_LIMIT_WINDOW", 60),

		// CORS
		AllowedOrigins: []string{
			getEnv("FRONTEND_URL", "http://localhost:3000"),
		},
	}

	log.Println(" Configuration loaded")
	return cfg
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

// getEnvInt gets an environment variable as int or returns a default value
func getEnvInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		// Simple conversion (in production, add error handling)
		var intValue int
		if _, err := fmt.Sscanf(value, "%d", &intValue); err == nil {
			return intValue
		}
	}
	return defaultValue
}
