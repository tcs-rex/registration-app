import * as React from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";

export default function NotificationDialog(props) {
  const { notification, open, onClose } = props;

  return (
    <Dialog
      open={open}
      onClose={() => onClose()}
      aria-labelledby="alert-dialog-title"
      aria-describedby="alert-dialog-description"
    >
      <DialogTitle id="alert-dialog-title">
        {notification ? notification.status : "Submitting Request..."}
      </DialogTitle>
      <DialogContent>
        <DialogContentText id="alert-dialog-description">
          {notification ? notification.message : ""}
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button onClick={() => onClose()} disabled={!notification}>
          Close
        </Button>
      </DialogActions>
    </Dialog>
  );
}
