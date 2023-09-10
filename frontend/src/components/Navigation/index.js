import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import CustomerNavigation from "./components/CustomerNavigation";
import SupplierNavigation from "./components/SupplierNavigation";
import { checkIfSupplier } from "./api";

const Navigation = () => {
  const [isSupplier, setIsSupplier] = useState(false);
  const location = useLocation();

  useEffect(() => {
    const fetchData = async () => {
      //   const result = await checkIfSupplier();
      setIsSupplier(false);
    };
    fetchData();
  }, []);

  if (
    location.pathname === "/login" ||
    location.pathname === "/signup" ||
    location.pathname === "/"
  ) {
    return null;
  }

  return isSupplier ? <SupplierNavigation /> : <CustomerNavigation />;
};

export default Navigation;
