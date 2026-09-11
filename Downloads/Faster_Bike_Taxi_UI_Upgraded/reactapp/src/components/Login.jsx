import React, { useState } from "react";
import {
  Link,
  useNavigate,
} from "react-router-dom";
import { login } from "../services/authService";
import { useAuth } from "../context/AuthContext";

function Login() {
  const navigate = useNavigate();
  const { loginUser } = useAuth();

  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (
      !formData.username.trim() ||
      !formData.password.trim()
    ) {
      setError(
        "Please enter both username and password."
      );
      return;
    }

    try {
      setLoading(true);
      setError("");

      const data = await login(
        formData.username.trim(),
        formData.password
      );

      const token = data.token;

      if (!token) {
        throw new Error(
          "Login failed. Token was not received."
        );
      }

      localStorage.setItem(
        "username",
        formData.username.trim()
      );

      loginUser(token);

      navigate("/dashboard");
    } catch (err) {
      console.error(err);

      setError(
        err.message ||
          "Invalid username or password."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="form-page">
      <div className="section-heading">
        <span className="eyebrow">
          WELCOME BACK
        </span>

        <h2>Login</h2>

        <p>
          Sign in to continue to your Faster Bike
          Taxi account.
        </p>
      </div>

      <form
        className="glass-form auth-form"
        onSubmit={handleSubmit}
      >
        <div className="field">
          <label htmlFor="username">
            Username:
          </label>

          <input
            type="text"
            id="username"
            name="username"
            value={formData.username}
            onChange={handleChange}
            placeholder="Enter your username"
          />
        </div>

        <div className="field">
          <label htmlFor="password">
            Password:
          </label>

          <input
            type="password"
            id="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            placeholder="Enter your password"
          />
        </div>

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
          {loading
            ? "Logging in..."
            : "Login"}
        </button>

        <div className="auth-footer">
          <p>
            Don't have an account?{" "}
            <Link to="/register">
              Register here
            </Link>
          </p>
        </div>
      </form>
    </main>
  );
}

export default Login;