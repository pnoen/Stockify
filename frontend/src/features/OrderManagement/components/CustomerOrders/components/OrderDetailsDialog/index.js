import React, { useEffect, useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TableFooter,
  TablePagination,
} from "@mui/material";
import { fetchOrderDetailsById, fetchOrderItemsByOrderId } from "./api";
import "./styles.css";

export default function OrderDetailsDialog({ orderId, open, onClose }) {
  const [orderDetails, setOrderDetails] = useState(null);
  const [orderItems, setOrderItems] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(3);

  useEffect(() => {
    const fetchDetails = async () => {
      if (orderId) {
        try {
          const detailsResponse = await fetchOrderDetailsById(orderId);
          setOrderDetails(detailsResponse.order);

          const itemsResponse = await fetchOrderItemsByOrderId(orderId);
          setOrderItems(itemsResponse.products);
        } catch (error) {
          console.error("Error fetching order details:", error);
        }
      }
    };
    fetchDetails();
  }, [orderId]);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  return (
    <Dialog
      open={open}
      onClose={onClose}
      PaperProps={{
        sx: {
          width: "50vw",
          maxWidth: "100%",
          height: "70vh",
          maxHeight: "100%",
          borderRadius: "20px",
        },
      }}
    >
      <DialogTitle>
        <Typography variant="h3" align="center" sx={{ fontWeight: "bold" }}>
          Order #{orderDetails?.id}
        </Typography>
      </DialogTitle>
      <DialogContent>
        <Typography sx={{ fontWeight: "bold" }} variant="h6">
          Business Name: {orderDetails?.businessName}
        </Typography>
        <Typography
          sx={{ fontWeight: "bold", paddingTop: "0.5rem" }}
          variant="h6"
        >
          Order Status: {orderDetails?.orderStatus}
        </Typography>

        <Typography
          variant="h6"
          sx={{ fontWeight: "bold" }}
          style={{ paddingTop: "4rem" }}
        >
          Order Items
        </Typography>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell className="table-header-cell">Name</TableCell>
              <TableCell className="table-header-cell">Description</TableCell>
              <TableCell className="table-header-cell">Price</TableCell>
              <TableCell className="table-header-cell">Quantity</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {orderItems.map((item) => (
              <TableRow key={item.id}>
                <TableCell>{item.name}</TableCell>
                <TableCell>{item.description}</TableCell>
                <TableCell>${item.price}</TableCell>
                <TableCell>{item.quantity}</TableCell>
              </TableRow>
            ))}
          </TableBody>
          <TableFooter>
            <TableRow>
              <TableCell colSpan={4} style={{ textAlign: "center" }}>
                <div style={{ display: "flex", justifyContent: "center" }}>
                  <TablePagination
                    rowsPerPageOptions={[3]}
                    component="div"
                    count={orderItems.length}
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
      </DialogContent>
    </Dialog>
  );
}
