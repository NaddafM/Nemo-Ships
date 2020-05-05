# Nemo-Ships

Converting a complicated riquirments to an ERD Diagram , building a database on SQL Sever using DDL to match it , writing DML stored procedures to be used by the application layer to retrive and manupilate data and developing a full featured desktop JAVA application using MVC arcitecture and DAO design pattern allowing different types of users to view/manuplate the data of the system throw a FXML GUI built with scene builder and JavaFX


in order to use the application you need to import the .bak database file to your SQL Server and 
the go to model/DataBaseConnection and add you sql server user name , password and url if needed :


	public static void initConn() throws Exception {

		String userName = "------";
		String password = "------";
		String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=Nemo_Ships";
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		conn = DriverManager.getConnection(url, userName, password);

	}
