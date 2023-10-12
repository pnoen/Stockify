import React from "react";
import { Modal, Box, Typography, Button } from "@mui/material";

export default function ProductDetailModal({ open, onClose, product }) {
  return (
    <Modal open={open} onClose={onClose} aria-labelledby="product-detail-modal">
      <Box
        sx={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          bgcolor: "background.paper",
          boxShadow: 24,
          p: 4,
          width: "80%",
          maxWidth: "600px",
        }}
      >
        <Typography variant="h6">{product.supplierName}</Typography>
        <Typography variant="h4" gutterBottom>
          {product.itemName}
        </Typography>
        <Typography>{product.shortDescription}</Typography>
        <Typography variant="h6">Price: ${product.price}</Typography>
        <Typography>Quantity Available: {product.quantity}</Typography>
        <Box mt={3}>
          <Button
            variant="outlined"
            onClick={onClose}
            style={{ marginRight: "10px" }}
          >
            Back
          </Button>
          <Button
            variant="contained"
            style={{ backgroundColor: "#1DB954", color: "white" }}
          >
            Add to Cart
          </Button>
        </Box>
      </Box>
    </Modal>
  );
}
