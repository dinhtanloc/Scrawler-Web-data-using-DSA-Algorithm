import React from "react";
import "@/styles/renderContent.css"; // Import file CSS

const RenderContent = ({ content }) => {
  if (!content) return <p className="text-gray-500">Chưa có nội dung đọc được.</p>;

  return (
    <div>
      {content.title && <div className="render-title">{content.title}</div>}
      {content.h1 && <h1 className="render-h1">{content.h1}</h1>}
      {content.h2 && <h2 className="render-h2">{content.h2}</h2>}
      {content.h3 && <h3 className="render-h3">{content.h3}</h3>}
      {content.p && <p className="render-p">{content.p}</p>}
      {content.span && <span className="render-span">{content.span}</span>}
    </div>
  );
};

export default RenderContent;
