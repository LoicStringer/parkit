package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

@Tag("FareCalculationTests")
@DisplayName("Calculate fares according to several situations")
public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Nested
	@Tag("ExceptionsTests")
	@DisplayName("Exceptions Checking")
	class ExceptionsTests {

	
		@Test
		@DisplayName("When no vehicle type has been registered")
		public void calculateUnknownTypeFare() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
			
		}

		@Test
		@DisplayName("When the registered In Time is after the Out Time, car case")
		public void calculateCarFareWithFutureInTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When the registered In Time is after the Out Time, bike case")
		public void calculateBikeFareWithFutureInTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When no In Time has been registered, car case")
		public void calculateCarFareWithNoInTime() {
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(null);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When no In Time has been registered, bike case")
		public void calculateBikeFareWithNoInTime() {
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(null);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When no Out Time has been registered, car case")
		public void calculateCarFareWithNoOutTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(null);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When no Out Time has been registered, bike case")
		public void calculateBikeFareWithNoOutTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(null);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When no Parking Spot has been registered, car case")
		public void calculateCarFareWithNoParkingSpot() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
			Date outTime = new Date();

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(null);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When no Parking Spot has been registered, bike case")
		public void calculateBikeFareWithNoParkingSpot() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
			Date outTime = new Date();

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(null);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
		}
	}

	@Nested
	@Tag("UsualFaresTests")
	@DisplayName("Usual fares calculation")
	class UsualFaresTests {

		@Test
		@DisplayName("Calculate car fare for a one hour parking time")
		public void calculateCarFareForAnHourParkingTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);

			assertEquals((Fare.CAR_RATE_PER_HOUR - (Fare.CAR_RATE_PER_HOUR / 2)), ticket.getPrice());
		}

		@Test
		@DisplayName("Calculate bike fare for a one hour parking time")
		public void calculateBikeFareForAnHourParkingTime() {

			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);

			assertEquals((Fare.BIKE_RATE_PER_HOUR - (Fare.BIKE_RATE_PER_HOUR / 2)), ticket.getPrice());
		}

		@Test
		@DisplayName("Calculate car fare for a less than one hour parking time")
		public void calculateCarFareForLessThanOneHourParkingTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
																			// parking fare
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals(((0.75 * Fare.CAR_RATE_PER_HOUR) - (Fare.CAR_RATE_PER_HOUR / 2)), ticket.getPrice());
		}

		@Test
		@DisplayName("Calculate bike fare for a less than one hour parking time")
		public void calculateBikeFareForLessThanOneHourParkingTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
																			// parking fare
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals(((0.75 * Fare.BIKE_RATE_PER_HOUR) - (Fare.BIKE_RATE_PER_HOUR / 2)), ticket.getPrice());
		}

		@Test
		@DisplayName("Calculate car fare for a less than half an hour parking time")
		public void calculateCarFareForLessThanHalfAnHourParkingTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (29 * 60 * 1000));
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals(0, ticket.getPrice());
		}

		@Test
		@DisplayName("Calculate bike fare for a less than half an hour parking time")
		public void calculateBikeFareForLessThanHalfAnHourParkingTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (29 * 60 * 1000));
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals(0, ticket.getPrice());
		}

		@Test
		@DisplayName("Calculate car fare for a one day parking time ")
		public void calculateFareForAOneDayCarParkingTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																				// parking fare per hour
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals(((24 * Fare.CAR_RATE_PER_HOUR) - (Fare.CAR_RATE_PER_HOUR / 2)), ticket.getPrice());
		}

		@Test
		@DisplayName("Calculate bike fare for a one day parking time ")
		public void calculateFareForAOneDayBikeParkingTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																				// parking fare per hour
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals(((24 * Fare.BIKE_RATE_PER_HOUR) - (Fare.BIKE_RATE_PER_HOUR / 2)), ticket.getPrice());
		}

		@Test
		@DisplayName("Calculate car fare for a less than a second parking time")
		public void calculateFareForALessThanASecondCarParkinTime() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - 999);
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);

			assertEquals(0, ticket.getPrice());
		}
	}

	@Nested
	@Tag("RegularUsersFaresTests")
	@DisplayName("Regular Users fares calculation")
	class RegularUsersFaresTests {
		
		@Test
		@DisplayName("Regular user car fare calculation for an hour parking time")
		public void calculateCarFareWithRegularUserDiscountForAnHour() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateRegularUsersReducedFare(ticket);

			assertEquals(((Fare.CAR_RATE_PER_HOUR - (Fare.CAR_RATE_PER_HOUR / 2)) * Fare.REGULAR_USER_DISCOUNT_RATE),
					ticket.getPrice());
		}
		
		@Test
		@DisplayName("Regular user bike fare calculation for an hour parking time")
		public void calculateBikeFareWithRegularUserDiscountForAnHour() {
			Date inTime = new Date();
			inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateRegularUsersReducedFare(ticket);

			assertEquals(((Fare.BIKE_RATE_PER_HOUR - (Fare.BIKE_RATE_PER_HOUR / 2)) * Fare.REGULAR_USER_DISCOUNT_RATE),
					ticket.getPrice());

		}
	}
}
