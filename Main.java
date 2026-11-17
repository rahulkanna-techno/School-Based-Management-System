import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

// Student Class
class Student {

    String studentId;
    String name, standard;
    char section;

    double tamil, english, maths, science, socialScience;

    // Constructor
    public Student(String studentId, String name, String standard,
                   char section, double tamil, double english,
                   double maths, double science, double socialScience) {

        this.studentId = studentId;
        this.name = name;
        this.standard = standard;
        this.section = section;

        this.tamil = tamil;
        this.english = english;
        this.maths = maths;
        this.science = science;
        this.socialScience = socialScience;
    }

    // Calculate Total Marks
    public double calculateTotal() {
        return tamil + english + maths + science + socialScience;
    }

    // Calculate Average Marks
    public double calculateAverage() {
        return calculateTotal() / 5.0;
    }
}

// Main School Management System Class
public class Main extends JFrame {

    // Admin Login Details (change these if you want)
    static final String ADMIN_USERNAME = "admin";
    static final String ADMIN_PASSWORD = "admin123";

    ArrayList<Student> studentList = new ArrayList<>();

    // Attendance Data: date -> (studentId -> "Present" / "Absent")
    TreeMap<String, Map<String, String>> attendanceData = new TreeMap<>();

    int studentCounter = 1;

    JTextField nameField, standardField, sectionField;
    JTextField tamilField, englishField, mathsField;
    JTextField scienceField, socialField;

    // Search Components
    JTextField searchField;
    JComboBox<String> searchTypeBox;

    // Attendance Components
    JTextField attendanceDateField;
    DefaultTableModel attendanceModel;
    JTable attendanceTable;
    DefaultTableModel summaryModel;

    DefaultTableModel model;

    // Constructor
    public Main() {

        setTitle("School Management System");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Default Student Record
        studentList.add(new Student(
            "STU001", "Rodri", "10th", 'C',
            60, 90, 80, 87, 65
        ));

        studentCounter = 2;

        // Tabs: Students and Attendance
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Students", buildStudentPanel());
        tabs.addTab("Attendance", buildAttendancePanel());

        add(tabs);

        displayStudents();
        loadAttendanceTable();
        refreshSummary();

        setVisible(true);
    }

    // ================= STUDENT TAB =================

    JPanel buildStudentPanel() {

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));

        mainPanel.setBackground(new Color(235, 242, 250));

        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // Heading
        JLabel title = new JLabel(
            "SCHOOL MANAGEMENT SYSTEM",
            JLabel.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(25, 55, 100));

        mainPanel.add(title, BorderLayout.NORTH);

        // Student Input Panel
        JPanel inputPanel = new JPanel(
            new GridLayout(8, 2, 10, 10)
        );

        inputPanel.setBorder(
            BorderFactory.createTitledBorder("Student Details")
        );

        nameField = new JTextField();
        standardField = new JTextField();
        sectionField = new JTextField();

        tamilField = new JTextField();
        englishField = new JTextField();
        mathsField = new JTextField();
        scienceField = new JTextField();
        socialField = new JTextField();

        addField(inputPanel, "Student Name:", nameField);
        addField(inputPanel, "Standard:", standardField);
        addField(inputPanel, "Section:", sectionField);

        addField(inputPanel, "Tamil Marks:", tamilField);
        addField(inputPanel, "English Marks:", englishField);
        addField(inputPanel, "Maths Marks:", mathsField);
        addField(inputPanel, "Science Marks:", scienceField);
        addField(inputPanel, "Social Science Marks:", socialField);

        // Buttons
        JButton addButton = new JButton("Add Student");
        JButton viewButton = new JButton("View Students");
        JButton clearButton = new JButton("Clear");
        JButton logoutButton = new JButton("Logout");
        JButton exitButton = new JButton("Exit");

        // Button Panel
        JPanel buttonPanel = new JPanel(
            new GridLayout(3, 2, 10, 10)
        );

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(exitButton);

        // Left Panel
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));

        leftPanel.add(inputPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Search Panel
        JPanel searchPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        searchPanel.setBorder(
            BorderFactory.createTitledBorder("Search Student")
        );

        searchTypeBox = new JComboBox<>(new String[] {
            "All Fields", "Student ID", "Name", "Standard", "Section"
        });

        searchField = new JTextField(20);

        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");

        searchPanel.add(new JLabel("Search By:"));
        searchPanel.add(searchTypeBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        // Student Records Table
        String[] columns = {
            "Student ID", "Name", "Standard", "Section",
            "Tamil", "English", "Maths",
            "Science", "Social Science",
            "Total", "Average"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);

        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(
            BorderFactory.createTitledBorder("Student Records")
        );

        // Center Panel (Search + Table)
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        centerPanel.setOpaque(false);

        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add Student Button
        addButton.addActionListener(e -> addStudent());

        // View Students Button
        viewButton.addActionListener(e -> {
            searchField.setText("");
            displayStudents();
        });

        // Clear Button
        clearButton.addActionListener(e -> clearFields());

        // Search Button
        searchButton.addActionListener(e -> searchStudents());

        // Press Enter in search box to search
        searchField.addActionListener(e -> searchStudents());

        // Show All Button
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            searchTypeBox.setSelectedIndex(0);
            displayStudents();
        });

        // Logout Button
        logoutButton.addActionListener(e -> {

            int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {

                dispose();

                if (showLoginDialog()) {
                    new Main();
                } else {
                    System.exit(0);
                }
            }
        });

        // Exit Button
        exitButton.addActionListener(e -> {

            int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit",
                JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        return mainPanel;
    }

    // ================= ATTENDANCE TAB =================

    JPanel buildAttendancePanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        panel.setBackground(new Color(235, 242, 250));

        panel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // Top Panel: Date and Buttons
        JPanel topPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        topPanel.setBorder(
            BorderFactory.createTitledBorder("Attendance Date")
        );

        attendanceDateField = new JTextField(
            LocalDate.now().toString(), 10
        );

        JButton loadButton = new JButton("Load Date");
        JButton allPresentButton = new JButton("Mark All Present");
        JButton allAbsentButton = new JButton("Mark All Absent");
        JButton saveButton = new JButton("Save Attendance");

        topPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        topPanel.add(attendanceDateField);
        topPanel.add(loadButton);
        topPanel.add(allPresentButton);
        topPanel.add(allAbsentButton);
        topPanel.add(saveButton);

        panel.add(topPanel, BorderLayout.NORTH);

        // Daily Attendance Table
        String[] attColumns = {
            "Student ID", "Name", "Standard", "Section", "Status"
        };

        attendanceModel = new DefaultTableModel(attColumns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        attendanceTable = new JTable(attendanceModel);

        attendanceTable.setRowHeight(25);

        // Dropdown for Present / Absent
        JComboBox<String> statusBox = new JComboBox<>(
            new String[] { "Present", "Absent" }
        );

        attendanceTable.getColumnModel().getColumn(4)
            .setCellEditor(new DefaultCellEditor(statusBox));

        JScrollPane attScroll = new JScrollPane(attendanceTable);

        attScroll.setBorder(
            BorderFactory.createTitledBorder("Mark Attendance")
        );

        // Attendance Summary Table
        String[] sumColumns = {
            "Student ID", "Name", "Total Days",
            "Present", "Absent", "Attendance %"
        };

        summaryModel = new DefaultTableModel(sumColumns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable summaryTable = new JTable(summaryModel);

        summaryTable.setRowHeight(25);

        JScrollPane sumScroll = new JScrollPane(summaryTable);

        sumScroll.setBorder(
            BorderFactory.createTitledBorder("Attendance Summary")
        );

        // Split Pane (Daily + Summary)
        JSplitPane split = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT, attScroll, sumScroll
        );

        split.setResizeWeight(0.55);

        panel.add(split, BorderLayout.CENTER);

        // Button Actions
        loadButton.addActionListener(e -> {

            if (getValidDate() != null) {
                loadAttendanceTable();
            }
        });

        allPresentButton.addActionListener(e -> setAllStatus("Present"));

        allAbsentButton.addActionListener(e -> setAllStatus("Absent"));

        saveButton.addActionListener(e -> saveAttendance());

        return panel;
    }

    // Validate Date Entered
    String getValidDate() {

        String dateText = attendanceDateField.getText().trim();

        try {

            return LocalDate.parse(dateText).toString();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter date in YYYY-MM-DD format.\nExample: 2026-11-16"
            );

            return null;
        }
    }

    // Load Attendance Table for Selected Date
    void loadAttendanceTable() {

        String date;

        try {
            date = LocalDate.parse(
                attendanceDateField.getText().trim()
            ).toString();
        } catch (Exception ex) {
            date = LocalDate.now().toString();
        }

        Map<String, String> saved = attendanceData.get(date);

        attendanceModel.setRowCount(0);

        for (Student s : studentList) {

            String status = "Present";

            if (saved != null && saved.containsKey(s.studentId)) {
                status = saved.get(s.studentId);
            }

            attendanceModel.addRow(new Object[] {
                s.studentId, s.name, s.standard, s.section, status
            });
        }
    }

    // Mark All Students Present / Absent
    void setAllStatus(String status) {

        if (attendanceTable.isEditing()) {
            attendanceTable.getCellEditor().stopCellEditing();
        }

        for (int i = 0; i < attendanceModel.getRowCount(); i++) {
            attendanceModel.setValueAt(status, i, 4);
        }
    }

    // Save Attendance for Selected Date
    void saveAttendance() {

        if (attendanceTable.isEditing()) {
            attendanceTable.getCellEditor().stopCellEditing();
        }

        String date = getValidDate();

        if (date == null) {
            return;
        }

        Map<String, String> record = new LinkedHashMap<>();

        for (int i = 0; i < attendanceModel.getRowCount(); i++) {

            String id = attendanceModel.getValueAt(i, 0).toString();
            String status = attendanceModel.getValueAt(i, 4).toString();

            record.put(id, status);
        }

        attendanceData.put(date, record);

        refreshSummary();

        JOptionPane.showMessageDialog(
            this,
            "Attendance saved for " + date
        );
    }

    // Refresh Attendance Summary Table
    void refreshSummary() {

        summaryModel.setRowCount(0);

        for (Student s : studentList) {

            int present = 0;
            int absent = 0;

            for (Map<String, String> day : attendanceData.values()) {

                String status = day.get(s.studentId);

                if ("Present".equals(status)) {
                    present++;
                } else if ("Absent".equals(status)) {
                    absent++;
                }
            }

            int total = present + absent;

            double percentage = (total == 0)
                ? 0 : (present * 100.0) / total;

            summaryModel.addRow(new Object[] {
                s.studentId,
                s.name,
                total,
                present,
                absent,
                String.format("%.2f%%", percentage)
            });
        }
    }

    // ================= COMMON METHODS =================

    // Add Labels and Text Fields
    void addField(JPanel panel, String label, JTextField field) {

        panel.add(new JLabel(label));
        panel.add(field);
    }

    // Add New Student
    void addStudent() {

        try {

            String name = nameField.getText().trim();
            String standard = standardField.getText().trim();
            String sectionText = sectionField.getText().trim();

            // Validate Student Details
            if (name.isEmpty() || standard.isEmpty()
                    || sectionText.length() != 1) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please enter all student details."
                );

                return;
            }

            // Get Subject Marks
            double tamil = Double.parseDouble(tamilField.getText().trim());
            double english = Double.parseDouble(englishField.getText().trim());
            double maths = Double.parseDouble(mathsField.getText().trim());
            double science = Double.parseDouble(scienceField.getText().trim());
            double social = Double.parseDouble(socialField.getText().trim());

            // Validate Marks
            double[] marks = {
                tamil, english, maths, science, social
            };

            for (double mark : marks) {

                if (!Double.isFinite(mark) || mark < 0 || mark > 100) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Marks must be between 0 and 100."
                    );

                    return;
                }
            }

            // Generate Unique Student ID
            String studentId = String.format(
                "STU%03d", studentCounter
            );

            // Create Student Object
            Student s = new Student(
                studentId,
                name,
                standard,
                Character.toUpperCase(sectionText.charAt(0)),
                tamil,
                english,
                maths,
                science,
                social
            );

            // Store Student Record
            studentList.add(s);

            studentCounter++;

            JOptionPane.showMessageDialog(
                this,
                "Student Added Successfully!\nStudent ID: "
                + studentId
            );

            clearFields();

            displayStudents();

            // Update attendance tables with the new student
            loadAttendanceTable();
            refreshSummary();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter valid numerical marks."
            );
        }
    }

    // Search Students
    void searchStudents() {

        String keyword = searchField.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter a search keyword."
            );

            return;
        }

        String type = searchTypeBox.getSelectedItem().toString();

        ArrayList<Student> results = new ArrayList<>();

        for (Student s : studentList) {

            String id = s.studentId.toLowerCase();
            String name = s.name.toLowerCase();
            String standard = s.standard.toLowerCase();
            String section = String.valueOf(s.section).toLowerCase();

            boolean match;

            switch (type) {
                case "Student ID":
                    match = id.contains(keyword);
                    break;
                case "Name":
                    match = name.contains(keyword);
                    break;
                case "Standard":
                    match = standard.contains(keyword);
                    break;
                case "Section":
                    match = section.contains(keyword);
                    break;
                default:
                    match = id.contains(keyword)
                         || name.contains(keyword)
                         || standard.contains(keyword)
                         || section.contains(keyword);
            }

            if (match) {
                results.add(s);
            }
        }

        displayStudents(results);

        if (results.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "No students found."
            );
        }
    }

    // Display All Student Records
    void displayStudents() {

        displayStudents(studentList);
    }

    // Display Given List of Student Records
    void displayStudents(ArrayList<Student> list) {

        model.setRowCount(0);

        for (Student s : list) {

            model.addRow(new Object[] {

                s.studentId,
                s.name,
                s.standard,
                s.section,

                s.tamil,
                s.english,
                s.maths,
                s.science,
                s.socialScience,

                String.format("%.2f", s.calculateTotal()),

                String.format("%.2f%%", s.calculateAverage())
            });
        }
    }

    // Clear Input Fields
    void clearFields() {

        nameField.setText("");
        standardField.setText("");
        sectionField.setText("");

        tamilField.setText("");
        englishField.setText("");
        mathsField.setText("");
        scienceField.setText("");
        socialField.setText("");
    }

    // ================= ADMIN LOGIN =================

    // Show Login Dialog (3 attempts)
    static boolean showLoginDialog() {

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JPanel loginPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(userField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passField);

        int attempts = 0;

        while (attempts < 3) {

            userField.setText("");
            passField.setText("");

            int option = JOptionPane.showConfirmDialog(
                null,
                loginPanel,
                "Admin Login",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (option != JOptionPane.OK_OPTION) {
                return false;
            }

            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.equals(ADMIN_USERNAME)
                    && password.equals(ADMIN_PASSWORD)) {

                return true;
            }

            attempts++;

            if (attempts < 3) {

                JOptionPane.showMessageDialog(
                    null,
                    "Invalid username or password.\nAttempts left: "
                    + (3 - attempts),
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }

        JOptionPane.showMessageDialog(
            null,
            "Too many failed attempts. Program will close.",
            "Access Denied",
            JOptionPane.ERROR_MESSAGE
        );

        return false;
    }

    // Main Method
    public static void main(String[] args) {

        // Check GUI Support
        if (GraphicsEnvironment.isHeadless()) {

            System.out.println(
                "GUI is not supported in this environment."
            );

            System.out.println(
                "Please run this program on your Windows laptop."
            );

            return;
        }

        // Launch GUI after successful admin login
        SwingUtilities.invokeLater(() -> {

            if (showLoginDialog()) {
                new Main();
            } else {
                System.exit(0);
            }
        });
    }
}
