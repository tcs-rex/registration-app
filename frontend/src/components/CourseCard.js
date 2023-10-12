import React, { useContext, useState, useEffect } from "react";
import {
  Box,
  Card,
  CardActions,
  CardContent,
  Collapse,
  IconButton,
  Button,
  Typography,
  List,
  ListItem,
  Divider,
} from "@mui/material";
import { styled } from "@mui/material/styles";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import CheckCircle from "@mui/icons-material/CheckCircle";
import Error from "@mui/icons-material/Error";
import { StudentAPIContext } from "../contexts/student-api-provider";

export default function CourseCard(props) {
  const { getCoursePrerequisites, getCourseOfferings, studentCourses } =
    useContext(StudentAPIContext);
  const { handleRegister, handleDrop } = props;
  const { uuid, courseNumber, courseName, courseDept } = props.course;
  const [preReqs, setPreReqs] = React.useState(null);
  const [offeringList, setOfferingList] = React.useState(null);
  const [expanded, setExpanded] = React.useState(false);
  const [loading, setLoading] = useState(false);

  // Check the status of a specific offeringUUID
  const offeringStatus = (uuid) => {
    // search studentCourses to see if uuid is included
    const result = studentCourses.find((obj) => obj.theOffering.uuid === uuid);
    //return the status of the uuid if the student was enrolled
    return result === undefined ? null : result.theStatus;
  };

  // Check the status of a specific courseUUID
  const enrollmentStatus = (uuid) => {
    const result = studentCourses.find((obj) => obj.theCourse.uuid === uuid);
    return result === undefined ? null : result.theStatus;
  };
  const isEnrolled = enrollmentStatus(uuid) != null;

  useEffect(() => {
    // load data on expansion if data has not yet loaded
    if (expanded === true && (preReqs == null || offeringList == null)) {
      async function fetchCourseLists() {
        setLoading(true);
        setPreReqs(await getCoursePrerequisites(uuid));
        setOfferingList(await getCourseOfferings(uuid));
        setLoading(false);
      }

      fetchCourseLists();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [expanded]);

  const handleExpandClick = () => {
    setExpanded(!expanded);
  };

  const ExpandMore = styled((props) => {
    const { expand, ...other } = props;
    return <IconButton {...other} />;
  })(({ theme, expand }) => ({
    transform: !expand ? "rotate(0deg)" : "rotate(180deg)",
    marginLeft: "auto",
    transition: theme.transitions.create("transform", {
      duration: theme.transitions.duration.shortest,
    }),
  }));

  return (
    <Card sx={{ mb: "1rem" }} elevation={2}>
      <CardContent>
        <Typography color="text.secondary">{courseDept}</Typography>
        <Typography variant="h5" component="div">
          {courseNumber} - {courseName}
        </Typography>
        {isEnrolled && (
          <Typography color="text.secondary">
            Status: {enrollmentStatus(uuid)}
          </Typography>
        )}
      </CardContent>
      <CardActions>
        <Box sx={{ marginRight: "auto" }}>
          {handleRegister && (
            <Button
              onClick={() => handleRegister(uuid, courseNumber)}
              disabled={isEnrolled}
            >
              Register
            </Button>
          )}
          {handleDrop && (
            <Button
              onClick={() => handleDrop(uuid, courseNumber)}
              disabled={!(enrollmentStatus(uuid) === "registered")}
            >
              Drop
            </Button>
          )}
        </Box>
        <ExpandMore
          expand={expanded}
          onClick={handleExpandClick}
          aria-expanded={expanded}
          aria-label="show more"
        >
          <ExpandMoreIcon />
        </ExpandMore>
      </CardActions>

      <Collapse in={expanded} timeout="auto" unmountOnExit>
        <CardContent>
          <Divider sx={{ mb: "0.5rem" }} />
          {loading ? (
            <Typography>Loading...</Typography>
          ) : (
            <>
              <Typography color="text.secondary">Offerings:</Typography>
              <List>
                {offeringList == null || offeringList.length === 0 ? (
                  <Typography color="text.secondary">None</Typography>
                ) : (
                  offeringList.map((o) => {
                    const os = offeringStatus(o.uuid);
                    const goodStatus = ["completed", "registered"];
                    const badStatus = ["failed"];
                    return (
                      <ListItem key={o.uuid}>
                        <Typography color="text.secondary">
                          Section {o.section} - {o.semester} {o.year} (
                          {o.currentEnrollment} students currently enrolled)
                        </Typography>
                        {goodStatus.includes(os) && (
                          <CheckCircle color="success" />
                        )}
                        {badStatus.includes(os) && <Error color="error" />}
                        <Typography
                          color={
                            goodStatus.includes(os)
                              ? "success.main"
                              : "error.main"
                          }
                        >
                          {os}
                        </Typography>
                      </ListItem>
                    );
                  })
                )}
              </List>
              <Typography color="text.secondary">Prerequisites:</Typography>
              <List>
                {preReqs == null || preReqs.length === 0 ? (
                  <Typography color="text.secondary">None</Typography>
                ) : (
                  preReqs.map((p) => {
                    const ps = enrollmentStatus(p.uuid);
                    const goodStatus = ["completed"];
                    return (
                      <ListItem key={p.uuid}>
                        <Typography color="text.secondary">
                          {p.courseNumber} - {p.courseName}
                        </Typography>
                        {goodStatus.includes(ps) ? (
                          <CheckCircle color="success" />
                        ) : (
                          <Error color="error" />
                        )}
                        <Typography
                          color={
                            goodStatus.includes(ps)
                              ? "success.main"
                              : "error.main"
                          }
                        >
                          {ps}
                        </Typography>
                      </ListItem>
                    );
                  })
                )}
              </List>{" "}
            </>
          )}
        </CardContent>
      </Collapse>
    </Card>
  );
}
