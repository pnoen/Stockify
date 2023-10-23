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
import { makeStyles } from "@mui/styles";
import { getLinkedClients } from "./api";
import LinkClientModal from "./components/LinkClientModal";
import UnlinkClientModal from "./components/UnlinkClientModal";
import LoadingSpinner from "../../../../components/LoadingSpinner";

const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif", // Replace "Your Nice Font" with the desired font
  },
}));

export default function LinkedClientsPage() {
  const classes = useStyles();
  const [clients, setClients] = useState([]);
  const [isLinkClientModalOpen, setIsLinkClientModalOpen] = useState(false);
  const [isUnlinkClientModalOpen, setIsUnlinkClientModalOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true);
        const clientsList = await getLinkedClients();
        setClients(clientsList.users);
        setIsLoading(false);
      } catch (error) {
        console.error(
          "An error occurred while fetching linked clients:",
          error
        );
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
            Linked Clients
          </Typography>
          <Box>
            <Button
              variant="contained"
              style={{ marginRight: "8px", backgroundColor: "#1DB954" }}
              onClick={() => setIsLinkClientModalOpen(true)}
            >
              Link Client
            </Button>

            <Button
              variant="contained"
              style={{ marginRight: "8px", backgroundColor: "#ce595f" }}
              onClick={() => setIsUnlinkClientModalOpen(true)}
            >
              Unlink Client
            </Button>
          </Box>
        </div>

        <Paper>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell className="table-header-cell">Client ID</TableCell>
                <TableCell className="table-header-cell">First Name</TableCell>
                <TableCell className="table-header-cell">Last Name</TableCell>
                <TableCell className="table-header-cell">Email</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {clients
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((client) => (
                  <TableRow hover key={client.id}>
                    <TableCell>{client.id}</TableCell>
                    <TableCell>{client.firstName}</TableCell>
                    <TableCell>{client.lastName}</TableCell>
                    <TableCell>{client.email}</TableCell>
                  </TableRow>
                ))}
            </TableBody>
            <TableFooter>
              <TableRow>
                <TableCell
                  colSpan={4}
                  style={{ textAlign: "center", padding: "0.5em" }}
                >
                  <div style={{ display: "flex", justifyContent: "center" }}>
                    <LoadingSpinner
                      isLoading={isLoading}
                      props={
                        <TablePagination
                          rowsPerPageOptions={[5]}
                          component="div"
                          count={clients.length}
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
        <LinkClientModal
          open={isLinkClientModalOpen}
          onClose={() => setIsLinkClientModalOpen(false)}
        />
        <UnlinkClientModal
          open={isUnlinkClientModalOpen}
          onClose={() => setIsUnlinkClientModalOpen(false)}
        />
      </div>
    </div>
  );
}
