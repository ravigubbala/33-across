package com.mywork.main;

import static com.mywork.main.QueryBuilder.column;
import static com.mywork.main.QueryBuilder.select;

public class Main {

	public static void main(String[] args) {
		//build a Table from ccds2.tsv file
		Table tsv = FetchTable.from("ccds2.tsv", '\t');
		tsv.setColumns(new String[] { "ID", "COUNTRY", "F1", "F2", "F3" });

		//build a Table from ccds1.csv file
		Table csv = FetchTable.from("ccds1.csv", ',');
		csv.setColumns(new String[] { "ID", "UA" });

		String[] europeanCountries = new String[] { "AL", "AD", "AM", "AT", "BY", "BE", "BA", "BG", "CH", "CY", "CZ",
				"DE", "DK", "EE", "ES", "FO", "FI", "FR", "GB", "GE", "GI", "GR", "HU", "HR", "IE", "IS", "IT", "LT",
				"LU", "LV", "MC", "MK", "MT", "NO", "NL", "PO", "PT", "RO", "RU", "SE", "SI", "SK", "SM", "TR", "UA",
				"VA" };

		try {
		select("t1.ID", "t1.COUNTRY", "t2.UA", "t1.F1", "t1.F2", "t1.F3")
			.from(tsv.join(csv))
				.where(() -> {
					try {
						column("t1.COUNTRY").notIn(europeanCountries);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}