package model;

public class Port {

	
	private Country country;
	private String portName;
	
	public Port(Country country, String port) {
		this.country = country;
		this.portName = port;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String port) {
		this.portName = port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((portName == null) ? 0 : portName.hashCode());
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
		Port other = (Port) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (portName == null) {
			if (other.portName != null)
				return false;
		} else if (!portName.equals(other.portName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Port [country=" + country.getCountryName() + ", port=" + portName + "]";
	}
	
	
	
	
	
	
}
