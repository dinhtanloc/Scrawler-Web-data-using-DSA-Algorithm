"use client";

import { useState } from "react";
import Tooltip from "@/components/Tooltip";
import UploadHTML from "@/components/UploadHTML";
import Spinner from "@/components/Spinner";
import {getReq, postReq} from "@/utils/fetchData";
import { FaSearch } from 'react-icons/fa';

export default function Home() {
  const [url, setUrl] = useState("");
  const [htmlContent, setHtmlContent] = useState("");
  const [parsedContent, setParsedContent] = useState("");
  const [loading, setLoading] = useState(false);
  const [isDarkMode, setIsDarkMode] = useState(false);

  const toggleDarkMode = () => {
    setIsDarkMode(!isDarkMode);
    document.documentElement.classList.toggle("dark");
  };

  const handleViewHtml = () => {
    setLoading(true);
    setTimeout(() => {
      setHtmlContent(`
        <html>
          <body>
            <p>Hello</p>
            <span>This is a label</span>
          </body>
        </html>
      `);
      setLoading(false);
    }, 2000);
  };

  const handleParseHtml = async () => {
    setLoading(true);
  
    try {
      // Kiểm tra nếu có nội dung HTML trong htmlContent
      if (htmlContent) {
        const formData = new FormData();
        formData.append("htmlContent", htmlContent);  
  
        const response = await postReq("/api/html/read", { "htmlContent": htmlContent });
        setParsedContent(response);
        console.log(response);
  
      } else {
        console.error("Error: No HTML content provided.");
        alert("Vui lòng nhập nội dung HTML.");
      }
  
    } catch (error) {
      console.error("Error reading HTML:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSave = () => {
    alert("Nội dung đã được lưu vào cơ sở dữ liệu!");
  };

  const handleChatBot = () => {
    alert("Chuyển đến Chat Bot!");
  };

  const handleUpload = (html) => {
    setHtmlContent(html); 
  };

  const handleCrawlHtml = async () => {
    if (!url) {
      alert("Please enter a URL.");
      return;
    }
    
    setLoading(true);
    try {
      console.log("URL:", url);
      const response = await postReq("/api/html/crawl", { "url": url });
      console.log(response);
      setHtmlContent(response.html);
      console.log(response.html);
    } catch (error) {
      console.error("Error fetching HTML:", error);
      alert("Failed to fetch HTML.");
    } finally {
      setLoading(false);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      handleCrawlHtml(); 
    }
  };

  

  return (
    <div
      className={`min-h-screen ${
        isDarkMode ? "bg-black text-white" : "bg-white text-black"
      } p-6`}
    >
      {/* Dark Mode Toggle */}
      <button
        onClick={toggleDarkMode}
        className={`fixed top-4 right-4 px-4 py-2 rounded-md ${
          isDarkMode
            ? "bg-white text-black border border-gray-700"
            : "bg-black text-white"
        }`}
      >
        {isDarkMode ? "Light Mode" : "Dark Mode"}
      </button>

      {/* Tiêu đề */}
      <h1
        className={`text-center text-4xl font-bold mb-6 ${
          isDarkMode ? "text-red-500" : "text-red-700"
        }`}
      >
        HTML Document Reader
      </h1>

      {/* Thanh nhập URL và nút tải tệp HTML */}
      <div className="flex justify-center items-center mb-6 space-x-4">
        <div className="flex w-1/3">
          <div className="relative w-full">
            <input
              type="text"
              placeholder="Nhập URL trang web..."
              className={`w-full p-3 pr-10 border rounded-md focus:outline-none ${
                isDarkMode
                  ? "bg-black text-white border-gray-700"
                  : "bg-white text-black border-gray-300"
              }`}
              onKeyDown={handleKeyDown}
              value={url}
              onChange={(e) => setUrl(e.target.value)}
            />
            <button
              onClick={handleCrawlHtml} 
              className="absolute top-1/2 right-3 transform -translate-y-1/2 text-lg"
            >
              <FaSearch /> 
            </button>
          </div>
        </div>
        <span
          className={`text-lg ${
            isDarkMode ? "text-gray-400" : "text-gray-600"
          }`}
        >
          hoặc
        </span>
        <Tooltip text="Tải tệp HTML từ máy tính">
          <UploadHTML onUpload={handleUpload} />
        </Tooltip>
      </div>

      {/* Loading Indicator */}
      {loading && <Spinner />}

      {/* Khung hiển thị nội dung */}
      <div className="flex space-x-6">
        {/* Khung HTML */}
        <div
          className={`w-1/2 p-4 shadow-md rounded-md ${
            isDarkMode
              ? "bg-black border border-red-500"
              : "bg-gray-100 border border-gray-300"
          }`}
        >
          {/* Nút HTML */}
          <div className="mb-4">
            <Tooltip text="Hiển thị nội dung HTML">
              <button
                onClick={handleViewHtml}
                className={`px-6 py-3 rounded-md ${
                  isDarkMode
                    ? "bg-red-500 hover:bg-red-600 text-white"
                    : "bg-red-700 hover:bg-red-800 text-white"
                }`}
              >
                HTML
              </button>
            </Tooltip>
          </div>

          <h2
            className={`text-xl font-semibold mb-4 ${
              isDarkMode ? "text-red-500" : "text-red-700"
            }`}
          >
            HTML Gốc
          </h2>
          <div className="overflow-auto h-[500px] border border-gray-700 p-4 rounded-md">
            <pre className={`${isDarkMode ? "text-white" : "text-black"}`}>
              {htmlContent}
            </pre>
          </div>
        </div>

        {/* Khung nội dung đã đọc */}
        <div
          className={`w-1/2 p-4 shadow-md rounded-md ${
            isDarkMode
              ? "bg-black border border-red-500"
              : "bg-gray-100 border border-gray-300"
          }`}
        >
          {/* Ba nút Đọc, Save, Chat Bot */}
          <div className="flex space-x-4 mb-4">
            <Tooltip text="Chuyển đổi HTML sang nội dung dễ đọc">
              <button
                onClick={handleParseHtml}
                className={`px-6 py-3 rounded-md ${
                  isDarkMode
                    ? "bg-red-500 hover:bg-red-600 text-white"
                    : "bg-red-700 hover:bg-red-800 text-white"
                }`}
              >
                Đọc
              </button>
            </Tooltip>
            <Tooltip text="Lưu nội dung vào cơ sở dữ liệu">
              <button
                onClick={handleSave}
                className={`px-6 py-3 rounded-md ${
                  isDarkMode
                    ? "bg-red-500 hover:bg-red-600 text-white"
                    : "bg-red-700 hover:bg-red-800 text-white"
                }`}
              >
                Save
              </button>
            </Tooltip>
            <Tooltip text="Chuyển đến Chat Bot">
              <button
                onClick={handleChatBot}
                className={`px-6 py-3 rounded-md ${
                  isDarkMode
                    ? "bg-red-500 hover:bg-red-600 text-white"
                    : "bg-red-700 hover:bg-red-800 text-white"
                }`}
              >
                Chat Bot →
              </button>
            </Tooltip>
          </div>

          <h2
            className={`text-xl font-semibold mb-4 ${
              isDarkMode ? "text-red-500" : "text-red-700"
            }`}
          >
            Nội dung đã đọc
          </h2>
          <div className="overflow-auto h-[500px] border border-gray-700 p-4 rounded-md">
            <div className={`${isDarkMode ? "text-white" : "text-black"}`}>
              {renderContent(parsedContent)}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}



const renderContent = (content) => {
  return (
    <div>
      {content.p && <p>{content.p}</p>}
      {content.div && <div>{content.div}</div>}
      {content.h2 && <h2>{content.h2}</h2>}
      {content.h3 && <h3>{content.h3}</h3>}
      {content.title && <title>{content.title}</title>}
      {content.span && <span>{content.span}</span>}
    </div>
  );
};