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
  FormControl,
  Select,
  Box,
  MenuItem,
  DialogActions,
  Button,
  Divider,
  Paper,
} from "@mui/material";
import {
  fetchOrderDetailsById,
  fetchOrderItemsByOrderId,
  updateOrderStatus,
  fetchCustomerDetails,
} from "./api";
import CancelOrderConfirmationDialog from "../CancelOrderConfirmationDialog";
import "./styles.css";
import LoadingSpinner from "../../../../../../components/LoadingSpinner";

export default function BusinessOrderDetailsDialog({
  orderId,
  open,
  onClose,
  onSuccess,
}) {
  const [orderDetails, setOrderDetails] = useState(null);
  const [orderItems, setOrderItems] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(3);
  const [orderStatus, setOrderStatus] = useState("");
  const [customerDetails, setCustomerDetails] = useState(null);
  const [cancelDialogOpen, setCancelDialogOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [isCancelLoading, setIsCancelLoading] = useState(false);

  useEffect(() => {
    const fetchDetails = async () => {
      if (orderId) {
        try {
          setIsLoading(true);
          const detailsResponse = await fetchOrderDetailsById(orderId);
          setOrderDetails(detailsResponse.order);
          setOrderStatus(detailsResponse.order?.orderStatus);
          const customerResponse = await fetchCustomerDetails(
            detailsResponse.order?.customerId
          );
          setCustomerDetails(customerResponse);

          const itemsResponse = await fetchOrderItemsByOrderId(orderId);
          setOrderItems(itemsResponse.products);
          setIsLoading(false);
        } catch (error) {
          console.error("Error fetching order details:", error);
          setIsLoading(false);
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

  const handleStatusChange = async (event) => {
    const newStatus = event.target.value;
    setOrderStatus(newStatus);
    try {
      const updateResponse = await updateOrderStatus(
        orderDetails.id,
        newStatus
      );
      onSuccess(updateResponse.message);
      onClose();
    } catch (error) {
      console.error("Error updating order status:", error);
    }
  };

  const handleCancelConfirm = async () => {
    try {
      setIsCancelLoading(true);
      const updateResponse = await updateOrderStatus(
        orderDetails.id,
        "CANCELLED"
      );
      setOrderStatus("CANCELLED");
      onSuccess(updateResponse.message);
      setIsCancelLoading(false);
      setCancelDialogOpen(false);
      onClose();
    } catch (error) {
      console.error("Error cancelling order:", error);
      setIsCancelLoading(false);
    }
  };

  return (
    <Dialog
      open={open}
      onClose={onClose}
      PaperProps={{
        sx: {
          width: "50vw",
          maxWidth: "100%",
          borderRadius: "20px",
          bgcolor: "background.paper",
          padding: "1em 0.5em",
        },
      }}
    >
      <DialogTitle>
        <Divider />
        <Typography variant="h4" align="center" sx={{ margin: "0.5em 0" }}>
          Order #{orderDetails?.id}
        </Typography>
        <Divider />
      </DialogTitle>
      <DialogContent>
        <Box sx={{ padding: "0 1em" }}>
          <Typography>
            <span style={{ fontWeight: "bold" }}>Customer Name: </span>
            {customerDetails?.firstName} {customerDetails?.lastName}
          </Typography>
          <Box display="flex" alignItems="center" sx={{ paddingTop: "0.5em" }}>
            <Typography sx={{ fontWeight: "bold", paddingRight: "0.5em" }}>
              Order Status:
            </Typography>

            <FormControl variant="outlined" sx={{ minWidth: 120 }}>
              <Select
                value={orderStatus}
                onChange={handleStatusChange}
                sx={{ height: "2em" }}
              >
                <MenuItem value={"PURCHASED"}>Purchased</MenuItem>
                <MenuItem value={"PACKING"}>Packing</MenuItem>
                <MenuItem value={"AWAITING_SHIPMENT"}>
                  Awaiting Shipment
                </MenuItem>
                <MenuItem value={"COMPLETE"}>Complete</MenuItem>
              </Select>
            </FormControl>
          </Box>
          <Typography sx={{ paddingTop: "0.5em" }}>
            <span style={{ fontWeight: "bold" }}>Order Date: </span>
            {orderDetails?.orderDate}
          </Typography>
          {orderDetails?.completionDate ? (
            <Typography sx={{ paddingTop: "0.5em" }}>
              <span style={{ fontWeight: "bold" }}>Completion Date: </span>
              {orderDetails?.completionDate}
            </Typography>
          ) : null}

          <Typography sx={{ fontWeight: "bold", paddingTop: "0.5em" }}>
            Order Items:
          </Typography>
          <Paper sx={{ paddingTop: "0.2em" }}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell className="table-header-cell">Name</TableCell>
                  <TableCell className="table-header-cell">
                    Description
                  </TableCell>
                  <TableCell className="table-header-cell">Price ($)</TableCell>
                  <TableCell className="table-header-cell">Quantity</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {orderItems
                  .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                  .map((item) => (
                    <TableRow hover key={item.id}>
                      <TableCell>{item.name}</TableCell>
                      <TableCell>{item.description}</TableCell>
                      <TableCell>{item.price}</TableCell>
                      <TableCell>{item.quantity}</TableCell>
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
                            rowsPerPageOptions={[3]}
                            component="div"
                            count={orderItems.length}
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
        </Box>
      </DialogContent>
      <DialogActions sx={{ justifyContent: "center" }}>
        <LoadingSpinner
          isLoading={isCancelLoading}
          props={
            <Button
              variant="outlined"
              color="error"
              onClick={() => setCancelDialogOpen(true)}
            >
              Cancel Order
            </Button>
          }
        />
      </DialogActions>
      <CancelOrderConfirmationDialog
        open={cancelDialogOpen}
        onClose={() => setCancelDialogOpen(false)}
        onConfirm={handleCancelConfirm}
      />
    </Dialog>
  );
}
