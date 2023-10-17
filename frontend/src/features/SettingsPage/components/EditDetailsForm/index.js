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
  Paper,
} from "@mui/material";
import { checkIfBusiness, getUserId, editUser } from "./api";
import SuccessSnackBar from "../../../../components/Snackbars/SuccessSnackbar";
import LoadingSpinner from "../../../../components/LoadingSpinner";

export default function EditDetailsForm() {
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");
  const [user, setUser] = useState({
    id: null,
    firstName: "",
    lastName: "",
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
    business: "",
  });
  const [isBusiness, setIsBusiness] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true);
        const businessCheck = await checkIfBusiness();
        setIsBusiness(businessCheck === 200);
        setIsLoading(false);
      } catch (error) {
        console.error("Error checking business status:", error);
        setIsBusiness(false);
        setIsLoading(false);
      }
    };
    fetchData();
  }, []);

  if (isBusiness === null) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setIsLoading(true);
      const updatedUser = user.id ? user : { ...user, id: await getUserId() };

      const statusCode = await editUser(updatedUser);
      if (statusCode >= 200 && statusCode < 300) {
        setSnackBarMessage("Edited user successfully!");
        setSnackBarOpen(true);
      }
      setIsLoading(false);
    } catch (error) {
      console.error("An error occurred while submitting the form:", error);
      setIsLoading(false);
    }
  };

  return (
    <>
      <Paper style={{ marginBottom: "32px" }}>
        <div style={{ width: "100%", padding: "16px" }}>
          <Typography variant="h6" gutterBottom>
            User Details
          </Typography>

          <form onSubmit={handleSubmit}>
            <TextField
              label="First Name"
              value={user.firstName}
              onChange={(e) => setUser({ ...user, firstName: e.target.value })}
              margin="normal"
              fullWidth
            />
            <TextField
              label="Last Name"
              value={user.lastName}
              onChange={(e) => setUser({ ...user, lastName: e.target.value })}
              margin="normal"
              fullWidth
            />
            {isBusiness ? (
              <TextField
                label="Business Name"
                value={user.business}
                onChange={(e) => setUser({ ...user, business: e.target.value })}
                margin="normal"
                fullWidth
              />
            ) : null}

            <Box
              sx={{ display: "flex", justifyContent: "flex-end", marginTop: 1 }}
            >
              <LoadingSpinner
                isLoading={isLoading}
                props={
                  <Button
                    type="submit"
                    variant="contained"
                    style={{ backgroundColor: "#1DB954", color: "white" }}
                  >
                    Update
                  </Button>
                }
              />
            </Box>
          </form>
        </div>
      </Paper>
      <SuccessSnackBar
        open={snackBarOpen}
        message={snackBarMessage}
        onClose={() => setSnackBarOpen(false)}
      />
    </>
  );
}
