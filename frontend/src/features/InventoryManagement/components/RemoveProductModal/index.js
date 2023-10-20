import React, { useState, useEffect } from "react";
import {
  Modal,
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
import LoadingSpinner from "../../../../components/LoadingSpinner";

export default function RemoveProductModal({ open, onClose }) {
  const [products, setProducts] = useState([]);
  const [product, setProduct] = useState({
    id: "",
  });

  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true);
        const businessCode = await getBusinessCode();
        const inventory = await getInventory(businessCode);
        setProducts(inventory.product);
        setIsLoading(false);
      } catch (error) {
        console.error("An error occurred while fetching inventory:", error);
        setIsLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setIsLoading(true);
      const statusCode = await removeProduct(product);
      if (statusCode >= 200 && statusCode < 300) {
        setSnackBarMessage("Removed product successfully!");
        setSnackBarOpen(true);
      }
      setIsLoading(false);
      onClose();
    } catch (error) {
      console.error("An error occurred while submitting the form:", error);
      setIsLoading(false);
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
    borderRadius: "20px",
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
                sx={{ minWidth: "7em" }}
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
              <LoadingSpinner
                isLoading={isLoading}
                props={
                  <Button type="submit" variant="contained" color="primary">
                    Submit
                  </Button>
                }
              />
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
