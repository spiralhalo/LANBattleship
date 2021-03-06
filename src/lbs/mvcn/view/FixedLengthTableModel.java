/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.view;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author spiralhalo
 */
public class FixedLengthTableModel extends AbstractTableModel {
    
    private Object[][] source;
    private String[] columnName;
    
    public FixedLengthTableModel (Object[][] source, String[] columnName) {
        
        this.columnName = columnName;
        this.source = source;
            
    }
    
    @Override
    public String getColumnName(int column) {
        
        if (columnName != null && column < columnName.length)
            return columnName[column];
        
        String result = "";
        for (; column >= 0; column = column / 26 - 1) {
            result = (char)((char)(column%26)+'A') + result;
        }
        return result;
        
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        try{
            return source[0][columnIndex].getClass();
        } catch (NullPointerException e){
            return Object.class;
        }
    }
    
    @Override
    public int getRowCount() {
        return source.length;
    }

    @Override
    public int getColumnCount() {
        return columnName.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return source[rowIndex][columnIndex];
    }
    
}
