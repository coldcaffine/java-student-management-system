
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Comparator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

class Student {

    // Encapsulation: private data members
    private int id;
    private String name;
    private double marks;

    // Constructor
    public Student(int id, String name, double marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMarks() {
        return marks;
    }

    // Display student information
    public void display() {
        System.out.println(
                "ID: " + id +
                        " | Name: " + name +
                        " | Marks: " + marks);
    }
}

public class Main {

    static Scanner sc = new Scanner(System.in);

    // ArrayList to store student objects
    static ArrayList<Student> students = new ArrayList<>();

    static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {

        loadStudents();

        while (true) {

            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Calculate Average Marks");
            System.out.println("6. Sort Students by Marks");
            System.out.println("0. Save and Exit");

            System.out.print("Enter your choice: ");

            int choice = readInt();

            switch (choice) {

                case 1:
                    addStudent();
                    break;

                case 2:
                    viewStudents();
                    break;

                case 3:
                    searchStudent();
                    break;

                case 4:
                    deleteStudent();
                    break;

                case 5:
                    calculateAverage();
                    break;

                case 6:
                    sortStudents();
                    break;

                case 0:
                    saveStudents();
                    System.out.println("Data saved. Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // Method to safely read integers
    public static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    // Add a new student
    public static void addStudent() {

        System.out.print("Enter student ID: ");
        int id = readInt();

        for (Student s : students) {
            if (s.getId() == id) {
                System.out.println("ID already exists!");
                return;
            }
        }

        System.out.print("Enter student name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty() || name.contains("|")) {
            System.out.println("Invalid name!");
            return;
        }

        System.out.print("Enter marks (0-100): ");
        double marks;

        try {
            marks = Double.parseDouble(sc.nextLine());

            if (marks < 0 || marks > 100) {
                System.out.println("Marks must be between 0 and 100!");
                return;
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid marks!");
            return;
        }

        Student student = new Student(id, name, marks);
        students.add(student);

        System.out.println("Student added successfully!");
    }

    // Display all students
    public static void viewStudents() {

        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        for (Student s : students) {
            s.display();
        }
    }

    // Search for a student
    public static void searchStudent() {

        System.out.print("Enter student ID: ");
        int id = readInt();

        for (Student s : students) {

            if (s.getId() == id) {
                System.out.println("Student found:");
                s.display();
                return;
            }
        }

        System.out.println("Student not found.");
    }

    // Delete a student
    public static void deleteStudent() {

        System.out.print("Enter student ID to delete: ");
        int id = readInt();

        boolean removed = students.removeIf(
                s -> s.getId() == id);

        if (removed) {
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Student not found.");
        }
    }

    // Calculate average marks
    public static void calculateAverage() {

        if (students.isEmpty()) {
            System.out.println("No records available.");
            return;
        }

        double total = 0;

        for (Student s : students) {
            total += s.getMarks();
        }

        double average = total / students.size();

        System.out.println("Average Marks: " + average);
    }

    // Sort students by marks
    public static void sortStudents() {

        students.sort(
                Comparator.comparingDouble(Student::getMarks).reversed());

        System.out.println("Students sorted by marks:");
        viewStudents();
    }

    // Save student records to a file
    public static void saveStudents() {

        ArrayList<String> lines = new ArrayList<>();

        for (Student s : students) {
            String line = s.getId() + "|" +
                    s.getName() + "|" +
                    s.getMarks();

            lines.add(line);
        }

        try {
            Files.write(
                    Path.of(FILE_NAME),
                    lines);

            System.out.println("Records saved successfully!");

        } catch (IOException e) {
            System.out.println("Error saving records: " + e.getMessage());
        }
    }

    // Load student records from a file
    public static void loadStudents() {

        Path path = Path.of(FILE_NAME);

        if (!Files.exists(path)) {
            return;
        }

        try {
            for (String line : Files.readAllLines(path)) {

                String[] data = line.split("\\|", -1);

                if (data.length == 3) {
                    try {
                        int id = Integer.parseInt(data[0]);
                        String name = data[1];
                        double marks = Double.parseDouble(data[2]);

                        if (!name.isEmpty() && marks >= 0 && marks <= 100) {
                            students.add(new Student(id, name, marks));
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid record.");
                    }
                }
            }

            System.out.println("Student records loaded!");

        } catch (IOException e) {
            System.out.println("Error loading records: " + e.getMessage());
        }
    }
}
