import React, { useState, useEffect } from "react";
import {
  Modal,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  Button,
  Box,
  Typography,
} from "@mui/material";
import { getBusinessCode, removeClientLink } from "./api";
import { getLinkedClients } from "../../api";
import SuccessSnackBar from "../../../../../../components/Snackbars/SuccessSnackbar";
import LoadingSpinner from "../../../../../../components/LoadingSpinner";

export default function UnlinkClientModal({ open, onClose }) {
  const [clients, setClients] = useState([]);
  const [link, setLink] = useState({
    businessCode: null,
    userId: "",
  });
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true);
        const clientsList = await getLinkedClients();
        setClients(clientsList.users);
        setIsLoading(false);
      } catch (error) {
        console.error("An error occurred while fetching clients:", error);
        setIsLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setIsLoading(true);
      const updatedLink = link.businessCode
        ? link
        : { ...link, businessCode: await getBusinessCode() };

      const statusCode = await removeClientLink(updatedLink);
      if (statusCode >= 200 && statusCode < 300) {
        setSnackBarMessage("Removed link successfully!");
        setSnackBarOpen(true);
      }
      setIsLoading(false);
      onClose();
    } catch (error) {
      console.error("An error occurred while submitting the form:", error);
      setIsLoading(false);
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
    borderRadius: "20px",
  };

  return (
    <>
      <Modal open={open} onClose={onClose}>
        <Box sx={{ ...modalStyle, p: 3 }}>
          <Typography variant="h6" align="center" marginBottom={2}>
            Remove Link Form
          </Typography>
          <form onSubmit={handleSubmit}>
            <FormControl fullWidth required margin="normal">
              <InputLabel id="client-label">Client</InputLabel>
              <Select
                labelId="client-label"
                value={link.userId}
                onChange={(e) => setLink({ ...link, userId: e.target.value })}
                sx={{ width: "6em" }}
              >
                {clients.map((client) => (
                  <MenuItem key={client.id} value={client.id}>
                    {client.email}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <Box
              sx={{ display: "flex", justifyContent: "center", marginTop: 2 }}
            >
              <LoadingSpinner
                isLoading={isLoading}
                props={
                  <Button type="submit" variant="contained" color="primary">
                    Submit
                  </Button>
                }
              />
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
