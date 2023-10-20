import React, { useState } from "react";
import {
  TextField,
  Button,
  Box,
  Typography,
  Paper,
} from "@mui/material";
import { makeStyles } from "@mui/styles";
import { getUserId, editUser } from "./api";
import SuccessSnackBar from "../../../../components/Snackbars/SuccessSnackbar";
import LoadingSpinner from "../../../../components/LoadingSpinner";

const useStyles = makeStyles((theme) => ({
  input: {
    background: "#cbf5d68f",
  },
}));

export default function EditPasswordForm() {
  const classes = useStyles();
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
  const [isLoading, setIsLoading] = useState(false);

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
      <Paper style={{ borderRadius: "10px" }}>
        <div style={{ width: "100%", padding: "16px" }}>
          <Typography variant="h6" gutterBottom>
            Password
          </Typography>

          <form onSubmit={handleSubmit}>
            <TextField
              label="Current Password"
              value={user.currentPassword}
              onChange={(e) =>
                setUser({ ...user, currentPassword: e.target.value })
              }
              margin="normal"
              InputProps={{ className: classes.input }}
              fullWidth
            />
            <TextField
              label="New Password"
              value={user.newPassword}
              onChange={(e) =>
                setUser({ ...user, newPassword: e.target.value })
              }
              margin="normal"
              InputProps={{ className: classes.input }}
              fullWidth
            />
            <TextField
              label="Confirm Password"
              value={user.confirmPassword}
              onChange={(e) =>
                setUser({ ...user, confirmPassword: e.target.value })
              }
              margin="normal"
              InputProps={{ className: classes.input }}
              fullWidth
            />
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
