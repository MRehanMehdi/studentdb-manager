package edu.rehan.studentdb.ui;

import edu.rehan.studentdb.dao.StudentDAO;
import edu.rehan.studentdb.model.Student;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainFrame extends JFrame {

    // Colors
    private final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private final Color SECONDARY_COLOR = new Color(46, 204, 113);
    private final Color ACCENT_COLOR = new Color(155, 89, 182);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color PANEL_BACKGROUND = Color.WHITE;
    private final Color TABLE_HEADER_COLOR = new Color(41, 128, 185);
    private final Color TABLE_EVEN_ROW = new Color(248, 249, 250);
    private final Color TABLE_ODD_ROW = Color.WHITE;
    private final Color BUTTON_HOVER = new Color(41, 128, 185);

    // Components
    private final JTextField tfFirst = createStyledTextField();
    private final JTextField tfLast = createStyledTextField();
    private final JTextField tfAge = createStyledTextField();
    private final JTextField tfEmail = createStyledTextField();
    private final JTextField tfSearchId = createStyledTextField();

    private final JButton btnAdd = createPrimaryButton("Add Student");
    private final JButton btnClear = createSecondaryButton("Clear");
    private final JButton btnView = createPrimaryButton("View Students");
    private final JButton btnSearch = createPrimaryButton("Search");

    private final JTextArea taStatus = new JTextArea(5, 50);
    private final StudentTableModel tableModel = new StudentTableModel();
    private final JTable table = new JTable(tableModel);
    private final StudentDAO studentDAO = new StudentDAO();

    public MainFrame() {
        super("Student DB Manager");
        initializeFrame();
        setupUI();
        loadStudents(); // Load students on startup
    }

    private void initializeFrame() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(BACKGROUND_COLOR);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700); // Slightly increased height
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void setupUI() {
        // Header
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 15, 15, 15));

        // Left panel (Input + Search)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.add(createInputPanel());
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(createSearchPanel());

        // Center panel (Table)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(PANEL_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(5, 5, 5, 5)
        ));
        setupTable();
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(null);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(PANEL_BACKGROUND);
        tablePanel.add(createTableHeader(), BorderLayout.NORTH);
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Status panel
        add(createStatusPanel(), BorderLayout.SOUTH);

        // Event handlers
        attachHandlers();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("StudentDB Manager");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Manage student records efficiently");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(240, 240, 240));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(PRIMARY_COLOR);
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(subtitle);

        header.add(textPanel, BorderLayout.WEST);

        JLabel iconLabel = new JLabel("👨‍🎓");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setForeground(new Color(255, 255, 255, 180));
        header.add(iconLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel title = new JLabel(" Add New Student");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(PRIMARY_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(createInputLabel("First Name:"), gbc);
        gbc.gridx = 1; panel.add(tfFirst, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(createInputLabel("Last Name:"), gbc);
        gbc.gridx = 1; panel.add(tfLast, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(createInputLabel("Age:"), gbc);
        gbc.gridx = 1; panel.add(tfAge, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(createInputLabel("Email:"), gbc);
        gbc.gridx = 1; panel.add(tfEmail, gbc);

        // Buttons panel
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(0, 0, 0, 0)); // Transparent
        buttonPanel.setOpaque(false);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnClear);
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel title = new JLabel(" Search Student");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(ACCENT_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel searchFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchFieldPanel.setBackground(PANEL_BACKGROUND);
        searchFieldPanel.add(new JLabel("Student ID:"));
        tfSearchId.setPreferredSize(new Dimension(100, 35));
        searchFieldPanel.add(tfSearchId);
        searchFieldPanel.add(btnSearch);

        JPanel viewAllPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        viewAllPanel.setBackground(PANEL_BACKGROUND);
        viewAllPanel.add(btnView);

        panel.add(title, BorderLayout.NORTH);
        panel.add(searchFieldPanel, BorderLayout.CENTER);
        panel.add(viewAllPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTableHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PANEL_BACKGROUND);
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel tableTitle = new JLabel(" Student Records");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(TABLE_HEADER_COLOR);

        JLabel recordCount = new JLabel("Total: 0");
        recordCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        recordCount.setForeground(new Color(100, 100, 100));

        headerPanel.add(tableTitle, BorderLayout.WEST);
        headerPanel.add(recordCount, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
                new EmptyBorder(10, 20, 10, 20)
        ));

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(new Color(100, 100, 100));

        taStatus.setEditable(false);
        taStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        taStatus.setBackground(new Color(250, 250, 250));
        taStatus.setForeground(new Color(60, 60, 60));
        taStatus.setBorder(new EmptyBorder(5, 10, 5, 10));
        taStatus.setLineWrap(true);
        taStatus.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(taStatus);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(0, 0, 0, 0)
        ));
        scrollPane.setPreferredSize(new Dimension(100, 80));

        panel.add(statusLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void setupTable() {
        table.setFillsViewportHeight(true);
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(240, 240, 240));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? TABLE_EVEN_ROW : TABLE_ODD_ROW);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                return this;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setSelectionBackground(new Color(220, 235, 255));
        table.setSelectionForeground(Color.BLACK);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        field.setPreferredSize(new Dimension(200, 35));
        return field;
    }

    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(80, 80, 80));
        return label;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(PRIMARY_COLOR.darker(), 1, true),
                new EmptyBorder(10, 25, 10, 25)
        ));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(true);
        button.setOpaque(true);

        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER);
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BUTTON_HOVER.darker(), 1, true),
                        new EmptyBorder(10, 25, 10, 25)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(PRIMARY_COLOR.darker(), 1, true),
                        new EmptyBorder(10, 25, 10, 25)
                ));
            }
        });
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(240, 240, 240));
        button.setForeground(new Color(60, 60, 60));
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(10, 25, 10, 25)
        ));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 220));
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(180, 180, 180), 1, true),
                        new EmptyBorder(10, 25, 10, 25)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 240, 240));
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(200, 200, 200), 1, true),
                        new EmptyBorder(10, 25, 10, 25)
                ));
            }
        });
        return button;
    }

    private void attachHandlers() {
        btnAdd.addActionListener(this::addStudent);
        btnClear.addActionListener(e -> clearFields());
        btnView.addActionListener(e -> loadStudents());
        btnSearch.addActionListener(this::searchStudent);

        // Add Enter key support
        tfEmail.addActionListener(this::addStudent);
        tfAge.addActionListener(this::addStudent);
        tfSearchId.addActionListener(this::searchStudent);
    }

    private void clearFields() {
        tfFirst.setText("");
        tfLast.setText("");
        tfAge.setText("");
        tfEmail.setText("");
        tfFirst.requestFocus();
        status("Cleared input fields.");
    }

    private void addStudent(ActionEvent e) {
        String f = tfFirst.getText().trim();
        String l = tfLast.getText().trim();
        String a = tfAge.getText().trim();
        String em = tfEmail.getText().trim();

        if (f.isEmpty() || l.isEmpty()) {
            status("⚠️ First and Last name are required.");
            tfFirst.requestFocus();
            return;
        }

        Integer age = null;
        if (!a.isEmpty()) {
            try {
                age = Integer.parseInt(a);
                if (age <= 0 || age > 120) {
                    status("❌ Age must be between 1 and 120.");
                    tfAge.requestFocus();
                    return;
                }
            }
            catch (NumberFormatException x) {
                status("❌ Age must be a valid number.");
                tfAge.requestFocus();
                return;
            }
        }

        if (!em.isEmpty() && !em.contains("@")) {
            status("⚠️ Email format is invalid.");
            tfEmail.requestFocus();
            return;
        }

        Student s = new Student(f, l, age, em);

        new SwingWorker<Student, Void>() {
            @Override
            protected Student doInBackground() throws Exception {
                return studentDAO.addStudent(s);
            }

            @Override
            protected void done() {
                try {
                    Student saved = get();
                    status("✅ Added student: " + saved.getFirstName() + " " +
                            saved.getLastName() + " (ID: " + saved.getId() + ")");
                    loadStudents();
                    clearFields();
                } catch (Exception ex) {
                    status("❌ Error adding student: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void loadStudents() {
        new SwingWorker<List<Student>, Void>() {
            @Override
            protected List<Student> doInBackground() throws Exception {
                return studentDAO.getAllStudents();
            }

            @Override
            protected void done() {
                try {
                    List<Student> list = get();
                    tableModel.setStudents(list);
                    status("📊 Loaded " + list.size() + " student(s).");

                    // Update record count in table header
                    JPanel tablePanel = (JPanel)((JPanel)table.getParent().getParent()).getComponent(0);
                    JLabel recordCount = (JLabel)((JPanel)tablePanel.getComponent(0)).getComponent(1);
                    recordCount.setText("Total: " + list.size());
                } catch (Exception ex) {
                    status("❌ Error loading students: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void searchStudent(ActionEvent e) {
        String idText = tfSearchId.getText().trim();
        if (idText.isEmpty()) {
            status("⚠️ Please enter a Student ID to search.");
            tfSearchId.requestFocus();
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
            if (id <= 0) {
                status("❌ ID must be a positive number.");
                return;
            }
        }
        catch (Exception ex) {
            status("❌ Invalid ID format. Must be a number.");
            return;
        }

        new SwingWorker<Student, Void>() {
            @Override
            protected Student doInBackground() throws Exception {
                return studentDAO.findStudentById(id);
            }

            @Override
            protected void done() {
                try {
                    Student s = get();
                    if (s == null) {
                        status("🔍 No student found with ID: " + id);
                    } else {
                        status("🔍 Found: " + s.getFirstName() + " " + s.getLastName() +
                                " (ID: " + s.getId() + ", Age: " +
                                (s.getAge() != null ? s.getAge() : "N/A") +
                                ", Email: " + (s.getEmail() != null ? s.getEmail() : "N/A") + ")");

                        // Highlight in table if present
                        table.clearSelection();
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (Integer.parseInt(table.getValueAt(i, 0).toString()) == id) {
                                table.setRowSelectionInterval(i, i);
                                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    status("❌ Error searching: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void status(String msg) {
        String timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        taStatus.append("[" + timestamp + "] " + msg + "\n");
        taStatus.setCaretPosition(taStatus.getDocument().getLength());
    }
}