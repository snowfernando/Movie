package com.movie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Reservation {

	private SeatRow[] rows;
	private int totalSeatsAvailable = 0;
	
	public Reservation(){
		
		init();
	}
	
	/*
	 * Initializing the system with rows based on the config param
	 * 
	 */
	private void init(){
		
		String seatsPerRow = Config.getInstance().getProperty("no_of_seats").trim();
		
		rows = new SeatRow[new Integer(Config.getInstance().getProperty("no_of_rows")).intValue()];
		
		for( int row = 0 ; row < rows.length; row++ ){
			rows[row] = new SeatRow("" + (char) (row + 65),  new Integer(seatsPerRow));
			totalSeatsAvailable += new Integer(seatsPerRow).intValue();
		}
	}
	
	//Reserve the seats based on the available rows
	public String bookSeats(String reservationNo, int seats){	
		
		String bookingResult = reservationNo + " No seats available";
		if ( areSeatsAvailable(seats) ) {
			for( SeatRow row : rows){
				if ( row.getAvailableCount() >= seats ){
					totalSeatsAvailable = totalSeatsAvailable - seats;
					return reservationNo + " " + row.bookSeats(reservationNo, seats);
				}
			}
		}else{
			if (totalSeatsAvailable >= seats ){
				boolean allocationMode = Boolean.valueOf(Config.getInstance().getProperty("allocation_based_on_max_seats_in_a_row").trim());
				
				// Allocates seats based on the availability. 
				// If the allocation mode is false it will assign seats based on the seats from rows A-J
				// Else it will assign seats in rows with maximum seats available.
				if ( !allocationMode ){
					bookingResult = reservationNo + " " + getAvailableSeats(reservationNo, seats);
				}else{
					bookingResult = reservationNo + " " + getAvailableSeatsBasedOnMaxSeats(reservationNo, seats);
				}
				bookingResult = bookingResult.substring(0, bookingResult.length()-1);
			}
			return bookingResult;
		}
		return bookingResult;
	}
	
	/*
	 * Will assign seats based on availability. 
	 * Since there are no seats available in the same row. 
	 * Preference would be given from Row A through Row J
	 * 
	 */
	public String getAvailableSeats(String reservationNo, int seatsToBeBooked){
		
		int seatsBooked = 0;
		StringBuffer result = new StringBuffer();
		
		//Iterate through each seat row and assign seats based on the request. 
		for( SeatRow row : rows){
			
			if( seatsBooked != seatsToBeBooked && row.getAvailableCount() > 0 ){
				
				int rowSeatsAvailable = row.getAvailableCount();
				
				if ( (seatsToBeBooked-seatsBooked) >= rowSeatsAvailable ){
			
					result.append(row.bookSeats(reservationNo, rowSeatsAvailable) + ",");
					seatsBooked += rowSeatsAvailable;
					
				}else{
					
					result.append( row.bookSeats(reservationNo, (seatsToBeBooked-seatsBooked) ) + "," );
					seatsBooked += (seatsToBeBooked-seatsBooked);
					
				}
				
			}
		
		}
		totalSeatsAvailable = totalSeatsAvailable - seatsBooked;
		
		return result.toString();
	}
	
	
	/*
	 * Will assign seats based on availability. 
	 * Since there are no seats available in the same row. 
	 * Preference would be given based on max seats available in a row
	 * 
	 */
	public String getAvailableSeatsBasedOnMaxSeats(String reservationNo, int seatsToBeBooked){
		
		int seatsBooked = 0;
		StringBuffer result = new StringBuffer();
		
		//Book seats from available rows 
		while ( seatsBooked < seatsToBeBooked ){
			
			//Getting the seat row with maximum seats availability
			SeatRow maxRow = getMaxRow();
			
			int rowSeatsAvailable = maxRow.getAvailableCount();
			
			if ( (seatsToBeBooked-seatsBooked) >= rowSeatsAvailable ){
				
				result.append(maxRow.bookSeats(reservationNo, rowSeatsAvailable) + ",");
				seatsBooked += rowSeatsAvailable;
				
			}else{
				
				result.append( maxRow.bookSeats(reservationNo, (seatsToBeBooked-seatsBooked) ) + "," );
				seatsBooked += (seatsToBeBooked-seatsBooked);
				
			}
			
		}
		
		totalSeatsAvailable = totalSeatsAvailable - seatsBooked;
		
		return result.toString();
	}
	
	/*
	 * Fetches the seat row with max number of seats. 
	 */
	private SeatRow getMaxRow(){
		SeatRow maxRow = Collections.max(Arrays.asList(rows), new Comparator<SeatRow>() {

			@Override
			public int compare(SeatRow o1, SeatRow o2) {
			
			return o1.getAvailableCount().compareTo(o2.getAvailableCount());
			}           
			});
		return maxRow;
	}
	
	
	//Check if all seats are available in a single row - for better customer satisfaction. 
	private boolean areSeatsAvailable(int seats){
		
		for( SeatRow row : rows){
			if ( row.getAvailableCount() < seats ){
				continue;
			}else{
				return true;
			}
		}	
		return false;

	}
	
	//Print the reservation status
	public void printReservationStatus(){
		
		for( SeatRow row : rows){
			System.out.print("\n Row " + row.getRowName() + " : ");
			
			for( Seat seat : row.getSeats() ){
				if ( seat.isBooked() )
					System.out.print(" " + seat.getReservationNumber() + " ");
				else
					System.out.print("(AV) ");
			}
		}
	}
	
	
	public static void main(String args[]){
		
		Reservation reservation = new Reservation();
		
		try (
			FileInputStream fstream = new FileInputStream( args[0] );
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream)) ;
			FileWriter fw = new FileWriter(new File("output.txt"))){
	
			String strLine;
	
			StringTokenizer tokenizer;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				
				tokenizer = new StringTokenizer(strLine);
				fw.write( reservation.bookSeats(tokenizer.nextToken(), new Integer(tokenizer.nextToken()).intValue()) + "\n");
			 
			}
		}catch( NumberFormatException | IOException exception ){ 
			System.out.println("Unable to initialize input file - Exiting !");
			System.out.println("Exception => " + exception.getMessage());
			System.exit(0);
		}

		System.out.println("Successfully processed all reservation requests !!!");
		
		//Prints the current movie theater status after processing all requests
		//reservation.printReservationStatus();
		
	}

}
