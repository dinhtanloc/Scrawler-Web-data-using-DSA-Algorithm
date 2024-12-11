import React from "react";
import "@/styles/renderContent.css"; // Import file CSS

const RenderContent = ({ content, error }) => {
  // Trường hợp không có nội dung và không có lỗi
  if (!content && !error) return <p className="text-gray-500">Chưa có nội dung đọc được.</p>;

  // Trường hợp có lỗi cú pháp
  if (error) {
    const { reason, lines } = error; // reason: lý do lỗi, lines: mảng chứa các dòng liên quan

    return (
      <div>
        <h1 className="error-title">Lỗi:</h1>
        <p className="error-reason">{reason}</p>
        <pre className="error-context">
          {lines.map((line, index) => (
            <div
              key={index}
              className={line.isError ? "error-line" : "context-line"} // Tô đỏ dòng lỗi
            >
              {line.content}
            </div>
          ))}
        </pre>
      </div>
    );
  }

  // Trường hợp không có lỗi, hiển thị nội dung bình thường
  return (
    <div>
      {content.title && <div className="render-title">{content.title}</div>}
      {content.h1 && <h1 className="render-h1">{content.h1}</h1>}
      {content.h2 && <h2 className="render-h2">{content.h2}</h2>}
      {content.h3 && <h3 className="render-h3">{content.h3}</h3>}
      {content.h4 && <h4 className="render-h4">{content.h4}</h4>}
      {content.h5 && <h5 className="render-h5">{content.h5}</h5>}
      {content.h6 && <h6 className="render-h6">{content.h6}</h6>}
      {content.p && <p className="render-p">{content.p}</p>}
      {content.span && <span className="render-span">{content.span}</span>}
    </div>
  );
};

export default RenderContent;
