package bbdd.testJDBC;
import java.util.Scanner;

import javafx.animation.KeyValue.Type;

import java.sql.*;

public class Program {
	private Connection con;
	private Statement st;
	
	public Program() throws SQLException {
		con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/labdb","SA","");
		st = con.createStatement();
	}
	public static void main(String[] args) {		
		try {
			Program p = new Program();
			//p.exercise1();
			p.exercise2();
			//p.exercise3();
		} catch (SQLException e) {			
			System.err.println("SQL Exception " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/*	 
		1. Crear un m�todo que aumente el n�mero de transformadores de aquellas estaciones en las que alguna de las centrales nucleares que les entregan energ�a genera m�s de cierto volumen de residuos. El n�mero de transformadores que se instalen nuevos as�como el volumen de residuos ser�n par�metros.
	 */
	public void exercise1() throws SQLException{		
		System.out.println("################### EXERCISE 1 ###################");
		StringBuilder query = new StringBuilder();
		query.append("update estacion e set e.transformadores = e.transformadores + ? ");
		query.append("where e.enombre in ( select s.ENOMBRE ");
		query.append("from estacion s, entrega e, nuclear n ");
		query.append("where s.ENOMBRE = e.ENOMBRE and e.PNOMBRE = n.PNOMBRE ");
		query.append("and (n.RESIDUOS > ?))");
		PreparedStatement ps = con.prepareStatement(query.toString());
		int param1,param2;
		System.out.println("Introduce the number of transformers you want to add");
		param1 = ReadInt();
		System.out.println("Introduce the 'residuos' bound");
		param2 = ReadInt();
		ps.setInt(1, param1);
		ps.setInt(2, param2);
		ps.executeUpdate();
		
	}
	
	/*
	 * 2. Crear un m�todo que muestre por pantalla las redes de distribuci�n junto con la longitud total de todas sus l�neas 
	 ordenadas de mayor a menor por dicha longitud siempre y cuando
	 est�n formadas por m�s de una l�nea. Para cada una de dichas redes debe mostrarse
	 su estaci�n de cabecera y el n�mero de redes a las que env�a energ�a. Adem�s debe mostrase para cada una de ellas las 
	 redes a las que env�a energ�a junto con sus estaciones de cabecera y el volumen de energ�a que les env�a.
		RedEnvia:  numred longitud enombrecabecera nredes<-- Ordenados por longitud de mayor a menor
		  RedRecibe: numred enombrecabecera volumen
		  RedRecibe: numred enombrecabecera volumen
		  �
		RedEnvia:  numred longitud enombrecabecera nredes <-- Ordenados por longitud de mayor a menor
		�
	 */
	
	public void exercise2() throws SQLException {		
		System.out.println("################### EXERCISE 2 ###################");		
		System.out.println("‐NetworkSend:networknumber-total_length-header_sname-numberofnetworks");
		String q1_1 = "select r.NUMRED,sum(longitud) suma_redes "
				+ "from RED_DISTRIBUCION r,linea l "
				+ "where r.NUMRED = l.NUMRED "
				+ "group by r.NUMRED "
				+ "having count(l.NLINEA) > 1 "
				+ "order by sum(longitud) desc";
		
		String q1_2 = "select e.enombre "
				+ "from ESTACION e, RED_DISTRIBUCION rd "
				+ "where e.ENOMBRE = rd.ENOMBRE and rd.NUMRED = ?";
		
		String q1_3 = "select count(env.NUMRED_ENVIA) "
				+ "FROM RED_DISTRIBUCION r,ENVIA_ENERGIA env "
				+ "where r.NUMRED = env.NUMRED_ENVIA "
				+ "and r.numred = ?";
		
		String q2 = "select rd.NUMRED, e.ENOMBRE, env.VOLUMEN "
				+ "from RED_DISTRIBUCION rd, ENVIA_ENERGIA env, estacion e "
				+ "where rd.NUMRED = env.NUMRED_RECIBE "
				+ "and rd.ENOMBRE = e.ENOMBRE and rd.NUMRED = ?";
		
		ResultSet rs1_1  = st.executeQuery(q1_1);
		while(rs1_1.next()){
			String numred = rs1_1.getString(1);
			String length = rs1_1.getString(2);
			PreparedStatement ps1_2 =con.prepareStatement(q1_2);
			ps1_2.setString(1, numred);
			ResultSet rs2_2 = ps1_2.executeQuery();
			while(rs2_2.next()){
				String header_sname = rs2_2.getString(1);
				PreparedStatement ps1_3 =con.prepareStatement(q1_3);
				ps1_3.setString(1, numred);
				ResultSet rs3 =  ps1_3.executeQuery();
				while(rs3.next()){
					String numberOfNetworks = rs3.getString(1);
					System.out.println("-NetworkSend:"+numred +"\t"+ length +"\t" + header_sname +"\t"+numberOfNetworks );
					PreparedStatement ps2 = con.prepareStatement(q2);
					ps2.setString(1, numred);
					ResultSet rs2 = ps2.executeQuery();
					while(rs2.next()){
						String numredReceive = rs2.getString(1);
						String headerName = rs2.getString(2);
						String volume = rs2.getString(3);
						System.out.println("\tNetworkReceive:"+numredReceive+"\t"+headerName+"\t"+volume);
					}
				}
			}
		}
	}

	
	/*	
		3. Invocar desde Java al procedimiento informacion_subestacion  que dado el nombre de
		 una subestaci�n devuelve la l�nea que abastece a dicha estaci�n. Adem�s devuelve la longitud de dicha l�nea. Mostrar por pantalla la informaci�n obtenida.
	 		PROCEDURE informacion_subestacion
			(nombresubestacion IN subestacion.nsubestacion%TYPE, 
			numerolinea OUT linea.nlinea%TYPE, 
			numerored OUT linea.numred%TYPE, 
			longitudlinea OUT linea.longitud%TYPE
			)

	 
	 */
	public void exercise3() throws SQLException {		
		System.out.println("################### EXERCISE 3 ###################");		
		String  query= "{call INFORMACION_SUBESTACION(?,?,?,?)}";
		CallableStatement cal = con.prepareCall(query);
		String param1;
		int param2,param3;
		float param4 ;
		System.out.println("Introduce the substation name");
		param1 = ReadString();
		cal.setString(1,param1);
		cal.registerOutParameter(2, Types.INTEGER);
		cal.registerOutParameter(3, Types.INTEGER);
		cal.registerOutParameter(4, Types.DECIMAL);
		cal.execute();
		param2 = cal.getInt(2);
		param3 = cal.getInt(3);
		param4 = cal.getFloat(4);
		System.out.println(param2+" "+param3+" "+param4);
	}
	

    // ------------------- DATABASE UTILS -------------------
	
	
	@SuppressWarnings("resource")
	private static String ReadString(){
		return new Scanner(System.in).nextLine();		
	}
	
	@SuppressWarnings("resource")
	private static int ReadInt(){
		return new Scanner(System.in).nextInt();			
	}

    @SuppressWarnings("resource")
	private static Double ReadDouble(){
		return new Scanner(System.in).nextDouble();			
	}	
}
