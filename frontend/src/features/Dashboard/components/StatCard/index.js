import { Card, CardContent, Typography } from "@mui/material";
const StatCard = ({ title, value }) => (
  <Card sx={{ backgroundColor: "#cbf5d6" }}>
    <CardContent>
      <Typography component="div" fontWeight="bold">
        {title}
      </Typography>
      <Typography
        sx={{
          backgroundColor: "rgba(255, 255, 255, 0.6)",
          padding: "0.5em",
          borderRadius: "5px",
          margin: "0.5em 0",
        }}
      >
        {value}
      </Typography>
    </CardContent>
  </Card>
);

export default StatCard;
