"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Tooltip from "@/components/Tooltip";
import UploadHTML from "@/components/UploadHTML";
import Spinner from "@/components/Spinner";
import { postReq } from "@/utils/fetchData";
import { FaSearch } from "react-icons/fa";
import "@/styles/renderContent.css";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import processParsedContent from "@/utils/processParsedContent";
export default function Home() {
  const [url, setUrl] = useState("");
  const [urlCheck, setUrlCheck] = useState(false);
  const [htmlContent, setHtmlContent] = useState("");
  const [parsedContent, setParsedContent] = useState(null);
  const [loading, setLoading] = useState(false);
  const [isDarkMode, setIsDarkMode] = useState(false);

  const router = useRouter();

  useEffect(() => {
   
  }, [htmlContent,parsedContent]);

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
      if (htmlContent) {
        const response = await postReq("/api/html/read", { htmlContent, urlCheck });
        console.log(response);
        setParsedContent(response);
      } else {
        toast.error(`Vui lòng nhập nội dung HTML.`, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      }
    } catch (error) {
      console.error("Error parsing HTML:", error);
      toast.error(`Không thể lấy nội dung HTML. ${error}`, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
    } finally {
      setUrlCheck(false);
      setLoading(false);
    }
  };

 

  const handleChatBot = () => {
    router.push("/chatbot");
  };

  const handleUpload = (html) => {
    setHtmlContent(html);
  };

  const handleCrawlHtml = async () => {
    console.log('check crawl');
    if (!url) {
      toast.warning("Nội dung HTML rỗng. Vui lòng kiểm tra lại!", {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      return;
    }

    setLoading(true);
    try {
      const response = await postReq("/api/html/crawl", { url });
      setHtmlContent(response.html);
      setUrlCheck(true);
    } catch (error) {
      console.error("Error fetching HTML:", error);
      setUrlCheck(false);
      toast.error(`Không thể lấy nội dung HTML. ${error}`, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
    
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async(e) => {
    console.log('check save');
    e.preventDefault();
    if (!htmlContent) {
      toast.warning("Nội dung HTML rỗng. Vui lòng kiểm tra lại!", {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });

      return;
    }
    const processedContent = processParsedContent(parsedContent);
    setLoading(true);
    try {
      const response = await postReq("/chunks/save", { processedContent });
      toast.success("Nội dung đã được lưu thành công!", {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      
    } catch (error) {
      console.error("Error fetching HTML:", error);
      
      toast.warning("Nội dung HTML rỗng. Vui lòng kiểm tra lại!", {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      
    } finally {
      setLoading(false);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      handleCrawlHtml();
    }
  };

  const renderContent = (content) => {
    if (!content) return null;
  
    return (
      <div className="space-y-4">
        {content.title && (
          <div className="text-lg font-bold text-center text-blue-600 border-b-2 pb-2">
            {content.title}
          </div>
        )}
        {content.h1 && (
          <h1 className="text-2xl font-bold border-b-2 pb-2">{content.h1}</h1>
        )}
        {content.h2 && (
          <h2 className="text-xl font-semibold border-l-4 pl-2 border-blue-500">
            {content.h2}
          </h2>
        )}
        {content.h3 && (
          <h3 className="text-lg font-medium pl-4 text-gray-700">{content.h3}</h3>
        )}
        {content.p && (
          <p className="text-base leading-relaxed text-justify pl-6">{content.p}</p>
        )}
        {content.span && (
          <span className="text-sm italic pl-8 text-gray-600">{content.span}</span>
        )}
      </div>
    );
  };
  
  

  return (
    <div
      className={`min-h-screen ${
        isDarkMode ? "bg-black text-white" : "bg-white text-black"
      } p-6`}
    >
      <ToastContainer />
      {/* Dark Mode Toggle */}
      <button
        onClick={toggleDarkMode}
        className={`px-4 py-2 rounded-md ${
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
                type="button"
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
            {renderContent(parsedContent)}
          </div>
        </div>
      </div>
    </div>
  );
}
