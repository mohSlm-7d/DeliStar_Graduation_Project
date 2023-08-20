import React, { useState } from 'react';
import { useHistory,useLocation, Link } from 'react-router-dom';
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "./OrderInfoPage.css";

import { EditOrderLink, RescheduleOrderLink, CancelOrderLink } from '../Assets/Links'; 

const OrderInfoPage = ({driversData}) => {
  let history = useHistory()
  const location = useLocation();
  const PreOrder = location.state?.order || {};
  const [order, setOrder] = useState(
    { number: PreOrder.number, date: PreOrder.date, owner: PreOrder.owner, phoneNumber: PreOrder.phoneNumber,
      address: PreOrder.address, PlusCode: PreOrder.PlusCode, driverId: PreOrder.driverId }
    );
  const [isEditing, setIsEditing] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');  // For error messages


  const CancelOrder = async() => {
    const adminId = localStorage.getItem("adminId");
    const adminToken = localStorage.getItem("adminToken");
    const orderNumber = order.number

    try {
      const response = await fetch(CancelOrderLink, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          adminId,
          adminToken,
          orderNumber
        }),
      });
      console.log(adminId,
        adminToken,
        orderNumber)
      if (response.ok) {
        const data = await response.json();

        if (data && data.admin.adminToken){
          localStorage.setItem('adminToken', data.admin.adminToken);
        }

        if (data.status === "Success") {
          alert('Order cancelled successfully');
          history.push('/OrdersHistoryPage.js');
        } else {
          alert(data.message);
        }
      } else{
        setErrorMessage('There was an error sending data:');
      }
    } catch (error) {
      console.error('There was an error sending data:', error);
    }
  };

  const RescheduleOrder = () => {
    setIsEditing(true);
  };

  const EditOrder = () => {
    setIsEditing(true);
  };

  const handleFormSubmit = async(event) => {
    event.preventDefault();
    const adminId = localStorage.getItem("adminId");
    const adminToken = localStorage.getItem("adminToken");

    let short = String(order.PlusCode)
    console.log(short)
    short = short.trim()
    console.log(short)
    short = short.split(' ')[0]
    console.log(short)
      short = short.replace(/\+/g, '%2B');
      console.log(short)

    const Order = {
      orderNumber: order.number,
      orderDeliveryDate: order.date.toISOString(),
      orderDropOffAddress: order.address,
      locationPlusCode: order.PlusCode,
      orderLocationLink: "https://www.google.com/maps/search/?api=1&query=" + short, 
      assignedDriverId: order.driverId,
      customerName: order.owner,
      customerPhoneNo: order.phoneNumber
    }
    console.log(Order)
    try {
      const response = await fetch(PreOrder.status === "cancelled"? RescheduleOrderLink: EditOrderLink, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          adminId,
          adminToken,
          order:Order
        }),
      });
      console.log(Order)
      if (response.ok) {
        const data = await response.json();
        console.log(data)
        if (data && data.admin.adminToken){
          localStorage.setItem('adminToken', data.admin.adminToken);
        }

        if (data.status === "Success") {
          alert(PreOrder.status === "cancelled" ? 'Order rescheduled successfully' : 'Order edited successfully');
          history.push('/OrdersHistoryPage.js');
        } else {
          setErrorMessage(data.message);
        }
      } else {
        setErrorMessage('There was an error sending data:');
      }
    } catch (error) {
      console.error('There was an error sending data:', error);
    }

    setIsEditing(false);
  };

  return (
    <div className='ContentContainer'>
      <div className="OrderInfoContainer">
        <h1>Order Details</h1>
        {isEditing ?
          <form className='OrderDetails'>
            <label>
              Order Number:
              <input readOnly disabled type='number' value={order.number} onChange={(e) => setOrder(prev => ({ ...prev, number: e.target.value }))} />
            </label>
            <label>
              Delivery Date:
              <DatePicker
                  selected={order.date}
                  onChange={(date) =>
                  setOrder(prev => ({ ...prev, date: date }))}
               />
            </label>
            <label>
              Owner:
              <input type='text' value={order.owner} onChange={(e) => setOrder(prev => ({ ...prev, owner: e.target.value }))} />
            </label>
            <label>
              Phone Number:
              <input type='text' value={order.phoneNumber} onChange={(e) => setOrder(prev => ({ ...prev, phoneNumber: e.target.value }))} />
            </label>
            <label>
              Address:
              <input type='text' value={order.address} onChange={(e) => setOrder(prev => ({ ...prev, address: e.target.value }))} />
            </label>
            <label>
              Plus Code:
              <input type='text' value={order.PlusCode} onChange={(e) => setOrder(prev => ({ ...prev, PlusCode: e.target.value }))} />
            </label>
            <label>
              Driver:
              <select
                    name="driver"
                    value={order.driverId}
                    onChange={(e) => setOrder(prev => ({ ...prev, driverId: e.target.value }))}>
                    <option value="">Select...</option>
                    {driversData.map(driver => (
                      <option key={driver.id} value={driver.id}>
                        {driver.name}
                      </option>
                    ))}
                  </select>
            </label>
          </form> :
          <div className='OrderDetails'>
            <p><u>Order Number:</u>&nbsp; #{PreOrder.number}</p>
            <p><u>Date:</u>&nbsp; {new Date(PreOrder.date).toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric', year: 'numeric' })}</p>
            <p><u>Owner:</u>&nbsp; {PreOrder.owner}</p>
            <p><u>Phone Number:</u>&nbsp; {PreOrder.phoneNumber}</p>
            <p><u>Address:</u>&nbsp; {PreOrder.address}</p>
            <p><u>Map Link:</u>&nbsp; <Link to={{ pathname: PreOrder.mapLink }} target="_blank">{PreOrder.mapLink}</Link></p>
            <p><u>Driver:</u>&nbsp; {PreOrder.DriverName}</p>
						<p><u>Status:</u>&nbsp; {PreOrder.status}</p>
            {order.orderReport && <p><u>Order Report:</u>&nbsp; {order.orderReport}</p>}
          </div>
        }
        {PreOrder.status === "pending" && !isEditing ?
          <div className="CancelledOrderInfo">
            <p onClick={CancelOrder} className='OrderInfoButton CancelButton'>Cancel Order</p>
            <p onClick={EditOrder} className='OrderInfoButton EditButton'>Edit Order</p>
          </div>
          : null}
        {PreOrder.status === "cancelled" && !isEditing ? <p onClick={RescheduleOrder} className='OrderInfoButton RescheduleButton'>Reschedule Order</p> : null}
        {isEditing ? <p type="submit" onClick={handleFormSubmit} className='OrderInfoButton SubmitButton'>Submit</p>  : null}
      </div>
    </div>
  );
}

export default OrderInfoPage;
