import React, { useEffect, useState } from "react";
import { Typography } from "@mui/material";
import "./styles.css";
import CurrentOrderTable from "../CustomerOrders/CurrentOrderTable";
import OrderHistoryTable from "../CustomerOrders/OrderHistoryTable";
import { makeStyles } from "@mui/styles";

const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif", // Replace "Your Nice Font" with the desired font
  },
}));

export default function CustomerOrderPage() {
  const classes = useStyles();

  return (
    <div
      style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        paddingTop: "5vh",
      }}
    >
      <div style={{ maxWidth: "90%", width: "100%" }}>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: "16px",
          }}
        >
          <Typography variant="h3" gutterBottom className={classes.boldText}>
            Orders
          </Typography>
        </div>

        <CurrentOrderTable />
        <OrderHistoryTable />
      </div>
    </div>
  );
}
