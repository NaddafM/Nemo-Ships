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
<img src="<img width="936" alt="dashboard" 
src="https://userimages.githubusercontent.com/57488522/81082810-74c21280-8efc-11ea-8297-ae4156354519.png">
<img width="925" alt="personUI" src="https://user-images.githubusercontent.com/57488522/81082831-7ab7f380-8efc-11ea-8301-688bc22db115.png">
<img width="917" alt="AddPersonUI" src="https://user-images.githubusercontent.com/57488522/81082841-7d1a4d80-8efc-11ea-8df3-541be24a6815.png">
<img width="921" alt="addShipUI" src="https://user-images.githubusercontent.com/57488522/81082846-7e4b7a80-8efc-11ea-9c14-1fc9ed5f431d.png">
<img width="911" alt="EditPersonUI" src="https://user-images.githubusercontent.com/57488522/81082857-81466b00-8efc-11ea-9eec-b02aa428d3b8.png">
<img width="916" alt="QueriesUI" src="https://user-images.githubusercontent.com/57488522/81082868-83a8c500-8efc-11ea-9ec0-064a71c621af.png">
<img width="441" alt="LogInUI" src="https://user-images.githubusercontent.com/57488522/81082882-87d4e280-8efc-11ea-96e7-e960d8c3d9de.png">
<img width="637" alt="UserUI" src="https://user-images.githubusercontent.com/57488522/81082896-8dcac380-8efc-11ea-8c9c-55c7e90584eb.png">
![Uploading UserDashboard.png…](" width="15%"></img> 
