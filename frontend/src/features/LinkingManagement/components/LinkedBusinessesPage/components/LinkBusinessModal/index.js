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
import { getBusinessCode, createBusinessLink } from "./api";
import SuccessSnackBar from "../../../../../../components/Snackbars/SuccessSnackbar";

export default function LinkBusinessModal({ open, onClose }) {
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");
  const [link, setLink] = useState({
    businessCode: "",
    email: "",
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const updatedLink = link.businessCode
        ? link
        : { ...link, businessCode: await getBusinessCode() };

      const statusCode = await createBusinessLink(updatedLink);
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
              label="Business Code"
              type="number"
              required
              value={link.businessCode}
              onChange={(e) => {
                setLink({ ...link, businessCode: e.target.value });
              }}
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
