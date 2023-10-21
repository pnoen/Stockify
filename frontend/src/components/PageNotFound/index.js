import React from "react";
import { Typography, Button, Box } from "@mui/material";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";
import { useNavigate } from "react-router-dom";

const PageNotFound = () => {
  const navigate = useNavigate();

  const handleHomePageRedirect = () => {
    const token = localStorage.getItem("userToken");
    const isAuthenticated = Boolean(token);
    const redirectPath = isAuthenticated ? "/orders" : "/";
    navigate(redirectPath);
  };

  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      height="85vh"
      textAlign="center"
      gap={2}
    >
      <ErrorOutlineIcon style={{ fontSize: 80, color: "#1DB954" }} />
      <Typography
        variant="h3"
        component="div"
        color="black"
        sx={{ fontWeight: "bold" }}
      >
        404
      </Typography>
      <Typography variant="h4" component="div" sx={{ fontWeight: "bold" }}>
        Page Not Found
      </Typography>
      <Typography variant="body1" component="p">
        Oops! We couldn't find the page that you're looking for. Please check
        and try again.
      </Typography>
      <Button
        variant="outlined"
        onClick={handleHomePageRedirect}
        sx={{
          color: "#1DB954",
          borderColor: "#1DB954",
          "&:hover": {
            borderColor: "#1DB954",
            backgroundColor: "#1DB9540A",
          },
        }}
      >
        Home Page
      </Button>
    </Box>
  );
};

export default PageNotFound;
