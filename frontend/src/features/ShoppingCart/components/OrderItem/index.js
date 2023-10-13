import React from "react";
import { Box, Typography, Card, CardContent, IconButton } from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import { makeStyles } from "@mui/styles";

const useStyles = makeStyles((theme) => ({
  card: {
    "&.MuiCard-root": {
      marginBottom: 10,
      backgroundColor: "rgba(255, 255, 255, 0.7)",
      border: "1px solid green",
      borderRadius: "4px",
    },
  },
}));
function OrderItem({ product, onRemove }) {
  const classes = useStyles();
  return (
    <Card className={classes.card}>
      <CardContent>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="h6">{product.name}</Typography>
          <IconButton onClick={() => onRemove(product.id)}>
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
