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

export default function AddUserModal({ open, onClose }) {
  const [roles, setRoles] = useState([]);
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    email: "",
    role: "",
    businessCode: null,
  });

  useEffect(() => {
    const fetchRoles = async () => {
      try {
        const rolesList = await getRoles();

        setRoles(rolesList);
      } catch (error) {
        console.error("An error occurred while fetching roles:", error);
      }
    };
    fetchRoles();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const updatedUser = user.businessCode
        ? user
        : { ...user, businessCode: await getBusinessCode() };

      console.log(updatedUser);
      await addUser(updatedUser);
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
          <Button type="submit" variant="contained" color="primary">
            Submit
          </Button>
        </form>
      </Box>
    </Modal>
  );
}
