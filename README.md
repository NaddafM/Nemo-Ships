# Nemo-Ships
Converting a complicated requirements to an ERD Diagram , Building a database on SQL Server using DDL to match it , 
Writing DML stored procedures to be used by the application layer to access the data and developing a full featured desktop JAVA application using MVC architecture and DAO design pattern allowing different types of users to view/manipulate the data of the system through a FXML GUI built with scene builder and JavaFX .

admin authentication :
username :admin , password: admin
user authentication :
username : # use any person id from the database , password: abc123

in order to use the application you need to import the .bak database file to your SQL Server and 
then go to model/DataBaseConnection and add your sql server user name , password and url if needed :

	public static void initConn() throws Exception {

		String userName = "------";
		String password = "------";
		String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=Nemo_Ships";
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		conn = DriverManager.getConnection(url, userName, password);

	}
<img width="936" alt="dashboard" src="https://user-images.githubusercontent.com/57488522/81082810-74c21280-8efc-11ea-8297-ae4156354519.png">
<img width="925" alt="personUI" src="https://user-images.githubusercontent.com/57488522/81082831-7ab7f380-8efc-11ea-8301-688bc22db115.png">
<img width="917" alt="AddPersonUI" src="https://user-images.githubusercontent.com/57488522/81082841-7d1a4d80-8efc-11ea-8df3-541be24a6815.png">
<img width="921" alt="addShipUI" src="https://user-images.githubusercontent.com/57488522/81082846-7e4b7a80-8efc-11ea-9c14-1fc9ed5f431d.png">
<img width="911" alt="EditPersonUI" src="https://user-images.githubusercontent.com/57488522/81082857-81466b00-8efc-11ea-9eec-b02aa428d3b8.png">
<img width="916" alt="QueriesUI" src="https://user-images.githubusercontent.com/57488522/81082868-83a8c500-8efc-11ea-9ec0-064a71c621af.png">
<img width="441" alt="LogInUI" src="https://user-images.githubusercontent.com/57488522/81082882-87d4e280-8efc-11ea-96e7-e960d8c3d9de.png">
<img width="637" alt="UserUI" src="https://user-images.githubusercontent.com/57488522/81082896-8dcac380-8efc-11ea-8c9c-55c7e90584eb.png">
<img width="599" alt="UserDashboard" src="https://user-images.githubusercontent.com/57488522/81082897-8dcac380-8efc-11ea-9dd1-a945e6f64796.png">
<img width="609" alt="PayUI" src="https://user-images.githubusercontent.com/57488522/81082902-8efbf080-8efc-11ea-882f-179bc9f0ebce.png">
<img width="747" alt="SQL_ERD" src="https://user-images.githubusercontent.com/57488522/81175121-a6de7d80-8fab-11ea-9601-561643418f38.png">

