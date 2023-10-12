import React from "react";
import { Typography, Box } from "@mui/material";

export default function ProductItem({
  supplierName,
  itemName,
  shortDescription,
}) {
  return (
    <Box
      sx={{
        backgroundColor: "rgba(255, 255, 255, 0.3)", 
        border: "1px solid green",
        borderRadius: "4px",
        padding: "1rem",
      }}
    >
      {/* Mock placeholder for the product image */}
      <div
        style={{
          width: "100%",
          height: "150px",
          backgroundColor: "#e0e0e0",
          marginBottom: "15px",
        }}
      ></div>
      <Typography>{supplierName}</Typography>
      <Typography>{itemName}</Typography>
      <Typography>{shortDescription}</Typography>
    </Box>
  );
}
