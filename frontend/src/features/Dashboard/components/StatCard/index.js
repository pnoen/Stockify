import { Card, CardContent, Typography } from "@mui/material";
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

export default StatCard;
