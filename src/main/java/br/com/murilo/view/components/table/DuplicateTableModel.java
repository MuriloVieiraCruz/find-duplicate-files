package br.com.murilo.view.components.table;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.Collection;
import java.util.List;

public class DuplicateTableModel extends AbstractTableModel {

    private final List<File> files;

    public DuplicateTableModel(List<File> files) {
        this.files = files;
    }

    public File get(int rowIndex) {
        return files.get(rowIndex);
    }

    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "File";
        }
        throw new IllegalArgumentException("Invalid index");
    }

    @Override
    public int getRowCount() {
        return files.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        File file = files.get(rowIndex);
        if (columnIndex == 0) {
            return file.getAbsolutePath();
        }
        throw new IllegalArgumentException("Invalid index");
    }

    public void remove(Collection<File> rowsIndexes) {
        this.files.removeAll(rowsIndexes);
    }

    public boolean isEmpty() {
        return files.isEmpty();
    }

}
