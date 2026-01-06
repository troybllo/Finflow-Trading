import Layout from "./components/layout/Layout";

export default function App() {
  return <Layout />;
}

// Placeholder for pages not yet implemented
export function PlaceholderPage({ title }: { title: string }) {
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
