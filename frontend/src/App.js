import React, { useState, useEffect, createContext, useContext } from "react";
import { Route, Routes, Navigate } from "react-router-dom";
import LoginPage from "./features/LoginPage";
import OrderPage from "./features/OrderManagement";
import SignupPage from "./features/SignupPage";
export const AuthContext = createContext();

const App = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("userToken");
    console.log(token);
    if (token) {
      setIsAuthenticated(true);
    }
    setIsLoading(false);
  }, []);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, setIsAuthenticated }}>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route
          path="/orders"
          element={
            <ProtectedRoute>
              <OrderPage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </AuthContext.Provider>
  );
};
const ProtectedRoute = ({ children }) => {
  const { isAuthenticated } = useContext(AuthContext);
  // console.log(isAuthenticated);
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default App;
