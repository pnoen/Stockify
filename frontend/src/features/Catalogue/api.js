import axios from "axios";
import config from "../../config.json";

export const getProductsForCustomer = async () => {
  const email = localStorage.getItem("userToken");
  try {
    const response = await axios.get(
      `${config.productServiceUrl}/api/product/getProductsCustomer`,
      {
        params: {
          email: email,
        },
      }
    );
    return response.data.products;
  } catch (error) {
    console.error("Error fetching products for customer:", error);
    throw error;
  }
};
