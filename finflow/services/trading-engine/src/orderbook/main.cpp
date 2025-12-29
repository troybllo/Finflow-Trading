#include "order.h"
#include "orderbook.h"
#include <iomanip>
#include <iostream>
#include <limits>
#include <string>
#include <vector>

using namespace trading;

// Structure to track executed trades
struct Trade {
  uint64_t buyOrderId;
  uint64_t sellOrderId;
  double price;
  double quantity;
};

std::vector<Trade> recentTrades;
uint64_t nextOrderId = 1;

// Clear screen (cross-platform)
void clearScreen() {
#ifdef _WIN32
  system("cls");
#else
  system("clear");
#endif
}

// Print separator line
void printLine(char c = '=', int width = 80) {
  std::cout << std::string(width, c) << "\n";
}

// Print centered text
void printCentered(const std::string &text, int width = 80) {
  int padding = (width - text.length()) / 2;
  std::cout << std::string(padding, ' ') << text << "\n";
}

// Display the orderbook in a nice format
void displayOrderBook(const OrderBook &book) {
  printLine('=', 80);
  printCentered("ORDERBOOK - LIVE MARKET DATA", 80);
  printLine('=', 80);

  // Display spread
  double bestBid = book.getBestBid();
  double bestAsk = book.getBestAsk();
  double spread = book.getSpread();

  std::cout << "\n";
  std::cout << "  Best Bid: $" << std::fixed << std::setprecision(2)
            << std::setw(8) << bestBid;
  std::cout << " | Best Ask: $" << std::setw(8) << bestAsk;
  std::cout << " | Spread: $" << std::setw(6) << spread << "\n\n";

  printLine('-', 80);
  std::cout << std::setw(20) << "BIDS (Buy Orders)" << std::setw(20) << " "
            << std::setw(20) << "ASKS (Sell Orders)" << "\n";
  std::cout << std::setw(10) << "Price" << std::setw(10) << "Quantity"
            << std::setw(20) << " " << std::setw(10) << "Price" << std::setw(10)
            << "Quantity"
            << "\n";
  printLine('-', 80);

  std::cout << "\n";
  std::cout << "  (Orderbook internal state - use menu to add orders)\n\n";
  printLine('=', 80);
}

// Display recent trades
void displayRecentTrades() {
  if (recentTrades.empty()) {
    return;
  }

  std::cout << "\n";
  printLine('-', 80);
  printCentered("RECENT TRADES", 80);
  printLine('-', 80);

  std::cout << std::setw(15) << "Buy Order" << std::setw(15) << "Sell Order"
            << std::setw(15) << "Price" << std::setw(15) << "Quantity" << "\n";
  printLine('-', 80);

  // Show last 5 trades
  size_t start = recentTrades.size() > 5 ? recentTrades.size() - 5 : 0;
  for (size_t i = start; i < recentTrades.size(); i++) {
    const Trade &t = recentTrades[i];
    std::cout << std::setw(15) << t.buyOrderId << std::setw(15) << t.sellOrderId
              << std::setw(14) << "$" << std::fixed << std::setprecision(2)
              << t.price << std::setw(15) << t.quantity << "\n";
  }

  printLine('=', 80);
}

// Display menu
void displayMenu() {
  std::cout << "\n";
  printLine('-', 80);
  std::cout << "  [1] Add BUY Order\n";
  std::cout << "  [2] Add SELL Order\n";
  std::cout << "  [3] View Orderbook\n";
  std::cout << "  [4] View Recent Trades\n";
  std::cout << "  [5] Exit\n";
  printLine('-', 80);
  std::cout << "  Enter choice: ";
}

// Add an order interactively
void addOrderInteractive(OrderBook &book, OrderSide side) {
  double price, quantity;

  std::cout << "\n";
  printLine('-', 80);
  if (side == OrderSide::BUY) {
    std::cout << "  ADD BUY ORDER\n";
  } else {
    std::cout << "  ADD SELL ORDER\n";
  }
  printLine('-', 80);

  std::cout << "  Enter price: $";
  std::cin >> price;

  std::cout << "  Enter quantity: ";
  std::cin >> quantity;

  if (std::cin.fail() || price <= 0 || quantity <= 0) {
    std::cin.clear();
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    std::cout << "\n  ERROR: Invalid input! Price and quantity must be "
                 "positive.\n";
    std::cout << "  Press Enter to continue...";
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    std::cin.get();
    return;
  }

  Order newOrder(nextOrderId++, side, price, quantity);

  std::cout << "\n  Order #" << newOrder.getId() << " created: ";
  if (side == OrderSide::BUY) {
    std::cout << "BUY ";
  } else {
    std::cout << "SELL ";
  }
  std::cout << quantity << " @ $" << std::fixed << std::setprecision(2) << price
            << "\n";

  book.addOrder(newOrder);

  // Try to match orders
  std::cout << "  Attempting to match orders...\n";
  book.matchOrders();

  std::cout << "\n  Order added successfully!\n";
  std::cout << "  Press Enter to continue...";
  std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
  std::cin.get();
}

int main() {
  OrderBook book;

  std::cout << std::fixed << std::setprecision(2);

  clearScreen();
  printLine('=', 80);
  printCentered("FINFLOW TRADING ENGINE", 80);
  printCentered("Orderbook Matching System", 80);
  printLine('=', 80);

  std::cout << "\n  Welcome to the FinFlow Trading Engine!\n";
  std::cout << "  This is a simple orderbook demonstration.\n\n";

  // Add some sample orders to start
  std::cout << "  Loading sample market data...\n\n";

  book.addOrder(Order(nextOrderId++, OrderSide::BUY, 99.50, 100));
  book.addOrder(Order(nextOrderId++, OrderSide::BUY, 99.00, 150));
  book.addOrder(Order(nextOrderId++, OrderSide::BUY, 98.50, 200));

  book.addOrder(Order(nextOrderId++, OrderSide::SELL, 100.50, 120));
  book.addOrder(Order(nextOrderId++, OrderSide::SELL, 101.00, 180));
  book.addOrder(Order(nextOrderId++, OrderSide::SELL, 101.50, 90));

  std::cout << "  Sample orders loaded!\n";
  std::cout << "  Press Enter to continue...";
  std::cin.get();

  bool running = true;
  while (running) {
    clearScreen();
    displayOrderBook(book);
    displayRecentTrades();
    displayMenu();

    int choice;
    std::cin >> choice;

    if (std::cin.fail()) {
      std::cin.clear();
      std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
      continue;
    }

    switch (choice) {
    case 1:
      addOrderInteractive(book, OrderSide::BUY);
      break;
    case 2:
      addOrderInteractive(book, OrderSide::SELL);
      break;
    case 3:
      clearScreen();
      displayOrderBook(book);
      std::cout << "\n  Press Enter to continue...";
      std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
      std::cin.get();
      break;
    case 4:
      clearScreen();
      displayRecentTrades();
      std::cout << "\n  Press Enter to continue...";
      std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
      std::cin.get();
      break;
    case 5:
      running = false;
      break;
    default:
      std::cout << "\n  Invalid choice! Press Enter to continue...";
      std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
      std::cin.get();
      break;
    }
  }

  clearScreen();
  printLine('=', 80);
  printCentered("Thank you for using FinFlow Trading Engine!", 80);
  printLine('=', 80);
  std::cout << "\n";

  return 0;
}
