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
import "./styles.css";
import { getUsers } from "./api";

export default function UserManagementPage() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const usersList = await getUsers();
        setUsers(usersList);
      } catch (error) {
        console.error("An error occurred while fetching users:", error);
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
          }}
        >
          <Typography variant="h4" gutterBottom>
            Users
          </Typography>
          <Box>
            <Button
              variant="contained"
              color="primary"
              style={{ marginRight: "8px" }}
            >
              Add User
            </Button>
            <Button variant="contained" color="secondary">
              Delete User
            </Button>
          </Box>
        </div>

        <Paper>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell className="table-header-cell">User ID</TableCell>
                <TableCell className="table-header-cell">Firstname</TableCell>
                <TableCell className="table-header-cell">Lastname</TableCell>
                <TableCell className="table-header-cell">Email</TableCell>
                <TableCell className="table-header-cell">Role</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((user) => (
                <TableRow
                  hover
                  key={user.id}
                  onClick={() => console.log("Row clicked:", user.id)}
                  style={{ cursor: "pointer" }}
                >
                  <TableCell>{user.id}</TableCell>
                  <TableCell>{user.firstName}</TableCell>
                  <TableCell>{user.lastName}</TableCell>
                  <TableCell>{user.email}</TableCell>
                  <TableCell>{user.role}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Paper>
      </div>
    </div>
  );
}
