import { BrowserRouter, Routes, Route } from "react-router-dom";
import PatientBooking from "./components/patient/PatientBooking";
import BookingSuccess from "./components/patient/BookingSuccess";
import AdminSlotManager from "./components/AdminSlotManager";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<PatientBooking />} />
        <Route path="/success" element={<BookingSuccess />} />
        <Route path="/admin-page" element={<AdminSlotManager />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
