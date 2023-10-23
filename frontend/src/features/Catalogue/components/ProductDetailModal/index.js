import React, { useState, useEffect } from "react";
import {
  Modal,
  Box,
  Typography,
  Button,
  TextField,
  Divider,
  Paper,
} from "@mui/material";
import {
  createDraftOrder,
  createOrderItem,
  getBusinessName,
  getImageUrl,
} from "./api";
import SuccessSnackBar from "../../../../components/Snackbars/SuccessSnackbar";
import LoadingSpinner from "../../../../components/LoadingSpinner";

export default function ProductDetailModal({
  open,
  onClose,
  product,
  onAddToCartSuccess,
  onAddToCartFailure,
}) {
  const [quantity, setQuantity] = useState(0);
  const [imageUrl, setImageUrl] = useState(null);
  const [businessName, setBusinessName] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [isImageLoading, setIsImageLoading] = useState(false);

  useEffect(() => {
    const fetchImage = async () => {
      try {
        setIsImageLoading(true);
        const url = await getImageUrl(product.imageURL);
        setImageUrl(url);
        setIsImageLoading(false);
      } catch (error) {
        console.error("Error fetching image:", error);
        setIsImageLoading(false);
      }
    };

    if (product.imageURL) {
      fetchImage();
    }
  }, [product.imageURL]);

  useEffect(() => {
    const fetchBusinessName = async () => {
      try {
        const business = await getBusinessName(product.businessCode);
        setBusinessName(business);
      } catch (error) {
        console.error("Error fetching business name:", error);
      }
    };
    fetchBusinessName();
  }, []);

  const handleQuantityChange = (e) => {
    const value = e.target.value;
    if (value >= 0 && value <= product.quantity) {
      setQuantity(value);
    }
  };

  const handleAddToCart = async (product, quantity) => {
    try {
      setIsLoading(true);
      const draftOrderData = await createDraftOrder();
      const draftOrderId = draftOrderData.message;

      const orderItemData = await createOrderItem(
        parseInt(draftOrderId),
        product,
        quantity
      );

      if (orderItemData.statusCode === 200) {
        console.log(orderItemData);
        onAddToCartSuccess(orderItemData.message);
        setIsLoading(false);
        onClose();
      } else {
        console.error("Error placing the order.");
        setIsLoading(false);
        onAddToCartFailure(orderItemData.message);
      }
    } catch (error) {
      console.error("Error adding to cart:", error);
      setIsLoading(false);
    }
  };
  return (
    <Modal open={open} onClose={onClose} aria-labelledby="product-detail-modal">
      <Box
        sx={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          bgcolor: "#cbf5d6",
          boxShadow: 24,
          p: 4,
          width: "80%",
          maxWidth: "600px",
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          height: "auto",
          borderRadius: "20px",
        }}
      >
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            position: "relative",
          }}
        >
          <div
            style={{
              width: "100%",
              height: "35vh",
              backgroundColor: "rgba(255, 255, 255, 0.6)",
              marginBottom: "3px",
              borderRadius: "10px",
              display: "flex",
              justifyContent: "center",
            }}
          >
            {imageUrl ? (
              <img
                src={imageUrl}
                alt={product.name}
                style={{
                  width: "100%",
                  height: "35vh",
                  objectFit: "contain",
                  marginBottom: "3px",
                  borderRadius: "10px",
                }}
              />
            ) : (
              <LoadingSpinner isLoading={isImageLoading} />
            )}
          </div>
        </Box>

        <Typography
          variant="body2"
          sx={{
            alignSelf: "flex-start",
            backgroundColor: "rgba(255, 255, 255, 0.6)",
            padding: "0.5em",
            borderRadius: "5px",
            margin: "0.5em 0",
          }}
        >
          {businessName}
        </Typography>
        {/* Centered Name and Description */}
        <Paper sx={{ margin: "0em", padding: "1em", borderRadius: "5px" }}>
          <Typography variant="h4" gutterBottom align="center">
            {product.name}
          </Typography>
          <Typography align="center">{product.description}</Typography>
          <Divider sx={{ margin: "1em 0" }} />
          <Typography variant="h6">Price: ${product.price}</Typography>
          <Typography variant="h6">
            Quantity Available: {product.quantity}
          </Typography>

          <Box display="flex" alignItems="center">
            <Typography variant="h6" sx={{ paddingRight: "1vh" }}>
              Order Quantity:
            </Typography>
            <TextField
              type="number"
              value={quantity}
              onChange={handleQuantityChange}
              InputProps={{
                inputProps: {
                  min: 0,
                  max: product.quantity,
                },
              }}
              sx={{
                marginRight: "10px",
                width: "12%",
                "& .MuiInputBase-input": {
                  height: "0em",
                },
              }}
            />
          </Box>
        </Paper>

        <Box
          mt={3}
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <Button
            variant="outlined"
            onClick={onClose}
            color="error"
            sx={{ backgroundColor: "rgba(255, 255, 255, 0.6)" }}
          >
            Back
          </Button>

          <LoadingSpinner
            isLoading={isLoading}
            props={
              <Button
                variant="contained"
                style={{ backgroundColor: "#1DB954", color: "white" }}
                onClick={() => handleAddToCart(product, quantity)}
              >
                Add to Cart
              </Button>
            }
          />
        </Box>
      </Box>
    </Modal>
  );
}
