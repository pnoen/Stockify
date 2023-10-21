import axios from "axios";
import config from "../../config.json";
export const getBusinessDetails = async () => {
  const email = localStorage.getItem("userToken");
  const url = `${config.userManagementServiceUrl}/account/getBusinessDetails?email=${email}`;

  try {
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    console.error("An error occurred while logging in:", error);
    throw error;
  }
};

export const getBusinessStats = async () => {
  const email = localStorage.getItem("userToken");
  const url = `${config.orderManagementServiceUrl}/api/order/getBusinessStats?email=${email}`;

  try {
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    console.error("An error occurred while logging in:", error);
    throw error;
  }
};
