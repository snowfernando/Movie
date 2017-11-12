package com.movie.test;

import org.junit.Test;

import com.movie.Reservation;

import junit.framework.Assert;

public class MovieTest {

	@Test
	public void testSimpleReservation() {
		Reservation reservation = new Reservation();

		String result = reservation.bookSeats("R1000", 2);

		Assert.assertTrue(result.contains("R1000 A1,A2"));

	}
	
	@Test
	public void testReservationWithMultipleRows() {
		
		Reservation reservation = new Reservation();

		String result = reservation.bookSeats("R1000", 22);

		Assert.assertTrue(result.contains("B1,B2"));

	}
	
	
	@Test
	public void testReservationWithNoAvailability() {
		
		Reservation reservation = new Reservation();

		String result = reservation.bookSeats("R1000", 201);

		Assert.assertTrue(result.contains("R1000 No seats available"));

	}
	
	
}