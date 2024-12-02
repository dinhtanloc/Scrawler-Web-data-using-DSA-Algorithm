export default function UploadHTML({ onUpload }) {
    const handleFileChange = (event) => {
      const file = event.target.files[0];
  
      if (file) {
        const reader = new FileReader();
          reader.onload = (e) => {
          const fileContent = e.target.result; 
          onUpload(fileContent); 
        };
          reader.readAsText(file);
      }
    };
  
    return (
      <label className="bg-red-500 text-white px-4 py-3 rounded-md cursor-pointer">
        Tải tệp HTML
        <input
          type="file"
          accept=".html"
          onChange={handleFileChange}
          className="hidden"
        />
      </label>
    );
  }
  