import CircularProgress from "@mui/material/CircularProgress";

export default function LoadingSpinner({ isLoading, props }) {
  if (isLoading) {
    return (
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <CircularProgress color="success" />
      </div>
    );
  }

  if (props) {
    return props;
  }
  return null;
}
