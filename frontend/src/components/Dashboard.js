import React, { useContext } from "react";
import { StudentAPIContext } from "../contexts/student-api-provider";
import { Container, Typography, Paper, Button } from "@mui/material/";

//TODO: Add links to the register and drop courses after adding routes

export default function Dashboard() {
  // get the information for the logged in student from the StudentAPIContext
  const { studentInfo } = useContext(StudentAPIContext);
  return (
    <Container>
      <Typography
        component="h1"
        variant="h4"
        align="center"
        sx={{
          p: "1rem",
        }}
      >
        {studentInfo == null
          ? "Loading Student Info..."
          : "Please Select an Option from the Menu below"}
      </Typography>

      <Paper
        sx={{
          padding: "1rem 2rem",
          margin: " 1.5rem auto",
          maxWidth: "sm",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
        elevation={2}
      >
        <Button
          variant="contained"
          href="/viewallcourses"
          disabled={!studentInfo}
          fullWidth
          sx={{ margin: 1 }}
        >
          View All Courses
        </Button>
        <Button
          variant="contained"
          href="/viewregisteredcourses"
          disabled={!studentInfo}
          fullWidth
          sx={{ margin: 1 }}
        >
          View Registered Courses
        </Button>
        <Button
          variant="contained"
          href="/searchcourses"
          disabled={!studentInfo}
          fullWidth
          sx={{ margin: 1 }}
        >
          Search Courses
        </Button>
        <Button
          variant="contained"
          href="/registercourse"
          disabled={!studentInfo}
          fullWidth
          sx={{ margin: 1 }}
        >
          Register for Course
        </Button>
        <Button
          variant="contained"
          href="/dropcourse"
          disabled={!studentInfo}
          fullWidth
          sx={{ margin: 1 }}
        >
          Drop Course
        </Button>
      </Paper>
    </Container>
  );
}
