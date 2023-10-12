import React, { useContext, useState } from "react";
import { DarkModeContext } from "../contexts/theme.context";
import { StudentAPIContext } from "../contexts/student-api-provider";
import {
  Typography,
  AppBar,
  Toolbar,
  IconButton,
  Box,
  Tooltip,
  Menu,
  MenuItem,
} from "@mui/material/";
import { Brightness4, Brightness7, AccountCircle } from "@mui/icons-material";

function NavBar() {
  const { darkMode, setDarkMode } = useContext(DarkModeContext);
  const { studentInfo, isLoggedIn, logout } = useContext(StudentAPIContext);

  const [anchorElUser, setAnchorElUser] = useState(null);
  const settings = [{ text: "Logout", action: logout }];

  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };
  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  return (
    <AppBar position="static" sx={{ mb: 4 }}>
      <Toolbar style={{ justifyContent: "space-between" }}>
        <Typography>
          {studentInfo
            ? "Welcome, " + studentInfo.firstName + " " + studentInfo.lastName
            : "Student Registration"}
        </Typography>
        <Box sx={{ flexGrow: 0 }}>
          <Tooltip title="Toggle Theme">
            <IconButton onClick={() => setDarkMode(!darkMode)}>
              {darkMode ? <Brightness4 /> : <Brightness7 />}
            </IconButton>
          </Tooltip>
          {isLoggedIn() && (
            <Tooltip title="User Settings">
              <IconButton onClick={handleOpenUserMenu}>
                <AccountCircle />
              </IconButton>
            </Tooltip>
          )}
          <Menu
            sx={{ mt: "45px" }}
            id="menu-appbar"
            anchorEl={anchorElUser}
            anchorOrigin={{
              vertical: "top",
              horizontal: "right",
            }}
            keepMounted
            transformOrigin={{
              vertical: "top",
              horizontal: "right",
            }}
            open={Boolean(anchorElUser)}
            onClose={handleCloseUserMenu}
          >
            {settings.map((setting) => (
              <MenuItem key={setting.text} onClick={setting.action}>
                <Typography textAlign="center">{setting.text}</Typography>
              </MenuItem>
            ))}
          </Menu>
        </Box>
      </Toolbar>
    </AppBar>
  );
}

export default NavBar;
