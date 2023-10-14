import axios from "axios";
import config from "../../config.json";

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

export const getInventory = async (businessCode) => {
  const url = `${config.productServiceUrl}/api/product/getInventory?businessCode=${businessCode}`;
  
  try {
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    console.error("An error occurred while fetching inventory:", error);
    throw error;
  }
}