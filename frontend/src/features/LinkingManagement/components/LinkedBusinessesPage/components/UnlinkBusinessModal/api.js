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

export const removeBusinessLink = async (link) => {
  const email = localStorage.getItem("userToken");
  const url = `${config.businessLinkServiceUrl}/api/businessLink/removeBusinessLink`;

  try {
    const response = await axios.delete(url, {
      data: {
        businessCode: link.businessCode,
        email: email,
      },
    });
    return response.status;
  } catch (error) {
    console.error("An error occurred while removing a link:", error);
    throw error;
  }
};
