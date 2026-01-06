// ============================================================
// Core Domain Types
// ============================================================

export interface User {
  id: string;
  name: string;
  email: string;
  avatar?: string;
  createdAt: string;
  updatedAt: string;
}

export interface AuthResponse {
  token: string;
  refreshToken: string;
  user: User;
  expiresIn: number;
}

// ============================================================
// Portfolio & Holdings
// ============================================================

export interface Holding {
  id: string;
  userId: string;
  symbol: string;
  quantity: number;
  averageCost: number;
  currentPrice?: number;
  marketValue?: number;
  unrealizedPnL?: number;
  unrealizedPnLPercent?: number;
  assetType: "stock" | "crypto" | "forex" | "option" | "future";
  exchange: string;
  createdAt: string;
  updatedAt: string;
}

export interface Portfolio {
  id: string;
  userId: string;
  totalValue: number;
  cashBalance: number;
  buyingPower: number;
  dailyChange: number;
  dailyChangePercent: number;
  totalGainLoss: number;
  totalGainLossPercent: number;
  holdings: Holding[];
  updatedAt: string;
}

export interface PortfolioSummary {
  totalValue: number;
  dailyPnL: number;
  dailyPnLPercent: number;
  weeklyPnL: number;
  monthlyPnL: number;
  allTimePnL: number;
  winRate: number;
  totalTrades: number;
  activePositions: number;
}

// ============================================================
// Transactions & Orders
// ============================================================

export type TransactionSide = "buy" | "sell";
export type TransactionType = "market" | "limit" | "stop" | "stop_limit";
export type TransactionStatus =
  | "pending"
  | "filled"
  | "partial"
  | "cancelled"
  | "rejected";

export interface Transaction {
  id: string;
  userId: string;
  symbol: string;
  side: TransactionSide;
  type: TransactionType;
  quantity: number;
  price: number;
  totalValue: number;
  fees: number;
  status: TransactionStatus;
  exchange: string;
  externalId?: string; // ID from external platform
  notes?: string;
  tags?: string[];
  executedAt: string;
  createdAt: string;
}

export interface Order {
  id: string;
  userId: string;
  symbol: string;
  side: TransactionSide;
  type: TransactionType;
  quantity: number;
  filledQuantity: number;
  remainingQuantity: number;
  limitPrice?: number;
  stopPrice?: number;
  status: TransactionStatus;
  exchange: string;
  externalId?: string;
  createdAt: string;
  updatedAt: string;
  filledAt?: string;
  cancelledAt?: string;
}

// ============================================================
// Trade Journal
// ============================================================

export interface JournalEntry {
  id: string;
  userId: string;
  transactionId?: string;
  title: string;
  content: string;
  mood?: "confident" | "neutral" | "uncertain" | "regret";
  tags?: string[];
  images?: string[];
  isPublic: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface TradeAnalysis {
  transactionId: string;
  entryReason: string;
  exitReason?: string;
  setupType?: string;
  timeframe?: string;
  riskRewardRatio?: number;
  initialStop?: number;
  targetPrice?: number;
  actualPnL?: number;
  lessonsLearned?: string;
}

// ============================================================
// External Account Integration
// ============================================================

export type ExternalPlatform =
  | "alpaca"
  | "robinhood"
  | "coinbase"
  | "binance"
  | "interactive_brokers"
  | "td_ameritrade";
export type ConnectionStatus =
  | "connected"
  | "disconnected"
  | "error"
  | "syncing";

export interface ExternalAccount {
  id: string;
  userId: string;
  platform: ExternalPlatform;
  accountName: string;
  accountNumber?: string;
  status: ConnectionStatus;
  lastSyncAt?: string;
  syncEnabled: boolean;
  holdings?: Holding[];
  createdAt: string;
  updatedAt: string;
}

export interface SyncStatus {
  accountId: string;
  status: ConnectionStatus;
  lastSync: string;
  nextSync?: string;
  itemsSynced: number;
  errors?: string[];
}

// ============================================================
// Social Features
// ============================================================

export interface Friend {
  id: string;
  userId: string;
  name: string;
  avatar?: string;
  status: "pending" | "accepted" | "blocked";
  followedAt?: string;
}

export interface FriendActivity {
  id: string;
  userId: string;
  userName: string;
  userAvatar?: string;
  activityType: "trade" | "post" | "achievement";
  title: string;
  description?: string;
  profitLoss?: number;
  symbol?: string;
  isPublic: boolean;
  likesCount: number;
  commentsCount: number;
  createdAt: string;
}

export interface Leaderboard {
  period: "daily" | "weekly" | "monthly" | "all_time";
  users: LeaderboardEntry[];
  updatedAt: string;
}

export interface LeaderboardEntry {
  rank: number;
  userId: string;
  userName: string;
  avatar?: string;
  totalPnL: number;
  totalPnLPercent: number;
  winRate: number;
  totalTrades: number;
  badges?: string[];
}

// ============================================================
// Analytics & Insights
// ============================================================

export interface PerformanceMetrics {
  userId: string;
  period: "daily" | "weekly" | "monthly" | "yearly" | "all_time";
  totalPnL: number;
  totalPnLPercent: number;
  totalTrades: number;
  winningTrades: number;
  losingTrades: number;
  winRate: number;
  avgWin: number;
  avgLoss: number;
  largestWin: number;
  largestLoss: number;
  profitFactor: number;
  sharpeRatio?: number;
  maxDrawdown: number;
  avgHoldingPeriod: number;
  bestSymbol?: string;
  worstSymbol?: string;
}

export interface AIInsight {
  id: string;
  type: "opportunity" | "risk" | "pattern" | "recommendation";
  title: string;
  description: string;
  confidence: number;
  symbols?: string[];
  actionable: boolean;
  priority: "low" | "medium" | "high";
  createdAt: string;
  expiresAt?: string;
}

export interface MarketData {
  symbol: string;
  price: number;
  change: number;
  changePercent: number;
  volume: number;
  high: number;
  low: number;
  open: number;
  previousClose: number;
  timestamp: string;
}

// ============================================================
// Notifications
// ============================================================

export type NotificationType =
  | "trade"
  | "price_alert"
  | "friend"
  | "achievement"
  | "system";
export type NotificationPriority = "low" | "medium" | "high" | "urgent";

export interface Notification<T> {
  id: string;
  userId: string;
  type: NotificationType;
  priority: NotificationPriority;
  title: string;
  message: string;
  data?: Record<string, T>;
  isRead: boolean;
  actionUrl?: string;
  createdAt: string;
}

// ============================================================
// API Request/Response Types
// ============================================================

export interface PaginationParams {
  page?: number;
  limit?: number;
  sortBy?: string;
  sortOrder?: "asc" | "desc";
}

export interface PaginatedResponse<T> {
  data: T[];
  pagination: {
    page: number;
    limit: number;
    total: number;
    totalPages: number;
  };
}

export interface ApiError {
  code: string;
  message: string;
  details?: Record<string, any>;
  timestamp: string;
}

export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  error?: ApiError;
}
