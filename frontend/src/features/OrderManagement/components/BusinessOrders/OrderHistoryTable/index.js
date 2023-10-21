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
import { fetchOrderHistory } from "./api";
import BusinessCompleteOrderDetailsDialog from "../components/BusinessCompleteOrderDetailsDialog";
import "./styles.css";
import LoadingSpinner from "../../../../../components/LoadingSpinner";

export default function OrderHistoryTable() {
  const [completedOrders, setCompletedOrders] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [selectedOrderId, setSelectedOrderId] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleOpenDialog = (orderId) => {
    setSelectedOrderId(orderId);
  };

  const handleCloseDialog = () => {
    setSelectedOrderId(null);
  };

  useEffect(() => {
    const getOrders = async () => {
      try {
        setIsLoading(true);
        const response = await fetchOrderHistory();
        setCompletedOrders(response.orderList || []);
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching orders:", error);
        setIsLoading(false);
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

  return (
    <div style={{ width: "100%" }}>
      <Typography variant="h6" gutterBottom>
        Order History
      </Typography>
      <Paper>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell className="table-header-cell">Order ID</TableCell>
              <TableCell className="table-header-cell">
                Completion Date
              </TableCell>
              <TableCell className="table-header-cell">
                Total Cost ($)
              </TableCell>
              <TableCell className="table-header-cell">Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {completedOrders.map((order) => (
              <TableRow
                hover
                key={order.id}
                onClick={() => handleOpenDialog(order.id)}
                style={{ cursor: "pointer" }}
              >
                <TableCell>{order.id}</TableCell>
                <TableCell>{order.completionDate}</TableCell>
                <TableCell>{order.totalCost}</TableCell>
                <TableCell>{order.orderStatus}</TableCell>
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
                        count={completedOrders.length}
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
      <BusinessCompleteOrderDetailsDialog
        orderId={selectedOrderId}
        open={!!selectedOrderId}
        onClose={handleCloseDialog}
      />
    </div>
  );
}
