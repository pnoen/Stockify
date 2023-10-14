import React, { useState, useEffect } from "react";
import {
  TextField,
  Button,
  Grid,
  Container,
  Typography,
  Box,
} from "@mui/material";
import { makeStyles } from "@mui/styles";
import ProductItem from "./components/ProductItem";
import ProductDetailModal from "./components/ProductDetailModal";
import { getProductsForCustomer } from "./api";
import "./styles.css";
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
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const data = await getProductsForCustomer();
        setProducts(data);
        setLoading(false);
      } catch (error) {
        console.error("Error fetching products:", error);
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  if (loading) {
    return <div>Loading...</div>; // Or any loading spinner
  }

  return (
    <Box display="flex" justifyContent="center" width="100%" pt={"5vh"}>
      <Box width="80%">
        <Typography variant="h3" gutterBottom className={classes.boldText}>
          Catalogue
        </Typography>
        <Box
          display="flex"
          alignItems="center"
          justifyContent="space-between"
          marginBottom={3}
        ></Box>
        <Box sx={{ maxHeight: "60vh", overflowY: "auto", padding:"2rem" }}>
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
            />
          )}
        </Box>
      </Box>
    </Box>
  );
}
