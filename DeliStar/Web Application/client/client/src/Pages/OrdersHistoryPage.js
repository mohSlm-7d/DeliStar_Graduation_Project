import React, { useState, useEffect } from "react";
import { useLocation, useHistory } from "react-router-dom";
// import * as XLSX from 'xlsx'; // Import the xlsx library

import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "./OrdersHistoryPage.css";

const OrdersHistoryPage = ({ordersData, driversData}) => {
  const history = useHistory();

  const location = useLocation();
  const initialFilter = location.state || {};

  const [startDate, setStartDate] = useState(initialFilter.startDate || undefined);
  const [endDate, setEndDate] = useState(undefined);
  const [selectedStatus, setSelectedStatus] = useState(
    initialFilter.status || ""
  );
  const [filteredOrders, setFilteredOrders] = useState([]);

  const statusOptions = [
    { value: "pending", label: "pending" },
    { value: "confirmed", label: "confirmed" },
    { value: "cancelled", label: "cancelled" },
  ];

  const [selectedDriver, setSelectedDriver] = useState(initialFilter.driver || "");

  // Drivers are received from the backend
  const driverOptions = driversData.map(driver => ({
    value: driver.id,
    label: driver.name
  }));

  useEffect(() => {

    console.log("OrdersHistoryPage rendered!");

    let filtered = [...ordersData];

    // If only startDate is selected, show only orders on that date.
    if (startDate && !endDate) {
      const startDay = startDate.getDate();
      const startMonth = startDate.getMonth();
      const startYear = startDate.getFullYear();
      filtered = filtered.filter((order) => {
        const orderDay = order.date.getDate();
        const orderMonth = order.date.getMonth();
        const orderYear = order.date.getFullYear();
        return (
          orderDay === startDay &&
          orderMonth === startMonth &&
          orderYear === startYear
        );
      });
    }

    // If only endDate is selected, show orders up to and including that date.
    if (!startDate && endDate) {
      const endDay = endDate.getDate();
      const endMonth = endDate.getMonth();
      const endYear = endDate.getFullYear();
      filtered = filtered.filter((order) => {
        const orderDay = order.date.getDate();
        const orderMonth = order.date.getMonth();
        const orderYear = order.date.getFullYear();
        const orderDateIsSameOrBeforeEndDate =
          orderYear < endYear ||
          (orderYear === endYear && orderMonth < endMonth) ||
          (orderYear === endYear &&
            orderMonth === endMonth &&
            orderDay <= endDay);
        return orderDateIsSameOrBeforeEndDate
      });
    }

    // If both dates are selected, show orders between those dates (inclusive of endDate).
    if (startDate && endDate) {
      const startDay = startDate.getDate();
      const startMonth = startDate.getMonth();
      const startYear = startDate.getFullYear();
      const endDay = endDate.getDate();
      const endMonth = endDate.getMonth();
      const endYear = endDate.getFullYear();
      filtered = filtered.filter((order) => {
        const orderDay = order.date.getDate();
        const orderMonth = order.date.getMonth();
        const orderYear = order.date.getFullYear();
        const orderDateIsSameOrAfterStartDate = 
          orderYear > startYear ||
          (orderYear === startYear && orderMonth > startMonth) ||
          (orderYear === startYear && orderMonth === startMonth && orderDay >= startDay);
        const orderDateIsSameOrBeforeEndDate =
          orderYear < endYear ||
          (orderYear === endYear && orderMonth < endMonth) ||
          (orderYear === endYear && orderMonth === endMonth && orderDay <= endDay);
        return (
          orderDateIsSameOrAfterStartDate &&
          orderDateIsSameOrBeforeEndDate
        );
      });
    }

    if (selectedStatus) {
      filtered = filtered.filter((order) => order.status === selectedStatus);
    }

    if (selectedDriver) {
      filtered = filtered.filter(
        (order) => order.driverId === Number(selectedDriver)
      );
    }

    setFilteredOrders(filtered);
  }, [startDate, endDate, selectedStatus, selectedDriver]);

  const handleDownload = () => {
    // // Generate Excel worksheet from the filtered orders
    // const ws = XLSX.utils.json_to_sheet(filteredOrders);

    // // Create a new workbook and append the worksheet to it
    // const wb = XLSX.utils.book_new();
    // XLSX.utils.book_append_sheet(wb, ws, "Orders");

    // // Generate Excel file and trigger download
    // XLSX.writeFile(wb, "filtered_orders.xlsx");
  };


  const handleReset = () => {
    setStartDate(null);
    setEndDate(null);
    setSelectedStatus(null);
    setSelectedDriver(null);
  };

  return (
    <div className="ContentContainer">
      <div className="OrdersHistoryContainer">
        <div className="tools-bar FlexHori">
          <div className="FlexHori">
            <div className="FlexHori DatePickerContainer">
              <div className="FlexHori">
                <p>From: </p>
                <DatePicker
                  className="DatePicker"
                  selected={startDate}
                  onChange={(date) => setStartDate(date)}
                />
              </div>
              <div className="FlexHori">
                <p>To: </p>
                <DatePicker
                  className="DatePicker"
                  selected={endDate}
                  onChange={(date) => setEndDate(date)}
                />
              </div>
            </div>

            <div className="FlexHori">
              <p>Status: </p>
              <select
                value={selectedStatus || ""}
                onChange={(e) => setSelectedStatus(e.target.value)}
              >
                <option hidden>Select Status</option>
                {statusOptions.map((option, index) => (
                  <option key={index} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </div>

            <div className="FlexHori">
              <p>Driver: </p>
              <select
                value={selectedDriver || ""}
                onChange={(e) => setSelectedDriver(e.target.value)}
              >
                <option hidden>Select Driver</option>
                {driverOptions.map((option, index) => (
                  <option key={index} value={option.value}>
                    {option.label}
                  </option>
                ))}
              </select>
            </div>

            <button className="reset-button" onClick={handleReset}>
              Reset
            </button>
          </div>
          <button className="download-button" onClick={handleDownload}>
            Download
          </button>
        </div>
        <div className="search-results">
          <p>----- ({filteredOrders.length}) Results Found -----</p>
        </div>
        <div className="orders-list">
          {filteredOrders.map((order, index) => (
            <div
              key={index}
              className="order-card"
              onClick={() => history.push("/OrderInfo", { order })}
            >
              <div className="OrderCardLeftSide">
                <div className={`status ${order.status}`}>{order.status}</div>
                <p className="order-owner">{order.owner}</p>
              </div>
              <p className="order-number">{"#" + order.number}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
export default React.memo(OrdersHistoryPage);

