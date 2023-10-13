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
import { getBusinessCode, removeProduct } from "./api";
import { getInventory } from "../../api";
import SuccessSnackBar from "../../../../components/Snackbars/SuccessSnackbar";

export default function RemoveProductModal({ open, onClose }) {
  const [products, setProducts] = useState([]);
  const [product, setProduct] = useState({
    id: "",
  });

  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const businessCode = await getBusinessCode();
        const inventory = await getInventory(businessCode);
        setProducts(inventory.product);
      } catch (error) {
        console.error("An error occurred while fetching inventory:", error);
      }
    };
    fetchData();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const statusCode = await removeProduct(product);
      if (statusCode >= 200 && statusCode < 300) {
        setSnackBarMessage("Removed product successfully!");
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
            Remove Product Form
          </Typography>
          <form onSubmit={handleSubmit}>
            <FormControl fullWidth required margin="normal">
              <InputLabel id="product-label">Product</InputLabel>
              <Select
                labelId="product-label"
                value={product.id}
                onChange={(e) => setProduct({ ...product, id: e.target.value })}
              >
                {products.map((inventoryProduct) => (
                  <MenuItem
                    key={inventoryProduct.id}
                    value={inventoryProduct.id}
                  >
                    {inventoryProduct.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
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
