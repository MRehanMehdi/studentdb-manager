package edu.rehan.studentdb.ui;

import edu.rehan.studentdb.model.Student;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StudentTableModel extends AbstractTableModel {

    private final List<Student> students = new ArrayList<>();
    private final String[] columns = {"ID", "First Name", "Last Name", "Age", "Email"};

    public void setStudents(List<Student> list) {
        students.clear();
        if (list != null)
            students.addAll(list);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student s = students.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> s.getId();
            case 1 -> s.getFirstName();
            case 2 -> s.getLastName();
            case 3 -> s.getAge();
            case 4 -> s.getEmail();
            default -> "";
        };
    }
}
