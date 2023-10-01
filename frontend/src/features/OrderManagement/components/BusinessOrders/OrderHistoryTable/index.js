import React, { useEffect, useState } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Paper,
  Typography,
} from "@mui/material";
import { fetchOpenOrders, fetchOrderHistory } from "./api";
import "./styles.css";

export default function OrderHistoryTable() {
  const [orderHistory, setOrderHistory] = useState([]);

  useEffect(() => {
    const getOrders = async () => {
      try {
        setOrderHistory([
          { id: 1, date: "2023-01-01", totalCost: 100, status: "Pending" },
          { id: 2, date: "2023-01-02", totalCost: 200, status: "Processing" },
        ]);
      } catch (error) {
        console.error("Error fetching orders:", error);
      }
    };

    getOrders();
  }, []);

  return (
    
      <div style={{ maxWidth: "90%", width: "100%" }}>
        <Typography variant="h6" gutterBottom>
          Order History
        </Typography>
        <Paper>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell className="table-header-cell">Order ID</TableCell>
                <TableCell className="table-header-cell">Order Date</TableCell>
                <TableCell className="table-header-cell">Total Cost</TableCell>
                <TableCell className="table-header-cell">Status</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {orderHistory.map((order) => (
                <TableRow key={order.id}>
                  <TableCell>{order.id}</TableCell>
                  <TableCell>{order.date}</TableCell>
                  <TableCell>{order.totalCost}</TableCell>
                  <TableCell>{order.status}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Paper>
      </div>
  
  );
}
