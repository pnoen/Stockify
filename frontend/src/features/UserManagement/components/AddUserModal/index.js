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
import { getRoles, addUser, getBusinessCode } from "./api";
import SuccessSnackBar from "../../../../components/Snackbars/SuccessSnackbar";
import LoadingSpinner from "../../../../components/LoadingSpinner";

export default function AddUserModal({ open, onClose }) {
  const [roles, setRoles] = useState([]);
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    email: "",
    role: "",
    businessCode: null,
  });
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchRoles = async () => {
      try {
        setIsLoading(true);
        const rolesList = await getRoles();

        setRoles(rolesList);
        setIsLoading(false);
      } catch (error) {
        console.error("An error occurred while fetching roles:", error);
        setIsLoading(false);
      }
    };
    fetchRoles();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setIsLoading(true);
      const updatedUser = user.businessCode
        ? user
        : { ...user, businessCode: await getBusinessCode() };

      const statusCode = await addUser(updatedUser);
      if (statusCode >= 200 && statusCode < 300) {
        setSnackBarMessage("User added successfully!");
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
            Add User Form
          </Typography>
          <form onSubmit={handleSubmit}>
            <TextField
              label="First Name"
              required
              value={user.firstName}
              onChange={(e) => setUser({ ...user, firstName: e.target.value })}
              margin="normal"
              fullWidth
            />
            <TextField
              label="Last Name"
              required
              value={user.lastName}
              onChange={(e) => setUser({ ...user, lastName: e.target.value })}
              margin="normal"
              fullWidth
            />
            <TextField
              label="Email"
              required
              type="email"
              value={user.email}
              onChange={(e) => setUser({ ...user, email: e.target.value })}
              margin="normal"
              fullWidth
            />
            <FormControl fullWidth required margin="normal">
              <InputLabel id="role-label">Role</InputLabel>
              <Select
                labelId="role-label"
                value={user.role}
                onChange={(e) => setUser({ ...user, role: e.target.value })}
              >
                {roles.map((role) => (
                  <MenuItem key={role.id} value={role.id}>
                    {role.label}
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
