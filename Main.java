package org.generation.italy;

import java.sql.*;
import java.util.Scanner;


public class Main {
	
	private final static String URL = "jdbc:mysql://localhost:3306/db-nations";
	private final static String USER = "root";
	private final static String PASSWORD = "rootpassword";

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		
		try(Connection con = DriverManager.getConnection(URL, USER, PASSWORD)){
			
			System.out.print("Search: ");
			String search = scan.nextLine();
			search = "%" + search + "%";
			
			String query = "select c.country_id , c.name , r.name , c2.name from countries c join regions r on r.region_id = c.region_id join continents c2 on c2.continent_id = r.continent_id where c.name like ? order by c.name ;";
			
			try(PreparedStatement ps = con.prepareStatement(query)){
				ps.setString(1, search);
				try(ResultSet rs = ps.executeQuery()){
					
					if(rs.next()) {
						System.out.format("%3s %44s %29s %19s%n" , "ID" , "COUNTRY" , "REGION" , "CONTINENT");
						
							do {
								System.out.format("%3d", rs.getInt(1));
								System.out.format("%45s", rs.getString(2));
								System.out.format("%30s", rs.getString(3));
								System.out.format("%20s%n", rs.getString(4));
							} while (rs.next());
								
					}else {
						throw new Exception("No results");
					}
				}
			}
			
		}catch(SQLException s){
			System.out.println("Error");
			System.out.println(s.getMessage());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		scan.close();
	}

}
