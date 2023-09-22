import axios from "axios";
import config from "../../../../config.json";

export const getRoles = async () => {
  try {
    const url = `${config.businessServiceUrl}/api/roles/getRoles`;
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    console.error("An error occurred while fetching roles:", error);
    throw error;
  }
};

export const addUser = async (user) => {
  const url = `${config.businessServiceUrl}/api/businessUser/addUser`;

  try {
    const response = await axios.post(url, {
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      role: user.role,
      businessCode: user.businessCode,
    });
    return response.status;
  } catch (error) {
    console.error("An error occurred while adding the user:", error);
    throw error;
  }
};

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
