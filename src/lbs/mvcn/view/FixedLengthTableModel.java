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
 * @author lenovo z40
 */
public class FixedLengthTableModel extends AbstractTableModel {
    
    private Object[][] source;
    
    public FixedLengthTableModel (Object[][] source) {
        
        if(source.length != 0)
            this.source = source;
        else
            this.source = new Object[1][1];
            
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return source[0][columnIndex].getClass();
    }
    
    @Override
    public int getRowCount() {
        return source.length;
    }

    @Override
    public int getColumnCount() {
        return source[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return source[rowIndex][columnIndex];
    }
    
}
