package edu.rehan.studentdb.ui;

import edu.rehan.studentdb.dao.StudentDAO;
import edu.rehan.studentdb.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {

    private final JTextField tfFirst = new JTextField(15);
    private final JTextField tfLast = new JTextField(15);
    private final JTextField tfAge = new JTextField(5);
    private final JTextField tfEmail = new JTextField(20);
    private final JTextField tfSearchId = new JTextField(5);

    private final JButton btnAdd = new JButton("Add Student");
    private final JButton btnClear = new JButton("Clear");
    private final JButton btnView = new JButton("View Students");
    private final JButton btnSearch = new JButton("Search");

    private final JTextArea taStatus = new JTextArea(5, 50);
    private final StudentTableModel tableModel = new StudentTableModel();
    private final JTable table = new JTable(tableModel);
    private final StudentDAO studentDAO = new StudentDAO();

    public MainFrame() {
        super("StudentDB Manager");
        initializeFrame();
        setupUI();
        attachHandlers();
    }

    private void initializeFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void setupUI() {
        // Header
        JLabel header = new JLabel("StudentDB Manager", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // Left panel: Input + Search
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Student"));
        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(tfFirst);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(tfLast);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(tfAge);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(tfEmail);
        inputPanel.add(btnAdd);
        inputPanel.add(btnClear);
        leftPanel.add(inputPanel);

        leftPanel.add(Box.createVerticalStrut(20));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Student"));
        searchPanel.add(new JLabel("Student ID:"));
        searchPanel.add(tfSearchId);
        searchPanel.add(btnSearch);
        searchPanel.add(btnView);
        leftPanel.add(searchPanel);

        add(leftPanel, BorderLayout.WEST);

        // Center panel: Table
        JScrollPane tableScroll = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        JTableHeader headerTable = table.getTableHeader();
        headerTable.setFont(new Font("Arial", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        add(tableScroll, BorderLayout.CENTER);

        // Status panel
        JScrollPane statusScroll = new JScrollPane(taStatus);
        taStatus.setEditable(false);
        statusScroll.setBorder(BorderFactory.createTitledBorder("Status"));
        add(statusScroll, BorderLayout.SOUTH);
    }

    private void attachHandlers() {
        btnAdd.addActionListener(this::addStudent);
        btnClear.addActionListener(e -> clearFields());
        btnView.addActionListener(e -> loadStudents());
        btnSearch.addActionListener(this::searchStudent);
    }

    private void clearFields() {
        tfFirst.setText("");
        tfLast.setText("");
        tfAge.setText("");
        tfEmail.setText("");
    }

    private void addStudent(ActionEvent e) {
        String f = tfFirst.getText().trim();
        String l = tfLast.getText().trim();
        String a = tfAge.getText().trim();
        String em = tfEmail.getText().trim();

        if (f.isEmpty() || l.isEmpty()) { status("First and Last name required."); return; }

        Integer age = null;
        if (!a.isEmpty()) {
            try { age = Integer.parseInt(a); }
            catch (NumberFormatException ex) { status("Age must be a number."); return; }
        }

        Student s = new Student(f, l, age, em);
        try {
            Student saved = studentDAO.addStudent(s);
            status("Added student ID: " + saved.getId());
            loadStudents();
            clearFields();
        } catch (Exception ex) {
            status("Error: " + ex.getMessage());
        }
    }

    private void loadStudents() {
        try {
            tableModel.setStudents(studentDAO.getAllStudents());
            status("Loaded students.");
        } catch (Exception ex) {
            status("Error: " + ex.getMessage());
        }
    }

    private void searchStudent(ActionEvent e) {
        try {
            int id = Integer.parseInt(tfSearchId.getText().trim());
            Student s = studentDAO.findStudentById(id);
            if (s != null) status("Found: " + s.getFirstName() + " " + s.getLastName());
            else status("No student found with ID: " + id);
        } catch (NumberFormatException ex) {
            status("Invalid ID.");
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void status(String msg) {
        taStatus.append(msg + "\n");
        taStatus.setCaretPosition(taStatus.getDocument().getLength());
    }
}
