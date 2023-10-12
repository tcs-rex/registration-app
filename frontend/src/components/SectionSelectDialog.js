import React, { useContext } from "react";
import PropTypes from "prop-types";
import {
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  RadioGroup,
  Radio,
  Typography,
  FormControlLabel,
} from "@mui/material/";
import { StudentAPIContext } from "../contexts/student-api-provider";

function SectionSelectDialog(props) {
  const { getCourseOfferings } = useContext(StudentAPIContext);
  const {
    onClose,
    value: valueProp,
    open,
    courseUUID,
    courseNum,
    ...other
  } = props;
  const [value, setValue] = React.useState(valueProp);
  const [offeringList, setOfferingList] = React.useState(null);
  const radioGroupRef = React.useRef(null);

  React.useEffect(() => {
    async function fetchOfferings() {
      setOfferingList(await getCourseOfferings(courseUUID));
    }
    if (!open) {
      setValue(valueProp);
    } else {
      fetchOfferings();
    }
  }, [valueProp, open, courseUUID, getCourseOfferings]);

  const handleEntering = () => {
    if (radioGroupRef.current != null) {
      radioGroupRef.current.focus();
    }
  };

  const handleCancel = () => {
    onClose();
    setOfferingList(null);
  };

  const handleRegister = () => {
    onClose(value);
    setOfferingList(null);
  };

  const handleChange = (event) => {
    setValue(event.target.value);
  };

  return (
    <Dialog
      sx={{ "& .MuiDialog-paper": { width: "80%", maxHeight: 435 } }}
      maxWidth="xs"
      TransitionProps={{ onEntering: handleEntering }}
      open={open}
      {...other}
    >
      <DialogTitle>{courseNum} - Choose Section</DialogTitle>
      <DialogContent dividers>
        {!offeringList ? (
          <Typography>Loading...</Typography>
        ) : offeringList.length === 0 ? (
          <Typography>No offerings for this course at this time.</Typography>
        ) : (
          <RadioGroup
            ref={radioGroupRef}
            aria-label="ringtone"
            name="ringtone"
            value={value || null}
            onChange={handleChange}
          >
            {offeringList.map((o) => (
              <FormControlLabel
                value={o.section}
                key={o.uuid}
                control={<Radio />}
                label={`Section ${o.section} - ${o.semester} ${o.year}`}
              />
            ))}
          </RadioGroup>
        )}
      </DialogContent>
      <DialogActions>
        <Button autoFocus onClick={handleCancel}>
          Cancel
        </Button>
        <Button onClick={handleRegister} disabled={!value}>
          Register
        </Button>
      </DialogActions>
    </Dialog>
  );
}

SectionSelectDialog.propTypes = {
  onClose: PropTypes.func.isRequired,
  open: PropTypes.bool.isRequired,
};

export default SectionSelectDialog;
