import React, { useState } from "react";
import { AdminLoginLink } from '../Assets/Links.js'

import "./LoginPage.css";

const LoginPage = ({setIsAuthenticated}) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLoginClick = async (e) => {
    e.preventDefault();
  
    if (!email.trim() || !password.trim()) {
      setError("Fields cannot be empty.");
      return;
    }
  
    const emailRegex = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/;
    if (!emailRegex.test(email)) {
      setError("Invalid email format.");
      return;
    }

    try {
    const response = await fetch(AdminLoginLink, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ adminEmail:email, adminPassword:password }),
    });

    if (response.ok) {
      const data = await response.json();
      console.log(data)
      if (data && data.admin.adminToken){
        console.log(data.admin.adminToken)
        localStorage.setItem('adminToken', data.admin.adminToken);
        localStorage.setItem('adminId', data.admin.adminId);
        setIsAuthenticated(true)
        console.log('Successfully set isAuthenticated to true');
      }

      if (data.status === "Success") {
        console.log("Login successfully");
      } else {
        console.error(data.message);
      }
    }
    else{
      throw new Error('Login failed');
    }
  } catch (err) {
    setError(err.message);
    console.log("Error Message Here")
  }
};


  return (
    <div className="contentContainer">
      <form className="login-container" onSubmit={handleLoginClick}>
        <h1 className="LoginHeader">Login Page</h1>
        <input
          required
          className="login-input
          required"
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          required
          className="login-input
          required"
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button className="login-button" type="submit">Login</button>
        {error && <p className="error-message">{error}</p>}
      </form>
    </div>
  );
};

export default LoginPage;
