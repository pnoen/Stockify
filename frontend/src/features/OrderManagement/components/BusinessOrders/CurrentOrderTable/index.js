import React, { useEffect, useState } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TableFooter,
  Paper,
  Typography,
  TablePagination,
} from "@mui/material";
import { fetchOpenOrders } from "./api";
import "./styles.css";
import BusinessOrderDetailsDialog from "../components/BusinessOrderDetailsDialog";
import SuccessSnackBar from "../../../../../components/Snackbars/SuccessSnackbar";

export default function CurrentOrderTable() {
  const [openOrders, setOpenOrders] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [selectedOrderId, setSelectedOrderId] = useState(null);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  useEffect(() => {
    const getOrders = async () => {
      try {
        const response = await fetchOpenOrders();
        setOpenOrders(response.orderList || []);
      } catch (error) {
        console.error("Error fetching orders:", error);
      }
    };

    getOrders();
  }, []);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const handleOpenDialog = (orderId) => {
    setSelectedOrderId(orderId);
  };

  const handleCloseDialog = () => {
    console.log("closed");
    setSelectedOrderId(null);
  };

  return (
    <div style={{ maxWidth: "90%", width: "100%" }}>
      <Typography variant="h6" gutterBottom>
        Open Orders
      </Typography>
      <Paper>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell className="table-header-cell">Order ID</TableCell>
              <TableCell className="table-header-cell">Order Date</TableCell>
              <TableCell className="table-header-cell">
                Total Cost ($)
              </TableCell>
              <TableCell className="table-header-cell">Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {openOrders.map((order) => (
              <TableRow
                key={order.id}
                onClick={() => handleOpenDialog(order.id)}
                sx={{
                  cursor: "pointer",
                  "&:hover": {
                    backgroundColor: "lightgray",
                  },
                }}
              >
                <TableCell>{order.id}</TableCell>
                <TableCell>{order.orderDate}</TableCell>
                <TableCell>{order.totalCost}</TableCell>
                <TableCell>{order.orderStatus}</TableCell>
              </TableRow>
            ))}
          </TableBody>
          <TableFooter>
            <TableRow>
              <TableCell colSpan={4} style={{ textAlign: "center" }}>
                <div style={{ display: "flex", justifyContent: "center" }}>
                  <TablePagination
                    rowsPerPageOptions={[5]}
                    component="div"
                    count={openOrders.length}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onPageChange={handleChangePage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                  />
                </div>
              </TableCell>
            </TableRow>
          </TableFooter>
        </Table>
      </Paper>
      <BusinessOrderDetailsDialog
        orderId={selectedOrderId}
        open={!!selectedOrderId}
        onClose={handleCloseDialog}
        onSuccess={(message) => {
          setSnackbarMessage(message);
          setSnackbarOpen(true);
        }}
      />
      <SuccessSnackBar
        open={snackbarOpen}
        message={snackbarMessage}
        onClose={() => setSnackbarOpen(false)}
      />
    </div>
  );
}
