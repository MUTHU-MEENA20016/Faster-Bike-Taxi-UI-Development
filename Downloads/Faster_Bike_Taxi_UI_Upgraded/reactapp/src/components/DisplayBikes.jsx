import React, { useEffect, useState } from "react";
import { apiRequest } from "../services/api";

function DisplayBikes() {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    apiRequest("/api/applications/my")
      .then(setApplications)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, []);

  return (
    <main className="table-page">
      <div className="section-heading split-heading">
        <div>
          <span className="eyebrow">MY APPLICATION</span>
          <h2>Bike Details</h2>
          <p>View your registered bike and current application status.</p>
        </div>
        <span className="count-badge">
          {applications.length} application{applications.length === 1 ? "" : "s"}
        </span>
      </div>

      {error ? (
        <div className="notice error">{error}</div>
      ) : (
        <div className="table-card">
          <div className="table-scroll">
            {loading ? (
              <div className="empty-state">
                <div>
                  <div className="spinner" />
                  Loading...
                </div>
              </div>
            ) : applications.length === 0 ? (
              <div className="empty-state">
                No bike application has been submitted yet.
              </div>
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Bike Number</th>
                    <th>Driver</th>
                    <th>Age</th>
                    <th>Phone</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {applications.map((a) => (
                    <tr key={a.id}>
                      <td>
                        <span className="bike-tag">{a.bikeNumber}</span>
                      </td>
                      <td>{a.name}</td>
                      <td>{a.age}</td>
                      <td>{a.phoneNumber}</td>
                      <td>
                        <span className="status-badge">
                          {String(a.status).replaceAll("_", " ")}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      )}
    </main>
  );
}

export default DisplayBikes;