import React, { useState } from "react";
import {
  AppBar,
  Toolbar,
  Typography,
  Drawer,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Divider,
  Collapse,
  Badge,
  Box,
  IconButton,
} from "@mui/material";
import { ExpandLess, ExpandMore } from "@mui/icons-material";
import NotificationsIcon from "@mui/icons-material/Notifications";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import HomeIcon from "@mui/icons-material/Home";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import logo from "../../../assets/logo.png"; // Import your logo here

export default function SupplierNavigation() {
  const [profileOpen, setProfileOpen] = useState(false);

  const handleProfileClick = () => {
    setProfileOpen(!profileOpen);
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
            backgroundColor: "#FFFFFF",
            color: "#000000",
            borderRight: "2px solid #7FFFD4",
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
          <ListItem button>
            <ListItemIcon>
              <HomeIcon />
            </ListItemIcon>
            <ListItemText primary="Home" />
          </ListItem>
          <ListItem button>
            <ListItemIcon>
              <ShoppingCartIcon />
            </ListItemIcon>
            <ListItemText primary="Orders" />
          </ListItem>
          <ListItem button onClick={handleProfileClick}>
            <ListItemIcon>
              <AccountCircleIcon />
            </ListItemIcon>
            <ListItemText primary="Profile" />
            {profileOpen ? <ExpandLess /> : <ExpandMore />}
          </ListItem>
          <Collapse in={profileOpen} timeout="auto" unmountOnExit>
            <List component="div" disablePadding>
              <ListItem button style={{ paddingLeft: 32 }}>
                <ListItemText primary="Settings" />
              </ListItem>
              <ListItem button style={{ paddingLeft: 32 }}>
                <ListItemText primary="Logout" />
              </ListItem>
            </List>
          </Collapse>
        </List>
      </Drawer>
    </Box>
  );
}
