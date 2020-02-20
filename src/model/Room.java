package model;

public class Room {

	private int cruiseShipID;
	private int roomNumber;
	private int bedsAmount;
	private String type;
	private double pricePerNight;

	public int getCruiseShipID() {
		return cruiseShipID;
	}

	public void setCruiseShipID(int cruiseShipID) {
		this.cruiseShipID = cruiseShipID;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public int getBedsAmount() {
		return bedsAmount;
	}

	public void setBedsAmount(int bedsAmount) {
		this.bedsAmount = bedsAmount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getPricePerNight() {
		return pricePerNight;
	}

	public void setPricePerNight(double pricePerNight) {
		this.pricePerNight = pricePerNight;
	}

	public Room(int cruiseShipID, int roomNumber, int bedsAmount, String type, double pricePerNight) {
		super();
		this.cruiseShipID = cruiseShipID;
		this.roomNumber = roomNumber;
		this.bedsAmount = bedsAmount;
		this.type = type;
		this.pricePerNight = pricePerNight;
	}

	@Override
	public String toString() {
		return "Room [cruiseShipID=" + cruiseShipID + ", roomNumber=" + roomNumber + ", bedsAmount=" + bedsAmount
				+ ", type=" + type + ", pricePerNight=" + pricePerNight + "]";
	}

}
