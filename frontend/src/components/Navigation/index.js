import React, { useState } from "react";
import { useLocation } from "react-router-dom";
import CustomerNavigation from "./components/CustomerNavigation";
import SupplierNavigation from "./components/SupplierNavigation";
import { checkIfBusiness } from "./api";

const Navigation = () => {
  const [isBusiness, setisBusiness] = useState(false);
  const location = useLocation();

  const fetchData = async () => {
    const businessCheck = await checkIfBusiness();
    if (businessCheck === 200) {
      setisBusiness(true);
    } else {
      setisBusiness(false);
    }
  };

  if (
    location.pathname === "/login" ||
    location.pathname === "/signup" ||
    location.pathname === "/"
  ) {
    return null;
  } else {
    fetchData();
    return isBusiness ? <SupplierNavigation /> : <CustomerNavigation />;
  }
};

export default Navigation;
