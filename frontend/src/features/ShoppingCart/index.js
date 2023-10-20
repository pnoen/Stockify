import React, { useState, useEffect } from "react";
import { Box, Typography, Button, Divider, Paper } from "@mui/material";
import { makeStyles } from "@mui/styles";
import OrderItem from "./components/OrderItem";
import { getDraftOrder, getOrderItems, updateDraftOrder } from "./api";
import CheckoutConfirmationDialog from "./components/CheckoutConfirmationDialog";
import SuccessSnackBar from "../../components/Snackbars/SuccessSnackbar";
import FailureSnackbar from "../../components/Snackbars/FailureSnackbar";
const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif",
  },
  scrollContainer: {
    maxHeight: "70%",
    overflowY: "auto",
    padding: "5px",
  },
}));

export default function ShoppingCart() {
  const classes = useStyles();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [orderItems, setOrderItems] = useState([]);
  const [openDialog, setOpenDialog] = useState(false);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarErrorOpen, setSnackbarErrorOpen] = useState(false);
  const [snackbarErrorMessage, setSnackbarErrorMessage] = useState("");
  const [totalCost, setTotalCost] = useState(0.0);

  const handleCheckout = async () => {
    try {
      const draftOrder = await getDraftOrder();
      const draftOrderId = parseInt(draftOrder.message);

      const response = await updateDraftOrder(
        draftOrderId,
        parseFloat(totalCost.toFixed(2))
      );

      if (response.statusCode === 200) {
        setSnackbarMessage(response.message);
        setSnackbarOpen(true);
        setOrderItems([]);
      } else {
        console.error("Error placing the order.");
        setSnackbarErrorMessage(response.message);
        setSnackbarErrorOpen(true);
      }
    } catch (err) {
      console.error("Error:", err);
    }

    setOpenDialog(false);
  };

  useEffect(() => {
    const fetchOrderData = async () => {
      try {
        const draftOrder = await getDraftOrder();
        const draftOrderId = parseInt(draftOrder.message);

        if (draftOrder && !isNaN(draftOrderId)) {
          const response = await getOrderItems(draftOrderId);
          setOrderItems(response.products);
        }
      } catch (err) {
        setError(err);
      } finally {
        setLoading(false);
      }
    };

    fetchOrderData();
  }, []);

  useEffect(() => {
    const newTotal = Array.isArray(orderItems)
      ? orderItems.reduce((acc, item) => acc + item.price * item.quantity, 0)
      : 0;
    setTotalCost(newTotal);
  }, [orderItems]);

  const handleRemove = (id) => {
    setOrderItems((prevItems) => prevItems.filter((item) => item.id !== id));
  };

  const total = Array.isArray(orderItems)
    ? orderItems.reduce((acc, item) => acc + item.price * item.quantity, 0)
    : 0;

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (
    <Box alignItems="center" display="flex" justifyContent="center" pt={"5vh"}>
      <Box width="90%">
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: "16px",
          }}
        >
          <Typography variant="h3" gutterBottom className={classes.boldText}>
            Shopping Cart
          </Typography>
        </div>

        <Typography variant="h6" gutterBottom>
          You have {orderItems.length} item{orderItems.length !== 1 ? "s" : ""}{" "}
          in your cart
        </Typography>
        <Paper
          sx={{ borderRadius: "10px", backgroundColor: "#cbf5d6" }}
        >
          <Box className={classes.scrollContainer}>
            {orderItems.map((item) => (
              <OrderItem key={item.id} product={item} onRemove={handleRemove} />
            ))}
          </Box>
        </Paper>

        <Box display="flex" justifyContent="flex-end" sx={{ margin: "1em" }}>
          <Typography variant="h6">
            <Divider sx={{ background: "#0000003a" }} />
            Subtotal ({orderItems.length} items): $
            {parseFloat(totalCost.toFixed(2))}
          </Typography>
        </Box>

        <Box display="flex" justifyContent="flex-end">
          <Button
            variant="contained"
            sx={{
              backgroundColor: "#1DB954",
              "&:hover": {
                backgroundColor: "#cbf5d6",
                color: "#1DB954",
                outlineColor: "#1DB954",
              },
              marginRight: "1em",
            }}
            onClick={() => setOpenDialog(true)}
          >
            Create Order
          </Button>
          <CheckoutConfirmationDialog
            open={openDialog}
            onClose={() => setOpenDialog(false)}
            onConfirm={handleCheckout}
          />
          <SuccessSnackBar
            open={snackbarOpen}
            message={snackbarMessage}
            onClose={() => setSnackbarOpen(false)}
          />
          <FailureSnackbar
            open={snackbarErrorOpen}
            message={snackbarErrorMessage}
            onClose={() => setSnackbarErrorOpen(false)}
          />
        </Box>
      </Box>
    </Box>
  );
}
