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
import { getBusinessCode, removeBusinessLink } from "./api";
import { getLinkedBusinesses } from "../../api";
import SuccessSnackBar from "../../../../../../components/Snackbars/SuccessSnackbar";
import LoadingSpinner from "../../../../../../components/LoadingSpinner";

export default function UnlinkBusinessModal({ open, onClose }) {
  const [businesses, setBusinesses] = useState([]);
  const [link, setLink] = useState({
    businessCode: "",
    email: "",
  });
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true);
        const businessesList = await getLinkedBusinesses();
        setBusinesses(businessesList.businesses);
        setIsLoading(false);
      } catch (error) {
        console.error("An error occurred while fetching businesses:", error);
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

      const statusCode = await removeBusinessLink(updatedLink);
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
              <InputLabel id="business-label">Business</InputLabel>
              <Select
                labelId="business-label"
                value={link.businessCode}
                onChange={(e) =>
                  setLink({ ...link, businessCode: e.target.value })
                }
                sx={{ minWidth: "8em" }}
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
