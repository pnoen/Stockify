import React, { useState, useEffect } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Paper,
  Typography,
  Button,
  Box,
  Checkbox,
  ButtonBase,
} from "@mui/material";
import { makeStyles } from "@mui/styles";
import { checkIfBusiness } from "./api";
import BussinessEditDetailsForm from "./components/BusinessEditDetailsForm";
import CustomerEditDetailsForm from "./components/CustomerEditDetailsForm";

const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif", // Replace "Your Nice Font" with the desired font
  },
}));

export default function SettingsPage() {
  const classes = useStyles();
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
            Settings
          </Typography>
        </div>

        {isBusiness ? <BussinessEditDetailsForm /> : <CustomerEditDetailsForm />}
      </div>
    </div>
  );
}
