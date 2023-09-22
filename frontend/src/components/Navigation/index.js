import React, { useState, useEffect } from "react";
import { useLocation, Routes, Route, Navigate } from "react-router-dom";
import { Box, CssBaseline } from "@mui/material";
import CustomerNavigation from "./components/CustomerNavigation";
import SupplierNavigation from "./components/SupplierNavigation";
import { checkIfBusiness } from "./api";
import OrdersPage from "../../features/OrderManagement/components/OrdersPage/index";
import UserManagementPage from "../../features/UserManagement";
import LinkedUsersPage from "../../features/LinkedUsersPage";

const LayoutWrapper = ({ isBusiness, children }) => (
  <Box
    sx={{
      marginLeft: isBusiness ? "13%" : "0%",
      width: isBusiness ? "87%" : "100%",
      backgroundColor: "#e6ffe3",
      minHeight: "100vh",
    }}
  >
    {children}
  </Box>
);

const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem("userToken");
  const isAuthenticated = Boolean(token);

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

const Navigation = () => {
  const [isBusiness, setIsBusiness] = useState(false);
  const location = useLocation();

  useEffect(() => {
    if (
      location.pathname !== "/login" &&
      location.pathname !== "/signup" &&
      location.pathname !== "/"
    ) {
      const fetchData = async () => {
        const businessCheck = await checkIfBusiness();
        if (businessCheck === 200) {
          setIsBusiness(true);
        } else {
          setIsBusiness(false);
        }
      };
      fetchData();
    }
  }, [location.pathname]);

  if (
    location.pathname === "/login" ||
    location.pathname === "/signup" ||
    location.pathname === "/"
  ) {
    return null;
  }

  return (
    <>
      {isBusiness ? <SupplierNavigation /> : <CustomerNavigation />}
      <LayoutWrapper isBusiness={isBusiness}>
        <CssBaseline />
        <Routes>
          <Route
            path="/orders"
            element={
              <ProtectedRoute>
                <OrdersPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/users"
            element={
              <ProtectedRoute>
                <UserManagementPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/linkedUsers"
            element={
              <ProtectedRoute>
                <LinkedUsersPage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </LayoutWrapper>
    </>
  );
};

export default Navigation;
