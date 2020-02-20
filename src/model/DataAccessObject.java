package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.CallableStatement;
import java.sql.Date;

/**
 * 
 * 
 * The Data Access Class is basically an Class that provides access to an
 * underlying database in our case it is the SQL Server .
 * 
 * @author L.A
 *
 */
public class DataAccessObject {

	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	// ----------------------- Cruise Ship -----------------------------

	public static ArrayList<CruiseShip> getAllCruiseShips() throws SQLException, ParseException {

		ArrayList<CruiseShip> result = new ArrayList<CruiseShip>();

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getAllCruiseShips";
		rs = s.executeQuery(msg);

		while (rs.next()) {

			java.util.Date manDate = formatter.parse(rs.getString("manufacturingDate"));
			CruiseShip cs = new CruiseShip(rs.getInt(1), rs.getString(2), manDate, rs.getInt(4), rs.getInt(5));
			result.add(cs);

		}
		return result;

	}

	public static void addCruiseShip(String ShipId, String shipName, LocalDate manufacturingDate, String maxCapacity,
			String maxNumOfPeople) throws SQLException {

		Date manufacturingSQLDate = Date.valueOf(manufacturingDate);

		String query = "INSERT INTO cruiseShip VALUES (?,?,?,?,?)";
		PreparedStatement statement = DataBaseConnection.conn.prepareStatement(query);
		statement.setString(1, ShipId);
		statement.setString(2, shipName);
		statement.setDate(3, manufacturingSQLDate);
		statement.setString(4, maxCapacity);
		statement.setString(5, maxNumOfPeople);
		statement.execute();

	}

	public static void deleteCruiseShip(CruiseShip CruiseShioToDelete) throws SQLException {

		String msg = String.format("DELETE FROM CruiseShip WHERE (cruiseShipID=%s)", CruiseShioToDelete.getNumber());
		java.sql.Statement statment = DataBaseConnection.conn.createStatement();
		statment.execute(msg);

	}

	public static void updateCruiseShip(CruiseShip cruiseShipToUpdate) throws SQLException {

		java.sql.Date sqlDate = new java.sql.Date(cruiseShipToUpdate.getManufacturingDate().getTime());

		String query = "exec updateCruiseShip ?,?,?,?,?";
		PreparedStatement statement = DataBaseConnection.conn.prepareStatement(query);
		statement.setInt(1, cruiseShipToUpdate.getNumber());
		statement.setString(2, cruiseShipToUpdate.getName());
		statement.setDate(3, sqlDate);
		statement.setInt(4, cruiseShipToUpdate.getMaxCapacity());
		statement.setInt(5, cruiseShipToUpdate.getMaxNumberOfPeople());

		statement.execute();

	}

	// --------------------------- Room ---------------------------------

	public static void updateShip(int cruiseShipNumber, String editColName, Object value) {

		String msg = String.format("UPDATE cruiseShip " + "SET %s ='%s' Where cruiseShipID ='%s'", editColName, value,
				cruiseShipNumber);
		try {
			DataBaseConnection.conn.createStatement().execute(msg);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}

	}

	public static ArrayList<Room> getRooms(CruiseShip cruiseShip) throws SQLException {

		int cruiseShipID = cruiseShip.getNumber();
		ArrayList<Room> result = new ArrayList<Room>();

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getRooms " + cruiseShipID;
		rs = s.executeQuery(msg);

		while (rs.next()) {

			Room r = new Room(cruiseShipID, rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getDouble(5));
			result.add(r);

		}
		return result;

	}

	public static ArrayList<Room> getRooms(Integer cruiseShipId) throws SQLException {

		ArrayList<Room> result = new ArrayList<Room>();
		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getRooms " + cruiseShipId;
		rs = s.executeQuery(msg);

		while (rs.next()) {

			Room r = new Room(cruiseShipId, rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getDouble(5));
			result.add(r);

		}
		return result;

	}

	public static void deleteRoom(Room r) throws SQLException {

		String msg = String.format("exec deleteRoom " + r.getCruiseShipID() + "," + r.getRoomNumber());
		java.sql.Statement statment = DataBaseConnection.conn.createStatement();
		statment.execute(msg);

	}

	public static void addRoom(CruiseShip cs, Room r) throws SQLException {

		String msg = String.format("exec addRoom " + cs.getNumber() + "," + r.getRoomNumber() + "," + r.getBedsAmount()
				+ "," + r.getType() + "," + r.getPricePerNight());
		java.sql.Statement statment = DataBaseConnection.conn.createStatement();
		statment.execute(msg);

	}

	// -------------------------- Person --------------------------------

	public static ArrayList<Person> getPersons() throws SQLException, ParseException {

		ArrayList<Person> result = new ArrayList<Person>();

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getPersons";
		rs = s.executeQuery(msg);
		while (rs.next()) {

			java.util.Date BDate = formatter.parse(rs.getString(4));
			Person p = new Person(rs.getString(1), rs.getString(2), rs.getString(3), BDate, rs.getString(5),
					rs.getString(6));
			if (checkVIPcustomer(p.getId()))
				p.setAsVIP();

			result.add(p);
		}
		return result;

	}

	public static void addPerson(Person p) throws SQLException, ParseException {

		java.sql.Date sqlDate = new java.sql.Date(p.getBirthDate().getTime());

		String query = "exec addPerson ?,?,?,?,?,?";
		PreparedStatement statement = DataBaseConnection.conn.prepareStatement(query);
		statement.setString(1, p.getId());
		statement.setString(2, p.getFirstName());
		statement.setString(3, p.getLastName());
		statement.setDate(4, sqlDate);
		statement.setString(5, p.getPhoneNumber());
		statement.setString(6, p.getEmail());
		statement.execute();

	}

	public static Person getPerson(String id) throws SQLException, ParseException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getPerson " + id;
		rs = s.executeQuery(msg);

		Person p = null;
		while (rs.next()) {
			java.util.Date BDate = formatter.parse(rs.getString(4));
			p = new Person(rs.getString(1), rs.getString(2), rs.getString(3), BDate, rs.getString(5), rs.getString(6));

		}
		return p;

	}

	public static void deletePerson(Person p) throws SQLException {

		String msg = String.format("exec deletePerson " + p.getId());
		java.sql.Statement statment = DataBaseConnection.conn.createStatement();
		statment.execute(msg);

	}

	public static void updatePerson(Person PersonToUpdate) throws SQLException {

		java.sql.Date bDate = new java.sql.Date(PersonToUpdate.getBirthDate().getTime());
		String query = "exec updatePerson ?,?,?,?,?,?";
		PreparedStatement statement = DataBaseConnection.conn.prepareStatement(query);
		statement.setString(1, PersonToUpdate.getId());
		statement.setString(2, PersonToUpdate.getFirstName());
		statement.setString(3, PersonToUpdate.getLastName());
		statement.setDate(4, bDate);
		statement.setString(5, PersonToUpdate.getEmail());
		statement.setString(6, PersonToUpdate.getPhoneNumber());
		statement.execute();

	}

	// --------------------------- Ports ---------------------------------

	public static ArrayList<Port> getPorts() throws SQLException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getPorts";
		rs = s.executeQuery(msg);

		ArrayList<Port> result = new ArrayList<Port>();
		while (rs.next()) {

			Port p = new Port(new Country(rs.getString(1)), rs.getString(2));
			result.add(p);
		}
		return result;

	}

	public static void deletePort(Port p) throws SQLException {

		String query = "exec deletePort ?,?";
		PreparedStatement statement = DataBaseConnection.conn.prepareStatement(query);
		statement.setString(1, p.getCountry().getCountryName());
		statement.setString(2, p.getPortName());
		statement.execute();

	}

	public static void addPort(Port p) throws SQLException {

		String query = "exec addNewPort ?,?";
		PreparedStatement statement = DataBaseConnection.conn.prepareStatement(query);
		statement.setString(1, p.getCountry().getCountryName());
		statement.setString(2, p.getPortName());
		statement.execute();

	}

	// --------------------------- Countries ---------------------------------

	public static ArrayList<Country> getCountries() throws SQLException {

		ArrayList<Country> result = new ArrayList<Country>();

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getCountries";
		rs = s.executeQuery(msg);

		while (rs.next()) {

			Country c = new Country(rs.getString(1));
			result.add(c);
		}
		return result;

	}

	public static void addCountry(Country c) throws SQLException {

		String query = "exec addCountry ?";
		PreparedStatement statement = DataBaseConnection.conn.prepareStatement(query);
		statement.setString(1, c.getCountryName());
		statement.execute();

	}

	public static void deleteCountry(Country data) throws SQLException {

		String msg = String.format("exec deleteCountry " + data.getCountryName());
		java.sql.Statement statment = DataBaseConnection.conn.createStatement();
		statment.execute(msg);
	}

	public static void updateCountry(Country data) throws SQLException {

		String msg = String.format("exec updateCountry " + data.getCountryName());
		java.sql.Statement statment = DataBaseConnection.conn.createStatement();
		statment.execute(msg);

	}

	// ------------------------- Cruise orders --------------------------------

	public static ArrayList<CruiseOrder> getCruiseOrders() throws SQLException {

		ArrayList<CruiseOrder> result = new ArrayList<CruiseOrder>();

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getCruiseOrders";
		rs = s.executeQuery(msg);
		while (rs.next()) {
			CruiseOrder co = new CruiseOrder(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4));
			result.add(co);
		}
		return result;
	}

	public static Integer getOrdersAmount(String id) throws SQLException {

		int count = 0;
		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getOrdersAmount " + id;
		rs = s.executeQuery(msg);
		while (rs.next()) {
			count = rs.getInt(1);
		}
		return count;

	}

	public static void deleteCruiseOrder(CruiseOrder data) throws SQLException {

		String query = "exec deleteCruiseOrder ?,?,?";
		PreparedStatement statement = DataBaseConnection.conn.prepareStatement(query);
		statement.setInt(1, data.getCruiseId());
		statement.setInt(2, data.getCruiseShipId());
		statement.setInt(3, data.getRoomNumber());
		statement.execute();

	}

	public static void addCruiseOrder(CruiseOrder newCruiseOrder) throws SQLException {

		String query = "exec addCruiseOrder ?,?,?,?";
		PreparedStatement statement = DataBaseConnection.conn.prepareStatement(query);

		statement.setInt(1, newCruiseOrder.getCruiseId());
		statement.setInt(2, newCruiseOrder.getCruiseShipId());
		statement.setInt(3, newCruiseOrder.getRoomNumber());
		statement.setString(4, newCruiseOrder.getPersonId());
		statement.execute();

	}

	// ----------------------------- Sailing -----------------------------------

	public static void addSailing(Sailing data) throws SQLException {

		java.sql.Timestamp leavingTime = new java.sql.Timestamp(data.getDepartureDateAndTime().getTime());
		java.sql.Timestamp returnTime = new java.sql.Timestamp(data.getArrivaldateAndTime().getTime());

		String query = "exec addSailing ?,?,?";
		PreparedStatement statement = DataBaseConnection.conn.prepareStatement(query);
		statement.setInt(1, data.getNumber());
		statement.setTimestamp(2, leavingTime);
		statement.setTimestamp(3, returnTime);
		statement.execute();

	}

	public static void deleteSailing(Sailing data) throws SQLException {

		String msg = String.format("exec deleteSailing " + data.getNumber());
		java.sql.Statement statment = DataBaseConnection.conn.createStatement();
		statment.execute(msg);
	}

	public static ArrayList<Sailing> getSailings() throws SQLException, ParseException {

		ArrayList<Sailing> result = new ArrayList<Sailing>();

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getSailings";
		rs = s.executeQuery(msg);
		while (rs.next()) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			java.util.Date leavingTime = sdf.parse(rs.getString(2));
			java.util.Date returnTime = sdf.parse(rs.getString(3));
			Sailing newSailing = new Sailing(rs.getInt(1), leavingTime, returnTime);
			result.add(newSailing);

		}
		return result;
	}

	// ----------------------------- Cruises -----------------------------------

	public static ArrayList<Cruise> getCruises() throws SQLException, ParseException {

		ArrayList<Cruise> result = new ArrayList<Cruise>();

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getCruises";
		rs = s.executeQuery(msg);
		while (rs.next()) {

			ResultSet sailingResult;
			java.sql.Statement s2 = DataBaseConnection.conn.createStatement();
			sailingResult = s2.executeQuery("exec getSailing " + rs.getInt(1));
			java.util.Date leavingTime = null;
			java.util.Date returnTime = null;

			if (sailingResult.next()) {

				leavingTime = formatter.parse(sailingResult.getString(2));
				returnTime = formatter.parse(sailingResult.getString(3));
			}

			Cruise c = new Cruise(rs.getInt(1), rs.getInt(2), leavingTime, returnTime);
			result.add(c);

		}
		return result;
	}

	public static void addCruise(Cruise newCruise) throws SQLException {

		String query = "exec addCruise ?,?";
		PreparedStatement statement = DataBaseConnection.conn.prepareStatement(query);
		statement.setInt(1, newCruise.getNumber());
		statement.setInt(2, newCruise.getShipNumber());
		statement.execute();

	}

	public static void deleteCruise(Cruise data) throws SQLException {

		String msg = String.format("exec deleteCruise " + data.getNumber());
		java.sql.Statement statment = DataBaseConnection.conn.createStatement();
		statment.execute(msg);
	}

	// ----------------------------- Sail To -----------------------------------

	public static ArrayList<SailTo> getSailTo(Sailing sailing) throws SQLException {

		ArrayList<SailTo> result = new ArrayList<SailTo>();

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getSailTo " + sailing.getNumber();
		rs = s.executeQuery(msg);
		while (rs.next()) {

			java.sql.Date arDate = rs.getDate(4);
			java.sql.Date leDate = rs.getDate(5);

			SailTo st = new SailTo(rs.getString(1), rs.getString(2), rs.getInt(3), arDate, leDate);
			result.add(st);
		}
		return result;

	}

	public static void addSailTo(SailTo sailTo) throws SQLException {

		java.sql.Date arDate = new java.sql.Date(sailTo.getArrivaleDate().getTime());
		java.sql.Date leDate = new java.sql.Date(sailTo.getLeavingDate().getTime());

		String query = "exec addSailTo ?,?,?,?,?";
		PreparedStatement statement = DataBaseConnection.conn.prepareStatement(query);
		statement.setString(1, sailTo.getCountryName());
		statement.setString(2, sailTo.getPortName());
		statement.setInt(3, sailTo.getSailingId());
		statement.setDate(4, arDate);
		statement.setDate(5, leDate);
		statement.execute();

	}

	public static void deleteSailTo(SailTo sailtoToDelete) throws SQLException {

		String msg = String.format("exec deleteSailTo " + sailtoToDelete.getSailingId() + ","
				+ sailtoToDelete.getCountryName() + "," + sailtoToDelete.getPortName());
		java.sql.Statement statment = DataBaseConnection.conn.createStatement();
		statment.execute(msg);

	}

	public static Integer getShipByCruiseNumber(Integer cruiseNumber) throws SQLException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getCruise " + cruiseNumber;
		rs = s.executeQuery(msg);
		if (rs.next()) {

			return rs.getInt(2);
		}
		return 0;
	}

	// -------------------- Admin Statistics Queries --------------------------

	public static ResultSet getPopularDestinations() throws SQLException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getPopularDestinations";
		rs = s.executeQuery(msg);
		return rs;

	}

	public static ResultSet getOrdersCountByDistination() throws SQLException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getOrdersCountByDistination";
		rs = s.executeQuery(msg);
		return rs;

	}

	public static ResultSet getAvailbleRoomsBySailing() throws SQLException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getAvailbleRoomsBySailing";
		rs = s.executeQuery(msg);
		return rs;

	}

	public static ResultSet getProfitPerYear() throws SQLException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getProfitPerYear";
		rs = s.executeQuery(msg);
		return rs;

	}

	public static ResultSet getUpComingSailingCruiseJoin() throws SQLException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getUpComingSailingCruiseJoin";
		rs = s.executeQuery(msg);
		return rs;

	}

	// ---------------------- User Statistics Queries ------------------------------

	public static ArrayList<CruiseOrder> getOrdersHistory(String id) throws SQLException {

		ArrayList<CruiseOrder> result = new ArrayList<CruiseOrder>();

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getOrdersHistory " + id;
		rs = s.executeQuery(msg);
		while (rs.next()) {
			CruiseOrder co = new CruiseOrder(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));
			result.add(co);
		}
		return result;

	}

	public static Integer getTotalOrdersAmount() throws SQLException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getTotalOrdersAmount";
		rs = s.executeQuery(msg);
		int amount = 0;
		while (rs.next()) {
			amount = rs.getInt(1);

		}
		return amount;

	}

	public static Date getLastCruiseDate(String personId) throws SQLException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getLastCruiseDate " + personId;
		rs = s.executeQuery(msg);
		Date d = null;
		while (rs.next()) {

			d = rs.getDate(1);
		}
		return d;

	}

	public static ResultSet getDistinationCounter(String id) throws SQLException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getDistinationCounter " + id;
		rs = s.executeQuery(msg);
		return rs;

	}

	public static Integer getTotalPaid(String id) throws SQLException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getTotalPaid " + id;
		rs = s.executeQuery(msg);
		int paid = 0;
		while (rs.next()) {
			paid = rs.getInt(1);

		}
		return paid;

	}

	// ----------------------------- utilities -----------------------------------

	public static Boolean checkVIPcustomer(String PersonId) throws SQLException {

		CallableStatement s = DataBaseConnection.conn.prepareCall("{? = call checkVIPcustomer(?)}");
		s.registerOutParameter(1, java.sql.Types.INTEGER);
		s.setString(2, PersonId);

		s.execute();
		if (s.getInt(1) == 1)
			return true;

		return false;

	}

	public static String getStoredProcedure(String storedProcedure) throws SQLException {

		String result = "";

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getStoredProcedure " + storedProcedure;
		rs = s.executeQuery(msg);
		while (rs.next()) {

			result = rs.getString(1);
		}
		return result;

	}

	public static ArrayList<String> getAllStoredProcedures() throws SQLException {

		ArrayList<String> result = new ArrayList<String>();

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getAllStoredProcedures";
		rs = s.executeQuery(msg);
		while (rs.next()) {

			result.add(rs.getString(1));
		}
		return result;

	}

	public static ArrayList<Integer> getEmptyRooms(int CruiseNumber) throws SQLException {

		ArrayList<Integer> result = new ArrayList<Integer>();

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getEmptyRooms " + CruiseNumber;
		rs = s.executeQuery(msg);
		while (rs.next()) {
			result.add(rs.getInt(1));
		}
		return result;

	}

	public static Integer getUsersAmount() throws SQLException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getUsersAmount";
		rs = s.executeQuery(msg);
		int amount = 0;
		while (rs.next()) {
			amount = rs.getInt(1);

		}
		return amount;

	}

	public static Integer getShipsAmount() throws SQLException {

		ResultSet rs;
		java.sql.Statement s = DataBaseConnection.conn.createStatement();
		String msg = "exec getShipsAmount";
		rs = s.executeQuery(msg);
		int amount = 0;
		while (rs.next()) {
			amount = rs.getInt(1);

		}
		return amount;

	}

}
