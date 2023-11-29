package roles;
import courses.Course;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Admin extends User {
	 private List<Course> courses; // Instance variable declaration
	 private List<Student> students; // Add this line
	 private List<Professor> professors; // Add this line
	 private Map<String, Professor> professorMap;
	 
	 public Admin(String id, String name, String username, String password) {
		    super(id, name, username, password);
		    this.courses = new ArrayList<>();      // Initialize courses list
		    this.students = new ArrayList<>();     // Initialize students list
		    this.professors = new ArrayList<>();   // Initialize professors list
		    this.professorMap = new HashMap<>();
		    
		}

	 public void setCourses(List<Course> courses) {
		    this.courses = courses;
		}
	 
	 /**
	  * Manages administrative operations related to courses, students, and professors.
	  * Provides a menu for the admin to select from various management options.
	  *
	  * @param scanner The scanner object for reading user input.
	  */
	 public void manageAdminOperations(Scanner scanner) {
		    boolean exit = false;
		    while (!exit) {
		        System.out.println("---------------------------");
		        System.out.println("Admin Operations");
		        System.out.println("---------------------------");
		        System.out.println("1 -- Manage Courses");
		        System.out.println("2 -- Manage Students");
		        System.out.println("3 -- Manage Professors");
		        System.out.println("4 -- Return to Main Menu");
		        System.out.println("");
		        System.out.println("Please enter your option, e.g., '1'.");

		        try {
		            int choice = scanner.nextInt();
		            switch (choice) {
		                case 1:
		                    manageCourses(scanner);
		                    break;
		                case 2:
		                    manageStudents(scanner, students);
		                    break;
		                case 3:
		                    manageProfessors(scanner);
		                    break;
		                case 4:
		                    exit = true;
		                    break;
		                default:
		                    System.out.println("Invalid choice.");
		            }
		        } catch (InputMismatchException e) {
		            System.out.println("Invalid input. Please enter a number between 1 and 4.");
		            scanner.nextLine();
		        }
		    }
		}

    // Method to get the list of courses
    private List<Course> getCourses() {
        return courses;
    }

    // Method to get the list of professors
    private List<Professor> getProfessors() {
        // Return a new list containing all items from the original list
        // This prevents external modifications to the internal list
        return new ArrayList<>(professors);
    }
    

	/**
	 * Manages course-related operations such as adding, editing, or deleting a course.
	 *
	 * @param scanner The scanner object for reading user input.
	 */
    public void manageCourses(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("---------------------------");
            System.out.println("Course Management");
            System.out.println("---------------------------");
            System.out.println("1 -- Add Course");
            System.out.println("2 -- Edit Course");
            System.out.println("3 -- Delete Course");
            System.out.println("4 -- Return to Admin Operations Menu");
            System.out.println("");
            System.out.println("Please enter your option, e.g., '1'.");

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // clear the buffer
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // to consume the invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    addCourse(scanner);
                    break;
                case 2:
                    editCourse(scanner);
                    break;
                case 3:
                    deleteCourse(scanner);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }


    
    /**
     * Saves the current list of courses to a file.
     *
     * @param filePath The path of the file where courses data will be saved.
     */
    private void saveCoursesToFile(String filePath) {
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Course course : this.courses) {
                String line = String.join("; ",
                        course.getCourseId().trim(),
                        course.getCourseName().trim(),
                        course.getProfessorName().trim(),
                        course.getDays().trim(),
                        course.getStartTime().trim(),
                        course.getEndTime().trim(),
                        String.valueOf(course.getCapacity()).trim());

                writer.write(line);
                writer.newLine();  // Ensures each course starts on a new line
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }



    /**
     * Adds a new course based on user input.
     * Allows the user to input various details of the course, and checks for conflicts and duplicates.
     *
     * @param scanner The scanner object for reading user input.
     * @return A message indicating the result of the operation.
     */
    private String addCourse(Scanner scanner) {
        // Prompt the user to enter a course ID and read their input.
        System.out.println("Please enter the course ID, or type 'q' to end:");
        String courseId = scanner.nextLine();
        // Check if the user wants to cancel the operation.
        if ("q".equalsIgnoreCase(courseId)) return "Operation cancelled by user.";

        // Check if the course ID already exists in the system.
        if (findCourseById(courseId) != null) {
            System.out.println("Course with this ID already exists.");
            return "Error: Course with this ID already exists.";
        }

        // Continue prompting for other course details, such as name, start/end times, days, and capacity.
        // Each input is read and validated. If the user enters 'q', the operation is cancelled.
        System.out.println("Please enter the course name, or type 'q' to end:");
        String courseName = scanner.nextLine();
        if ("q".equalsIgnoreCase(courseName)) return "Operation cancelled by user.";

        System.out.println("Please enter the course start time (e.g., '19:00'), or type 'q' to end:");
        String startTime = scanner.nextLine();
        if ("q".equalsIgnoreCase(startTime)) return "Operation cancelled by user.";

        System.out.println("Please enter the course end time (e.g., '21:00'), or type 'q' to end:");
        String endTime = scanner.nextLine();
        if ("q".equalsIgnoreCase(endTime)) return "Operation cancelled by user.";

        System.out.println("Please enter the course days (e.g., 'MWF' for Monday, Wednesday, Friday), or type 'q' to end:");
        String days = scanner.nextLine();
        if ("q".equalsIgnoreCase(days)) return "Operation cancelled by user.";

        System.out.println("Please enter the course capacity, or type 'q' to end:");
        String input = scanner.nextLine();
        if ("q".equalsIgnoreCase(input)) return "Operation cancelled by user.";

        int capacity;
        // Makes sure capacity input is clean
        try {
            capacity = Integer.parseInt(input);
            if (capacity < 0) {
                System.out.println("Invalid capacity: Capacity cannot be negative.");
                return "Error: Invalid capacity - negative number.";
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid capacity. Please enter a valid number.");
            return "Error: Invalid capacity - not a number.";
        }


        System.out.println("Please enter the course lecturer ID, or type 'q' to end:");
        String lecturerId = scanner.nextLine();
        if ("q".equalsIgnoreCase(lecturerId)) return "Operation cancelled by user.";

        // Check if the professor exists in the system using the provided ID.
        System.out.println("Current professorMap: " + professorMap);

        Professor professor = professorMap.get(lecturerId);
        if (professor == null) {
            System.out.println("Professor ID not found for lecturerId: " + lecturerId);

            // Handle the case where the professor is not found, and add a new professor.
            System.out.println("Professor not found. Adding a new professor:");
            boolean isProfessorAdded = addProfessor(scanner); // Modify addProfessor to return a boolean
            if (!isProfessorAdded) {
                return "Error: Professor was not successfully added.";
            }
            professor = professorMap.get(lecturerId); // Get the newly added professor
        }
        
        // Check for time conflicts with existing courses
        for (Course existingCourse : courses) {
            // Only compare with courses taught by the same professor
            if (existingCourse.getProfessorId().equals(lecturerId)) {
                // Check if the existing course conflicts with the new course details
                if (existingCourse.hasTimeConflict(startTime, endTime, days)) {
                    System.out.println("Time conflict with another course taught by the same lecturer.");
                    return "Error: Time conflict with another course.";
                }
            }
        }

        
        // Create a new course object and add it to the list of courses.
        Course newCourse = new Course(
            courseId.trim(),
            courseName.trim(),
            professor.getName().trim(), // Professor's name
            professor.getId().trim(),   // Professor's ID
            days.trim(),
            startTime.trim(),
            endTime.trim(),
            capacity
        );

        // Successfully add the course and save the updated course list to a file.
        this.courses.add(newCourse);
        System.out.println("Course added successfully.");

        // Save the course list to the file
        saveCoursesToFile("src/courseinfo.txt");

        // Return a success message
        return "Course added successfully.";

    }

	/**
	 * Checks for time conflicts between the new course and existing courses.
	 *
	 * @param existingCourse The existing course to compare against.
	 * @param newStartTime   The start time of the new course.
	 * @param newEndTime     The end time of the new course.
	 * @param newDays        The days the new course will take place.
	 * @return true if there is a conflict, false otherwise.
	 */
    private boolean timeConflict(Course existingCourse, String newStartTime, String newEndTime, String newDays) {
        // Loop through each day the new course occurs on.
        for (char day : newDays.toCharArray()) {
            // Check if the existing course occurs on the same day.
            if (existingCourse.getDays().indexOf(day) != -1) {
                // Check if the time of the new course overlaps with the existing course.
                if (overlaps(existingCourse.getStartTime(), existingCourse.getEndTime(), newStartTime, newEndTime)) {
                    // Return true if there is a time conflict.
                    return true;
                }
            }
        }
        // Return false if no conflicts are found.
        return false;
    }



    /**
     * Helper method to determine if two time periods overlap.
     *
     * @param startTime1 Start time of the first period.
     * @param endTime1   End time of the first period.
     * @param startTime2 Start time of the second period.
     * @param endTime2   End time of the second period.
     * @return true if the periods overlap, false otherwise.
     */
    private boolean overlaps(String startTime1, String endTime1, String startTime2, String endTime2) {
        // Convert time strings to integer representations of minutes since midnight.
        int start1 = timeToInt(startTime1.trim());
        int end1 = timeToInt(endTime1.trim());
        int start2 = timeToInt(startTime2.trim());
        int end2 = timeToInt(endTime2.trim());

        // Check if the time intervals of the two courses overlap.
        return (start1 >= start2 && start1 < end2) || (end1 > start2 && end1 <= end2) ||
               (start2 >= start1 && start2 < end1) || (end2 > start1 && end2 <= end1);
    }


    /**
     * Converts a time string in "HH:mm" format to an integer representing minutes since midnight.
     *
     * @param time The time string.
     * @return The number of minutes since midnight.
     */
    private int timeToInt(String time) {
        String[] parts = time.trim().split(":"); // Trim the time string before splitting
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes; // Convert time to minutes since midnight
    }



    /**
     * Edits an existing course based on user input.
     * Provides options to edit various attributes of a course such as the name, professor, timing, etc.
     *
     * @param scanner The scanner object for reading user input.
     */
    private void editCourse(Scanner scanner) {
        System.out.println("Available courses: ");
        for (Course c : this.courses) {
            System.out.println(c.getCourseId() + " - " + c.getCourseName());
        }

        System.out.println("Enter Course ID to edit:");
        String courseId = scanner.nextLine();
        Course course = findCourseById(courseId);

        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        // Edit course name
        System.out.println("Current Course Name: " + course.getCourseName());
        System.out.println("Enter new course name, or press enter to keep current:");
        String newCourseName = scanner.nextLine();
        if (!newCourseName.trim().isEmpty()) {
            course.setCourseName(newCourseName.trim());
        }


        // Edit professor name
        System.out.println("Current Professor Name: " + course.getProfessorName());
        System.out.println("Enter new professor ID, or press enter to keep current:");
        String newProfessorId = scanner.nextLine();
        if (!newProfessorId.trim().isEmpty()) {
            Professor newProfessor = professorMap.get(newProfessorId.trim());
            if (newProfessor != null) {
                course.setProfessorName(newProfessor.getName());
                course.setProfessorId(newProfessorId.trim());
                System.out.println("Professor updated to: " + newProfessor.getName());
            } else {
                System.out.println("Professor ID not found. Keeping current professor.");
            }
        }

        // Edit course days
        System.out.println("Current Course Days: " + course.getDays());
        System.out.println("Enter new course days, or press enter to keep current:");
        String newDays = scanner.nextLine();
        if (!newDays.trim().isEmpty()) {
            course.setDays(newDays.trim());
        }

        // Edit start time
        System.out.println("Current Start Time: " + course.getStartTime());
        System.out.println("Enter new start time, or press enter to keep current:");
        String newStartTime = scanner.nextLine();
        if (!newStartTime.trim().isEmpty()) {
            course.setStartTime(newStartTime.trim());
        }

        // Edit end time
        System.out.println("Current End Time: " + course.getEndTime());
        System.out.println("Enter new end time, or press enter to keep current:");
        String newEndTime = scanner.nextLine();
        if (!newEndTime.trim().isEmpty()) {
            course.setEndTime(newEndTime.trim());
        }

        // Edit course capacity
        System.out.println("Current Capacity: " + course.getCapacity());
        System.out.println("Enter new capacity, or press enter to keep current:");
        String newCapacityStr = scanner.nextLine();
        if (!newCapacityStr.trim().isEmpty()) {
            try {
                int newCapacity = Integer.parseInt(newCapacityStr.trim());
                course.setCapacity(newCapacity);
            } catch (NumberFormatException e) {
                System.out.println("Invalid capacity entered. Keeping current capacity.");
            }
        }

        System.out.println("Course updated successfully.");
        saveCoursesToFile("src/courseinfo.txt");
    }



    /**
     * Deletes a course based on the course ID provided by the user.
     *
     * @param scanner The scanner object for reading user input.
     */
    private void deleteCourse(Scanner scanner) {
        System.out.println("Enter Course ID to delete:");
        String courseId = scanner.next();
        
        Course courseToDelete = findCourseById(courseId);
        if (courseToDelete != null) {
            this.courses.remove(courseToDelete);
            System.out.println("Course deleted successfully.");
            saveCoursesToFile("src/courseinfo.txt");
        } else {
            System.out.println("Course not found.");
        }
    }
    
    /**
     * Finds a course by its ID.
     *
     * @param courseId The ID of the course to find.
     * @return The Course object if found, or null if not found.
     */
    private Course findCourseById(String courseId) {
        return this.courses.stream()
                           .filter(course -> course.getCourseId().equals(courseId))
                           .findFirst()
                           .orElse(null);
    }

    /**
     * Saves the current list of students to a file.
     *
     * @param filePath The file path where the students' data will be saved.
     */
    public void saveStudentsToFile(String filePath) {
        // Create a new file object with the specified file path.
        File file = new File(filePath);

        // Use try-with-resources to automatically close the BufferedWriter.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Iterate through each student in the list.
            for (Student student : this.students) {
                // Build a string line for each student's data.
                String line = buildStudentLine(student);
                // Write the student's data line to the file.
                writer.write(line);
                // Add a newline after each student's data.
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle any IO exceptions that might occur during file writing.
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Builds a line representing a student's information for file storage.
     *
     * @param student The student object to be formatted as a line.
     * @return A formatted string representing the student's information.
     */
    private String buildStudentLine(Student student) {
        // Initialize a StringBuilder to construct the line.
        StringBuilder lineBuilder = new StringBuilder();
        // Append the student's ID, name, username, and password to the line.
        lineBuilder.append(student.getId())
                   .append("; ")
                   .append(student.getName())
                   .append("; ")
                   .append(student.getUsername())
                   .append("; ")
                   .append(student.getPassword());

        // Check if the student is enrolled in any courses.
        if (!student.getCourses().isEmpty()) {
            // Convert the student's courses map into a string representation.
            String coursesInfo = student.getCourses().entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));
            // Append the courses information to the line.
            lineBuilder.append("; ").append(coursesInfo);
        }
        // Return the constructed line as a string.
        return lineBuilder.toString();
    }




    /**
     * Manages student-related operations such as adding, editing, or deleting a student.
     *
     * @param scanner          The scanner object for reading user input.
     * @param updatedStudents  The list of updated student data.
     */
    public void manageStudents(Scanner scanner, List<Student> updatedStudents) {
        boolean exit = false;
        while (!exit) {
            loadStudentsFromFile("src/studentinfo.txt");
            this.students = new ArrayList<>(updatedStudents);

            System.out.println("---------------------------");
            System.out.println("Student Management");
            System.out.println("---------------------------");
            System.out.println("1 -- Add Student");
            System.out.println("2 -- Edit Student");
            System.out.println("3 -- Delete Student");
            System.out.println("4 -- Return to Admin Operations Menu");
            System.out.println("");
            System.out.println("Please enter your option, e.g., '1'.");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addStudent(scanner);
                    break;
                case 2:
                    editStudent(scanner);
                    break;
                case 3:
                    deleteStudent(scanner);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    
    /**
     * Prints all students along with their enrolled courses.
     */
    private void printAllStudentsCourses() {
        for (Student student : students) {
            System.out.println("Student ID: " + student.getId() + " Courses: " + student.getCourses());
        }
    }
    
    /**
     * Checks if a student ID already exists in the system.
     *
     * @param id The student ID to check.
     * @return true if the ID exists, false otherwise.
     */
    private boolean idExists(String id) {
        return students.stream().anyMatch(s -> s.getId().equals(id));
    }

    /**
     * Checks if a username already exists in the system.
     *
     * @param username The username to check.
     * @return true if the username exists, false otherwise.
     */
    private boolean usernameExists(String username) {
        return students.stream().anyMatch(s -> s.getUsername().equals(username));
    }
    
    /**
     * Adds a new student based on user input.
     *
     * @param scanner The scanner object for reading user input.
     */
    private void addStudent(Scanner scanner) {
        // Prompt for the student's name and read input. Exit if 'q' is entered.
        System.out.println("Please enter the student's name, or type 'q' to end:");
        String name = scanner.nextLine();
        if ("q".equalsIgnoreCase(name)) return;
        // Capitalize the entered name for consistency.
        name = capitalizeName(name);

        // Loop to obtain a unique student ID. Exit if 'q' is entered.
        String id = "";
        while (true) {
            System.out.println("Please enter the student ID, or type 'q' to end:");
            id = scanner.nextLine();
            if ("q".equalsIgnoreCase(id)) return;
            // Check if the ID already exists. If not, exit the loop.
            if (!idExists(id)) break;
            System.out.println("A student with this ID already exists. Please try a different ID.");
        }

        // Loop to obtain a unique username. Exit if 'q' is entered.
        String username = "";
        while (true) {
            System.out.println("Please enter the student's username, or type 'q' to end:");
            username = scanner.nextLine();
            if ("q".equalsIgnoreCase(username)) return;
            // Check if the username already exists. If not, exit the loop.
            if (!usernameExists(username)) break;
            System.out.println("A student with this username already exists. Please try a different username.");
        }

        // Prompt for the student's password and read input. Exit if 'q' is entered.
        System.out.println("Please enter the student's password, or type 'q' to end:");
        String password = scanner.nextLine();
        if ("q".equalsIgnoreCase(password)) return;

        // Initialize a map to hold the student's courses and grades.
        Map<String, String> studentCourses = new HashMap<>();
        // Loop to add courses and grades for the student. Exit if 'q' is entered.
        while (true) {
            System.out.println("Enter a course ID to add for the student (or type 'q' to finish adding student):");
            String courseId = scanner.nextLine();
            if ("q".equalsIgnoreCase(courseId)) break;

            System.out.println("Enter the grade for the course (or type 'q' to finish adding student):");
            String grade = scanner.nextLine();
            if ("q".equalsIgnoreCase(grade)) break;

            studentCourses.put(courseId.trim(), grade.trim());
        }

        // Create a deep copy of the courses map to ensure data integrity.
        Map<String, String> deepCopiedCourses = new HashMap<>();
        for (Map.Entry<String, String> entry : studentCourses.entrySet()) {
            deepCopiedCourses.put(entry.getKey(), entry.getValue());
        }

        // Create a new Student object and add it to the list.
        Student newStudent = new Student(id.trim(), name.trim(), username.trim(), password.trim(), deepCopiedCourses);
        this.students.add(newStudent);
        // Print the courses and grades of all students for verification.
        printAllStudentsCourses();
        // Save the updated student list to a file.
        saveStudentsToFile("src/studentinfo.txt");

        // Confirm successful addition of the student.
        System.out.println("Student added successfully.");
    }


    /**
     * Edits an existing student based on user input.
     * Provides options to edit various attributes of a student such as name, username, password, and grades.
     *
     * @param scanner The scanner object for reading user input.
     */
    private void editStudent(Scanner scanner) {
        // Prompt for the ID of the student to be edited and retrieve the input
        System.out.println("Enter Student ID to edit:");
        String studentId = scanner.nextLine().trim(); // Trimming to remove any leading/trailing spaces
        // Find the student by their ID
        Student student = findStudentById(studentId);

        // Check if the student exists; if not, exit the method
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        // Edit the student's name
        System.out.println("Current Name: " + student.getName());
        System.out.println("Enter new name, or press enter to keep current:");
        String newName = scanner.nextLine();
        if (!newName.trim().isEmpty()) {
            // Update the name only if a new name is provided
            student.setName(capitalizeName(newName.trim()));
        }

        // Edit the student's username
        System.out.println("Current Username: " + student.getUsername());
        System.out.println("Enter new username, or press enter to keep current:");
        String newUsername = scanner.nextLine();
        if (!newUsername.trim().isEmpty()) {
            // Update the username only if a new username is provided
            student.setUsername(newUsername.trim());
        }

        // Edit the student's password
        System.out.println("Enter new password, or press enter to keep current:");
        String newPassword = scanner.nextLine();
        if (!newPassword.trim().isEmpty()) {
            // Update the password only if a new password is provided
            student.setPassword(newPassword.trim());
        }

        // Prompt to edit grades and process accordingly
        System.out.println("Do you want to edit grades? (yes/no)");
        String response = scanner.nextLine().trim();
        if (response.equalsIgnoreCase("yes")) {
            Map<String, String> studentCourses = student.getCourses();
            // Iterate over each course to possibly update grades
            studentCourses.forEach((courseId, grade) -> {
                System.out.println("Course ID: " + courseId + ", Current Grade: " + grade);
                System.out.println("Enter new grade for this course, or press enter to keep current:");
                String newGrade = scanner.nextLine().trim();
                if (!newGrade.isEmpty()) {
                    // Update the grade for the course
                    studentCourses.put(courseId, newGrade);
                }
            });
            // Set the updated courses and grades map
            student.setCourses(studentCourses);
        }

        // Save the updated student information to the file
        saveStudentsToFile("src/studentinfo.txt");
        // Confirm the successful update
        System.out.println("Student updated successfully.");
    }



 // ... [existing code]
    
    /**
     * Loads students' information from a file into the system.
     *
     * @param filePath The file path from where to load the students' data.
     */
    private void loadStudentsFromFile(String filePath) {
        // Clearing the existing students list to refresh data from file
        this.students.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Iterating through each line of the file
            while ((line = reader.readLine()) != null) {
                // Splitting each line into parts using ";" as delimiter
                String[] parts = line.split(";");
                if (parts.length >= 5) { // Ensuring line has enough parts to form a student
                    // Extracting and trimming student information
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String username = parts[2].trim();
                    String password = parts[3].trim();

                    // Initializing a map to store course IDs and grades
                    Map<String, String> courses = new HashMap<>();
                    // Splitting the course and grade information
                    String[] courseGradePairs = parts[4].trim().split(",");
                    for (String pair : courseGradePairs) {
                        String[] courseGrade = pair.split(":");
                        if (courseGrade.length == 2) { // Each pair should have two parts: course ID and grade
                            // Storing course ID and grade in the map
                            courses.put(courseGrade[0].trim(), courseGrade[1].trim());
                        }
                    }

                    // Creating a new Student object and adding it to the list
                    Student student = new Student(id, name, username, password, courses);
                    students.add(student);
                }
            }
        } catch (IOException e) {
            // Handling potential I/O errors
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }


    /**
     * Finds a student by their ID.
     *
     * @param studentId The ID of the student to find.
     * @return The Student object if found, otherwise null.
     */
    private Student findStudentById(String studentId) {
        // Debugging log to indicate the search operation
        System.out.println("Searching for Student ID: " + studentId);
        // Using a stream to find the first student with a matching ID
        return this.students.stream()
                            .filter(student -> student.getId().equals(studentId))
                            .findFirst()
                            .orElse(null); // Returns null if no match is found
    }

    
    /**
     * Deletes a student from the system based on their ID.
     *
     * @param scanner The scanner object for reading user input.
     */
    public void deleteStudent(Scanner scanner) {
        // Prompt for the student ID to be deleted
        System.out.println("Enter Student ID to delete:");
        String studentId = scanner.nextLine().trim(); // Trimming to remove whitespace

        // Reload students from file to ensure up-to-date information
        loadStudentsFromFile("src/studentinfo.txt");
        
        // Find the student to be deleted
        Student studentToDelete = findStudentById(studentId);
        if (studentToDelete != null) {
            // Remove the student if found
            this.students.remove(studentToDelete);
            // Save the updated list back to the file
            saveStudentsToFile("src/studentinfo.txt");
            System.out.println("Student deleted successfully.");
        } else {
            // Handle case where student is not found
            System.out.println("Student not found.");
        }
    }

    /**
     * Saves the current list of professors to a file.
     *
     * @param filePath The file path where the professors' data will be saved.
     */
    private void saveProfessorsToFile(String filePath) {
        File file = new File(filePath);

        // Rewrite the entire file with the current state of professors list
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Professor professor : this.professors) {
                String line = String.join("; ",
                        professor.getName(),
                        professor.getId(),
                        professor.getUsername(),
                        professor.getPassword()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }


    
    /**
     * Manages professor-related operations such as adding, editing, or deleting a professor.
     *
     * @param scanner The scanner object for reading user input.
     */
    public void manageProfessors(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("---------------------------");
            System.out.println("Professor Management");
            System.out.println("---------------------------");
            System.out.println("1 -- Add Professor");
            System.out.println("2 -- Edit Professor");
            System.out.println("3 -- Delete Professor");
            System.out.println("4 -- Return to Admin Operations Menu");
            System.out.println("");
            System.out.println("Please enter your option, e.g., '1'.");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addProfessor(scanner);
                    break;
                case 2:
                    editProfessor(scanner);
                    break;
                case 3:
                    deleteProfessor(scanner);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }



    
    /**
     * Adds a new professor to the system based on user input.
     *
     * @param scanner The scanner object for reading user input.
     * @return true if the professor is successfully added, false otherwise.
     */
    private boolean addProfessor(Scanner scanner) {
        // Prompting for professor ID and handling cancellation
        System.out.println("Please enter the professor ID, or type 'q' to end:");
        String id = scanner.nextLine();
        if ("q".equalsIgnoreCase(id)) {
            System.out.println("Operation cancelled.");
            return false;
        }

        // Prompting for professor's name and handling cancellation
        System.out.println("Please enter the professor's name, or type 'q' to end:");
        String name = scanner.nextLine();
        if ("q".equalsIgnoreCase(name)) {
            System.out.println("Operation cancelled.");
            return false;
        }
        name = capitalizeName(name); // Capitalizing the name for consistency

        // Prompting for username and handling cancellation
        System.out.println("Please enter the professor's username, or type 'q' to end:");
        String username = scanner.nextLine();
        if ("q".equalsIgnoreCase(username)) {
            System.out.println("Operation cancelled.");
            return false;
        }
        username = capitalizeName(username); // Capitalizing the username

        // Prompting for password and handling cancellation
        System.out.println("Please enter the professor's password, or type 'q' to end:");
        String password = scanner.nextLine();
        if ("q".equalsIgnoreCase(password)) {
            System.out.println("Operation cancelled.");
            return false;
        }

        // Checking if professor ID already exists
        if (this.professorMap.containsKey(id)) {
            System.out.println("Professor ID already exists.");
            return false;
        }

        // Creating and adding the new professor to the list and map
        Professor newProfessor = new Professor(id.trim(), name.trim(), username.trim(), password.trim(), new ArrayList<>());
        this.professors.add(newProfessor);
        this.professorMap.put(id, newProfessor);

        // Saving the updated professor list to file
        saveProfessorsToFile("src/profinfo.txt");
        System.out.println("Professor added successfully.");
        return true;
    }



    private String capitalizeName(String name) {
        // Splitting the name into words and capitalizing each word
        return Arrays.stream(name.split("\\s+"))
                     .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                     .collect(Collectors.joining(" "));
    }


    /**
     * Edits an existing professor's information based on user input.
     *
     * @param scanner The scanner object for reading user input.
     */
    private void editProfessor(Scanner scanner) {
        // Prompting for professor ID to edit
        System.out.println("Enter Professor ID to edit:");
        String professorId = scanner.nextLine();
        // Finding the professor by ID
        Professor professor = findProfessorById(professorId);

        if (professor == null) {
            System.out.println("Professor not found.");
            return;
        }

        // Editing name
        System.out.println("Current Name: " + professor.getName());
        System.out.println("Enter new name, or press enter to keep current:");
        String newName = scanner.nextLine();
        if (!newName.trim().isEmpty()) {
            professor.setName(capitalizeName(newName.trim()));
        }

        // Editing username
        System.out.println("Current Username: " + professor.getUsername());
        System.out.println("Enter new username, or press enter to keep current:");
        String newUsername = scanner.nextLine();
        if (!newUsername.trim().isEmpty()) {
            professor.setUsername(newUsername.trim());
        }

        // Editing password
        System.out.println("Enter new password, or press enter to keep current:");
        String newPassword = scanner.nextLine();
        if (!newPassword.trim().isEmpty()) {
            professor.setPassword(newPassword.trim());
        }

        // Confirmation of update
        System.out.println("Professor updated successfully.");
        // Saving updated list to file
        saveProfessorsToFile("src/profinfo.txt");
    }





    /**
     * Sets the list of professors and updates the corresponding map.
     *
     * @param professors The list of professors to set.
     */
    public void setProfessors(List<Professor> professors) {
        // Creating a fresh copy of the list
        this.professors = new ArrayList<>(professors);
        // Clearing and updating the professorMap for quick ID-based access
        this.professorMap.clear();
        for (Professor p : professors) {
            this.professorMap.put(p.getId(), p);
        }
    }

    
    /**
     * Finds a professor by their ID.
     *
     * @param professorId The ID of the professor to find.
     * @return The Professor object if found, otherwise null.
     */
    private Professor findProfessorById(String professorId) {
        return professorMap.get(professorId);
    }
    
    /**
     * Deletes a professor from the system based on their ID.
     *
     * @param scanner The scanner object for reading user input.
     */
    private void deleteProfessor(Scanner scanner) {
        System.out.println("Enter Professor ID to delete:");
        String professorId = scanner.next();
        Professor professorToDelete = findProfessorById(professorId);

        if (professorToDelete != null) {
            this.professors.remove(professorToDelete);
            professorMap.remove(professorId);
            System.out.println("Professor deleted successfully.");
            saveProfessorsToFile("src/profinfo.txt"); // Reflect changes in the file
        } else {
            System.out.println("Professor not found.");
        }
    }

}
