package roles;

import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import courses.Course;

public class Student extends User {
    private Map<String, String> courses; // Course ID and Grade

    public Student(String id, String name, String username, String password, Map<String, String> courses) {
        super(id, name, username, password);
        this.courses = courses;
    }

    // Manage student operations
    public void manageOperations(List<Course> allCourses, List<Student> allStudents, Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("Select option: 1. View Courses 2. Add Course 3. Drop Course 4. View Grades 5. Return to Main Menu");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    viewCourses();
                    break;
                case 2:
                    addCourse(allCourses, allStudents, scanner);
                    break;
                case 3:
                	dropCourse(scanner, allStudents, "src/studentinfo.txt");
                    break;
                case 4:
                    viewGrades();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // View enrolled courses
    public void viewCourses() {
        courses.forEach((courseId, grade) -> System.out.println("Course ID: " + courseId + ", Grade: " + grade));
    }

    // Add a course
    public void addCourse(List<Course> allCourses, List<Student> allStudents, Scanner scanner) {
        System.out.println("Enter Course ID to add:");
        String courseId = scanner.next();
        Course courseToAdd = allCourses.stream()
                                      .filter(c -> c.getCourseId().equals(courseId))
                                      .findFirst()
                                      .orElse(null);

        if (courseToAdd != null && !courses.containsKey(courseId) && courseToAdd.addStudent(this.getId())) {
            courses.put(courseId, "Not Graded");
            System.out.println("Course added successfully.");
            Student.saveStudentsToFile(allStudents, "src/studentinfo.txt"); // Save changes to file
        } else {
            System.out.println("Course could not be added. It may be full or not exist.");
        }
    }

 // New method to save student data to file
    public static void saveStudentsToFile(List<Student> students, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Student student : students) {
                String studentData = student.getId() + "; " + 
                                     student.getName() + "; " + 
                                     student.getUsername() + "; " + 
                                     student.getPassword() + "; " +
                                     student.getCourses().entrySet().stream()
                                            .map(entry -> entry.getKey() + ": " + entry.getValue())
                                            .collect(Collectors.joining(", "));
                writer.write(studentData);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    
 // In your methods where you modify student data
 // Example in dropCourse method
    public void dropCourse(Scanner scanner, List<Student> allStudents, String filePath) {
    	System.out.println("Enter Course ID to drop:");
	     String courseId = scanner.next();
	     if (this.courses.remove(courseId) != null) {
	         System.out.println("Course dropped successfully.");
	         Student.saveStudentsToFile(allStudents, filePath);
	     } else {
	         System.out.println("Course not found in your schedule.");
	     }
	 }



    // View grades
    public void viewGrades() {
        courses.forEach((courseId, grade) -> System.out.println("Course ID: " + courseId + ", Grade: " + grade));
    }

    // Getters and setters
    public Map<String, String> getCourses() {
        return courses;
    }

    public void setCourses(Map<String, String> courses) {
        this.courses = courses;
    }

    // Additional methods can be implemented as needed
}

