package com.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Main {



	private static final Logger logger = LoggerFactory.getLogger("");
	private static final String JDBC_DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
	private static String connectionUrl;

	public static void main(String[] args) throws IOException {
		System.setProperty("log4j2.debug","true");
	String url ="jdbc:hive2://127.0.0.1:10000/;ssl=false";
		if (args.length < 1) {
			logger.error("1 arg is required :\n\t- connectionurl ex: jdbc:hive2://hiveserver:10000/;ssl=false");
			System.err.println("1 arg is required :\n\t-connectionurl  ex: jdbc:hive2://hiveserver:10000/;ssl=false");

		}else
		{
			url=args[0];
		}
		// Get url Connection
		connectionUrl = url;

		// Init Connection
		Connection con = null;

		//==== Select data from Hive Table
		String sqlStatementDrop = "DROP TABLE IF EXISTS helloworld";
		String sqlStatementCreate = "CREATE TABLE helloworld (message String) STORED AS PARQUET";
		String sqlStatementInsert = "INSERT INTO helloworld VALUES (\"helloworld\")";
		String sqlStatementSelect = "SELECT * from helloworld";
		try {
			// Set JDBC Hive Driver
			Class.forName(JDBC_DRIVER_NAME);
			// Connect to Hive
			con = DriverManager.getConnection(connectionUrl,"hdfs","");
			// Init Statement
			Statement stmt = con.createStatement();
			// Execute DROP TABLE Query
			stmt.execute(sqlStatementDrop);
			System.out.println("Drop Hive table : OK");
			// Execute CREATE Query
			stmt.execute(sqlStatementCreate);
			System.out.println("Create Hive table : OK");
			// Execute INSERT Query
			stmt.execute(sqlStatementInsert);
			System.out.println("Insert into Hive table : OK");
			// Execute SELECT Query
			ResultSet rs = stmt.executeQuery(sqlStatementSelect);
			while(rs.next()) {
				System.out.println(rs.getString(1));
			}
			System.out.println("Select from Hive table : OK");

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				// swallow
			}
		}

	}
}
