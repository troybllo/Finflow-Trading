export default function TradingInstruments() {
  const categories = ["Indices", "Forex", "Crypto", "Energies", "Metals"];
  const symbols = [
    { name: "NDAQ", active: true },
    { name: "SP", active: false },
    { name: "DOW", active: false },
    { name: "DAX", active: false },
    { name: "ASX", active: false },
    { name: "NIKK", active: false },
  ];

  const tradingHours = [
    { day: "Monday", hours: "01:05-23:55", status: "open" },
    { day: "Wednesday", hours: "01:05-23:55", status: "open" },
    { day: "Thursday", hours: "01:05-23:55", status: "open" },
    { day: "Friday", hours: "01:05-23:55", status: "open" },
    { day: "Saturday", hours: "Market is Closed", status: "closed" },
    { day: "Sunday", hours: "Market is Closed", status: "closed" },
  ];

  const info = [
    { label: "Symbol", value: "NSDQ" },
    { label: "Group", value: "Indices" },
    { label: "Leverage", value: "25" },
    { label: "Commission", value: "None" },
    { label: "Contract Size", value: "05" },
    { label: "Digits", value: "02" },
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
                d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z"
              />
            </svg>
            <h2 className="text-white font-semibold text-lg">
              Trading Instruments
            </h2>
          </div>
        </div>

        {/* Category Tabs */}
        <div className="flex items-center gap-2 mb-6">
          {categories.map((cat, idx) => (
            <button
              key={cat}
              className={`px-4 py-2 rounded-lg text-sm transition ${
                idx === 0
                  ? "bg-emerald-500/20 text-emerald-400 border border-emerald-500/30"
                  : "text-white/60 hover:bg-white/5 hover:text-white"
              }`}
            >
              {cat}
            </button>
          ))}
        </div>

        {/* Symbols Row */}
        <div className="flex items-center gap-3 mb-6 pb-6 border-b border-white/10">
          {symbols.map((symbol) => (
            <button
              key={symbol.name}
              className={`flex items-center gap-2 px-3 py-2 rounded-lg text-sm transition ${
                symbol.active
                  ? "bg-emerald-500/20 text-emerald-400"
                  : "text-white/60 hover:bg-white/5"
              }`}
            >
              <div
                className={`w-2 h-2 rounded-full ${symbol.active ? "bg-emerald-400" : "bg-white/20"}`}
              ></div>
              {symbol.name}
            </button>
          ))}
        </div>

        {/* Search */}
        <div className="mb-6">
          <div className="relative">
            <svg
              className="w-4 h-4 text-white/40 absolute left-3 top-1/2 -translate-y-1/2"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
              />
            </svg>
            <input
              type="text"
              placeholder="Search Symbol"
              className="w-full bg-white/5 border border-white/10 rounded-lg pl-10 pr-4 py-2.5 text-white placeholder-white/40 text-sm focus:outline-none focus:border-emerald-500/50"
            />
          </div>
        </div>

        {/* Instrument Details */}
        <div className="bg-white/5 rounded-xl border border-white/10 p-6">
          <div className="flex items-center justify-between mb-6">
            <h3 className="text-white font-semibold text-lg">
              Nasdaq 100 Index
            </h3>
            <div className="flex items-center gap-2">
              <button className="px-3 py-1.5 bg-white/5 border border-white/10 rounded-lg text-white/60 text-sm">
                No Commissions
              </button>
              <button className="px-3 py-1.5 bg-white/5 border border-white/10 rounded-lg text-white/60 text-sm">
                Raw Spreads
              </button>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-6">
            {/* Trading Hours */}
            <div className="bg-white/5 rounded-xl border border-white/10 p-4">
              <div className="flex items-center gap-2 mb-4">
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
                    d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
                  />
                </svg>
                <h4 className="text-white font-medium">Trading Hours</h4>
              </div>
              <div className="space-y-3">
                {tradingHours.map((item) => (
                  <div
                    key={item.day}
                    className="flex items-center justify-between"
                  >
                    <span className="text-white/60 text-sm">{item.day}</span>
                    <span
                      className={`text-sm ${item.status === "open" ? "text-emerald-400" : "text-red-400"}`}
                    >
                      {item.hours}
                    </span>
                  </div>
                ))}
              </div>
              <div className="mt-4 pt-4 border-t border-white/10 flex items-center gap-2 text-xs text-yellow-400">
                <svg
                  className="w-4 h-4"
                  fill="currentColor"
                  viewBox="0 0 20 20"
                >
                  <path
                    fillRule="evenodd"
                    d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z"
                    clipRule="evenodd"
                  />
                </svg>
                All times are in UTC timezone
              </div>
            </div>

            {/* Information */}
            <div className="bg-white/5 rounded-xl border border-white/10 p-4">
              <div className="flex items-center gap-2 mb-4">
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
                    d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                  />
                </svg>
                <h4 className="text-white font-medium">Information</h4>
              </div>
              <div className="space-y-3">
                {info.map((item) => (
                  <div
                    key={item.label}
                    className="flex items-center justify-between"
                  >
                    <span className="text-white/60 text-sm">{item.label}</span>
                    <span className="text-emerald-400 text-sm font-medium">
                      {item.value}
                    </span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
