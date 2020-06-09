package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingSpotDAO {
	private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

	//Set up the database access
	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	/**
	 * Query the next available parking spot number from the database. 
	 * Using the try-with-resources statements which automatically close resources.
	 * Exceptions are handled individually.
	 * 
	 * @param a {@link com.parkit.parkingsystem.constants.ParkingType } instance according to the vehicle type.
	 * @return an int, result of the database query. 
	 * @throws SQLException.
	 */
	public int getNextAvailableSlot(ParkingType parkingType) {
		Connection con = null;
		int result = -1;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
			try (ps) {
				ps.setString(1, parkingType.toString());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						result = rs.getInt(1);
					}
				}catch(SQLException ex) {
					logger.error("Error fetching next available slot", ex);
				}
			}catch (SQLException ex) {
				logger.error("Error fetching next available slot", ex);
			}
		} catch (Exception ex) {
			logger.error("Error fetching next available slot", ex);
		} finally {
			dataBaseConfig.closeConnection(con);
		}
		return result;
	}

	/**
	 * Update the availability for that parking slot. 
	 * Using the try-with-resources statement which automatically closes resources.
	 * Exceptions are handled individually.
	 * 
	 * @param a {@link com.parkinit.parkingsystem.model.ParkingSpot} instance.
	 * @return true if succeed. 
	 * @throws SQLException.
	 */
	public boolean updateParking(ParkingSpot parkingSpot) {
		Connection con = null;
		try {
			con = dataBaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
			try (ps) {
				ps.setBoolean(1, parkingSpot.isAvailable());
				ps.setInt(2, parkingSpot.getId());
				int updateRowCount = ps.executeUpdate();
				return (updateRowCount == 1);
			}catch(SQLException ex) {
				logger.error("Error updating parking info", ex);
				return false;
			}
		} catch (Exception ex) {
			logger.error("Error updating parking info", ex);
			return false;
		} finally {
			dataBaseConfig.closeConnection(con);
		}
	}
}
