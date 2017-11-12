package com.movie;

public class SeatRow {
	
	private String rowName;
	private Seat[] seats;
	private Integer availableCount;
	
	public SeatRow(String rowName, Integer noOfSeats){
		this.rowName = rowName;
		intializeSeats(noOfSeats);
	}
	
	public String getRowName() {
		return rowName;
	}
	public void setRowName(String rowName) {
		this.rowName = rowName;
	}
	public Seat[] getSeats() {
		return seats;
	}
	public void setSeats(Seat[] seats) {
		this.seats = seats;
	}
	public Integer getAvailableCount() {
		return availableCount;
	}
	public void setAvailableCount(Integer availableCount) {
		this.availableCount = availableCount;
	}
	
	/*
	 * Initializes the seat row with predefined seats based on the config file
	 */
	private void intializeSeats(Integer noOfSeats){
		
		seats = new Seat[noOfSeats];
		for( int i=0; i< noOfSeats; i++ ){
			seats[i] = new Seat();
		}
		this.availableCount = noOfSeats;
	}
	
	
	/*
	 * This method is used for booking seats in a row
	 */
	public String bookSeats(String reservationNo, int noOfSeats){
		
		Integer totalSeats = new Integer(Config.getInstance().getProperty("no_of_seats").trim());
		StringBuffer seatNos = new StringBuffer();
		
		int pointer = totalSeats-availableCount;
		
		//This marks the seat as booked and allocates the reservation no. 
		for( int i=pointer; i< (pointer+noOfSeats); i++ ){
			if ( !seats[i].isBooked() ){
				seats[i].setBooked(true);
				seats[i].setReservationNumber(reservationNo);
				seatNos.append(this.rowName + (i+1) + ",");
			}
		}
		
		availableCount = availableCount - noOfSeats;
		return seatNos.length() > 2 ? seatNos.toString().substring(0, seatNos.length()-1) : seatNos.toString();	
	}
	
}
