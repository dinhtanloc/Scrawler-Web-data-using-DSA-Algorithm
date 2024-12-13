const processParsedContent = (content) => {
    let result = [];
  
    // Đệ quy để lấy tất cả chuỗi từ object hoặc mảng
    function deepExtract(value) {
      if (typeof value === "string") {
        result.push(value.trim()); // Loại bỏ khoảng trắng thừa
      } else if (Array.isArray(value)) {
        value.forEach(deepExtract);
      } else if (typeof value === "object" && value !== null) {
        Object.values(value).forEach(deepExtract);
      }
    }
  
    deepExtract(content);
  
    // Ghép các chuỗi lại thành một đoạn văn bản hoàn chỉnh
    return result.join(" ");
  };
  
  export default processParsedContent;
  