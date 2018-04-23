package com.mywork.main;

import java.io.File;
import java.io.FileReader;

import au.com.bytecode.opencsv.CSVReader;

public class FetchTable {

	static Table from(String fileName, char delim) {

		Table table = new Table();

		// Get file from resources folder
		ClassLoader classLoader = new FetchTable().getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		try {
			CSVReader reader = new CSVReader(new FileReader(file), delim, '"', 1);

			// Read CSV line by line and use the string array as you want
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				if (nextLine != null) {
					// Verifying the read data here
					table.addRow(nextLine);
				}
			}
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;

	}

}
