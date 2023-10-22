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
import { getLinkedBusinesses } from "./api";
import LinkBusinessModal from "./components/LinkBusinessModal";
import UnlinkBusinessModal from "./components/UnlinkBusinessModal";
import LoadingSpinner from "../../../../components/LoadingSpinner";

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
  const [isUninkBusinessModalOpen, setIsUnlinkBusinessModalOpen] =
    useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true);
        const businessesList = await getLinkedBusinesses();
        setBusinesses(businessesList.businesses);
        setIsLoading(false);
      } catch (error) {
        console.error("An error occurred while fetching linked users:", error);
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
                <TableCell className="table-header-cell">
                  Business Code
                </TableCell>
                <TableCell className="table-header-cell">
                  Business Name
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {businesses
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((business) => (
                  <TableRow hover key={business.businessCode}>
                    <TableCell>{business.businessCode}</TableCell>
                    <TableCell>{business.businessName}</TableCell>
                  </TableRow>
                ))}
            </TableBody>
            <TableFooter>
              <TableRow>
                <TableCell
                  colSpan={2}
                  style={{ textAlign: "center", padding: "0.5em" }}
                >
                  <div style={{ display: "flex", justifyContent: "center" }}>
                    <LoadingSpinner
                      isLoading={isLoading}
                      props={
                        <TablePagination
                          rowsPerPageOptions={[5]}
                          component="div"
                          count={businesses.length}
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
        <LinkBusinessModal
          open={isLinkBusinessModalOpen}
          onClose={() => setIsLinkBusinessModalOpen(false)}
        />
        <UnlinkBusinessModal
          open={isUninkBusinessModalOpen}
          onClose={() => setIsUnlinkBusinessModalOpen(false)}
        />
      </div>
    </div>
  );
}
