import axios from "axios";
import config from "../../../../config.json";
import storage from "../../../../firebase";
import { ref, uploadBytes } from "firebase/storage";

export const editProduct = async (product, productId) => {
  const url = `${config.productServiceUrl}/api/product/edit`;

  try {
    const response = await axios.post(url, {
      id: productId,
      ...product,
    });
    
    return response.status;
  } catch (error) {
    console.error("An error occurred while editing a product:", error);
    throw error;
  }
};

export const uploadFile = async (file) => {
  const filename = Math.random().toString(36).slice(2) + ".jpg";
  const storageRef = ref(storage, filename);
  try {
    await uploadBytes(storageRef, file);
    return filename;
  } catch (error) {
    console.error("An error occurred while uploading file:", error);
    throw error;
  }
};
