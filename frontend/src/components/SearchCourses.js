import React, { useState, useContext } from "react";
import { StudentAPIContext } from "../contexts/student-api-provider";
import { Container, Button, TextField, Typography, Box } from "@mui/material/";
import DisplayCourses from "./DisplayCourses";

function SearchCourses() {
  const [query, setQuery] = useState();

  const { searchCourse } = useContext(StudentAPIContext);
  const [searchResults, setSearchResults] = useState(null);
  const [searching, setSearching] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setSearching(true);
    setSearchResults(null);

    const results = await searchCourse(query);
    setSearchResults(results);
    setSearching(false);
  }

  return (
    <Container>
      <Typography component="h1" variant="h4" align="center">
        Search for Courses
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
            disabled={searching}
            sx={{ ml: 1, mr: 1, mt: 3, mb: 2 }}
          >
            Search
          </Button>
        </form>
        <DisplayCourses courses={searchResults} loading={searching} />
      </Box>
    </Container>
  );
}

export default SearchCourses;
