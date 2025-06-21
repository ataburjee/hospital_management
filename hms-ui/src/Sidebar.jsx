import { useState } from "react";

function Sidebar() {
  const [isOnline, setIsOnline] = useState(true);

  return (
    <div className="w-[25%] min-h-screen bg-[#343E59] text-white p-6 flex flex-col items-center">
      {/* Profile Picture */}
      <div className="w-24 h-24 rounded-full bg-white overflow-hidden mb-4">
        <img
          src="https://via.placeholder.com/100"
          alt="Profile"
          className="w-full h-full object-cover"
        />
      </div>

      {/* Doctor Info */}
      <h2 className="text-xl font-semibold">Dr. Amanda Rich</h2>
      <p className="text-sm text-gray-300 mt-1">Cardiologist</p>

      {/* Availability Toggle */}
      <div className="mt-6 w-full text-center">
        <p className="text-sm text-gray-300 mb-2">Availability</p>

        <div
          className={`inline-block px-4 py-2 rounded-full cursor-pointer font-semibold transition-colors ${
            isOnline ? "bg-green-500 text-white" : "bg-red-500 text-white"
          }`}
          onClick={() => setIsOnline(!isOnline)}
        >
          {isOnline ? "Online" : "Offline"}
        </div>
      </div>
    </div>
  );
}

export default Sidebar;
