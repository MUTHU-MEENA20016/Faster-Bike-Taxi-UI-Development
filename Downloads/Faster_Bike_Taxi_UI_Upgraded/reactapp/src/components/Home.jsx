// import React from "react";
// import { Link } from "react-router-dom";
// import { useAuth } from "../context/AuthContext";
// import bikeTaxiHero from "../assets/bike-taxi-hero.jpg";

// function Home() {
//   const { token, role } = useAuth();

//   return (
//     <main className="home-page">
//       <section className="hero">
//         <div className="hero-copy">
//           <span className="eyebrow">
//             FASTER • SAFER • SMARTER
//           </span>

//           <h2>
//             Welcome to Faster Bike Taxi
//           </h2>

//           <p className="hero-description">
//             A simple and reliable platform for
//             drivers to join our bike taxi network.
//           </p>

//           {!token && (
//             <div className="hero-actions">
//               <Link
//                 className="primary-button"
//                 to="/register"
//               >
//                 Register as Driver
//               </Link>

//               <Link
//                 className="secondary-button"
//                 to="/login"
//               >
//                 Login
//               </Link>
//             </div>
//           )}

//           {token && role === "DRIVER" && (
//             <div className="hero-actions">
//               <Link
//                 className="primary-button"
//                 to="/dashboard"
//               >
//                 My Dashboard
//               </Link>
//             </div>
//           )}

//           {token && role !== "DRIVER" && (
//             <div className="hero-actions">
//               <Link
//                 className="primary-button"
//                 to="/dashboard"
//               >
//                 Go to Dashboard
//               </Link>
//             </div>
//           )}

//           <div className="trust-row">
//             <span>✓ Simple application</span>
//             <span>✓ Secure login</span>
//             <span>✓ Application tracking</span>
//           </div>
//         </div>

//         <div className="hero-visual">
//           <img
//             src={bikeTaxiHero}
//             alt="Faster Bike Taxi rider"
//             className="hero-photo"
//           />

//           <div className="floating-card card-top">
//             <strong>Ready to ride?</strong>
//             <span>
//               Start your journey today
//             </span>
//           </div>

//           <div className="floating-card card-bottom">
//             <span className="status-dot" />
//             Applications made easy
//           </div>
//         </div>
//       </section>

//       <section className="feature-grid">
//         <article className="feature-card">
//           <div className="feature-icon">
//             ⚡
//           </div>

//           <h3>Quick onboarding</h3>

//           <p>
//             Complete your driver application with
//             a clear, guided form.
//           </p>
//         </article>

//         <article className="feature-card">
//           <div className="feature-icon">
//             🛡️
//           </div>

//           <h3>Secure access</h3>

//           <p>
//             Role-based dashboards keep driver and
//             management information separate.
//           </p>
//         </article>

//         <article className="feature-card">
//           <div className="feature-icon">
//             📍
//           </div>

//           <h3>Track progress</h3>

//           <p>
//             Drivers can sign in and view their own
//             application information.
//           </p>
//         </article>
//       </section>
//     </main>
//   );
// }

// export default Home;
import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import bikeTaxiHero from "../assets/bike-taxi-hero.jpg";

function Home() {
  const { token, role } = useAuth();

  return (
    <main className="home-page">
      <section className="hero">
        <div className="hero-copy">
          <span className="eyebrow">FASTER • SAFER • SMARTER</span>

          <h2>Welcome to Faster Bike Taxi</h2>

          <p className="hero-description">
            A simple and reliable platform for drivers to join our bike taxi
            network, submit documents and track their onboarding progress.
          </p>

          {/* {!token && (
            <div className="hero-actions">
              <Link className="primary-button" to="/register">
                Register as Driver
              </Link>

              <Link className="secondary-button" to="/login">
                Login
              </Link>
            </div>
          )} */}

          {/* {token && (
            <div className="hero-actions">
              <Link className="primary-button" to="/dashboard">
                {role === "DRIVER"
                  ? "Open My Dashboard"
                  : "Open Management Dashboard"}
              </Link>
            </div>
          )} */}

          <div className="trust-row">
            <span>✓ Simple application</span>
            <span>✓ Secure login</span>
            <span>✓ Application tracking</span>
          </div>
        </div>

        <div className="hero-visual">
          <img
            src={bikeTaxiHero}
            alt="Faster Bike Taxi rider"
            className="hero-photo"
          />

          <div className="floating-card card-top">
            <strong>Ready to ride?</strong>
            <span>Start your driver journey today</span>
          </div>

          <div className="floating-card card-bottom">
            <strong>Secure onboarding</strong>
            <span>Documents reviewed by our team</span>
          </div>
        </div>
      </section>

      <section className="home-features">
        <div className="section-heading">
          <span className="eyebrow">HOW IT WORKS</span>
          <h2>Everything you need in one place</h2>
          <p>
            Complete your application, submit the required documents and track
            the verification process from your dashboard.
          </p>
        </div>

        <div className="feature-grid">
          <article className="dashboard-card">
            <div className="feature-icon">01</div>
            <h3>Create your account</h3>
            <p>
              Register as a driver with your basic contact information.
            </p>
          </article>

          <article className="dashboard-card">
            <div className="feature-icon">02</div>
            <h3>Submit your application</h3>
            <p>
              Enter your bike details and upload the four required documents.
            </p>
          </article>

          <article className="dashboard-card">
            <div className="feature-icon">03</div>
            <h3>Track verification</h3>
            <p>
              Check your application status and receive important notifications.
            </p>
          </article>
        </div>
      </section>
    </main>
  );
}

export default Home;