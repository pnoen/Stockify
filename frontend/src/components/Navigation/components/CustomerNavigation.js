import React, { useState } from "react";
import {
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  Button,
  Menu,
  MenuItem,
} from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import logo from "../../../assets/logoGreen.png"; // Import your logo here
import { makeStyles } from "@mui/styles";

const useStyles = makeStyles((theme) => ({
  button: {
    color: "#ffffff !important",
    "&:hover": {
      color: "#cbf5d6 !important",
    },
    "& .MuiSvgIcon-root": {
      color: "#ffffff !important",
    },
    "& .MuiSvgIcon-root:hover": {
      color: "#cbf5d6 !important",
    },
  },
  openDropdown: {
    color: "#cbf5d6 !important",
    "& .MuiSvgIcon-root": {
      color: "#cbf5d6 !important",
    },
    "&:hover": {
      color: "#c4eecf !important",
    },
  },
  logoButton: {
    textTransform: "none !important",
    color: "#cbf5d6 !important",
  },
}));

export default function CustomerNavigation() {
  const classes = useStyles();
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
    localStorage.removeItem("userToken");
    navigate("/login", { replace: true });
  };

  return (
    <AppBar
      position="static"
      color="default"
      sx={{
        borderBottom: "2px solid #1DB954",
        backgroundColor: "#1c2536",
      }}
    >
      <Toolbar sx={{ height: "5vh" }}>
        <Button
          color="inherit"
          component={Link}
          to="/home"
          className={classes.logoButton}
        >
          <img
            src={logo}
            alt="Logo"
            style={{ maxHeight: "30px", marginRight: "10px" }}
          />
          <Typography
            variant="h6"
            style={{ marginRight: "10px", color: "#cbf5d6" }}
          >
            Stockify
          </Typography>
        </Button>
        <Button
          color="inherit"
          component={Link}
          to="/orders"
          className={classes.button}
        >
          Orders
        </Button>
        <Button
          color="inherit"
          onClick={handleAdminMenu}
          className={adminOpen ? classes.openDropdown : classes.button}
        >
          Admin
        </Button>
        <div style={{ flexGrow: 1 }}></div>
        <IconButton
          color="inherit"
          component={Link}
          to="/shoppingcart"
          className={classes.button}
        >
          <ShoppingCartIcon />
        </IconButton>
        <IconButton
          edge="end"
          color="inherit"
          onClick={handleMenu}
          className={open ? classes.openDropdown : classes.button}
        >
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
