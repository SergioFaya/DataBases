package bbdd.lab12;
import java.sql.*;
import java.util.Scanner;

public class Program {
	
	public final static String URL_HYPERSONIC =  "jdbc:hsqldb:hsql://localhost/labdb";
	public static Connection con;
	public static Statement st;
	
	public static void main(String[] args) throws SQLException {
		//Ejemplos para leer por teclado
//		System.out.println("Leer un entero por teclado");	
//		int entero = ReadInt();
//		System.out.println("Leer una cadena por teclado");	
//		String cadena = ReadString();
		con = DriverManager.getConnection(URL_HYPERSONIC, "SA","");
		st = con.createStatement();
//		exercise1_1();
//		exercise1_2();
//		exercise2();
//		exercise3();
//		exercise4();
//		exercise5_1();
//		exercise5_2();
//		exercise5_3();
		exercise6_2();
		st.close();
		con.close();
	}

	/*
		1. Mostrar por pantalla los resultados de las consultas 21 y 32 de la Pr�ctica 2. 
		1.1. 21. Obtener el nombre y el apellido de los clientes que han adquirido un coche en un concesionario de Madrid, el cual dispone de coches del modelo gti.
	 */
	public static void exercise1_1() throws SQLException {
		ResultSet rs1 = st.executeQuery("select distinct Clientes.NOMBRE,Clientes.APELLIDO"
				+ " from clientes,ventas,coches,distribucion,concesionarios"
				+ " where clientes.dni = ventas.dni "
				+ "and ventas.CIFC = concesionarios.cifc "
				+ "and concesionarios.cifc = distribucion.cifc "
				+ "and distribucion.codcoche = coches.CODCOCHE "
				+ "and coches.modelo ='gti'and CONCESIONARIOS.ciudadc = 'madrid'");
		System.out.println("Nombres \t Apellidos");
		while (rs1.next()){
			String nombre = rs1.getString("nombre");
			String apellido = rs1.getString("apellido");
			
			System.out.println(nombre + " \t " + apellido);
		}
		
		rs1.close();
		
		
	}
	
	/* 
		1.2. 32. Obtener un listado de los concesionarios cuyo promedio de coches supera a la cantidad promedio de todos los concesionarios.  
	*/
	public static void exercise1_2() throws SQLException {

		ResultSet rs2 = st.executeQuery("select cifc ,nombrec,ciudadc "
				+ "from concesionarios"
				+ " where cifc in "
				+ "(select cifc "
				+ "from distribucion group by cifc "
				+ "having avg(cantidad)>(select avg(cantidad) "
				+ "from distribucion))");
		System.out.println();
		System.out.println("CIFC\tNOMBREC\tCIUDADC");
		while (rs2.next()){
			String cifc = rs2.getString("cifc");
			String nombrec = rs2.getString("nombrec");
			String ciudadc =  rs2.getString("ciudadc");
			
			System.out.println(cifc + " \t " + nombrec + " \t " + ciudadc);
		}
		rs2.close();
	}
	
	/*
		2. Mostrar por pantalla el resultado de la consulta 6 de la Pr�ctica 2 de forma el color de la b�squeda sea introducido por el usuario.
			6. Obtener el nombre de las marcas de las que se han vendido coches de un color introducido por el usuario.
	*/
	public static void exercise2() throws SQLException {
		System.out.println("Introduce a color");
		PreparedStatement psQuery = con.prepareStatement("select nombrem "
				+ "FROM marcas m,coches c,marco mc ,ventas v "
				+ "where m.cifm = mc.cifm and mc.CODCOCHE = c.CODCOCHE "
				+ "and c.CODCOCHE = v.codcoche and v.color = ?");
		String color = ReadString();
		psQuery.setString(1, color);
		ResultSet rs3 = psQuery.executeQuery();
		System.out.println("NOMBREM");
		while (rs3.next()){
			String nombrem = rs3.getString(1);
			String[] output = {nombrem};
			print(output);
			
		}
		rs3.close();
	}
	
	private static void print(String[] output){
		StringBuilder builder = new StringBuilder();
		for (String string : output) {
			builder.append(string+"\t");
		}
		System.out.println(builder.toString());
	}
	
	/*
		3. Realizar una aplicaci�n en Java para ejecutar la consulta 27 de la Pr�ctica 2 de forma que los l�mites la cantidad de coches sean introducidos por el usuario. 
			27. Obtener el cifc de los concesionarios que disponen de una cantidad de coches comprendida entre dos cantidades introducidas por el usuario, ambas inclusive.
	 */
	public static void exercise3() throws SQLException {
		PreparedStatement psQuery = con.prepareStatement("select CIFC, sum(distribucion.CANTIDAD) "
				+ "from distribucion "
				+ "group by cifc "
				+ "having (sum(distribucion.CANTIDAD) >=? "
				+ "and sum(distribucion.CANTIDAD)<=?)" );
		System.out.println("Introduce lower bound");
		String bound1 = ReadString();
		System.out.println("Introduce upper bound");
		String bound2 = ReadString();
		psQuery.setString(1, bound1);
		psQuery.setString(2, bound2);
		ResultSet rs = psQuery.executeQuery();
		String[] header= {"CIFC","CANTIDAD"};
		print(header);
		while(rs.next()){
			String cifc = rs.getString(1);
			String cant = rs.getString(2);
			String[] output = {cifc,cant};
			print(output);
		}
	}
	
	/*
		4. Realizar una aplicaci�n en Java para ejecutar la consulta 24 de la Pr�ctica 2 de forma que tanto la ciudad del concesionario como el color sean introducidos por el usuario. 
			24.	Obtener los nombres de los clientes que no han comprado coches de un color introducido por el usuario en concesionarios de una ciudad introducida por el usuario.
	 */
	public static void exercise4()throws SQLException {
		PreparedStatement psQuery = con.prepareStatement("select clientes.nombre "
				+ "from clientes,ventas,concesionarios"
				+ "	except "
				+ "	select clientes.nombre "
				+ "from clientes,ventas,concesionarios "
				+ "where clientes.dni = ventas.dni "
				+ "and ventas.cifc = concesionarios.cifc "
				+ "and concesionarios.CIUDADC = ? "
				+ "and ventas.COLOR = ?");
		System.out.println("Introduce the city where the Concesionario is");
		String bound1 = ReadString();
		System.out.println("Introduce the color of the car");
		String bound2 = ReadString();
		psQuery.setString(1, bound1);
		psQuery.setString(2, bound2);
		ResultSet rs = psQuery.executeQuery();
		System.out.println("NOMBRE");
		while(rs.next()){
			String name = rs.getString(1);
			System.out.println(name);
		}
	}
	
	/*
		5. Realizar una aplicaci�n en Java que haciendo uso de la instrucci�n SQL adecuada: 
		5.1. Introduzca datos en la tabla coches cuyos datos son introducidos por el usuario
	 */
	public static void exercise5_1(){
		try{
		PreparedStatement psQuery = con.prepareStatement("insert into coches "
				+ "values (?,?,?)");
		System.out.println("Introduce the CODCOCHE(primary key)");
		String bound1 = ReadString();
		System.out.println("Introduce the NOMBRECH of the car");
		String bound2 = ReadString();
		System.out.println("Introduce the MODELO of the car");
		String bound3 = ReadString();
		psQuery.setString(1, bound1);
		psQuery.setString(2, bound2);
		psQuery.setString(3, bound3);
		psQuery.executeUpdate();
		System.out.println("Insertion completed new coche("+bound1+" "+bound2+" "+bound3);
		}catch(SQLException e){
			e.printStackTrace();
			System.out.printf("Fail when inserting");
		}
	}
	
	/*
		5.2. Borre un determinado coche cuyo c�digo es introducido por el usuario. 
	 */
	public static void exercise5_2() {
		try{
			PreparedStatement psQuery = con.prepareStatement("delete from coches "
					+ "where coches.CODCOCHE = ?");
			System.out.println("Introduce the CODCOCHE(primary key)");
			String bound1 = ReadString();
			psQuery.setString(1, bound1);
			psQuery.executeUpdate();
			System.out.println("Deletion completed from coches where the CODCOCHE is "+bound1);
			}catch(SQLException e){
				e.printStackTrace();
				System.out.printf("Fail when deleting");
			}
	}
	
	/*	 
		5.3. Actualice el nombre y el modelo para un determinado coche cuyo c�digo es introducido por el usuario.
	 */
	public static void exercise5_3() {		
		try{
		PreparedStatement psQuery = con.prepareStatement("UPDATE coches "
		+ "SET NOMBRECH = ? , MODELO= ? "
		+ "WHERE CODCOCHE = ?");
		System.out.println("Select the new NOMBRECH");
		String bound1 = ReadString();
		System.out.println("Select the new MODELO");
		String bound2 = ReadString();
		System.out.println("Select the CODCOCHE where these values will be replaced");
		String bound3 = ReadString();
		psQuery.setString(1, bound1);
		psQuery.setString(2, bound2);
		psQuery.setString(3, bound3);
		psQuery.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed when updating such value");
		}
	}
	
	/*
		6. Invocar la funci�n y el procedimiento del ejercicio 10 de la Pr�ctica 10 desde una aplicaci�n Java. 
		10.	Realizar un procedimiento y una funci�n que dado un c�digo de concesionario devuelve el n�mero ventas que se han realizado en el mismo.
		
		
		6.1. Funcion
	 */
	public static void exercise6_1() throws SQLException {		
		System.out.println("Please, enter concessioner code: ");	
		String cifc = ReadString();		
		StringBuilder query = new StringBuilder();
		query.append("{? = call FUNCTION10(?)}");
		query.append("begin ?:= FUNCTION10(?); end;");		
		CallableStatement cst = con.prepareCall(query.toString());
		cst.registerOutParameter(1, Types.INTEGER,0);
		cst.setString(2,cifc);
		cst.execute();
		int ventas = cst.getInt(1);
		System.out.println("Ventas\n" + ventas);
		cst.close();
		con.close();
	}
	
	/*	
		6.1. Procedimiento
	 */
	public static void exercise6_2() throws SQLException {		
		System.out.println("################### EXERCISE 6-2 ###################");
		System.out.println("Please, enter concessioner code: ");	
		String cifc = ReadString();
		StringBuilder query = new StringBuilder();
		query.append("{call PROCEDURE10(?,?)}");	
		query.append("begin PROCEDURE10(?,?); end;");		
		CallableStatement cst = con.prepareCall(query.toString());
		cst.setString(1, cifc);
		cst.registerOutParameter(2,Types.INTEGER);
		//cst.executeQuery(); //Not supported in HSQLDB
		cst.execute();		
		System.out.println("Ventas\n" + cst.getInt(2));
		cst.close();
	}
	
	/*
		7. Invocar la funci�n y el procedimiento del ejercicio 11 de la Pr�ctica 10 desde una aplicaci�n Java. 
			11. Realizar un procedimiento y una funci�n que dada una ciudad que se le pasa como par�metro devuelve el n�mero de clientes de dicha ciudad.
		
		
		7.1. Funcion
	 */
	public static void exercise7_1() {		
			
	}	
	
	/*
		7.2. Procedimiento
	 */
	public static void exercise7_2() {		
				
	}
		
	@SuppressWarnings("resource")
	private static String ReadString(){
		return new Scanner(System.in).nextLine();		
	}
	
	@SuppressWarnings("resource")
	private static int ReadInt(){
		return new Scanner(System.in).nextInt();			
	}	
}
