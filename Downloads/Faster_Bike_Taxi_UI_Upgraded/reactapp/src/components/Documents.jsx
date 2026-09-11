import React, { useEffect, useState } from "react";
import { apiRequest, API_URL, authHeaders } from "../services/api";
import { useAuth } from "../context/AuthContext";

function Documents() {
  const { role } = useAuth();
  const management = role === "MANAGER" || role === "ADMIN";

  const [documents, setDocuments] = useState([]);
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const load = async () => {
    try {
      if (management) {
        const data = await apiRequest("/api/documents");
        setDocuments(data);
      } else {
        const apps = await apiRequest("/api/applications/my");
        setApplications(apps);
        if (apps[0]) {
          setDocuments(await apiRequest(`/api/documents/application/${apps[0].id}`));
        }
      }
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, [management]);

  const verify = async (id, status) => {
    try {
      await apiRequest(`/api/documents/${id}/status?status=${status}`, {
        method: "PATCH",
      });
      await load();
    } catch (e) {
      setError(e.message);
    }
  };

  const download = async (id, name) => {
    try {
      const response = await fetch(`${API_URL}/api/documents/${id}/download`, {
        headers: { ...authHeaders() },
      });
      if (!response.ok) return setError("Unable to download document.");
      
      const blob = await response.blob();
      const url = URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = name || "document";
      a.click();
      URL.revokeObjectURL(url);
    } catch (e) {
      setError("Unable to download document.");
    }
  };

  return (
    <main className="table-page">
      <div className="section-heading split-heading">
        <div>
          <span className="eyebrow">DOCUMENT MANAGEMENT</span>
          <h2>{management ? "Document Verification" : "My Documents"}</h2>
          <p>
            {management
              ? "Review and verify documents submitted by applicants."
              : "Track the verification status of your submitted documents."}
          </p>
        </div>
      </div>

      {error && <div className="notice error">{error}</div>}

      {loading ? (
        <div className="empty-state">
          <div>
            <div className="spinner" />
            Loading documents...
          </div>
        </div>
      ) : documents.length === 0 ? (
        <div className="table-card">
          <div className="empty-state">
            {applications.length === 0
              ? "Submit an application to see your documents here."
              : "No documents found."}
          </div>
        </div>
      ) : (
        <div className="table-card">
          <div className="table-scroll">
            <table>
              <thead>
                <tr>
                  <th>Document</th>
                  <th>File</th>
                  <th>Status</th>
                  <th>Uploaded</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {documents.map((d) => (
                  <tr key={d.id}>
                    <td>{String(d.documentType).replaceAll("_", " ")}</td>
                    <td className="file-name">{d.fileName}</td>
                    <td>
                      <span
                        className={`status-badge ${
                          String(d.verificationStatus).toLowerCase() === "verified"
                            ? "status-approved"
                            : String(d.verificationStatus).toLowerCase() === "rejected"
                            ? "status-rejected"
                            : "status-docs"
                        }`}
                      >
                        {String(d.verificationStatus).replaceAll("_", " ")}
                      </span>
                    </td>
                    <td>
                      {d.uploadDate
                        ? new Date(d.uploadDate).toLocaleDateString()
                        : "—"}
                    </td>
                    <td>
                      <div className="page-actions">
                        <button
                          className="action-button light"
                          onClick={() => download(d.id, d.fileName)}
                        >
                          View file
                        </button>
                        {management && (
                          <>
                            <button
                              className="action-button success"
                              onClick={() => verify(d.id, "VERIFIED")}
                            >
                              Verify
                            </button>
                            <button
                              className="action-button danger"
                              onClick={() => verify(d.id, "REJECTED")}
                            >
                              Reject
                            </button>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </main>
  );
}

export default Documents;