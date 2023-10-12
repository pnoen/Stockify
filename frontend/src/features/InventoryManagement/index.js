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

const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif", // Replace "Your Nice Font" with the desired font
  },
}));

export default function InventoryManagementPage() {
  const classes = useStyles();
  const [products, setProducts] = useState([]);

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
              onClick={console.log("add")}
            >
              Add Product
            </Button>

            <Button
              variant="contained"
              style={{ marginRight: "8px", backgroundColor: "#ce595f" }}
              onClick={console.log("remove")}
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
                <TableCell className="table-header-cell">Price per unit ($AUD)</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {products.map((product) => (
                <TableRow hover key={product.id} style={{ cursor: "pointer" }}>
                  <TableCell>{product.id}</TableCell>
                  <TableCell>{product.name}</TableCell>
                  <TableCell>{product.quantity}</TableCell>
                  <TableCell>{product.price}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Paper>
      </div>
    </div>
  );
}
