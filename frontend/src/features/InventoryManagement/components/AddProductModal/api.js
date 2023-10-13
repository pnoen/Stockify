import axios from "axios";
import config from "../../../../config.json";
import storage from "../../../../firebase";
import { ref, uploadBytes } from "firebase/storage";

export const getBusinessCode = async () => {
  const email = localStorage.getItem("userToken");
  const url = `${config.userManagementServiceUrl}/account/getBusinessCode?email=${email}`;

  try {
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    console.error("An error occurred while logging in:", error);
    throw error;
  }
};

export const addProduct = async (product) => {
  const url = `${config.productServiceUrl}/api/product/add`;

  try {
    const response = await axios.post(url, {
      name: product.name,
      description: product.description,
      quantity: product.quantity,
      price: product.price,
      businessCode: product.businessCode,
      imageUrl: product.imageUrl,
    });
    return response.status;
  } catch (error) {
    console.error(
      "An error occurred while adding a product to the inventory:",
      error
    );
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
