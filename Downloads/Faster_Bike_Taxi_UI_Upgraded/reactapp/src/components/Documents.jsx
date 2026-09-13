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
          setDocuments(
            await apiRequest(
              `/api/documents/application/${apps[0].id}`
            )
          );
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
      await apiRequest(
        `/api/documents/${id}/status?status=${status}`,
        {
          method: "PATCH",
        }
      );

      await load();
    } catch (e) {
      setError(e.message);
    }
  };

  const download = async (id, name) => {
    try {
      const response = await fetch(
        `${API_URL}/api/documents/${id}/download`,
        {
          headers: {
            ...authHeaders(),
          },
        }
      );

      if (!response.ok) {
        setError("Unable to download document.");
        return;
      }

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
          <span className="eyebrow">
            DOCUMENT MANAGEMENT
          </span>

          <h2>
            {management
              ? "Document Verification"
              : "My Documents"}
          </h2>

          <p>
            {management
              ? "Review and verify documents submitted by applicants."
              : "Track the verification status of your submitted documents."}
          </p>
        </div>
      </div>

      {error && (
        <div className="notice error">
          {error}
        </div>
      )}

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

                  {management && (
                    <th>Applicant</th>
                  )}

                  <th>File</th>
                  <th>Status</th>
                  <th>Uploaded</th>
                  <th>Action</th>
                </tr>
              </thead>

              <tbody>

                {documents.map((d) => {

                  const status =
                    String(
                      d.verificationStatus || ""
                    ).toUpperCase();

                  return (
                    <tr key={d.id}>

                      {/* DOCUMENT */}
                      <td>
                        {String(
                          d.documentType
                        ).replaceAll("_", " ")}
                      </td>

                      {/* APPLICANT - MANAGEMENT ONLY */}
                      {management && (
                        <td>
                          <strong>
                            {d.applicantName || "—"}
                          </strong>

                          <small
                            style={{
                              display: "block",
                              marginTop: "4px",
                            }}
                          >
                            {d.applicantPhone || "—"}
                          </small>

                          <small
                            style={{
                              display: "block",
                              marginTop: "3px",
                            }}
                          >
                            {d.applicationId
                              ? `Application #${d.applicationId}`
                              : "Application —"}
                          </small>
                        </td>
                      )}

                      {/* FILE */}
                      <td className="file-name">
                        {d.fileName}
                      </td>

                      {/* STATUS */}
                      <td>
                        <span
                          className={`status-badge ${
                            status === "VERIFIED"
                              ? "status-approved"
                              : status === "REJECTED"
                              ? "status-rejected"
                              : "status-docs"
                          }`}
                        >
                          {status
                            ? status.replaceAll(
                                "_",
                                " "
                              )
                            : "PENDING"}
                        </span>
                      </td>

                      {/* UPLOADED */}
                      <td>
                        {d.uploadDate
                          ? new Date(
                              d.uploadDate
                            ).toLocaleDateString()
                          : "—"}
                      </td>

                      {/* ACTION */}
                      <td>
                        <div className="page-actions">

                          <button
                            className="action-button light"
                            onClick={() =>
                              download(
                                d.id,
                                d.fileName
                              )
                            }
                          >
                            View file
                          </button>

                          {management && (
                            <>
                              {/* SHOW ACCEPT UNLESS ALREADY ACCEPTED */}
                              {status !== "VERIFIED" && (
                                <button
                                  className="action-button success"
                                  onClick={() =>
                                    verify(
                                      d.id,
                                      "VERIFIED"
                                    )
                                  }
                                >
                                  Accept
                                </button>
                              )}

                              {/* SHOW REJECT UNLESS ALREADY REJECTED */}
                              {status !== "REJECTED" && (
                                <button
                                  className="action-button danger"
                                  onClick={() =>
                                    verify(
                                      d.id,
                                      "REJECTED"
                                    )
                                  }
                                >
                                  Reject
                                </button>
                              )}
                            </>
                          )}

                        </div>
                      </td>

                    </tr>
                  );
                })}

              </tbody>

            </table>

          </div>

        </div>
      )}

    </main>
  );
}

export default Documents;