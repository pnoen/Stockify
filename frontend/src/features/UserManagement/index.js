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
  TableFooter,
  TablePagination,
} from "@mui/material";
import "./styles.css";
import { getUsers } from "./api";
import AddUserModal from "./components/AddUserModal";
import EditUserModal from "./components/EditUserModal";
import { makeStyles } from "@mui/styles";
import LoadingSpinner from "../../components/LoadingSpinner";

const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif", // Replace "Your Nice Font" with the desired font
  },
}));

export default function UserManagementPage() {
  const classes = useStyles();
  const [users, setUsers] = useState([]);
  const [isAddUserModalOpen, setIsAddUserModalOpen] = useState(false);
  const [isEditUserModalOpen, setIsEditUserModalOpen] = useState(false);
  const [selectedUserId, setSelectedUserId] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true);
        const usersList = await getUsers();
        setUsers(usersList);
        setIsLoading(false);
      } catch (error) {
        console.error("An error occurred while fetching users:", error);
        setIsLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

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
            Users
          </Typography>
          <Box>
            <Button
              variant="contained"
              style={{ marginRight: "8px", backgroundColor: "#1DB954" }}
              onClick={() => setIsAddUserModalOpen(true)}
            >
              Add User
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
                <TableCell className="table-header-cell">Role</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((user) => (
                  <TableRow
                    hover
                    key={user.id}
                    onClick={() => {
                      setSelectedUserId(user.id);
                      setIsEditUserModalOpen(true);
                    }}
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
            <TableFooter>
              <TableRow>
                <TableCell
                  colSpan={5}
                  style={{ textAlign: "center", padding: "0.5em" }}
                >
                  <div style={{ display: "flex", justifyContent: "center" }}>
                    <LoadingSpinner
                      isLoading={isLoading}
                      props={
                        <TablePagination
                          rowsPerPageOptions={[5]}
                          component="div"
                          count={users.length}
                          rowsPerPage={rowsPerPage}
                          page={page}
                          onPageChange={handleChangePage}
                          onRowsPerPageChange={handleChangeRowsPerPage}
                        />
                      }
                    />
                  </div>
                </TableCell>
              </TableRow>
            </TableFooter>
          </Table>
        </Paper>
        <AddUserModal
          open={isAddUserModalOpen}
          onClose={() => setIsAddUserModalOpen(false)}
        />
        <EditUserModal
          open={isEditUserModalOpen}
          onClose={() => {
            setIsEditUserModalOpen(false);
            setSelectedUserId(null);
          }}
          userId={selectedUserId}
        />
      </div>
    </div>
  );
}
