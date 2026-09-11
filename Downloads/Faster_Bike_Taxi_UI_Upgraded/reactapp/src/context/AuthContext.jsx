import React, { createContext, useContext, useState } from "react";

const AuthContext = createContext({
  token: null,
  role: null,
  loginUser: () => {},
  logoutUser: () => {},
});

function decodeRole(token) {
  try {
    const base64 = token.split(".")[1].replace(/-/g, "+").replace(/_/g, "/");
    return JSON.parse(atob(base64)).role || null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem("token") || null);
  const [role, setRole] = useState(token ? decodeRole(token) : null);

  const loginUser = (newToken) => {
    localStorage.setItem("token", newToken);
    setToken(newToken);
    setRole(decodeRole(newToken));
  };

  const logoutUser = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    setToken(null);
    setRole(null);
  };

  return <AuthContext.Provider value={{ token, role, loginUser, logoutUser }}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}