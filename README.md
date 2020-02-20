# Nemo-Ships
in order to use the application you need to import the .bak database file to your SQL Server and 
the go to model/DataBaseConnection and add you sql server user name , password and url if needed :


	public static void initConn() throws Exception {

		String userName = "------";
		String password = "------";
		String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=Nemo_Ships";
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		conn = DriverManager.getConnection(url, userName, password);

	}
