package model;

import java.util.Date;

public class CruiseShip extends Ship{
	
	private int maxNumberOfPeople;

	public CruiseShip(int number, String name, Date manufacturingDate, int maxCapacity, int maxNumberOfPeople) {
		super(number, name, manufacturingDate, maxCapacity);
        this.maxNumberOfPeople=maxNumberOfPeople;
	
	}

	public int getMaxNumberOfPeople() {
		return maxNumberOfPeople;
	}

	public void setMaxNumberOfPeople(int maxNumberOfPeople) {
		this.maxNumberOfPeople = maxNumberOfPeople;
	}
	
	
	
	

}
