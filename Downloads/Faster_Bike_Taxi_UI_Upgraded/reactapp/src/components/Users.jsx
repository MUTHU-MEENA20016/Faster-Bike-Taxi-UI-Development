import React, { useEffect, useState } from "react";
import { apiRequest } from "../services/api";

function Users() {
  const [rows, setRows] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    apiRequest("/api/users")
      .then(setRows)
      .catch((e) => setError(e.message));
  }, []);

  return (
    <main className="table-page">
      <div className="section-heading">
        <span className="eyebrow">ADMINISTRATION</span>
        <h2>Users</h2>
        <p>View registered accounts and their roles.</p>
      </div>

      {error && <div className="notice error">{error}</div>}

      <div className="table-card">
        <div className="table-scroll">
          <table>
            <thead>
              <tr>
                <th>Username</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
                <th>Active</th>
              </tr>
            </thead>
            <tbody>
              {rows.map((u) => (
                <tr key={u.id}>
                  <td>{u.username}</td>
                  <td>{u.email}</td>
                  <td>{u.phoneNumber || "—"}</td>
                  <td>
                    <span className="role-pill">{u.role}</span>
                  </td>
                  <td>{u.isActive === false ? "No" : "Yes"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </main>
  );
}

export default Users;