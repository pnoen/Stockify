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

export const fetchOrderItemsByOrderId = async (orderId) => {
  try {
    const response = await axios.get(
      `${config.orderManagementServiceUrl}/orderItem/getAllByOrderId?orderId=${orderId}`
    );
    return response.data;
  } catch (error) {
    console.error("Error fetching order items by order ID:", error);
    throw error;
  }
};

export const fetchCustomerDetails = async (customerId) => {
  try {
    const response = await axios.get(
      `${config.userManagementServiceUrl}/account/getUserDetails?userId=${customerId}`
    );
    return response.data;
  } catch (error) {
    console.error("Error fetching order items by order ID:", error);
    throw error;
  }
};
