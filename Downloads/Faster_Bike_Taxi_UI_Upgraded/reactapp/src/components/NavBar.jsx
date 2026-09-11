import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Navbar() {
  const {
    token,
    role,
    logoutUser,
  } = useAuth();

  return (
    <header className="site-header">
      <nav className="navbar">
        <Link
          className="brand"
          to="/"
        >
          <span className="brand-mark">
            F
          </span>

          <span>
            <strong>Faster</strong>{" "}
            Bike Taxi
          </span>
        </Link>

        <div className="nav-links">
          <Link
            className="nav-link"
            to="/"
          >
            Home
          </Link>

          {token && (
            <Link
              className="nav-link"
              to="/bikedetails"
            >
              Bike Details
            </Link>
          )}

          {!token && (
            <>
              <Link
                className="nav-link"
                to="/login"
              >
                Login
              </Link>

              <Link
                className="nav-cta"
                to="/register"
              >
                Register
              </Link>
            </>
          )}

          {token && (
            <>
              {role === "DRIVER" && (
                <Link
                  className="nav-cta"
                  to="/apply"
                >
                  Apply Now
                </Link>
              )}

              <Link
                className="nav-link"
                to="/dashboard"
              >
                {role} Dashboard
              </Link>

              <button
                className="logout-button"
                onClick={logoutUser}
              >
                Logout
              </button>
            </>
          )}
        </div>
      </nav>
    </header>
  );
}

export default Navbar;
