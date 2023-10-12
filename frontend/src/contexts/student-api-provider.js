import { createContext } from "react";
import { useSessionStorageState } from "../hooks/useSessionStorageState";

// const API_URL = "http://localhost:8080/";
const API_URL = "https://gs-spring-boot-docker-iigfnkziqq-uc.a.run.app/";

export const StudentAPIContext = createContext();

export function StudentAPIProvider(props) {
  // Create a token and studentInfo for the user and save in session storage. Default value is null.
  const [token, setToken] = useSessionStorageState("token", null);
  const [studentInfo, setStudentInfo] = useSessionStorageState("student", null);
  const [studentCourses, setStudentCourses] = useSessionStorageState(
    "studentCourses",
    null
  );

  async function login(email, password) {
    // Send credentials to server and save the token from the response
    try {
      const response = await fetch(API_URL + "login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email,
          password,
        }),
      });

      const body = await response.json();
      if (body.status === "success") {
        // Set the token in session storage for use in later API calls
        const token = body.data;
        setToken(token);
        getStudent(token);
        getStudentCourses(token);
        return true;
      } else return body.message;
    } catch (e) {
      console.log(e);
      return "Server communication error";
    }
  }

  function logout() {
    setToken(null);
    setStudentInfo(null);
    setStudentCourses(null);
  }

  const isLoggedIn = () => {
    return token != null;
  };

  async function register(email, password, firstName, lastName) {
    // Send credentials to server and save the token from the response
    try {
      const response = await fetch(API_URL + "register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          firstName,
          lastName,
          email,
          password,
        }),
      });
      const body = await response.json();

      if (body.status === "success") return true;
      else return body.message;
    } catch (e) {
      console.log(e);
      return "Server communication error";
    }
  }

  async function getStudent(id) {
    // Send studentID to server and save student info from the response
    try {
      const response = await fetch(API_URL + "student/" + id, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      const body = await response.json();
      if (body.status === "success") {
        // Set the studentInfo in session storage for use in later API calls
        setStudentInfo(body.data);
        return true;
      } else {
        return false;
      }
    } catch (e) {
      console.log(e);
      return false;
    }
  }

  async function getStudentCourses(id) {
    try {
      const response = await fetch(API_URL + "student/" + id + "/courses", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      const body = await response.json();
      if (body.status === "success") {
        // Set the studentCourses in session storage for use in later API calls
        setStudentCourses(body.data);
        return true;
      } else {
        return false;
      }
    } catch (e) {
      console.log(e);
      return false;
    }
  }

  async function registerCourse(courseId, section) {
    try {
      const response = await fetch(
        API_URL + "student/" + token + "/course/" + courseId + "/" + section,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      const body = await response.json();
      if (body.status === "success") {
        // update the local cache of student courses
        getStudentCourses(token);
      }
      return body;
    } catch (e) {
      console.log(e);
      return "Server communication error";
    }
  }

  async function dropCourse(courseId, section) {
    try {
      console.log(courseId);
      console.log(section);
      const response = await fetch(
        API_URL + "student/" + token + "/course/" + courseId + "/" + section,
        {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      const body = await response.json();
      if (body.status === "success") {
        // update the local cache of student courses
        getStudentCourses(token);
      }
      return body;
    } catch (e) {
      console.log(e);
      return "Server communication error";
    }
  }

  async function getAllCourses() {
    try {
      const response = await fetch(API_URL + "course/", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      const body = await response.json();
      if (body.status === "success") {
        // Return the result
        return body.data;
      } else {
        return null;
      }
    } catch (e) {
      console.log(e);
      return null;
    }
  }

  async function getCourse(id) {
    try {
      const response = await fetch(API_URL + "course/" + id, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      const body = await response.json();
      if (body.status === "success") {
        // Return the result
        return body.data;
      } else {
        return null;
      }
    } catch (e) {
      console.log(e);
      return null;
    }
  }

  async function getCoursePrerequisites(id) {
    try {
      const response = await fetch(
        API_URL + "course/" + id + "/prerequisites",
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      const body = await response.json();
      if (body.status === "success") {
        // Return the result
        return body.data;
      } else {
        return null;
      }
    } catch (e) {
      console.log(e);
      return null;
    }
  }

  async function getCourseOfferings(id) {
    try {
      const response = await fetch(API_URL + "course/" + id + "/offerings", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      const body = await response.json();
      if (body.status === "success") {
        // Return the result
        return body.data;
      } else {
        return null;
      }
    } catch (e) {
      console.log(e);
      return null;
    }
  }

  async function searchCourse(query) {
    try {
      const response = await fetch(API_URL + "course/search?query=" + query, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      const body = await response.json();
      if (body.status === "success") {
        // Return the result
        return body.data;
      } else {
        return null;
      }
    } catch (e) {
      console.log(e);
      return null;
    }
  }

  return (
    <StudentAPIContext.Provider
      value={{
        login,
        logout,
        isLoggedIn,
        register,
        studentInfo,
        studentCourses,
        registerCourse,
        dropCourse,
        getAllCourses,
        getCourse,
        getCoursePrerequisites,
        getCourseOfferings,
        searchCourse,
      }}
    >
      {props.children}
    </StudentAPIContext.Provider>
  );
}
