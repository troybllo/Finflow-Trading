export default function Header() {
  return (
    <nav className="flex items-center justify-between px-8 py-4 border-b border-white/10">
      <div className="flex items-center gap-8">
        <div className="w-10 h-10 bg-white/10 backdrop-blur-md rounded-lg flex items-center justify-center border border-white/20">
          <span className="text-white font-bold">T</span>
        </div>

        <div className="flex items-center gap-6 text-sm">
          <button className="text-white/60 hover:text-white transition">
            Account Overview
          </button>
          <button className="text-white hover:text-white transition">
            Trading Overview
          </button>
          <button className="text-white/60 hover:text-white transition">
            Transactions
          </button>
          <button className="text-white/60 hover:text-white transition">
            Composition
          </button>
        </div>
      </div>

      <div className="flex items-center gap-4">
        <button className="w-10 h-10 bg-white/5 hover:bg-white/10 backdrop-blur-md rounded-lg flex items-center justify-center border border-white/10 transition">
          <svg
            className="w-5 h-5 text-white/60"
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
        </button>
        <button className="w-10 h-10 bg-white/5 hover:bg-white/10 backdrop-blur-md rounded-lg flex items-center justify-center border border-white/10 transition">
          <svg
            className="w-5 h-5 text-white/60"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"
            />
          </svg>
        </button>
        <button className="w-10 h-10 bg-white/5 hover:bg-white/10 backdrop-blur-md rounded-lg flex items-center justify-center border border-white/10 transition">
          <svg
            className="w-5 h-5 text-white/60"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"
            />
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"
            />
          </svg>
        </button>
        <div className="flex items-center gap-2 bg-white/5 backdrop-blur-md rounded-lg px-3 py-2 border border-white/10">
          <div className="w-6 h-6 bg-purple-500 rounded-full"></div>
          <div className="text-xs">
            <div className="text-white font-medium">User Name</div>
            <div className="text-white/40">user@email.com</div>
          </div>
          <svg
            className="w-4 h-4 text-white/60"
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
        <button className="w-10 h-10 bg-white/5 hover:bg-white/10 backdrop-blur-md rounded-lg flex items-center justify-center border border-white/10 transition">
          <svg
            className="w-5 h-5 text-white/60"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M4 6h16M4 12h16M4 18h16"
            />
          </svg>
        </button>
      </div>
    </nav>
  );
}
