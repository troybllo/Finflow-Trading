import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Sidebar from "./components/layout/Sidebar";
import Header from "./components/layout/Header";
import Dashboard from "./pages/Dashboard";
import TradingInstruments from "./pages/TradingInstruments";
import Leaderboard from "./pages/Leaderboard";
import EconomicCalendar from "./pages/EconomicCalendar";

function App() {
  return (
    <BrowserRouter>
      <div className="h-screen w-screen overflow-hidden bg-gradient-to-br from-black via-emerald-950 to-black flex">
        <Sidebar />

        <div className="flex-1 flex flex-col overflow-hidden">
          <Header />

          <main className="flex-1 overflow-hidden">
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route
                path="/account"
                element={<PlaceholderPage title="Account Overview" />}
              />
              <Route
                path="/orders"
                element={<PlaceholderPage title="Order List" />}
              />
              <Route
                path="/trading"
                element={<PlaceholderPage title="Trading" />}
              />
              <Route
                path="/settings"
                element={<PlaceholderPage title="User Settings" />}
              />
              <Route path="/leaderboard" element={<Leaderboard />} />
              <Route
                path="/extras"
                element={<PlaceholderPage title="Account Extras" />}
              />
              <Route
                path="/news"
                element={<PlaceholderPage title="News Feed" />}
              />
              <Route path="/calendar" element={<EconomicCalendar />} />
              <Route path="/instruments" element={<TradingInstruments />} />
              <Route
                path="/help"
                element={<PlaceholderPage title="Help Center" />}
              />
              <Route
                path="/website"
                element={<PlaceholderPage title="Back to Website" />}
              />
            </Routes>
          </main>
        </div>
      </div>
    </BrowserRouter>
  );
}

// Placeholder for pages not yet implemented
function PlaceholderPage({ title }: { title: string }) {
  return (
    <div className="p-6 h-full flex items-center justify-center">
      <div className="bg-white/5 backdrop-blur-md rounded-2xl border border-white/10 p-12 text-center">
        <svg
          className="w-16 h-16 text-emerald-400/50 mx-auto mb-4"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={1.5}
            d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"
          />
        </svg>
        <h2 className="text-white text-2xl font-semibold mb-2">{title}</h2>
        <p className="text-white/40">This page is coming soon</p>
      </div>
    </div>
  );
}

export default App;
