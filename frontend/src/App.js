import React, { useState, useEffect, createContext, useContext } from "react";
import { Route, Routes, Navigate } from "react-router-dom";
import LoginPage from "./features/LoginPage";
import SignupPage from "./features/SignupPage";
import CustomerNavigation from "./components/Navigation/components/CustomerNavigation";
import Navigation from "./components/Navigation";
import OrdersPage from "./features/OrderManagement/components/OrdersPage";
export const AuthContext = createContext();

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
      <Navigation />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route
          path="/orders"
          element={
            <ProtectedRoute>
              <OrdersPage />
            </ProtectedRoute>
          }
        />
      </Routes>
    </AuthContext.Provider>
  );
};

const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem("userToken");
  const isAuthenticated = Boolean(token);

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default App;
