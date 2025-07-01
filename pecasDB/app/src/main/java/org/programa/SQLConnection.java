package org.programa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnection {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement pStatement = null;
	private ResultSet resultSet = null;

	public void readDataBase() throws Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
							+ "user=lius&passord=0511");
			statement = connect.createStatement();

			resultSet = statement
					.executeQuery("select * from PECAS.estoque");
			writeResultSet(resultSet);

			pStatement = connect
					.pStatement("insert into PECAS.estoque values (default, ?)");

			pStatement.setString(1, "Lius");
			pStatement.executeUpdate();
		} catch (Exception e) {

		}
	}

}
