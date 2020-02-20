package model;

import java.util.Date;

abstract class Ship {

	private Integer number;
	private String name;
	private Date ManufacturingDate;
	private Integer maxCapacity;
	

	public Ship(int number, String name, Date manufacturingDate, int maxCapacity) {

		this.number = number;
		this.name = name;
		ManufacturingDate = manufacturingDate;
		this.maxCapacity = maxCapacity;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getManufacturingDate() {
		return ManufacturingDate;
	}

	public void setManufacturingDate(Date manufacturingDate) {
		ManufacturingDate = manufacturingDate;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ManufacturingDate == null) ? 0 : ManufacturingDate.hashCode());
		result = prime * result + maxCapacity;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Ship other = (Ship) obj;
		if (ManufacturingDate == null) {
			if (other.ManufacturingDate != null)
				return false;
		} else if (!ManufacturingDate.equals(other.ManufacturingDate))
			return false;
		if (maxCapacity != other.maxCapacity)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (number != other.number)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Ship [number=" + number + ", name=" + name + ", ManufacturingDate=" + ManufacturingDate
				+ ", maxCapacity=" + maxCapacity + "]";
	}

}
