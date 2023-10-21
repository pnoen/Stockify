// Dashboard.js

import React, { useState, useEffect } from "react";
import { Box, Grid, Typography } from "@mui/material";
import StatCard from "./components/StatCard";
import { getBusinessDetails, getBusinessStats } from "./api";

const Dashboard = () => {
  const [businessInfo, setBusinessInfo] = useState({
    businessName: "",
    businessCode: "",
  });

  const [stats, setStats] = useState({});

  useEffect(() => {
    const fetchBusinessDetails = async () => {
      try {
        const info = await getBusinessDetails();
        setBusinessInfo(info);
      } catch (error) {
        console.error("Failed to fetch business details:", error);
      }
    };

    const fetchBusinessStats = async () => {
      try {
        const statsData = await getBusinessStats();
        setStats(statsData);
      } catch (error) {
        console.error("Failed to fetch business stats:", error);
      }
    };

    fetchBusinessDetails();
    fetchBusinessStats();
  }, []);

  return (
    <Box sx={{ flexGrow: 1, m: 3, maxWidth: "60%" }}>
      <Typography variant="h2">Dashboard</Typography>
      <Typography variant="h6" mb={3}>
        Welcome {businessInfo.businessName}!
      </Typography>
      <Grid container spacing={3}>
        {Object.entries(stats).map(([title, value], index) => (
          <Grid item xs={4} key={index}>
            <StatCard title={title} value={value} />
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default Dashboard;
