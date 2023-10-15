import config from "../../../../config.json";
import axios from "axios";

export const deleteOrderItem = async (orderItemId) => {
  try {
    const response = await axios.delete(
      `${config.orderManagementServiceUrl}/orderItem/delete`,
      {
        data: { orderItemId },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Failed to delete order item:", error);
    throw error;
  }
};
