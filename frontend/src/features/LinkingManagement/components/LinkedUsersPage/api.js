import axios from "axios";
import config from "../../../../config.json";

export const getLinkedUsers = async () => {
  const email = localStorage.getItem("userToken");
  const url = `${config.businessLinkServiceUrl}/api/businessLink/getUsers?email=${email}`;

  try {
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    console.error("An error occurred while fetching linked users:", error);
    throw error;
  }
};
