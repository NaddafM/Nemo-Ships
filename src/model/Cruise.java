package model;

import java.util.Date;

public class Cruise extends Sailing {

	private int shipNumber;

	public Cruise(int number,int shipNumber, Date departureDateAndTime, Date arrivaldateAndTime) {
		super(number, departureDateAndTime, arrivaldateAndTime);
		this.shipNumber = shipNumber;

	}


	public int getShipNumber() {
		return shipNumber;
	}

	public void setShipNumber(int shipNumber) {
		this.shipNumber = shipNumber;
	}

	@Override
	public String toString() {
		return "Cruise [cruiseNumber="+super.getNumber()+" shipNumber=" + shipNumber + "]";
	}
	
	
	
	
	

}
