import React from "react";
import { Box, Card, CardContent, Grid, Typography } from "@mui/material";

const StatCard = ({ title, value }) => (
  <Card sx={{ minWidth: 275 }}>
    <CardContent>
      <Typography variant="h5" component="div">
        {title}
      </Typography>
      <Typography variant="body2">{value}</Typography>
    </CardContent>
  </Card>
);

const Dashboard = () => {
  // Mock data
  const businessInfo = {
    name: "Your Business Name",
    code: "YBN123",
  };

  const totalOrders = 200;
  const openOrders = 50;
  const totalRevenue = 10000;

  return (
    <Box sx={{ flexGrow: 1, m: 3, maxWidth: "60%" }}>
      <Typography variant="h4" mb={3}>
        {businessInfo.name} ({businessInfo.code})
      </Typography>
      <Grid container spacing={3}>
        <Grid item xs={4}>
          <StatCard title="Total Orders" value={totalOrders} />
        </Grid>
        <Grid item xs={4}>
          <StatCard title="Open Orders" value={openOrders} />
        </Grid>
        <Grid item xs={4}>
          <StatCard title="Total Revenue" value={`$${totalRevenue}`} />
        </Grid>
        {/* ...other stat cards */}
      </Grid>
    </Box>
  );
};

export default Dashboard;
