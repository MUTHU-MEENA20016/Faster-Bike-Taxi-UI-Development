// import React, { useEffect, useState } from "react";
// import { apiRequest } from "../services/api";

// function Notifications() {
//   const [rows, setRows] = useState([]);
//   const [error, setError] = useState("");

//   useEffect(() => {
//     apiRequest("/api/notifications")
//       .then(setRows)
//       .catch((e) => setError(e.message));
//   }, []);

//   return (
//     <main className="table-page">
//       <div className="section-heading">
//         <span className="eyebrow">UPDATES</span>
//         <h2>Notifications</h2>
//         <p>Important onboarding and verification updates.</p>
//       </div>

//       {error && <div className="notice error">{error}</div>}

//       <div className="notification-list">
//         {rows.length === 0 ? (
//           <div className="table-card">
//             <div className="empty-state">No notifications available.</div>
//           </div>
//         ) : (
//           rows.map((n) => (
//             <article
//               className={`notification-card ${n.isRead === false ? "unread" : ""}`}
//               key={n.id}
//             >
//               <div>
//                 <strong>{n.type || "INFO"}</strong>
//                 <p>{n.message}</p>
//               </div>
//               <span className="status-badge">{n.priority || "MEDIUM"}</span>
//             </article>
//           ))
//         )}
//       </div>
//     </main>
//   );
// }

// export default Notifications;
import React, { useEffect, useState } from "react";
import { apiRequest } from "../services/api";

function Notifications() {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadNotifications = async () => {
      try {
        setLoading(true);

        const data = await apiRequest("/api/notifications");

        setNotifications(Array.isArray(data) ? data : []);
      } catch (e) {
        setError(e.message || "Unable to load notifications.");
      } finally {
        setLoading(false);
      }
    };

    loadNotifications();
  }, []);

  return (
    <main className="table-page">
      <div className="section-heading">
        <span className="eyebrow">UPDATES</span>

        <h2>Notifications</h2>

        <p>
          Important updates about your application and verification process.
        </p>
      </div>

      {error && <div className="notice error">{error}</div>}

      {loading ? (
        <div className="empty-state">Loading notifications...</div>
      ) : notifications.length === 0 ? (
        <div className="empty-state">
          <div className="feature-icon">✓</div>
          <h3>No notifications yet</h3>
          <p>
            You will receive an update when your application status changes.
          </p>
        </div>
      ) : (
        <div className="notification-list">
          {notifications.map((notification) => (
            <article
              key={notification.id}
              className={`notification-card ${
                notification.isRead ? "" : "unread"
              }`}
            >
              <div className="notification-header">
                <span className="eyebrow">
                  {notification.type || "UPDATE"}
                </span>

                {notification.priority && (
                  <span className="status-badge">
                    {notification.priority}
                  </span>
                )}
              </div>

              <p>{notification.message}</p>
            </article>
          ))}
        </div>
      )}
    </main>
  );
}

export default Notifications;