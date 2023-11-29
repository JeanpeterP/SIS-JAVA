package roles;

import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import courses.Course;

public class Student extends User {
    private Map<String, String> courses; // Course ID and Grade
    
    /**
     * Represents a student user in the system.
     * Extends the User class with student-specific attributes and operations.
     * Includes functionality for managing courses, viewing grades, and handling course scheduling conflicts.
     */
    public Student(String id, String name, String username, String password, Map<String, String> courses) {
        super(id, name, username, password);
        this.courses = courses;
    }

    /**
     * Manages student-specific operations, providing options to view courses, add or drop courses, view grades, and return to the main menu.
     * Utilizes a menu-driven approach to handle different student operations.
     *
     * @param allCourses   A list of all available courses.
     * @param allStudents  A list of all students for potential operations that require it.
     * @param scanner      Scanner object to read user input.
     */
    public void manageOperations(List<Course> allCourses, List<Student> allStudents, Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            // Display menu
            System.out.println("---------------------------");
            System.out.println("Student Operations");
            System.out.println("---------------------------");
            System.out.println("1 -- View Courses");
            System.out.println("2 -- Add Course");
            System.out.println("3 -- Drop Course");
            System.out.println("4 -- View Grades");
            System.out.println("5 -- Return to Main Menu");
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
                    viewCourses();
                    break;
                case 2:
                    addCourse(allCourses, allStudents, scanner);
                    break;
                case 3:
                    dropCourse(scanner, allStudents, "src/studentinfo.txt");
                    break;
                case 4:
                    viewGrades(allCourses);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }


    /**
     * Displays the courses currently enrolled by the student along with their grades.
     * Iterates over the courses map and prints each course ID and corresponding grade.
     */
    private void viewCourses() {
        courses.forEach((courseId, grade) -> System.out.println("Course ID: " + courseId + ", Grade: " + grade));
    }

    /**
     * Adds a course to the student's schedule after checking for course existence and potential scheduling conflicts.
     * Ensures the student is not already enrolled in the course and that no time conflicts occur with existing courses.
     *
     * @param allCourses   A list of all available courses to check against.
     * @param allStudents  A list of all students, used for saving data after modification.
     * @param scanner      Scanner object to read user input.
     */
    private void addCourse(List<Course> allCourses, List<Student> allStudents, Scanner scanner) {
        System.out.println("Enter Course ID to add:");
        String courseId = scanner.next();
        Course courseToAdd = allCourses.stream()
                                      .filter(c -> c.getCourseId().equals(courseId))
                                      .findFirst()
                                      .orElse(null);

        if (courseToAdd == null) {
            System.out.println("Course not found.");
            return;
        }

        if (courses.containsKey(courseId)) {
            System.out.println("You are already enrolled in this course.");
            return;
        }

        // Check for time conflicts
        for (Map.Entry<String, String> entry : courses.entrySet()) {
            Course existingCourse = allCourses.stream()
                                              .filter(c -> c.getCourseId().equals(entry.getKey()))
                                              .findFirst()
                                              .orElse(null);

            if (existingCourse != null && hasTimeConflict(existingCourse, courseToAdd)) {
                System.out.println("Cannot add course due to a schedule conflict with " + existingCourse.getCourseId());
                return;
            }
        }

        if (courseToAdd.addStudent(this.getId())) {
            courses.put(courseId, "Not Graded");
            System.out.println("Course added successfully.");
            Student.saveStudentsToFile(allStudents, "src/studentinfo.txt"); // Save changes to file
        } else {
            System.out.println("Could not add course.");
        }
    }

    /**
     * Checks for a scheduling conflict between an existing course and a new course.
     * Compares the days and times of both courses to determine overlap.
     *
     * @param existingCourse The existing course to compare against.
     * @param newCourse      The new course to be added.
     * @return               True if there is a conflict, false otherwise.
     */
    private boolean hasTimeConflict(Course existingCourse, Course newCourse) {
        // Loop through each day of the new course. The days are represented as characters in a string.
        for (char day : newCourse.getDays().toCharArray()) {
            // Check if the existing course occurs on the same day as the new course.
            if (existingCourse.getDays().indexOf(day) != -1) {
                // If they occur on the same day, check for overlapping times.
                // Calls the 'overlaps' method to determine if the time periods of the two courses overlap.
                if (overlaps(existingCourse.getStartTime(), existingCourse.getEndTime(),
                             newCourse.getStartTime(), newCourse.getEndTime())) {
                    // If there is an overlap in time, return true indicating a time conflict.
                    return true;
                }
            }
        }
        // If no conflict is found after checking all days, return false.
        return false;
    }


    /**
     * Determines if two time periods overlap using LocalTime for comparison.
     * Parses the start and end times of each period and compares them for overlap.
     *
     * @param startTime1 Start time of the first period.
     * @param endTime1   End time of the first period.
     * @param startTime2 Start time of the second period.
     * @param endTime2   End time of the second period.
     * @return           True if the periods overlap, false otherwise.
     */
    private boolean overlaps(String startTime1, String endTime1, String startTime2, String endTime2) {
        // Define a DateTimeFormatter to parse the time strings. The format is "HH:mm" (hours and minutes).
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            // Parse the start and end times of the first period using the formatter.
            LocalTime start1 = LocalTime.parse(startTime1.trim(), formatter);
            LocalTime end1 = LocalTime.parse(endTime1.trim(), formatter);

            // Parse the start and end times of the second period using the formatter.
            LocalTime start2 = LocalTime.parse(startTime2.trim(), formatter);
            LocalTime end2 = LocalTime.parse(endTime2.trim(), formatter);

            // Check for overlapping time periods.
            // If start1 is not after end2 and start2 is not after end1, there's an overlap.
            return !start1.isAfter(end2) && !start2.isAfter(end1);
        } catch (DateTimeParseException e) {
            // If there is an error parsing the time strings, catch the exception and print an error message.
            System.err.println("Error parsing time: " + e.getMessage());
            // Return false or handle the error as appropriate. Here, we choose to return false,
            // indicating no overlap as the time could not be parsed correctly.
            return false;
        }
    }



    /**
     * Saves the updated list of students to a file.
     * Formats each student's information into a string and writes to the specified file.
     *
     * @param students   The list of students to be saved.
     * @param filePath   The path of the file to save the student data.
     */
    private static void saveStudentsToFile(List<Student> students, String filePath) {
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

    
    /**
     * Drops a course from the student's schedule.
     * Checks if the course is in the student's current schedule before removing it.
     *
     * @param scanner      Scanner object to read user input.
     * @param allStudents  A list of all students, used for saving data after modification.
     * @param filePath     The path of the file to save the student data.
     */
    private void dropCourse(Scanner scanner, List<Student> allStudents, String filePath) {
    	System.out.println("Enter Course ID to drop:");
	     String courseId = scanner.next();
	     if (this.courses.remove(courseId) != null) {
	         System.out.println("Course dropped successfully.");
	         Student.saveStudentsToFile(allStudents, filePath);
	     } else {
	         System.out.println("Course not found in your schedule.");
	     }
	 }



    /**
     * Displays the grades for each course the student is enrolled in.
     * Iterates over the courses map and prints the grade for each course.
     *
     * @param allCourses A list of all available courses to find course names.
     */
    private void viewGrades(List<Course> allCourses) {
        courses.forEach((courseId, grade) -> {
            Course course = allCourses.stream()
                                      .filter(c -> c.getCourseId().equals(courseId))
                                      .findFirst()
                                      .orElse(null);

            if (course != null) {
                System.out.println("Grade of" + course.getCourseName() + " (" + courseId + "): " + grade);
            } else {
                System.out.println("Course ID: " + courseId + " not found");
            }
        });
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

