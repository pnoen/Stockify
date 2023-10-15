import React, { useState, useEffect } from "react";
import { Box, Typography, Button } from "@mui/material";
import { makeStyles } from "@mui/styles";
import OrderItem from "./components/OrderItem";
import { getDraftOrder, getOrderItems, updateDraftOrder } from "./api";
import CheckoutConfirmationDialog from "./components/CheckoutConfirmationDialog";
import SuccessSnackBar from "../../components/Snackbars/SuccessSnackbar";
const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif",
  },
  scrollContainer: {
    maxHeight: "70%",
    overflowY: "auto",
    marginBottom: 30,
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

  const handleCheckout = async () => {
    try {
      const draftOrder = await getDraftOrder();
      const draftOrderId = parseInt(draftOrder.message);

      if (draftOrder && !isNaN(draftOrderId)) {
        const response = await updateDraftOrder(draftOrderId);

        if (response.statusCode === 200) {
          setSnackbarMessage("Order placed successfully!");
          setSnackbarOpen(true);
          setOrderItems([]);
        } else {
          console.error("Error placing the order.");
        }
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

  const handleRemove = (id) => {
    setOrderItems((prevItems) => prevItems.filter((item) => item.id !== id));
  };

  const total = Array.isArray(orderItems)
    ? orderItems.reduce((acc, item) => acc + item.price * item.quantity, 0)
    : 0;

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (
    <Box display="flex" justifyContent="center" pt={"5vh"}>
      <Box width="80%">
        <Typography variant="h3" gutterBottom className={classes.boldText}>
          Shopping Cart
        </Typography>
        <Typography variant="h6" gutterBottom>
          You have {orderItems.length} item{orderItems.length !== 1 ? "s" : ""}{" "}
          in your cart
        </Typography>

        <Box className={classes.scrollContainer}>
          {orderItems.map((item) => (
            <OrderItem key={item.id} product={item} onRemove={handleRemove} />
          ))}
        </Box>

        <Box display="flex" justifyContent="space-between">
          <Typography variant="h6">
            Subtotal ({orderItems.length} items): $
            {parseFloat(total.toFixed(2))}
          </Typography>
          <Button
            variant="contained"
            sx={{
              backgroundColor: "#1DB954",
              "&:hover": {
                backgroundColor: "#cbf5d6",
                color: "#1DB954",
                outlineColor: "#1DB954",
              },
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
        </Box>
      </Box>
    </Box>
  );
}
