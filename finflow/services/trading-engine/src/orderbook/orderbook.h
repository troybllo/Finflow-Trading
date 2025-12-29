#ifndef ORDERBOOK_H
#define ORDERBOOK_H

#include "order.h"
#include <functional>
#include <map>
#include <vector>

namespace trading {

class OrderBook {
public:
  OrderBook();

  void addOrder(const Order &order);
  void removeOrder(uint64_t orderId);
  void matchOrders();

  double getSpread() const;
  double getBestBid() const;
  double getBestAsk() const;

private:
  std::multimap<double, Order, std::greater<double>> bids_;
  std::multimap<double, Order> asks_;
};
} // namespace trading
#endif // ORDERBOOK_H
