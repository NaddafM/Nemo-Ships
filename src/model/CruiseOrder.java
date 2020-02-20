package model;

public class CruiseOrder {
	
	private Integer counter; //optional
	private Integer cruiseId;
	private Integer cruiseShipId;
	private Integer roomNumber;
	private String personId;
    private Integer price; //optional

	public CruiseOrder(Integer cruiseId, Integer cruiseShipId, Integer roomNumber, String personId) {
		this.cruiseId = cruiseId;
		this.cruiseShipId = cruiseShipId;
		this.roomNumber = roomNumber;
		this.personId = personId;
	}
	public CruiseOrder(Integer counter,Integer cruiseId, Integer cruiseShipId, Integer roomNumber,Integer price) { //optional
		this.setCounter(counter);
		this.cruiseId = cruiseId;
		this.cruiseShipId = cruiseShipId;
		this.roomNumber = roomNumber;
		this.setPrice(price);
	}
	public Integer getCruiseId() {
		return cruiseId;
	}

	public void setCruiseId(Integer cruiseId) {
		this.cruiseId = cruiseId;
	}

	public Integer getCruiseShipId() {
		return cruiseShipId;
	}

	public void setCruiseShipId(Integer cruiseShipId) {
		this.cruiseShipId = cruiseShipId;
	}

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@Override
	public String toString() {
		return "CruiseOrder [cruiseId=" + cruiseId + ", cruiseShipId=" + cruiseShipId + ", roomNumber=" + roomNumber
				+ ", personId=" + personId + "]";
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getCounter() {
		return counter;
	}
	public void setCounter(Integer counter) {
		this.counter = counter;
	}

}
