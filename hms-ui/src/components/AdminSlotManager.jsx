import { useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

function AdminSlotPage() {
    const [activeTab, setActiveTab] = useState("create");
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [startTime, setStartTime] = useState("");
    const [endTime, setEndTime] = useState("");
    const [duration, setDuration] = useState(15);
    const [file, setFile] = useState(null);
    const [feedback, setFeedback] = useState("");

    // Bulk Create Specific
    const [bulkStartDate, setBulkStartDate] = useState(new Date());
    const [bulkEndDate, setBulkEndDate] = useState(new Date());
    const [repeatOn, setRepeatOn] = useState([]);
    const [slotType, setSlotType] = useState("ONLINE");
    const [tag, setTag] = useState("");
    const [note, setNote] = useState("");
    const [colorCode, setColorCode] = useState("#000000");

    const handleSubmit = () => {
        handleSubmitMessage("✅ Slot operation completed!");
    };

    const handleSubmitMessage = (message) => {
        setFeedback(message);
        setTimeout(() => setFeedback(""), 5000);
    };

    const handleFileUpload = (e) => {
        setFile(e.target.files[0]);
        setFeedback("📁 File selected. Ready to import.");
    };

    const createSlot = async () => {
        const payload = {
            doctorId: "HMS-DOC86a65e4f-34e4-43b8-b1d8-ebe488fdc582",
            date: selectedDate,
            startTime: startTime,
            endTime: endTime,
            slotDurationInMinutes: duration,
            repeatOn: repeatOn,
            slotType: slotType,
            tag: tag,
            note: note,
            colorCode: colorCode,
            recurrencePattern: ""
        };

        try {
            const response = await fetch("http://localhost:8080/api/slots", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            if (!response.ok) throw new Error("Slot creation failed");

            const result = await response.text();
            console.log("Slot creation result = ", result);
            handleSubmit(result);

        } catch (err) {
            handleSubmitMessage("Slot operation failed");
            console.error(err);
        }
        handleSubmit();
    };

    const bulkCreateSlot = async () => {
        const payload = {
            doctorId: "HMS-DOC86a65e4f-34e4-43b8-b1d8-ebe488fdc582",
            startDate: bulkStartDate,
            endDate: bulkEndDate,
            startTime: startTime,
            endTime: endTime,
            slotDurationInMinutes: duration,
            repeatOn: repeatOn,
            slotType: slotType,
            tag: tag,
            note: note,
            colorCode: colorCode,
        };

        try {
            const response = await fetch("http://localhost:8080/api/slots/recurring", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            if (!response.ok) throw new Error("Bulk creating slots failed");

            const result = await response.text();
            console.log("Result = ", result);
            handleSubmit(result);

        } catch (err) {
            setFeedback("Bulk slot operation failed");
            setTimeout(() => setFeedback(""), 3000);
            console.error(err);
        }
    };

    const updateSlot = async () => {
        const payload = {
            date: selectedDate,
            startTime: startTime,
            endTime: endTime,
        };

        // "HMS-GENb0e8af0a-596c-4fc7-ab03-56b5da7bd5be"
        try {
            const response = await fetch("http://localhost:8080/api/slots/HMS-GENb0e8af0a-596c-4fc7-ab03-56b5da7bd5be", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            if (!response.ok) throw new Error("Updating slot failed");

            const result = await response.text();
            console.log("Result = ", result);
            handleSubmit(result);

        } catch (err) {
            setFeedback("Slot update operation failed");
            setTimeout(() => setFeedback(""), 3000);
            console.error(err);
        }
        handleSubmit();
    };

    const bulkUpdateSlot = async () => {
        const payload = {
            date: selectedDate,
            startDate: bulkStartDate,
            endDate: bulkEndDate,
        };


        try {
            const response = await fetch("http://localhost:8080/api/slots/bulk-update-duration", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            if (!response.ok) throw new Error("Bulk Updating slot failed");

            const result = await response.text();
            console.log("Result = ", result);
            handleSubmit(result);

        } catch (err) {
            setFeedback("Bulk slot update operation failed");
            setTimeout(() => setFeedback(""), 3000);
            console.error(err);
        }
        handleSubmit();
    }

    const deleteSlot = async () => {
        try {
            const response = await fetch("http://localhost:8080/api/slots/HMS-GENb0e8af0a-596c-4fc7-ab03-56b5da7bd5be", {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                },
            });

            if (!response.ok) throw new Error("Deleting slot failed");

            const result = await response.text();
            console.log("Result = ", result);
            handleSubmit(result);

        } catch (err) {
            setFeedback("Slot delete operation failed");
            setTimeout(() => setFeedback(""), 3000);
            console.error(err);
        }
        handleSubmit();
    }

    const bulkDeleteSlot = async () => {
        const payload = {
            doctorId: "HMS-DOC86a65e4f-34e4-43b8-b1d8-ebe488fdc582",
            startDate: bulkStartDate,
            endDate: bulkEndDate,
        };
        try {
            const response = await fetch("http://localhost:8080/api/slots/bulk-delete", {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            if (!response.ok) throw new Error("Deleting bulk slot failed");

            const result = await response.text();
            console.log("Result = ", result);
            handleSubmit(result);

        } catch (err) {
            setFeedback("Bulk slot deleting operation failed");
            setTimeout(() => setFeedback(""), 3000);
            console.error(err);
        }
        handleSubmit();
    }

    const importCSV = async () => {
        if (!file) {
            handleSubmitMessage("❌ Please select a CSV file first.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await fetch("http://localhost:8080/api/slots/import-csv", {
                method: "POST",
                body: formData,
            });

            if (!response.ok) throw new Error("Upload failed");

            const result = await response.text();
            setFeedback(`✅ ${result}`);
        } catch (err) {
            console.error(err);
            handleSubmitMessage("❌ Failed to import CSV.");
        }
    }

    const toggleDay = (day) => {
        setRepeatOn(prev =>
            prev.includes(day)
                ? prev.filter(d => d !== day)
                : [...prev, day]
        );
    };

    const renderForm = () => {
        switch (activeTab) {
            case "create":
                return (
                    <div className="space-y-4">
                        <label className="block">
                            Date :
                            <DatePicker
                                selected={selectedDate}
                                onChange={(date) => setSelectedDate(date)}
                                className="border rounded p-2 w-full"
                                dateFormat="yyyy-MM-dd"
                            />
                        </label>
                        <label className="block">
                            Start Time :
                            <input
                                type="time"
                                value={startTime}
                                onChange={(e) => setStartTime(e.target.value)}
                                className="border rounded p-2 w-full"
                            />
                        </label>
                        <label className="block">
                            End Time :
                            <input
                                type="time"
                                value={endTime}
                                onChange={(e) => setEndTime(e.target.value)}
                                className="border rounded p-2 w-full"
                            />
                        </label>
                        <label className="block">
                            Slot Type:
                            <select value={slotType} onChange={(e) => setSlotType(e.target.value)} className="border rounded p-2 w-full">
                                <option value="">-- Select Slot Type --</option>
                                <option value="CONSULTATION">Consultation</option>
                                <option value="SURGERY">Surgery</option>
                                <option value="BREAK">Break</option>
                            </select>
                        </label>
                        <button
                            onClick={createSlot}
                            className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
                        >
                            Create Slot
                        </button>
                    </div>
                );
            case "bulkCreate":
                return (
                    <div className="space-y-4">
                        <label className="block">
                            Start Date :
                            <DatePicker
                                selected={bulkStartDate}
                                onChange={(date) => setBulkStartDate(date)}
                                className="border rounded p-2 w-full"
                                dateFormat="yyyy-MM-dd"
                            />
                        </label>
                        <label className="block">
                            End Date :
                            <DatePicker
                                selected={bulkEndDate}
                                onChange={(date) => setBulkEndDate(date)}
                                className="border rounded p-2 w-full"
                                dateFormat="yyyy-MM-dd"
                            />
                        </label>
                        <label className="block">
                            Start Time :
                            <input
                                type="time"
                                value={startTime}
                                onChange={(e) => setStartTime(e.target.value)}
                                className="border rounded p-2 w-full"
                            />
                        </label>
                        <label className="block">
                            End Time :
                            <input
                                type="time"
                                value={endTime}
                                onChange={(e) => setEndTime(e.target.value)}
                                className="border rounded p-2 w-full"
                            />
                        </label>
                        <label className="block">
                            Duration (minutes):
                            <input
                                type="number"
                                value={duration}
                                onChange={(e) => setDuration(Number(e.target.value))}
                                className="border rounded p-2 w-full"
                            />
                        </label>
                        <label className="block">
                            Repeat On :
                            <div className="flex flex-wrap gap-2">
                                {["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"].map(day => (
                                    <button
                                        key={day}
                                        type="button"
                                        onClick={() => toggleDay(day)}
                                        className={`px-3 py-1 rounded ${repeatOn.includes(day) ? "bg-blue-600 text-white" : "bg-gray-200"}`}
                                    >
                                        {day.substring(0, 3)}
                                    </button>
                                ))}
                            </div>
                        </label>
                        <label className="block">
                            Slot Type:
                            <select value={slotType} onChange={(e) => setSlotType(e.target.value)} className="border rounded p-2 w-full">
                                <option value="">-- Select Slot Type --</option>
                                <option value="CONSULTATION">Consultation</option>
                                <option value="SURGERY">Surgery</option>
                                <option value="BREAK">Break</option>
                            </select>
                        </label>
                        <input type="text" placeholder="Tag" value={tag} onChange={(e) => setTag(e.target.value)} className="border rounded p-2 w-full" />
                        <textarea placeholder="Note" value={note} onChange={(e) => setNote(e.target.value)} className="border rounded p-2 w-full" />
                        <label className="block">
                            Color Code :
                            <input type="color" value={colorCode} onChange={(e) => setColorCode(e.target.value)} className="w-16 h-10 border rounded" />
                        </label>
                        <button
                            onClick={bulkCreateSlot}
                            className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
                        >
                            Bulk Create
                        </button>
                    </div>
                );
            case "update":
                return (
                    <div className="space-y-4">
                        <input type="text" placeholder="Slot ID" className="border rounded p-2 w-full" />
                        <input type="time" placeholder="New Start Time" className="border rounded p-2 w-full" />
                        <input type="time" placeholder="New End Time" className="border rounded p-2 w-full" />
                        <button
                            onClick={updateSlot}
                            className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600"
                        >
                            Update Slot
                        </button>
                    </div>
                );
            case "bulkUpdate":
                return (
                    <div className="space-y-4">
                        <label className="block">
                            Start Date :
                            <DatePicker
                                selected={bulkStartDate}
                                onChange={(date) => setBulkStartDate(date)}
                                className="border rounded p-2 w-full"
                                dateFormat="yyyy-MM-dd"
                            />
                        </label>
                        <label className="block">
                            End Date :
                            <DatePicker
                                selected={bulkEndDate}
                                onChange={(date) => setBulkEndDate(date)}
                                className="border rounded p-2 w-full"
                                dateFormat="yyyy-MM-dd"
                            />
                        </label>
                        <label className="block">
                            New Duration (minutes):
                            <input
                                type="number"
                                value={duration}
                                defaultValue={15}
                                onChange={(e) => setDuration(e.target.value)}
                                className="border rounded p-2 w-full"
                            />
                        </label>
                        <button
                            onClick={bulkUpdateSlot}
                            className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700"
                        >
                            Bulk Update Duration
                        </button>
                    </div>
                );
            case "delete":
                return (
                    <div className="space-y-4">
                        <input type="text" placeholder="Slot ID to Delete" className="border rounded p-2 w-full" />
                        <button
                            onClick={deleteSlot}
                            className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700"
                        >
                            Delete Slot
                        </button>
                    </div>
                );

            case "bulkDelete":
                return (
                    <div className="space-y-4">
                        <input
                            type="text"
                            placeholder="Doctor ID"
                            className="border rounded p-2 w-full"
                        />
                        <h3 style={{ textAlign: "center" }}>---Or Date Range---</h3>
                        <div style={{ display: "flex", justifyContent: "space-between" }}>
                            <label className="block">
                                Start Date :
                                <DatePicker
                                    selected={bulkStartDate}
                                    onChange={(date) => setBulkStartDate(date)}
                                    className="border rounded p-2 w-full"
                                    dateFormat="yyyy-MM-dd"
                                />
                            </label>
                            <label className="block">
                                End Date :
                                <DatePicker
                                    selected={bulkEndDate}
                                    onChange={(date) => setBulkEndDate(date)}
                                    className="border rounded p-2 w-full"
                                    dateFormat="yyyy-MM-dd"
                                />
                            </label>
                        </div>

                        <button
                            onClick={bulkDeleteSlot}
                            className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                        >
                            Bulk Delete Slots
                        </button>
                    </div>
                );
            case "import":
                return (
                    <div className="space-y-4">
                        <input
                            type="file"
                            onChange={handleFileUpload}
                            className="block w-full text-sm text-gray-500"
                        />
                        <button
                            onClick={importCSV}
                            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
                        >
                            Import CSV
                        </button>
                    </div>
                );
            default:
                return null;
        }
    };

    return (
        <div>
            <nav className="bg-white border-gray-200 dark:bg-gray-900">
                <div className="flex flex-wrap justify-between items-center mx-auto max-w-screen-xl p-4">
                    <a href="#" className="flex items-center space-x-3 rtl:space-x-reverse">
                        <img src="https://flowbite.com/docs/images/logo.svg" className="h-8" alt="Flowbite Logo" />
                        <span className="self-center text-2xl font-semibold whitespace-nowrap dark:text-white">Admin</span>
                    </a>

                    <div className="flex items-center space-x-6 rtl:space-x-reverse">
                        <a href="tel:5541251234" className="text-sm text-gray-500 dark:text-white hover:underline">
                            (555) 412-1234
                        </a>

                        {/* Avatar Dropdown */}
                        <div className="relative group">
                            <img
                                src="https://i.pravatar.cc/40?img=12" // Replace with user's image if available
                                className="w-10 h-10 rounded-full cursor-pointer border-2 border-blue-500"
                                alt="User Avatar"
                            />
                            <div className="absolute right-0 mt-1 w-64 bg-white rounded-md shadow-lg p-4 z-50 hidden group-hover:block">
                                <div className="text-center">
                                    <img
                                        src="https://i.pravatar.cc/80?img=12"
                                        className="w-16 h-16 rounded-full mx-auto mb-2 border"
                                        alt="User Avatar Large"
                                    />
                                    <h4 className="text-lg font-semibold text-gray-800">Atabur Rahaman</h4>
                                    <p className="text-sm text-gray-500">rehman.khan@easyatech.com</p>
                                </div>
                                <div className="mt-4 space-y-2">
                                    <button className="w-full text-left px-4 py-2 hover:bg-gray-100 rounded-md text-sm text-gray-700">
                                        Edit Profile
                                    </button>
                                    <button className="w-full text-left px-4 py-2 hover:bg-gray-100 rounded-md text-sm text-gray-700">
                                        Settings
                                    </button>
                                    <button className="w-full text-left px-4 py-2 hover:bg-gray-100 rounded-md text-sm text-red-500">
                                        Logout
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </nav>

            {/* Landing page */}
            <div className="p-6 max-w-4xl mx-auto space-y-6">
                <h2 className="text-2xl font-semibold text-center text-gray-800">🩺 Admin Slot Management</h2>

                <div className="flex flex-wrap justify-center gap-2">
                    {["create", "bulkCreate", "update", "bulkUpdate", "delete", "bulkDelete", "import"].map((tab) => (
                        <button
                            key={tab}
                            onClick={() => setActiveTab(tab)}
                            className={`px-4 py-2 rounded ${activeTab === tab
                                ? "bg-blue-600 text-white"
                                : "bg-gray-200 text-gray-800 hover:bg-blue-100"}`}
                        >
                            {tab.charAt(0).toUpperCase() + tab.slice(1).replace("bulk", "Bulk ")}
                        </button>
                    ))}
                </div>

                <div className="bg-white p-6 rounded-lg shadow">{renderForm()}</div>

                {feedback && (
                    <div className="fixed bottom-4 right-4 bg-green-100 border border-green-300 text-green-800 px-4 py-2 rounded shadow-lg transition-all duration-300 z-50">
                        {feedback}
                    </div>
                )}

            </div>
        </div>
    );
}

export default AdminSlotPage;
