import React, { useContext, useState, useEffect } from "react";
import { StudentAPIContext } from "../contexts/student-api-provider";
import { Container, Typography, Box, Button } from "@mui/material/";
import DisplayCourses from "./DisplayCourses";

function ViewAllCourses() {
  const { getAllCourses } = useContext(StudentAPIContext);
  const [courseList, setCourseList] = useState(null);
  const [loading, setLoading] = useState(false);

  // [] option will behave like depreciated componentDidMount and run only once at startup
  useEffect(() => {
    async function fetchCourses() {
      setLoading(true);
      setCourseList(await getAllCourses());
      setLoading(false);
    }

    fetchCourses();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  function sortByName() {
    setCourseList(
      [...courseList].sort((a, b) =>
        a.courseName > b.courseName ? 1 : a.courseName < b.courseName ? -1 : 0
      ),
      console.log("updated")
    );
  }

  function sortByCode() {
    setCourseList(
      [...courseList].sort((a, b) =>
        a.courseNumber > b.courseNumber
          ? 1
          : a.courseNumber < b.courseNumber
          ? -1
          : 0
      ),
      console.log("updated")
    );
  }

  return (
    <Container
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
    >
      <Typography
        component="h1"
        variant="h4"
        align="center"
        sx={{ mb: "1rem" }}
      >
        View All Courses
      </Typography>
      <Box>
        <Button variant="outlined" sx={{ m: "1rem" }} onClick={sortByName}>
          Sort by Name
        </Button>
        <Button variant="outlined" sx={{ m: "1rem" }} onClick={sortByCode}>
          Sort by Code
        </Button>
      </Box>
      <DisplayCourses courses={courseList} loading={loading} />
    </Container>
  );
}

export default ViewAllCourses;
