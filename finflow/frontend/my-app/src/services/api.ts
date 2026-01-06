import axios, { AxiosError, AxiosInstance, AxiosRequestConfig } from "axios";
import type {
  User,
  AuthResponse,
  Portfolio,
  PortfolioSummary,
  Holding,
  Transaction,
  Order,
  JournalEntry,
  TradeAnalysis,
  ExternalAccount,
  SyncStatus,
  Friend,
  FriendActivity,
  Leaderboard,
  PerformanceMetrics,
  AIInsight,
  MarketData,
  Notification,
  PaginationParams,
  PaginatedResponse,
  TransactionSide,
  TransactionType,
  ExternalPlatform,
} from "../types";

// ============================================================
// API Client Configuration
// ============================================================

const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";
const API_VERSION = "v1";

const apiClient: AxiosInstance = axios.create({
  baseURL: `${API_BASE_URL}/api/${API_VERSION}`,
  timeout: 30000,
  headers: {
    "Content-Type": "application/json",
  },
});

// ============================================================
// Request/Response Interceptors
// ============================================================

// Request interceptor - attach auth token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("authToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    // Add request ID for tracing
    config.headers["X-Request-ID"] = crypto.randomUUID();

    return config;
  },
  (error) => Promise.reject(error),
);

// Response interceptor - handle errors globally
apiClient.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    if (error.response?.status === 401) {
      // Token expired, attempt refresh
      const refreshToken = localStorage.getItem("refreshToken");
      if (refreshToken) {
        try {
          const { data } = await axios.post(
            `${API_BASE_URL}/api/${API_VERSION}/auth/refresh`,
            {
              refreshToken,
            },
          );
          localStorage.setItem("authToken", data.token);
          // Retry original request
          if (error.config) {
            error.config.headers.Authorization = `Bearer ${data.token}`;
            return apiClient.request(error.config);
          }
        } catch (refreshError) {
          // Refresh failed, clear tokens and redirect to login
          localStorage.removeItem("authToken");
          localStorage.removeItem("refreshToken");
          window.location.href = "/login";
        }
      }
    }
    return Promise.reject(error);
  },
);

// ============================================================
// Helper Functions
// ============================================================

const handleApiError = <T>(error: T): never => {
  if (axios.isAxiosError(error)) {
    const message = error.response?.data?.message || error.message;
    throw new Error(message);
  }
  throw error;
};

const buildQueryString = <T>(params: Record<string, T>): string => {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null) {
      query.append(key, String(value));
    }
  });
  return query.toString();
};

// ============================================================
// Authentication API
// ============================================================

export const authAPI = {
  /**
   * Register new user
   */
  register: async (data: {
    name: string;
    email: string;
    password: string;
  }): Promise<AuthResponse> => {
    try {
      const response = await apiClient.post<AuthResponse>(
        "/auth/register",
        data,
      );
      const { token, refreshToken } = response.data;
      localStorage.setItem("authToken", token);
      localStorage.setItem("refreshToken", refreshToken);
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Login user
   */
  login: async (data: {
    email: string;
    password: string;
  }): Promise<AuthResponse> => {
    try {
      const response = await apiClient.post<AuthResponse>("/auth/login", data);
      const { token, refreshToken } = response.data;
      localStorage.setItem("authToken", token);
      localStorage.setItem("refreshToken", refreshToken);
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Logout user
   */
  logout: async (): Promise<void> => {
    try {
      await apiClient.post("/auth/logout");
      localStorage.removeItem("authToken");
      localStorage.removeItem("refreshToken");
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Refresh access token
   */
  refreshToken: async (refreshToken: string): Promise<AuthResponse> => {
    try {
      const response = await apiClient.post<AuthResponse>("/auth/refresh", {
        refreshToken,
      });
      localStorage.setItem("authToken", response.data.token);
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get current user profile
   */
  getCurrentUser: async (): Promise<User> => {
    try {
      const response = await apiClient.get<User>("/auth/me");
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Update user profile
   */
  updateProfile: async (data: Partial<User>): Promise<User> => {
    try {
      const response = await apiClient.patch<User>("/auth/me", data);
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },
};

// ============================================================
// Portfolio API
// ============================================================

export const portfolioAPI = {
  /**
   * Get user's portfolio
   */
  getPortfolio: async (userId: string): Promise<Portfolio> => {
    try {
      const response = await apiClient.get<Portfolio>(`/portfolio/${userId}`);
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get portfolio summary with key metrics
   */
  getSummary: async (userId: string): Promise<PortfolioSummary> => {
    try {
      const response = await apiClient.get<PortfolioSummary>(
        `/portfolio/${userId}/summary`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get all holdings
   */
  getHoldings: async (userId: string): Promise<Holding[]> => {
    try {
      const response = await apiClient.get<Holding[]>(
        `/portfolio/${userId}/holdings`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Add a new holding manually
   */
  addHolding: async (
    userId: string,
    data: {
      symbol: string;
      quantity: number;
      averageCost: number;
      assetType: string;
      exchange: string;
    },
  ): Promise<Holding> => {
    try {
      const response = await apiClient.post<Holding>(
        `/portfolio/${userId}/holdings`,
        data,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Update existing holding
   */
  updateHolding: async (
    userId: string,
    holdingId: string,
    data: Partial<Holding>,
  ): Promise<Holding> => {
    try {
      const response = await apiClient.patch<Holding>(
        `/portfolio/${userId}/holdings/${holdingId}`,
        data,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Delete holding
   */
  deleteHolding: async (userId: string, holdingId: string): Promise<void> => {
    try {
      await apiClient.delete(`/portfolio/${userId}/holdings/${holdingId}`);
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get portfolio performance over time
   */
  getPerformanceHistory: async (
    userId: string,
    params: {
      startDate?: string;
      endDate?: string;
      interval?: "day" | "week" | "month";
    },
  ): Promise<{ date: string; value: number }[]> => {
    try {
      const queryString = buildQueryString(params);
      const response = await apiClient.get(
        `/portfolio/${userId}/performance?${queryString}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },
};

// ============================================================
// Transaction API
// ============================================================

export const transactionAPI = {
  /**
   * Get all transactions with pagination and filters
   */
  getTransactions: async (
    userId: string,
    params?: PaginationParams & {
      symbol?: string;
      side?: TransactionSide;
      startDate?: string;
      endDate?: string;
      status?: string;
    },
  ): Promise<PaginatedResponse<Transaction>> => {
    try {
      const queryString = buildQueryString(params || {});
      const response = await apiClient.get<PaginatedResponse<Transaction>>(
        `/trading/transactions/${userId}?${queryString}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get single transaction by ID
   */
  getTransaction: async (transactionId: string): Promise<Transaction> => {
    try {
      const response = await apiClient.get<Transaction>(
        `/trading/transactions/${transactionId}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Create manual transaction (for journal entry)
   */
  createTransaction: async (
    userId: string,
    data: {
      symbol: string;
      side: TransactionSide;
      type: TransactionType;
      quantity: number;
      price: number;
      fees?: number;
      notes?: string;
      tags?: string[];
      executedAt?: string;
    },
  ): Promise<Transaction> => {
    try {
      const response = await apiClient.post<Transaction>(
        `/trading/transactions/${userId}`,
        data,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Update transaction
   */
  updateTransaction: async (
    transactionId: string,
    data: Partial<Transaction>,
  ): Promise<Transaction> => {
    try {
      const response = await apiClient.patch<Transaction>(
        `/trading/transactions/${transactionId}`,
        data,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Delete transaction
   */
  deleteTransaction: async (transactionId: string): Promise<void> => {
    try {
      await apiClient.delete(`/trading/transactions/${transactionId}`);
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get transaction statistics
   */
  getStatistics: async (
    userId: string,
    params?: { period?: "week" | "month" | "year"; symbol?: string },
  ): Promise<{
    totalVolume: number;
    totalTrades: number;
    buyCount: number;
    sellCount: number;
    avgTradeSize: number;
    totalFees: number;
  }> => {
    try {
      const queryString = buildQueryString(params || {});
      const response = await apiClient.get(
        `/trading/transactions/${userId}/stats?${queryString}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },
};

// ============================================================
// Orders API
// ============================================================

export const ordersAPI = {
  /**
   * Get all orders
   */
  getOrders: async (
    userId: string,
    params?: PaginationParams & { status?: string; symbol?: string },
  ): Promise<PaginatedResponse<Order>> => {
    try {
      const queryString = buildQueryString(params || {});
      const response = await apiClient.get<PaginatedResponse<Order>>(
        `/trading/orders/${userId}?${queryString}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get active (open) orders
   */
  getActiveOrders: async (userId: string): Promise<Order[]> => {
    try {
      const response = await apiClient.get<Order[]>(
        `/trading/orders/${userId}/active`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get single order
   */
  getOrder: async (orderId: string): Promise<Order> => {
    try {
      const response = await apiClient.get<Order>(`/trading/orders/${orderId}`);
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Cancel order
   */
  cancelOrder: async (orderId: string): Promise<Order> => {
    try {
      const response = await apiClient.post<Order>(
        `/trading/orders/${orderId}/cancel`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },
};

// ============================================================
// Journal API
// ============================================================

export const journalAPI = {
  /**
   * Get all journal entries
   */
  getEntries: async (
    userId: string,
    params?: PaginationParams & { tags?: string[] },
  ): Promise<PaginatedResponse<JournalEntry>> => {
    try {
      const queryString = buildQueryString(params || {});
      const response = await apiClient.get<PaginatedResponse<JournalEntry>>(
        `/journal/${userId}?${queryString}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get single journal entry
   */
  getEntry: async (entryId: string): Promise<JournalEntry> => {
    try {
      const response = await apiClient.get<JournalEntry>(
        `/journal/entries/${entryId}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Create journal entry
   */
  createEntry: async (
    userId: string,
    data: {
      title: string;
      content: string;
      transactionId?: string;
      mood?: string;
      tags?: string[];
      isPublic?: boolean;
    },
  ): Promise<JournalEntry> => {
    try {
      const response = await apiClient.post<JournalEntry>(
        `/journal/${userId}`,
        data,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Update journal entry
   */
  updateEntry: async (
    entryId: string,
    data: Partial<JournalEntry>,
  ): Promise<JournalEntry> => {
    try {
      const response = await apiClient.patch<JournalEntry>(
        `/journal/entries/${entryId}`,
        data,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Delete journal entry
   */
  deleteEntry: async (entryId: string): Promise<void> => {
    try {
      await apiClient.delete(`/journal/entries/${entryId}`);
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get trade analysis for a transaction
   */
  getTradeAnalysis: async (transactionId: string): Promise<TradeAnalysis> => {
    try {
      const response = await apiClient.get<TradeAnalysis>(
        `/journal/analysis/${transactionId}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Create/update trade analysis
   */
  saveTradeAnalysis: async (
    transactionId: string,
    data: Partial<TradeAnalysis>,
  ): Promise<TradeAnalysis> => {
    try {
      const response = await apiClient.post<TradeAnalysis>(
        `/journal/analysis/${transactionId}`,
        data,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },
};

// ============================================================
// External Accounts API (Connect external brokers)
// ============================================================

export const externalAccountsAPI = {
  /**
   * Get all connected external accounts
   */
  getAccounts: async (userId: string): Promise<ExternalAccount[]> => {
    try {
      const response = await apiClient.get<ExternalAccount[]>(
        `/accounts/${userId}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Connect new external account
   */
  connectAccount: async (
    userId: string,
    data: {
      platform: ExternalPlatform;
      credentials: Record<string, string>;
      accountName: string;
    },
  ): Promise<ExternalAccount> => {
    try {
      const response = await apiClient.post<ExternalAccount>(
        `/accounts/${userId}/connect`,
        data,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Disconnect external account
   */
  disconnectAccount: async (accountId: string): Promise<void> => {
    try {
      await apiClient.delete(`/accounts/${accountId}`);
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Sync external account data
   */
  syncAccount: async (accountId: string): Promise<SyncStatus> => {
    try {
      const response = await apiClient.post<SyncStatus>(
        `/accounts/${accountId}/sync`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get sync status
   */
  getSyncStatus: async (accountId: string): Promise<SyncStatus> => {
    try {
      const response = await apiClient.get<SyncStatus>(
        `/accounts/${accountId}/sync-status`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get holdings from external account
   */
  getAccountHoldings: async (accountId: string): Promise<Holding[]> => {
    try {
      const response = await apiClient.get<Holding[]>(
        `/accounts/${accountId}/holdings`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },
};

// ============================================================
// Social/Friends API
// ============================================================

export const socialAPI = {
  /**
   * Get user's friends list
   */
  getFriends: async (userId: string): Promise<Friend[]> => {
    try {
      const response = await apiClient.get<Friend[]>(
        `/social/${userId}/friends`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Search for users
   */
  searchUsers: async (query: string): Promise<User[]> => {
    try {
      const response = await apiClient.get<User[]>(
        `/social/search?q=${encodeURIComponent(query)}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Send friend request
   */
  sendFriendRequest: async (
    userId: string,
    friendId: string,
  ): Promise<Friend> => {
    try {
      const response = await apiClient.post<Friend>(
        `/social/${userId}/friends`,
        {
          friendId,
        },
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Accept friend request
   */
  acceptFriendRequest: async (
    userId: string,
    friendId: string,
  ): Promise<Friend> => {
    try {
      const response = await apiClient.post<Friend>(
        `/social/${userId}/friends/${friendId}/accept`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Reject friend request
   */
  rejectFriendRequest: async (
    userId: string,
    friendId: string,
  ): Promise<void> => {
    try {
      await apiClient.delete(`/social/${userId}/friends/${friendId}`);
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get friends' activity feed
   */
  getFriendsActivity: async (
    userId: string,
    params?: PaginationParams,
  ): Promise<PaginatedResponse<FriendActivity>> => {
    try {
      const queryString = buildQueryString(params || {});
      const response = await apiClient.get<PaginatedResponse<FriendActivity>>(
        `/social/${userId}/feed?${queryString}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get friend's profile and stats
   */
  getFriendProfile: async (
    friendId: string,
  ): Promise<{ user: User; stats: PerformanceMetrics }> => {
    try {
      const response = await apiClient.get(`/social/users/${friendId}/profile`);
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get leaderboard
   */
  getLeaderboard: async (
    period: "daily" | "weekly" | "monthly" | "all_time" = "weekly",
  ): Promise<Leaderboard> => {
    try {
      const response = await apiClient.get<Leaderboard>(
        `/social/leaderboard?period=${period}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Like a friend's activity
   */
  likeActivity: async (activityId: string): Promise<void> => {
    try {
      await apiClient.post(`/social/activities/${activityId}/like`);
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Comment on friend's activity
   */
  commentOnActivity: async (
    activityId: string,
    comment: string,
  ): Promise<void> => {
    try {
      await apiClient.post(`/social/activities/${activityId}/comments`, {
        comment,
      });
    } catch (error) {
      return handleApiError(error);
    }
  },
};

// ============================================================
// Analytics API (AI/ML powered insights)
// ============================================================

export const analyticsAPI = {
  /**
   * Get performance metrics
   */
  getPerformanceMetrics: async (
    userId: string,
    period: "daily" | "weekly" | "monthly" | "yearly" | "all_time" = "all_time",
  ): Promise<PerformanceMetrics> => {
    try {
      const response = await apiClient.get<PerformanceMetrics>(
        `/analytics/${userId}/performance?period=${period}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get AI-powered insights and recommendations
   */
  getInsights: async (userId: string): Promise<AIInsight[]> => {
    try {
      const response = await apiClient.get<AIInsight[]>(
        `/analytics/${userId}/insights`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get portfolio analysis with RAG
   */
  analyzePortfolio: async (
    userId: string,
    includeRecommendations: boolean = true,
  ): Promise<{
    totalValue: number;
    riskScore: number;
    diversificationScore: number;
    recommendations: string[];
    analyzedAt: string;
  }> => {
    try {
      const response = await apiClient.post(`/analytics/portfolio`, {
        user_id: userId,
        include_recommendations: includeRecommendations,
      });
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Query market with natural language (RAG-powered)
   */
  queryMarket: async (
    query: string,
    symbols?: string[],
  ): Promise<{
    query: string;
    insights: { symbol: string; insight: string; confidence: number }[];
    sources: string[];
  }> => {
    try {
      const response = await apiClient.post(`/analytics/query`, {
        query,
        symbols,
      });
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get symbol analysis
   */
  getSymbolAnalysis: async (
    symbol: string,
  ): Promise<{
    symbol: string;
    price: number;
    change: number;
    changePercent: number;
    sentiment: string;
    technicalRating: string;
    summary: string;
  }> => {
    try {
      const response = await apiClient.get(`/analytics/symbols/${symbol}`);
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get risk analysis
   */
  getRiskAnalysis: async (
    userId: string,
  ): Promise<{
    overallRisk: number;
    concentrationRisk: number;
    volatilityRisk: number;
    recommendations: string[];
  }> => {
    try {
      const response = await apiClient.get(`/analytics/${userId}/risk`);
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },
};

// ============================================================
// Market Data API
// ============================================================

export const marketDataAPI = {
  /**
   * Get real-time quote
   */
  getQuote: async (symbol: string): Promise<MarketData> => {
    try {
      const response = await apiClient.get<MarketData>(
        `/market/quote/${symbol}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get multiple quotes
   */
  getQuotes: async (symbols: string[]): Promise<MarketData[]> => {
    try {
      const response = await apiClient.post<MarketData[]>(`/market/quotes`, {
        symbols,
      });
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get historical data
   */
  getHistoricalData: async (
    symbol: string,
    params: {
      startDate?: string;
      endDate?: string;
      interval?: "1min" | "5min" | "15min" | "1hour" | "1day";
    },
  ): Promise<
    {
      date: string;
      open: number;
      high: number;
      low: number;
      close: number;
      volume: number;
    }[]
  > => {
    try {
      const queryString = buildQueryString(params);
      const response = await apiClient.get(
        `/market/history/${symbol}?${queryString}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Search symbols
   */
  searchSymbols: async (
    query: string,
  ): Promise<
    {
      symbol: string;
      name: string;
      type: string;
      exchange: string;
    }[]
  > => {
    try {
      const response = await apiClient.get(
        `/market/search?q=${encodeURIComponent(query)}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },
};

// ============================================================
// Notifications API
// ============================================================

export const notificationsAPI = {
  /**
   * Get user notifications
   */
  getNotifications: async (
    userId: string,
    params?: PaginationParams & { unreadOnly?: boolean },
  ): Promise<PaginatedResponse<Notification>> => {
    try {
      const queryString = buildQueryString(params || {});
      const response = await apiClient.get<PaginatedResponse<Notification>>(
        `/notifications/${userId}?${queryString}`,
      );
      return response.data;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Mark notification as read
   */
  markAsRead: async (notificationId: string): Promise<void> => {
    try {
      await apiClient.patch(`/notifications/${notificationId}/read`);
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Mark all notifications as read
   */
  markAllAsRead: async (userId: string): Promise<void> => {
    try {
      await apiClient.post(`/notifications/${userId}/read-all`);
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Delete notification
   */
  deleteNotification: async (notificationId: string): Promise<void> => {
    try {
      await apiClient.delete(`/notifications/${notificationId}`);
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Get unread count
   */
  getUnreadCount: async (userId: string): Promise<number> => {
    try {
      const response = await apiClient.get<{ count: number }>(
        `/notifications/${userId}/unread-count`,
      );
      return response.data.count;
    } catch (error) {
      return handleApiError(error);
    }
  },

  /**
   * Subscribe to push notifications
   */
  subscribeToPush: async (
    userId: string,
    subscription: PushSubscription,
  ): Promise<void> => {
    try {
      await apiClient.post(`/notifications/${userId}/subscribe`, subscription);
    } catch (error) {
      return handleApiError(error);
    }
  },
};

// ============================================================
// Export API client for custom requests
// ============================================================

export default apiClient;
