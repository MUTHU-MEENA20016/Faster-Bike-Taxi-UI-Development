// import React, { useEffect, useState } from "react";
// import { apiRequest } from "../services/api";

// function Applications() {
//   const [rows, setRows] = useState([]);
//   const [error, setError] = useState("");
//   const [loading, setLoading] = useState(true);

//   const load = () =>
//     apiRequest("/api/applications")
//       .then(setRows)
//       .catch((e) => setError(e.message))
//       .finally(() => setLoading(false));

//   useEffect(load, []);

//   const update = async (id, status) => {
//     try {
//       await apiRequest(`/api/applications/${id}`, {
//         method: "PUT",
//         headers: { "Content-Type": "application/json" },
//         body: JSON.stringify({ status }),
//       });
//       load();
//     } catch (e) {
//       setError(e.message);
//     }
//   };

//   return (
//     <main className="table-page">
//       <div className="section-heading split-heading">
//         <div>
//           <span className="eyebrow">ONBOARDING WORKFLOW</span>
//           <h2>Applications</h2>
//           <p>Review submitted applications and move them through verification.</p>
//         </div>
//         <span className="count-badge">{rows.length} total</span>
//       </div>

//       {error && <div className="notice error">{error}</div>}

//       <div className="table-card">
//         <div className="table-scroll">
//           {loading ? (
//             <div className="empty-state">Loading applications...</div>
//           ) : (
//             <table>
//               <thead>
//                 <tr>
//                   <th>Applicant</th>
//                   <th>Phone</th>
//                   <th>Bike</th>
//                   <th>License</th>
//                   <th>Status</th>
//                   <th>Action</th>
//                 </tr>
//               </thead>
//               <tbody>
//                 {rows.map((a) => (
//                   <tr key={a.id}>
//                     <td>
//                       {a.name}
//                       <br />
//                       <small>{a.applicantUsername}</small>
//                     </td>
//                     <td>{a.phoneNumber}</td>
//                     <td>
//                       <span className="bike-tag">{a.bikeNumber}</span>
//                     </td>
//                     <td>{a.drivingLicenseNumber}</td>
//                     <td>
//                       <span className="status-badge">
//                         {String(a.status).replaceAll("_", " ")}
//                       </span>
//                     </td>
//                     <td>
//                       <div className="page-actions">
//                         <button
//                           className="action-button light"
//                           onClick={() => update(a.id, "DOCUMENT_VERIFICATION")}
//                         >
//                           Documents
//                         </button>
//                         <button
//                           className="action-button success"
//                           onClick={() => update(a.id, "APPROVED")}
//                         >
//                           Approve
//                         </button>
//                         <button
//                           className="action-button danger"
//                           onClick={() => update(a.id, "REJECTED")}
//                         >
//                           Reject
//                         </button>
//                       </div>
//                     </td>
//                   </tr>
//                 ))}
//               </tbody>
//             </table>
//           )}
//         </div>
//       </div>
//     </main>
//   );
// }

// export default Applications;
import React, { useEffect, useState } from "react";
import { apiRequest } from "../services/api";

function Applications() {
  const [rows, setRows] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const load = async () => {
    try {
      setLoading(true);
      setError("");

      const data = await apiRequest("/api/applications");
      setRows(Array.isArray(data) ? data : []);
    } catch (e) {
      setError(e.message || "Unable to load applications.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const update = async (id, status) => {
    let rejectionReason = "";

    if (status === "REJECTED") {
      rejectionReason =
        window.prompt(
          "Enter the rejection reason:",
          "Required information or documents need correction"
        ) || "";

      if (!rejectionReason.trim()) {
        return;
      }
    }

    try {
      await apiRequest(`/api/applications/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          status,
          rejectionReason: rejectionReason.trim(),
        }),
      });

      await load();
    } catch (e) {
      setError(e.message || "Unable to update application.");
    }
  };

  return (
    <main className="table-page">
      <div className="section-heading split-heading">
        <div>
          <span className="eyebrow">MANAGEMENT • ONBOARDING</span>

          <h2>Applications</h2>

          <p>
            Review driver applications and move them through the verification
            process.
          </p>
        </div>

        <span className="count-badge">{rows.length} total</span>
      </div>

      {error && <div className="notice error">{error}</div>}

      <div className="table-card">
        <div className="table-scroll">
          {loading ? (
            <div className="empty-state">Loading applications...</div>
          ) : rows.length === 0 ? (
            <div className="empty-state">
              No applications are available.
            </div>
          ) : (
            <table>
              <thead>
                <tr>
                  <th>Applicant</th>
                  <th>Phone</th>
                  <th>Bike</th>
                  <th>License</th>
                  <th>Status</th>
                  <th>Action</th>
                </tr>
              </thead>

              <tbody>
                {rows.map((application) => (
                  <tr key={application.id}>
                    <td>
                      {application.name}
                      <br />
                      <small>{application.applicantUsername}</small>
                    </td>

                    <td>{application.phoneNumber}</td>

                    <td>
                      <span className="bike-tag">
                        {application.bikeNumber}
                      </span>
                    </td>

                    <td>{application.drivingLicenseNumber}</td>

                    <td>
                      <span className="status-badge">
                        {String(application.status).replaceAll("_", " ")}
                      </span>
                    </td>

                    <td>
                      <div className="page-actions">
                        <button
                          className="action-button light"
                          onClick={() =>
                            update(
                              application.id,
                              "DOCUMENT_VERIFICATION"
                            )
                          }
                        >
                          Documents
                        </button>

                        <button
                          className="action-button success"
                          onClick={() =>
                            update(application.id, "APPROVED")
                          }
                        >
                          Approve
                        </button>

                        <button
                          className="action-button danger"
                          onClick={() =>
                            update(application.id, "REJECTED")
                          }
                        >
                          Reject
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </main>
  );
}

export default Applications;