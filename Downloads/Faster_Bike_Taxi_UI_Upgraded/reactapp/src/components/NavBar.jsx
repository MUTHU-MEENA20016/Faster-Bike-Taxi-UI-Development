import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { apiRequest } from "../services/api";

function Navbar() {
  const {
    token,
    role,
    logoutUser,
  } = useAuth();

  const [hasApplied, setHasApplied] = useState(false);

  useEffect(() => {
    const checkApplication = async () => {
      if (!token || role !== "DRIVER") {
        setHasApplied(false);
        return;
      }

      const username = localStorage.getItem("username");

      /*
       * First check this specific user's local state.
       */
      if (
        username &&
        localStorage.getItem(
          `applicationSubmitted_${username}`
        ) === "true"
      ) {
        setHasApplied(true);
        return;
      }

      /*
       * Backend remains the source of truth.
       * This also handles cases such as:
       * - browser refresh
       * - previously submitted application
       */
      try {
        const applications = await apiRequest(
          "/api/applications/my"
        );

        const alreadyApplied =
          Array.isArray(applications) &&
          applications.length > 0;

        setHasApplied(alreadyApplied);

        /*
         * Remember the result for this specific user.
         */
        if (alreadyApplied && username) {
          localStorage.setItem(
            `applicationSubmitted_${username}`,
            "true"
          );
        }
      } catch (error) {
        setHasApplied(false);
      }
    };

    checkApplication();

    /*
     * Listen for successful submission from ApplyForm.
     */
    const handleApplicationSubmitted = () => {
      setHasApplied(true);
    };

    window.addEventListener(
      "applicationSubmitted",
      handleApplicationSubmitted
    );

    return () => {
      window.removeEventListener(
        "applicationSubmitted",
        handleApplicationSubmitted
      );
    };
  }, [token, role]);

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
              {role === "DRIVER" && !hasApplied && (
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