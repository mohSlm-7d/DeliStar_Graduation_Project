import React, { useState } from "react";
import { useHistory } from "react-router-dom";
import { AdminRegisterLink } from "../Assets/Links.js";

import "./LoginPage.css";

const RegisterPage = () => {
  const history = useHistory();
  const [showPassword, setShowPassword] = useState(false);
  const [CompanyName, setCompanyName] = useState("");
  const [CompanyPhone, setCompanyPhone] = useState("+962");
  const [AdminPhone, setAdminPhone] = useState("+962");
  const [AdminGender, setAdminGender] = useState("");
  const [AdminName, setAdminName] = useState("");
  const [CompanyEmail, setCompanyEmail] = useState("");
  const [AdminEmail, setAdminEmail] = useState("");
  const [AdminPassword, setAdminPassword] = useState("");
  const [error, setError] = useState("");

  const toggleShowPassword = () => {
    setShowPassword(!showPassword);
  };

  const isValid = () => {
    if (/\d/.test(AdminName) || /\d/.test(CompanyName)) {
      setError("Names shouldn't contain numbers.");
      return false;
    }

    if (
      !/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/.test(AdminEmail) ||
      !/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/.test(CompanyEmail)
    ) {
      setError("Invalid email format.");
      return false;
    }

    if (
      !AdminPhone.startsWith("+962") ||
      AdminPhone.length !== 13 ||
      !CompanyPhone.startsWith("+962") ||
      CompanyPhone.length !== 13
    ) {
      setError(
        "Phone numbers should start with '+962 ' and have a total length of 13."
      );
      return false;
    }
    return true;
  };

  const handleRegisterClick = async (e) => {
    e.preventDefault()

    if (!isValid()) {
      return;
    }

    try {
      const response = await fetch(AdminRegisterLink, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          companyName: CompanyName,
          companyEmail: CompanyEmail,
          companyPhoneNo: CompanyPhone,
          adminEmail: AdminEmail,
          adminName: AdminName,
          adminGender: AdminGender,
          adminPhoneNo: AdminPhone,
          adminPassword: AdminPassword,
        }),
      });
      console.log({
        companyName: CompanyName,
        companyEmail: CompanyEmail,
        companyPhoneNo: CompanyPhone,
        adminEmail: AdminEmail,
        adminName: AdminName,
        adminGender: AdminGender,
        adminPhoneNo: AdminPhone,
        adminPassword: AdminPassword,
      });
      if (!response.ok) {
        throw new Error("Registration failed");
      }

      const data = await response.json();
      console.log(data);
      if (data.status === "Success") {
        alert("Registration succeeded!");
        history.push("/login"); // This will redirect to the LoginPage
      } else {
        setError(data.message);
      }
    } catch (error) {
      console.error(error.message);
    }
  };

  return (
    <div className="contentContainer">
      <form className="Register-container">
        <h1 className="LoginHeader">Registration Page</h1>
        <input
          required
          className="login-input"
          type="text"
          placeholder="Company Name"
          value={CompanyName}
          onChange={(e) => setCompanyName(e.target.value)}
        />
        <input
          required
          className="login-input"
          type="email"
          placeholder="Company Email"
          value={CompanyEmail}
          onChange={(e) => setCompanyEmail(e.target.value)}
        />
        <input
          required
          className="login-input"
          type="text"
          placeholder="Company Phone"
          value={CompanyPhone}
          onChange={(e) => setCompanyPhone(e.target.value)}
        />
        <input
          required
          className="login-input"
          type="text"
          placeholder="Admin Name"
          value={AdminName}
          onChange={(e) => setAdminName(e.target.value)}
        />
        <select
          required
          className="login-input"
          value={AdminGender}
          onChange={(e) => setAdminGender(e.target.value)}
        >
          <option value="" disabled>
            Admin Gender
          </option>
          <option value="Male">Male</option>
          <option value="Female">Female</option>
        </select>
        <input
          required
          className="login-input"
          type="text"
          placeholder="Admin Phone"
          value={AdminPhone}
          onChange={(e) => setAdminPhone(e.target.value)}
        />
        <input
          required
          className="login-input"
          type="email"
          placeholder="Admin Email"
          value={AdminEmail}
          onChange={(e) => setAdminEmail(e.target.value)}
        />
        <div className="login-input">
          <input
            required
            className="login-input-password"
            type={showPassword ? "text" : "password"}
            placeholder="Password"
            value={AdminPassword}
            maxLength={14}
            onChange={(e) => setAdminPassword(e.target.value)}
          />
          <button className="ShowPassword" onClick={toggleShowPassword}>
            {showPassword ? "Hide" : "Show"}
          </button>
        </div>
        <button className="login-button" onClick={handleRegisterClick}>
          Register
        </button>
        {error && <p className="error-message">{error}</p>}
      </form>
    </div>
  );
};

export default RegisterPage;
