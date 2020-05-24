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


import java.time.Instant;
import java.time.temporal.ChronoUnit;


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
			Instant inTime = Instant.now().minusMillis((60 * 60 * 1000));
			
			Instant outTime = Instant.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
			
		}

		@Test
		@DisplayName("When the registered In Time is after the Out Time, car case")
		public void calculateCarFareWithFutureInTime() {
			Instant inTime = Instant.now().plusMillis((60 * 60 * 1000));
			
			Instant outTime = Instant.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When the registered In Time is after the Out Time, bike case")
		public void calculateBikeFareWithFutureInTime() {
			Instant inTime = Instant.now().plusMillis((60 * 60 * 1000));
			
			Instant outTime = Instant.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When no In Time has been registered, car case")
		public void calculateCarFareWithNoInTime() {
			Instant outTime = Instant.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(null);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When no In Time has been registered, bike case")
		public void calculateBikeFareWithNoInTime() {
			Instant outTime = Instant.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(null);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When no Out Time has been registered, car case")
		public void calculateCarFareWithNoOutTime() {
			Instant inTime = Instant.now().minusMillis((60 * 60 * 1000));
			
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(null);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When no Out Time has been registered, bike case")
		public void calculateBikeFareWithNoOutTime() {
			Instant inTime = Instant.now().minusMillis((60 * 60 * 1000));
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(null);
			ticket.setParkingSpot(parkingSpot);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When no Parking Spot has been registered, car case")
		public void calculateCarFareWithNoParkingSpot() {
			Instant inTime = Instant.now().minusMillis((60 * 60 * 1000));
			Instant outTime = Instant.now();

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(null);
			assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
		}

		@Test
		@DisplayName("When no Parking Spot has been registered, bike case")
		public void calculateBikeFareWithNoParkingSpot() {
			Instant inTime = Instant.now().minusMillis((60 * 60 * 1000));
			Instant outTime = Instant.now();


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
			Instant inTime = Instant.now().minusMillis((60 * 60 * 1000));
			Instant outTime = Instant.now();

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

			Instant inTime = Instant.now().minusMillis((60 * 60 * 1000));
			Instant outTime = Instant.now();

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
			Instant inTime = Instant.now().minusMillis((45 * 60 * 1000));// 45 minutes parking time should give 3/4th
																			// parking fare
			Instant outTime = Instant.now();
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
			Instant inTime = Instant.now().minusMillis((45 * 60 * 1000));// 45 minutes parking time should give 3/4th
																			// parking fare
			Instant outTime = Instant.now();
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
			Instant inTime = Instant.now().minusMillis((29 * 60 * 1000));
			Instant outTime = Instant.now();
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
			Instant inTime = Instant.now().minusMillis((29 * 60 * 1000));
			Instant outTime = Instant.now();
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
			Instant inTime = Instant.now().minusMillis((24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																				// parking fare per hour
			Instant outTime = Instant.now();
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
			Instant inTime = Instant.now().minusMillis((24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
																				// parking fare per hour
			Instant outTime = Instant.now();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateFare(ticket);
			assertEquals(((24 * Fare.BIKE_RATE_PER_HOUR) - (Fare.BIKE_RATE_PER_HOUR / 2)), ticket.getPrice());
		}

		@Test
		@DisplayName("Calculate car fare for a less than a second parking time")
		public void calculateFareForALessThanASecondCarParkingTime() {
			Instant inTime = Instant.now().minusMillis((999));
			Instant outTime = Instant.now();
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
			Instant inTime = Instant.now().minusMillis((60 * 60 * 1000));
			Instant outTime = Instant.now();
			
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
			Instant inTime = Instant.now().minusMillis((60 * 60 * 1000));
			Instant outTime = Instant.now();
			
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			fareCalculatorService.calculateRegularUsersReducedFare(ticket);

			assertEquals(((Fare.BIKE_RATE_PER_HOUR - (Fare.BIKE_RATE_PER_HOUR / 2)) * Fare.REGULAR_USER_DISCOUNT_RATE),
					ticket.getPrice());
			System.out.println(Instant.now().truncatedTo(ChronoUnit.SECONDS));
		}
	}
}
