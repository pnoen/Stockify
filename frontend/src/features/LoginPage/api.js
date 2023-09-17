import axios from "axios";
import config from "../../config.json"; 

export const loginUser = async (email, password) => {
    const url = `${config.userManagementServiceUrl}/account/login`; 
    const data = {
      email,
      password,
    };
  
    try {
      const response = await axios.post(url, data);
      const statusCode = response.data.statusCode;
      const message = response.data.message;
      const userToken = response.data.userToken;
  
      if (statusCode === 200) {
        localStorage.setItem("userToken",userToken);
        alert(message);
      } else if (statusCode === 404 || statusCode === 401) {
 
        alert(message);
      }
  
      return statusCode; 
    } catch (error) {
      console.error("An error occurred while logging in:", error);
      throw error;
    }
  };
  