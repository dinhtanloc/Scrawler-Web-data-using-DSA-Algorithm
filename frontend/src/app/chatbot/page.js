"use client";

import { useState, useEffect, useRef } from "react";
import { useRouter } from "next/navigation";
import MessageBubble from "@/components/MessageBubble";
import ChatInput from "@/components/ChatInput";

export default function ChatbotPage() {
  const [messages, setMessages] = useState([
    { sender: "bot", text: "Chào bạn! Đây là công cụ đọc thẻ HTML. Bạn cần hỗ trợ gì?" },
  ]);
  const messagesEndRef = useRef(null);

  const router = useRouter();

  // const handleSendMessage = (message) => {
  //   setMessages((prevMessages) => [...prevMessages, { sender: "user", text: message }]);

  //   fetch("chat", {
  //     method: "POST",
  //     headers: {
  //       "Content-Type": "application/json",
  //     },
  //     body: JSON.stringify({"query": message }),
  //   })
  //     .then((response) => response.json())
  //     .then((data) => {
  //       setMessages((prevMessages) => [
  //         ...prevMessages,
  //         { sender: "bot", text: data.reply },
  //       ]);
  //     })
  //     .catch((error) => {
  //       console.error("Error:", error);
  //       setMessages((prevMessages) => [
  //         ...prevMessages,
  //         { sender: "bot", text: "Xin lỗi, đã xảy ra lỗi!" },
  //       ]);
  //     });
  // };


  const handleSendMessage = async(message) => {
    setMessages((prevMessages) => [...prevMessages, { sender: "user", text: message }]);
    if (!url) {
      alert("Vui lòng nhập URL.");
      return;
    }

    setLoading(true);
    try {
      const response = await postReq("chat", { "query": message });
      setMessages((prevMessages) => [
        ...prevMessages,
        { sender: "bot", text: data.reply },
      ]);
    } catch (error) {
      console.error("Error fetching HTML:", error);
      setMessages((prevMessages) => [
        ...prevMessages,
        { sender: "bot", text: "Xin lỗi, đã xảy ra lỗi!" },
      ]);
      alert("Không thể lấy nội dung HTML.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages]);

  return (
    <div
      className={`min-h-screen flex justify-center bg-gray-100 dark:bg-gray-900`}
    >
      <div className="relative w-full max-w-screen-lg bg-white dark:bg-gray-800 rounded-lg shadow-lg flex flex-col h-full">
        <header className="relative p-4 bg-red-500 text-white text-center text-xl font-bold rounded-t-lg">
          <button
            onClick={() => router.push("/")}
            className="absolute left-4 top-1/2 transform -translate-y-1/2 text-white text-2xl"
          >
            ←
          </button>
          Chatbot
        </header>

        <div
          className="flex-1 overflow-y-auto p-4"
          style={{
            minHeight: "500px", // Đặt chiều cao tối thiểu cố định
            maxHeight: "calc(100vh - 200px)", // Hạn chế tối đa chiều cao
          }}
        >
          {messages.map((msg, index) => (
            <MessageBubble key={index} sender={msg.sender} text={msg.text} />
          ))}
          <div ref={messagesEndRef} />
        </div>

        <div
          className="bg-gray-200 dark:bg-gray-700 p-4"
          style={{
            marginBottom: "0px",
            borderRadius: "7px",
          }}
        >
          <ChatInput onSend={handleSendMessage} />
        </div>
      </div>
    </div>
  );
}
