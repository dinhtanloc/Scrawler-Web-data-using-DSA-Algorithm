import { useState } from "react";

export default function ChatInput({ onSend }) {
  const [input, setInput] = useState("");

  const handleSend = () => {
    if (input.trim()) {
      onSend(input);
      setInput(""); // Xóa nội dung sau khi gửi
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      handleSend();
    }
  };

  return (
    <div className="p-4 bg-gray-200 dark:bg-gray-700 rounded-b-lg">
      <div className="flex">
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="Nhập tin nhắn..."
          className="flex-1 p-3 border rounded-l-md focus:outline-none dark:bg-gray-600 dark:text-white"
        />
        <button
          onClick={handleSend}
          className="bg-red-500 text-white px-4 py-3 rounded-r-md hover:bg-red-600"
        >
          Gửi
        </button>
      </div>
    </div>
  );
}
