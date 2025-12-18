package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/troybllo/finflow/api-gateway/internal/config"
	"github.com/troybllo/finflow/api-gateway/internal/router"
)

// FinFlow API Gateway
// Day 2: Go + Gin framework setup
//
// This gateway sits in front of all microservices and handles:
// - Rate limiting (Day 3)
// - CORS

func main() {
	// Load configuration
	cfg := config.Load()

	// Initialize router with all routes and middleware
	r := router.Setup(cfg)

	// Create HTTP server
	srv := &http.Server{
		Addr:         fmt.Sprintf(":%s", cfg.Port),
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start server in a goroutine
	go func() {
		log.Printf(" FinFlow API Gateway starting on port %s", cfg.Port)
		log.Printf(" Environment: %s", cfg.Environment)
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Failed to start server: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	log.Println(" Shutting down server...")

	// Graceful shutdown with 5 second timeout
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		log.Fatal("Server forced to shutdown:", err)
	}

	log.Println("Server exited successfully")
}
