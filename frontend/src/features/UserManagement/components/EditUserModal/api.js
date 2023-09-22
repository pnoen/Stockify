import axios from "axios";
import config from "../../../../config.json";

export const getUserDetails = async (userId) => {
  try {
    const response = await axios.get(
      `${config.userManagementServiceUrl}/account/getUserDetails`,
      {
        params: { userId },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error fetching user details", error);
    throw error;
  }
};

export const updateUser = async (userId, updatedUser) => {
  try {
    const response = await axios.put(
      `${config.businessServiceUrl}/api/businessUser/updateUser`,
      {
        id: userId,
        ...updatedUser,
      }
    );
    return response.status;
  } catch (error) {
    console.error("Error updating user", error);
    if (error.response) {
      return error.response.status;
    }
  }
};

export const deleteUser = async (userId) => {
  try {
    const response = await axios.post(
      `${config.businessServiceUrl}/api/businessUser/deleteUser`,
      {
        id: userId,
      }
    );
    return response.status;
  } catch (error) {
    console.error("Error deleting user", error);
    throw error;
  }
};
