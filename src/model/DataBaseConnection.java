package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnection {

	protected DriverManager dm;
	public static Connection conn;

	public static void initConn() throws Exception {

		String userName = "sa";
		String password = "sa1234";
		String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=Nemo_Ships";
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		conn = DriverManager.getConnection(url, userName, password);

	}

}