package main;

import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import files.FileInfoReader;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import roles.Student;
import roles.Professor;
import roles.Admin;
import courses.Course;

public class Controller {
    private static FileInfoReader fileInfoReader;
    private static List<Student> students;
    private static List<Professor> professors;
    private static List<Admin> admins;
    private static List<Course> courses;
    private static Admin adminInstance; // Admin instance
    
    // Declare file path variables as static fields
    private static String courseInfoPath;
    private static String studentInfoPath;
    private static String profInfoPath;
    private static String adminInfoPath;

    /**
     * Loads data from specified file paths into the system.
     * Initializes lists of courses, students, professors, and admins.
     *
     * @param courseInfoPath  Path to the course information file.
     * @param studentInfoPath Path to the student information file.
     * @param profInfoPath    Path to the professor information file.
     * @param adminInfoPath   Path to the admin information file.
     */
    private static void loadData(String courseInfoPath, String studentInfoPath, String profInfoPath, String adminInfoPath) {
        fileInfoReader = new FileInfoReader(courseInfoPath, studentInfoPath, profInfoPath, adminInfoPath);

        try {
            courses = fileInfoReader.readCourseInfo();
            students = fileInfoReader.readStudentInfo();
            professors = fileInfoReader.readProfInfo();
            admins = fileInfoReader.readAdminInfo();

         // Create a map for quick professor lookups by ID
            Map<String, Professor> professorIdMap = new HashMap<>();
            for (Professor professor : professors) {
                professorIdMap.put(professor.getId(), professor);
            }

            // Link courses with professors
            for (Course course : courses) {
                // Assuming course.getProfessorName() returns the professor's ID
                String professorId = course.getProfessorName(); 
                if (professorIdMap.containsKey(professorId)) {
                    Professor professor = professorIdMap.get(professorId);
                    professor.getCourses().add(course.getCourseId());
                    course.setProfessorId(professor.getId()); // This line might be redundant now
                } else {
                    System.out.println("No matching professor found for Course: " + course.getCourseName() + ", Professor ID: " + course.getProfessorName());
                }
            }


            // Create a map for quick course lookups
            Map<String, Course> courseMap = new HashMap<>();
            for (Course course : courses) {
                courseMap.put(course.getCourseId(), course);
            }

            // Link students with courses
            for (Student student : students) {
                Map<String, String> studentCourses = student.getCourses();
                for (String courseId : studentCourses.keySet()) {
                    Course course = courseMap.get(courseId);
                    if (course != null) {
                        course.addStudent(student.getId());
                    }
                }
            }


        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            System.exit(1);
        }
    }






    /**
     * The main method to start the application.
     * Initializes paths to data files and loads data.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        // Initialize paths to the data files
        courseInfoPath = "src/courseinfo.txt";
        studentInfoPath = "src/studentinfo.txt";
        profInfoPath = "src/profinfo.txt";
        adminInfoPath = "src/admininfo.txt";

        // Load data
        loadData(courseInfoPath, studentInfoPath, profInfoPath, adminInfoPath);

        // Main application logic
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                // Display menu
                System.out.println("---------------------------");
                System.out.println("Students Management System");
                System.out.println("---------------------------");
                System.out.println("1 -- Login as a student");
                System.out.println("2 -- Login as a professor");
                System.out.println("3 -- Login as an admin");
                System.out.println("4 -- Quit the system");
                System.out.println("");
                System.out.println("Please enter your option, e.g., '1'.");

                String option = scanner.nextLine();

                // Check if the option is a valid integer
                int choice = -1;
                try {
                    choice = Integer.parseInt(option);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                // Process the user's choice
                switch (choice) {
                    case 1:
                        handleStudentLogin(scanner, courses, students);
                        break;
                    case 2:
                        handleProfessorLogin(scanner, courses, students);
                        break;
                    case 3:
                        handleAdminLogin(scanner);
                        break;
                    case 4:
                        System.out.println("Exiting system.");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                }
            }
        }
    }

    /**
     * Generic method to find a user by username and password from a list of users.
     * This method is flexible and can be used with any user type (e.g., Student, Professor, Admin)
     * as long as the type has username and password fields.
     *
     * @param <T>                The type of the user, inferred from the users list.
     * @param users              The list of users of type T.
     * @param username           The username to search for.
     * @param password           The password to search for.
     * @param usernameExtractor  A function that takes an object of type T and returns its username.
     *                           This function is used to extract the username from each user object.
     * @param passwordExtractor  A function that takes an object of type T and returns its password.
     *                           This function is used to extract the password from each user object.
     * @return                   The user object of type T if found, null otherwise.
     *
     * Example usage:
     *   User foundUser = findByUsernameAndPassword(usersList, "john_doe", "password123", User::getUsername, User::getPassword);
     */
    private static <T> T findByUsernameAndPassword(
    	    List<T> users,
    	    String username,
    	    String password,
    	    Function<T, String> usernameExtractor,
    	    Function<T, String> passwordExtractor) {

    	    return users.stream()
    	                .filter(user -> usernameExtractor.apply(user).equals(username) && passwordExtractor.apply(user).equals(password))
    	                .findFirst()
    	                .orElse(null);
    	}

    /**
     * Handles the student login process.
     *
     * @param scanner    The scanner object for reading user input.
     * @param allCourses The list of all courses.
     * @param allStudents The list of all students.
     */
    private static void handleStudentLogin(Scanner scanner, List<Course> allCourses, List<Student> allStudents) {
        loadData(courseInfoPath, studentInfoPath, profInfoPath, adminInfoPath);
        System.out.println("Enter student username:");
        String username = scanner.next();
        System.out.println("Enter password:");
        String password = scanner.next();

        Student student = findByUsernameAndPassword(
            allStudents,
            username,
            password,
            Student::getUsername,
            Student::getPassword
        );

        if (student != null) {
            System.out.println("Logged in as " + student.getName()); // Add this line to display the login message
            student.manageOperations(allCourses, allStudents, scanner); // Pass the required lists
        } else {
            System.out.println("Invalid login. Please try again.");
        }
    }
    
    /**
     * Handles the professor login process.
     *
     * @param scanner    The scanner object for reading user input.
     * @param allCourses The list of all courses.
     * @param allStudents The list of all students.
     */
    private static void handleProfessorLogin(Scanner scanner, List<Course> allCourses, List<Student> allStudents) {
        loadData(courseInfoPath, studentInfoPath, profInfoPath, adminInfoPath);
        System.out.println("Enter professor username:");
        String username = scanner.next();
        System.out.println("Enter password:");
        String password = scanner.next();

        Professor professor = findByUsernameAndPassword(
            professors,
            username,
            password,
            Professor::getUsername,
            Professor::getPassword
        );

        if (professor != null) {
            professor.manageOperations(allCourses, allStudents, scanner);
        } else {
            System.out.println("Invalid login. Please try again.");
        }
    }

    /**
     * Handles the admin login process.
     *
     * @param scanner The scanner object for reading user input.
     */
    private static void handleAdminLogin(Scanner scanner) {
        loadData(courseInfoPath, studentInfoPath, profInfoPath, adminInfoPath);
        System.out.println("Enter admin username:");
        String username = scanner.next();
        System.out.println("Enter password:");
        String password = scanner.next();

        Admin admin = findByUsernameAndPassword(
            admins,
            username,
            password,
            Admin::getUsername,
            Admin::getPassword
        );

        if (admin != null) {
            adminInstance = admin; // Assuming admin objects are already created in the admins list
            adminInstance.setCourses(new ArrayList<>(courses)); // Set the courses list
            adminInstance.setProfessors(professors); // Set the professors list
            adminInstance.manageAdminOperations(scanner);
        } else {
            System.out.println("Invalid login. Please try again.");
        }
    }



}

