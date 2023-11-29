package roles;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import courses.Course;

public class Professor extends User {
    private List<String> courses; // List of Course IDs
    
    /**
     * Constructor for the Professor class.
     *
     * @param id       The unique identifier for the professor.
     * @param name     The name of the professor.
     * @param username The username for the professor's account.
     * @param password The password for the professor's account.
     * @param courses  A list of course IDs that the professor teaches.
     */
    public Professor(String id, String name, String username, String password, List<String> courses) {
        super(id, name, username, password);
        this.courses = (courses != null) ? courses : new ArrayList<>();
    }



    /**
     * Manages the operations specific to a professor.
     * Provides a menu to view courses, student lists, and return to the main menu.
     *
     * @param allCourses  List of all available courses.
     * @param allStudents List of all students.
     * @param scanner     Scanner object to read user input.
     */
    public void manageOperations(List<Course> allCourses, List<Student> allStudents, Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("---------------------------");
            System.out.println("Professor Operations");
            System.out.println("---------------------------");
            System.out.println("1 -- View My Courses");
            System.out.println("2 -- View Student List for a Course");
            System.out.println("3 -- Return to Main Menu");
            System.out.println("");
            System.out.println("Please enter your option, e.g., '1'.");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear the scanner buffer

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
    
    /**
     * Prompts the professor to enter a course ID and then displays the list of students enrolled in that course.
     *
     * @param scanner     Scanner object to read user input.
     * @param allCourses  List of all available courses.
     * @param allStudents List of all students.
     */
 // Method to prompt for course ID and view students
    private void promptAndViewStudentsInCourse(Scanner scanner, List<Course> allCourses, List<Student> allStudents) {
        // Display courses taught by the professor
        if (this.courses.isEmpty()) {
            System.out.println("You are not teaching any courses.");
            return; // Exit the method if the professor has no courses
        } else {
            System.out.println("Courses you teach:");
            this.courses.forEach(courseId -> allCourses.stream()
                                                       .filter(course -> course.getCourseId().equals(courseId))
                                                       .forEach(course -> System.out.println("Course ID: " + course.getCourseId() + ", Name: " + course.getCourseName())));
        }

        // Ask for the course ID
        System.out.println("Enter Course ID to view students:");
        String courseId = scanner.nextLine();

        // Call the method to view students
        viewStudentsInCourse(courseId, allCourses, allStudents);
    }


    /**
     * Displays the students enrolled in a specific course.
     *
     * @param courseId    The ID of the course for which to view students.
     * @param allCourses  List of all available courses.
     * @param allStudents List of all students.
     */
    public void viewStudentsInCourse(String selectedCourseId, List<Course> allCourses, List<Student> allStudents) {
        // First, display all courses taught by the professor
        System.out.println("Courses you teach:");
        this.courses.forEach(courseId -> allCourses.stream()
                                                   .filter(course -> course.getCourseId().equals(courseId))
                                                   .forEach(course -> System.out.println("Course ID: " + course.getCourseId() + ", Name: " + course.getCourseName())));

        // Check if the professor teaches the specified course
        if (this.courses.contains(selectedCourseId)) {
            // Find the specific course and display enrolled students
            allCourses.stream()
                      .filter(c -> c.getCourseId().equals(selectedCourseId))
                      .findFirst()
                      .ifPresent(course -> {
                          System.out.println("Students in " + selectedCourseId + ":");
                          for (String studentId : course.getEnrolledStudents()) {
                              allStudents.stream()
                                         .filter(s -> s.getId().equals(studentId))
                                         .findFirst()
                                         .ifPresent(student -> System.out.println("Student ID: " + student.getId() + ", Name: " + student.getName()));
                          }
                      });
        } else {
            // Message if the professor does not teach the course
            System.out.println("You do not teach this course.");
        }
    }




    /**
     * Displays the list of courses taught by the professor.
     *
     * @param allCourses List of all available courses.
     */
    public void viewProfessorCourses(List<Course> allCourses) {
        // Check if the professor teaches any courses
        if (this.courses.isEmpty()) {
            System.out.println("You are not teaching any courses.");
        } else {
            // Debugging line displaying all course IDs
            System.out.println("Your Course IDs: " + this.courses);
            // Counting and displaying each course taught by the professor
            long count = this.courses.stream()
                                     .filter(courseId -> allCourses.stream().anyMatch(c -> c.getCourseId().equals(courseId)))
                                     .peek(courseId -> allCourses.stream()
                                                                 .filter(c -> c.getCourseId().equals(courseId))
                                                                 .findFirst()
                                                                 .ifPresent(course -> System.out.println("Course ID: " + courseId + ", Course Name: " + course.getCourseName())))
                                     .count();
            // Handling the case where no courses are found
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
