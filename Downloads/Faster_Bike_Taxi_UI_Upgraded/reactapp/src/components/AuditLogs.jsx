// import React, { useEffect, useState } from "react";
// import { apiRequest } from "../services/api";

// function AuditLogs() {
//   const [rows, setRows] = useState([]);
//   const [error, setError] = useState("");

//   useEffect(() => {
//     apiRequest("/api/auditlogs")
//       .then(setRows)
//       .catch((e) => setError(e.message));
//   }, []);

//   return (
//     <main className="table-page">
//       <div className="section-heading">
//         <span className="eyebrow">SYSTEM SECURITY</span>
//         <h2>Audit Logs</h2>
//         <p>Track important system activity.</p>
//       </div>

//       {error && <div className="notice error">{error}</div>}

//       <div className="table-card">
//         <div className="table-scroll">
//           <table>
//             <thead>
//               <tr>
//                 <th>Action</th>
//                 <th>Entity</th>
//                 <th>Entity ID</th>
//                 <th>Timestamp</th>
//                 <th>IP</th>
//               </tr>
//             </thead>
//             <tbody>
//               {rows.map((a) => (
//                 <tr key={a.id}>
//                   <td>{a.action}</td>
//                   <td>{a.entityType}</td>
//                   <td>{a.entityId || "—"}</td>
//                   <td>
//                     {a.timestamp
//                       ? new Date(a.timestamp).toLocaleString()
//                       : "—"}
//                   </td>
//                   <td>{a.ipAddress || "—"}</td>
//                 </tr>
//               ))}
//             </tbody>
//           </table>
//         </div>
//       </div>
//     </main>
//   );
// }

// export default AuditLogs;
import React, { useEffect, useState } from "react";
import { apiRequest } from "../services/api";

function AuditLogs() {
  const [rows, setRows] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    apiRequest("/api/auditlogs")
      .then((data) => {
        setRows(Array.isArray(data) ? data : []);
      })
      .catch((e) => {
        setError(
          e.message || "Unable to load audit logs."
        );
      });
  }, []);

  return (
    <main className="table-page">

      <div className="section-heading">
        <span className="eyebrow">
          SYSTEM SECURITY
        </span>

        <h2>Audit Logs</h2>

        <p>
          Track important application activity.
        </p>
      </div>

      {error && (
        <div className="notice error">
          {error}
        </div>
      )}

      <div className="table-card">
        <div className="table-scroll">

          <table>

            <thead>
              <tr>
                <th>Applicant ID</th>
                <th>Applicant</th>
                <th>Action</th>
                <th>Timestamp</th>
              </tr>
            </thead>

            <tbody>

              {rows.length === 0 ? (

                <tr>
                  <td
                    colSpan="4"
                    style={{
                      textAlign: "center",
                      padding: "30px",
                      color: "#667085",
                    }}
                  >
                    No audit activity yet.
                  </td>
                </tr>

              ) : (

                rows.map((audit) => (

                  <tr key={audit.id}>

                    <td>
                      {audit.entityId || "—"}
                    </td>

                    <td>
                      {audit.newValue || "—"}
                    </td>

                    <td>
                      {audit.action || "—"}
                    </td>

                    <td>
                      {audit.timestamp
                        ? new Date(
                            audit.timestamp
                          ).toLocaleString()
                        : "—"}
                    </td>

                  </tr>

                ))

              )}

            </tbody>

          </table>

        </div>
      </div>

    </main>
  );
}

export default AuditLogs;