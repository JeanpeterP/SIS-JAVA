package roles;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import courses.Course;

public class Professor extends User {
    private List<String> courses; // List of Course IDs

    public Professor(String id, String name, String username, String password, List<String> courses) {
        super(id, name, username, password);
        this.courses = (courses != null) ? courses : new ArrayList<>();
    }


    // Existing methods...

    // Method to manage professor operations
    public void manageOperations(List<Course> allCourses, List<Student> allStudents, Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("Select option: 1. View My Courses 2. View Student List for a Course 3. Return to Main Menu");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    viewProfessorCourses(allCourses);
                    break;
                case 2:
                    promptAndViewStudentsInCourse(scanner, allCourses, allStudents);
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Method to prompt for course ID and view students
    private void promptAndViewStudentsInCourse(Scanner scanner, List<Course> allCourses, List<Student> allStudents) {
        System.out.println("Enter Course ID to view students:");
        String courseId = scanner.next();

        viewStudentsInCourse(courseId, allCourses, allStudents);
    }

    // Method to view students in a specific course
    public void viewStudentsInCourse(String courseId, List<Course> allCourses, List<Student> allStudents) {
        if (this.courses.contains(courseId)) {
            allCourses.stream()
                      .filter(c -> c.getCourseId().equals(courseId))
                      .findFirst()
                      .ifPresent(course -> {
                          System.out.println("Students in " + courseId + ":");
                          for (String studentId : course.getEnrolledStudents()) {
                              allStudents.stream()
                                         .filter(s -> s.getId().equals(studentId))
                                         .findFirst()
                                         .ifPresent(student -> System.out.println("Student ID: " + student.getId() + ", Name: " + student.getName()));
                          }
                      });
        } else {
            System.out.println("You do not teach this course.");
        }
    }


    // Method to view the courses taught by the professor
    public void viewProfessorCourses(List<Course> allCourses) {
        if (this.courses.isEmpty()) {
            System.out.println("You are not teaching any courses.");
        } else {
            System.out.println("Your Course IDs: " + this.courses); // Debugging line
            long count = this.courses.stream()
                                     .filter(courseId -> allCourses.stream().anyMatch(c -> c.getCourseId().equals(courseId)))
                                     .peek(courseId -> allCourses.stream()
                                                                 .filter(c -> c.getCourseId().equals(courseId))
                                                                 .findFirst()
                                                                 .ifPresent(course -> System.out.println("Course ID: " + courseId + ", Course Name: " + course.getCourseName())))
                                     .count();

            if (count == 0) {
                System.out.println("No courses found for your ID. Please check your course list.");
            }
        }
    }


    // Method to set the courses for the professor
    public void setCourses(List<String> courses) {
        this.courses = courses;
    }
    
    // Method to get the list of course IDs
    public List<String> getCourses() {
        return courses;
    }
    
    // Getter for professorId
    public String getProfessorId() {
        return id;
    }

    

}
