import React, { useState } from "react";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import { loginUser } from "./api";
import {
  Box,
  TextField,
  Button,
  CssBaseline,
  Typography,
  Grid,
  Link,
} from "@mui/material";
import { green } from "@mui/material/colors";
import logo from "../../assets/logo.png"; // Import your logo here
import LoadingSpinner from "../../components/LoadingSpinner";

export default function LoginPage() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });
  const [isLoading, setIsLoading] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setIsLoading(true);
      const result = await loginUser(formData.email, formData.password);
      setIsLoading(false);
      if (result === 200) {
        navigate("/orders", { replace: true });
      }
    } catch (error) {
      console.error("An error occurred during login:", error);
      setIsLoading(false);
    }
  };

  return (
    <Grid container component="main" sx={{ height: "100vh" }}>
      <CssBaseline />
      <Grid
        item
        xs={12}
        sm={8}
        md={6}
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Typography component="h1" variant="h2" fontFamily={"monospace"}>
            Login
          </Typography>
          <Box
            component="form"
            onSubmit={handleSubmit}
            sx={{ mt: 1 }}
          >
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              type="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              autoFocus
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              value={formData.password}
              onChange={handleInputChange}
            />
            <LoadingSpinner
              isLoading={isLoading}
              props={
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx={{
                    mt: 3,
                    mb: 2,
                    backgroundColor: green[500],
                    "&:hover": {
                      backgroundColor: green[700],
                    },
                  }}
                >
                  LogIn
                </Button>
              }
            />

            <Box sx={{ mt: 2, alignSelf: "flex-start" }}>
              <Link
                component={RouterLink}
                to="/signup"
                sx={{ color: green[500] }}
              >
                Don't have an account? Sign up
              </Link>
            </Box>
          </Box>
        </Box>
      </Grid>
      <Grid
        item
        xs={false}
        sm={4}
        md={4}
        sx={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <img
          src={logo}
          alt="Logo"
          style={{ maxHeight: "100%", maxWidth: "100%" }}
        />
        <Typography component="h1" variant="h2" fontFamily={"monospace"}>
          tockify
        </Typography>
      </Grid>
    </Grid>
  );
}
