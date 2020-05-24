package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.Instant;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;


/**
 * This class provides the methods that set up the parking tickets prices, 
 * according to the parking duration and the vehicle type. 
 * @author newbie
 *
 */
public class FareCalculatorService {
	/**
	 * Called when the vehicle is exiting, this method set up the ticket price 
	 * according to the vehicle type fare and the parking duration, 
	 * including the free 30 first minutes.
	 * Uses minutes as units and the java.time api instead of the nearly deprecated java.date one.
	 * @see {@link com.parkit.parkingsystem.service.ParkingService.processExitingVehicle} 
	 * @param a {@link Ticket} instance
	 * @throws IllegalArgumentException
	 */
	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		Instant dIn = ticket.getInTime();
		Instant dOut = ticket.getOutTime();

		long duration = Duration.between(dIn, dOut).toMinutes();

		if (duration <= 30) {
			ticket.setPrice(0);
		} else {
			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				ticket.setPrice((duration * (Fare.CAR_RATE_PER_HOUR / 60)) - (Fare.CAR_RATE_PER_HOUR / 2));
				break;
			}
			case BIKE: {
				ticket.setPrice((duration * (Fare.BIKE_RATE_PER_HOUR / 60)) - (Fare.BIKE_RATE_PER_HOUR / 2));
				break;
			}
			default:
				throw new IllegalArgumentException("Unknown Parking Type");
			}
		}
	}

	/**
	 * This method applies a reduced fare to the ticket price.<br/>
	 * Called if the {@link com.parkit.parkingsystem.TicketDAO.checkRegularUser} method
	 * returns true.
	 * @see {@link com.parkit.parkingsystem.service.ParkingService.processExitingVehicle} 
	 * @param a {@link Ticket} instance.
	 * @throws IllegalArgumentException
	 */
	public void calculateRegularUsersReducedFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		Instant dIn = ticket.getInTime();
		Instant dOut = ticket.getOutTime();

		long duration = Duration.between(dIn, dOut).toMinutes();

		if (duration <= 30) {
			ticket.setPrice(0);
		} else {
			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				ticket.setPrice((duration * (Fare.CAR_RATE_PER_HOUR / 60) - (Fare.CAR_RATE_PER_HOUR / 2))
						* Fare.REGULAR_USER_DISCOUNT_RATE);
				break;
			}
			case BIKE: {
				ticket.setPrice((duration * (Fare.BIKE_RATE_PER_HOUR / 60) - (Fare.BIKE_RATE_PER_HOUR / 2))
						* Fare.REGULAR_USER_DISCOUNT_RATE);
				break;
			}
			default:
				throw new IllegalArgumentException("Unknown Parking Type");
			}
		}
	}
}