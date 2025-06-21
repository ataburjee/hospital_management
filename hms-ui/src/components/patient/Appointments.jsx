import { useState } from "react";
import DateNavigator from "../DateNavigator";

const appointments = [
  {
    id: 1,
    name: "John Doe",
    time: "9:00 AM – 9:30 AM",
    purpose: "Routine Check-up",
    status: "Confirmed",
    date: "20",
  },
  {
    id: 2,
    name: "Priya Sharma",
    time: "10:00 AM – 10:30 AM",
    purpose: "Blood Pressure Review",
    status: "Pending",
    date: "20",
  },
  {
    id: 3,
    name: "Alex Roy",
    time: "11:00 AM – 11:45 AM",
    purpose: "Follow-up Consultation",
    status: "Confirmed",
    date: "21",
  },
];

function Appointments() {
  const [selectedDate, setSelectedDate] = useState("20");
  const [searchQuery, setSearchQuery] = useState("");

  const filteredAppointments = appointments
    .filter((appt) => appt.date === selectedDate)
    .filter((appt) =>
      appt.name.toLowerCase().includes(searchQuery.toLowerCase())
    );

  return (
    <div className="w-full">
      {/* Header */}
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-2xl font-semibold text-gray-800">Appointments</h2>
        <div className="flex space-x-2">
          <button className="px-4 py-2 bg-gray-100 text-sm rounded-md">Today</button>
          <button className="px-4 py-2 bg-gray-100 text-sm rounded-md">Week</button>
          <button className="px-4 py-2 bg-purple-600 text-white text-sm rounded-md">Month</button>
        </div>
      </div>

      {/* Date Navigator */}
      <DateNavigator
        selectedDate={selectedDate}
        onDateChange={(d) => setSelectedDate(d)}
      />

      {/* Search Input */}
      <div className="mb-4">
        <input
          type="text"
          placeholder="Search by patient name..."
          className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {/* Appointment Cards */}
      {filteredAppointments.length > 0 ? (
        filteredAppointments.map((appt) => (
          <div
            key={appt.id}
            className="bg-white p-4 rounded-xl shadow-sm mb-4 flex items-center justify-between border"
          >
            <div>
              <h3 className="text-lg font-semibold text-gray-900">{appt.name}</h3>
              <p className="text-sm text-gray-600">{appt.time}</p>
              <p className="text-sm text-purple-600 font-medium">{appt.purpose}</p>
            </div>
            <div
              className={`text-sm font-medium ${
                appt.status === "Confirmed"
                  ? "text-green-600"
                  : "text-yellow-600"
              }`}
            >
              {appt.status}
            </div>
          </div>
        ))
      ) : (
        <div className="text-gray-400 text-center mt-10">No Appointments Found</div>
      )}
    </div>
  );
}

export default Appointments;
