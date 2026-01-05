export type User = {
  id: string | number;
  email: string;
  username: string;
  created_at: Date;
  updated_at: Date;
};

export type Portfolio = {
  id: string | number;
  name: string;
  created_at: Date;
};

export type Holding = {
  id: string | number;
  symbol: string;
  quantity: number;
  averageCost: number;
};

export type Order = {
  id: string | number;
  symbol: string;
  side: string;
  type: "Market" | "Limit";
  quantity: number;
  price: number;
  status: string;
  created_at: Date;
  filled_at: Date;
};
