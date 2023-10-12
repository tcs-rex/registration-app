import React, { useContext } from "react";
import { StudentAPIContext } from "../contexts/student-api-provider";
import { Container, Typography } from "@mui/material/";
import DisplayCourses from "./DisplayCourses";
import NotificationDialog from "./NotificationDialog";

function DropCourse() {
  const { studentCourses, dropCourse } = useContext(StudentAPIContext);
  const courseList = studentCourses
    .filter((sc) => sc.theStatus === "registered")
    .map((sc) => sc.theCourse);

  const [notification, setNotification] = React.useState(null);
  const [notificationDialogOpen, setNotificationDialogOpen] =
    React.useState(false);

  async function handleDrop(courseUUID, courseNumber) {
    setNotificationDialogOpen(true);
    const section = studentCourses.filter(
      (sc) => sc.theCourse.uuid === courseUUID
    )[0].theOffering.section;

    const result = await dropCourse(courseUUID, section);

    const status = result.status === "success" ? "success" : "error";
    const message = courseNumber + ": " + result.message;
    setNotification({ status, message });
  }

  async function handleNotificationClose() {
    setNotificationDialogOpen(false);
    setNotification(null);
  }

  return (
    <Container>
      <Typography
        component="h1"
        variant="h4"
        align="center"
        sx={{ mb: "1rem" }}
      >
        Drop a Course
      </Typography>
      <Typography align="center" sx={{ mb: "1rem" }}>
        Courses eligible to be dropped are listed below.
      </Typography>
      <DisplayCourses courses={courseList} handleDrop={handleDrop} />
      <NotificationDialog
        id="notification-dialog"
        keepMounted
        open={notificationDialogOpen}
        onClose={handleNotificationClose}
        notification={notification}
      />
    </Container>
  );
}

export default DropCourse;
