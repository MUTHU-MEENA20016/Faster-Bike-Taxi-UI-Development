// import React, {
//   useEffect,
//   useState,
// } from "react";

// import {
//   Link,
// } from "react-router-dom";

// import {
//   useAuth,
// } from "../context/AuthContext";

// import {
//   apiRequest,
// } from "../services/api";


// function DriverDashboard() {

//   const [applications, setApplications] =
//     useState([]);

//   const [loading, setLoading] =
//     useState(true);

//   const [error, setError] =
//     useState("");


//   useEffect(() => {

//     apiRequest(
//       "/api/applications/my"
//     )

//       .then((data) => {

//         setApplications(
//           Array.isArray(data)
//             ? data
//             : []
//         );

//       })

//       .catch((e) => {

//         setError(
//           e.message ||
//           "Unable to load your application."
//         );

//       })

//       .finally(() => {

//         setLoading(false);

//       });

//   }, []);


//   const app =
//     applications[0];

//   const status =
//     app?.status ||
//     "NOT SUBMITTED";


//   const docsStatus =
//     app
//       ? "Submitted for verification"
//       : "Not submitted";


//   return (

//     <main className="dashboard-page">

//       <div className="dashboard-header">

//         <div>

//           <span className="eyebrow">
//             DRIVER PORTAL
//           </span>

//           <h2>
//             My Dashboard
//           </h2>

//           <p>
//             Track your onboarding application
//             and documents.
//           </p>

//         </div>

//         <span className="role-pill">
//           DRIVER
//         </span>

//       </div>


//       {loading ? (

//         <div className="empty-state">

//           <div>

//             <div className="spinner" />

//             Loading your application...

//           </div>

//         </div>

//       ) : error ? (

//         <div className="notice error">
//           {error}
//         </div>

//       ) : (

//         <>

//           <div className="stats-grid">

//             <div className="stat-card">

//               <span className="stat-icon">
//                 🪪
//               </span>

//               <div>

//                 <small>
//                   Application
//                 </small>

//                 <strong>
//                   {status.replaceAll(
//                     "_",
//                     " "
//                   )}
//                 </strong>

//               </div>

//             </div>


//             <div className="stat-card">

//               <span className="stat-icon">
//                 🏍️
//               </span>

//               <div>

//                 <small>
//                   Bike
//                 </small>

//                 <strong>
//                   {app?.bikeNumber ||
//                     "Not added"}
//                 </strong>

//               </div>

//             </div>


//             <div className="stat-card">

//               <span className="stat-icon">
//                 📄
//               </span>

//               <div>

//                 <small>
//                   Documents
//                 </small>

//                 <strong>
//                   {docsStatus}
//                 </strong>

//               </div>

//             </div>

//           </div>


//           {!app ? (

//             <div className="dashboard-card role-dashboard">

//               <div className="feature-icon">
//                 🏍️
//               </div>

//               <h3>
//                 Start your driver application
//               </h3>

//               <p>
//                 Submit your details and four
//                 required documents to begin
//                 verification.
//               </p>

//             </div>

//           ) : (

//             <>

//               <div className="dashboard-card">

//                 <div className="card-heading">

//                   <h3>
//                     Application Overview
//                   </h3>

//                   <p>
//                     Your submitted information
//                     and current verification stage.
//                   </p>

//                 </div>


//                 <div className="application-row">

//                   <div>

//                     <span>
//                       Applicant
//                     </span>

//                     <strong>
//                       {app.name}
//                     </strong>

//                   </div>


//                   <div>

//                     <span>
//                       Phone
//                     </span>

//                     <strong>
//                       {app.phoneNumber}
//                     </strong>

//                   </div>


//                   <div>

//                     <span>
//                       Bike
//                     </span>

//                     <strong>
//                       {app.bikeNumber}
//                     </strong>

//                   </div>


//                   <div>

//                     <span>
//                       Status
//                     </span>

//                     <strong className="status-badge">
//                       {status.replaceAll(
//                         "_",
//                         " "
//                       )}
//                     </strong>

//                   </div>

//                 </div>


//                 {status === "REJECTED" &&
//                   app.rejectionReason && (

//                     <div
//                       className="notice error"
//                       style={{
//                         marginTop: "20px",
//                       }}
//                     >

//                       <strong>
//                         Application rejected
//                       </strong>

//                       <p
//                         style={{
//                           marginBottom: 0,
//                         }}
//                       >
//                         {app.rejectionReason}
//                       </p>

//                       <p
//                         style={{
//                           marginBottom: 0,
//                           marginTop: "8px",
//                         }}
//                       >
//                         Please replace the
//                         rejected document from
//                         the Documents page.
//                       </p>

//                     </div>

//                   )}


//                 <div className="page-actions">

//                   <Link
//                     className="secondary-button"
//                     to="/bikedetails"
//                   >
//                     View Bike Details
//                   </Link>

//                   <Link
//                     className="secondary-button"
//                     to="/documents"
//                   >
//                     View Documents
//                   </Link>

//                   <Link
//                     className="secondary-button"
//                     to="/notifications"
//                   >
//                     Notifications
//                   </Link>

//                 </div>

//               </div>

//             </>

//           )}

//         </>

//       )}

//     </main>

//   );
// }


// function ManagementDashboard({ role }) {
//   const isAdmin = role === "ADMIN";

//   const [users, setUsers] = useState([]);
//   const [applications, setApplications] = useState([]);
//   const [loading, setLoading] = useState(isAdmin);
//   const [error, setError] = useState("");

//   useEffect(() => {
//     if (!isAdmin) {
//       return;
//     }

//     const loadAdminData = async () => {
//       try {
//         setError("");

//         const [userData, applicationData] =
//           await Promise.all([
//             apiRequest("/api/users"),
//             apiRequest("/api/applications"),
//           ]);

//         setUsers(
//           Array.isArray(userData)
//             ? userData
//             : []
//         );

//         setApplications(
//           Array.isArray(applicationData)
//             ? applicationData
//             : []
//         );
//       } catch (e) {
//         setError(
//           e.message ||
//           "Unable to load admin data."
//         );
//       } finally {
//         setLoading(false);
//       }
//     };

//     loadAdminData();
//   }, [isAdmin]);

//   const totalUsers = users.length;

//   const totalAdmins = users.filter(
//     (user) => user.role === "ADMIN"
//   ).length;

//   const totalManagers = users.filter(
//     (user) => user.role === "MANAGER"
//   ).length;

//   const totalDrivers = users.filter(
//     (user) => user.role === "DRIVER"
//   ).length;

//   const totalApplications =
//     applications.length;

//   const submittedApplications =
//     applications.filter(
//       (application) =>
//         application.status === "SUBMITTED"
//     ).length;

//   const approvedApplications =
//     applications.filter(
//       (application) =>
//         application.status === "APPROVED"
//     ).length;

//   const rejectedApplications =
//     applications.filter(
//       (application) =>
//         application.status === "REJECTED"
//     ).length;

//   const cards = isAdmin
//     ? [
//         [
//           "📋",
//           "Applications",
//           "Review and manage driver applications.",
//           "/applications",
//         ],
//         [
//           "👥",
//           "Users",
//           "Manage driver, manager and admin accounts.",
//           "/users",
//         ],
//         [
//           "📄",
//           "Documents",
//           "Verify submitted applicant documents.",
//           "/documents",
//         ],
//         [
//           "🔔",
//           "Notifications",
//           "View onboarding notifications.",
//           "/notifications",
//         ],
//         [
//           "🧾",
//           "Audit Logs",
//           "Review system activity records.",
//           "/auditlogs",
//         ],
//       ]
//     : [
//         [
//           "📋",
//           "Applications",
//           "Review submitted driver applications.",
//           "/applications",
//         ],
//         [
//           "📄",
//           "Documents",
//           "Verify applicant documents.",
//           "/documents",
//         ],
//         [
//           "🔔",
//           "Notifications",
//           "View onboarding notifications.",
//           "/notifications",
//         ],
//       ];

//   return (
//     <main className="dashboard-page">

//       <div className="dashboard-header">
//         <div>
//           <span className="eyebrow">
//             MANAGEMENT PORTAL
//           </span>

//           <h2>
//             {role} Dashboard
//           </h2>

//           <p>
//             Manage the driver onboarding
//             workflow from one place.
//           </p>
//         </div>

//         <span className="role-pill">
//           {role}
//         </span>
//       </div>

//       {isAdmin && (
//         <>
//           {error && (
//             <div className="notice error">
//               {error}
//             </div>
//           )}

//           {loading ? (
//             <div className="empty-state">
//               <div>
//                 <div className="spinner" />
//                 Loading admin dashboard...
//               </div>
//             </div>
//           ) : (
//             <div className="stats-grid">

//               <div className="stat-card">
//                 <span className="stat-icon">
//                   👥
//                 </span>
//                 <div>
//                   <small>Total Users</small>
//                   <strong>
//                     {totalUsers}
//                   </strong>
//                 </div>
//               </div>

//               <div className="stat-card">
//                 <span className="stat-icon">
//                   👑
//                 </span>
//                 <div>
//                   <small>Admins</small>
//                   <strong>
//                     {totalAdmins}
//                   </strong>
//                 </div>
//               </div>

//               <div className="stat-card">
//                 <span className="stat-icon">
//                   🧑‍💼
//                 </span>
//                 <div>
//                   <small>Managers</small>
//                   <strong>
//                     {totalManagers}
//                   </strong>
//                 </div>
//               </div>

//               <div className="stat-card">
//                 <span className="stat-icon">
//                   🏍️
//                 </span>
//                 <div>
//                   <small>Drivers</small>
//                   <strong>
//                     {totalDrivers}
//                   </strong>
//                 </div>
//               </div>

//               <div className="stat-card">
//                 <span className="stat-icon">
//                   📋
//                 </span>
//                 <div>
//                   <small>Total Applications</small>
//                   <strong>
//                     {totalApplications}
//                   </strong>
//                 </div>
//               </div>

//               <div className="stat-card">
//                 <span className="stat-icon">
//                   ⏳
//                 </span>
//                 <div>
//                   <small>Submitted</small>
//                   <strong>
//                     {submittedApplications}
//                   </strong>
//                 </div>
//               </div>

//               <div className="stat-card">
//                 <span className="stat-icon">
//                   ✅
//                 </span>
//                 <div>
//                   <small>Approved</small>
//                   <strong>
//                     {approvedApplications}
//                   </strong>
//                 </div>
//               </div>

//               <div className="stat-card">
//                 <span className="stat-icon">
//                   ❌
//                 </span>
//                 <div>
//                   <small>Rejected</small>
//                   <strong>
//                     {rejectedApplications}
//                   </strong>
//                 </div>
//               </div>

//             </div>
//           )}
//         </>
//       )}

//       <div className="management-grid">

//         {cards.map(
//           ([
//             icon,
//             title,
//             text,
//             to,
//           ]) => (

//             <Link
//               className="management-link"
//               to={to}
//               key={title}
//             >
//               <div className="dashboard-card">

//                 <div className="feature-icon">
//                   {icon}
//                 </div>

//                 <h3>
//                   {title}
//                 </h3>

//                 <p>
//                   {text}
//                 </p>

//               </div>
//             </Link>

//           )
//         )}

//       </div>

//     </main>
//   );
// }

// export default function Dashboard() {

//   const { role } =
//     useAuth();

//   return role === "DRIVER"

//     ? <DriverDashboard />

//     : <ManagementDashboard
//         role={role || "USER"}
//       />;
// }
import React, {
  useEffect,
  useState,
} from "react";

import {
  Link,
} from "react-router-dom";

import {
  useAuth,
} from "../context/AuthContext";

import {
  apiRequest,
} from "../services/api";


function DriverDashboard() {

  const [applications, setApplications] =
    useState([]);

  const [loading, setLoading] =
    useState(true);

  const [error, setError] =
    useState("");


  useEffect(() => {

    apiRequest(
      "/api/applications/my"
    )

      .then((data) => {

        setApplications(
          Array.isArray(data)
            ? data
            : []
        );

      })

      .catch((e) => {

        setError(
          e.message ||
          "Unable to load your application."
        );

      })

      .finally(() => {

        setLoading(false);

      });

  }, []);


  const app =
    applications[0];

  const status =
    app?.status ||
    "NOT SUBMITTED";


  const docsStatus =
    app
      ? "Submitted for verification"
      : "Not submitted";


  return (

    <main className="dashboard-page">

      <div className="dashboard-header">

        <div>

          <span className="eyebrow">
            DRIVER PORTAL
          </span>

          <h2>
            My Dashboard
          </h2>

          <p>
            Track your onboarding application
            and documents.
          </p>

        </div>

        <span className="role-pill">
          DRIVER
        </span>

      </div>


      {loading ? (

        <div className="empty-state">

          <div>

            <div className="spinner" />

            Loading your application...

          </div>

        </div>

      ) : error ? (

        <div className="notice error">
          {error}
        </div>

      ) : (

        <>

          <div className="stats-grid">

            <div className="stat-card">

              <span className="stat-icon">
                🪪
              </span>

              <div>

                <small>
                  Application
                </small>

                <strong>
                  {status.replaceAll(
                    "_",
                    " "
                  )}
                </strong>

              </div>

            </div>


            <div className="stat-card">

              <span className="stat-icon">
                🏍️
              </span>

              <div>

                <small>
                  Bike
                </small>

                <strong>
                  {app?.bikeNumber ||
                    "Not added"}
                </strong>

              </div>

            </div>


            <div className="stat-card">

              <span className="stat-icon">
                📄
              </span>

              <div>

                <small>
                  Documents
                </small>

                <strong>
                  {docsStatus}
                </strong>

              </div>

            </div>

          </div>


          {!app ? (

            <div className="dashboard-card role-dashboard">

              <div className="feature-icon">
                🏍️
              </div>

              <h3>
                Start your driver application
              </h3>

              <p>
                Submit your details and four
                required documents to begin
                verification.
              </p>

            </div>

          ) : (

            <div className="dashboard-card">

              <div className="card-heading">

                <h3>
                  Application Overview
                </h3>

                <p>
                  Your submitted information
                  and current verification stage.
                </p>

              </div>


              <div className="application-row">

                <div>

                  <span>
                    Applicant
                  </span>

                  <strong>
                    {app.name}
                  </strong>

                </div>


                <div>

                  <span>
                    Phone
                  </span>

                  <strong>
                    {app.phoneNumber}
                  </strong>

                </div>


                <div>

                  <span>
                    Bike
                  </span>

                  <strong>
                    {app.bikeNumber}
                  </strong>

                </div>


                <div>

                  <span>
                    Status
                  </span>

                  <strong className="status-badge">
                    {status.replaceAll(
                      "_",
                      " "
                    )}
                  </strong>

                </div>

              </div>


              {status === "REJECTED" &&
                app.rejectionReason && (

                  <div
                    className="notice error"
                    style={{
                      marginTop: "20px",
                    }}
                  >

                    <strong>
                      Application rejected
                    </strong>

                    <p
                      style={{
                        marginBottom: 0,
                      }}
                    >
                      {app.rejectionReason}
                    </p>

                    <p
                      style={{
                        marginBottom: 0,
                        marginTop: "8px",
                      }}
                    >
                      Please replace the
                      rejected document from
                      the Documents page.
                    </p>

                  </div>

                )}


              <div className="page-actions">

                <Link
                  className="secondary-button"
                  to="/bikedetails"
                >
                  View Bike Details
                </Link>

                <Link
                  className="secondary-button"
                  to="/documents"
                >
                  View Documents
                </Link>

                <Link
                  className="secondary-button"
                  to="/notifications"
                >
                  Notifications
                </Link>

              </div>

            </div>

          )}

        </>

      )}

    </main>

  );
}


function ManagementDashboard({
  role,
}) {

  const isAdmin =
    role === "ADMIN";


  /*
   * Admin dashboard data
   *
   * These are read from the existing
   * users and applications APIs.
   *
   * No new backend API is required.
   */
  const [users, setUsers] =
    useState([]);

  const [applications, setApplications] =
    useState([]);

  const [loading, setLoading] =
    useState(isAdmin);

  const [error, setError] =
    useState("");


  useEffect(() => {

    /*
     * Manager dashboard does not
     * need admin API calls.
     */
    if (!isAdmin) {
      return;
    }


    const loadAdminData =
      async () => {

        try {

          setError("");


          const [
            userData,
            applicationData,
          ] = await Promise.all([

            apiRequest(
              "/api/users"
            ),

            apiRequest(
              "/api/applications"
            ),

          ]);


          setUsers(
            Array.isArray(userData)
              ? userData
              : []
          );


          setApplications(
            Array.isArray(
              applicationData
            )
              ? applicationData
              : []
          );


        } catch (e) {

          setError(
            e.message ||
            "Unable to load admin dashboard."
          );

        } finally {

          setLoading(false);

        }

      };


    loadAdminData();

  }, [isAdmin]);


  /*
   * USER COUNTS
   */

  const totalUsers =
    users.length;


  const totalAdmins =
    users.filter(
      (user) =>
        user.role === "ADMIN"
    ).length;


  const totalManagers =
    users.filter(
      (user) =>
        user.role === "MANAGER"
    ).length;


  const totalDrivers =
    users.filter(
      (user) =>
        user.role === "DRIVER"
    ).length;


  /*
   * APPLICATION COUNTS
   */

  const totalApplications =
    applications.length;


  const submittedApplications =
    applications.filter(
      (application) =>
        application.status === "SUBMITTED"
    ).length;


  const approvedApplications =
    applications.filter(
      (application) =>
        application.status === "APPROVED"
    ).length;


  const rejectedApplications =
    applications.filter(
      (application) =>
        application.status === "REJECTED"
    ).length;


  /*
   * EXISTING MANAGEMENT CARDS
   *
   * These are unchanged.
   */

  const cards = isAdmin
  ? [
      [
        "👥",
        "User Management",
        "Manage registered users and manager accounts.",
        "/users",
      ],
      [
        "🧾",
        "Audit Logs",
        "Review important system activity records.",
        "/auditlogs",
      ],
    ]
  : [
      [
        "📋",
        "Applications",
        "Review submitted driver applications.",
        "/applications",
      ],
      [
        "📄",
        "Documents",
        "Verify applicant documents.",
        "/documents",
      ],
      [
        "🔔",
        "Notifications",
        "View onboarding notifications.",
        "/notifications",
      ],
    ];


  return (

    <main className="dashboard-page">

      <div className="dashboard-header">

        <div>

          <span className="eyebrow">
            MANAGEMENT PORTAL
          </span>

          <h2>
            {role} Dashboard
          </h2>

          <p>
            Manage the driver onboarding
            workflow from one place.
          </p>

        </div>

        <span className="role-pill">
          {role}
        </span>

      </div>


      {isAdmin && (

        <>

          {error && (

            <div className="notice error">
              {error}
            </div>

          )}


          {loading ? (

            <div className="empty-state">

              <div>

                <div className="spinner" />

                Loading admin dashboard...

              </div>

            </div>

          ) : (

            <div className="stats-grid">


              {/* TOTAL USERS */}

              <div className="stat-card">

                <span className="stat-icon">
                  👥
                </span>

                <div>

                  <small>
                    Total Users
                  </small>

                  <strong>
                    {totalUsers}
                  </strong>

                </div>

              </div>


              {/* ADMINS */}

              <div className="stat-card">

                <span className="stat-icon">
                  👑
                </span>

                <div>

                  <small>
                    Admins
                  </small>

                  <strong>
                    {totalAdmins}
                  </strong>

                </div>

              </div>


              {/* MANAGERS */}

              <div className="stat-card">

                <span className="stat-icon">
                  🧑‍💼
                </span>

                <div>

                  <small>
                    Managers
                  </small>

                  <strong>
                    {totalManagers}
                  </strong>

                </div>

              </div>


              {/* DRIVERS */}

              <div className="stat-card">

                <span className="stat-icon">
                  🏍️
                </span>

                <div>

                  <small>
                    Drivers
                  </small>

                  <strong>
                    {totalDrivers}
                  </strong>

                </div>

              </div>


              {/* TOTAL APPLICATIONS */}

              <div className="stat-card">

                <span className="stat-icon">
                  📋
                </span>

                <div>

                  <small>
                    Total Applications
                  </small>

                  <strong>
                    {totalApplications}
                  </strong>

                </div>

              </div>


              {/* SUBMITTED */}

              <div className="stat-card">

                <span className="stat-icon">
                  ⏳
                </span>

                <div>

                  <small>
                    Submitted
                  </small>

                  <strong>
                    {submittedApplications}
                  </strong>

                </div>

              </div>


              {/* APPROVED */}

              <div className="stat-card">

                <span className="stat-icon">
                  ✅
                </span>

                <div>

                  <small>
                    Approved
                  </small>

                  <strong>
                    {approvedApplications}
                  </strong>

                </div>

              </div>


              {/* REJECTED */}

              <div className="stat-card">

                <span className="stat-icon">
                  ❌
                </span>

                <div>

                  <small>
                    Rejected
                  </small>

                  <strong>
                    {rejectedApplications}
                  </strong>

                </div>

              </div>


            </div>

          )}

        </>

      )}


      {/* EXISTING MANAGEMENT CARDS */}

      <div className="management-grid">

        {cards.map(
          ([
            icon,
            title,
            text,
            to,
          ]) => (

            <Link
              className="management-link"
              to={to}
              key={title}
            >

              <div className="dashboard-card">

                <div className="feature-icon">
                  {icon}
                </div>

                <h3>
                  {title}
                </h3>

                <p>
                  {text}
                </p>

              </div>

            </Link>

          )
        )}

      </div>

    </main>

  );
}


export default function Dashboard() {

  const {
    role,
  } = useAuth();


  return role === "DRIVER"

    ? <DriverDashboard />

    : <ManagementDashboard
        role={role || "USER"}
      />;

}