import axios from "axios";
import config from "../../config.json"; // Adjust the path to your actual config.json location

export const loginUser = async (email, password) => {
  const url = `${config.userManagementServiceUrl}/account/login`; // Make sure your config.json key matches
  const data = {
    email,
    password,
  };

  try {
    const response = await axios.post(url, data);
    return response.data;
  } catch (error) {
    console.error("An error occurred while logging in:", error);
    throw error;
  }
};
