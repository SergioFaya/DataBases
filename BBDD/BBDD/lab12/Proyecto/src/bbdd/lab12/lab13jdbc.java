package bbdd.lab12;

import java.sql.*;


public class lab13jdbc {
	public static java.sql.Connection con;
	public static Statement st;
	
	public static void main(String[] args) throws SQLException {
		con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/labdb","SA","");
		st =con.createStatement();
		exercise();
		st.close();
		con.close();
	}

	public static void exercise() throws SQLException {
		PreparedStatement psQuery = con.prepareStatement("create Table ?");
		ResultSet rs = st.executeQuery("create table");
		psQuery.setString(0, "hello");
		psQuery.executeQuery();
	}
}
