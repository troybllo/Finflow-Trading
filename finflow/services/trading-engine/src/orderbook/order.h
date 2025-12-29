#ifndef ORDER_H
#define ORDER_H

#include <cstdint>
namespace trading {

enum class OrderSide { BUY, SELL };

enum class OrderStatus { OPEN, FILLED, PARTIALLY_FILLED, CANCELLED };

class Order {
public:
  Order(uint64_t id, OrderSide side, double price, double quantity);

  uint64_t getId() const { return orderId_; }
  OrderSide getSide() const { return side_; }
  double getPrice() const { return price_; }
  double getQuantity() const { return quantity_; }
  double getFilledQuantity() const { return filledQty_; }
  OrderStatus getStatus() const { return status_; }

  void fill(double qty);
  void cancel();

private:
  uint64_t orderId_;
  OrderSide side_;
  double price_;
  double quantity_;
  double filledQty_;
  OrderStatus status_;
  uint64_t timestamp_;
};

} // namespace trading

#endif // ORDER_H
