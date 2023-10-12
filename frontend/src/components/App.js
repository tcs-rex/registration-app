import { useEffect } from "react";
import CreateRoutes from "./CreateRoutes";
import { StudentAPIProvider } from "../contexts/student-api-provider";
import { DarkModeProvider } from "../contexts/theme.context";
import { CssBaseline } from "@mui/material/";
import NavBar from "./NavBar";

//TODO: Change the name of the document in the browser bar

function App() {
  useEffect(() => {
    document.title = "Student Registration";
  }, []);

  const routes = CreateRoutes();
  return (
    <DarkModeProvider>
      <StudentAPIProvider>
        <CssBaseline />
        <NavBar />
        {routes}
      </StudentAPIProvider>
    </DarkModeProvider>
  );
}

export default App;
