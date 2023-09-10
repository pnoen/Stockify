import axios from 'axios';

export const checkIfSupplier = async () => {
  try {
    const response = await axios.get('http://localhost:8080/user/isSupplier');
    return response.data; // Assuming the response is a boolean
  } catch (error) {
    console.error('Failed to check if user is a supplier:', error);
    return false;
  }
};
