import axios from "axios";
import config from "../../../../config.json";

const createDraftOrder = async () => {
  const email = localStorage.getItem("userToken");
  try {
    const response = await axios.post(
      `${config.orderManagementServiceUrl}/api/order/createDraftOrder?email=${email}`
    );
    return response.data;
  } catch (error) {
    console.error("Error fetching draft order:", error);
    throw error;
  }
};

const createOrderItem = async (draftOrderId, product, quantity) => {
  try {
    const response = await axios.post(
      `${config.orderManagementServiceUrl}/orderItem/create`,
      {
        orderId: draftOrderId,
        productId: product.id,
        price: product.price,
        businessCode: product.businessCode,
        quantity,
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error creating order item:", error);
    throw error;
  }
};

export { createDraftOrder, createOrderItem };
