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
import { getLinkedClients } from "./api";
import LinkClientModal from "./components/LinkClientModal";
import UnlinkClientModal from "./components/UnlinkClientModal";


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

  useEffect(() => {
    const fetchData = async () => {
      try {
        const clientsList = await getLinkedClients();
        setClients(clientsList.users);
      } catch (error) {
        console.error("An error occurred while fetching linked clients:", error);
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
              {clients.map((client) => (
                <TableRow hover key={client.id} style={{ cursor: "pointer" }}>
                  <TableCell>{client.id}</TableCell>
                  <TableCell>{client.firstName}</TableCell>
                  <TableCell>{client.lastName}</TableCell>
                  <TableCell>{client.email}</TableCell>
                </TableRow>
              ))}
            </TableBody>
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
