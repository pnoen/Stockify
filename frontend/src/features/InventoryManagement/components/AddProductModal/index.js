import React, { useState, useEffect } from "react";
import {
  Modal,
  TextField,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  Button,
  Box,
  Typography,
} from "@mui/material";
import { getBusinessCode, addProduct } from "./api";
import SuccessSnackBar from "../../../../components/Snackbars/SuccessSnackbar";

export default function AddProductModal({ open, onClose }) {
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");
  const [product, setProduct] = useState({
    name: "",
    description: "",
    quantity: 0,
    price: 0.0,
    businessCode: null,
  });

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      if (
        Math.floor(product.price) != product.price &&
        product.price.toString().split(".")[1].length > 2
      ) {
        throw new Error("Incorrect price format");
      }

      const updatedProduct = product.businessCode
        ? product
        : { ...product, businessCode: await getBusinessCode() };

      const statusCode = await addProduct(updatedProduct);
      if (statusCode >= 200 && statusCode < 300) {
        setSnackBarMessage("Added product successfully!");
        setSnackBarOpen(true);
      }
      onClose();
    } catch (error) {
      console.error("An error occurred while submitting the form:", error);
    }
  };

  const modalStyle = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 400,
    bgcolor: "background.paper",
    boxShadow: 24,
    p: 4,
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  };

  return (
    <>
      <Modal open={open} onClose={onClose}>
        <Box sx={{ ...modalStyle, p: 3 }}>
          <Typography variant="h6" align="center" marginBottom={2}>
            Add Product Form
          </Typography>
          <form onSubmit={handleSubmit}>
            <TextField
              label="Name"
              required
              value={product.name}
              onChange={(e) => setProduct({ ...product, name: e.target.value })}
              margin="normal"
              fullWidth
            />
            <TextField
              label="Description"
              required
              value={product.description}
              onChange={(e) =>
                setProduct({ ...product, description: e.target.value })
              }
              margin="normal"
              fullWidth
            />
            <TextField
              label="Quantity"
              required
              type="number"
              value={product.quantity}
              onChange={(e) =>
                setProduct({ ...product, quantity: e.target.value })
              }
              margin="normal"
              fullWidth
            />
            <TextField
              label="Price per unit"
              required
              type="number"
              value={product.price}
              inputProps={{ step: 0.01 }}
              onChange={(e) =>
                setProduct({ ...product, price: e.target.value })
              }
              margin="normal"
              fullWidth
            />
            <Box
              sx={{ display: "flex", justifyContent: "center", marginTop: 2 }}
            >
              <Button type="submit" variant="contained" color="primary">
                Submit
              </Button>
            </Box>
          </form>
        </Box>
      </Modal>
      <SuccessSnackBar
        open={snackBarOpen}
        message={snackBarMessage}
        onClose={() => setSnackBarOpen(false)}
      />
    </>
  );
}
