import axios from "axios";
import config from "../../config.json"; // Adjust the path to your actual config.json location

export const registerUser = async (formData) => {
  const url = `${config.userManagementServiceUrl}/account/register`; // Make sure your config.json key matches
  const data = {
    firstName: formData.firstName,
    lastName: formData.lastName,
    email: formData.email,
    password: formData.password,
    confirmPassword: formData.confirmPassword,
    business: formData.isBusiness ? formData.businessName : null,
  };

  try {
    const response = await axios.post(url, data);
    const statusCode = response.data.statusCode;
    const message = response.data.message;

    alert(message);

    return statusCode;
  } catch (error) {
    console.error("An error occurred while registering:", error);
  }
};
