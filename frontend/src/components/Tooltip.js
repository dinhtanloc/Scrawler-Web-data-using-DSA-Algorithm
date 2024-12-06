export default function Tooltip({ text, children }) {
    return (
      <div className="relative group">
        {children}
        <span className="absolute left-1/2 transform -translate-x-1/2 bottom-full mb-2 hidden group-hover:block bg-gray-700 text-white text-xs p-2 rounded-md">
          {text}
        </span>
      </div>
    );
  }
  