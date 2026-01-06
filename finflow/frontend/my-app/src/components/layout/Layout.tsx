import { BrowserRouter, Routes, Route } from "react-router-dom";
import { PlaceholderPage } from "../../App";
import Sidebar from "./Sidebar";
import Header from "./Header";
import Dashboard from "../../pages/Dashboard";
import Leaderboard from "../../pages/Leaderboard";
import TradingInstruments from "../../pages/TradingInstruments";
import EconomicCalendar from "../../pages/EconomicCalendar";
import Portfolio from "../../pages/Portfolio";

export default function Layout() {
  return (
    <BrowserRouter>
      <div className="h-screen w-screen overflow-hidden bg-gradient-to-br from-black via-emerald-950 to-black flex">
        <Sidebar />

        <div className="flex-1 flex flex-col overflow-hidden">
          <Header />

          <main className="flex-1 overflow-hidden">
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/portfolio" element={<Portfolio />} />
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
