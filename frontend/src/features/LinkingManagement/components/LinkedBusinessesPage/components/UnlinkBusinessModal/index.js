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
import { getBusinessCode, removeBusinessLink, removeUserLink } from "./api";
import { getLinkedBusinesses } from "../../api";
import SuccessSnackBar from "../../../../../../components/Snackbars/SuccessSnackbar";

export default function UnlinkBusinessModal({ open, onClose }) {
  const [businesses, setBusinesses] = useState([]);
  const [link, setLink] = useState({
    businessCode: "",
    email: "",
  });
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const businessesList = await getLinkedBusinesses();
        setBusinesses(businessesList.businesses);
      } catch (error) {
        console.error("An error occurred while fetching businesses:", error);
      }
    };
    fetchData();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const updatedLink = link.businessCode
        ? link
        : { ...link, businessCode: await getBusinessCode() };

      const statusCode = await removeBusinessLink(updatedLink);
      if (statusCode >= 200 && statusCode < 300) {
        setSnackBarMessage("Removed link successfully!");
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
            Remove Link Form
          </Typography>
          <form onSubmit={handleSubmit}>
            <FormControl fullWidth required margin="normal">
              <InputLabel id="business-label">Business</InputLabel>
              <Select
                labelId="business-label"
                value={link.businessCode}
                onChange={(e) =>
                  setLink({ ...link, businessCode: e.target.value })
                }
              >
                {businesses.map((business) => (
                  <MenuItem
                    key={business.businessCode}
                    value={business.businessCode}
                  >
                    {business.businessName}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
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
