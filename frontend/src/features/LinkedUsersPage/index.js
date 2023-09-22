import React, { useState, useEffect } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Paper,
  Typography,
  Button,
  Box,
  Checkbox,
  ButtonBase,
} from "@mui/material";
import { makeStyles } from "@mui/styles";
import { getLinkedUsers } from "./api";

const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif", // Replace "Your Nice Font" with the desired font
  },
}));

export default function LinkedUsersPage() {
  const classes = useStyles();
  const [users, setUsers] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      


      try {
        const usersList = await getLinkedUsers();
        setUsers(usersList);
        console.log(users);
      } catch (error) {
        console.error("An error occurred while fetching linked users:", error);
      }
    };
    fetchData();
  }, []);

  return (
    <div
      style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        paddingTop: "5vh",
      }}
    >
      <div style={{ maxWidth: "90%", width: "100%" }}>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: "16px",
            background:"red"
          }}
        >
          <Typography variant="h3" gutterBottom className={classes.boldText}>
            Linked Users
          </Typography>
        </div>
      </div>
    </div>
  );
}
