import { Link } from "react-router-dom";

function BookingSuccess() {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-green-50 text-center">
      <h1 className="text-3xl font-bold text-green-600 mb-4">🎉 Booking Confirmed!</h1>
      <p className="text-gray-700 text-lg mb-6">Your appointment has been successfully scheduled.</p>
      <Link
        to="/"
        className="px-6 py-2 bg-purple-600 text-white rounded-md hover:bg-purple-700 transition"
      >
        Book Another
      </Link>
    </div>
  );
}

export default BookingSuccess;
