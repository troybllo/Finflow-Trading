export default function EconomicCalendar() {
  const currencies = ["GBP", "EUR", "CHF", "CAD", "USD", "AUD", "All"];
  const impacts = ["Low", "Medium", "High", "Holiday"];

  const events = [
    { event: "Retail Sales m/m", impact: "High", currency: "GBP", date: "25/04/2025 12:00:00", forecast: "-0.4%", previous: "1.0%" },
    { event: "Italian Bank Holiday", impact: "Holiday", currency: "EUR", date: "25/04/2025 12:03:00", forecast: "0", previous: "0" },
    { event: "SNB Chairman Schlegel Speaks", impact: "Medium", currency: "CHF", date: "25/04/2025 14:00:00", forecast: "0", previous: "0" },
    { event: "Core Retail Sales m/m", impact: "High", currency: "CAD", date: "25/04/2025 18:30:00", forecast: "0.2%", previous: "0.2%" },
    { event: "Retail Sales m/m", impact: "High", currency: "CAD", date: "25/04/2025 12:00:00", forecast: "-0.4%", previous: "-0.6%" },
    { event: "Revised UoM Inflation Expect", impact: "Low", currency: "USD", date: "25/04/2025 12:00:00", forecast: "0", previous: "6.7%" },
    { event: "Revised UoM Consumer Sentiment", impact: "Medium", currency: "USD", date: "25/04/2025 20:00:00", forecast: "50.8", previous: "50.8" },
    { event: "MPC Member Greene Speaks", impact: "Low", currency: "GBP", date: "25/04/2025 20:00:00", forecast: "0", previous: "0" },
    { event: "IMF Meeting", impact: "Low", currency: "All", date: "25/04/2025 20:15:00", forecast: "0", previous: "0" },
    { event: "IMF Meeting", impact: "Low", currency: "All", date: "25/04/2025 20:15:00", forecast: "0", previous: "0" },
  ];

  const getImpactColor = (impact: string) => {
    switch (impact) {
      case "High": return "bg-red-500/20 text-red-400";
      case "Medium": return "bg-yellow-500/20 text-yellow-400";
      case "Low": return "bg-blue-500/20 text-blue-400";
      case "Holiday": return "bg-purple-500/20 text-purple-400";
      default: return "bg-white/10 text-white/60";
    }
  };

  return (
    <div className="p-6 space-y-6 overflow-y-auto h-full">
      <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-6">
        {/* Header */}
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center gap-2">
            <svg className="w-5 h-5 text-emerald-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
            <h2 className="text-white font-semibold text-lg">Economic Calendar</h2>
          </div>
          <div className="flex items-center gap-2">
            <button className="px-4 py-2 bg-white/5 text-white/60 rounded-lg text-sm border border-white/10">
              Friday
            </button>
            <button className="px-4 py-2 bg-emerald-500/20 text-emerald-400 rounded-lg text-sm border border-emerald-500/30">
              Saturday
            </button>
          </div>
        </div>

        {/* Filters */}
        <div className="flex items-center justify-between mb-6 pb-6 border-b border-white/10">
          {/* Currency Filter */}
          <div className="flex items-center gap-2">
            <svg className="w-4 h-4 text-emerald-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <span className="text-white/60 text-sm mr-2">Currency</span>
            <div className="flex items-center gap-1">
              {currencies.map((currency) => (
                <button
                  key={currency}
                  className={`px-3 py-1.5 rounded-lg text-xs transition ${
                    currency === "GBP"
                      ? "bg-emerald-500/20 text-emerald-400 border border-emerald-500/30"
                      : "text-white/60 hover:bg-white/5"
                  }`}
                >
                  {currency}
                </button>
              ))}
            </div>
          </div>

          {/* Impact Filter */}
          <div className="flex items-center gap-2">
            <svg className="w-4 h-4 text-emerald-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
            <span className="text-white/60 text-sm mr-2">Impact</span>
            <div className="flex items-center gap-1">
              {impacts.map((impact) => (
                <button
                  key={impact}
                  className={`px-3 py-1.5 rounded-lg text-xs transition ${getImpactColor(impact)}`}
                >
                  {impact}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* Table */}
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-white/10">
                <th className="text-left text-white/40 text-xs font-medium px-4 py-3">Event</th>
                <th className="text-left text-white/40 text-xs font-medium px-4 py-3">
                  <div className="flex items-center gap-1">
                    Impact
                    <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </th>
                <th className="text-left text-white/40 text-xs font-medium px-4 py-3">
                  <div className="flex items-center gap-1">
                    Currency
                    <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </th>
                <th className="text-left text-white/40 text-xs font-medium px-4 py-3">
                  <div className="flex items-center gap-1">
                    Date
                    <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </th>
                <th className="text-left text-white/40 text-xs font-medium px-4 py-3">
                  <div className="flex items-center gap-1">
                    Forecast
                    <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </th>
                <th className="text-left text-white/40 text-xs font-medium px-4 py-3">
                  <div className="flex items-center gap-1">
                    Previous
                    <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                </th>
              </tr>
            </thead>
            <tbody>
              {events.map((event, idx) => (
                <tr key={idx} className="border-b border-white/5 hover:bg-white/5 transition">
                  <td className="px-4 py-3 text-white text-sm">{event.event}</td>
                  <td className="px-4 py-3">
                    <span className={`px-2 py-1 rounded text-xs ${getImpactColor(event.impact)}`}>
                      {event.impact}
                    </span>
                  </td>
                  <td className="px-4 py-3 text-white/60 text-sm">{event.currency}</td>
                  <td className="px-4 py-3 text-white/60 text-sm font-mono">{event.date}</td>
                  <td className="px-4 py-3 text-white/60 text-sm">{event.forecast}</td>
                  <td className="px-4 py-3 text-white/60 text-sm">{event.previous}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Footer Note */}
        <div className="mt-6 pt-4 border-t border-white/10 flex items-center gap-2 text-xs text-yellow-400">
          <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
            <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clipRule="evenodd" />
          </svg>
          <span>Restricted News.</span>
          <a href="#" className="text-emerald-400 hover:underline">More Info</a>
        </div>

        {/* Pagination */}
        <div className="mt-4 flex items-center justify-center gap-2">
          <button className="w-8 h-8 rounded-lg bg-white/5 text-white/40 hover:bg-white/10 transition flex items-center justify-center">
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
            </svg>
          </button>
          {[1, 2, 3, 4, 5].map((page) => (
            <button
              key={page}
              className={`w-8 h-8 rounded-lg text-sm transition ${
                page === 2
                  ? "bg-emerald-500/20 text-emerald-400 border border-emerald-500/30"
                  : "bg-white/5 text-white/60 hover:bg-white/10"
              }`}
            >
              {page.toString().padStart(2, "0")}
            </button>
          ))}
          <button className="w-8 h-8 rounded-lg bg-white/5 text-white/40 hover:bg-white/10 transition flex items-center justify-center">
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
            </svg>
          </button>
        </div>
      </div>
    </div>
  );
}
