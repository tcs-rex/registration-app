import React, { useState, useContext } from "react";
import { StudentAPIContext } from "../contexts/student-api-provider";
import {
  Alert,
  AlertTitle,
  Button,
  Link,
  TextField,
  Box,
  Typography,
  Container,
} from "@mui/material";

export default function Signup() {
  // import the signup function from the StudentAPIContext
  const { register } = useContext(StudentAPIContext);
  // keep track of field states
  const [firstName, setFirstName] = useState();
  const [lastName, setLastName] = useState();
  const [email, setEmail] = useState();
  const [password, setPassword] = useState();
  // use a state to keep track of whether or not there is an error with signup
  const [signupStatus, setSignupStatus] = useState(null);

  async function handleSubmit(e) {
    e.preventDefault();
    const result = await register(email, password, firstName, lastName);
    // set signup status
    setSignupStatus(result);
  }

  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          marginBottom: 8,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Typography component="h1" variant="h5">
          Sign Up
        </Typography>
        <form onSubmit={handleSubmit}>
          <TextField
            margin="normal"
            required
            fullWidth
            autoComplete="given-name"
            label="First Name"
            value={firstName || ""}
            onChange={(e) => setFirstName(e.target.value)}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            autoComplete="family-name"
            label="Last Name"
            value={lastName || ""}
            onChange={(e) => setLastName(e.target.value)}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            autoComplete="email"
            label="Email Address"
            type="email"
            value={email || ""}
            onChange={(e) => setEmail(e.target.value)}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            autoComplete="new-password"
            label="Password"
            type="password"
            value={password || ""}
            onChange={(e) => setPassword(e.target.value)}
          />
          {signupStatus === true ? (
            <Alert severity="success">
              Registation was successful. Please login.
            </Alert>
          ) : (
            signupStatus != null && (
              <Alert severity="error">
                <AlertTitle>Error</AlertTitle>
                {signupStatus}
              </Alert>
            )
          )}
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Sign Up
          </Button>
        </form>
        <Link href="/login" underline="hover">
          {"Or Log In"}
        </Link>
      </Box>
    </Container>
  );
}
