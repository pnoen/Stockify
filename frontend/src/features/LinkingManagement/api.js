import axios from "axios";
import config from "../../config.json";

export const checkIfBusiness = async () => {
    const email = localStorage.getItem("userToken");
    const url = `${config.userManagementServiceUrl}/account/checkIfBusiness?email=${email}`;
    const data = {
      email,
    };
  
    try {
      const response = await axios.get(url, data);
      return response.data.statusCode;
    } catch (error) {
      console.error("An error occurred while logging in:", error);
      throw error;
    }
  };