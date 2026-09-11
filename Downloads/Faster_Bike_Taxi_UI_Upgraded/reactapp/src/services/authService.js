// const API_URL = "http://localhost:8080";
// const API_URL =
//   "https://8080-defcfabecccedbefaebababcdbdecdeabcadecaab.premiumproject.examly.io";



const API_URL = "http://localhost:8080";

export const register = async (formData) => {
  const response = await fetch(`${API_URL}/auth/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(formData),
  });

  if (!response.ok) {
    const errorMessage = await response.text();
    throw new Error(errorMessage || "Registration failed");
  }

  return response.text();
};

export const login = async (username, password) => {
  const response = await fetch(`${API_URL}/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      username,
      password,
    }),
  });

  if (!response.ok) {
    throw new Error(await response.text());
  }

  return response.json();
};