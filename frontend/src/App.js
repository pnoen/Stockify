import React, { useState, useEffect, createContext, useContext } from "react";
import { Route, Routes } from "react-router-dom";
import LoginPage from "./features/LoginPage";
import SignupPage from "./features/SignupPage";
import Navigation from "./components/Navigation";
import "./styles.css";

export const AuthContext = createContext();

const App = () => {
  return (
    <AuthContext.Provider>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/*" element={<Navigation />} />
      </Routes>
    </AuthContext.Provider>
  );
};

export default App;
