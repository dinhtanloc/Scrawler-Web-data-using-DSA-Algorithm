export default function MessageBubble({ sender, text }) {
    const isUser = sender === "user";
    return (
      <div
        className={`flex ${
          isUser ? "justify-end" : "justify-start"
        } mb-4`}
      >
        <div
          className={`p-3 rounded-lg max-w-xs ${
            isUser
              ? "bg-blue-500 text-white"
              : "bg-gray-300 text-black"
          }`}
        >
          {text}
        </div>
      </div>
    );
  }
  