import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { API_URL, authHeaders } from "../services/api";

const REQUIRED_DOCUMENTS = [
  {
    key: "drivingLicense",
    label: "Driving License",
  },
  {
    key: "vehicleRegistration",
    label: "Vehicle Registration Certificate",
  },
  {
    key: "insurance",
    label: "Vehicle Insurance",
  },
  {
    key: "pollutionCertificate",
    label: "Pollution Certificate",
  },
];

function ApplyForm() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    name: "",
    bikeNumber: "",
    age: "",
    address: "",
    emergencyContact: "",
    drivingLicenseNumber: "",
  });

  const [files, setFiles] = useState({});
  const [uploaded, setUploaded] = useState({});
  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [submitted, setSubmitted] = useState(false);
  const [loading, setLoading] = useState(false);
  const [uploading, setUploading] = useState({});

  const [uploadToken] = useState(
    () =>
      `${Date.now()}-${Math.random()
        .toString(36)
        .slice(2)}`
  );

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    setErrors((prev) => ({
      ...prev,
      [name]: "",
    }));

    setServerError("");
  };

  const handleFile = (key, file) => {
    if (!file) return;

    if (file.size > 10 * 1024 * 1024) {
      setErrors((prev) => ({
        ...prev,
        [key]: "File must be 10 MB or smaller.",
      }));
      return;
    }

    const allowed = [
      "application/pdf",
      "image/jpeg",
      "image/png",
    ];

    if (file.type && !allowed.includes(file.type)) {
      setErrors((prev) => ({
        ...prev,
        [key]: "Only PDF, JPG or PNG files are allowed.",
      }));
      return;
    }

    setFiles((prev) => ({
      ...prev,
      [key]: file,
    }));

    setUploaded((prev) => ({
      ...prev,
      [key]: false,
    }));

    setErrors((prev) => ({
      ...prev,
      [key]: "",
    }));
  };

  const removeFile = (key) => {
    setFiles((prev) => {
      const updated = { ...prev };
      delete updated[key];
      return updated;
    });

    setUploaded((prev) => {
      const updated = { ...prev };
      delete updated[key];
      return updated;
    });

    setErrors((prev) => ({
      ...prev,
      [key]: "",
    }));
  };

  const stageUpload = async (key) => {
    if (!files[key]) {
      setErrors((prev) => ({
        ...prev,
        [key]: "Choose a file first.",
      }));
      return;
    }

    const typeMap = {
      drivingLicense: "DRIVING_LICENSE",
      vehicleRegistration: "VEHICLE_REGISTRATION",
      insurance: "INSURANCE",
      pollutionCertificate: "POLLUTION_CERTIFICATE",
    };

    try {
      setUploading((prev) => ({
        ...prev,
        [key]: true,
      }));

      const body = new FormData();

      body.append("file", files[key]);
      body.append("documentType", typeMap[key]);
      body.append("uploadToken", uploadToken);

      const response = await fetch(
        `${API_URL}/api/documents/temp`,
        {
          method: "POST",
          headers: {
            ...authHeaders(),
          },
          body,
        }
      );

      if (!response.ok) {
        throw new Error(
          (await response.text()) ||
            "Document upload failed."
        );
      }

      setUploaded((prev) => ({
        ...prev,
        [key]: true,
      }));

      setErrors((prev) => ({
        ...prev,
        [key]: "",
      }));
    } catch (error) {
      setUploaded((prev) => ({
        ...prev,
        [key]: false,
      }));

      setErrors((prev) => ({
        ...prev,
        [key]: error.message,
      }));
    } finally {
      setUploading((prev) => ({
        ...prev,
        [key]: false,
      }));
    }
  };

  const validate = () => {
    const e = {};
    const age = Number(formData.age);

    if (!formData.name.trim()) {
      e.name = "Name is required";
    } else if (!/^[A-Za-z ]+$/.test(formData.name.trim())) {
      e.name =
        "Name can contain only letters and spaces";
    }

    if (
      !/^[A-Z]{2}\s?\d{1,2}\s?[A-Z]{1,3}\s?\d{4}$/i.test(
        formData.bikeNumber.trim()
      )
    ) {
      e.bikeNumber = "Enter a valid bike number";
    }

    if (!Number.isInteger(age) || age < 18 || age > 65) {
      e.age = "Age must be between 18 and 65";
    }

    if (!formData.address.trim()) {
      e.address = "Address is required";
    }

    if (!formData.emergencyContact.trim()) {
      e.emergencyContact =
        "Emergency contact is required";
    }

    if (!formData.drivingLicenseNumber.trim()) {
      e.drivingLicenseNumber =
        "Driving license number is required";
    }

    REQUIRED_DOCUMENTS.forEach(({ key }) => {
      if (!files[key] || !uploaded[key]) {
        e[key] =
          "Select the file and click Upload";
      }
    });

    return e;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const validationErrors = validate();

    setErrors(validationErrors);
    setServerError("");

    if (Object.keys(validationErrors).length) {
      return;
    }

    const username = localStorage.getItem("username");

    const body = new FormData();

    body.append("name", formData.name.trim());
    body.append(
      "bikeNumber",
      formData.bikeNumber.trim().toUpperCase()
    );
    body.append("age", String(Number(formData.age)));
    body.append("address", formData.address.trim());
    body.append(
      "emergencyContact",
      formData.emergencyContact.trim()
    );
    body.append(
      "drivingLicenseNumber",
      formData.drivingLicenseNumber
        .trim()
        .toUpperCase()
    );
    body.append("username", username || "");
    body.append("uploadToken", uploadToken);

    try {
      setLoading(true);

      const response = await fetch(
        `${API_URL}/api/applications/submit`,
        {
          method: "POST",
          headers: {
            ...authHeaders(),
          },
          body,
        }
      );

      if (!response.ok) {
        throw new Error(
          (await response.text()) ||
            "Unable to submit application."
        );
      }

      /*
       * Mark this specific user's application
       * as submitted.
       */
      if (username) {
        localStorage.setItem(
          `applicationSubmitted_${username}`,
          "true"
        );
      }

      /*
       * Tell Navbar immediately.
       * No browser refresh is required.
       */
      window.dispatchEvent(
        new Event("applicationSubmitted")
      );

      setSubmitted(true);

      setTimeout(() => {
        navigate("/dashboard");
      }, 1200);
    } catch (error) {
      setServerError(error.message);
    } finally {
      setLoading(false);
    }
  };

  if (submitted) {
    return (
      <main className="form-page">
        <div className="dashboard-card">
          <div className="feature-icon">✅</div>

          <h2>Application Submitted</h2>

          <p>
            Your application has been submitted
            successfully.
          </p>

          <p className="muted">
            You will receive notifications when your
            application status changes.
          </p>
        </div>
      </main>
    );
  }

  return (
    <main className="form-page">
      <div className="section-heading">
        <span className="eyebrow">
          DRIVER ONBOARDING
        </span>

        <h2>Driver Application</h2>

        <p>
          Enter your details and upload all required
          documents.
        </p>
      </div>

      <form
        className="glass-form"
        onSubmit={handleSubmit}
      >
        <div className="field">
          <label>Name *</label>

          <input
            name="name"
            value={formData.name}
            onChange={handleChange}
            placeholder="Enter your full name"
          />

          {errors.name && (
            <span className="error">
              {errors.name}
            </span>
          )}
        </div>

        <div className="field">
          <label>Bike Number *</label>

          <input
            name="bikeNumber"
            value={formData.bikeNumber}
            onChange={handleChange}
            placeholder="TN 45 A 1547"
          />

          {errors.bikeNumber && (
            <span className="error">
              {errors.bikeNumber}
            </span>
          )}
        </div>

        <div className="field">
          <label>Age *</label>

          <input
            type="number"
            name="age"
            value={formData.age}
            onChange={handleChange}
            placeholder="Enter your age"
          />

          {errors.age && (
            <span className="error">
              {errors.age}
            </span>
          )}
        </div>

        <div className="field">
          <label>Address *</label>

          <textarea
            name="address"
            value={formData.address}
            onChange={handleChange}
            placeholder="Enter your address"
            rows="3"
          />

          {errors.address && (
            <span className="error">
              {errors.address}
            </span>
          )}
        </div>

        <div className="field">
          <label>Emergency Contact *</label>

          <input
            name="emergencyContact"
            value={formData.emergencyContact}
            onChange={handleChange}
            placeholder="Emergency contact"
          />

          {errors.emergencyContact && (
            <span className="error">
              {errors.emergencyContact}
            </span>
          )}
        </div>

        <div className="field">
          <label>Driving License Number *</label>

          <input
            name="drivingLicenseNumber"
            value={formData.drivingLicenseNumber}
            onChange={handleChange}
            placeholder="Enter license number"
          />

          {errors.drivingLicenseNumber && (
            <span className="error">
              {errors.drivingLicenseNumber}
            </span>
          )}
        </div>

        <div className="document-upload-section">
          <h3>Required Documents</h3>

          <p className="muted">
            Upload all four documents before submitting
            your application.
          </p>

          {REQUIRED_DOCUMENTS.map(
            ({ key, label }) => (
              <div
                className="document-upload-card"
                key={key}
              >
                <div className="document-upload-info">
                  <strong>{label} *</strong>

                  {!uploaded[key] ? (
                    <>
                      <input
                        type="file"
                        id={key}
                        accept=".pdf,.jpg,.jpeg,.png"
                        onChange={(e) =>
                          handleFile(
                            key,
                            e.target.files[0]
                          )
                        }
                      />

                      <button
                        type="button"
                        className="action-button"
                        onClick={() =>
                          stageUpload(key)
                        }
                        disabled={
                          !files[key] ||
                          uploading[key]
                        }
                      >
                        {uploading[key]
                          ? "Uploading..."
                          : "Upload"}
                      </button>
                    </>
                  ) : (
                    <div className="uploaded-file-row">
                      <span className="uploaded-file">
                        ✓ {files[key]?.name}
                      </span>

                      <button
                        type="button"
                        className="remove-file-button"
                        title="Remove document"
                        onClick={() =>
                          removeFile(key)
                        }
                      >
                        ✕
                      </button>
                    </div>
                  )}

                  {errors[key] && (
                    <span className="error">
                      {errors[key]}
                    </span>
                  )}
                </div>
              </div>
            )
          )}
        </div>

        {serverError && (
          <div className="notice error">
            {serverError}
          </div>
        )}

        <button
          className="primary-button full-button"
          type="submit"
          disabled={loading}
        >
          {loading
            ? "Submitting Application..."
            : "Submit Application"}
        </button>
      </form>
    </main>
  );
}

export default ApplyForm;