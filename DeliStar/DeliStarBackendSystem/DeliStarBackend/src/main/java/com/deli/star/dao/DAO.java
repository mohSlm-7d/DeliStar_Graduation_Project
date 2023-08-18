package com.deli.star.dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.time.LocalDate;
import java.util.ArrayList;

import com.deli.star.DTO.Admin;
import com.deli.star.DTO.Company;
import com.deli.star.DTO.Customer;
import com.deli.star.DTO.Driver;
import com.deli.star.DTO.Notification;
import com.deli.star.DTO.Order;
public class DAO {
	private static Connection conn = null;

	public DAO() {
		//System.out.println("DAO constructor!");
		//System.out.println("connections is close: "+DAO.conn.isClosed());
		this.OpenConnection();
	}
	public void OpenConnection () {
		try {

			if(DAO.conn == null || DAO.conn.isClosed()) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				DAO.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/delistar", "root", "101001#Amem");
				DAO.conn.setAutoCommit(false);
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}



	public boolean UpdateAdminToken(Admin admin) {
		try { 
			this.OpenConnection();

			String updateStatement;
			PreparedStatement pst;

			if(admin.getAdminId() != 0) {
				updateStatement = "UPDATE `admin` SET `admin_token`= ? WHERE `admin_id` = ?";

				pst = DAO.conn.prepareStatement(updateStatement);
				pst.setString(1, admin.getAdminToken());
				pst.setInt(2, admin.getAdminId());
			}
			else {
				updateStatement = "UPDATE `admin` SET `admin_token`= ? WHERE `admin_email` = ?";
				pst = DAO.conn.prepareStatement(updateStatement);
				pst.setString(1, admin.getAdminToken());
				pst.setString(2, admin.getAdminEmail());
			}


			int NoOfAffectedRecords = pst.executeUpdate();

			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}


			DAO.conn.commit();

			pst.close();
			this.CloseConnection();

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return false;
	}

	public boolean AddAdmin(Admin newAdmin) {
		try { 
			this.OpenConnection();

			String insertStatement = "INSERT INTO `Admin`"
					+ "(admin_name,"
					+ "admin_email,"
					+ "admin_password,"
					+ "admin_gender,"
					+ "admin_phone_no,"
					+ "company_id)"

					+" values(?, ?, ?, ?, ?, ?)";

			PreparedStatement pst = DAO.conn.prepareStatement(insertStatement);
			pst.setString(1, newAdmin.getAdminName());
			pst.setString(2, newAdmin.getAdminEmail());
			pst.setString(3, newAdmin.getAdminPassword());
			pst.setString(4, newAdmin.getAdminGender());
			pst.setString(5, newAdmin.getAdminPhoneNo());
			pst.setInt(6, newAdmin.getCompany().getCompanyId());
			System.out.println("IN DAO comp ID: " + newAdmin.getCompany().getCompanyId());
			System.out.println("IN DAO add admin insert query: " + pst.toString());
			int NoOfAffectedRecords = pst.executeUpdate();

			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}


			DAO.conn.commit();
			pst.close();

			this.CloseConnection();		
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return false;

	}

	public Admin GetAdmin(Admin admin){
		try { 
			this.OpenConnection();

			String query;
			PreparedStatement pst;

			if(admin.getAdminId() != 0) {
				query = "SELECT `admin`.`admin_id`,"
						+ " `admin`.`admin_name`,"
						+ " `admin`.`admin_email`,"
						+ " `admin`.`admin_password`,"
						+ " `admin`.`admin_gender`,"
						+ " `admin`.`admin_phone_no`,"
						+ " `admin`.`admin_token`,"
						+ " `company`.`company_id`,"
						+ " `company`.`company_name`,"
						+ " `company`.`company_email`,"
						+ " `company`.`company_phone_no`"
						+ "FROM `delistar`.`admin`, `delistar`.`company` where `admin`.`admin_id` = + ? and `company`.`company_id` = `admin`.`company_id`;";

				pst = DAO.conn.prepareStatement(query);
				pst.setInt(1, admin.getAdminId());


			}
			else {
				query = "SELECT `admin`.`admin_id`,"
						+ " `admin`.`admin_name`,"
						+ " `admin`.`admin_email`,"
						+ " `admin`.`admin_password`,"
						+ " `admin`.`admin_gender`,"
						+ " `admin`.`admin_phone_no`,"
						+ " `admin`.`admin_token`,"
						+ " `company`.`company_id`,"
						+ " `company`.`company_name`,"
						+ " `company`.`company_email`,"
						+ " `company`.`company_phone_no`"
						+ "FROM `delistar`.`admin`, `delistar`.`company` where `admin`.`admin_email` = ? and `company`.`company_id` = `admin`.`company_id`;";

				pst = DAO.conn.prepareStatement(query);
				pst.setString(1, admin.getAdminEmail());

			}


			ResultSet SetOfRecords = pst.executeQuery();

			Admin NewAdmin = new Admin();
			while(SetOfRecords.next()) {
				NewAdmin.setAdminId(SetOfRecords.getInt(1));
				NewAdmin.setAdminName(SetOfRecords.getString(2));
				NewAdmin.setAdminEmail(SetOfRecords.getString(3));
				NewAdmin.setAdminPassword(SetOfRecords.getString(4).toString());
				NewAdmin.setAdminGender(SetOfRecords.getString(5));

				NewAdmin.setAdminPhoneNo(SetOfRecords.getString(6));
				NewAdmin.setAdminToken(SetOfRecords.getString(7));

				Company adminCompany = new Company();
				adminCompany.setCompanyId(SetOfRecords.getInt(8));
				adminCompany.setCompanyName(SetOfRecords.getString(9));
				adminCompany.setCompanyEmail(SetOfRecords.getString(10));
				adminCompany.setCompanyPhoneNo(SetOfRecords.getString(11));

				NewAdmin.setCompany(adminCompany);
			}

			SetOfRecords.close();
			pst.close();
			this.CloseConnection();

			return NewAdmin;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return null;
	}


	public Company GetAdminCompany(Admin admin){
		try { 
			this.OpenConnection();

			String query = "SELECT `company_id` FROM `admin` where `admin_id` = ? ;";

			PreparedStatement pst = DAO.conn.prepareStatement(query);
			pst.setInt(1, admin.getAdminId());

			ResultSet SetOfRecords = pst.executeQuery();

			Company company = new Company();
			while(SetOfRecords.next()) {
				company.setCompanyId(SetOfRecords.getInt(1));
			}

			SetOfRecords.close();
			pst.close();
			this.CloseConnection();

			return company;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return null;
	}

	public Admin GetAdminCredentials(Admin admin){
		try {			
			this.OpenConnection();


			String query;
			PreparedStatement pst;
			if(admin.getAdminId() != 0){
				query = "SELECT `admin_id`, `admin_token`, `admin_email`, `admin_password` FROM `admin` where `admin_id` = ? ;";
				pst = DAO.conn.prepareStatement(query);
				pst.setInt(1, admin.getAdminId());
			}
			else {
				query = "SELECT `admin_id`, `admin_token`, `admin_email`, `admin_password` FROM `admin` where `admin_email` = ? ;";
				pst = DAO.conn.prepareStatement(query);
				pst.setString(1, admin.getAdminEmail());
			}

			ResultSet SetOfRecords = pst.executeQuery();

			Admin NewAdmin = new Admin();
			while(SetOfRecords.next()) {
				NewAdmin.setAdminId(SetOfRecords.getInt(1));
				NewAdmin.setAdminToken(SetOfRecords.getString(2));
				NewAdmin.setAdminEmail(SetOfRecords.getString(3));
				NewAdmin.setAdminPassword(SetOfRecords.getString(4));
			}

			SetOfRecords.close();
			pst.close();
			this.CloseConnection();

			return NewAdmin;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return null;
	}

	public boolean UpdateCompany(Company updatedCompany) {
		try { 
			this.OpenConnection();

			String updateStatement = "UPDATE `delistar`.`company` "
					+ "SET"
					+ " `company_name` = ? ,"
					+ " `company_email` = ? ,"
					+ " `company_phone_no` = ?"
					+ " WHERE `company_id` = ? ;";

			PreparedStatement pst = DAO.conn.prepareStatement(updateStatement);
			pst.setString(1, updatedCompany.getCompanyName());
			pst.setString(2, updatedCompany.getCompanyEmail());
			pst.setString(3, updatedCompany.getCompanyPhoneNo());
			pst.setInt(4, updatedCompany.getCompanyId());

			int NoOfRecordsAffected = pst.executeUpdate();
			if(NoOfRecordsAffected == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}


			pst.close();
			this.CloseConnection();

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return false;
	}

	public boolean UpdateAdmin(Admin editedAdmin) {
		try { 
			this.OpenConnection();

			String updateStatement = "UPDATE `admin` SET "		
					+ "`admin_name` = ? ,"
					+ "`admin_email` = ? ,"
					+ "`admin_password` = ? ,"
					+ "`admin_gender` = ? ,"
					+ "`admin_phone_no` = ? ,"
					+ "`admin_token` = ? "
					+ "WHERE `admin_id` = ?";

			PreparedStatement pst = DAO.conn.prepareStatement(updateStatement);
			pst.setString(1, editedAdmin.getAdminName());
			pst.setString(2, editedAdmin.getAdminEmail());
			pst.setString(3, editedAdmin.getAdminPassword());
			pst.setString(4, editedAdmin.getAdminGender());
			pst.setString(5, editedAdmin.getAdminPhoneNo());
			pst.setString(6, editedAdmin.getAdminToken());
			pst.setInt(7, editedAdmin.getAdminId());

			int NoOfAffected = pst.executeUpdate();

			if(NoOfAffected == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}


			DAO.conn.commit();
			pst.close();

			this.CloseConnection();		
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return false;
	}
	public boolean DeleteAdmin(Admin admin) {
		try { 
			this.OpenConnection();

			String deleteStatement = "delete from `admin` Where admin_id = ?";
			PreparedStatement pst = DAO.conn.prepareStatement(deleteStatement);
			pst.setInt(1, admin.getAdminId());

			int NoOfAffectedRecords = pst.executeUpdate();

			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}



			DAO.conn.commit();

			pst.close();
			this.CloseConnection();

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return false;
	}



	public boolean UpdateDriverToken(Driver driver) {
		try { 
			this.OpenConnection();

			String updateStatement;
			PreparedStatement pst;
			if(driver.getDriverId() != 0) {
				updateStatement = "UPDATE `driver` SET `driver_token`= ? Where `driver_id` = ?";
				pst = DAO.conn.prepareStatement(updateStatement);
				pst.setString(1, driver.getDriverToken());
				pst.setInt(2, driver.getDriverId());
			}
			else {
				updateStatement = "UPDATE `driver` SET `driver_token`= ? Where `driver_email` = ?";
				pst = DAO.conn.prepareStatement(updateStatement);
				pst.setString(1, driver.getDriverToken());
				pst.setString(2, driver.getDriverEmail());
			}




			int NoOfAffectedRecords = pst.executeUpdate();

			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}


			DAO.conn.commit();

			pst.close();
			this.CloseConnection();

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return false;
	}

	public boolean AddDriver(Driver newDriver) {
		try { 
			this.OpenConnection();

			String insertStatement = "INSERT INTO `driver`"
					+ "(`driver_name`,"
					+ "`driver_email`,"
					+ "`driver_password`,"
					+ "`driver_gender`,"
					+ "`driver_phone_no`,"
					+ "`company_id`)"					
					+ "VALUES(?, ?, ?, ?, ?, ?)";

			PreparedStatement pst = DAO.conn.prepareStatement(insertStatement);
			pst.setString(1, newDriver.getDriverName());
			pst.setString(2, newDriver.getDriverEmail());
			pst.setString(3, newDriver.getDriverPassword());
			pst.setString(4, newDriver.getDriverGender());
			pst.setString(5, newDriver.getDriverPhoneNo());
			pst.setInt(6, newDriver.getCompany().getCompanyId());

			int NoOfAffectedRecords = pst.executeUpdate();

			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}



			DAO.conn.commit();

			pst.close();
			this.CloseConnection();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return false;		
	}

	public Driver GetDriver(Driver driver) {
		try { 
			this.OpenConnection();

			String query;
			PreparedStatement pst;

			if(driver.getDriverId() != 0) {
				query = "SELECT `driver`.`driver_id`,"
						+ " `driver`.`driver_name`,"
						+ " `driver`.`driver_email`,"
						+ " `driver`.`driver_password`,"
						+ " `driver`.`driver_gender`,"
						+ " `driver`.`driver_phone_no`,"
						+ " `driver`.`isBanned`,"
						+ " `driver`.`bannedUntil`,"
						+ " `driver`.`driver_token`,"
						+ " `company`.`company_id`,"
						+ " `company`.`company_name`,"
						+ " `company`.`company_email`,"
						+ " `company`.`company_phone_no`"
						+ "FROM `delistar`.`driver`, `delistar`.`company` where `driver`.`driver_id` = ? and `company`.`company_id` = `driver`.`company_id`;";
				pst = DAO.conn.prepareStatement(query);
				pst.setInt(1, driver.getDriverId());

			}
			else {
				query = "SELECT `driver`.`driver_id`,"
						+ " `driver`.`driver_name`,"
						+ " `driver`.`driver_email`,"
						+ " `driver`.`driver_password`,"
						+ " `driver`.`driver_gender`,"
						+ " `driver`.`driver_phone_no`,"
						+ " `driver`.`isBanned`,"
						+ " `driver`.`bannedUntil`,"
						+ " `driver`.`driver_token`,"
						+ " `company`.`company_id`,"
						+ " `company`.`company_name`,"
						+ " `company`.`company_email`,"
						+ " `company`.`company_phone_no`"
						+ "FROM `delistar`.`driver`, `delistar`.`company` where `driver`.`driver_email` = ? and `company`.`company_id` = `driver`.`company_id`;";

				pst = DAO.conn.prepareStatement(query);
				pst.setString(1, driver.getDriverEmail());

			}


			ResultSet SetOfRecords = pst.executeQuery();
			Driver NewDriver = new Driver();
			while(SetOfRecords.next()) {
				NewDriver.setDriverId(SetOfRecords.getInt(1));
				NewDriver.setDriverName(SetOfRecords.getString(2));
				NewDriver.setDriverEmail(SetOfRecords.getString(3));
				NewDriver.setDriverPassword(SetOfRecords.getString(4));
				NewDriver.setDriverGender(SetOfRecords.getString(5));

				NewDriver.setDriverPhoneNo(SetOfRecords.getString(6));



				NewDriver.setIsBanned(SetOfRecords.getBoolean(7));
				NewDriver.setBannedUntil(SetOfRecords.getDate(8) != null ?
						SetOfRecords.getDate(8).toLocalDate() :
							null);
				NewDriver.setDriverToken(SetOfRecords.getString(9));

				Company driverCompany = new Company();
				driverCompany.setCompanyId(SetOfRecords.getInt(10));
				driverCompany.setCompanyName(SetOfRecords.getString(11));
				driverCompany.setCompanyEmail(SetOfRecords.getString(12));
				driverCompany.setCompanyPhoneNo(SetOfRecords.getString(13));
				NewDriver.setCompany(driverCompany); 
			}

			SetOfRecords.close();
			pst.close();
			this.CloseConnection();

			return NewDriver;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return null;
	}

	public ArrayList<Driver> GetCompanyDrivers(Company company) {
		try { 
			this.OpenConnection();

			String query = "SELECT * from `driver` where `driver`.`company_id` = ? ;";
			PreparedStatement pst = DAO.conn.prepareStatement(query);
			pst.setInt(1, company.getCompanyId());


			ResultSet SetOfRecords = pst.executeQuery();
			ArrayList<Driver> companyDrivers = new ArrayList<Driver>();

			while(SetOfRecords.next()) {
				Driver NewDriver = new Driver();
				NewDriver.setDriverId(SetOfRecords.getInt(1));
				NewDriver.setDriverName(SetOfRecords.getString(2));
				NewDriver.setDriverEmail(SetOfRecords.getString(3));
				NewDriver.setDriverGender(SetOfRecords.getString(5));

				NewDriver.setDriverPhoneNo(SetOfRecords.getString(6));



				NewDriver.setIsBanned(SetOfRecords.getBoolean(8));
				NewDriver.setBannedUntil(SetOfRecords.getDate(9) != null ?
						SetOfRecords.getDate(9).toLocalDate() :
							null);

				companyDrivers.add(NewDriver);

			}

			SetOfRecords.close();
			pst.close();
			this.CloseConnection();

			return companyDrivers;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return null;
	}

	public Company GetDriverCompany(Driver driver){
		try { 
			this.OpenConnection();

			String query = "SELECT `company_id` FROM `driver` where `driver_id` = ?";

			PreparedStatement pst = DAO.conn.prepareStatement(query);
			pst.setInt(1, driver.getDriverId());

			ResultSet SetOfRecords = pst.executeQuery();
			Company company = new Company();
			while(SetOfRecords.next()) {
				company.setCompanyId(SetOfRecords.getInt(1));
			}

			SetOfRecords.close();
			pst.close();
			this.CloseConnection();

			return company;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return null;
	}

	public Driver GetDriverCredentials(Driver driver){
		try { 
			this.OpenConnection();

			String query = "SELECT `driver_token`, `driver_email`, `driver_password` FROM `driver` where `driver_id` = ? ;";

			PreparedStatement pst = DAO.conn.prepareStatement(query);
			pst.setInt(1, driver.getDriverId());

			ResultSet SetOfRecords = pst.executeQuery();
			Driver NewDriver = new Driver();
			while(SetOfRecords.next()) {
				NewDriver.setDriverToken(SetOfRecords.getString(1));
				NewDriver.setDriverEmail(SetOfRecords.getString(2));
				NewDriver.setDriverPassword(SetOfRecords.getString(3));
			}

			SetOfRecords.close();
			pst.close();
			this.CloseConnection();

			return NewDriver;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return null;
	}


	public boolean UpdateDriver(Driver editedDriver) {
		try {
			this.OpenConnection();

			String updateStatement = "UPDATE `driver` SET"
					+ "`driver_name` = ?, "
					+ "`driver_email` = ?, "
					+ "`driver_password` = ?, "
					+ "`driver_gender` = ?, "
					+ "`driver_phone_no` = ?, "
					+ "`isBanned` = ?, "
					+ "`bannedUntil` = ?, " 
					+"`driver_token` = ?"
					+ " where `driver_id` = ?";



			PreparedStatement pst = DAO.conn.prepareStatement(updateStatement);
			pst.setString(1, editedDriver.getDriverName());
			pst.setString(2, editedDriver.getDriverEmail());
			pst.setString(3, editedDriver.getDriverPassword());
			pst.setString(4, editedDriver.getDriverGender());
			pst.setString(5, editedDriver.getDriverPhoneNo());
			pst.setBoolean(6, editedDriver.getIsBanned());
			pst.setDate(7, editedDriver.getBannedUntil() == null ?
					null
					: Date.valueOf(editedDriver.getBannedUntil()));
			pst.setString(8, editedDriver.getDriverToken());
			pst.setInt(9, editedDriver.getDriverId());

			int NoOfAffected = pst.executeUpdate();

			if(NoOfAffected == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}

			DAO.conn.commit();
			pst.close();

			this.CloseConnection();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return false;
	}
	public boolean DeleteDriver(Driver driver) {
		try { 
			this.OpenConnection();

			String deleteStatement = "delete from `driver` Where driver_id = ?";
			PreparedStatement pst = DAO.conn.prepareStatement(deleteStatement);
			pst.setInt(1, driver.getDriverId());

			int NoOfAffectedRecords = pst.executeUpdate();

			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}

			DAO.conn.commit();

			pst.close();
			this.CloseConnection();

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return false;
	}



	public boolean AddOrder(Order newOrder) {
		try { 
			this.OpenConnection();

			String insertStatement = "INSERT INTO `order`"

				+ "(`order_delivery_date`,"
				+ "`order_drop_off_address`,"
				+ "`order_location_link`,"
				+ "`customer_id`,"
				+ "`driver_id`,"
				+ "`company_id`, "
				+ "`order_number`, "
				+ "`location_coordinates`,"
				+ "`location_plus_code`)"

				+ "VALUES(?, "
				+ "?, "
				+ "?, "
				+ "?, "
				+ "?, "
				+ "?, "
				+ "?, "
				+ "?,"
				+ "?)";

			PreparedStatement pst = DAO.conn.prepareStatement(insertStatement);
			pst.setString(1, newOrder.getOrderDeliveryDate().toString());
			pst.setString(2, newOrder.getOrderDropOffAddress());
			pst.setString(3, newOrder.getOrderLocationLink());
			pst.setInt(4, newOrder.getCustomer().getCustomerId());
			pst.setInt(5, newOrder.getAssignedDriver().getDriverId());
			pst.setInt(6, newOrder.getCompanyId());
			pst.setInt(7, newOrder.getOrderNumber());
			pst.setString(8, newOrder.getLocationCoordinates());
			pst.setString(9, newOrder.getLocationPlusCode());



			int NoOfAffectedRecords = pst.executeUpdate();

			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}


			DAO.conn.commit();


			pst.close();
			this.CloseConnection();

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return false;



	}

	public ArrayList<Order> GetDriverOrders(Driver driver, String orderState){
		try { 
			this.OpenConnection();

			String query = "SELECT `order`.`order_id`,"
					+ " `order`.`order_number`,"
					+ " `order`.`order_report`,"
					+ " `order`.`order_state`,"
					+ " `order`.`order_rank`,"
					+ " `order`.`order_delivery_date`,"
					+ " `order`.`order_drop_off_address`,"
					+ " `order`.`order_location_link`,"
					+ " `order`.`location_coordinates`, "
					+ " `order`.`location_plus_code`, "
					+ " `customer`.`customer_id`,"
					+ " `customer`.`customer_name`,"
					+ " `customer`.`customer_phone_no`,"
					+ " `order`.`company_id` "
					+ "FROM `delistar`.`order` ,`delistar`.`customer`"
					+ " where `order`.`driver_id` = ? and `order`.`order_delivery_date` = curdate()"
					+ " and `customer`.`customer_id` = `order`.`customer_id`"
					+ "  and order_state = ? order by `order`.`order_rank`desc;";


			PreparedStatement pst = DAO.conn.prepareStatement(query);
			pst.setInt(1, driver.getDriverId());
			pst.setString(2, orderState);

			ResultSet RS = pst.executeQuery();

			ArrayList<Order> DriverOrders = new ArrayList<Order>();

			while(RS.next()) {
				Order OrderItr = new Order();
				OrderItr.setOrderId(RS.getInt(1));
				OrderItr.setOrderNumber(RS.getInt(2));
				OrderItr.setOrderReport(RS.getString(3));
				OrderItr.setOrderState(RS.getString(4));
				OrderItr.setOrderRank(RS.getInt(5));
				OrderItr.setOrderDeliveryDate(LocalDate.parse(RS.getDate(6).toString()));
				OrderItr.setOrderDropOffAddress(RS.getString(7));
				OrderItr.setOrderLocationLink(RS.getString(8));
				OrderItr.setLocationCoordinates(RS.getString(9));
				OrderItr.setLocationPlusCode(RS.getString(10));
				
				System.out.println("ENTERED DAO GETF_DIVER_Orders");

				Customer orderCustomer = new Customer();
				orderCustomer.setCustomerId(RS.getInt(11));
				orderCustomer.setCustomerName(RS.getString(12));
				orderCustomer.setCustomerPhoneNo(RS.getString(13));
				OrderItr.setCustomer(orderCustomer);

				OrderItr.setCompanyId(RS.getInt(14));

				DriverOrders.add(OrderItr);
			}



			if(DriverOrders.size() == 0 || DriverOrders.get(0).getOrderId() == 0) {
				RS.close();
				pst.close();
				this.CloseConnection();

				return null;
			}

			RS.close();
			pst.close();
			this.CloseConnection();

			return DriverOrders;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return null;
	}

	public ArrayList<Order> GetDriverAllOrders(Driver driver){
		try { 
			this.OpenConnection();

			String query = "SELECT `order`.`order_id`,"
					+ " `order`.`order_number`,"
					+ " `order`.`order_report`,"
					+ " `order`.`order_state`,"
					+ " `order`.`order_rank`,"
					+ " `order`.`order_delivery_date`,"
					+ " `order`.`order_drop_off_address`,"
					+ " `order`.`order_location_link`,"
					+ " `order`.`location_coordinates`, "
					+ " `order`.`location_plus_code`, "
					+ " `customer`.`customer_id`,"
					+ " `customer`.`customer_name`,"
					+ " `customer`.`customer_phone_no`,"
					+ " `order`.`company_id` "
					+ "FROM `delistar`.`order` ,`delistar`.`customer`"
					+ " where `order`.`driver_id` = ? "
					+ " and `customer`.`customer_id` = `order`.`customer_id`"
					+ "  order by `order`.`order_rank`desc;";


			PreparedStatement pst = DAO.conn.prepareStatement(query);
			pst.setInt(1, driver.getDriverId());

			ResultSet RS = pst.executeQuery();

			ArrayList<Order> DriverOrders = new ArrayList<Order>();

			while(RS.next()) {
				Order OrderItr = new Order();
				OrderItr.setOrderId(RS.getInt(1));
				OrderItr.setOrderNumber(RS.getInt(2));
				OrderItr.setOrderReport(RS.getString(3));
				OrderItr.setOrderState(RS.getString(4));
				OrderItr.setOrderRank(RS.getInt(5));
				OrderItr.setOrderDeliveryDate(LocalDate.parse(RS.getDate(6).toString()));
				OrderItr.setOrderDropOffAddress(RS.getString(7));
				OrderItr.setOrderLocationLink(RS.getString(8));
				OrderItr.setLocationCoordinates(RS.getString(9));
				OrderItr.setLocationPlusCode(RS.getString(10));


				Customer orderCustomer = new Customer();
				orderCustomer.setCustomerId(RS.getInt(11));
				orderCustomer.setCustomerName(RS.getString(12));
				orderCustomer.setCustomerPhoneNo(RS.getString(13));
				OrderItr.setCustomer(orderCustomer);

				OrderItr.setCompanyId(RS.getInt(14));

				DriverOrders.add(OrderItr);
			}



			if(DriverOrders.size() == 0 || DriverOrders.get(0).getOrderId() == 0) {
				RS.close();
				pst.close();
				this.CloseConnection();

				return null;
			}

			RS.close();
			pst.close();
			this.CloseConnection();

			return DriverOrders;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return null;
	}
	public ArrayList<Order> GetCompanyOrders(Admin companyAdmin){
		try {


			String query = "SELECT `order`.`order_id`,"
					+ " `order`.`order_number`,"
					+ " `order`.`order_report`,"
					+ " `order`.`order_state`,"
					+ " `order`.`order_rank`,"
					+ " `order`.`order_delivery_date`,"
					+ " `order`.`order_drop_off_address`,"
					+ " `order`.`order_location_link`,"
					+ " `order`.`location_plus_code`,"
					+ " `customer`.`customer_id`,"
					+ " `customer`.`customer_name`,"
					+ " `customer`.`customer_phone_no`,"
					+ " `driver`.`driver_id`,"
					+ " `driver`.`driver_name`,"
					+ " `driver`.`driver_phone_no`,"
					+ " `driver`.`driver_email`,"
					+ " `driver`.`driver_gender`,"
					+ " `driver`.`isBanned`,"
					+ " `driver`.`bannedUntil` "
					+ "FROM `delistar`.`order` ,`delistar`.`customer`, `delistar`.`driver` where `order`.`company_id` = ? and `driver`.`driver_id` = `order`.`driver_id` and `customer`.`customer_id` = `order`.`customer_id`"
					+ " ";

			int companyId = this.GetCompany(companyAdmin, null).getCompanyId();
			System.out.println("GETCOMPANY_Orders____ companyId: " + companyId);

			this.OpenConnection();
			PreparedStatement pst = DAO.conn.prepareStatement(query);
			pst.setInt(1, companyId);



			ResultSet RS = pst.executeQuery();

			ArrayList<Order> CompanyOrders = new ArrayList<Order>();			
			while(RS.next()) {
				Order OrderItr = new Order();
				OrderItr.setOrderId(RS.getInt(1));
				OrderItr.setOrderNumber(RS.getInt(2));
				OrderItr.setOrderReport(RS.getString(3));
				OrderItr.setOrderState(RS.getString(4));
				OrderItr.setOrderRank(RS.getInt(5));
				OrderItr.setOrderDeliveryDate(LocalDate.parse(RS.getDate(6).toString()));
				OrderItr.setOrderDropOffAddress(RS.getString(7));
				OrderItr.setOrderLocationLink(RS.getString(8));
				OrderItr.setLocationPlusCode(RS.getString(9));

				Customer orderCustomer = new Customer();
				orderCustomer.setCustomerId(RS.getInt(10));
				orderCustomer.setCustomerName(RS.getString(11));
				orderCustomer.setCustomerPhoneNo(RS.getString(12));

				OrderItr.setCustomer(orderCustomer);


				Driver orderDriver = new Driver();
				orderDriver.setDriverId(RS.getInt(13));
				orderDriver.setDriverName(RS.getString(14));
				orderDriver.setDriverPhoneNo(RS.getString(15));
				orderDriver.setDriverEmail(RS.getString(16));
				orderDriver.setDriverGender(RS.getString(17));
				orderDriver.setIsBanned(RS.getBoolean(18));
				if(RS.getDate(19) == null) {
					orderDriver.setBannedUntil(null);
				}
				else {
					orderDriver.setBannedUntil(RS.getDate(19).toLocalDate());
				}
				System.out.println("driverIDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD: " + orderDriver.getDriverId());

				OrderItr.assignDriver(orderDriver);
				CompanyOrders.add(OrderItr);
			}



			if(CompanyOrders.size() == 0) {
				RS.close();
				pst.close();
				this.CloseConnection();
				return null;
			}


			RS.close();
			pst.close();
			this.CloseConnection();

			return CompanyOrders;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return null;
	}

	public boolean UpdateOrderRank(Order editedOrder) {
		try { 
			this.OpenConnection();

			String updateStatement = "UPDATE `delistar`.`order`"
					+ "SET `order_rank` = ?"
					+ " WHERE `order_number` = ?"
					+ " and `company_id` = ?;";

			PreparedStatement pst = DAO.conn.prepareStatement(updateStatement);
			pst.setInt(1, editedOrder.getOrderRank());
			pst.setInt(2, editedOrder.getOrderNumber());
			pst.setInt(3, editedOrder.getCompanyId());


			int NoOfAffectedRecords = pst.executeUpdate();


			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}

			DAO.conn.commit();


			pst.close();
			this.CloseConnection();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return false;
	}

	public boolean UpdateOrder(Order editedOrder) {
		try { 
			this.OpenConnection();

			String updateStatement = "UPDATE `delistar`.`order` "
					+ "SET `order_report` = ?, "
					+ "`order_state` = ?, "
					+ "`order_rank` = ?, "
					+ "`order_delivery_date` = ?, "
					+ "`order_drop_off_address` = ?, "
					+ "`order_location_link` = ?, "
					+ "`driver_id` = ?, "
					+"`location_coordinates` = ?,"
					+ "`location_plus_code` = ?,"
					+ "`customer_id` = ?"
					+ " WHERE `order_number` = ?"
					+ " and `company_id` = ?;";

			PreparedStatement pst = DAO.conn.prepareStatement(updateStatement);
			pst.setString(1, editedOrder.getOrderReport());
			pst.setString(2, editedOrder.getOrderState());
			pst.setInt(3, editedOrder.getOrderRank());
			pst.setString(4, editedOrder.getOrderDeliveryDate().toString());
			pst.setString(5, editedOrder.getOrderDropOffAddress());
			pst.setString(6, editedOrder.getOrderLocationLink());
			pst.setInt(7, editedOrder.getAssignedDriver().getDriverId());
			pst.setString(8, editedOrder.getLocationCoordinates());
			pst.setString(9, editedOrder.getLocationPlusCode());
			pst.setInt(10, editedOrder.getCustomer().getCustomerId());
			pst.setInt(11, editedOrder.getOrderNumber());
			pst.setInt(12, editedOrder.getCompanyId());


			int NoOfAffectedRecords = pst.executeUpdate();


			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}

			DAO.conn.commit();

			pst.close();
			this.CloseConnection();

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return false;
	}

	public boolean DeleteOrder(Order order) {
		try { 
			this.OpenConnection();

			String deleteStatement = "delete from `order` Where ";
			PreparedStatement pst;
			if(order.getOrderId() != 0) {
				deleteStatement+="`order_number` = ? and company_id = ?";
				pst = DAO.conn.prepareStatement(deleteStatement);
				pst.setInt(1, order.getOrderId());
				pst.setInt(2, order.getCompanyId());
			}
			else {
				deleteStatement += "order_number = ? and company_id = ?";
				pst = DAO.conn.prepareStatement(deleteStatement);
				pst.setInt(1, order.getOrderNumber());
				pst.setInt(2, order.getCompanyId());
			}

			int NoOfAffectedRecords = pst.executeUpdate();

			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}

			DAO.conn.commit();

			pst.close();
			this.CloseConnection();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return false;
	}

	public Order GetOrder(Order order) {
		try { 
			this.OpenConnection();

			String query = "select * from `order` where `order`.`order_number` = ? and `order`.`company_id` = ?"; 
			PreparedStatement pst;


			pst = DAO.conn.prepareStatement(query);
			pst.setInt(1, order.getOrderNumber());
			pst.setInt(2, order.getCompanyId());

			ResultSet RS = pst.executeQuery();

			Order fetchedOrder = new Order();
			while(RS.next()) {				
				fetchedOrder.setOrderId(RS.getInt(1));
				fetchedOrder.setOrderNumber(RS.getInt(2));
				fetchedOrder.setOrderReport(RS.getString(3));
				fetchedOrder.setOrderState(RS.getString(4));
				fetchedOrder.setOrderRank(RS.getInt(5));
				fetchedOrder.setOrderDeliveryDate(LocalDate.parse(RS.getDate(6).toString()));
				fetchedOrder.setOrderDropOffAddress(RS.getString(7));
				fetchedOrder.setOrderLocationLink(RS.getString(8));

				Customer orderCustomer = new Customer();
				orderCustomer.setCustomerId(RS.getInt(9));
				fetchedOrder.setCustomer(orderCustomer);

				Driver assignedDriver = new Driver();
				assignedDriver.setDriverId(RS.getInt(10));

				fetchedOrder.assignDriver(assignedDriver);

				fetchedOrder.setCompanyId(RS.getInt(11));

				fetchedOrder.setLocationCoordinates(RS.getString(12));
				fetchedOrder.setLocationPlusCode(RS.getString(13));
			}

			RS.close();
			pst.close();
			this.CloseConnection();

			return fetchedOrder;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return null;
	}

	public Company GetOrderCompany(Order order){
		try { 
			this.OpenConnection();

			String query = "select `order`.`company_id` from `order` where "; 
			query += "`order`.`order_id` = ? ;";


			PreparedStatement pst = DAO.conn.prepareStatement(query);
			pst.setInt(1, order.getOrderId());

			ResultSet SetOfRecords = pst.executeQuery();
			Company company = new Company();
			while(SetOfRecords.next()) {
				company.setCompanyId(SetOfRecords.getInt(1));
			}

			SetOfRecords.close();
			pst.close();
			this.CloseConnection();

			return company;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return null;
	}

	public boolean UpdateOrderState(Order order) {
		try { 
			this.OpenConnection();

			String updateStatement =  "UPDATE `order` SET "
					+ " order_report = ?, "
					+ " order_state = ?"
					+" WHERE order_number = ? and company_id = ? ;";


			PreparedStatement pst = DAO.conn.prepareStatement(updateStatement);
			pst.setString(1, order.getOrderReport());
			pst.setString(2, order.getOrderState());

			pst.setInt(3, order.getOrderNumber());
			pst.setInt(4, order.getCompanyId());


			int NoOfAffectedRecords = pst.executeUpdate();

			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}

			DAO.conn.commit();

			pst.close();
			this.CloseConnection();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return false;
	}

	public boolean AddCompany(Company newCompany) {
		try {
			this.OpenConnection();

			String insertStatement = "INSERT INTO `delistar`.`company`"
					+ "(`company_name`,"
					+ "`company_email`,"
					+ "`company_phone_no`)"

				+ "VALUES"
				+ "(?, "
				+ "?, "
				+ "?)";

			PreparedStatement pst = DAO.conn.prepareStatement(insertStatement);
			pst.setString(1, newCompany.getCompanyName());
			pst.setString(2, newCompany.getCompanyEmail());
			pst.setString(3, newCompany.getCompanyPhoneNo());

			int NoOfAffectedRecords = pst.executeUpdate();

			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}

			System.out.println("comapny addEDDDDD");
			DAO.conn.commit();
			pst.close();
			this.CloseConnection();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return false;
	}

	public Company GetCompany(Admin admin, Company company) {
		try {
			this.OpenConnection();
			
			String getCompanyQuery;
			PreparedStatement pst;
			if(admin != null) {
				getCompanyQuery = "select * from `company` where `company`.`company_id` = "
						+ "(select `admin`.`company_id` from `admin` "
						+ "where `admin`.`admin_id` = ?);";

				System.out.println("GEtCompany for ADmin :::: adminId = " + admin.getAdminId());
				pst = DAO.conn.prepareStatement(getCompanyQuery);
				pst.setInt(1, admin.getAdminId());
			}

			else {
				getCompanyQuery = "select * from `company` where `company`.`company_email` = ? and company_phone_no = ?;";
				pst = DAO.conn.prepareStatement(getCompanyQuery);
				pst.setString(1, company.getCompanyEmail());
				pst.setString(2, company.getCompanyPhoneNo());
			}


			ResultSet RS = pst.executeQuery();

			Company requestedCompany = new Company();			
			while(RS.next()) {
				requestedCompany.setCompanyId(RS.getInt(1));
				requestedCompany.setCompanyName(RS.getString(2));
				requestedCompany.setCompanyEmail(RS.getString(3));
				requestedCompany.setCompanyPhoneNo(RS.getString(4));
			}

			RS.close();
			pst.close();
			this.CloseConnection();

			return requestedCompany;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return null;
	}

	public boolean AddCustomer(Customer customer) {
		// TODO Auto-generated method stub
		try { 
			this.OpenConnection();

			String insertStatement = "INSERT INTO `delistar`.`customer`"
					+ "(`customer_name`,"
					+ "`customer_phone_no`)"
					+ "VALUES"
					+ "(?,"
					+ "?);";

			PreparedStatement pst = DAO.conn.prepareStatement(insertStatement);
			pst.setString(1, customer.getCustomerName());
			pst.setString(2, customer.getCustomerPhoneNo());

			int NoOfAffectedRecords = pst.executeUpdate();
			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}

			DAO.conn.commit();

			pst.close();

			this.CloseConnection();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return false;
	}

	public Customer GetCustomer(Customer requestedCustomer) {
		try { 
			this.OpenConnection();

			String query = "select * from `customer` where `customer`.";
			PreparedStatement pst;

			if(requestedCustomer.getCustomerId() != 0) {
				query += "`customer_id` = ?";
				pst = DAO.conn.prepareStatement(query);
				pst.setInt(1, requestedCustomer.getCustomerId());
			}
			else{
				query += "`customer_phone_no` = ?;";
				pst = DAO.conn.prepareStatement(query);
				pst.setString(1, requestedCustomer.getCustomerPhoneNo());
			}


			ResultSet RS = pst.executeQuery();
			Customer customer = new Customer();
			while(RS.next()) {				
				customer.setCustomerId(RS.getInt(1));
				customer.setCustomerName(RS.getString(2));
				customer.setCustomerPhoneNo(RS.getString(3));
			}

			RS.close();
			pst.close();
			this.CloseConnection();

			return customer;		
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return null;
	}

	public boolean UpdateCustomer(Customer editedCustomer) {
		// TODO Auto-generated method stub
		try { 
			this.OpenConnection();

			String updateStatement = "UPDATE `delistar`.`customer`"
					+ "SET"
					+ "`customer_name` = ?, "
					+ "`customer_phone_no` = ?";

			PreparedStatement pst;

			if(editedCustomer.getCustomerId() != 0 ) {
				updateStatement += " WHERE `customer_id` = ? ;";
				pst = DAO.conn.prepareStatement(updateStatement);
				pst.setString(1, editedCustomer.getCustomerName());
				pst.setString(2, editedCustomer.getCustomerPhoneNo());
				pst.setInt(3, editedCustomer.getCustomerId());
			}
			else {
				updateStatement +=" WHERE `customer_phone_no` = ?;";
				pst = DAO.conn.prepareStatement(updateStatement);
				pst.setString(1, editedCustomer.getCustomerName());
				pst.setString(2, editedCustomer.getCustomerPhoneNo());
				pst.setString(3, editedCustomer.getCustomerPhoneNo());
			}


			int NoOfAffectedRecords = pst.executeUpdate();

			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}

			DAO.conn.commit();				
			pst.close();

			this.CloseConnection();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


		this.CloseConnection();
		return false;
	}


	public boolean AddNotification(Notification notification) {
		try { 
			this.OpenConnection();

			String insertStatement = "INSERT INTO `delistar`.`drivernotifications`"
					+ "(`driver_id`,"
					+ "`notification_message`)"
					+ "VALUES"
					+ "(?, "
					+ "?);";
			PreparedStatement pst = DAO.conn.prepareStatement(insertStatement);
			pst.setInt(1, notification.getDriverId());
			pst.setString(2, notification.getMessage());

			int NoOfAffectedRecords = pst.executeUpdate();
			if(NoOfAffectedRecords == 0) {
				DAO.conn.rollback();

				pst.close();
				this.CloseConnection();
				return false;
			}

			DAO.conn.commit();

			pst.close();
			this.CloseConnection();

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return false;
	}

	public ArrayList<Notification> GetNotifications(Driver driver){
		try { 
			this.OpenConnection();
			String query = "select * from `drivernotifications` where `driver_id` = ?";

			PreparedStatement pst = DAO.conn.prepareStatement(query);
			pst.setInt(1, driver.getDriverId());

			ArrayList<Notification> driverNotifications = new ArrayList<Notification>();
			ResultSet RS = pst.executeQuery();			
			while(RS.next()) {
				Notification notificationIterator = new Notification();

				notificationIterator.setMessage(RS.getString(3));
				driverNotifications.add(notificationIterator);
			}
			if(driverNotifications.size() == 0) {
				RS.close();
				pst.close();
				this.CloseConnection();

				return null;
			}

			RS.close();
			pst.close();
			this.CloseConnection();

			return driverNotifications;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();
		return null;
	}


	public boolean DeleteCompany(Company newCompany) {
		try {
			this.OpenConnection();

			String deleteStatement = "DELETE FROM `delistar`.`company` WHERE company_id = ?;";

			PreparedStatement pst = DAO.conn.prepareStatement(deleteStatement);
			pst.setInt(1, newCompany.getCompanyId());

			int noOfAffectedRows = pst.executeUpdate();
			if(noOfAffectedRows == 0) {
				DAO.conn.rollback();
				pst.close();
				this.CloseConnection();
				return false;
			}


			DAO.conn.commit();
			pst.close();
			this.CloseConnection();

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		this.CloseConnection();

		return false;

	}
	public boolean DeleteCustomer(Customer customer) {
		try {
			this.OpenConnection();
			
			String deleteStatement = "DELETE FROM `delistar`.`customer` WHERE customer_id = ? ;";

			PreparedStatement pst = DAO.conn.prepareStatement(deleteStatement);
			pst.setInt(1, customer.getCustomerId());

			int noOfAffectedRows = pst.executeUpdate();
			if(noOfAffectedRows == 0) {
				DAO.conn.rollback();
				pst.close();
				this.CloseConnection();

				return false;
			}

			DAO.conn.commit();
			pst.close();

			this.CloseConnection();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return false;

	}
	
	public boolean DeleteNotifications(Driver driver) {
		try {
			this.OpenConnection();
			
			String deleteStatement = "DELETE FROM `delistar`.`drivernotifications` WHERE driver_id = ? ;";

			PreparedStatement pst = DAO.conn.prepareStatement(deleteStatement);
			pst.setInt(1, driver.getDriverId());

			int noOfAffectedRows = pst.executeUpdate();
			if(noOfAffectedRows == 0) {
				DAO.conn.rollback();
				pst.close();
				this.CloseConnection();

				return false;
			}

			DAO.conn.commit();
			pst.close();

			this.CloseConnection();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return false;

	}
	public void CloseConnection() {
		try {

			if(DAO.conn != null && !DAO.conn.isClosed()) {
				DAO.conn.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

}
