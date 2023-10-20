import axios from "axios";
import config from "../../config.json";

const getDraftOrder = async () => {
  const email = localStorage.getItem("userToken");
  try {
    const response = await axios.get(
      `${config.orderManagementServiceUrl}/api/order/getDraftOrder?email=${email}`
    );
    return response.data;
  } catch (error) {
    console.error("Error fetching draft order:", error);
    throw error;
  }
};

const getOrderItems = async (draftOrderId) => {
  try {
    const response = await axios.get(
      `${config.orderManagementServiceUrl}/orderItem/getAllByOrderId?orderId=${draftOrderId}`
    );
    return response.data;
  } catch (error) {
    console.error("Error creating order item:", error);
    throw error;
  }
};

const updateDraftOrder = async (orderId, totalCost) => {
  try {
    const response = await axios.post(
      `${config.orderManagementServiceUrl}/api/order/updateDraftOrder`,
      {
        orderId,
        totalCost,
      }
    );
    return response.data;
  } catch (error) {
    console.error("Failed to delete order item:", error);
    throw error;
  }
};

export { getDraftOrder, getOrderItems, updateDraftOrder };
