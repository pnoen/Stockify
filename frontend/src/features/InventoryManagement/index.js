import React, { useState, useEffect } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Paper,
  Typography,
  Button,
  Box,
  Checkbox,
  ButtonBase,
} from "@mui/material";
import "./styles.css";
import { makeStyles } from "@mui/styles";
import { getBusinessCode, getInventory } from "./api";
import AddProductModal from "./components/AddProductModal";
import RemoveProductModal from "./components/RemoveProductModal";
import EditProductModal from "./components/EditProductModal";
import LoadingSpinner from "../../components/LoadingSpinner";

const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif", // Replace "Your Nice Font" with the desired font
  },
}));

export default function InventoryManagementPage() {
  const classes = useStyles();
  const [products, setProducts] = useState([]);
  const [isAddProductModalOpen, setIsAddProductModalOpen] = useState(false);
  const [isRemoveProductModalOpen, setIsRemoveProductModalOpen] =
    useState(false);
  const [isEditProductModalOpen, setIsEditProductModalOpen] = useState(false);
  const [selectedProductId, setSelectedProductId] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true);
        const businessCode = await getBusinessCode();
        const inventory = await getInventory(businessCode);
        setIsLoading(false);
        setProducts(inventory.product);
      } catch (error) {
        console.error("An error occurred while fetching inventory:", error);
        setIsLoading(false);
      }
    };
    fetchData();
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
            Inventory
          </Typography>
          <Box>
            <Button
              variant="contained"
              style={{ marginRight: "8px", backgroundColor: "#1DB954" }}
              onClick={() => setIsAddProductModalOpen(true)}
            >
              Add Product
            </Button>

            <Button
              variant="contained"
              style={{ marginRight: "8px", backgroundColor: "#ce595f" }}
              onClick={() => setIsRemoveProductModalOpen(true)}
            >
              Remove Product
            </Button>
          </Box>
        </div>

        <Paper>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell className="table-header-cell">Product ID</TableCell>
                <TableCell className="table-header-cell">Product</TableCell>
                <TableCell className="table-header-cell">Quantity</TableCell>
                <TableCell className="table-header-cell">
                  Price per unit ($AUD)
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {products.map((product) => (
                <TableRow
                  hover
                  key={product.id}
                  style={{ cursor: "pointer" }}
                  onClick={() => {
                    setSelectedProductId(product.id);
                    setIsEditProductModalOpen(true);
                  }}
                >
                  <TableCell>{product.id}</TableCell>
                  <TableCell>{product.name}</TableCell>
                  <TableCell>{product.quantity}</TableCell>
                  <TableCell>{product.price}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Paper>
        <LoadingSpinner isLoading={isLoading} />
        <AddProductModal
          open={isAddProductModalOpen}
          onClose={() => setIsAddProductModalOpen(false)}
        />
        <RemoveProductModal
          open={isRemoveProductModalOpen}
          onClose={() => setIsRemoveProductModalOpen(false)}
        />
        <EditProductModal
          open={isEditProductModalOpen}
          onClose={() => {
            setIsEditProductModalOpen(false);
            setSelectedProductId(null);
          }}
          productId={selectedProductId}
        />
      </div>
    </div>
  );
}
