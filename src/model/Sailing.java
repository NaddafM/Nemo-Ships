package model;

import java.util.Date;

public class Sailing {

	private int number;
	private Date departureDateAndTime;
	private Date arrivaldateAndTime;

	public Sailing(int number, Date departureDateAndTime, Date arrivaldateAndTime) {
		this.number = number;
		this.departureDateAndTime = departureDateAndTime;
		this.arrivaldateAndTime = arrivaldateAndTime;
	}

	public Sailing(int number) {
		this.number = number;

	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Date getDepartureDateAndTime() {
		return departureDateAndTime;
	}

	public void setDepartureDateAndTime(Date departureDateAndTime) {
		this.departureDateAndTime = departureDateAndTime;
	}

	public Date getArrivaldateAndTime() {
		return arrivaldateAndTime;
	}

	public void setArrivaldateAndTime(Date arrivaldateAndTime) {
		this.arrivaldateAndTime = arrivaldateAndTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arrivaldateAndTime == null) ? 0 : arrivaldateAndTime.hashCode());
		result = prime * result + ((departureDateAndTime == null) ? 0 : departureDateAndTime.hashCode());
		result = prime * result + number;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sailing other = (Sailing) obj;
		if (arrivaldateAndTime == null) {
			if (other.arrivaldateAndTime != null)
				return false;
		} else if (!arrivaldateAndTime.equals(other.arrivaldateAndTime))
			return false;
		if (departureDateAndTime == null) {
			if (other.departureDateAndTime != null)
				return false;
		} else if (!departureDateAndTime.equals(other.departureDateAndTime))
			return false;
		if (number != other.number)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sail [number=" + number + ", departureDateAndTime=" + departureDateAndTime + ", arrivaldateAndTime="
				+ arrivaldateAndTime + "]";
	}

}
