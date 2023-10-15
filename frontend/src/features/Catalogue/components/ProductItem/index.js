import React, { useState, useEffect } from "react";
import { Typography, Box } from "@mui/material";
import { getImageUrl } from "./api";

export default function ProductItem({ product }) {
  const [imageUrl, setImageUrl] = useState(null);

  useEffect(() => {
    const fetchImage = async () => {
      try {
        const url = await getImageUrl(product.imageURL);
        setImageUrl(url);
      } catch (error) {
        console.error("Error fetching image:", error);
      }
    };

    if (product.imageURL) {
      fetchImage();
    }
  }, [product.imageURL]);
  return (
    <Box
      sx={{
        backgroundColor: "rgba(29, 185, 84, 0.15)",
        borderRadius: "10px",
        padding: "1rem",
        cursor: "pointer",
        boxShadow: "0 2px 6px",
        transition: "transform 0.2s ease",
        "&:hover": {
          transform: "scale(1.05)",
        },
      }}
    >
      {imageUrl ? (
        <img
          src={imageUrl}
          alt={product.name}
          style={{
            width: "100%",
            height: "150px",
            objectFit: "cover",
            marginBottom: "3px",
            borderRadius: "10px",
          }}
        />
      ) : (
        <div
          style={{
            width: "100%",
            height: "150px",
            backgroundColor: "#e0e0e0",
            marginBottom: "10px",
          }}
        ></div>
      )}

      <Typography>{product.businessName}</Typography>
      <Typography>{product.name}</Typography>
      <Typography>{product.description}</Typography>
    </Box>
  );
}
