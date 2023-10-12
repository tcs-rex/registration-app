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

export default function Login() {
  // import the login function from the StudentAPIContext
  const { login } = useContext(StudentAPIContext);
  // keep track of email and password state
  const [email, setEmail] = useState();
  const [password, setPassword] = useState();
  // use a state to keep track of whether or not there is an error with login
  const [loginStatus, setLoginStatus] = useState(true);

  async function handleSubmit(e) {
    e.preventDefault();
    const result = await login(email, password);
    // set login error status
    setLoginStatus(result);
  }

  //TODO: Delete below. Keeping for now just to show what simple HTML looked like before using MUI components
  // return (
  //   <div className="login-wrapper">
  //     <h1>Please Log In</h1>
  //     <form onSubmit={handleSubmit}>
  //       <label>
  //         <p>Email</p>
  //         <input
  //           type="text"
  //           value={email || ""}
  //           onChange={(e) => setEmail(e.target.value)}
  //         />
  //       </label>
  //       <label>
  //         <p>Password</p>
  //         <input
  //           type="password"
  //           value={password || ""}
  //           onChange={(e) => setPassword(e.target.value)}
  //         />
  //       </label>
  //       <div>
  //         <button type="submit">Submit</button>
  //       </div>
  //       <div>
  //         <Link to="/signup">Sign Up</Link>
  //       </div>
  //     </form>
  //   </div>
  // );

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
          Log In
        </Typography>
        <form onSubmit={handleSubmit}>
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
            autoComplete="current-password"
            label="Password"
            type="password"
            value={password || ""}
            onChange={(e) => setPassword(e.target.value)}
          />
          {loginStatus !== true && (
            <Alert severity="error">
              <AlertTitle>Error</AlertTitle>
              {loginStatus}
            </Alert>
          )}
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
          >
            Log In
          </Button>
        </form>
        <Link href="/signup" underline="hover">
          {"Or Sign Up"}
        </Link>
      </Box>
    </Container>
  );
}
