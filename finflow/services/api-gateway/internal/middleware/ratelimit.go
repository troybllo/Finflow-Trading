package middleware

import (
	"log"
	"net/http"
	"sync"
	"time"

	"github.com/gin-gonic/gin"
)

type TokenBucket struct {
	capacity       float64
	tokens         float64
	refillRate     float64
	lastRefillTime time.Time
	mu             sync.Mutex
}

func NewTokenBucket(capacity, refillRate float64) *TokenBucket {
	return &TokenBucket{
		capacity:       capacity,
		tokens:         capacity,
		refillRate:     refillRate,
		lastRefillTime: time.Now(),
	}
}

func (tb *TokenBucket) Allow() bool {
	tb.mu.Lock()
	defer tb.mu.Unlock()

	tb.refill()

	if tb.tokens >= 1 {
		tb.tokens -= 1
		return true
	}

	return false
}

func (tb *TokenBucket) refill() {
	now := time.Now()
	elapsed := now.Sub(tb.lastRefillTime).Seconds()

	tokensToAdd := elapsed * tb.refillRate
	tb.tokens += tokensToAdd

	if tb.tokens > tb.capacity {
		tb.tokens = tb.capacity
	}
	tb.lastRefillTime = now
}

func (tb *TokenBucket) GetTokens() float64 {
	tb.mu.Lock()
	defer tb.mu.Unlock()
	tb.refill()
	return tb.tokens
}

type rateLimiter struct {
	buckets    map[string]*TokenBucket
	capacity   float64
	refillRate float64
	mu         sync.RWMutex
}

func newRateLimiter(capacity, refillRate float64) *rateLimiter {
	return &rateLimiter{
		buckets:    make(map[string]*TokenBucket),
		capacity:   capacity,
		refillRate: refillRate,
	}
}

func (rl *rateLimiter) Allow(clientID string) bool {
	// Try to get existing bucket with read lock (fast path)
	rl.mu.RLock()
	bucket, exists := rl.buckets[clientID]
	rl.mu.RUnlock()

	// If bucket doesn't exist, create it with write lock
	if !exists {
		rl.mu.Lock()
		// Double-check in case another goroutine created it
		bucket, exists = rl.buckets[clientID]
		if !exists {
			bucket = NewTokenBucket(rl.capacity, rl.refillRate)
			rl.buckets[clientID] = bucket
		}
		rl.mu.Unlock()
	}

	return bucket.Allow()
}

// RateLimiter returns a Gin middleware that limits requests per client
func RateLimiter(maxRequests, windowSeconds int) gin.HandlerFunc {
	// Convert config values to token bucket parameters
	capacity := float64(maxRequests)
	refillRate := float64(maxRequests) / float64(windowSeconds)

	log.Printf("Rate Limiter initialized: capacity=%.2f, refillRate=%.2f/sec (max %d requests per %d seconds)",
		capacity, refillRate, maxRequests, windowSeconds)

	limiter := newRateLimiter(capacity, refillRate)

	requestCount := 0
	return func(c *gin.Context) {
		// Use client IP as identifier
		clientID := c.ClientIP()
		requestCount++

		if !limiter.Allow(clientID) {
			log.Printf("[RATE LIMIT] Request #%d from %s BLOCKED", requestCount, clientID)
			c.JSON(http.StatusTooManyRequests, gin.H{
				"error": "Rate limit exceeded. Please try again later.",
			})
			c.Abort()
			return
		}

		if requestCount%10 == 0 {
			log.Printf("[RATE LIMIT] Request #%d from %s allowed", requestCount, clientID)
		}

		c.Next()
	}
}
