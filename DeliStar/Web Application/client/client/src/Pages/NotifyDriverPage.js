import React, { useState } from 'react';
import './NotifyDriver.css';
import {NotifyDriverLink} from "../Assets/Links"
const NotifyDriver = ({driversData}) => {
    const adminId = localStorage.getItem("adminId");
    const adminToken = localStorage.getItem("adminToken");

    const [isMultiple, setIsMultiple] = useState(false);
    const [selectedDrivers, setSelectedDrivers] = useState([]);
    const [notificationMessage, setNotificationMessage] = useState('');
    const [error, setError] = useState("");


    const toggleMultiple = () => {
        setIsMultiple(!isMultiple);
    }

    const handleDriversChange = (event) => {
        const options = event.target.options;
        let selectedValues = [];
        for (let i = 0; i < options.length; i++) {
            if (options[i].selected) {
                selectedValues.push(parseInt(options[i].value, 10)); // Convert to integer
            }
        }
        setSelectedDrivers(selectedValues);
    }    

    const handleSendMessage = async (e) => {
        e.preventDefault();  // Prevents default form submission behavior
    
        console.log({ "driverIds": selectedDrivers,"notificationMssg": notificationMessage });
        
        try {
            const response = await fetch(NotifyDriverLink, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    adminId,
                    adminToken,
                    driverIds: selectedDrivers,
                    notificationMssg: notificationMessage
                }),
            });
            
            if (response.ok) {
                const data = await response.json();
    
                if (data && data.admin.adminToken){
                    localStorage.setItem('adminToken', data.admin.adminToken);
                }
    
                if (data.status === "Success") {
                    console.log('Notification sent successfully');
                    
                    // Alert the user and reset the fields
                    alert('Notification sent successfully');
                    setNotificationMessage(''); // Reset the message textarea
                    setSelectedDrivers([]); // Deselect all selected drivers
                } else {
                    setError(data.message);
                }
            } else{
                setError('There was an error sending data:');
            }
        } catch (error) {
            setError('There was an error sending the notification:', error);
        }
    }
    

    return (
        <div className='ContentContainer'>
            <form className="notify-container" onSubmit={handleSendMessage}>
                <div className="send-to-bar">
                    <p>Send to:</p>
                    <select 
                required 
                        className="select-driver" 
                        multiple={isMultiple} 
                        onChange={handleDriversChange}
                    >
                        <option value="" hidden disabled>Select Driver</option>
                        {driversData.map(driver => (
                            <option key={driver.id} value={driver.id}>
                                {driver.name}
                            </option>
                        ))}
                    </select>
                    <span className='MultipleToggle' onClick={toggleMultiple}>{isMultiple ? 'Single' : 'Multiple'}</span>
                </div>
                <textarea
                required 
                    className="message-area" 
                    rows="10" 
                    placeholder="message"
                    value={notificationMessage}
                    onChange={e => setNotificationMessage(e.target.value)}
                ></textarea>
                <button className="send-button" type='submit'>Send</button>
            </form>
        </div>
    );
}

export default NotifyDriver;
