import React from "react";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";
import { styled } from "@mui/material/styles";

const StyledAlert = styled(Alert)(({ theme }) => ({
  ".MuiAlert-icon": {
    color: "white",
  },
  backgroundColor: "#0c7300",
  color: "white",
}));

export default function SuccessSnackBar({ open, message, onClose }) {
  return (
    <Snackbar
      open={open}
      autoHideDuration={6000}
      onClose={onClose}
      anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
    >
      <StyledAlert onClose={onClose} severity="success" sx={{ width: "100%" }}>
        {message}
      </StyledAlert>
    </Snackbar>
  );
}
