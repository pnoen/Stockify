import React, { useState, useEffect } from "react";
import { Divider, Typography } from "@mui/material";
import { makeStyles } from "@mui/styles";
import EditDetailsForm from "./components/EditDetailsForm";
import EditPasswordForm from "./components/EditPasswordForm";

const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif", // Replace "Your Nice Font" with the desired font
  },
}));

export default function SettingsPage() {
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
            Settings
          </Typography>
        </div>

        <EditDetailsForm />
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            margin: "1.5em 0",
          }}
        >
          <Divider sx={{ width: "75%" }} />
        </div>
        <EditPasswordForm />
      </div>
    </div>
  );
}
