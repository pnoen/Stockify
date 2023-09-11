import React, { useState } from "react";
import { Link as RouterLink } from "react-router-dom";
import {
  Box,
  TextField,
  Button,
  CssBaseline,
  Typography,
  Grid,
  Link,
  FormControl,
  FormLabel,
  FormControlLabel,
  Switch,
} from "@mui/material";
import { green } from "@mui/material/colors";
import logo from "../../assets/logo.png"; // Import your logo here

export default function SignupPage() {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    isBusiness: false,
    businessName: "",
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
            width: {
              xs: "90%",
              sm: "80%",
              md: "60%",
            },
          }}
        >
          <Typography component="h1" variant="h2" fontFamily={"monospace"}>
            Sign Up
          </Typography>
          <Box
            component="form"
            onSubmit={handleSubmit}
            noValidate
            sx={{ mt: 1 }}
          >
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  variant="outlined"
                  margin="normal"
                  required
                  fullWidth
                  id="firstName"
                  label="First Name"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleInputChange}
                  autoFocus
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  variant="outlined"
                  margin="normal"
                  required
                  fullWidth
                  id="lastName"
                  label="Last Name"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleInputChange}
                />
              </Grid>
            </Grid>
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
            <FormControl component="fieldset" sx={{ mt: 2 }}>
              <FormLabel component="legend">Are you a business?</FormLabel>
              <FormControlLabel
                control={
                  <Switch
                    checked={formData.isBusiness}
                    onChange={() =>
                      setFormData({
                        ...formData,
                        isBusiness: !formData.isBusiness,
                      })
                    }
                    name="isBusiness"
                  />
                }
                label={formData.isBusiness ? "Yes" : "No"}
              />
            </FormControl>
            {formData.isBusiness && (
              <TextField
                variant="outlined"
                margin="normal"
                required
                fullWidth
                id="businessName"
                label="Business Name"
                name="businessName"
                value={formData.businessName}
                onChange={handleInputChange}
              />
            )}
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
              Sign Up
            </Button>
            <Box sx={{ mt: 2, alignSelf: "flex-start" }}>
              <Link
                component={RouterLink}
                to="/login"
                sx={{ color: green[500] }}
              >
                Already have an account? Log in
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
