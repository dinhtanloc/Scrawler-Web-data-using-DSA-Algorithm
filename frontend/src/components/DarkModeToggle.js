"use client";

import { useState } from "react";

export default function DarkModeToggle() {
  const [isDarkMode, setIsDarkMode] = useState(false);

  const toggleDarkMode = () => {
    setIsDarkMode(!isDarkMode);
    document.documentElement.classList.toggle("dark");
  };

  return (
    <button
      onClick={toggleDarkMode}
      className="fixed top-4 right-4 bg-gray-800 text-white px-4 py-2 rounded-md"
    >
      {isDarkMode ? "Light Mode" : "Dark Mode"}
    </button>
  );
}
