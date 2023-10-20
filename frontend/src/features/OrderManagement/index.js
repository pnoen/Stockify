import React, { useEffect, useState } from "react";
import CustomerOrderPage from "./components/CustomerOrders";
import BusinessOrderPage from "./components/BusinessOrders";
import { checkIfBusiness } from "./api";
const OrderPage = () => {
  const [isBusiness, setIsBusiness] = useState(null);
  useEffect(() => {
    const fetchData = async () => {
      try {
        const businessCheck = await checkIfBusiness();
        setIsBusiness(businessCheck === 200);
      } catch (error) {
        console.error("Error checking business status:", error);
        setIsBusiness(false);
      }
    };
    fetchData();
  }, []);

  if (isBusiness === null) return null;

  return isBusiness ? <BusinessOrderPage /> : <CustomerOrderPage />;
};

export default OrderPage;
