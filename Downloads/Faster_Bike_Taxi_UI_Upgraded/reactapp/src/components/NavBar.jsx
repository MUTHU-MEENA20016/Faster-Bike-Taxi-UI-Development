// // import React, {
// //   useEffect,
// //   useState,
// // } from "react";

// // import {
// //   Link,
// // } from "react-router-dom";

// // import {
// //   useAuth,
// // } from "../context/AuthContext";

// // import {
// //   apiRequest,
// // } from "../services/api";


// // function Navbar() {

// //   const {
// //     token,
// //     role,
// //     logoutUser,
// //   } = useAuth();


// //   const [hasApplied, setHasApplied] =
// //     useState(false);


// //   useEffect(() => {

// //     const checkApplication =
// //       async () => {

// //         if (!token ||
// //             role !== "DRIVER") {

// //           setHasApplied(false);

// //           return;
// //         }


// //         try {

// //           const applications =
// //             await apiRequest(
// //               "/api/applications/my"
// //             );


// //           const alreadyApplied =
// //             Array.isArray(
// //               applications
// //             ) &&
// //             applications.length > 0;


// //           setHasApplied(
// //             alreadyApplied
// //           );

// //         } catch (error) {

// //           setHasApplied(false);
// //         }
// //       };


// //     checkApplication();


// //     const handleApplicationSubmitted =
// //       () => {

// //         setHasApplied(true);
// //       };


// //     window.addEventListener(
// //       "applicationSubmitted",
// //       handleApplicationSubmitted
// //     );


// //     return () => {

// //       window.removeEventListener(
// //         "applicationSubmitted",
// //         handleApplicationSubmitted
// //       );

// //     };

// //   }, [token, role]);


// //   return (

// //     <header className="site-header">

// //       <nav className="navbar">

// //         <Link
// //           className="brand"
// //           to="/"
// //         >

// //           <span className="brand-mark">
// //             F
// //           </span>

// //           <span>
// //             <strong>
// //               Faster
// //             </strong>{" "}
// //             Bike Taxi
// //           </span>

// //         </Link>


// //         <div className="nav-links">

// //           <Link
// //             className="nav-link"
// //             to="/"
// //           >
// //             Home
// //           </Link>


// //           {token && (

// //             <Link
// //               className="nav-link"
// //               to="/bikedetails"
// //             >
// //               Bike Details
// //             </Link>

// //           )}


// //           {!token && (

// //             <>

// //               <Link
// //                 className="nav-link"
// //                 to="/login"
// //               >
// //                 Login
// //               </Link>


// //               <Link
// //                 className="nav-cta"
// //                 to="/register"
// //               >
// //                 Register
// //               </Link>

// //             </>

// //           )}


// //           {token && (

// //             <>

// //               {role === "DRIVER" &&
// //                 !hasApplied && (

// //                 <Link
// //                   className="nav-cta"
// //                   to="/apply"
// //                 >
// //                   Apply Now
// //                 </Link>

// //               )}


// //               {role === "DRIVER" &&
// //                 hasApplied && (

// //                 <Link
// //                   className="nav-link"
// //                   to="/documents"
// //                 >
// //                   Documents
// //                 </Link>

// //               )}


// //               <Link
// //                 className="nav-link"
// //                 to="/dashboard"
// //               >
// //                 {role} Dashboard
// //               </Link>


// //               <button
// //                 className="logout-button"
// //                 onClick={logoutUser}
// //               >
// //                 Logout
// //               </button>

// //             </>

// //           )}

// //         </div>

// //       </nav>

// //     </header>

// //   );
// // }


// // export default Navbar;
// import React from "react";
// import { Link, useNavigate } from "react-router-dom";
// import { useAuth } from "../context/AuthContext";

// function NavBar() {
//   const { token, role, logoutUser } = useAuth();
//   const navigate = useNavigate();

//   const handleLogout = () => {
//     logoutUser();
//     navigate("/login");
//   };

//   return (
//     <nav className="navbar">
//       <div className="nav-brand">
//         <Link to="/">Faster Bike Taxi</Link>
//       </div>

//       <div className="nav-links">
//         {!token ? (
//           <>
//             <Link to="/login">Login</Link>
//             <Link to="/register">Register</Link>
//           </>
//         ) : (
//           <>
//             {/* COMMON */}
//             <Link to="/dashboard">Dashboard</Link>

//             {/* DRIVER */}
//             {role === "DRIVER" && (
//               <>
//                 <Link to="/apply">Apply</Link>
//                 <Link to="/bikedetails">Bike Details</Link>
//                 <Link to="/documents">Documents</Link>
//                 <Link to="/notifications">Notifications</Link>
//               </>
//             )}

//             {/* MANAGER */}
//             {role === "MANAGER" && (
//               <>
//                 <Link to="/applications">Applications</Link>
//                 <Link to="/documents">Documents</Link>
//                 <Link to="/notifications">Notifications</Link>
//               </>
//             )}

//             {/* ADMIN */}
//             {role === "ADMIN" && (
//               <>
//                 <Link to="/users">User Management</Link>
//                 <Link to="/auditlogs">Audit Logs</Link>
//               </>
//             )}

//             {/* LOGOUT */}
//             <button
//               type="button"
//               className="nav-logout"
//               onClick={handleLogout}
//             >
//               Logout
//             </button>
//           </>
//         )}
//       </div>
//     </nav>
//   );
// }

// export default NavBar;
import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function NavBar() {
  const { token, role, logoutUser } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logoutUser();
    navigate("/login");
  };

  return (
    <nav className="navbar">
      <div className="nav-brand">
        <Link to="/">Faster Bike Taxi</Link>
      </div>

      <div className="nav-links">
        {!token ? (
  <>
    {location.pathname !== "/login" && (
      <Link to="/login" className="nav-link">
        Login
      </Link>
    )}

    {location.pathname !== "/register" && (
      <Link to="/register" className="nav-cta">
        Register
      </Link>
    )}
  </>
) : (
          <>
            {/* COMMON */}
            <Link to="/dashboard" className="nav-link">
              Dashboard
            </Link>

            {/* DRIVER */}
            {role === "DRIVER" && (
              <>
                <Link to="/apply" className="nav-link">
                  Apply
                </Link>

                <Link to="/bikedetails" className="nav-link">
                  Bike Details
                </Link>

                <Link to="/documents" className="nav-link">
                  Documents
                </Link>

                <Link to="/notifications" className="nav-link">
                  Notifications
                </Link>
              </>
            )}

            {/* MANAGER */}
            {role === "MANAGER" && (
              <>
                <Link to="/applications" className="nav-link">
                  Applications
                </Link>

                <Link to="/documents" className="nav-link">
                  Documents
                </Link>

                <Link to="/notifications" className="nav-link">
                  Notifications
                </Link>
              </>
            )}

            {/* ADMIN */}
            {role === "ADMIN" && (
              <>
                <Link to="/users" className="nav-link">
                  User Management
                </Link>

                <Link to="/auditlogs" className="nav-link">
                  Audit Logs
                </Link>
              </>
            )}

            {/* LOGOUT */}
            <button
              type="button"
              className="logout-button"
              onClick={handleLogout}
            >
              Logout
            </button>
          </>
        )}
      </div>
    </nav>
  );
}

export default NavBar;