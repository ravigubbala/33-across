package com.mywork.main;

import java.io.InputStream;
import java.io.InputStreamReader;

import au.com.bytecode.opencsv.CSVReader;

public class FetchTable {

	static Table from(String fileName, char delim) {

		Table table = new Table();

		// Get file from resources folder
		ClassLoader classLoader = new FetchTable().getClass().getClassLoader();
//		File file = new File(classLoader.getResource(fileName).getFile());
		InputStream inputstream = classLoader.getResourceAsStream(fileName);
		InputStreamReader inreader = new InputStreamReader(inputstream);

		try {
			CSVReader reader = new CSVReader(inreader, delim, '"', 1);

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
