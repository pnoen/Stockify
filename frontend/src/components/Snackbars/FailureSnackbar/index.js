import React from "react";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";
import { styled } from "@mui/material/styles";

const StyledAlert = styled(Alert)(({ theme }) => ({
  ".MuiAlert-icon": {
    color: "white",
  },
  backgroundColor: "#fa3e3e",
  color: "white",
}));

export default function FailureSnackbar({ open, message, onClose }) {
  return (
    <Snackbar
      open={open}
      autoHideDuration={3000}
      onClose={onClose}
      anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
    >
      <StyledAlert onClose={onClose} severity="error">
        {message}
      </StyledAlert>
    </Snackbar>
  );
}
