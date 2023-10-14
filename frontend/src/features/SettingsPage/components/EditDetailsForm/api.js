import axios from "axios";
import config from "../../../../config.json";

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

export const getUserId = async () => {
  const email = localStorage.getItem("userToken");
  const url = `${config.userManagementServiceUrl}/account/getUserIdByEmail?email=${email}`;
  const data = {
    email,
  };

  try {
    const response = await axios.get(url, data);
    return response.data;
  } catch (error) {
    console.error("An error occurred while logging in:", error);
    throw error;
  }
};

export const editUser = async (user) => {
  const url = `${config.userManagementServiceUrl}/account/editUser`;

  try {
    const response = await axios.post(url, {
      id: user.id,
      firstName: user.firstName,
      lastName: user.lastName,
      currentPassword: user.currentPassword,
      newPassword: user.newPassword,
      confirmPassword: user.confirmPassword,
      business: user.business,
    });
    return response.status;
  } catch (error) {
    console.error("An error occurred while creating a link:", error);
    throw error;
  }
};
