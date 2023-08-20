import React, { useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "./AddOrderPage.css";

import { AddOrdersLink } from "../Assets/Links.js";

function AddOrderPage({ driversData }) {
  const [errorMessage, setErrorMessage] = useState("");

  const [rows, setRows] = useState([
    {
      number: "",
      date: new Date(),
      owner: "",
      phoneNumber: "+962",
      address: "",
      plusCode: "",
      driver: "",
    },
    {
      number: "",
      date: new Date(),
      owner: "",
      phoneNumber: "+962",
      address: "",
      plusCode: "",
      driver: "",
    },
    {
      number: "",
      date: new Date(),
      owner: "",
      phoneNumber: "+962",
      address: "",
      plusCode: "",
      driver: "",
    },
  ]);

  const handleAddRows = () => {
    setRows([
      ...rows,
      {
        number: "",
        date: new Date(),
        owner: "",
        phoneNumber: "+962",
        address: "",
        plusCode: "",
        driver: "",
      },
      {
        number: "",
        date: new Date(),
        owner: "",
        phoneNumber: "+962",
        address: "",
        plusCode: "",
        driver: "",
      },
      {
        number: "",
        date: new Date(),
        owner: "",
        phoneNumber: "+962",
        address: "",
        plusCode: "",
        driver: "",
      },
    ]);
  };

  const handleChange = (i, e) => {
    const { name, value } = e.target;
    let rowsCopy = [...rows];
    rowsCopy[i] = { ...rowsCopy[i], [name]: value };
    setRows(rowsCopy);
  };

  const handleAddOrders = async () => {
    const adminId = localStorage.getItem("adminId");
    const adminToken = localStorage.getItem("adminToken");

    const completeRows = rows.filter(
      (item) =>
        item.number &&
        item.date &&
        item.owner &&
        item.phoneNumber &&
        item.address &&
        item.plusCode &&
        item.driver
    );
    if (completeRows.length === 0) {
      setErrorMessage("Please enter all orders details");
      return;
    }

    for (const row of completeRows) {
      if (/\d/.test(row.owner)) {
        setErrorMessage("Owner name shouldn't contain numbers");
        return;
      }
      if (row.phoneNumber.length !== 13 || !row.phoneNumber.startsWith("+962")) {
        setErrorMessage(
          "Phone numbers should start with '+962 ' and the total length should be 13."
        );
        return;
      }
    }
    const orders = completeRows.map((item) => {
      let short = String(item.plusCode);
      short = short.trim();
      short = short.split(" ")[0];
      short = short.replace(/\+/g, "%2B");
      return {
        orderNumber: item.number,
        orderDeliveryDate: item.date.toISOString(), // Convert date object to string format
        orderDropOffAddress: item.address,
        locationPlusCode: item.plusCode,
        orderLocationLink:
          "https://www.google.com/maps/search/?api=1&query=" + short,
        assignedDriverId: item.driver, // Directly use the driver ID
        customerName: item.owner,
        customerPhoneNo: item.phoneNumber,
      };
    });

    try {
      const response = await fetch(AddOrdersLink, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          adminId,
          adminToken,
          orders,
        }),
      });
      console.log({
        adminId,
        adminToken,
        orders,
      });
      console.log(adminId, adminToken, orders);
      if (response.ok) {
        const data = await response.json();

        if (data && data.admin.adminToken) {
          localStorage.setItem("adminToken", data.admin.adminToken);
        }

        if (data.status === "Success") {
          handleAddRows();
          setRows((prevRows) =>
            prevRows.filter((row) => !completeRows.includes(row))
          ); // Remove sent rows
          setErrorMessage("")
          console.log("Successful Request");
        } else {
          console.error(data);
        }
      } else {
        console.error("There was an error sending data:");
      }
    } catch (error) {
      console.error("There was an error sending data:", error);
    }
  };

  return (
    <div className="ContentContainer">
      <div className="TableContainer">
        <h1>Add Orders</h1>
        <table>
          <thead>
            <tr>
              <th>Number</th>
              <th>Date</th>
              <th>Owner</th>
              <th>Phone number</th>
              <th>Address</th>
              <th>PlusCode</th>
              <th>Driver</th>
            </tr>
          </thead>
          <tbody>
            {rows.map((item, i) => (
              <tr key={i}>
                <td>
                  <input
                    type="number"
                    name="number"
                    value={item.number}
                    onChange={(e) => handleChange(i, e)}
                  />
                </td>
                <td>
                  <DatePicker
                    selected={item.date}
                    onChange={(date) =>
                      handleChange(i, { target: { name: "date", value: date } })
                    }
                  />
                </td>
                <td>
                  <input
                    type="text"
                    name="owner"
                    value={item.owner}
                    onChange={(e) => handleChange(i, e)}
                  />
                </td>
                <td>
                  <input
                    type="text"
                    name="phoneNumber"
                    value={item.phoneNumber}
                    onChange={(e) => handleChange(i, e)}
                  />
                </td>
                <td>
                  <input
                    type="text"
                    name="address"
                    value={item.address}
                    onChange={(e) => handleChange(i, e)}
                  />
                </td>
                <td>
                  <input
                    type="text"
                    name="plusCode"
                    value={item.plusCode}
                    onChange={(e) => handleChange(i, e)}
                  />
                </td>
                <td>
                  <select
                    name="driver"
                    value={item.driver}
                    onChange={(e) => handleChange(i, e)}
                  >
                    <option value="">Select...</option>
                    {driversData.map((driver) => (
                      <option key={driver.id} value={driver.id}>
                        {driver.name}
                      </option>
                    ))}
                  </select>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <button onClick={handleAddRows}>+</button>
        <button onClick={handleAddOrders}>Add Orders</button>
        {errorMessage && <p className="error-message">{errorMessage}</p>}
      </div>
    </div>
  );
}

export default AddOrderPage;
