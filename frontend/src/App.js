import React, { useState, useEffect, createContext, useContext } from "react";
import { Route, Routes, Navigate } from "react-router-dom";
import LoginPage from "./features/LoginPage";
import SignupPage from "./features/SignupPage";
import Navigation from "./components/Navigation";
import PageNotFound from "./components/PageNotFound";
import "./styles.css";

export const AuthContext = createContext();

const AuthenticatedNavigationWrapper = () => {
  const token = localStorage.getItem("userToken");
  const isAuthenticated = Boolean(token);

  if (!isAuthenticated) {
    return <PageNotFound />;
  }

  return <Navigation />;
};
const LoginHandler = () => {
  useEffect(() => {
    localStorage.removeItem("userToken");
  }, []);

  return <LoginPage />;
};

const App = () => {
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setIsLoading(false);
  }, []);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <AuthContext.Provider value={{ setIsLoading }}>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<LoginHandler />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/*" element={<AuthenticatedNavigationWrapper />} />
      </Routes>
    </AuthContext.Provider>
  );
};

export default App;
