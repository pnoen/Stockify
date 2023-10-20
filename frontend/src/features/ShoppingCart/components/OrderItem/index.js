import React from "react";
import { Box, Typography, Card, CardContent, IconButton } from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import { makeStyles } from "@mui/styles";
import { deleteOrderItem } from "./api";

const useStyles = makeStyles((theme) => ({
  card: {
    "&.MuiCard-root": {
      margin: "10px 0px",
      backgroundColor: "fafafafa6a",
      borderRadius: "4px",
    },
  },
}));
function OrderItem({ product, onRemove }) {
  const classes = useStyles();
  const handleDelete = async (id) => {
    try {
      await deleteOrderItem(id);
      onRemove(product.id);
    } catch (error) {
      console.error("Error deleting product:", error);
    }
  };
  return (
    <Card className={classes.card}>
      <CardContent>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="h6">{product.name}</Typography>
          <IconButton onClick={() => handleDelete(product.orderItemId)}>
            <DeleteIcon />
          </IconButton>
        </Box>
        <Typography variant="body1">{product.supplier}</Typography>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="body2">{product.quantity}</Typography>
          <Typography variant="body2">${product.price}</Typography>
        </Box>
      </CardContent>
    </Card>
  );
}

export default OrderItem;
