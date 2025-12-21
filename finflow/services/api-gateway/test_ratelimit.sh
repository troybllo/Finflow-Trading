#!/bin/bash

# Test rate limiting for FinFlow API Gateway
# Expected: 100 requests allowed, then 429 errors

ENDPOINT="http://localhost:8080/api/v1/analytics/health"
TOTAL_REQUESTS=105

echo "Testing Rate Limiting on $ENDPOINT"
echo "Sending $TOTAL_REQUESTS requests..."
echo ""

success_count=0
rate_limited_count=0

for i in $(seq 1 $TOTAL_REQUESTS); do
    # Make request and capture status code
    status=$(curl -s -o /dev/null -w "%{http_code}" $ENDPOINT)

    if [ "$status" == "200" ]; then
        ((success_count++))
        echo "✓ Request $i: Success (200)"
    elif [ "$status" == "429" ]; then
        ((rate_limited_count++))
        echo "✗ Request $i: Rate Limited (429)"
    else
        echo "? Request $i: Unexpected status ($status)"
    fi

    # Small delay to simulate real requests
    sleep 0.01
done

echo ""
echo "==================== RESULTS ===================="
echo "Total Requests:     $TOTAL_REQUESTS"
echo "Successful:         $success_count"
echo "Rate Limited (429): $rate_limited_count"
echo ""

if [ $rate_limited_count -gt 0 ]; then
    echo "✓ Rate limiting is WORKING!"
else
    echo "✗ Rate limiting is NOT working - all requests succeeded"
fi
