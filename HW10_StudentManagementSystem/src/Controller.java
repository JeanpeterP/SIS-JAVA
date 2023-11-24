import java.util.Scanner;
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
    private static void loadData(String courseInfoPath, String studentInfoPath, String profInfoPath, String adminInfoPath) {
        fileInfoReader = new FileInfoReader(courseInfoPath, studentInfoPath, profInfoPath, adminInfoPath);

        try {
            courses = fileInfoReader.readCourseInfo();
            students = fileInfoReader.readStudentInfo();
            professors = fileInfoReader.readProfInfo();
            admins = fileInfoReader.readAdminInfo();

            // Create a map for quick professor lookups
            Map<String, Professor> professorMap = new HashMap<>();
            for (Professor professor : professors) {
                professorMap.put(professor.getId(), professor);
            }

            // Link courses with professors (if needed)
            for (Course course : courses) {
                // Change to use professor ID to find the professor
                Professor professor = professorMap.get(course.getProfessorId());
                if (professor != null) {
                    professor.getCourses().add(course.getCourseId());
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

            // Print final state for debugging
            for (Professor professor : professors) {
            	System.out.println(professor.getId() + " - " + professor.getName());
                System.out.println("Professor: " + professor.getName() + ", Courses: " + professor.getCourses());
            }

        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
            System.exit(1);
        }
    }






    public static void main(String[] args) {
        // Initialize paths to the data files
    	
        String courseInfoPath = "src/courseinfo.txt";
        String studentInfoPath = "src/studentinfo.txt";
        String profInfoPath = "src/profinfo.txt";
        String adminInfoPath = "src/admininfo.txt";

        // Load data
        loadData(courseInfoPath, studentInfoPath, profInfoPath, adminInfoPath);

        // Main application logic
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("Select option: 1. Student Login 2. Professor Login 3. Admin Login 4. Quit");
            int choice = scanner.nextInt();
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
                    System.out.println("Invalid choice.");
            }
        }
        scanner.close();
    }
    
    private static void handleStudentLogin(Scanner scanner, List<Course> allCourses, List<Student> allStudents) {
        System.out.println("Enter student username:");
        String username = scanner.next();
        System.out.println("Enter password:");
        String password = scanner.next();

        Student student = findStudentByUsernameAndPassword(username, password);

        if (student != null) {
            student.manageOperations(allCourses, allStudents, scanner); // Pass the required lists
        } else {
            System.out.println("Invalid login. Please try again.");
        }
    }

    private static Student findStudentByUsernameAndPassword(String username, String password) {
        return students.stream()
                       .filter(s -> s.getUsername().equals(username) && s.getPassword().equals(password))
                       .findFirst()
                       .orElse(null);
    }

    

    
    private static void handleProfessorLogin(Scanner scanner, List<Course> allCourses, List<Student> allStudents) {
        System.out.println("Enter professor username:");
        String username = scanner.next();
        System.out.println("Enter password:");
        String password = scanner.next();

        Professor professor = findProfessorByUsernameAndPassword(username, password);

        if (professor != null) {
            professor.manageOperations(allCourses, allStudents, scanner);
        } else {
            System.out.println("Invalid login. Please try again.");
        }
    }

    private static Professor findProfessorByUsernameAndPassword(String username, String password) {
        return professors.stream()
                         .filter(p -> p.getUsername().equals(username) && p.getPassword().equals(password))
                         .findFirst()
                         .orElse(null);
    }




    private static void handleAdminLogin(Scanner scanner) {
        System.out.println("Enter admin username:");
        String username = scanner.next();
        System.out.println("Enter password:");
        String password = scanner.next();

        Admin admin = findAdminByUsernameAndPassword(username, password);

        if (admin != null) {
            // Initialize or update adminInstance
            adminInstance = admin; // Assuming admin objects are already created in the admins list
            adminInstance.setCourses(new ArrayList<>(courses)); // Set the courses list
            adminInstance.setProfessors(professors); // Set the professors list
            adminInstance.manageAdminOperations(scanner);
        } else {
            System.out.println("Invalid login. Please try again.");
        }
    }


    private static Admin findAdminByUsernameAndPassword(String username, String password) {
        return admins.stream()
                     .filter(a -> a.getUsername().equals(username.trim()) && a.getPassword().equals(password.trim()))
                     .findFirst()
                     .orElse(null);
    }



}

   


