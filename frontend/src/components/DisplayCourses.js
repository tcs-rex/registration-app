import React from "react";
import { Container, Box, Paper, Typography } from "@mui/material/";

import CourseCard from "./CourseCard";

function DisplayCourses(props) {
  const { courses, loading } = props;
  const { handleRegister, handleDrop } = props;

  return (
    <Container
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
    >
      <Box sx={{ width: 600, display: "flex", flexDirection: "column" }}>
        {loading ? (
          <Paper sx={{ p: "1rem" }} elevation={2} align="center">
            <Typography>Loading...</Typography>
          </Paper>
        ) : courses == null || courses.length === 0 ? (
          <Paper sx={{ p: "1rem" }} elevation={2} align="center">
            <Typography>No courses.</Typography>
          </Paper>
        ) : (
          courses.map((c) => (
            <React.Fragment key={c.uuid}>
              <CourseCard
                course={c}
                handleRegister={handleRegister}
                handleDrop={handleDrop}
              />
            </React.Fragment>
          ))
        )}
      </Box>
    </Container>
  );
}

// Set default props
DisplayCourses.defaultProps = {
  loading: false,
};

export default DisplayCourses;
