export default function Leaderboard() {
  type Trader = {
    rank: number;
    name: string;
    loss?: string;
    profit?: string;
    account: string;
    size: string;
    country: string;
  };
  const topTraders: Trader[] = [
    {
      rank: 1,
      name: "Tan S.",
      profit: "$66,262.25",
      account: "ETRACK",
      size: "$288,262.25",
      country: "SG",
    },
    {
      rank: 2,
      name: "Aashish G.",
      profit: "$49,056.00",
      account: "EGONE",
      size: "$300,000.00",
      country: "IN",
    },
    {
      rank: 3,
      name: "Nathan H.",
      profit: "$48,596.34",
      account: "EINFTURES1",
      size: "$100,000.00",
      country: "UK",
    },
    {
      rank: 4,
      name: "Artur Z.",
      loss: "-$40,635.53",
      account: "EGONE",
      size: "$300,000.00",
      country: "PL",
    },
  ];

  const traders = [
    {
      name: "James T.",
      time: "14 min. 36 sec",
      image: "/trader1.jpg",
      accountSize: "US$5,000.00 E8",
      country: "US",
    },
    {
      name: "Sanjeet T.",
      time: "13 min. 55 sec",
      image: "/trader2.jpg",
      accountSize: "US$25,000.00 E8",
      country: "IN",
    },
    {
      name: "Roger B.",
      time: "20 min. 2 sec",
      image: "/trader3.jpg",
      accountSize: "US$50,000.00 E8",
      country: "AU",
    },
  ];

  return (
    <div className="p-6 space-y-6 overflow-y-auto h-full">
      <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-6">
        {/* Header */}
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center gap-2">
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
                d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"
              />
            </svg>
            <h2 className="text-white font-semibold text-lg">Leaderboard</h2>
          </div>
          <div className="flex items-center gap-2">
            <button className="px-4 py-2 bg-emerald-500/20 text-emerald-400 rounded-lg text-sm border border-emerald-500/30">
              Global Leaderboard
            </button>
            <button className="px-4 py-2 text-white/60 hover:bg-white/5 rounded-lg text-sm">
              Analysis of Volume
            </button>
          </div>
        </div>

        {/* Highest Payout Section */}
        <div className="mb-8">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2">
              <svg
                className="w-4 h-4 text-yellow-400"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path
                  fillRule="evenodd"
                  d="M10 18a8 8 0 100-16 8 8 0 000 16zM9.555 7.168A1 1 0 008 8v4a1 1 0 001.555.832l3-2a1 1 0 000-1.664l-3-2z"
                  clipRule="evenodd"
                />
              </svg>
              <h3 className="text-white font-medium">Highest Payout</h3>
            </div>
            <div className="flex items-center gap-2">
              <span className="text-white/40 text-sm">In past</span>
              <select className="bg-white/5 border border-white/10 rounded-lg px-3 py-1.5 text-emerald-400 text-sm">
                <option>30 days</option>
              </select>
            </div>
          </div>

          {/* Top Traders Cards */}
          <div className="grid grid-cols-4 gap-4">
            {topTraders.map((trader, idx) => (
              <div
                key={trader.name}
                className={`bg-white/5 rounded-xl border ${
                  idx === 0 ? "border-yellow-500/30" : "border-white/10"
                } p-4 relative overflow-hidden`}
              >
                {idx === 0 && (
                  <div className="absolute top-0 right-0 w-16 h-16 bg-gradient-to-br from-yellow-500/20 to-transparent rounded-bl-full"></div>
                )}
                <div className="flex items-center gap-2 mb-3">
                  <div
                    className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-bold ${
                      idx === 0
                        ? "bg-yellow-500/20 text-yellow-400"
                        : idx === 1
                          ? "bg-gray-400/20 text-gray-300"
                          : idx === 2
                            ? "bg-amber-600/20 text-amber-500"
                            : "bg-white/10 text-white/60"
                    }`}
                  >
                    {trader.rank}
                  </div>
                  <span className="text-white font-medium">{trader.name}</span>
                  <svg
                    className="w-4 h-4 text-white/40 ml-auto"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M19 9l-7 7-7-7"
                    />
                  </svg>
                </div>
                <div className="mb-3">
                  <div className="text-white/40 text-xs mb-1">Profit</div>
                  {trader.profit ? (
                    <div className="text-emerald-400 text-xl font-bold">
                      {trader.profit}
                    </div>
                  ) : (
                    <div className="text-red-400 text-xl font-bold">
                      {trader.loss}
                    </div>
                  )}
                </div>
                <div className="grid grid-cols-2 gap-2 text-xs">
                  <div>
                    <div className="text-white/40 mb-1">Account</div>
                    <div className="text-white">{trader.account}</div>
                  </div>
                  <div>
                    <div className="text-white/40 mb-1">Size</div>
                    <div className="text-white">{trader.size}</div>
                  </div>
                </div>
                <div className="mt-3 flex items-center gap-2">
                  <div className="text-white/40 text-xs">Country</div>
                  <span className="text-xs px-2 py-0.5 bg-white/10 rounded text-white">
                    {trader.country}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Trader Profiles */}
        <div className="grid grid-cols-3 gap-6">
          {traders.map((trader) => (
            <div
              key={trader.name}
              className="bg-white/5 rounded-xl border border-white/10 overflow-hidden"
            >
              {/* Placeholder Image */}
              <div className="h-48 bg-gradient-to-br from-emerald-900/30 to-black/50 relative">
                <div className="absolute top-3 left-3 flex items-center gap-2 bg-black/50 backdrop-blur-sm rounded-lg px-2 py-1">
                  <div className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></div>
                  <span className="text-white text-xs">{trader.time}</span>
                </div>
                <div className="absolute inset-0 flex items-center justify-center">
                  <div className="w-20 h-20 bg-white/10 rounded-full flex items-center justify-center">
                    <svg
                      className="w-10 h-10 text-white/40"
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={1.5}
                        d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                      />
                    </svg>
                  </div>
                </div>
              </div>
              <div className="p-4">
                <div className="flex items-center justify-between mb-3">
                  <span className="text-white font-medium">{trader.name}</span>
                </div>
                <div className="grid grid-cols-2 gap-4 mb-4 text-sm">
                  <div>
                    <div className="text-white/40 text-xs mb-1">
                      Account Size
                    </div>
                    <div className="text-white">{trader.accountSize}</div>
                  </div>
                  <div>
                    <div className="text-white/40 text-xs mb-1">Country</div>
                    <div className="text-white">{trader.country}</div>
                  </div>
                </div>
                <button className="w-full py-2.5 bg-emerald-500/20 text-emerald-400 rounded-lg text-sm border border-emerald-500/30 hover:bg-emerald-500/30 transition">
                  View Dashboard
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
