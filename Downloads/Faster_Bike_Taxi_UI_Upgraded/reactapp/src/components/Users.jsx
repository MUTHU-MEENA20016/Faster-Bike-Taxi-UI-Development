import React, { useEffect, useState } from "react";
import { apiRequest } from "../services/api";

function Users() {
  const [rows, setRows] = useState([]);
  const [error, setError] = useState("");
  const [deletingId, setDeletingId] = useState(null);

  const loadUsers = async () => {
    try {
      setError("");

      const data = await apiRequest("/api/users");
      setRows(Array.isArray(data) ? data : []);
    } catch (e) {
      setError(e.message || "Unable to load users.");
    }
  };

  useEffect(() => {
    loadUsers();
  }, []);

  const removeManager = async (user) => {
    const confirmed = window.confirm(
      `Are you sure you want to remove manager "${user.username}"?`
    );

    if (!confirmed) {
      return;
    }

    try {
      setDeletingId(user.id);
      setError("");

      await apiRequest(`/api/users/${user.id}`, {
        method: "DELETE",
      });

      await loadUsers();
    } catch (e) {
      setError(e.message || "Unable to remove manager.");
    } finally {
      setDeletingId(null);
    }
  };

  return (
    <main className="table-page">
      <div className="section-heading">
        <span className="eyebrow">ADMINISTRATION</span>
        <h2>User Management</h2>
        <p>
          View registered users and remove managers who are no longer part of
          the application.
        </p>
      </div>

      {error && <div className="notice error">{error}</div>}

      <div className="table-card">
        <div className="table-scroll">
          {rows.length === 0 ? (
            <div className="empty-state">
              No users are available.
            </div>
          ) : (
            <table>
              <thead>
                <tr>
                  <th>Username</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>Role</th>
                  <th>Status</th>
                  <th>Action</th>
                </tr>
              </thead>

              <tbody>
                {rows.map((u) => (
                  <tr key={u.id}>
                    <td>{u.username}</td>

                    <td>{u.email}</td>

                    <td>{u.phoneNumber || "—"}</td>

                    <td>
                      <span className="role-pill">
                        {u.role}
                      </span>
                    </td>

                    <td>
                      {u.isActive === false ? "Inactive" : "Active"}
                    </td>

                    <td>
                      {u.role === "MANAGER" ? (
                        <button
                          className="action-button danger"
                          onClick={() => removeManager(u)}
                          disabled={deletingId === u.id}
                        >
                          {deletingId === u.id
                            ? "Removing..."
                            : "Remove"}
                        </button>
                      ) : (
                        "—"
                      )}
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

export default Users;