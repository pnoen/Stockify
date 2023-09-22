import React, { useState, useEffect } from "react";
import {
  Modal,
  Box,
  Typography,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button,
} from "@mui/material";
import { getUserDetails, updateUser, deleteUser } from "./api";
import { getRoles } from "../AddUserModal/api";
import SuccessSnackBar from "../../../../components/Snackbars/SuccessSnackbar";

export default function EditUserModal({ open, onClose, userId }) {
  const [roles, setRoles] = useState([]);
  const [user, setUser] = useState({ role: "" });
  const [firstName, setFirstName] = useState("");
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userDetails = await getUserDetails(userId);
        const rolesList = await getRoles();

        setFirstName(userDetails.firstName);
        // setUser({ firstName: userDetails.firstName, role: userDetails.role });
        setRoles(rolesList);
      } catch (error) {
        console.error("An error occurred while fetching data:", error);
      }
    };

    if (userId) fetchData();
  }, [userId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const statusCode = await updateUser(userId, user);
      if (statusCode >= 200 && statusCode < 300) {
        setSnackBarMessage("Role updated successfully!");
        setSnackBarOpen(true);
      }
      onClose();
    } catch (error) {
      console.error("An error occurred while submitting the form:", error);
    }
  };

  const handleDelete = async () => {
    try {
      const statusCode = await deleteUser(userId);
      if (statusCode >= 200 && statusCode < 300) {
        setSnackBarMessage("User deleted successfully!");
        setSnackBarOpen(true);
      }
      onClose();
    } catch (error) {
      console.error("An error occurred while deleting the user:", error);
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
            Edit {firstName}'s Role
          </Typography>
          <form onSubmit={handleSubmit}>
            <FormControl
              fullWidth
              required
              margin="normal"
              style={{ minWidth: "200px" }}
            >
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

            <Box display="flex" justifyContent="space-between" mt={2}>
              <Button
                type="submit"
                variant="contained"
                color="primary"
                style={{ marginRight: "8px" }}
              >
                Update
              </Button>
              <Button
                type="button"
                variant="contained"
                sx={{ backgroundColor: "#e66e6e" }}
                onClick={handleDelete}
              >
                Delete User
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
