// UserManagement/api.js
import axios from "axios";
import config from "../../config.json"; // Ensure you have your config file set up correctly

export const getUsers = async () => {
  const email = localStorage.getItem("userToken");
  const url = `${config.businessServiceUrl}/api/businessUser/getBusinessUsers?email=${email}`;

  try {
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    console.error("An error occurred while fetching users:", error);
    throw error;
  }
};
