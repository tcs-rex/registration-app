import React, { useMemo, createContext } from "react";
import { ThemeProvider, createTheme } from "@mui/material/styles";
import { red } from "@mui/material/colors";
import { useSessionStorageState } from "../hooks/useSessionStorageState";

export const DarkModeContext = createContext();

export function DarkModeProvider(props) {
  const [darkMode, setDarkMode] = useSessionStorageState("darkMode", false);

  const theme = useMemo(
    () =>
      createTheme({
        palette: {
          mode: darkMode ? "dark" : "light",
          primary: {
            main: red[500],
          },
        },
      }),
    [darkMode]
  );

  return (
    <DarkModeContext.Provider value={{ darkMode, setDarkMode }}>
      <ThemeProvider theme={theme}>{props.children}</ThemeProvider>
    </DarkModeContext.Provider>
  );
}
