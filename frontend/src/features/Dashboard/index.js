import React, { useState, useEffect } from "react";
import { Box, Grid, Paper, Typography } from "@mui/material";
import StatCard from "./components/StatCard";
import { getBusinessDetails, getBusinessStats } from "./api";
import { makeStyles } from "@mui/styles";

const useStyles = makeStyles((theme) => ({
  boldText: {
    fontWeight: "bold",
    fontFamily: "Your Nice Font, sans-serif", // Replace "Your Nice Font" with the desired font
  },
}));

const Dashboard = () => {
  const classes = useStyles();
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
    <div
      style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        paddingTop: "5vh",
      }}
    >
      <div style={{ width: "90%" }}>
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            marginBottom: "16px",
          }}
        >
          <Typography variant="h3" gutterBottom className={classes.boldText}>
            Dashboard
          </Typography>
        </div>

        <Typography variant="h6" gutterBottom>
          Welcome {businessInfo.businessName}!
        </Typography>

        <Paper style={{ borderRadius: "10px", padding: "16px" }}>
          <Box sx={{ flexGrow: 1 }}>
            <Grid container spacing={3}>
              {Object.entries(stats).map(([title, value], index) => (
                <Grid item xs={4} key={index}>
                  <StatCard title={title} value={value} />
                </Grid>
              ))}
            </Grid>
          </Box>
        </Paper>
      </div>
    </div>
  );
};

export default Dashboard;
