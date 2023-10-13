import React, { useState } from "react";
import { Box, Typography, Button } from "@mui/material";
import { makeStyles } from "@mui/styles";
import OrderItem from "./components/OrderItem";
const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif",
  },
  scrollContainer: {
    maxHeight: "70%", // Or any suitable height
    overflowY: "auto",
    marginBottom: 30,
  },
}));

export default function ShoppingCart() {
  const classes = useStyles();

  const dummyData = [
    {
      id: 1,
      name: "Product 1",
      supplier: "Supplier Name",
      quantity: 1,
      price: 681,
    },
    {
      id: 2,
      name: "Product 2",
      supplier: "Supplier Name",
      quantity: 1,
      price: 681,
    },
    {
      id: 3,
      name: "Product 2",
      supplier: "Supplier Name",
      quantity: 1,
      price: 681,
    },
    {
      id: 4,
      name: "Product 4",
      supplier: "Supplier Name",
      quantity: 1,
      price: 681,
    },
    {
      id: 5,
      name: "Product 5",
      supplier: "Supplier Name",
      quantity: 1,
      price: 681,
    },
  ];

  const [orderItems, setOrderItems] = useState(dummyData);

  const handleRemove = (id) => {
    setOrderItems((prevItems) => prevItems.filter((item) => item.id !== id));
  };

  const total = orderItems.reduce(
    (acc, item) => acc + item.price * item.quantity,
    0
  );

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
            Subtotal ({orderItems.length} items): ${total}
          </Typography>
          <Button variant="contained" color="primary">
            Proceed To Checkout
          </Button>
        </Box>
      </Box>
    </Box>
  );
}
