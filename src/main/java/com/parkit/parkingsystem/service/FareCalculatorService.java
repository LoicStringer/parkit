package com.parkit.parkingsystem.service;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	
	
	public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

   		Instant instantIn = ticket.getInTime().toInstant();
   		Instant instantOut = ticket.getOutTime().toInstant();
       

        
   		long duration = Duration.between(instantIn, instantOut).toMinutes();

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * (Fare.CAR_RATE_PER_HOUR/60));
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * (Fare.BIKE_RATE_PER_HOUR/60));
                break;
            }
            default: throw new IllegalArgumentException("Unknown Parking Type");
        }
    }

	public void calculateRegularUsersReducedFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		Instant dIn = ticket.getInTime().toInstant();
		Instant dOut = ticket.getOutTime().toInstant();

		long duration = Duration.between(dIn, dOut).toMinutes();

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			ticket.setPrice((duration * (Fare.CAR_RATE_PER_HOUR / 60)) * Fare.REGULAR_USER_DISCOUNT_RATE);
			break;
		}
		case BIKE: {
			ticket.setPrice((duration * (Fare.BIKE_RATE_PER_HOUR / 60)) * Fare.REGULAR_USER_DISCOUNT_RATE);
			break;
		}
		default:
			throw new IllegalArgumentException("Unknown Parking Type");
		}

	}
}