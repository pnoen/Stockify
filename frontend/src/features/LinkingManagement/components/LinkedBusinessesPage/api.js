import axios from "axios";
import config from "../../../../config.json";

export const getLinkedBusinesses = async () => {
  const email = localStorage.getItem("userToken");
  const url = `${config.businessLinkServiceUrl}/api/businessLink/getBusinesses?email=${email}`;

  try {
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    console.error("An error occurred while fetching linked businesses:", error);
    throw error;
  }
};
