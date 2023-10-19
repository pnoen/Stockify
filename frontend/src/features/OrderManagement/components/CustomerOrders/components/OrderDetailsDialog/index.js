// CustomerOrders/components/OrderDetailsDialog/index.js

import React, { useEffect, useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  Typography,
  List,
  ListItem,
  ListItemText,
  Button,
} from "@mui/material";
import { fetchOrderDetailsById } from "./api";

export default function OrderDetailsDialog({ orderId, open, onClose }) {
  const [orderDetails, setOrderDetails] = useState(null);

  useEffect(() => {
    const fetchDetails = async () => {
      if (orderId) {
        try {
          const response = await fetchOrderDetailsById(orderId);
          setOrderDetails(response.order);
        } catch (error) {
          console.error("Error fetching order details:", error);
        }
      }
    };
    fetchDetails();
  }, [orderId]);

  return (
    <Dialog
      open={open}
      onClose={onClose}
      PaperProps={{
        sx: {
          width: "70vw", 
          maxWidth: "100%", 
          height: "70vh", 
          maxHeight: "100%", 
        },
      }}
    >
      <DialogTitle>Order Details</DialogTitle>
      <DialogContent>
        <Typography variant="h6">Order ID: {orderDetails?.id}</Typography>
        <Typography variant="body1">
          Business: {orderDetails?.businessName}
        </Typography>

        <Typography variant="h6" style={{ marginTop: "1rem" }}>
          Order Items:
        </Typography>
        <List>
          {orderDetails?.orderItems?.map((item) => (
            <ListItem key={item.itemId}>
              <ListItemText
                primary={item.itemName}
                secondary={`Price: ${item.itemPrice}`}
              />
            </ListItem>
          ))}
        </List>
      </DialogContent>
    
    </Dialog>
  );
}
