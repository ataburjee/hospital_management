// src/DateNavigator.js
import { useState } from "react";

const days = [
  { day: "Mon", date: "17" },
  { day: "Tue", date: "18" },
  { day: "Wed", date: "19" },
  { day: "Thu", date: "20" },
  { day: "Fri", date: "21" },
  { day: "Sat", date: "22" },
  { day: "Sun", date: "23" },
];

function DateNavigator() {
  const [selected, setSelected] = useState("20");

  return (
    <div className="flex gap-4 overflow-x-auto mb-6">
      {days.map((d) => (
        <div
          key={d.date}
          className={`min-w-[60px] text-center p-2 rounded-xl cursor-pointer border transition-all
            ${selected === d.date
              ? "bg-purple-600 text-white border-purple-600"
              : "bg-white text-gray-700 border-gray-300"
            }`}
          onClick={() => setSelected(d.date)}
        >
          <div className="text-sm font-medium">{d.day}</div>
          <div className="text-lg font-bold">{d.date}</div>
        </div>
      ))}
    </div>
  );
}

export default DateNavigator;
