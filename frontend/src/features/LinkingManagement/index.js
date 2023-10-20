import React, { useEffect, useState } from "react";
import LinkedBusinessesPage from "./components/LinkedBusinessesPage";
import LinkedClientsPage from "./components/LinkedClientsPage";
import { checkIfBusiness } from "./api";

export default function LinkingPage() {
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

  return isBusiness ? <LinkedClientsPage /> : <LinkedBusinessesPage />;
};
