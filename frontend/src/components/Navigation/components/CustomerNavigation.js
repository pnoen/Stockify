import React, { useState } from "react";
import {
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  Button,
  Badge,
  Menu,
  MenuItem,
} from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import NotificationsIcon from "@mui/icons-material/Notifications";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import logo from "../../../assets/logo.png"; // Import your logo here

export default function CustomerNavigation() {
  const [anchorEl, setAnchorEl] = React.useState(null);
  const [adminAnchorEl, setAdminAnchorEl] = useState(null);
  const navigate = useNavigate();
  const open = Boolean(anchorEl);
  const adminOpen = Boolean(adminAnchorEl);

  const handleMenu = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleAdminMenu = (event) => {
    setAdminAnchorEl(event.currentTarget);
  };

  const handleAdminClose = () => {
    setAdminAnchorEl(null);
  };

  const handleLogout = () => {
    localStorage.removeItem("userToken"); // Remove userToken from localStorage
    navigate("/login", { replace: true }); // Navigate to login page
  };

  return (
    <AppBar
      position="static"
      color="default"
      sx={{ borderBottom: "2px solid #acf5ad" }}
    >
      <Toolbar>
        <img
          src={logo}
          alt="Logo"
          style={{ maxHeight: "30px", marginRight: "10px" }}
        />
        <Typography variant="h6" style={{ marginRight: "10px" }}>
          Stockify
        </Typography>
        <Button color="inherit" component={Link} to="/home">
          Home
        </Button>
        <Button color="inherit" component={Link} to="/orders">
          Orders
        </Button>
        <Button color="inherit" component={Link} to="/shoppingcart">
          Shopping Cart
        </Button>
        <Button color="inherit" onClick={handleAdminMenu}>
          Admin
        </Button>
        <div style={{ flexGrow: 1 }}></div>
        <IconButton color="inherit">
          <Badge badgeContent={4} color="error">
            <NotificationsIcon />
          </Badge>
        </IconButton>
        <IconButton edge="end" color="inherit" onClick={handleMenu}>
          <AccountCircleIcon />
        </IconButton>
        <Menu anchorEl={anchorEl} open={open} onClose={handleClose}>
          <MenuItem onClick={handleClose} component={Link} to="/settings">
            Settings
          </MenuItem>
          <MenuItem onClick={handleLogout}>Logout</MenuItem>
        </Menu>
        <Menu
          anchorEl={adminAnchorEl}
          open={adminOpen}
          onClose={handleAdminClose}
        >
          <MenuItem onClick={handleAdminClose} component={Link} to="/links">
            Linked Businesses
          </MenuItem>
        </Menu>
      </Toolbar>
    </AppBar>
  );
}
