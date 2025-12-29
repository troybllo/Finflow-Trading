#include "orderbook.h"
#include "order.h"
#include <algorithm>
#include <cstdint>

namespace trading {
OrderBook::OrderBook() {}

void OrderBook::addOrder(const Order &order) {
  double price = order.getPrice();
  OrderSide side = order.getSide();

  if (side == OrderSide::BUY) {
    bids_.insert({price, order});
  } else {
    asks_.insert({price, order});
  }
}

void OrderBook::removeOrder(uint64_t orderId) {
  for (auto it = bids_.begin(); it != bids_.end(); ++it) {
    if (it->second.getId() == orderId) {
      bids_.erase(it);
      return;
    }
  }

  for (auto it = asks_.begin(); it != asks_.end(); ++it) {
    if (it->second.getId() == orderId) {
      asks_.erase(it);
      return;
    }
  }
}

void OrderBook::matchOrders() {
  while (!bids_.empty() && !asks_.empty()) {
    auto bestBidIt = bids_.begin();
    auto bestAskIt = asks_.begin();

    double bidPrice = bestBidIt->first;
    double askPrice = bestAskIt->first;

    if (bidPrice < askPrice) {
      break;
    }

    Order buyOrder = bestBidIt->second;
    Order sellOrder = bestAskIt->second;

    double buyQty = buyOrder.getQuantity() - buyOrder.getFilledQuantity();
    double sellQty = sellOrder.getQuantity() - sellOrder.getFilledQuantity();

    double tradeQty = std::min(buyQty, sellQty);

    bids_.erase(bestBidIt);
    asks_.erase(bestAskIt);

    buyOrder.fill(tradeQty);
    sellOrder.fill(tradeQty);

    if (buyOrder.getStatus() == OrderStatus::PARTIALLY_FILLED) {
      bids_.insert({bidPrice, buyOrder});
    }
    if (sellOrder.getStatus() == OrderStatus::PARTIALLY_FILLED) {
      asks_.insert({askPrice, sellOrder});
    }
  }
}

double OrderBook::getSpread() const {
  if (bids_.empty() || asks_.empty()) {
    return 0.0;
  }
  return getBestAsk() - getBestBid();
}

double OrderBook::getBestAsk() const {
  if (!asks_.empty()) {
    auto it = asks_.begin();
    double bestAsk = it->first;
    return bestAsk;
  }
  return 0;
}

double OrderBook::getBestBid() const {
  if (!bids_.empty()) {
    auto it = bids_.begin();
    double bestBid = it->first;
    return bestBid;
  }
  return 0;
}

} // namespace trading
