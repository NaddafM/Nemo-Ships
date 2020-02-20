package model;

import java.util.Date;

public class SailTo {

	private String countryName;
	private String portName;
	private Integer sailingId;
	private Date arrivaleDate;
	private Date leavingDate;

	public SailTo(String countryName, String portName, Integer sailingId, Date arrivaleDate, Date leavingDate) {
		super();
		this.countryName = countryName;
		this.portName = portName;
		this.sailingId = sailingId;
		this.arrivaleDate = arrivaleDate;
		this.leavingDate = leavingDate;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public Integer getSailingId() {
		return sailingId;
	}

	public void setSailingId(Integer sailingId) {
		this.sailingId = sailingId;
	}

	public Date getArrivaleDate() {
		return arrivaleDate;
	}

	public void setArrivaleDate(Date arrivaleDate) {
		this.arrivaleDate = arrivaleDate;
	}

	public Date getLeavingDate() {
		return leavingDate;
	}

	public void setLeavingDate(Date leavingDate) {
		this.leavingDate = leavingDate;
	}

	@Override
	public String toString() {
		return "SailTo [countryName=" + countryName + ", portName=" + portName + ", sailingId=" + sailingId
				+ ", arrivaleDate=" + arrivaleDate + ", leavingDate=" + leavingDate + "]";
	}

}
