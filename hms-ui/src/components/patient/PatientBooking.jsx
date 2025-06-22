import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function PatientBooking() {
    const [step, setStep] = useState(1);
    const [selectedDate, setSelectedDate] = useState(6);
    const [availableSlots, setAvailableSlots] = useState([]);
    const [selectedSlot, setSelectedSlot] = useState(null);
    const [loading, setLoading] = useState(false);

    const [patient, setPatient] = useState({
        patientId: 1,
        patientName: "Rehman Khan",
        phone: "",
        age: "",
        gender: ""
    });

    const [doctors, setDoctors] = useState([
        { id: 1, name: "Dr. Vikash Rathee", specialization: "Cardiology" },
        { id: 2, name: "Dr. Aisha Sharma", specialization: "Neurology" },
        { id: 3, name: "Dr. Sameer Roy", specialization: "Pediatrics" },
        { id: 4, name: "Dr. Neha Singh", specialization: "Dermatology" },
        { id: 5, name: "Dr. Arjun Rao", specialization: "Orthopedics" },
        { id: 6, name: "Dr. Tanu Jain", specialization: "Pediatrics" },
        { id: 7, name: "Dr. Vikas Divyakirty", specialization: "Psychologist" },
        { id: 8, name: "Dr. Anirban Gorai", specialization: "Gynocologist" },
    ]);
    const [searchQuery, setSearchQuery] = useState("");

    const month = "March";
    const year = 2023;
    const daysInMonth = 31;

    const navigate = useNavigate();

    useEffect(() => {
        setAvailableSlots([]);
        setSelectedSlot(null);

        const fetchSlots = async () => {
            try {
                const formattedDate = `2025-06-${String(selectedDate).padStart(2, "0")}`;
                const res = await fetch(
                    `http://localhost:8080/api/slots/available?doctorId=HMS-DOC1879945d-5100-4a5c-9423-2dbdf19e9772&date=${formattedDate}`
                );
                const data = await res.json();

                const slots = data.map((slot) => ({
                    id: slot.timeSlotId,
                    time: slot.startTime,
                    end: slot.endTime,
                    status: slot.status,
                }));
                setAvailableSlots(slots);
            } catch (err) {
                console.error("Failed to fetch slots=", err);
                setAvailableSlots([]);
            }
        };

        fetchSlots();
    }, [selectedDate]);

    const handleConfirm = async () => {
        if (!selectedSlot || !patient.patientName.trim()) return;

        setLoading(true);

        const payload = {
            patientId: patient.patientId,
            appointmentSlotId: selectedSlot.id,
            patientName: patient.patientName,
        };

        try {
            const response = await fetch("http://localhost:8080/api/slots/book", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            if (!response.ok) throw new Error("Booking failed");

            const result = await response.text();
            alert(result);
            navigate("/success");
        } catch (err) {
            alert("❌ Booking failed.");
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const filteredDoctors = doctors.filter((doctor) =>
        doctor.name.toLowerCase().includes(searchQuery.toLowerCase())
    );

    return (
        <div className="min-h-screen flex flex-col">
            {/* Navbar */}
            <nav className="bg-white border-b border-gray-200 dark:bg-gray-900 dark:border-gray-700">
                <div className="max-w-screen-xl mx-auto flex items-center justify-between p-4">
                    <a href="#" className="flex items-center space-x-3">
                        <img src="https://flowbite.com/docs/images/logo.svg" className="h-8" alt="Logo" />
                        <span className="self-center text-2xl font-semibold whitespace-nowrap dark:text-white">HMS</span>
                    </a>
                    <div className="hidden md:flex items-center space-x-8">
                        <a href="#" className="text-gray-900 hover:text-blue-600 dark:text-white dark:hover:text-blue-400 font-medium">Home</a>
                        <a href="#" className="text-gray-900 hover:text-blue-600 dark:text-white dark:hover:text-blue-400 font-medium">About</a>
                        <a href="#" className="text-gray-900 hover:text-blue-600 dark:text-white dark:hover:text-blue-400 font-medium">Services</a>
                    </div>
                    <div className="flex items-center space-x-4">
                        <a href="tel:5541251234" className="text-sm text-gray-500 dark:text-white hover:underline">(555) 412-1234</a>
                        <a href="#" className="text-sm text-white bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded-md dark:bg-blue-500 dark:hover:bg-blue-600">Login</a>
                    </div>
                </div>
            </nav>

            {/* Main layout with Sidebar + Content */}
            <div className="flex flex-1 overflow-hidden">
                {/* Sidebar */}
                <aside className="w-80 border-r border-gray-200 bg-white p-4 overflow-y-auto">
                    {/* <h3 className="text-lg font-semibold text-purple-700 mb-4">Pick Your Doctor:</h3> */}
                    <input
                        type="text"
                        placeholder="Search doctor..."
                        className="w-full px-3 py-2 border rounded mb-4 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                    <ul className="space-y-3 max-h-[calc(100vh-10rem)] overflow-y-auto pr-2">
                        {filteredDoctors.map((doc) => (
                            <li key={doc.id} className="p-3 border rounded hover:bg-blue-50 cursor-pointer">
                                <p className="font-medium text-gray-800">{doc.name}</p>
                                <p className="text-sm text-gray-500">{doc.specialization}</p>
                            </li>
                        ))}
                    </ul>
                </aside>

                {/* Main Content */}
                <main className="flex-1 overflow-y-auto bg-gray-50 p-8">
                    {/* <h2 className="text-2xl font-semibold mb-6 text-purple-700 text-center">
                        {step === 1 ? "Patient Info & Slot Selection" : "Review & Confirm"}
                    </h2> */}

                    {step === 1 && (
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                            {/* Patient Info */}
                            <div className="bg-white p-6 rounded-xl shadow-md">
                                <h3 className="text-lg font-semibold mb-4 text-purple-700">Paitent Details</h3>
                                <div className="space-y-4">
                                    <input type="text" placeholder="Full Name" className="w-full px-3 py-2 border rounded"
                                        value={patient.patientName}
                                        onChange={(e) => setPatient({ ...patient, patientName: e.target.value })} />
                                    <input type="text" placeholder="Phone" className="w-full px-3 py-2 border rounded"
                                        value={patient.phone}
                                        onChange={(e) => setPatient({ ...patient, phone: e.target.value })} />
                                    <input type="text" placeholder="Age" className="w-full px-3 py-2 border rounded"
                                        value={patient.age}
                                        onChange={(e) => setPatient({ ...patient, age: e.target.value })} />
                                    <select value={patient.gender} className="w-full px-3 py-2 border rounded"
                                        onChange={(e) => setPatient({ ...patient, gender: e.target.value })}>
                                        <option value="">--Select Gender--</option>
                                        <option value="Male">Male</option>
                                        <option value="Female">Female</option>
                                        <option value="Trans">Trans</option>
                                    </select>
                                </div>
                            </div>

                            {/* Date & Slot Picker */}
                            <div>
                                <div className="bg-white p-4 rounded-xl shadow mb-4">
                                    <p className="font-medium text-gray-700 mb-2 text-center">
                                        {month} {year}
                                    </p>
                                    <div className="grid grid-cols-7 gap-2">
                                        {[...Array(daysInMonth)].map((_, i) => {
                                            const day = i + 1;
                                            const isSelected = selectedDate === day;
                                            return (
                                                <div
                                                    key={day}
                                                    onClick={() => setSelectedDate(day)}
                                                    className={`h-10 w-10 flex items-center justify-center rounded-full cursor-pointer text-sm ${isSelected
                                                        ? "bg-purple-600 text-white"
                                                        : "hover:bg-purple-100 text-gray-700"
                                                        }`}
                                                >
                                                    {day}
                                                </div>
                                            );
                                        })}
                                    </div>
                                </div>

                                <div className="bg-white p-4 rounded-xl shadow">
                                    <div className="flex justify-between items-center mb-3">
                                        <span className="font-medium text-gray-600">
                                            {month} {selectedDate}, {year}
                                        </span>
                                        <select className="border rounded px-2 py-1 text-sm">
                                            <option>12h</option>
                                            <option>24h</option>
                                        </select>
                                    </div>

                                    {availableSlots.length === 0 ? (
                                        <p className="text-gray-500 text-sm">Loading slots...</p>
                                    ) : (
                                        <div className="grid grid-cols-2 gap-2 mb-4">
                                            {availableSlots.map((slot) => (
                                                <button
                                                    key={slot.id}
                                                    onClick={() => setSelectedSlot(slot)}
                                                    className={`border rounded-md px-4 py-2 text-sm transition ${selectedSlot?.id === slot.id
                                                        ? "bg-purple-600 text-white border-purple-600"
                                                        : "border-purple-500 text-purple-600 hover:bg-purple-100"
                                                        }`}
                                                >
                                                    {slot.time}
                                                </button>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            </div>

                            {patient.patientName.trim() && selectedSlot && (
                                <div className="col-span-2 flex justify-end">
                                    <button
                                        onClick={() => setStep(2)}
                                        className="px-6 py-2 bg-purple-600 text-white rounded-md hover:bg-purple-700"
                                    >
                                        Next
                                    </button>
                                </div>
                            )}
                        </div>
                    )}

                    {step === 2 && (
                        <div className="bg-white p-6 rounded-xl shadow-md max-w-md mx-auto text-center mt-8">
                            <h3 className="text-xl font-semibold text-purple-700 mb-4">Confirm Booking</h3>
                            <div className="text-left mb-6 space-y-2 text-gray-700">
                                <p><span className="font-medium">Name:</span> {patient.patientName}</p>
                                <p><span className="font-medium">Phone:</span> {patient.phone}</p>
                                <p><span className="font-medium">Gender:</span> {patient.gender}</p>
                                <p><span className="font-medium">Doctor:</span> Vikash Rathee</p>
                                <p><span className="font-medium">Date:</span> {month} {selectedDate}, {year}</p>
                                <p><span className="font-medium">Time:</span> {selectedSlot?.time} - {selectedSlot?.end}</p>
                                <p><span className="font-medium">Platform:</span> Google Meet</p>
                            </div>

                            <div className="flex justify-center gap-1">
                                <button
                                    onClick={() => setStep(1)}
                                    className="px-4 py-2 border border-gray-400 text-gray-600 rounded-md hover:bg-gray-100"
                                >
                                    Back
                                </button>
                                <button
                                    onClick={handleConfirm}
                                    disabled={loading}
                                    className={`px-4 py-2 rounded-md text-white font-medium transition ${loading
                                        ? "bg-gray-400 cursor-not-allowed"
                                        : "bg-green-600 hover:bg-green-700"
                                        }`}
                                >
                                    {loading ? "Booking..." : "Confirm Booking"}
                                </button>
                            </div>
                        </div>
                    )}
                </main>
            </div>
        </div>
    );
}

export default PatientBooking;
