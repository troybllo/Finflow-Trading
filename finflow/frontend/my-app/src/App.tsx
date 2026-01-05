import "./App.css";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  ResponsiveContainer,
  Cell,
} from "recharts";
import Header from "./components/layout/Header";

function App() {
  // Placeholder chart data structure (no real values)
  const chartData = [
    { name: "01 W", value: 0 },
    { name: "02 W", value: 0 },
    { name: "03 W", value: 0 },
    { name: "04 W", value: 0 },
    { name: "05 W", value: 0 },
    { name: "06 W", value: 0 },
  ];

  return (
    <div className="h-screen w-screen overflow-hidden bg-gradient-to-br from-black via-purple-950 to-black">
      <Header />

      {/* Main Content */}
      <div className="p-8 h-[calc(100vh-73px)] flex flex-col gap-6">
        {/* Balance and Trading Pairs Card */}
        <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 overflow-hidden flex-1">
          <div className="grid grid-cols-2 divide-x divide-white/10 h-full">
            {/* Left: Chart Section */}
            <div className="p-6 flex flex-col">
              <div className="flex items-center justify-between mb-6">
                <div>
                  <div className="flex items-center gap-2 text-white/60 text-sm mb-1">
                    <svg
                      className="w-4 h-4"
                      fill="currentColor"
                      viewBox="0 0 20 20"
                    >
                      <path d="M8.433 7.418c.155-.103.346-.196.567-.267v1.698a2.305 2.305 0 01-.567-.267C8.07 8.34 8 8.114 8 8c0-.114.07-.34.433-.582zM11 12.849v-1.698c.22.071.412.164.567.267.364.243.433.468.433.582 0 .114-.07.34-.433.582a2.305 2.305 0 01-.567.267z" />
                      <path
                        fillRule="evenodd"
                        d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-13a1 1 0 10-2 0v.092a4.535 4.535 0 00-1.676.662C6.602 6.234 6 7.009 6 8c0 .99.602 1.765 1.324 2.246.48.32 1.054.545 1.676.662v1.941c-.391-.127-.68-.317-.843-.504a1 1 0 10-1.51 1.31c.562.649 1.413 1.076 2.353 1.253V15a1 1 0 102 0v-.092a4.535 4.535 0 001.676-.662C13.398 13.766 14 12.991 14 12c0-.99-.602-1.765-1.324-2.246A4.535 4.535 0 0011 9.092V7.151c.391.127.68.317.843.504a1 1 0 101.511-1.31c-.563-.649-1.413-1.076-2.354-1.253V5z"
                        clipRule="evenodd"
                      />
                    </svg>
                    <span>Total Balance</span>
                  </div>
                  <div className="text-white text-3xl font-semibold">$--</div>
                </div>
                <select className="bg-white/5 border border-white/10 rounded-lg px-3 py-1.5 text-white text-sm">
                  <option>Weekly</option>
                </select>
              </div>

              {/* Chart Area */}
              <div className="flex-1 min-h-0">
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={chartData} barCategoryGap="20%">
                    <XAxis
                      dataKey="name"
                      axisLine={false}
                      tickLine={false}
                      tick={{ fill: "rgba(255,255,255,0.4)", fontSize: 12 }}
                    />
                    <YAxis hide />
                    <Bar
                      dataKey="value"
                      radius={[8, 8, 0, 0]}
                      minPointSize={60}
                    >
                      {chartData.map((_, index) => (
                        <Cell
                          key={`cell-${index}`}
                          fill={
                            index === 3
                              ? "url(#purpleGradient)"
                              : "rgba(255,255,255,0.05)"
                          }
                        />
                      ))}
                    </Bar>
                    <defs>
                      <linearGradient
                        id="purpleGradient"
                        x1="0"
                        y1="1"
                        x2="0"
                        y2="0"
                      >
                        <stop offset="0%" stopColor="#7c3aed" />
                        <stop offset="100%" stopColor="#a78bfa" />
                      </linearGradient>
                    </defs>
                  </BarChart>
                </ResponsiveContainer>
              </div>

              <div className="mt-4 space-y-1">
                <div className="text-white text-xl font-semibold">--%</div>
                <div className="text-emerald-400 text-sm">
                  Compared to last week
                </div>
                <div className="text-white/40 text-xs">
                  Higher than last week $
                </div>
              </div>
            </div>

            {/* Right: Trading Pairs */}
            <div className="p-6 flex flex-col">
              <div className="flex items-center justify-between mb-4">
                <div className="flex gap-2 text-sm">
                  <button className="px-3 py-1.5 bg-purple-600 text-white rounded-lg">
                    Symbol
                  </button>
                  <button className="px-3 py-1.5 text-white/60 hover:text-white">
                    Bid
                  </button>
                  <button className="px-3 py-1.5 text-white/60 hover:text-white">
                    Ask
                  </button>
                  <button className="px-3 py-1.5 text-white/60 hover:text-white">
                    Daily Deals...
                  </button>
                </div>
              </div>

              {/* Trading pairs header */}
              <div className="grid grid-cols-4 gap-4 py-2 px-2 text-white/40 text-xs border-b border-white/10">
                <span>Symbol</span>
                <span>Bid</span>
                <span>Ask</span>
                <span>Change</span>
              </div>

              {/* Empty trading pairs list */}
              <div className="flex-1 overflow-y-auto">
                {[...Array(8)].map((_, idx) => (
                  <div
                    key={idx}
                    className="grid grid-cols-4 gap-4 py-3 hover:bg-white/5 rounded-lg px-2 transition border-b border-white/5"
                  >
                    <div className="flex items-center gap-2">
                      <div className="w-1.5 h-1.5 rounded-full bg-white/20"></div>
                      <span className="text-white/30 text-sm">--/--</span>
                    </div>
                    <span className="text-white/30 text-sm">--</span>
                    <span className="text-white/30 text-sm">--</span>
                    <span className="text-white/30 text-sm">--%</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>

        {/* Transactions Table */}
        <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 overflow-hidden h-64">
          <div className="overflow-x-auto h-full">
            <table className="w-full">
              <thead>
                <tr className="border-b border-white/10">
                  <th className="text-left text-white/60 text-sm font-medium px-6 py-4">
                    Symbol
                  </th>
                  <th className="text-left text-white/60 text-sm font-medium px-6 py-4">
                    Ticket
                  </th>
                  <th className="text-left text-white/60 text-sm font-medium px-6 py-4">
                    Time
                  </th>
                  <th className="text-left text-white/60 text-sm font-medium px-6 py-4">
                    Type
                  </th>
                  <th className="text-left text-white/60 text-sm font-medium px-6 py-4">
                    Volume
                  </th>
                  <th className="text-left text-white/60 text-sm font-medium px-6 py-4">
                    Price
                  </th>
                  <th className="text-left text-white/60 text-sm font-medium px-6 py-4">
                    T/P Price
                  </th>
                  <th className="text-left text-white/60 text-sm font-medium px-6 py-4">
                    Swap
                  </th>
                  <th className="text-left text-white/60 text-sm font-medium px-6 py-4">
                    Profit
                  </th>
                </tr>
              </thead>
              <tbody>
                {[...Array(3)].map((_, idx) => (
                  <tr
                    key={idx}
                    className="border-b border-white/5 hover:bg-white/5 transition"
                  >
                    <td className="px-6 py-4 text-white/30 text-sm">--/--</td>
                    <td className="px-6 py-4 text-white/30 text-sm">
                      --------
                    </td>
                    <td className="px-6 py-4 text-white/30 text-sm">
                      --:-- --:--:--
                    </td>
                    <td className="px-6 py-4 text-white/30 text-sm">--</td>
                    <td className="px-6 py-4 text-white/30 text-sm">--</td>
                    <td className="px-6 py-4 text-white/30 text-sm">--</td>
                    <td className="px-6 py-4 text-white/30 text-sm">--</td>
                    <td className="px-6 py-4 text-white/30 text-sm">--</td>
                    <td className="px-6 py-4 text-white/30 text-sm">--</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
