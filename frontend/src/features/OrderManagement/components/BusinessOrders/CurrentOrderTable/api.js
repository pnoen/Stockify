import axios from "axios";
import config from "../../../../../config.json";
export const fetchOpenOrders = async () => {
  try {
    const email = localStorage.getItem("userToken");

    const response = await axios.get(
      `${config.orderManagementServiceUrl}/api/order/getAllCurrentBusinessOrders?email=${email}`
    );

    return response.data;
  } catch (error) {
    console.error("Error fetching open orders:", error);
  }
};
