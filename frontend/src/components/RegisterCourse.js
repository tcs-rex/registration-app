import React, { useContext, useState, useEffect } from "react";
import { StudentAPIContext } from "../contexts/student-api-provider";
import { Container, Button, TextField, Typography, Box } from "@mui/material/";
import DisplayCourses from "./DisplayCourses";
import SectionSelectDialog from "./SectionSelectDialog";
import NotificationDialog from "./NotificationDialog";

function RegisterCourse() {
  const [query, setQuery] = useState();
  const { getAllCourses, searchCourse, registerCourse } =
    useContext(StudentAPIContext);
  const [courseList, setCourseList] = useState(null);
  const [loading, setLoading] = useState(false);

  const [selectionDialogOpen, setSelectionDialogOpen] = React.useState(false);
  const [notification, setNotification] = React.useState(null);
  const [notificationDialogOpen, setNotificationDialogOpen] =
    React.useState(false);

  const [registrationCourse, setRegistrationCourse] = React.useState(null);
  const [registrationCourseNum, setRegistrationCourseNum] =
    React.useState(null);
  const [registrationSection, setRegistrationSection] = React.useState(null);

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

  useEffect(() => {
    if (registrationCourse && registrationSection) {
      async function registerCourseCall() {
        const result = await registerCourse(
          registrationCourse,
          registrationSection
        );
        const status = result.status === "success" ? "success" : "error";
        const message = registrationCourseNum + ": " + result.message;
        setNotification({ status, message });
        setRegistrationCourse(null);
        setRegistrationSection(null);
      }
      registerCourseCall();
    }
  }, [
    registrationCourse,
    registrationSection,
    registrationCourseNum,
    registerCourse,
  ]);

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setCourseList(null);

    const results = await searchCourse(query);
    setCourseList(results);
    setLoading(false);
  }

  async function handleRegister(courseUUID, courseNumber) {
    setRegistrationCourse(courseUUID);
    setRegistrationCourseNum(courseNumber);
    setSelectionDialogOpen(true);
  }

  async function handleSelectionClose(newValue) {
    setSelectionDialogOpen(false);

    if (newValue) {
      setNotificationDialogOpen(true);
      setRegistrationSection(newValue);
    }
  }

  async function handleNotificationClose() {
    setNotificationDialogOpen(false);
    setNotification(null);
  }

  return (
    <Container>
      <Typography component="h1" variant="h4" align="center">
        Register for a Course
      </Typography>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          p: 1,
        }}
      >
        <form onSubmit={handleSubmit}>
          <TextField
            sx={{ ml: 1, mr: 1 }}
            margin="normal"
            label="Search Term"
            value={query || ""}
            onChange={(e) => setQuery(e.target.value)}
          />
          <Button
            type="submit"
            variant="contained"
            disabled={loading}
            sx={{ ml: 1, mr: 1, mt: 3, mb: 2 }}
          >
            Search
          </Button>
        </form>
        <Box sx={{ mt: "1rem" }}>
          <DisplayCourses
            courses={courseList}
            loading={loading}
            handleRegister={handleRegister}
          />
        </Box>
        <SectionSelectDialog
          id="section-selection"
          keepMounted
          open={selectionDialogOpen}
          onClose={handleSelectionClose}
          courseUUID={registrationCourse}
          courseNum={registrationCourseNum}
        />
        <NotificationDialog
          id="notification-dialog"
          keepMounted
          open={notificationDialogOpen}
          onClose={handleNotificationClose}
          notification={notification}
        />
      </Box>
    </Container>
  );
}

export default RegisterCourse;
