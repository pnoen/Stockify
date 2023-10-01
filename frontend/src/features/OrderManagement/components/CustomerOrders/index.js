import React, { useEffect, useState } from "react";
import { Typography } from "@mui/material";
import "./styles.css";
import CurrentOrderTable from "../CustomerOrders/CurrentOrderTable";
import OrderHistoryTable from "../CustomerOrders/OrderHistoryTable";

export default function CustomerOrderPage() {
  return (
    <div
      style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        flexDirection: "column",
        paddingTop: "5vh",
      }}
    >
      <Typography variant="h4" gutterBottom>
        Orders
      </Typography>

      <CurrentOrderTable />
      <OrderHistoryTable />
    </div>
  );
}
