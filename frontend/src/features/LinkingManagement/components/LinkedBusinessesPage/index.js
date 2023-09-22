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
import { getLinkedBusinesses } from "./api";

const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif", // Replace "Your Nice Font" with the desired font
  },
}));

export default function LinkedBusinessesPage() {
  const classes = useStyles();
  const [businesses, setBusinesses] = useState([]);
  const [isLinkBusinessModalOpen, setIsLinkBusinessModalOpen] = useState(false);
  const [isUninkBusinessModalOpen, setIsUnlinkBusinessModalOpen] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const businessesList = await getLinkedBusinesses();
        setBusinesses(businessesList.businesses);
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
            Linked Businesses
          </Typography>
          <Box>
            <Button
              variant="contained"
              style={{ marginRight: "8px", backgroundColor: "#1DB954" }}
              onClick={() => setIsLinkBusinessModalOpen(true)}
            >
              Link Business
            </Button>

            <Button
              variant="contained"
              style={{ marginRight: "8px", backgroundColor: "#ce595f" }}
              onClick={() => setIsUnlinkBusinessModalOpen(true)}
            >
              Unlink Business
            </Button>
          </Box>
        </div>

        <Paper>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell className="table-header-cell">Business Code</TableCell>
                <TableCell className="table-header-cell">Business Name</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {businesses.map((business) => (
                <TableRow hover key={business.businessCode} style={{ cursor: "pointer" }}>
                  <TableCell>{business.businessCode}</TableCell>
                  <TableCell>{business.businessName}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Paper>
      </div>
    </div>
  );
}
