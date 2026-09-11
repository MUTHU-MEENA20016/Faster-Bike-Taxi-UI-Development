import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { register } from "../services/authService";

function Register() {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    passwordHash: "",
    phoneNumber: "",
    role: "DRIVER",
  });

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const validate = () => {
    if (
      !formData.username.trim() ||
      !formData.email.trim() ||
      !formData.passwordHash ||
      !formData.phoneNumber.trim()
    ) {
      return "Please fill all the mandatory fields";
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email.trim())) {
      return "Please enter a valid email";
    }

    if (formData.passwordHash.length < 6) {
      return "Password must contain at least 6 characters";
    }

    if (!/^\d{10}$/.test(formData.phoneNumber.trim())) {
      return "Phone number must contain exactly 10 digits";
    }

    return "";
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const validationError = validate();

    if (validationError) {
      setError(validationError);
      setMessage("");
      return;
    }

    setError("");
    setMessage("");
    setLoading(true);

    try {
      const registrationData = {
        username: formData.username.trim(),
        email: formData.email.trim(),
        passwordHash: formData.passwordHash,
        phoneNumber: formData.phoneNumber.trim(),
        role: "DRIVER",
      };

      console.log("Registration data:", registrationData);

      await register(registrationData);

      setMessage("Registration successful. You can now log in.");

      setTimeout(() => {
        navigate("/login");
      }, 1200);
    } catch (err) {
      setError(err.message || "Registration failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="auth-page">
      <div className="auth-card register-card">

        <div className="auth-icon">🏍️</div>

        <span className="eyebrow">DRIVER REGISTRATION</span>

        <h2>Create your account</h2>

        <p className="muted">
          Join Faster Bike Taxi and start your driver journey.
        </p>

        <form className="auth-form" onSubmit={handleSubmit}>

          <div className="field">
            <label htmlFor="username">Username:</label>

            <input
              id="username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              placeholder="Choose a username"
              required
            />
          </div>

          <div className="field">
            <label htmlFor="email">Email:</label>

            <input
              id="email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="you@example.com"
              required
            />
          </div>

          <div className="field">
            <label htmlFor="passwordHash">Password:</label>

            <input
              id="passwordHash"
              name="passwordHash"
              type="password"
              value={formData.passwordHash}
              onChange={handleChange}
              placeholder="At least 6 characters"
              required
            />
          </div>

          <div className="field">
            <label htmlFor="phoneNumber">Phone Number:</label>

            <input
              id="phoneNumber"
              name="phoneNumber"
              type="tel"
              value={formData.phoneNumber}
              onChange={handleChange}
              placeholder="10-digit mobile number"
              maxLength="10"
              required
            />
          </div>

          {message && (
            <p className="success-text">
              {message}
            </p>
          )}

          {error && (
            <p className="error">
              {error}
            </p>
          )}

          <button
            className="primary-button full-button"
            type="submit"
            disabled={loading}
          >
            {loading ? "Creating Account..." : "Create Driver Account"}
          </button>

        </form>

        <p className="auth-switch">
          Already have an account?{" "}
          <Link to="/login">Login here</Link>
        </p>

      </div>
    </main>
  );
}

export default Register;