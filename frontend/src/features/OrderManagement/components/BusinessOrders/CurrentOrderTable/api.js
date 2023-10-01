import axios from "axios";

export const fetchOpenOrders = async () => {
  // Simulating an API call with Axios
  const response = await axios.get("/api/open-orders"); // replace with your actual API endpoint
  return response.data || [
    { id: 1, date: "2023-01-01", totalCost: 100, status: "Pending" },
    { id: 2, date: "2023-01-02", totalCost: 200, status: "Processing" },
  ];
};

export const fetchOrderHistory = async () => {
  // Simulating an API call with Axios
  const response = await axios.get("/api/order-history"); // replace with your actual API endpoint
  return response.data || [
    { id: 3, date: "2022-12-01", totalCost: 50, status: "Completed" },
    { id: 4, date: "2022-12-02", totalCost: 75, status: "Completed" },
  ];
};
