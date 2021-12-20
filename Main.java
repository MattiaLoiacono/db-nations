package org.generation.italy;

import java.sql.*;
import java.util.Scanner;


public class Main {
	
	private final static String URL = "jdbc:mysql://localhost:3306/db-nations";
	private final static String USER = "root";
	private final static String PASSWORD = "";

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		
		try(Connection con = DriverManager.getConnection(URL, USER, PASSWORD)){
			
			System.out.print("Search: ");
			String search = scan.nextLine();
			search = "%" + search + "%";
			
			String query1 = "select c.country_id , c.name , r.name , c2.name from countries c join regions r on r.region_id = c.region_id join continents c2 on c2.continent_id = r.continent_id where c.name like ? order by c.name ;";
			
			try(PreparedStatement ps = con.prepareStatement(query1)){
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
				
				System.out.print("Choose a country id: ");
				int id = scan.nextInt();
				String query2 = "select c.name , l.`language`, max(cs.`year`), cs.population , cs.gdp from countries c join country_stats cs on cs.country_id = c.country_id join country_languages cl on cl.country_id = cs.country_id join languages l on l.language_id = cl.language_id where c.country_id = ? group by l.language_id;";
				
				try(PreparedStatement ps2 = con.prepareStatement(query2)){
					ps2.setInt(1, id);
					try(ResultSet rs2 = ps2.executeQuery()){
						
						if(rs2.next()) {
							System.out.println("Details for country: " + rs2.getString(1));
							System.out.print("Languages: ");
							
							do {
								System.out.print(rs2.getString(2));
								if(!rs2.isLast()) {
									System.out.print(", ");
								}else {
									
									System.out.println("%nMost recent stats");
									System.out.println("Year: "+ rs2.getInt(3));
									System.out.println("Population: "+ rs2.getLong(4));
									System.out.println("GDP: "+ rs2.getLong(5));
								}
							}while(rs2.next());
							
						}else {
							throw new Exception("Invalid ID");
						}
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
