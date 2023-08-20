import React from 'react';
import { useHistory } from 'react-router-dom';
import './Home.css';
import Pending from "../Assets/HomePage/Pending.png"
import cancelled from "../Assets/HomePage/Cancelled.png"
import Delivered from "../Assets/HomePage/Delivered.png"
import PropTypes from 'prop-types';


const Home = ({ordersData, driversData}) => {
  Home.defaultProps = {
    ordersData: [],
    driversData: []
  };
	const history = useHistory();
  
  const goToOrders = (status) => {
    history.push('/OrdersHistoryPage', {
      status,
      startDate: new Date(),
    });
  };

	const countOrders = (status) => {
    if (!Array.isArray(ordersData)) return 0;
    if (!ordersData || ordersData.length === 0) return 0;

    const todaysOrders = ordersData
      .filter(order => {
        if (!order.date) return false;
        const orderDate = new Date(order.date);
        orderDate.setHours(0, 0, 0, 0);
        return orderDate.getTime() === new Date().setHours(0, 0, 0, 0);
      })
      .filter(order => order.status === status);
    
    return todaysOrders.length;
  };

  return (
		<div className='ContentContainer'>
			<div className="card-container">
					<div className="card" onClick={() => goToOrders('pending')}>
						<p className="card-text-normal">Pending</p>
						<p className="card-text-highlight">{countOrders('pending')}</p>
						<img src={Pending} alt="Icon" className="card-icon" />
					</div>
					<div className="card" onClick={() => goToOrders('cancelled')}>
						<p className="card-text-normal">Cancelled</p>
						<p className="card-text-highlight">{countOrders('cancelled')}</p>
						<img src={cancelled} alt="Icon" className="card-icon" />
					</div>
					<div className="card" onClick={() => goToOrders('confirmed')}>
						<p className="card-text-normal">Delivered</p>
						<p className="card-text-highlight">{countOrders('confirmed')}</p>
						<img src={Delivered} alt="Icon" className="card-icon" />
					</div>
			</div>
			<div className="drivers-list">
        {driversData && Array.isArray(driversData) ? driversData.map((driver) => (
          <div
            key={driver.id} // using driver.id instead of index
            className="driver-card"
            onClick={() => {
              const currentDate = new Date();
              history.push("/OrdersHistoryPage", { driver: driver.id, startDate: currentDate })
            }}
          >
            <div className="OrderCardLeftSide">
              <div className="status confirmed">Active</div>
              <p className="order-owner">{driver.name}</p>
            </div>
          </div>
        )) : null}
      </div>
		</div>
  );
}

export default Home;
