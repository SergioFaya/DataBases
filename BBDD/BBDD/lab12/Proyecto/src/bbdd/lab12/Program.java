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
		exercise1_1();
		exercise1_2();
		exercise2();
	
		
		st.close();
		con.close();
	}

	/*
		1. Mostrar por pantalla los resultados de las consultas 21 y 32 de la Pr�ctica 2. 
		1.1. 21. Obtener el nombre y el apellido de los clientes que han adquirido un coche en un concesionario de Madrid, el cual dispone de coches del modelo gti.
	 */
	public static void exercise1_1() throws SQLException {
		st = con.createStatement();
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
		String color = ReadString();
		ResultSet rs3 = st.executeQuery("select  nombrem FROM marcas m,coches c,marco mc ,ventas v "
				+ "where m.cifm = mc.cifm and mc.CODCOCHE = c.CODCOCHE select apellido from clientes where ciudad ='madrid' "
				+ "and c.CODCOCHE = v.codcoche and v.color = '"
				+ color
				+ "'");
		
		System.out.println("CIFC\tNOMBREC\tCIUDADC");
		while (rs3.next()){
			String nombrem = rs3.getString("nombrem");
			
			System.out.println(nombrem);
			
		}
		
		
		rs3.close();
	}
	
	/*
		3. Realizar una aplicaci�n en Java para ejecutar la consulta 27 de la Pr�ctica 2 de forma que los l�mites la cantidad de coches sean introducidos por el usuario. 
			27. Obtener el cifc de los concesionarios que disponen de una cantidad de coches comprendida entre dos cantidades introducidas por el usuario, ambas inclusive.
	 */
	public static void exercise3() {
		
	}
	
	/*
		4. Realizar una aplicaci�n en Java para ejecutar la consulta 24 de la Pr�ctica 2 de forma que tanto la ciudad del concesionario como el color sean introducidos por el usuario. 
			24.	Obtener los nombres de los clientes que no han comprado coches de un color introducido por el usuario en concesionarios de una ciudad introducida por el usuario.
	 */
	public static void exercise4() {
		
	}
	
	/*
		5. Realizar una aplicaci�n en Java que haciendo uso de la instrucci�n SQL adecuada: 
		5.1. Introduzca datos en la tabla coches cuyos datos son introducidos por el usuario
	 */
	public static void exercise5_1() {
		
	}
	
	/*
		5.2. Borre un determinado coche cuyo c�digo es introducido por el usuario. 
	 */
	public static void exercise5_2() {
		
	}
	
	/*	 
		5.3. Actualice el nombre y el modelo para un determinado coche cuyo c�digo es introducido por el usuario.
	 */
	public static void exercise5_3() {		
		
	}
	
	/*
		6. Invocar la funci�n y el procedimiento del ejercicio 10 de la Pr�ctica 10 desde una aplicaci�n Java. 
			10.	Realizar un procedimiento y una funci�n que dado un c�digo de concesionario devuelve el n�mero ventas que se han realizado en el mismo.
		6.1. Funcion
	 */
	public static void exercise6_1() {		
			
	}
	
	/*	
		6.1. Procedimiento
	 */
	public static void exercise6_2() {		
			
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