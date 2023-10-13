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
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          height: "auto",
          borderRadius: "20px",
        }}
      >
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            position: "relative",
          }}
        >
          {/* Product Image */}
          <Box
            component="img"
            src={product.imageUrl} 
            alt={product.name}
            sx={{ maxWidth: "100%", maxHeight: "300px" }}
          />
        </Box>
       
        <Typography
          variant="body2"
          sx={{
            alignSelf: "flex-start",
            backgroundColor: "rgba(255, 255, 255, 0.6)",
            padding: "0.5em",
            borderRadius: "5px",
            marginTop: "0.5em",
          }}
        >
          {product.businessCode}
        </Typography>
        {/* Centered Name and Description */}
        <Typography variant="h4" gutterBottom align="center">
          {product.name}
        </Typography>
        <Typography align="center">{product.description}</Typography>
        <Typography variant="h6">Price: ${product.price}</Typography>
        <Typography>Quantity Available: {product.quantity}</Typography>

        <Box
          mt={3}
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <Button variant="outlined" onClick={onClose}>
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
