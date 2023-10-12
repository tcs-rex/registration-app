import React, { useContext } from "react";
import { StudentAPIContext } from "../contexts/student-api-provider";
import { Container, Typography } from "@mui/material/";
import DisplayCourses from "./DisplayCourses";

function ViewRegisteredCourses() {
  const { studentCourses, studentInfo } = useContext(StudentAPIContext);

  const registeredCourses = studentCourses
    .filter((sc) => sc.theStatus === "registered")
    .map((sc) => sc.theCourse);
  const completedCourses = studentCourses
    .filter((sc) => sc.theStatus === "completed")
    .map((sc) => sc.theCourse);

  return (
    <Container>
      <Typography
        component="h1"
        variant="h4"
        align="center"
        sx={{ mb: "1rem" }}
      >
        View Registered Courses for {studentInfo.firstName}{" "}
        {studentInfo.lastName}
      </Typography>
      <Typography
        component="h1"
        variant="h5"
        align="center"
        sx={{ mb: "1rem" }}
      >
        Currently Registered Courses
      </Typography>
      <Typography align="center" sx={{ mb: "1rem" }}>
        Currently registered for {registeredCourses.length} courses. Students
        may register for maximum 6 courses.
      </Typography>
      <DisplayCourses courses={registeredCourses} />
      <Typography
        component="h1"
        variant="h5"
        align="center"
        sx={{ mb: "1rem" }}
      >
        Copmleted Courses
      </Typography>
      <Typography align="center" sx={{ mb: "1rem" }}>
        Copmleted {completedCourses.length} courses.
      </Typography>
      <DisplayCourses courses={completedCourses} />
    </Container>
  );
}

export default ViewRegisteredCourses;
