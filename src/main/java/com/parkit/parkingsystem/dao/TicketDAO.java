package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;

public class TicketDAO {

	private static final Logger logger = LogManager.getLogger("TicketDAO");

	//Set up the database access
	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	/**
	 * 
	 * Called when the vehicle is incoming the parking site.<br/>
	 * Register required ticket data into the database:<br/>
	 * Auto-increment ID,PARKING_NUMBER,VEHICLE_REG_NUMBER,PRICE,IN_TIME and OUT_TIME.<br/>
	 * Using the try-with-resources statement which automatically closes resources.
	 * Exceptions are handled globally.
	 * 
	 * @param a {@link Ticket} instance.
	 * @return true if succeed.
	 * @throws SQLException.
	 * @see com.parkit.parkingsystem.service.ParkingService
	 */
	public boolean saveTicket(Ticket ticket) {
		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.SAVE_TICKET);
			try (ps) {
				ps.setInt(1, ticket.getParkingSpot().getId());
				ps.setString(2, ticket.getVehicleRegNumber());
				ps.setDouble(3, ticket.getPrice());
				ps.setTimestamp(4, java.sql.Timestamp.from(ticket.getInTime()));
				ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : java.sql.Timestamp.from(ticket.getOutTime()));
				
				return ps.execute();
			}
		}catch (SQLException ex) {
			logger.error("Error fetching next available slot", ex);
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}
		return false;
	}

	
	/**
	 * Called when the vehicle is exiting the parking site.<br/>
	 * Display the required data selected from the database:<br/>
	 * Auto-increment ID,PARKING_NUMBER,VEHICLE_REG_NUMBER,PRICE,IN_TIME and OUT_TIME.<br/>
	 * Using the try-with-resources statement which automatically closes resources.
	 * Exceptions are handled globally.
	 * 
	 * @param the exiting vehicle's vehicleRegNumber as a {@link String}
	 * @return a {@link Ticket} instance.
	 * @throws SQLException.
	 * @see com.parkit.parkingsystem.service.ParkingService
	 */
	public Ticket getTicket(String vehicleRegNumber) {
		Connection con = null;
		Ticket ticket = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
			try (ps) {
				ps.setString(1, vehicleRegNumber);
				ResultSet rs = ps.executeQuery();
				try (rs) {
					if (rs.next()) {
						ticket = new Ticket();
						ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)),
								false);
						ticket.setParkingSpot(parkingSpot);
						ticket.setId(rs.getInt(2));
						ticket.setVehicleRegNumber(vehicleRegNumber);
						ticket.setPrice(rs.getDouble(3));
						ticket.setInTime(rs.getTimestamp(4).toInstant().truncatedTo(ChronoUnit.SECONDS));
						ticket.setOutTime(rs.getTimestamp(5)== null ? null : rs.getTimestamp(5).toInstant().truncatedTo(ChronoUnit.SECONDS));
					}
				}
			}
		} catch ( SQLException ex ) {
			logger.error("Error fetching next available slot", ex);
		} catch ( Exception ex){
			logger.error("Error fetching next available slot", ex);
		}
		finally {
			dataBaseConfig.closeConnection(con);
		}
		return ticket;
	}
	
	/**
	 * Called when the vehicle is exiting the parking site.<br/>
	 * Updating required ticket data in the database:<br/>
	 * PRICE, OUT_TIME and ID.
	 * Using the try-with-resources statement which automatically closes resources.
	 * Exceptions are handled globally.
	 * @param a {@link Ticket} instance.
	 * @return true if succeed.
	 * @throws SQLException.
	 * @see {@link com.parkit.parkingsystem.service.ParkingService}.
	 */
	public boolean updateTicket(Ticket ticket) {
		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
			try (ps) {
				ps.setDouble(1, ticket.getPrice());
				ps.setTimestamp(2, java.sql.Timestamp.from(ticket.getOutTime()));
				ps.setInt(3, ticket.getId());
				ps.execute();
				return true;
			}
		}catch (SQLException ex) {
			logger.error("Error saving ticket info", ex);
		} catch (Exception ex) {
			logger.error("Error saving ticket info", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}
		return false;
	}

	/**
	 * Called when the vehicle is exiting the parking site.<br/>
	 * Checking if the vehicle has already been registered before.
	 * Using the try-with-resources statement which automatically closes resources.
	 * Exceptions are handled globally.
	 * 
	 * @param the exiting vehicle's vehicleRegNumber as a {@link String}
	 * @return true if the vehicleRegNumber is found more than once in the database.
	 * @throws SQLException.
	 * @see {@link com.parkit.parkingsystem.service.ParkingService}
	 */
	
	public boolean checkRegularUser(String vehicleRegNumber) {
		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			CallableStatement cs = con.prepareCall(DBConstants.CHECK_REGULAR_USER);
			try (cs) {
				cs.setString(1, vehicleRegNumber);
				cs.registerOutParameter(2, java.sql.Types.INTEGER);
				cs.execute();

				int isRegular = cs.getInt(2);

				if (isRegular > 1)
					return true;
			}
		}catch (SQLException ex) {
			logger.error("Error looking for ticket info", ex);
		} catch (Exception ex) {
			logger.error("Error looking for ticket info", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}
		return false;
	}

}
