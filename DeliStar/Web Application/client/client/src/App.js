import React, { useState, useEffect } from "react";
import "./style.css";
import {
  BrowserRouter as Router,
  Route,
  Switch,
  Redirect,
  useLocation,
  useHistory,
} from "react-router-dom";
import LoginPage from "./Pages/LoginPage";
import RegisterPage from "./Pages/RegisterPage";
import HomePage from "./Pages/HomePage";
import NotifyDriverPage from "./Pages/NotifyDriverPage";
import AddOrderPage from "./Pages/AddOrderPage";
import AddDriverPage from "./Pages/AddDriverPage";
import OrdersHistoryPage from "./Pages/OrdersHistoryPage";
import Logo from "./Logo.png";
import { AdminLogoutLink, GetOrdersLink, GetDriversLink } from "./Assets/Links";
import OrderInfoPage from "./Pages/OrderInfoPage";

const App = () => {
  const history = useHistory();
  const [ordersData, setOrdersData] = useState([]);
  const [driversData, setDriversData] = useState([]); //{name:"Mahmoud",id: 3},{name:"Ahmad",id: 2},{name:"Ammar",id:5 }
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    if (localStorage.getItem("adminToken")) setIsAuthenticated(true);

    if (isAuthenticated) {
      const fetchData = async () => {
        await fetchOrders();
        await fetchDrivers();
      };
      fetchData();
    }
  }, [isAuthenticated]);

  const fetchOrders = async () => {
    const adminId = localStorage.getItem("adminId");
    const adminToken = localStorage.getItem("adminToken");
    try {
      const response = await fetch(GetOrdersLink, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ adminId, adminToken }),
      });

      if (response.ok) {
        const data = await response.json();
        console.log("sent Tokin: " + localStorage.getItem("adminToken"));
        console.log(data);

        if (data.status == "Success") {
          if (data && data.admin.adminToken) {
            localStorage.setItem("adminToken", data.admin.adminToken);
          }

          if (!data || !Array.isArray(data.listOfOrders)) {
            console.error("Invalid data structure for listOfOrders:", data);
            return;
          }
          const mappedOrders = data.listOfOrders.map((item) => ({
            number: item.orderNumber,
            date: new Date(item.orderDeliveryDate),
            owner: item.customerName,
            phoneNumber: item.customerPhoneNo,
            address: item.orderDropOffAddress,
            mapLink: item.orderLocationLink,
            PlusCode: item.orderLocationPlusCode,
            driverId: item.driverId,
            DriverName: item.driverName,
            orderReport: item.orderReport,
            status: item.orderState,
          }));

          // setOrdersData([...mappedOrders]);
          setOrdersData((prevOrders) => [...prevOrders, ...mappedOrders]);

        } else {
          console.log("first");
          handleLogout();
        }
      } else {
        handleLogout();
        console.error("Error occurred while fetching orders");
      }
    } catch (error) {
      console.error("There was an error sending data:", error);
    }
  };

  const fetchDrivers = async () => {
    const adminId = localStorage.getItem("adminId");
    const adminToken = localStorage.getItem("adminToken");
    try {
      const response = await fetch(GetDriversLink, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ adminId, adminToken }),
      });

      if (response.ok) {
        const data = await response.json();
        console.log("sent Tokin: " + localStorage.getItem("adminToken"));
        console.log(data);
        if (data && data.admin.adminToken) {
          localStorage.setItem("adminToken", data.admin.adminToken);
        }
        if (!data || !Array.isArray(data.listOfDrivers)) {
          console.error("Invalid data structure for listOfDrivers:", data);
          return;
        }
        const mappedDrivers = data.listOfDrivers.map((item) => ({
          id: item.driverId,
          name: item.driverName,
          phoneNumber: item.driverPhoneNo,
          driverGender: item.driverGender,
          driverEmail: item.driverEmail,
        }));

        setDriversData((prevDrivers) => [...prevDrivers, ...mappedDrivers]);
        if (data.status === "Success") {
        } else {
          console.error(data.message);
        }
      } else {
        console.error("Error occurred while fetching drivers");
      }
    } catch (error) {
      console.error("There was an error fetching drivers:", error);
    }
  };

  const handleLogout = async () => {
    // Clear the stored token
    const adminId = localStorage.getItem("adminId");
    const adminToken = localStorage.getItem("adminToken");
    try {
      const response = await fetch(AdminLogoutLink, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ adminId, adminToken }),
      });
      console.log(response);
      if (!response.ok) {
        console.error("Error occurred while logging out");
      }
    } catch (error) {
      console.error("There was an error sending data:", error);
    }

    localStorage.removeItem("adminToken");
    localStorage.removeItem("adminId");
    setIsAuthenticated(false);
    history.push("/login");
  };

  return (
    <Router>
      <HeaderAndNavigation
        isAuthenticated={isAuthenticated}
        onLogout={handleLogout}
      />
      <div className="content">
        <Switch>
          <Route path="/login">
            {isAuthenticated ? (
              <Redirect to="/home" />
            ) : (
              <LoginPage setIsAuthenticated={setIsAuthenticated} />
            )}
          </Route>
          <Route path="/register">
            {!isAuthenticated ? <RegisterPage /> : <Redirect to="/home" />}
          </Route>
          <PrivateRoute
            path="/home"
            component={HomePage}
            isAuthenticated={isAuthenticated}
            ordersData={ordersData}
            driversData={driversData}
          />
          <PrivateRoute
            path="/NotifyDriverPage"
            component={NotifyDriverPage}
            isAuthenticated={isAuthenticated}
            driversData={driversData}
          />
          <PrivateRoute
            path="/AddOrderPage"
            component={AddOrderPage}
            isAuthenticated={isAuthenticated}
            driversData={driversData}
          />
          <PrivateRoute
            path="/AddDriverPage"
            component={AddDriverPage}
            isAuthenticated={isAuthenticated}
          />
          <PrivateRoute
            path="/OrdersHistoryPage"
            component={OrdersHistoryPage}
            isAuthenticated={isAuthenticated}
            ordersData={ordersData}
            driversData={driversData}
          />
          <PrivateRoute
            path="/OrderInfo"
            component={OrderInfoPage}
            isAuthenticated={isAuthenticated}
            driversData={driversData}
          />
          <Redirect from="/" to="/login" />
        </Switch>
      </div>
    </Router>
  );
};

const HeaderAndNavigation = ({ isAuthenticated, onLogout }) => {
  const history = useHistory();
  const location = useLocation();

  return (
    <>
      <header className="header">
        <img className="header-logo" src={Logo} alt="Logo" />
        {isAuthenticated ? (
          <div className="header-title">
            <div className="Logout" onClick={onLogout}>
              Logout
            </div>
          </div>
        ) : (
          <button
            className="RegisterButton"
            onClick={() => {
              if (history.location.pathname === "/register") {
                history.push("/login");
              } else {
                history.push("/register");
              }
            }}
          >
            {history.location.pathname === "/register" ? "Login" : "Register"}
          </button>
        )}
      </header>

      {isAuthenticated && (
        <nav className="nav-bar">
          <ul>
            <li
              className={`nav-item ${
                location.pathname === "/home" ? "current" : ""
              }`}
              onClick={() => history.push("/home")}
            >
              HomePage
            </li>
            <li
              className={`nav-item ${
                location.pathname === "/NotifyDriverPage" ? "current" : ""
              }`}
              onClick={() => history.push("/NotifyDriverPage")}
            >
              Notify Driver
            </li>
            <li
              className={`nav-item ${
                location.pathname === "/AddOrderPage" ? "current" : ""
              }`}
              onClick={() => history.push("/AddOrderPage")}
            >
              Add Order
            </li>
            <li
              className={`nav-item ${
                location.pathname === "/AddDriverPage" ? "current" : ""
              }`}
              onClick={() => history.push("/AddDriverPage")}
            >
              Add Driver
            </li>
            <li
              className={`nav-item ${
                location.pathname === "/OrdersHistoryPage" ? "current" : ""
              }`}
              onClick={() => history.push("/OrdersHistoryPage")}
            >
              Orders History
            </li>
          </ul>
        </nav>
      )}
    </>
  );
};

const PrivateRoute = ({ component: Component, isAuthenticated, ...rest }) => {
  return (
    <Route
      {...rest}
      render={(props) =>
        isAuthenticated ? (
          <Component {...props} {...rest} />
        ) : (
          <Redirect
            to={{ pathname: "/login", state: { from: props.location } }}
          />
        )
      }
    />
  );
};

export default App;

// TestOrders =[
//   {
//   id: 1,
//   date: new Date("2023-08-13"),
//   status: "pending",
//   owner: "Emily Johnson",
//   number: "701",
//   phoneNumber: "701",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: 1,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 2,
//   date: new Date("2023-08-13"),
//   status: "confirmed",
//   owner: "Ava Wilson",
//   number: "1234",
//   phoneNumber: "1234",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 3,
//   date: new Date("2023-08-13"),
//   status: "cancelled",
//   owner: "Noah Garcia",
//   number: "912",
//   phoneNumber: "912",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "The customer didn't showup",
//   customerId: 11,
// },
// {
//   id: 4,
//   date: new Date("2023-08-13"),
//   status: "confirmed",
//   owner: "Mahmoud Owaida",
//   number: "898",
//   phoneNumber: "898",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 5,
//   date: new Date("2023-07-15"),
//   status: "pending",
//   owner: "Ali Salah",
//   number: "321",
//   phoneNumber: "321",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: 2,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 6,
//   date: new Date("2023-08-13"),
//   status: "confirmed",
//   owner: "Owner 1",
//   number: "1001",
//   phoneNumber: "1001",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 7,
//   date: new Date("2023-08-13"),
//   status: "cancelled",
//   owner: "Owner 2",
//   number: "1002",
//   phoneNumber: "1002",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "The customer didn't showup",
//   customerId: 11,
// },
// {
//   id: 8,
//   date: new Date("2023-08-13"),
//   status: "pending",
//   owner: "Owner 49",
//   number: "1049",
//   phoneNumber: "1049",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: 3,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 9,
//   date: new Date("2023-08-13"),
//   status: "confirmed",
//   owner: "Owner 50",
//   number: "1050",
//   phoneNumber: "1050",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 10,
//   date: new Date("2023-08-13"),
//   status: "cancelled",
//   owner: "Owner 3",
//   number: "1003",
//   phoneNumber: "1003",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "The customer didn't showup",
//   customerId: 11,
// },
// {
//   id: 11,
//   date: new Date("2023-08-13"),
//   status: "confirmed",
//   owner: "Owner 4",
//   number: "1004",
//   phoneNumber: "1004",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 12,
//   date: new Date("2023-07-05"),
//   status: "pending",
//   owner: "Owner 5",
//   number: "1005",
//   phoneNumber: "1005",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: 4,
//   orderReport: "",
//   customerId: 11,
// },
// // ... more items ...
// {
//   id: 13,
//   date: new Date("2023-07-28"),
//   status: "confirmed",
//   owner: "Owner 28",
//   number: "1028",
//   phoneNumber: "1028",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 14,
//   date: new Date("2023-07-29"),
//   status: "pending",
//   owner: "Owner 29",
//   number: "1029",
//   phoneNumber: "1029",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: 5,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 15,
//   date: new Date("2023-08-13"),
//   status: "cancelled",
//   owner: "Owner 30",
//   number: "1030",
//   phoneNumber: "1030",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "The customer didn't showup",
//   customerId: 11,
// },
// {
//   id: 16,
//   date: new Date("2023-08-13"),
//   status: "confirmed",
//   owner: "Owner 31",
//   number: "1031",
//   phoneNumber: "1031",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 17,
//   date: new Date("2023-07-01"),
//   status: "pending",
//   owner: "Owner 32",
//   number: "1032",
//   phoneNumber: "1032",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: 6,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 18,
//   date: new Date("2023-07-02"),
//   status: "cancelled",
//   owner: "Owner 33",
//   number: "1033",
//   phoneNumber: "1033",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "The customer didn't showup",
//   customerId: 11,
// },
// {
//   id: 19,
//   date: new Date("2023-07-28"),
//   status: "confirmed",
//   owner: "Owner 49",
//   number: "1049",
//   phoneNumber: "1049",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 20,
//   date: new Date("2023-07-29"),
//   status: "pending",
//   owner: "Owner 50",
//   number: "1050",
//   phoneNumber: "1050",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: 7,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 21,
//   date: new Date("2023-08-13"),
//   status: "cancelled",
//   owner: "Owner 51",
//   number: "1051",
//   phoneNumber: "1051",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "The customer didn't showup",
//   customerId: 11,
// },
// {
//   id: 22,
//   date: new Date("2023-08-13"),
//   status: "confirmed",
//   owner: "Owner 52",
//   number: "1052",
//   phoneNumber: "1052",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 23,
//   date: new Date("2023-07-01"),
//   status: "pending",
//   owner: "Owner 53",
//   number: "1053",
//   phoneNumber: "1053",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: 8,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 24,
//   date: new Date("2023-07-02"),
//   status: "cancelled",
//   owner: "Owner 54",
//   number: "1054",
//   phoneNumber: "1054",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "The customer didn't showup",
//   customerId: 11,
// },
// {
//   id: 25,
//   date: new Date("2023-07-28"),
//   status: "confirmed",
//   owner: "Owner 76",
//   number: "1076",
//   phoneNumber: "1076",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 20,
//   DriverName:"Hamza Al-Omar",
//   orderRank: null,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 26,
//   date: new Date("2023-07-29"),
//   status: "pending",
//   owner: "Owner 77",
//   number: "1077",
//   phoneNumber: "1077",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 2,
//   DriverName:"Mohammed Salman",
//   orderRank: 9,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 27,
//   date: new Date("2023-08-13"),
//   status: "cancelled",
//   owner: "Owner 78",
//   number: "1078",
//   phoneNumber: "1078",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 2,
//   DriverName:"Mohammed Salman",
//   orderRank: null,
//   orderReport: "The customer didn't showup",
//   customerId: 11,
// },
// {
//   id: 28,
//   date: new Date("2023-08-13"),
//   status: "confirmed",
//   owner: "Owner 79",
//   number: "1079",
//   phoneNumber: "1079",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 2,
//   DriverName:"Mohammed Salman",
//   orderRank: null,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 29,
//   date: new Date("2023-07-01"),
//   status: "pending",
//   owner: "Owner 80",
//   number: "1080",
//   phoneNumber: "1080",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 2,
//   DriverName:"Mohammed Salman",
//   orderRank: 10,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 30,
//   date: new Date("2023-07-02"),
//   status: "cancelled",
//   owner: "Owner 81",
//   number: "1081",
//   phoneNumber: "1081",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 2,
//   DriverName:"Mohammed Salman",
//   orderRank: null,
//   orderReport: "The customer didn't showup",
//   customerId: 11,
// },
// {
//   id: 31,
//   date: new Date("2023-07-28"),
//   status: "confirmed",
//   owner: "Owner 98",
//   number: "1098",
//   phoneNumber: "1098",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 1,
//   DriverName:"Ali Salah",
//   orderRank: null,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 32,
//   date: new Date("2023-07-29"),
//   status: "pending",
//   owner: "Owner 99",
//   number: "1099",
//   phoneNumber: "1099",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 1,
//   DriverName:"Ali Salah",
//   orderRank: 11,
//   orderReport: "",
//   customerId: 11,
// },
// {
//   id: 33,
//   date: new Date("2023-08-13"),
//   status: "cancelled",
//   owner: "Owner 100",
//   number: "1100",
//   phoneNumber: "1100",
//   address: "building 18 near the School",
//   PlusCode:"2X72+6C Amman",
//   mapLink: "https://maps.app.goo.gl/8ydc6FcHhsTS7xya8",
//   driverId: 1,
//   DriverName:"Ali Salah",
//   orderRank: null,
//   orderReport: "The customer didn't showup",
//   customerId: 11,
// },
// // add more orders here

// ]
