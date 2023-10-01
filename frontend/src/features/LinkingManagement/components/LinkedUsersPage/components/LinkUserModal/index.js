import React, { useState, useEffect } from "react";
import {
  Modal,
  TextField,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  Button,
  Box,
  Typography,
} from "@mui/material";
import { getBusinessCode, createUserLink } from "./api";
import SuccessSnackBar from "../../../../../../components/Snackbars/SuccessSnackbar"

export default function LinkUserModal({ open, onClose }) {
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");
  const [link, setLink] = useState({
    businessCode: null,
    firstName: "",
    lastName: "",
    email: "",
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const updatedLink = link.businessCode
        ? link
        : { ...link, businessCode: await getBusinessCode() };

      const statusCode = await createUserLink(updatedLink);
      if (statusCode >= 200 && statusCode < 300) {
        setSnackBarMessage("Created link successfully!");
        setSnackBarOpen(true);
      }
      onClose();
    } catch (error) {
      console.error("An error occurred while submitting the form:", error);
    }
  };

  const modalStyle = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 400,
    bgcolor: "background.paper",
    boxShadow: 24,
    p: 4,
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  };

  return (
    <>
      <Modal open={open} onClose={onClose}>
        <Box sx={{ ...modalStyle, p: 3 }}>
          <Typography variant="h6" align="center" marginBottom={2}>
            Create Link Form
          </Typography>
          <form onSubmit={handleSubmit}>
            <TextField
              label="First Name"
              required
              value={link.firstName}
              onChange={(e) => setLink({ ...link, firstName: e.target.value })}
              margin="normal"
              fullWidth
            />
            <TextField
              label="Last Name"
              required
              value={link.lastName}
              onChange={(e) => setLink({ ...link, lastName: e.target.value })}
              margin="normal"
              fullWidth
            />
            <TextField
              label="Email"
              required
              type="email"
              value={link.email}
              onChange={(e) => setLink({ ...link, email: e.target.value })}
              margin="normal"
              fullWidth
            />
            <Box
              sx={{ display: "flex", justifyContent: "center", marginTop: 2 }}
            >
              <Button type="submit" variant="contained" color="primary">
                Submit
              </Button>
            </Box>
          </form>
        </Box>
      </Modal>
      <SuccessSnackBar
        open={snackBarOpen}
        message={snackBarMessage}
        onClose={() => setSnackBarOpen(false)}
      />
    </>
  );
}
