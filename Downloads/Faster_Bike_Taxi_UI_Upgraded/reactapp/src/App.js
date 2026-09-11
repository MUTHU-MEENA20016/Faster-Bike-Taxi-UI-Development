// import React from "react";
// import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
// import "./App.css";
// import { AuthProvider } from "./context/AuthContext";
// import Navbar from "./components/NavBar";
// import Footer from "./components/Footer";
// import Home from "./components/Home";
// import ApplyForm from "./components/ApplyForm";
// import DisplayBikes from "./components/DisplayBikes";
// import Login from "./components/Login";
// import Register from "./components/Register";
// import Dashboard from "./components/Dashboard";
// import Documents from "./components/Documents";
// import Applications from "./components/Applications";
// import Users from "./components/Users";
// import Notifications from "./components/Notifications";
// import AuditLogs from "./components/AuditLogs";
// import ProtectedRoute from "./components/ProtectedRoute";

// function App() {
//   return (
//     <AuthProvider>
//       <Router>
//         <div className="app-shell">
//           <Navbar />
//           <main className="page-content">
//             <Routes>
//               {/* Public Routes */}
//               <Route path="/" element={<Home />} />
//               <Route path="/login" element={<Login />} />
//               <Route path="/register" element={<Register />} />

//               {/* Driver Routes */}
//               <Route
//                 path="/apply"
//                 element={
//                   <ProtectedRoute allowedRoles={["DRIVER"]}>
//                     <ApplyForm />
//                   </ProtectedRoute>
//                 }
//               />
//               <Route
//                 path="/bikedetails"
//                 element={
//                   <ProtectedRoute allowedRoles={["DRIVER"]}>
//                     <DisplayBikes />
//                   </ProtectedRoute>
//                 }
//               />

//               {/* Authenticated General Routes */}
//               <Route
//                 path="/dashboard"
//                 element={
//                   <ProtectedRoute>
//                     <Dashboard />
//                   </ProtectedRoute>
//                 }
//               />
//               <Route
//                 path="/documents"
//                 element={
//                   <ProtectedRoute>
//                     <Documents />
//                   </ProtectedRoute>
//                 }
//               />
//               <Route
//                 path="/notifications"
//                 element={
//                   <ProtectedRoute>
//                     <Notifications />
//                   </ProtectedRoute>
//                 }
//               />

//               {/* Manager & Admin Routes */}
//               <Route
//                 path="/applications"
//                 element={
//                   <ProtectedRoute allowedRoles={["MANAGER", "ADMIN"]}>
//                     <Applications />
//                   </ProtectedRoute>
//                 }
//               />

//               {/* Admin Only Routes */}
//               <Route
//                 path="/users"
//                 element={
//                   <ProtectedRoute allowedRoles={["ADMIN"]}>
//                     <Users />
//                   </ProtectedRoute>
//                 }
//               />
//               <Route
//                 path="/auditlogs"
//                 element={
//                   <ProtectedRoute allowedRoles={["ADMIN"]}>
//                     <AuditLogs />
//                   </ProtectedRoute>
//                 }
//               />
//             </Routes>
//           </main>
//           <Footer />
//         </div>
//       </Router>
//     </AuthProvider>
//   );
// }

// export default App;
import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./App.css";

import { AuthProvider } from "./context/AuthContext";
import Navbar from "./components/NavBar";
import Footer from "./components/Footer";

import Home from "./components/Home";
import ApplyForm from "./components/ApplyForm";
import DisplayBikes from "./components/DisplayBikes";
import Login from "./components/Login";
import Register from "./components/Register";
import Dashboard from "./components/Dashboard";
import Documents from "./components/Documents";
import Applications from "./components/Applications";
import Users from "./components/Users";
import Notifications from "./components/Notifications";
import AuditLogs from "./components/AuditLogs";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="app-shell">
          <Navbar />

          <main className="page-content">
            <Routes>
              {/* Public pages */}
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />

              {/* Driver only */}
              <Route
                path="/apply"
                element={
                  <ProtectedRoute allowedRoles={["DRIVER"]}>
                    <ApplyForm />
                  </ProtectedRoute>
                }
              />

              <Route
                path="/bikedetails"
                element={
                  <ProtectedRoute allowedRoles={["DRIVER"]}>
                    <DisplayBikes />
                  </ProtectedRoute>
                }
              />

              {/* All authenticated users */}
              <Route
                path="/dashboard"
                element={
                  <ProtectedRoute>
                    <Dashboard />
                  </ProtectedRoute>
                }
              />

              <Route
                path="/notifications"
                element={
                  <ProtectedRoute>
                    <Notifications />
                  </ProtectedRoute>
                }
              />

              {/* Manager + Admin */}
              <Route
                path="/documents"
                element={
                  <ProtectedRoute allowedRoles={["MANAGER", "ADMIN", "DRIVER"]}>
                    <Documents />
                  </ProtectedRoute>
                }
              />

              <Route
                path="/applications"
                element={
                  <ProtectedRoute allowedRoles={["MANAGER", "ADMIN"]}>
                    <Applications />
                  </ProtectedRoute>
                }
              />

              {/* Admin only */}
              <Route
                path="/users"
                element={
                  <ProtectedRoute allowedRoles={["ADMIN"]}>
                    <Users />
                  </ProtectedRoute>
                }
              />

              <Route
                path="/auditlogs"
                element={
                  <ProtectedRoute allowedRoles={["ADMIN"]}>
                    <AuditLogs />
                  </ProtectedRoute>
                }
              />
            </Routes>
          </main>

          <Footer />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;