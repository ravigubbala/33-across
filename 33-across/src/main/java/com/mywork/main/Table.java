package com.mywork.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**Object that holds data read from file
 * @author ravi.kiran.gubbala
 *
 */
public class Table {
	String[] columns;
	HashMap<Integer, Integer> widths;

	/**
	 * returns column names as a string array
	 * 
	 * @return
	 */
	public String[] getColumns() {
		return columns;
	}

	/**
	 * this method is used to specify schema of the file
	 * 
	 * @param columns
	 */
	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	// varaible to hold table data
	ArrayList<String[]> data = new ArrayList<String[]>();

	/**
	 * Add input String array as new row into the table
	 * 
	 * @param row
	 */
	public void addRow(String[] row) {
		data.add(row);
	}

	/**
	 * get all rows in this table
	 * 
	 * @return
	 */
	public ArrayList<String[]> getRows() {
		return data;
	}

	/**
	 * returns a map with "column index-column width" values
	 * 
	 * @return
	 */
	HashMap<Integer, Integer> calcColumnWidths() {
		HashMap<Integer, Integer> widths = new HashMap<Integer, Integer>();
		// initializing widths map with column widths
		for (int i = 0; i < columns.length; i++) {
			widths.put(i, columns[i].length());
		}

		for (int c = 0; c < columns.length; c++) {
			for (String[] row : data) {
				if (widths.get(c) < row[c].length())
					widths.put(c, row[c].length());
			}
		}

		return widths;
	}

	/**
	 * returns the index of given column in this table
	 * 
	 * @param column
	 * @return
	 * @throws Exception
	 */
	public int getIndex(String column) throws Exception {
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].equalsIgnoreCase(column))
				return i;
		}
		throw new Exception("Unknown column :" + column);
	}

	/**
	 * prints all the columns of this table to console
	 * 
	 * @throws Exception
	 */
	public void select() throws Exception {

		if (widths.isEmpty()) {
			widths = calcColumnWidths();
		}
		for (String column : columns) {
			int col = getIndex(column);
			System.out.print(String.format("%1$-"
					+ ((widths.get(col) - columns[col].length()) > 0 ? (widths.get(col)) : columns[col].length()) + "s",
					columns[col].substring(columns[col].indexOf(".") + 1)) + "\t");
		}
		System.out.println();
		for (String[] row : getRows()) {
			for (String column : columns) {
				int col = getIndex(column);
				System.out.print(String.format("%1$-"
						+ ((widths.get(col) - columns[col].length()) > 0 ? (widths.get(col)) : columns[col].length())
						+ "s", row[col]) + "\t");
			}
			System.out.println();
		}
	}

	/**
	 * prints the given columns of this table to console
	 * 
	 * @param selectedColumns
	 * @throws Exception
	 */
	public void select(String... selectedColumns) throws Exception {
		widths = calcColumnWidths();

		if (selectedColumns.length > 0) {
			ArrayList<Integer> indexLst = new ArrayList<Integer>();
			for (String column : selectedColumns) {
				indexLst.add(getIndex(column));
			}

			for (Integer col : indexLst) {
				System.out.print(String
						.format("%1$-" + ((widths.get(col) - columns[col].length()) > 0 ? (widths.get(col))
								: columns[col].length()) + "s", columns[col].substring(columns[col].indexOf(".") + 1))
						+ "\t");
			}
			System.out.println();
			for (String[] row : getRows()) {
				for (Integer col : indexLst) {
					System.out.print(String.format("%1$-" + ((widths.get(col) - columns[col].length()) > 0
							? (widths.get(col)) : columns[col].length()) + "s", row[col]) + "\t");
				}
				System.out.println();
			}

		} else {
			select();
		}
	}

	/**
	 * wrapper on crossMultiply method
	 * 
	 * @param table
	 * @return
	 */
	public Table join(Table table) {
		Table joinTable = crossMultiply(this, table);
		return joinTable;
	}

	/**
	 * calculates join of input two tables based on common columns
	 * 
	 * @param table1
	 * @param table2
	 * @return
	 */
	private Table crossMultiply(Table table1, Table table2) {
		ArrayList<String> jtCols = new ArrayList<String>();
		Table jt = new Table();
		for (String col : table1.getColumns()) {
			jtCols.add("t1." + col);
		}
		for (String col : table2.getColumns()) {
			jtCols.add("t2." + col);
		}
		jt.setColumns(jtCols.toArray(new String[0]));

		HashMap<String, Integer[]> comCols = commonColumns(table1, table2);
		ArrayList<String[]> rows1 = table1.getRows();
		ArrayList<String[]> rows2 = table2.getRows();

		for (String[] tab1Row : rows1) {
			for (String[] tab2Row : rows2) {

				int matched = 0;
				for (Entry<String, Integer[]> entry : comCols.entrySet()) {

					String val1 = tab1Row[entry.getValue()[0]];
					String val2 = tab2Row[entry.getValue()[1]];
					if (val1.equalsIgnoreCase(val2)) {
						matched++;
					}
				}
				if (matched == comCols.size()) {
					ArrayList<String> jtRow = new ArrayList<String>();
					jtRow.addAll(Arrays.asList(tab1Row));
					jtRow.addAll(Arrays.asList(tab2Row));
					jt.addRow(jtRow.toArray(new String[0]));
				}
			}
		}

		return jt;
	}

	/**
	 * find common columns in both the tables
	 * 
	 * @param table1
	 * @param table2
	 * @return
	 */
	private HashMap<String, Integer[]> commonColumns(Table table1, Table table2) {
		HashMap<String, Integer[]> comCols = new HashMap<String, Integer[]>();
		String[] tab1Cols = table1.getColumns();
		String[] tab2Cols = table2.getColumns();
		for (int t1 = 0; t1 < tab1Cols.length; t1++) {
			for (int t2 = 0; t2 < tab2Cols.length; t2++) {

				if (tab1Cols[t1].equalsIgnoreCase(tab2Cols[t2])) {
					comCols.put(tab1Cols[t1], new Integer[] { t1, t2 });
				}
			}
		}
		return comCols;

	}

	/**
	 * filters out all rows with specified value in given column. similar to
	 * "column1=value1" type conditons in sql where clause
	 * 
	 * @param column
	 * @param value
	 * @return
	 * @throws Exception
	 */
	Table filterBy(String column, String value) throws Exception {
		for (Iterator<String[]> iterator = data.iterator(); iterator.hasNext();) {
			String[] row = iterator.next();
			if (!row[getIndex(column)].equalsIgnoreCase(value)) {
				iterator.remove();
			}
		}

		return this;
	}

	/**
	 * filters out all rows with specified list values in given column. similar
	 * to "not in" expression in sql
	 * 
	 * @param column
	 * @param values
	 * @return
	 * @throws Exception
	 */
	Table notIn(String column, ArrayList<String> values) throws Exception {
		for (Iterator<String[]> iterator = data.iterator(); iterator.hasNext();) {
			String[] row = iterator.next();
			if (values.contains(row[getIndex(column)])) {
				iterator.remove();
			}
		}

		return this;
	}

}
