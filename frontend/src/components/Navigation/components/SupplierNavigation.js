import React, { useState, useEffect } from "react";
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
import { Dashboard, ExpandLess, ExpandMore } from "@mui/icons-material";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import AdminPanelSettingsIcon from "@mui/icons-material/AdminPanelSettings";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import InventoryIcon from "@mui/icons-material/Inventory";
import logo from "../../../assets/logoGreen.png";
import { getBusinessCode } from "./api";
import { Link, useNavigate } from "react-router-dom";
import { makeStyles } from "@mui/styles";

const useStyles = makeStyles((theme) => ({
  itemButton: {
    color: "#ffffff !important",
    "& .MuiSvgIcon-root": {
      color: "#ffffff !important",
    },
    "&:hover": {
      backgroundColor: "#151e2f !important",
    },
  },
  openedListButton: {
    color: "#ffffff !important",
    "& .MuiSvgIcon-root": {
      color: "#ffffff !important",
    },
    "&:hover": {
      backgroundColor: "#071021 !important",
    },
    backgroundColor: "#0e1728 !important",
  },
  listCollapsed: {
    backgroundColor: "#151e2f !important",
  },
  itemButtonCollapsed: {
    color: "#ffffff !important",
    "&:hover": {
      backgroundColor: "#0e1728 !important",
    },
  },
}));

export default function SupplierNavigation() {
  const classes = useStyles();
  const [businessCode, setBusinessCode] = useState("");
  const [openDropdowns, setOpenDropdowns] = useState({
    profile: false,
    admin: false,
  });
  const navigate = useNavigate();
  useEffect(() => {
    const fetchBusinessCode = async () => {
      try {
        const code = await getBusinessCode();
        setBusinessCode(code);
      } catch (error) {
        console.error("Failed to fetch business code:", error);
      }
    };

    fetchBusinessCode();
  }, []);
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
          width: "15vw",
          flexShrink: 0,
          "& .MuiDrawer-paper": {
            width: "15vw",
            boxSizing: "border-box",
            backgroundColor: "#1c2536",
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
            <Typography variant="h6" style={{ color: "#cbf5d6" }}>
              Stockify
            </Typography>
          </ListItem>
          <Divider sx={{ background: "#ffffff5d" }} />
          <ListItemButton
            component={Link}
            to="/dashboard"
            className={classes.itemButton}
          >
            <ListItemIcon>
              <Dashboard />
            </ListItemIcon>
            <ListItemText primary="Dashboard" />
          </ListItemButton>
          <ListItemButton
            component={Link}
            to="/orders"
            className={classes.itemButton}
          >
            <ListItemIcon>
              <ShoppingCartIcon />
            </ListItemIcon>
            <ListItemText primary="Orders" />
          </ListItemButton>
          <ListItemButton
            component={Link}
            to="/inventory"
            className={classes.itemButton}
          >
            <ListItemIcon>
              <InventoryIcon />
            </ListItemIcon>
            <ListItemText primary="Inventory" />
          </ListItemButton>
          <ListItemButton
            onClick={() => handleDropdownClick("admin")}
            className={
              openDropdowns.admin
                ? classes.openedListButton
                : classes.itemButton
            }
          >
            <ListItemIcon>
              <AdminPanelSettingsIcon />
            </ListItemIcon>
            <ListItemText primary="Admin" />
            {openDropdowns.admin ? <ExpandLess /> : <ExpandMore />}
          </ListItemButton>
          <Collapse
            in={openDropdowns.admin}
            timeout="auto"
            unmountOnExit
            className={classes.listCollapsed}
          >
            <List component="div" disablePadding>
              <ListItemButton
                sx={{ paddingLeft: 4 }}
                component={Link}
                to="/users"
                className={classes.itemButtonCollapsed}
              >
                <ListItemText primary="Users" />
              </ListItemButton>
              <ListItemButton
                sx={{ paddingLeft: 4 }}
                component={Link}
                to="/links"
                className={classes.itemButtonCollapsed}
              >
                <ListItemText primary="Linked Clients" />
              </ListItemButton>
            </List>
          </Collapse>
          <ListItemButton
            onClick={() => handleDropdownClick("profile")}
            className={
              openDropdowns.profile
                ? classes.openedListButton
                : classes.itemButton
            }
          >
            <ListItemIcon>
              <AccountCircleIcon />
            </ListItemIcon>
            <ListItemText primary="Profile" />
            {openDropdowns.profile ? <ExpandLess /> : <ExpandMore />}
          </ListItemButton>
          <Collapse
            in={openDropdowns.profile}
            timeout="auto"
            unmountOnExit
            className={classes.listCollapsed}
          >
            <List component="div" disablePadding>
              <ListItemButton
                sx={{ paddingLeft: 4 }}
                component={Link}
                to="/settings"
                className={classes.itemButtonCollapsed}
              >
                <ListItemText primary="Settings" />
              </ListItemButton>
              <ListItemButton
                sx={{ paddingLeft: 4 }}
                onClick={handleLogout}
                className={classes.itemButtonCollapsed}
              >
                <ListItemText primary="Logout" />
              </ListItemButton>
            </List>
          </Collapse>
        </List>
        <Box
          sx={{
            mt: "auto",
            mb: 2,
            color: "#cbf5d6",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          Business Code: {businessCode}
        </Box>
      </Drawer>
    </Box>
  );
}
