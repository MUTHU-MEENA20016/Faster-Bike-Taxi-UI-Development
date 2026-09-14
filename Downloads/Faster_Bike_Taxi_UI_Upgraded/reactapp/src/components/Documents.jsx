import React, { useEffect, useRef, useState } from "react";
import {
  apiRequest,
  API_URL,
  authHeaders,
} from "../services/api";
import { useAuth } from "../context/AuthContext";

function Documents() {
  const { role } = useAuth();

  const management =
    role === "MANAGER" || role === "ADMIN";

  const [documents, setDocuments] = useState([]);
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const fileInputs = useRef({});

  const load = async () => {
    try {
      setLoading(true);
      setError("");

      if (management) {
        /*
         * Get all documents.
         */
        const documentData =
          await apiRequest("/api/documents");

        /*
         * Get applications so that we can know
         * which applications are already approved.
         */
        const applicationData =
          await apiRequest("/api/applications");

        const allDocuments =
          Array.isArray(documentData)
            ? documentData
            : [];

        const allApplications =
          Array.isArray(applicationData)
            ? applicationData
            : [];

        /*
         * Find IDs of approved applications.
         */
        const approvedApplicationIds =
          new Set(
            allApplications
              .filter(
                (application) =>
                  String(
                    application.status
                  ).toUpperCase() === "APPROVED"
              )
              .map(
                (application) =>
                  application.id
              )
          );

        /*
         * IMPORTANT:
         *
         * Approved application's documents
         * should NOT appear in Manager's
         * Document Verification page.
         *
         * We are NOT deleting them from DB.
         */
        const pendingDocuments =
          allDocuments.filter(
            (document) =>
              !approvedApplicationIds.has(
                document.applicationId
              )
          );

        setDocuments(pendingDocuments);
      } else {
        /*
         * Driver sees only their own application.
         */
        const apps =
          await apiRequest(
            "/api/applications/my"
          );

        const myApplications =
          Array.isArray(apps)
            ? apps
            : [];

        setApplications(
          myApplications
        );

        if (myApplications.length > 0) {
          const data =
            await apiRequest(
              `/api/documents/application/${myApplications[0].id}`
            );

          setDocuments(
            Array.isArray(data)
              ? data
              : []
          );
        } else {
          setDocuments([]);
        }
      }
    } catch (e) {
      setError(
        e.message ||
          "Unable to load documents."
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, [management]);

  /*
   * Manager accepts/rejects a document.
   */
  const verify = async (
    id,
    status
  ) => {
    try {
      let rejectionReason = "";

      if (status === "REJECTED") {
        rejectionReason =
          window.prompt(
            "Enter the reason for rejecting this document:",
            "Image is unclear"
          ) || "";

        if (!rejectionReason.trim()) {
          return;
        }
      }

      let url =
        `/api/documents/${id}/status?status=${status}`;

      if (status === "REJECTED") {
        url +=
          `&rejectionReason=${encodeURIComponent(
            rejectionReason.trim()
          )}`;
      }

      await apiRequest(url, {
        method: "PATCH",
      });

      await load();
    } catch (e) {
      setError(
        e.message ||
          "Unable to update document status."
      );
    }
  };

  /*
   * Driver replaces ONLY the rejected document.
   */
  const replaceDocument = async (
    documentId,
    file
  ) => {
    if (!file) {
      return;
    }

    try {
      const body = new FormData();

      body.append(
        "file",
        file
      );

      await apiRequest(
        `/api/documents/${documentId}/replace`,
        {
          method: "POST",
          body,
        }
      );

      await load();
    } catch (e) {
      setError(
        e.message ||
          "Unable to replace the document."
      );
    }
  };

  /*
   * View/download document.
   */
  const download = async (
    id,
    name
  ) => {
    try {
      const response =
        await fetch(
          `${API_URL}/api/documents/${id}/download`,
          {
            headers: {
              ...authHeaders(),
            },
          }
        );

      if (!response.ok) {
        setError(
          "Unable to download document."
        );
        return;
      }

      const blob =
        await response.blob();

      const url =
        URL.createObjectURL(blob);

      const a =
        document.createElement("a");

      a.href = url;

      a.download =
        name || "document";

      document.body.appendChild(a);

      a.click();

      a.remove();

      URL.revokeObjectURL(url);
    } catch (e) {
      setError(
        "Unable to download document."
      );
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
          Loading documents...
        </div>

      ) : documents.length === 0 ? (

        <div className="table-card">

          <div className="empty-state">

            {management
              ? "No documents are waiting for verification."
              : applications.length === 0
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

                  <th>
                    Document
                  </th>

                  {management && (
                    <th>
                      Applicant
                    </th>
                  )}

                  <th>
                    File
                  </th>

                  <th>
                    Status
                  </th>

                  <th>
                    Uploaded
                  </th>

                  <th>
                    Action
                  </th>

                </tr>

              </thead>

              <tbody>

                {documents.map(
                  (document) => {

                    const status =
                      String(
                        document.verificationStatus ||
                          ""
                      ).toUpperCase();

                    return (

                      <tr
                        key={document.id}
                      >

                        <td>
                          {String(
                            document.documentType ||
                              ""
                          ).replaceAll(
                            "_",
                            " "
                          )}
                        </td>

                        {management && (

                          <td>

                            <strong>
                              {document.applicantName ||
                                "—"}
                            </strong>

                            <small
                              style={{
                                display:
                                  "block",
                                marginTop:
                                  "4px",
                              }}
                            >
                              {document.applicantPhone ||
                                "—"}
                            </small>

                            <small
                              style={{
                                display:
                                  "block",
                                marginTop:
                                  "3px",
                              }}
                            >
                              {document.applicationId
                                ? `Application #${document.applicationId}`
                                : "Application —"}
                            </small>

                          </td>

                        )}

                        <td>
                          {document.fileName}
                        </td>

                        <td>

                          <span
                            className={`status-badge ${
                              status ===
                              "VERIFIED"
                                ? "status-approved"
                                : status ===
                                  "REJECTED"
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

                        <td>
                          {document.uploadDate
                            ? new Date(
                                document.uploadDate
                              ).toLocaleDateString()
                            : "—"}
                        </td>

                        <td>

                          <div
                            className="page-actions"
                            style={{
                              alignItems:
                                "center",
                            }}
                          >

                            <button
                              className="action-button light"
                              onClick={() =>
                                download(
                                  document.id,
                                  document.fileName
                                )
                              }
                            >
                              View file
                            </button>

                            {management &&
                              status ===
                                "PENDING" && (
                                <>
                                  <button
                                    className="action-button success"
                                    onClick={() =>
                                      verify(
                                        document.id,
                                        "VERIFIED"
                                      )
                                    }
                                  >
                                    Accept
                                  </button>

                                  <button
                                    className="action-button danger"
                                    onClick={() =>
                                      verify(
                                        document.id,
                                        "REJECTED"
                                      )
                                    }
                                  >
                                    Reject
                                  </button>
                                </>
                              )}

                            {!management &&
                              status ===
                                "REJECTED" && (
                                <>

                                  <input
                                    type="file"
                                    accept="image/*,.pdf"
                                    style={{
                                      display:
                                        "none",
                                    }}
                                    ref={(element) => {
                                      fileInputs.current[
                                        document.id
                                      ] =
                                        element;
                                    }}
                                    onChange={(
                                      event
                                    ) => {

                                      const file =
                                        event
                                          .target
                                          .files?.[0];

                                      if (file) {
                                        replaceDocument(
                                          document.id,
                                          file
                                        );
                                      }

                                      event.target.value =
                                        "";
                                    }}
                                  />

                                  <button
                                    className="action-button success"
                                    onClick={() =>
                                      fileInputs.current[
                                        document.id
                                      ]?.click()
                                    }
                                  >
                                    Replace
                                  </button>

                                </>
                              )}

                          </div>

                        </td>

                      </tr>

                    );
                  }
                )}

              </tbody>

            </table>

          </div>

        </div>

      )}

    </main>
  );
}

export default Documents;