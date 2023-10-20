import React, { useState } from "react";
import {
  Modal,
  TextField,
  Button,
  Box,
  Typography,
} from "@mui/material";
import { editProduct, uploadFile } from "./api";
import SuccessSnackBar from "../../../../components/Snackbars/SuccessSnackbar";
import UploadFileIcon from "@mui/icons-material/UploadFile";
import LoadingSpinner from "../../../../components/LoadingSpinner";

export default function EditProductModal({ open, onClose, productId }) {
  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");
  const [product, setProduct] = useState({
    name: "",
    description: "",
    quantity: null,
    price: 0.0,
    imageUrl: "",
  });
  const [file, setFile] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      if (
        Math.floor(product.price) != product.price &&
        product.price.toString().split(".")[1].length > 2
      ) {
        throw new Error("Incorrect price format");
      }

      if (file) {
        setIsLoading(true);
        product.imageUrl = await uploadFile(file);
      }

      const statusCode = await editProduct(product, productId);
      if (statusCode >= 200 && statusCode < 300) {
        setSnackBarMessage("Edited product successfully!");
        setSnackBarOpen(true);
      }
      setIsLoading(false);
      onClose();
    } catch (error) {
      console.error("An error occurred while submitting the form:", error);
      setIsLoading(false);
    }
  };

  const modalStyle = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 400,
    bgcolor: "background.paper",
    boxShadow: 24,
    p: 4,
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    borderRadius: "20px"
  };

  return (
    <>
      <Modal open={open} onClose={onClose}>
        <Box sx={{ ...modalStyle, p: 3 }}>
          <Typography variant="h6" align="center" marginBottom={2}>
            Edit Product Form
          </Typography>
          <form onSubmit={handleSubmit}>
            <TextField
              label="Name"
              value={product.name}
              onChange={(e) => setProduct({ ...product, name: e.target.value })}
              margin="normal"
              fullWidth
            />
            <TextField
              label="Description"
              value={product.description}
              onChange={(e) =>
                setProduct({ ...product, description: e.target.value })
              }
              margin="normal"
              fullWidth
            />
            <TextField
              label="Quantity"
              type="number"
              value={product.quantity}
              onChange={(e) =>
                setProduct({ ...product, quantity: e.target.value })
              }
              margin="normal"
              inputProps={{ min: 0 }}
              fullWidth
            />
            <TextField
              label="Price per unit"
              type="number"
              value={product.price}
              inputProps={{ step: 0.01 }}
              onChange={(e) =>
                setProduct({ ...product, price: e.target.value })
              }
              margin="normal"
              fullWidth
            />
            <Box sx={{ display: "flex", alignItems: "center" }}>
              <Button
                component="label"
                variant="outlined"
                color="primary"
                startIcon={<UploadFileIcon />}
              >
                Upload image
                <input
                  type="file"
                  accept="image/jpeg"
                  onChange={(e) =>
                    e.target.files.length > 0
                      ? setFile(e.target.files[0])
                      : setFile(null)
                  }
                  hidden
                />
              </Button>
              <Box sx={{ marginLeft: 1 }}>{file ? file.name : ""}</Box>
            </Box>

            <Box
              sx={{ display: "flex", justifyContent: "center", marginTop: 2 }}
            >
              <LoadingSpinner
                isLoading={isLoading}
                props={
                  <Button type="submit" variant="contained" color="primary">
                    Update
                  </Button>
                }
              />
            </Box>
          </form>
        </Box>
      </Modal>
      <SuccessSnackBar
        open={snackBarOpen}
        message={snackBarMessage}
        onClose={() => setSnackBarOpen(false)}
      />
    </>
  );
}
