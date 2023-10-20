import React, { useState, useEffect } from "react";
import {
  Grid,
  Typography,
  Box,
} from "@mui/material";
import { makeStyles } from "@mui/styles";
import ProductItem from "./components/ProductItem";
import ProductDetailModal from "./components/ProductDetailModal";
import { getProductsForCustomer } from "./api";
import SuccessSnackBar from "../../components/Snackbars/SuccessSnackbar";
import FailureSnackbar from "../../components/Snackbars/FailureSnackbar";
import "./styles.css";
import LoadingSpinner from "../../components/LoadingSpinner";

const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif",
  },
}));

export default function Catalogue() {
  const classes = useStyles();
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [products, setProducts] = useState([]);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");
  const [snackbarErrorOpen, setSnackbarErrorOpen] = useState(false);
  const [snackbarErrorMessage, setSnackbarErrorMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setIsLoading(true);
        const data = await getProductsForCustomer();
        setProducts(data);
        setIsLoading(false);
      } catch (error) {
        console.error("Error fetching products:", error);
        setIsLoading(false);
      }
    };

    fetchProducts();
  }, []);


  return (
    <div
      style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        paddingTop: "5vh",
      }}
    >
      <div style={{ maxWidth: "90%", width: "100%" }}>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: "16px",
          }}
        >
          <Typography variant="h3" gutterBottom className={classes.boldText}>
            Catalogue
          </Typography>
        </div>
        <Box sx={{ maxHeight: "65vh", overflowY: "auto", padding: "1rem" }}>
          <Grid container spacing={2}>
            {products.map((product) => (
              <Grid item xs={2} key={product.id}>
                <div
                  onClick={() => {
                    setSelectedProduct(product);
                    setIsModalOpen(true);
                  }}
                >
                  <ProductItem product={product} />
                </div>
              </Grid>
            ))}
          </Grid>

          {selectedProduct && (
            <ProductDetailModal
              open={isModalOpen}
              onClose={() => {
                setIsModalOpen(false);
                setSelectedProduct(null);
              }}
              product={selectedProduct}
              onAddToCartSuccess={(message) => {
                setSnackbarMessage(message);
                setSnackbarOpen(true);
              }}
              onAddToCartFailure={(message) => {
                setSnackbarErrorMessage(message);
                setSnackbarErrorOpen(true);
              }}
            />
          )}
          <LoadingSpinner isLoading={isLoading} />
        </Box>
      </div>
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
    </div>
  );
}
