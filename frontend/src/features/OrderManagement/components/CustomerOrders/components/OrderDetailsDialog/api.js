import axios from "axios";
import config from "../../../../../../config.json";

export const fetchOrderDetailsById = async (orderId) => {
  try {
    const response = await axios.get(
      `${config.orderManagementServiceUrl}/api/order/getOrderById?orderId=${orderId}`
    );

    return response.data;
  } catch (error) {
    console.error("Error fetching open orders:", error);
  }
};
