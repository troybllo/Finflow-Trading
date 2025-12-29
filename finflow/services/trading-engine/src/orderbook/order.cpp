#include "order.h"
#include <algorithm>
#include <chrono>
#include <cstdint>

namespace trading {

Order::Order(uint64_t id, OrderSide side, double price, double quantity)
    : orderId_(id), side_(side), price_(price), quantity_(quantity),
      filledQty_(0.0), status_(OrderStatus::OPEN),
      timestamp_(std::chrono::system_clock::now().time_since_epoch().count()) {}

void Order::fill(double quantity) {
  filledQty_ += quantity;
  if (filledQty_ >= quantity_) {
    status_ = OrderStatus::FILLED;
  } else {
    status_ = OrderStatus::PARTIALLY_FILLED;
  }
}

void Order::cancel() { status_ = OrderStatus::CANCELLED; }

} // namespace trading
