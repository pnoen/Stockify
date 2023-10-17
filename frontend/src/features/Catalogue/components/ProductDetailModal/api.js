import axios from "axios";
import config from "../../../../config.json";
import { getDownloadURL, ref } from "firebase/storage";
import storage from "../../../../firebase";

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

const getImageUrl = async (imageName) => {
  const imageRef = ref(storage, imageName);
  return getDownloadURL(imageRef);
};


export { createDraftOrder, createOrderItem, getImageUrl };
