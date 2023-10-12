import React, { useState } from "react";
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
import "./styles.css";
const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif",
  },
}));
export default function Catalogue() {
  const classes = useStyles();
  const dummyData = [
    {
      id: 1,
      supplierName: "Supplier A",
      itemName: "Item 1",
      shortDescription: "Description 1",
    },
    {
      id: 2,
      supplierName: "Supplier B",
      itemName: "Item 2",
      shortDescription: "Description 2",
    },
    {
      id: 3,
      supplierName: "Supplier C",
      itemName: "Item 3",
      shortDescription: "Description 3",
    },

    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 1,
      supplierName: "Supplier A",
      itemName: "Item 1",
      shortDescription: "Description 1",
    },
    {
      id: 2,
      supplierName: "Supplier B",
      itemName: "Item 2",
      shortDescription: "Description 2",
    },
    {
      id: 3,
      supplierName: "Supplier C",
      itemName: "Item 3",
      shortDescription: "Description 3",
    },

    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 1,
      supplierName: "Supplier A",
      itemName: "Item 1",
      shortDescription: "Description 1",
    },
    {
      id: 2,
      supplierName: "Supplier B",
      itemName: "Item 2",
      shortDescription: "Description 2",
    },
    {
      id: 3,
      supplierName: "Supplier C",
      itemName: "Item 3",
      shortDescription: "Description 3",
    },

    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
    {
      id: 4,
      supplierName: "Supplier D",
      itemName: "Item 4",
      shortDescription: "Description 4",
    },
  ];

  const [searchTerm, setSearchTerm] = useState("");
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

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
        <Box style={{ maxHeight: "60vh", overflowY: "auto" }}>
          <Grid container spacing={2}>
            {dummyData.map((product) => (
              <Grid item xs={2} key={product.id}>
                <div
                  onClick={() => {
                    setSelectedProduct(product);
                    setIsModalOpen(true);
                  }}
                >
                  <ProductItem
                    supplierName={product.supplierName}
                    itemName={product.itemName}
                    shortDescription={product.shortDescription}
                  />
                </div>
              </Grid>
            ))}
          </Grid>

          {selectedProduct && (
            <ProductDetailModal
              open={isModalOpen}
              onClose={() => {
                setIsModalOpen(false);
                setSelectedProduct(null); // Clear the product when modal is closed
              }}
              product={selectedProduct}
            />
          )}
        </Box>
      </Box>
    </Box>
  );
}
