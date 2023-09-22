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
import { makeStyles } from "@mui/styles";
import { getLinkedUsers } from "./api";
import LinkUserModal from "./components/LinkUserModal";

const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif", // Replace "Your Nice Font" with the desired font
  },
}));

export default function LinkedUsersPage() {
  const classes = useStyles();
  const [users, setUsers] = useState([]);
  const [isLinkUserModalOpen, setisLinkUserModalOpen] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const usersList = await getLinkedUsers();
        setUsers(usersList.users);
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
          }}
        >
          <Typography variant="h3" gutterBottom className={classes.boldText}>
            Linked Users
          </Typography>
          <Box>
            <Button
              variant="contained"
              style={{ marginRight: "8px", backgroundColor: "#1DB954" }}
              onClick={() => setisLinkUserModalOpen(true)}
            >
              Link User
            </Button>

            <Button
              variant="contained"
              style={{ marginRight: "8px", backgroundColor: "#ce595f" }}
              onClick={() => console.log("unlink")}
            >
              Unlink User
            </Button>
          </Box>
        </div>

        <Paper>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell className="table-header-cell">User ID</TableCell>
                <TableCell className="table-header-cell">First Name</TableCell>
                <TableCell className="table-header-cell">Last Name</TableCell>
                <TableCell className="table-header-cell">Email</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((user) => (
                <TableRow hover key={user.id} style={{ cursor: "pointer" }}>
                  <TableCell>{user.id}</TableCell>
                  <TableCell>{user.firstName}</TableCell>
                  <TableCell>{user.lastName}</TableCell>
                  <TableCell>{user.email}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Paper>
        <LinkUserModal
          open={isLinkUserModalOpen}
          onClose={() => setisLinkUserModalOpen(false)}
        />
      </div>
    </div>
  );
}
