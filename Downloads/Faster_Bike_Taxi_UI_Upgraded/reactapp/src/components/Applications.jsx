// // import React, { useEffect, useState } from "react";
// // import { apiRequest } from "../services/api";

// // function Applications() {
// //   const [rows, setRows] = useState([]);
// //   const [error, setError] = useState("");
// //   const [loading, setLoading] = useState(true);

// //   const load = async () => {
// //     try {
// //       setLoading(true);
// //       setError("");

// //       const data = await apiRequest("/api/applications");
// //       setRows(Array.isArray(data) ? data : []);
// //     } catch (e) {
// //       setError(e.message || "Unable to load applications.");
// //     } finally {
// //       setLoading(false);
// //     }
// //   };

// //   useEffect(() => {
// //     load();
// //   }, []);

// //   const update = async (id, status) => {
// //     let rejectionReason = "";

// //     if (status === "REJECTED") {
// //       rejectionReason =
// //         window.prompt(
// //           "Enter the rejection reason:",
// //           "Required information or documents need correction"
// //         ) || "";

// //       if (!rejectionReason.trim()) {
// //         return;
// //       }
// //     }

// //     try {
// //       await apiRequest(`/api/applications/${id}`, {
// //         method: "PUT",
// //         headers: {
// //           "Content-Type": "application/json",
// //         },
// //         body: JSON.stringify({
// //           status,
// //           rejectionReason: rejectionReason.trim(),
// //         }),
// //       });

// //       await load();
// //     } catch (e) {
// //       setError(e.message || "Unable to update application.");
// //     }
// //   };

// //   return (
// //     <main className="table-page">
// //       <div className="section-heading split-heading">
// //         <div>
// //           <span className="eyebrow">MANAGEMENT • ONBOARDING</span>

// //           <h2>Applications</h2>

// //           <p>
// //             Review driver applications and move them through the verification
// //             process.
// //           </p>
// //         </div>

// //         <span className="count-badge">{rows.length} total</span>
// //       </div>

// //       {error && <div className="notice error">{error}</div>}

// //       <div className="table-card">
// //         <div className="table-scroll">
// //           {loading ? (
// //             <div className="empty-state">Loading applications...</div>
// //           ) : rows.length === 0 ? (
// //             <div className="empty-state">
// //               No applications are available.
// //             </div>
// //           ) : (
// //             <table>
// //               <thead>
// //                 <tr>
// //                   <th>Applicant</th>
// //                   <th>Phone</th>
// //                   <th>Bike</th>
// //                   <th>License</th>
// //                   <th>Status</th>
// //                   <th>Action</th>
// //                 </tr>
// //               </thead>

// //               <tbody>
// //                 {rows.map((application) => (
// //                   <tr key={application.id}>
// //                     <td>
// //                       {application.name}
// //                       <br />
// //                       <small>{application.applicantUsername}</small>
// //                     </td>

// //                     <td>{application.phoneNumber}</td>

// //                     <td>
// //                       <span className="bike-tag">
// //                         {application.bikeNumber}
// //                       </span>
// //                     </td>

// //                     <td>{application.drivingLicenseNumber}</td>

// //                     <td>
// //                       <span className="status-badge">
// //                         {String(application.status).replaceAll("_", " ")}
// //                       </span>
// //                     </td>

// //                     <td>
// //                       <div className="page-actions">
// //                         <button
// //                           className="action-button light"
// //                           onClick={() =>
// //                             update(
// //                               application.id,
// //                               "DOCUMENT_VERIFICATION"
// //                             )
// //                           }
// //                         >
// //                           Documents
// //                         </button>

// //                         <button
// //                           className="action-button success"
// //                           onClick={() =>
// //                             update(application.id, "APPROVED")
// //                           }
// //                         >
// //                           Approve
// //                         </button>

// //                         <button
// //                           className="action-button danger"
// //                           onClick={() =>
// //                             update(application.id, "REJECTED")
// //                           }
// //                         >
// //                           Reject
// //                         </button>
// //                       </div>
// //                     </td>
// //                   </tr>
// //                 ))}
// //               </tbody>
// //             </table>
// //           )}
// //         </div>
// //       </div>
// //     </main>
// //   );
// // }

// // export default Applications;
// import React, { useEffect, useState } from "react";
// import { useNavigate } from "react-router-dom";
// import { apiRequest } from "../services/api";

// function Applications() {
//   const navigate = useNavigate();

//   const [rows, setRows] = useState([]);
//   const [acknowledgedIds, setAcknowledgedIds] = useState(
//     new Set()
//   );
//   const [error, setError] = useState("");
//   const [loading, setLoading] = useState(true);

//   const load = async () => {
//     try {
//       setLoading(true);
//       setError("");

//       const data = await apiRequest(
//         "/api/applications"
//       );

//       const applications =
//         Array.isArray(data) ? data : [];

//       setRows(applications);

//       /*
//        * If an application becomes active again
//        * after document resubmission, show it again.
//        */
//       setAcknowledgedIds((previous) => {
//         const updated = new Set(previous);

//         applications.forEach((application) => {
//           const status = String(
//             application.status || ""
//           ).toUpperCase();

//           if (
//             status !== "APPROVED" &&
//             status !== "REJECTED"
//           ) {
//             updated.delete(application.id);
//           }
//         });

//         return updated;
//       });

//     } catch (e) {
//       setError(
//         e.message ||
//         "Unable to load applications."
//       );
//     } finally {
//       setLoading(false);
//     }
//   };


//   useEffect(() => {
//     load();
//   }, []);


//   /*
//    * Approve or reject the COMPLETE application.
//    */
//   const update = async (id, status) => {

//     let rejectionReason = "";

//     if (status === "REJECTED") {

//       const reason = window.prompt(
//         "Enter the rejection reason:",
//         "Required information or documents need correction"
//       );

//       if (reason === null) {
//         return;
//       }

//       if (!reason.trim()) {
//         setError(
//           "Please enter a rejection reason."
//         );
//         return;
//       }

//       rejectionReason = reason.trim();
//     }


//     try {

//       await apiRequest(
//         `/api/applications/${id}`,
//         {
//           method: "PUT",

//           headers: {
//             "Content-Type":
//               "application/json",
//           },

//           body: JSON.stringify({
//             status,
//             rejectionReason,
//           }),
//         }
//       );

//       await load();

//     } catch (e) {

//       setError(
//         e.message ||
//         "Unable to update application."
//       );
//     }
//   };


//   /*
//    * Remove completed application from
//    * the manager's current working list.
//    *
//    * This does NOT delete it from DB.
//    */
//   const acknowledge = (id) => {

//     setAcknowledgedIds((previous) => {

//       const updated =
//         new Set(previous);

//       updated.add(id);

//       return updated;
//     });
//   };


//   const visibleRows =
//     rows.filter(
//       (application) =>
//         !acknowledgedIds.has(application.id)
//     );


//   return (
//     <main className="table-page">

//       <div className="section-heading split-heading">

//         <div>

//           <span className="eyebrow">
//             MANAGEMENT • ONBOARDING
//           </span>

//           <h2>
//             Applications
//           </h2>

//           <p>
//             Review driver applications and move them
//             through the verification process.
//           </p>

//         </div>

//         <span className="count-badge">
//           {visibleRows.length} pending
//         </span>

//       </div>


//       {error && (
//         <div className="notice error">
//           {error}
//         </div>
//       )}


//       <div className="table-card">

//         <div className="table-scroll">

//           {loading ? (

//             <div className="empty-state">
//               Loading applications...
//             </div>

//           ) : visibleRows.length === 0 ? (

//             <div className="empty-state">
//               No applications require your attention.
//             </div>

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

//                 {visibleRows.map(
//                   (application) => {

//                     const status =
//                       String(
//                         application.status || ""
//                       ).toUpperCase();


//                     const canReview =
//                       status === "SUBMITTED" ||
//                       status === "DOCUMENT_VERIFICATION";


//                     const completed =
//                       status === "APPROVED" ||
//                       status === "REJECTED";


//                     return (

//                       <tr
//                         key={application.id}
//                       >

//                         <td>

//                           <strong>
//                             {application.name}
//                           </strong>

//                           <small
//                             style={{
//                               display: "block",
//                               marginTop: "4px",
//                             }}
//                           >
//                             {application.applicantUsername}
//                           </small>

//                         </td>


//                         <td>
//                           {application.phoneNumber}
//                         </td>


//                         <td>

//                           <span className="bike-tag">
//                             {application.bikeNumber}
//                           </span>

//                         </td>


//                         <td>
//                           {
//                             application.drivingLicenseNumber
//                           }
//                         </td>


//                         <td>

//                           <span
//                             className={`status-badge ${
//                               status === "APPROVED"
//                                 ? "status-approved"
//                                 : status === "REJECTED"
//                                 ? "status-rejected"
//                                 : "status-docs"
//                             }`}
//                           >
//                             {status.replaceAll(
//                               "_",
//                               " "
//                             )}
//                           </span>

//                         </td>


//                         <td>

//                           <div className="page-actions">

//                             <button
//                               className="action-button light"
//                               onClick={() =>
//                                 navigate(
//                                   `/documents?applicationId=${application.id}`
//                                 )
//                               }
//                             >
//                               Documents
//                             </button>


//                             {canReview && (
//                               <>
//                                 <button
//                                   className="action-button success"
//                                   onClick={() =>
//                                     update(
//                                       application.id,
//                                       "APPROVED"
//                                     )
//                                   }
//                                 >
//                                   Approve
//                                 </button>

//                                 <button
//                                   className="action-button danger"
//                                   onClick={() =>
//                                     update(
//                                       application.id,
//                                       "REJECTED"
//                                     )
//                                   }
//                                 >
//                                   Reject
//                                 </button>
//                               </>
//                             )}


//                             {completed && (
//                               <button
//                                 className="action-button success"
//                                 title="Mark as handled"
//                                 onClick={() =>
//                                   acknowledge(
//                                     application.id
//                                   )
//                                 }
//                                 style={{
//                                   width: "42px",
//                                   height: "42px",
//                                   minWidth: "42px",
//                                   borderRadius: "50%",
//                                   padding: 0,
//                                   fontSize: "20px",
//                                 }}
//                               >
//                                 ✓
//                               </button>
//                             )}

//                           </div>

//                         </td>

//                       </tr>

//                     );
//                   }
//                 )}

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
  const [completed, setCompleted] = useState({});

  const load = async () => {
    try {
      setLoading(true);
      setError("");

      const data = await apiRequest("/api/applications");

      /*
       * Manager only needs applications that still require action.
       * APPROVED and REJECTED applications are not shown after refresh.
       */
      const activeApplications = Array.isArray(data)
        ? data.filter(
            (application) =>
              application.status !== "APPROVED" &&
              application.status !== "REJECTED"
          )
        : [];

      setRows(activeApplications);
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

      /*
       * Do not immediately remove the row.
       * Show a confirmation tick first.
       */
      setCompleted((previous) => ({
        ...previous,
        [id]: true,
      }));
    } catch (e) {
      setError(e.message || "Unable to update application.");
    }
  };

  const removeCompleted = (id) => {
    setRows((previous) =>
      previous.filter((application) => application.id !== id)
    );

    setCompleted((previous) => {
      const copy = { ...previous };
      delete copy[id];
      return copy;
    });
  };

  const openDocuments = async (id) => {
    window.location.href = `/documents?applicationId=${id}`;
  };

  return (
    <main className="table-page">
      <div className="section-heading split-heading">
        <div>
          <span className="eyebrow">
            MANAGEMENT • ONBOARDING
          </span>

          <h2>Applications</h2>

          <p>
            Review driver applications and move them through
            the verification process.
          </p>
        </div>

        <span className="count-badge">
          {rows.length} active
        </span>
      </div>

      {error && (
        <div className="notice error">
          {error}
        </div>
      )}

      <div className="table-card">
        <div className="table-scroll">
          {loading ? (
            <div className="empty-state">
              Loading applications...
            </div>
          ) : rows.length === 0 ? (
            <div className="empty-state">
              No applications are waiting for review.
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
                {rows.map((application) => {
                  const isCompleted =
                    completed[application.id];

                  const status = String(
                    application.status || ""
                  ).toUpperCase();

                  return (
                    <tr key={application.id}>
                      <td>
                        <strong>
                          {application.name}
                        </strong>

                        <br />

                        <small>
                          {application.applicantUsername}
                        </small>
                      </td>

                      <td>
                        {application.phoneNumber}
                      </td>

                      <td>
                        <span className="bike-tag">
                          {application.bikeNumber}
                        </span>
                      </td>

                      <td>
                        {application.drivingLicenseNumber}
                      </td>

                      <td>
                        <span
                          className={`status-badge ${
                            status === "APPROVED"
                              ? "status-approved"
                              : status === "REJECTED"
                              ? "status-rejected"
                              : "status-docs"
                          }`}
                        >
                          {status.replaceAll("_", " ")}
                        </span>
                      </td>

                      <td>
                        {isCompleted ? (
                          <button
                            className="action-button success"
                            title="Confirm and remove"
                            onClick={() =>
                              removeCompleted(
                                application.id
                              )
                            }
                            style={{
                              width: "42px",
                              height: "42px",
                              borderRadius: "50%",
                              padding: 0,
                              fontSize: "20px",
                            }}
                          >
                            ✓
                          </button>
                        ) : (
                          <div className="page-actions">
                            <button
                              className="action-button light"
                              onClick={() =>
                                openDocuments(
                                  application.id
                                )
                              }
                            >
                              Documents
                            </button>

                            {status !== "REJECTED" && (
                              <button
                                className="action-button success"
                                onClick={() =>
                                  update(
                                    application.id,
                                    "APPROVED"
                                  )
                                }
                              >
                                Approve
                              </button>
                            )}

                            {status !== "APPROVED" && (
                              <button
                                className="action-button danger"
                                onClick={() =>
                                  update(
                                    application.id,
                                    "REJECTED"
                                  )
                                }
                              >
                                Reject
                              </button>
                            )}
                          </div>
                        )}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </main>
  );
}

export default Applications;