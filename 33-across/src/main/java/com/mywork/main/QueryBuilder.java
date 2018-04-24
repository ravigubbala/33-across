package com.mywork.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Singleton class used to build expression for fetching desired results
 * 
 * @author ravi.kiran.gubbala
 *
 */
public class QueryBuilder {

	// columns selected for output
	String[] selectedColumns;
	// table quieried in the current expression
	private Table table;
	// conditons on which to filter results
	HashMap<String, String> conditionMap = new HashMap<String, String>();

	void put(String key, String value) {
		conditionMap.put(key, value);
	}

	HashMap<String, String> getConditionMap() {
		return conditionMap;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public String[] getSelectedColumns() {
		return selectedColumns;
	}

	public void setSelectedColumns(String[] selectedColumns) {
		this.selectedColumns = selectedColumns;
	}

	public static QueryBuilder select(String... columns) {
		getSingletonInstance().setSelectedColumns(columns);
		return getSingletonInstance();
	}

	public QueryBuilder from(Table table) {
		getSingletonInstance().setTable(table);
		return getSingletonInstance();
	}

	private static QueryBuilder singletonInstance;

	// SingletonExample prevents any other class from instantiating
	private QueryBuilder() {
	}

	// Providing Global point of access
	public static QueryBuilder getSingletonInstance() {
		if (null == singletonInstance) {
			singletonInstance = new QueryBuilder();
		}
		return singletonInstance;
	}

	public void where(Condition condition) throws Exception {
		condition.addConditions();
		for (Entry<String, String> entry : getSingletonInstance().getConditionMap().entrySet()) {
			getSingletonInstance()
					.setTable(getSingletonInstance().getTable().filterBy(entry.getKey(), entry.getValue()));
		}
		getSingletonInstance().getTable().select(getSingletonInstance().getSelectedColumns());
	}

	public static Column column(String column) {

		return new Column(column);
	}

	public void equalTo(Column column1, Column column2) {
		// TODO
	}

	public void equalTo(Column column, String value) {
		conditionMap.put(column.name, value);
	}

	public void notIn(Column column, ArrayList<String> values) throws Exception {
		getSingletonInstance().getTable().notIn(column.name, values);
	}

}

@FunctionalInterface
interface Condition {
	void addConditions();
}

class Column {
	String name;

	Column(String name) {
		this.name = name;
	}

	void equalTo(Column column) {
		QueryBuilder.getSingletonInstance().equalTo(this, column);
	}

	void equalTo(String value) {
		QueryBuilder.getSingletonInstance().equalTo(this, value);
	}

	public void notIn(String[] values) throws Exception {
		QueryBuilder.getSingletonInstance().notIn(this, new ArrayList<String>(Arrays.asList(values)));
	}
}
