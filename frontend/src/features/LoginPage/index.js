import React, { useState } from "react";
import { Link as RouterLink, useNavigate } from "react-router-dom";
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

export default function LoginPage() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Form Data:", formData);
    localStorage.setItem("userToken", "XXX");
    navigate("/orders", { replace: true });
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
            Log in
          </Typography>
          <Box
            component="form"
            onSubmit={handleSubmit}
            noValidate
            sx={{ mt: 1 }}
          >
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
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
              Log In
            </Button>
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
          Tockify
        </Typography>
      </Grid>
    </Grid>
  );
}
