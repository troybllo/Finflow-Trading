import { useState } from "react";
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  ResponsiveContainer,
  Tooltip,
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
} from "recharts";

export default function Portfolio() {
  const [activeIndex, setActiveIndex] = useState(0);

  // Balance chart data
  const balanceData = [
    { time: "8:00AM", value: 32000 },
    { time: "9:00AM", value: 30000 },
    { time: "9:30AM", value: 33000 },
    { time: "10:00AM", value: 28000 },
    { time: "10:30AM", value: 35000 },
    { time: "11:00AM", value: 38154 },
    { time: "12:00AM", value: 36500 },
    { time: "01:00PM", value: 37200 },
    { time: "02:00PM", value: 38154 },
  ];

  // Asset allocation data
  const allocationData = [
    { name: "Forex", value: 45, color: "#10b981" },
    { name: "Indices", value: 25, color: "#6366f1" },
    { name: "Crypto", value: 15, color: "#f59e0b" },
    { name: "Commodities", value: 15, color: "#ef4444" },
  ];

  // P&L Distribution data
  const pnlData = [
    { name: "Mon", profit: 1200, loss: -400 },
    { name: "Tue", profit: 800, loss: -600 },
    { name: "Wed", profit: 1500, loss: -200 },
    { name: "Thu", profit: 600, loss: -900 },
    { name: "Fri", profit: 2000, loss: -300 },
  ];

  const timeFilters = ["1m", "15m", "1h", "4h", "1d", "1w"];

  return (
    <div className="p-6 space-y-6 overflow-y-auto h-full">
      {/* Welcome Header */}
      <h2 className="text-white font-bold text-2xl">Welcome, Bradley Mac</h2>

      {/* Top Section: Balance Chart + Account Loss Analysis */}
      <div className="grid grid-cols-3 gap-6">
        {/* Balance Chart - Takes 2 columns - Black Frost Glass */}
        <div className="col-span-2 bg-black/40 backdrop-blur-xl rounded-2xl border border-white/10 p-6 shadow-2xl">
          <div className="flex items-center justify-between mb-6">
            <div className="flex items-center gap-3">
              <svg
                className="w-5 h-5 text-emerald-400"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path d="M4 4a2 2 0 00-2 2v1h16V6a2 2 0 00-2-2H4z" />
                <path
                  fillRule="evenodd"
                  d="M18 9H2v5a2 2 0 002 2h12a2 2 0 002-2V9zM4 13a1 1 0 011-1h1a1 1 0 110 2H5a1 1 0 01-1-1zm5-1a1 1 0 100 2h1a1 1 0 100-2H9z"
                  clipRule="evenodd"
                />
              </svg>
              <span className="text-white/60 text-sm">Balance</span>
              <span className="text-white text-2xl font-bold">$38,154</span>
              <span className="text-white/40 text-sm">USD</span>
            </div>
            <div className="flex items-center gap-2">
              {timeFilters.map((filter) => (
                <button
                  key={filter}
                  className={`px-3 py-1.5 rounded-lg text-xs transition ${
                    filter === "1d"
                      ? "bg-emerald-500/20 text-emerald-400 border border-emerald-500/30"
                      : "text-white/40 hover:text-white hover:bg-white/5"
                  }`}
                >
                  {filter}
                </button>
              ))}
              <button className="px-3 py-1.5 bg-white/5 rounded-lg text-white/60 text-xs border border-white/10 ml-2">
                7 Days
              </button>
            </div>
          </div>

          {/* Chart */}
          <div className="h-56 relative">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={balanceData}>
                <defs>
                  <linearGradient
                    id="balanceGradient"
                    x1="0"
                    y1="0"
                    x2="0"
                    y2="1"
                  >
                    <stop offset="5%" stopColor="#10b981" stopOpacity={0.3} />
                    <stop offset="95%" stopColor="#10b981" stopOpacity={0} />
                  </linearGradient>
                </defs>
                <XAxis
                  dataKey="time"
                  axisLine={false}
                  tickLine={false}
                  tick={{ fill: "rgba(255,255,255,0.4)", fontSize: 10 }}
                />
                <YAxis
                  axisLine={false}
                  tickLine={false}
                  tick={{ fill: "rgba(255,255,255,0.4)", fontSize: 10 }}
                  tickFormatter={(value) => `${value / 1000}k`}
                  domain={["dataMin - 2000", "dataMax + 2000"]}
                />
                <Tooltip
                  contentStyle={{
                    backgroundColor: "rgba(0,0,0,0.9)",
                    border: "1px solid rgba(255,255,255,0.1)",
                    borderRadius: "8px",
                    padding: "8px 12px",
                  }}
                  labelStyle={{ color: "rgba(255,255,255,0.6)", fontSize: 11 }}
                  itemStyle={{ color: "#10b981", fontSize: 13 }}
                  formatter={(value: number | undefined) => [
                    `$${(value ?? 0).toLocaleString()}`,
                    "Balance",
                  ]}
                />
                <Area
                  type="monotone"
                  dataKey="value"
                  stroke="#10b981"
                  strokeWidth={2}
                  fillOpacity={1}
                  fill="url(#balanceGradient)"
                />
              </AreaChart>
            </ResponsiveContainer>

            {/* Tooltip indicator on chart */}
            <div className="absolute top-8 left-1/2 transform -translate-x-1/2 bg-black/80 backdrop-blur-md border border-white/20 rounded-lg px-3 py-1.5">
              <div className="text-emerald-400 text-sm font-semibold">
                $38,154 <span className="text-white/40 text-xs">USD</span>
              </div>
              <div className="text-white/40 text-xs">8 Apr 2025</div>
            </div>
          </div>
        </div>

        {/* Account Loss Analysis - Black Frost Glass */}
        <div className="bg-black/40 backdrop-blur-xl rounded-2xl border border-white/10 p-6 shadow-2xl">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-white font-medium">Account Loss Analysis</h3>
            <div className="flex items-center gap-3 text-xs">
              <div className="flex items-center gap-1.5">
                <div className="w-2 h-2 rounded-full bg-emerald-400"></div>
                <span className="text-white/40">Max Recorded</span>
              </div>
              <div className="flex items-center gap-1.5">
                <div className="w-2 h-2 rounded-full bg-purple-400"></div>
                <span className="text-white/40">Current</span>
              </div>
            </div>
          </div>

          <div className="space-y-5">
            {/* Initial Deposit Limit Level */}
            <div>
              <div className="flex items-center justify-between mb-2">
                <span className="text-white/80 text-sm">
                  Initial Deposit Limit Level
                </span>
              </div>
              <div className="h-3 bg-white/5 rounded-full overflow-hidden">
                <div className="h-full w-[85%] bg-gradient-to-r from-red-600 to-red-400 rounded-full relative">
                  <div className="absolute right-0 top-1/2 -translate-y-1/2 w-1 h-full bg-white/50"></div>
                </div>
              </div>
              <div className="flex justify-between mt-2 text-xs">
                <span className="text-white/40">
                  Initial Deposit: $25,000.00
                </span>
                <span className="text-white/40">Loss Limit: $250,000.00</span>
              </div>
            </div>

            {/* Daily Loss Limit Level */}
            <div>
              <div className="flex items-center justify-between mb-2">
                <span className="text-white/80 text-sm">
                  Daily Loss Limit Level
                </span>
              </div>
              <div className="h-3 bg-white/5 rounded-full overflow-hidden">
                <div className="h-full w-[45%] bg-gradient-to-r from-emerald-600 to-emerald-400 rounded-full"></div>
              </div>
              <div className="flex justify-between mt-2 text-xs">
                <span className="text-white/40">Entry Equity: $25,000.00</span>
                <span className="text-white/40">Loss Limit: $207,000.00</span>
              </div>
            </div>

            {/* Next Reset Timer */}
            <div className="pt-4 border-t border-white/10">
              <div className="flex items-center justify-between">
                <span className="text-white/60 text-sm">
                  Next Daily Loss Reset in
                </span>
                <span className="text-white font-mono text-lg">12:36:36</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Stats Cards Row */}
      <div className="grid grid-cols-4 gap-4">
        <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-5">
          <div className="flex items-center gap-2 mb-3">
            <div className="w-8 h-8 bg-emerald-500/20 rounded-lg flex items-center justify-center">
              <svg
                className="w-4 h-4 text-emerald-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"
                />
              </svg>
            </div>
            <span className="text-white/60 text-sm">Average Win</span>
          </div>
          <div className="text-white text-2xl font-bold">$987.47</div>
        </div>

        <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-5">
          <div className="flex items-center gap-2 mb-3">
            <div className="w-8 h-8 bg-red-500/20 rounded-lg flex items-center justify-center">
              <svg
                className="w-4 h-4 text-red-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M13 17h8m0 0V9m0 8l-8-8-4 4-6-6"
                />
              </svg>
            </div>
            <span className="text-white/60 text-sm">Average Loss</span>
          </div>
          <div className="text-red-400 text-2xl font-bold">-$781.70</div>
        </div>

        <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-5">
          <div className="flex items-center gap-2 mb-3">
            <div className="w-8 h-8 bg-blue-500/20 rounded-lg flex items-center justify-center">
              <svg
                className="w-4 h-4 text-blue-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"
                />
              </svg>
            </div>
            <span className="text-white/60 text-sm">Win Ratio</span>
          </div>
          <div className="text-white text-2xl font-bold">66%</div>
        </div>

        <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-5">
          <div className="flex items-center gap-2 mb-3">
            <div className="w-8 h-8 bg-yellow-500/20 rounded-lg flex items-center justify-center">
              <svg
                className="w-4 h-4 text-yellow-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M3 6l3 1m0 0l-3 9a5.002 5.002 0 006.001 0M6 7l3 9M6 7l6-2m6 2l3-1m-3 1l-3 9a5.002 5.002 0 006.001 0M18 7l3 9m-3-9l-6-2m0-2v2m0 16V5m0 16H9m3 0h3"
                />
              </svg>
            </div>
            <span className="text-white/60 text-sm">Risk Reward</span>
          </div>
          <div className="text-white text-2xl font-bold">66%</div>
        </div>
      </div>

      {/* Goal Overview - Black Frost Glass */}
      <div className="bg-black/40 backdrop-blur-xl rounded-2xl border border-white/10 p-6 shadow-2xl">
        <div className="flex items-center gap-2 mb-5">
          <svg
            className="w-5 h-5 text-emerald-400"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z"
            />
          </svg>
          <h3 className="text-white font-medium">Goal Overview</h3>
        </div>

        <div className="grid grid-cols-3 gap-4">
          {/* Minimum Trading Days */}
          <div className="bg-white/5 rounded-xl border border-white/10 p-4">
            <div className="flex items-center justify-between mb-4">
              <div className="flex items-center gap-2">
                <div className="w-6 h-6 bg-emerald-500/20 rounded-lg flex items-center justify-center">
                  <svg
                    className="w-3 h-3 text-emerald-400"
                    fill="currentColor"
                    viewBox="0 0 20 20"
                  >
                    <path
                      fillRule="evenodd"
                      d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                      clipRule="evenodd"
                    />
                  </svg>
                </div>
                <span className="text-white text-sm">Minimum Trading Days</span>
              </div>
              <span className="text-emerald-400 text-xs px-2 py-1 bg-emerald-500/20 rounded border border-emerald-500/30">
                Passes
              </span>
            </div>
            <div className="grid grid-cols-2 gap-4 text-center">
              <div>
                <div className="text-white/40 text-xs mb-1">Minimum Result</div>
                <div className="text-white font-bold">1 Days</div>
              </div>
              <div>
                <div className="text-white/40 text-xs mb-1">Current Result</div>
                <div className="text-emerald-400 font-bold">1 Days</div>
              </div>
            </div>
          </div>

          {/* Profit Target */}
          <div className="bg-white/5 rounded-xl border border-white/10 p-4">
            <div className="flex items-center justify-between mb-4">
              <div className="flex items-center gap-2">
                <div className="w-6 h-6 bg-emerald-500/20 rounded-lg flex items-center justify-center">
                  <svg
                    className="w-3 h-3 text-emerald-400"
                    fill="currentColor"
                    viewBox="0 0 20 20"
                  >
                    <path
                      fillRule="evenodd"
                      d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                      clipRule="evenodd"
                    />
                  </svg>
                </div>
                <span className="text-white text-sm">Profit Target</span>
              </div>
              <span className="text-emerald-400 text-xs px-2 py-1 bg-emerald-500/20 rounded border border-emerald-500/30">
                Passes
              </span>
            </div>
            <div className="grid grid-cols-2 gap-4 text-center">
              <div>
                <div className="text-white/40 text-xs mb-1">Minimum Result</div>
                <div className="text-white font-bold">US$400.00</div>
              </div>
              <div>
                <div className="text-white/40 text-xs mb-1">Current Result</div>
                <div className="text-emerald-400 font-bold">US$411.18</div>
              </div>
            </div>
          </div>

          {/* Initial Balance Loss */}
          <div className="bg-white/5 rounded-xl border border-white/10 p-4">
            <div className="flex items-center justify-between mb-4">
              <div className="flex items-center gap-2">
                <div className="w-6 h-6 bg-emerald-500/20 rounded-lg flex items-center justify-center">
                  <svg
                    className="w-3 h-3 text-emerald-400"
                    fill="currentColor"
                    viewBox="0 0 20 20"
                  >
                    <path
                      fillRule="evenodd"
                      d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                      clipRule="evenodd"
                    />
                  </svg>
                </div>
                <span className="text-white text-sm">Initial Balance Loss</span>
              </div>
              <span className="text-emerald-400 text-xs px-2 py-1 bg-emerald-500/20 rounded border border-emerald-500/30">
                Passes
              </span>
            </div>
            <div className="grid grid-cols-2 gap-4 text-center">
              <div>
                <div className="text-white/40 text-xs mb-1">Minimum Result</div>
                <div className="text-white font-bold">US$400.00</div>
              </div>
              <div>
                <div className="text-white/40 text-xs mb-1">Current Result</div>
                <div className="text-emerald-400 font-bold">US$0.00</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Visualizations Row */}
      <div className="grid grid-cols-2 gap-6">
        {/* Asset Allocation Pie Chart - Black Frost Glass */}
        <div className="bg-black/40 backdrop-blur-xl rounded-2xl border border-white/10 p-6 shadow-2xl">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-white font-medium">Asset Allocation</h3>
            <div className="flex items-center gap-2">
              <button className="px-3 py-1.5 bg-emerald-500/20 text-emerald-400 rounded-lg text-xs border border-emerald-500/30">
                Portfolio
              </button>
              <button className="px-3 py-1.5 text-white/40 hover:bg-white/5 rounded-lg text-xs transition">
                Watchlist
              </button>
            </div>
          </div>
          <div className="flex items-center gap-6">
            <div className="w-56 h-56 relative">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={allocationData}
                    cx="50%"
                    cy="50%"
                    innerRadius={55}
                    outerRadius={75}
                    paddingAngle={3}
                    dataKey="value"
                    onMouseEnter={(_, index) => setActiveIndex(index)}
                    style={{ cursor: "pointer" }}
                  >
                    {allocationData.map((entry, index) => (
                      <Cell
                        key={`cell-${index}`}
                        fill={entry.color}
                        stroke={
                          index === activeIndex ? entry.color : "transparent"
                        }
                        strokeWidth={index === activeIndex ? 3 : 0}
                        style={{
                          filter:
                            index === activeIndex
                              ? `brightness(1.2) drop-shadow(0 0 8px ${entry.color})`
                              : "brightness(0.7)",
                          transition: "all 0.3s ease",
                        }}
                      />
                    ))}
                  </Pie>
                  <Tooltip
                    contentStyle={{
                      backgroundColor: "rgba(0,0,0,0.95)",
                      border: "1px solid rgba(255,255,255,0.2)",
                      borderRadius: "12px",
                      padding: "12px 16px",
                      boxShadow: "0 8px 32px rgba(0,0,0,0.4)",
                    }}
                    formatter={(value: number | undefined) => [
                      `${value ?? 0}%`,
                      "Allocation",
                    ]}
                    labelStyle={{
                      color: "#fff",
                      fontWeight: "bold",
                      marginBottom: 4,
                    }}
                  />
                </PieChart>
              </ResponsiveContainer>
              {/* Center text showing active item */}
              <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
                <div className="text-center">
                  <div className="text-white/60 text-xs">
                    {allocationData[activeIndex]?.name}
                  </div>
                  <div className="text-emerald-400 text-xl font-bold">
                    {allocationData[activeIndex]?.value}%
                  </div>
                </div>
              </div>
            </div>
            <div className="flex-1 space-y-3">
              {allocationData.map((item, index) => (
                <div
                  key={item.name}
                  className={`flex items-center justify-between p-2 rounded-lg transition-all cursor-pointer ${
                    index === activeIndex
                      ? "bg-white/10 border border-white/20"
                      : "hover:bg-white/5"
                  }`}
                  onMouseEnter={() => setActiveIndex(index)}
                >
                  <div className="flex items-center gap-2">
                    <div
                      className="w-3 h-3 rounded-full transition-transform"
                      style={{
                        backgroundColor: item.color,
                        transform:
                          index === activeIndex ? "scale(1.3)" : "scale(1)",
                        boxShadow:
                          index === activeIndex
                            ? `0 0 8px ${item.color}`
                            : "none",
                      }}
                    ></div>
                    <span
                      className={`text-sm transition-colors ${
                        index === activeIndex ? "text-white" : "text-white/60"
                      }`}
                    >
                      {item.name}
                    </span>
                  </div>
                  <span
                    className={`font-medium transition-colors ${
                      index === activeIndex ? "text-emerald-400" : "text-white"
                    }`}
                  >
                    {item.value}%
                  </span>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* P&L Distribution - Black Frost Glass */}
        <div className="bg-black/40 backdrop-blur-xl rounded-2xl border border-white/10 p-6 shadow-2xl">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-white font-medium">P&L Distribution</h3>
            <div className="flex items-center gap-3 text-xs">
              <div className="flex items-center gap-1.5">
                <div className="w-2 h-2 rounded-full bg-emerald-400"></div>
                <span className="text-white/40">Profit</span>
              </div>
              <div className="flex items-center gap-1.5">
                <div className="w-2 h-2 rounded-full bg-red-400"></div>
                <span className="text-white/40">Loss</span>
              </div>
            </div>
          </div>
          <div className="h-44">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={pnlData} barGap={2}>
                <XAxis
                  dataKey="name"
                  axisLine={false}
                  tickLine={false}
                  tick={{ fill: "rgba(255,255,255,0.4)", fontSize: 11 }}
                />
                <YAxis
                  axisLine={false}
                  tickLine={false}
                  tick={{ fill: "rgba(255,255,255,0.4)", fontSize: 10 }}
                  tickFormatter={(value) => `$${Math.abs(value / 1000)}k`}
                />
                <Tooltip
                  contentStyle={{
                    backgroundColor: "rgba(0,0,0,0.9)",
                    border: "1px solid rgba(255,255,255,0.1)",
                    borderRadius: "8px",
                  }}
                  labelStyle={{ color: "rgba(255,255,255,0.6)" }}
                />
                <Bar dataKey="profit" fill="#10b981" radius={[4, 4, 0, 0]} />
                <Bar dataKey="loss" fill="#ef4444" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      {/* Recent Activity */}
      <div>
        <h3 className="text-white font-medium text-lg mb-4">Recent Activity</h3>
        <div className="grid grid-cols-4 gap-4">
          <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-5">
            <div className="flex items-center gap-2 mb-3">
              <div className="w-8 h-8 bg-blue-500/20 rounded-lg flex items-center justify-center">
                <svg
                  className="w-4 h-4 text-blue-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"
                  />
                </svg>
              </div>
              <span className="text-white/60 text-sm">Trade History</span>
            </div>
            <div className="text-white text-xl font-bold">127</div>
            <div className="text-white/40 text-xs mt-1">
              Total trades this month
            </div>
          </div>

          <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-5">
            <div className="flex items-center gap-2 mb-3">
              <div className="w-8 h-8 bg-emerald-500/20 rounded-lg flex items-center justify-center">
                <svg
                  className="w-4 h-4 text-emerald-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                  />
                </svg>
              </div>
              <span className="text-white/60 text-sm">Account Funding</span>
            </div>
            <div className="text-emerald-400 text-xl font-bold">+$5,000</div>
            <div className="text-white/40 text-xs mt-1">Last deposit</div>
          </div>

          <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-5">
            <div className="flex items-center gap-2 mb-3">
              <div className="w-8 h-8 bg-purple-500/20 rounded-lg flex items-center justify-center">
                <svg
                  className="w-4 h-4 text-purple-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z"
                  />
                </svg>
              </div>
              <span className="text-white/60 text-sm">Passive Income</span>
            </div>
            <div className="text-white text-xl font-bold">$234.50</div>
            <div className="text-white/40 text-xs mt-1">
              Dividends this month
            </div>
          </div>

          <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-5">
            <div className="flex items-center gap-2 mb-3">
              <div className="w-8 h-8 bg-red-500/20 rounded-lg flex items-center justify-center">
                <svg
                  className="w-4 h-4 text-red-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M9 14l6-6m-5.5.5h.01m4.99 5h.01M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16l3.5-2 3.5 2 3.5-2 3.5 2z"
                  />
                </svg>
              </div>
              <span className="text-white/60 text-sm">Trading Costs</span>
            </div>
            <div className="text-red-400 text-xl font-bold">-$89.20</div>
            <div className="text-white/40 text-xs mt-1">Fees this month</div>
          </div>
        </div>
      </div>

      {/* Risk Management */}
      <div>
        <h3 className="text-white font-medium text-lg mb-4">Risk Management</h3>
        <div className="grid grid-cols-4 gap-4">
          <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-5">
            <div className="flex items-center gap-2 mb-3">
              <span className="text-white/60 text-sm">Margin Used</span>
            </div>
            <div className="text-white text-xl font-bold">$12,450</div>
            <div className="w-full bg-white/10 rounded-full h-2 mt-3">
              <div
                className="bg-yellow-400 h-2 rounded-full"
                style={{ width: "45%" }}
              ></div>
            </div>
            <div className="text-white/40 text-xs mt-2">45% of available</div>
          </div>

          <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-5">
            <div className="flex items-center gap-2 mb-3">
              <span className="text-white/60 text-sm">Margin Available</span>
            </div>
            <div className="text-emerald-400 text-xl font-bold">$15,230</div>
            <div className="w-full bg-white/10 rounded-full h-2 mt-3">
              <div
                className="bg-emerald-400 h-2 rounded-full"
                style={{ width: "55%" }}
              ></div>
            </div>
            <div className="text-white/40 text-xs mt-2">55% remaining</div>
          </div>

          <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-5">
            <div className="flex items-center gap-2 mb-3">
              <span className="text-white/60 text-sm">Exposure by Asset</span>
            </div>
            <div className="text-white text-xl font-bold">Forex</div>
            <div className="text-white/40 text-xs mt-2">
              Highest exposure: 45%
            </div>
          </div>

          <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-5">
            <div className="flex items-center gap-2 mb-3">
              <span className="text-white/60 text-sm">Risk Level</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="text-yellow-400 text-xl font-bold">Medium</div>
              <div className="w-3 h-3 rounded-full bg-yellow-400"></div>
            </div>
            <div className="text-white/40 text-xs mt-2">
              Within safe parameters
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
