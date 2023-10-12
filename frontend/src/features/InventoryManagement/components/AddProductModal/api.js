import axios from "axios";
import config from "../../../../config.json";

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
    });
    return response.status;
  } catch (error) {
    console.error("An error occurred while adding a product to the inventory:", error);
    throw error;
  }
};
