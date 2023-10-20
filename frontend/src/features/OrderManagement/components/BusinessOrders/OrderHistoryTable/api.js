import axios from "axios";
import config from "../../../../../config.json";
export const fetchOrderHistory = async () => {
  try {
    const email = localStorage.getItem("userToken");

    const response = await axios.get(
      `${config.orderManagementServiceUrl}/api/order/getAllCompletedBusinessOrders?email=${email}`
    );

    return response.data;
  } catch (error) {
    console.error("Error fetching open orders:", error);
  }
};
