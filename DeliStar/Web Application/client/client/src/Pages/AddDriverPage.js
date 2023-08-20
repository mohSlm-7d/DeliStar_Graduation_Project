import React, { useState } from "react";
import "./AddDriver.css";
import { AddDriverLink } from "../Assets/Links";

const AddDriver = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [gender, setGender] = useState("");
  const [driverName, setDriverName] = useState("");
  const [driverEmail, setDriverEmail] = useState("");
  const [driverPhone, setDriverPhone] = useState("+962");
  const [driverPassword, setDriverPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState('');
  
  const adminId = localStorage.getItem("adminId");
  const adminToken = localStorage.getItem("adminToken");

  const toggleShowPassword = () => {
    setShowPassword(!showPassword);
  };

  const handleGenderChange = (event) => {
    setGender(event.target.value);
  };

  const handleAddDriver = async (e) => {
    e.preventDefault();

    const nameRegex = /^[a-zA-Z\s]*$/;
    if (!nameRegex.test(driverName)) {
      setErrorMessage("Names shouldn't contain numbers.");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(driverEmail)) {
        setErrorMessage("Please enter a valid email address.");
      return;
    }

    if (driverPhone.length !== 13 || !driverPhone.startsWith("+962")) {
        setErrorMessage(
        "Phone numbers should start with '+962 ' and the total length should be 13."
      );
      return;
    }

    try {
      const response = await fetch(AddDriverLink, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          adminId,
          adminToken,
          driverGender: gender,
          driverPhoneNo: driverPhone,
          driverEmail,
          driverPassword,
          driverName,
        }),
      });
      console.log({
        adminId,
        adminToken,
        driverGender: gender,
        driverPhoneNo: driverPhone,
        driverEmail,
        driverPassword,
        driverName,
      });
      if (response.ok) {
        const data = await response.json();
        console.log(data);
        if (data && data.admin.adminToken) {
          localStorage.setItem("adminToken", data.admin.adminToken);
          console.log(data.admin.adminToken);
        }

        if (data.status === "Success") {
          console.log("Driver added successfully");
          alert("Driver has been added successfully");
            setShowPassword("")
            setGender("")
            setDriverName("")
            setDriverEmail("")
            setDriverPhone("")
            setDriverPassword("")
            setErrorMessage("")
        } else {
          console.error(data.message);
        }
      } else {
        console.error("There was an error sending data:");
      }
    } catch (error) {
      console.error("There was an error sending data:", error);
    }
  };

  const selectClass = gender ? "selected-option" : "default-option";

  return (
    <div className="ContentContainer">
      <form className="add-driver-container" onSubmit={handleAddDriver}>
        <div className="input-bar">
          <input
          required
            placeholder="Driver Name:"
            maxLength={30}
            type="text"
            value={driverName}
            onChange={(e) => setDriverName(e.target.value)}
          />
        </div>
        <div className="input-bar">
          <input
          required
            placeholder="Driver Email:"
            maxLength={30}
            type="email"
            value={driverEmail}
            onChange={(e) => setDriverEmail(e.target.value)}
          />
        </div>
        <div className="input-bar">
          <select className={selectClass} onChange={handleGenderChange}>
            <option hidden value="">
              Driver Gender:
            </option>
            <option value="male">Male</option>
            <option value="female">Female</option>
          </select>
        </div>
        <div className="input-bar">
          <input
          required
            placeholder="Driver Phone:"
            maxLength={16}
            type="tel"
            value={driverPhone}
            onChange={(e) => setDriverPhone(e.target.value)}
          />
        </div>
        <div className="input-bar">
          <input
          required
            placeholder="Driver Password:"
            maxLength={20}
            type={showPassword ? "text" : "password"}
            value={driverPassword}
            onChange={(e) => setDriverPassword(e.target.value)}
          />
          <button className="ShowPassword" onClick={toggleShowPassword}>
            {showPassword ? "Hide" : "Show"}
          </button>
        </div>
        <button className="confirm-button" type="submit">Confirm</button>
        {errorMessage && <p className="error-message">{errorMessage}</p>}
      </form>
    </div>
  );
};

export default AddDriver;
