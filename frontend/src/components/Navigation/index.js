import React, { useState, useEffect } from "react";
import { useLocation, Routes, Route, Navigate } from "react-router-dom";
import { Box, CssBaseline } from "@mui/material";
import CustomerNavigation from "./components/CustomerNavigation";
import SupplierNavigation from "./components/SupplierNavigation";
import { checkIfBusiness } from "./api";
import OrderPage from "../../features/OrderManagement";
import UserManagementPage from "../../features/UserManagement";
import LinkingPage from "../../features/LinkingManagement";
import InventoryManagementPage from "../../features/InventoryManagement";
import Catalogue from "../../features/Catalogue";
import ShoppingCart from "../../features/ShoppingCart";
import "./styles.css";

const LayoutWrapper = ({ isBusiness, children }) => (
  <Box
    sx={{
      marginLeft: isBusiness ? "15%" : "0%",
      width: isBusiness ? "85%" : "100%",
      backgroundColor: "#FAFAFA",
      minHeight: isBusiness ? "100vh" : "92vh",
      maxHeight: isBusiness ? "100vh" : "92vh",
      minWidth: isBusiness ? "85vw" : "100vw",
      maxWidth: isBusiness ? "85vw" : "100vw",
      overflowY: "auto",
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
                <OrderPage />
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
            path="/links"
            element={
              <ProtectedRoute>
                <LinkingPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/inventory"
            element={
              <ProtectedRoute>
                <InventoryManagementPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/home"
            element={
              <ProtectedRoute>
                <Catalogue />
              </ProtectedRoute>
            }
          />
          <Route
            path="/shoppingcart"
            element={
              <ProtectedRoute>
                <ShoppingCart />
              </ProtectedRoute>
            }
          />
        </Routes>
      </LayoutWrapper>
    </>
  );
};

export default Navigation;
