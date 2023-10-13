import axios from "axios";
import config from "../../../../../../config.json";

export const getBusinessCode = async () => {
  const email = localStorage.getItem("userToken");
  const url = `${config.userManagementServiceUrl}/account/getBusinessCode?email=${email}`;

  try {
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    console.error("An error occurred while logging in:", error);
    throw error;
  }
};

export const createClientLink = async (link) => {
  const url = `${config.businessLinkServiceUrl}/api/businessLink/createUserLink`;

  try {
    const response = await axios.post(url, {
      businessCode: link.businessCode,
      firstName: link.firstName,
      lastName: link.lastName,
      email: link.email,
    });
    return response.status;
  } catch (error) {
    console.error("An error occurred while creating a link:", error);
    throw error;
  }
};
