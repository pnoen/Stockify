import React, { useState } from "react";
import {
  Toolbar,
  Typography,
  Drawer,
  List,
  ListItem,
  ListItemIcon,
  ListItemButton,
  ListItemText,
  Divider,
  Collapse,
  Box,
} from "@mui/material";
import { ExpandLess, ExpandMore } from "@mui/icons-material";
import NotificationsIcon from "@mui/icons-material/Notifications";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import HomeIcon from "@mui/icons-material/Home";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import InventoryIcon from '@mui/icons-material/Inventory';
import logo from "../../../assets/logo.png"; // Import your logo here
import { Link, useNavigate } from "react-router-dom";

export default function SupplierNavigation() {
  const [openDropdowns, setOpenDropdowns] = useState({
    profile: false,
    admin: false,
  });
  const navigate = useNavigate();

  const handleDropdownClick = (dropdown) => {
    setOpenDropdowns({
      ...openDropdowns,
      [dropdown]: !openDropdowns[dropdown],
    });
  };

  const handleLogout = () => {
    localStorage.removeItem("userToken");
    navigate("/login", { replace: true });
  };

  return (
    <Box sx={{ display: "flex" }}>
      <Drawer
        variant="permanent"
        sx={{
          width: 250,
          flexShrink: 0,
          "& .MuiDrawer-paper": {
            width: 250,
            boxSizing: "border-box",
          },
        }}
      >
        <Toolbar />
        <List>
          <ListItem>
            <img
              src={logo}
              alt="Logo"
              style={{ maxHeight: "30px", marginRight: "10px" }}
            />
            <Typography variant="h6">Stockify</Typography>
          </ListItem>
          <Divider />
          <ListItemButton>
            <ListItemIcon>
              <HomeIcon />
            </ListItemIcon>
            <ListItemText primary="Home" />
          </ListItemButton>
          <ListItemButton component={Link} to="/orders">
            <ListItemIcon>
              <ShoppingCartIcon />
            </ListItemIcon>
            <ListItemText primary="Orders" />
          </ListItemButton>
          <ListItemButton component={Link} to="/inventory">
            <ListItemIcon>
              <InventoryIcon />
            </ListItemIcon>
            <ListItemText primary="Inventory" />
          </ListItemButton>
          <ListItemButton onClick={() => handleDropdownClick("admin")}>
            <ListItemIcon>
              <AccountCircleIcon />
            </ListItemIcon>
            <ListItemText primary="Admin" />
            {openDropdowns.admin ? <ExpandLess /> : <ExpandMore />}
          </ListItemButton>
          <Collapse in={openDropdowns.admin} timeout="auto" unmountOnExit>
            <List component="div" disablePadding>
              <ListItemButton
                sx={{ paddingLeft: 4 }}
                component={Link}
                to="/users"
              >
                <ListItemText primary="Users" />
              </ListItemButton>
              <ListItemButton
                sx={{ paddingLeft: 4 }}
                component={Link}
                to="/links"
              >
                <ListItemText primary="Linked Clients" />
              </ListItemButton>
            </List>
          </Collapse>
          <ListItemButton onClick={() => handleDropdownClick("profile")}>
            <ListItemIcon>
              <AccountCircleIcon />
            </ListItemIcon>
            <ListItemText primary="Profile" />
            {openDropdowns.profile ? <ExpandLess /> : <ExpandMore />}
          </ListItemButton>
          <Collapse in={openDropdowns.profile} timeout="auto" unmountOnExit>
            <List component="div" disablePadding>
              <ListItemButton sx={{ paddingLeft: 4 }} to="/settings">
                <ListItemText primary="Settings" />
              </ListItemButton>
              <ListItemButton sx={{ paddingLeft: 4 }} onClick={handleLogout}>
                <ListItemText primary="Logout" />
              </ListItemButton>
            </List>
          </Collapse>
        </List>
      </Drawer>
    </Box>
  );
}
