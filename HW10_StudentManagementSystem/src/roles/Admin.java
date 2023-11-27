package roles;

import courses.Course;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ModuleLayer.Controller;
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

	 public void initializeProfessorMap(Map<String, Professor> professorMap) {
	     this.professorMap = new HashMap<>(professorMap);
	 }

	 public void setCourses(List<Course> courses) {
		    this.courses = courses;
		}

	 public void manageAdminOperations(Scanner scanner) {
		    boolean exit = false;
		    while (!exit) {
		        System.out.println("Select option: 1. Manage Courses 2. Manage Students 3. Manage Professors 4. Return to Main Menu");
		        try {
		            int choice = scanner.nextInt();
		            switch (choice) {
		                case 1:
		                    manageCourses(scanner);
		                    break;
		                case 2:
		                    manageStudents(scanner);
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
		            scanner.nextLine(); // Clear the invalid input before retrying
		        }
		    }
		    
		}
    // Method to get the list of courses
    public List<Course> getCourses() {
        return courses;
    }

    // Method to get the list of professors
    public List<Professor> getProfessors() {
        // Return a new list containing all items from the original list
        // This prevents external modifications to the internal list
        return new ArrayList<>(professors);
    }
    
 // Admin methodd
    // Manage courses and it's methods
    public void manageCourses(Scanner scanner) {
        System.out.println("Select operation: 1. Add Course 2. Edit Course 3. Delete Course");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                addCourse(scanner); // Use the instance variable 'courses' within this method
                break;
            case 2:
                editCourse(scanner); // Use the instance variable 'courses' within this method
                break;
            case 3:
                deleteCourse(scanner); // Use the instance variable 'courses' within this method
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }


    // Method to add a course
    public void saveCoursesToFile(String filePath) {
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < this.courses.size(); i++) {
                Course course = this.courses.get(i);
                // Join the course details with a semicolon and a space
                String line = String.join("; ",
                        course.getCourseId().trim(),
                        course.getCourseName().trim(),
                        course.getProfessorName().trim(),
                        course.getDays().trim(),
                        course.getStartTime().trim(),
                        course.getEndTime().trim(),
                        String.valueOf(course.getCapacity()).trim());

                writer.write(line);

                // Add a newline after each course except the last one
                if (i < this.courses.size() - 1) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }












    public void addCourse(Scanner scanner) {
        System.out.println("Please enter the course ID, or type 'q' to end:");
        String courseId = scanner.nextLine();
        if ("q".equalsIgnoreCase(courseId)) return;

        // Check if course already exists
        if (findCourseById(courseId) != null) {
            System.out.println("Course with this ID already exists.");
            return;
        }
        
        System.out.println("Please enter the course name, or type 'q' to end:");
        String courseName = scanner.nextLine();
        if ("q".equalsIgnoreCase(courseName)) return;

        System.out.println("Please enter the course start time (e.g., '19:00'), or type 'q' to end:");
        String startTime = scanner.nextLine();
        if ("q".equalsIgnoreCase(startTime)) return;

        System.out.println("Please enter the course end time (e.g., '21:00'), or type 'q' to end:");
        String endTime = scanner.nextLine();
        if ("q".equalsIgnoreCase(endTime)) return;

        System.out.println("Please enter the course days (e.g., 'MWF' for Monday, Wednesday, Friday), or type 'q' to end:");
        String days = scanner.nextLine();
        if ("q".equalsIgnoreCase(days)) return;

        System.out.println("Please enter the course capacity, or type 'q' to end:");
        int capacity;
        try {
            capacity = Integer.parseInt(scanner.nextLine());
            if (capacity < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Invalid capacity. Please enter a valid number or 'q' to end.");
            return;
        }

        System.out.println("Please enter the course lecturer ID, or type 'q' to end:");
        String lecturerId = scanner.nextLine();
        if ("q".equalsIgnoreCase(lecturerId)) return;

        // Check if the professor exists
        Professor professor = professorMap.get(lecturerId);
        String lecturerName; // Variable to hold lecturer's name
        if (professor != null) {
            System.out.println("Professor found: " + professor.getName());
            lecturerName = professor.getName(); // Use the professor's name
            System.out.println("Courses list: " + courses);
            System.out.println("Professor: " + professor);
            // Check for time conflicts
            for (Course existingCourse : courses) {
                System.out.println("Existing Course: " + existingCourse);
                System.out.println("Comparing Existing Course Professor Name: '" + existingCourse.getProfessorName().trim() + "' with Current Professor Name: '" + professor.getName().trim() + "'");

                // Check for time conflict and take action if conflict exists
                if (existingCourse.getProfessorName().trim().equals(professor.getName().trim()) && timeConflict(existingCourse, startTime, endTime, days)) {
                    System.out.println("Time conflict with another course taught by the same lecturer.");
                    return;
                }
            }
            
        } else {
            System.out.println("Professor not found. Adding a new professor:");
            addProfessor(scanner); // Call addProfessor method
            professor = professorMap.get(lecturerId); // Get the newly added professor
            if (professor == null) {
                System.out.println("Error: Professor was not successfully added.");
                return;
            }
            lecturerName = professor.getName(); // Use the new professor's name
        }
        
        
        // When creating a new course, include the professorId
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

        this.courses.add(newCourse); // Using the instance variable
        System.out.println("Course added successfully.");

        saveCoursesToFile("src/courseinfo.txt");
    }

    private boolean timeConflict(Course existingCourse, String newStartTime, String newEndTime, String newDays) {
        // Check if the days of the week overlap
    	System.out.println("Checking time conflict...");
        System.out.println("Checking time conflict for: " + existingCourse.getCourseId());

        for (char day : newDays.toCharArray()) {
            if (existingCourse.getDays().indexOf(day) != -1) {
                // Parse the times and compare
                if (overlaps(existingCourse.getStartTime(), existingCourse.getEndTime(), newStartTime, newEndTime)) {
                    return true;
                }
            }
        }
        return false;
    }


    // Helper method to check if two time periods overlap
    private boolean overlaps(String startTime1, String endTime1, String startTime2, String endTime2) {
        int start1 = timeToInt(startTime1.trim());
        int end1 = timeToInt(endTime1.trim());
        int start2 = timeToInt(startTime2.trim());
        int end2 = timeToInt(endTime2.trim());
        
        System.out.println("Comparing times: " + startTime1.trim() + "-" + endTime1.trim() + " with " + startTime2.trim() + "-" + endTime2.trim());

        // Check if start or end times of one course is within the other course's time frame
        return (start1 >= start2 && start1 < end2) || (end1 > start2 && end1 <= end2) ||
               (start2 >= start1 && start2 < end1) || (end2 > start1 && end2 <= end1);
    }


    // Helper method to convert time in "HH:mm" format to an integer for easy comparison
    private int timeToInt(String time) {
        String[] parts = time.trim().split(":"); // Trim the time string before splitting
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes; // Convert time to minutes since midnight
    }




    public void editCourse(Scanner scanner) {
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



    // Method to delete a course
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

    private Course findCourseById(String courseId) {
        return this.courses.stream()
                           .filter(course -> course.getCourseId().equals(courseId))
                           .findFirst()
                           .orElse(null);
    }

    // Methods to manage students and professors can be similarly implemented
    // Example: addStudent, editStudent, deleteStudent, addProfessor, editProfessor, deleteProfessor
    
    // Method to manage students
    public void saveStudentsToFile(String filePath) {
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Student student : this.students) {
                String studentData = buildStudentLine(student);
                writer.write(studentData);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private String buildStudentLine(Student student) {
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(student.getId())
                   .append("; ")
                   .append(student.getName())
                   .append("; ")
                   .append(student.getUsername())
                   .append("; ")
                   .append(student.getPassword());

        if (!student.getCourses().isEmpty()) {
            String coursesInfo = student.getCourses().entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));
            lineBuilder.append("; ").append(coursesInfo);
        }
        return lineBuilder.toString();
    }




    public void manageStudents(Scanner scanner) {
    	loadStudentsFromFile("src/studentinfo.txt");
    	
        System.out.println("Select operation: 1. Add Student 2. Edit Student 3. Delete Student");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                addStudent(scanner); // Use the instance variable 'students' within this method
                break;
            case 2:
                editStudent(scanner); // Use the instance variable 'students' within this method
                break;
            case 3:
                deleteStudent(scanner); // Use the instance variable 'students' within this method
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    private void printAllStudentsCourses() {
        for (Student student : students) {
            System.out.println("Student ID: " + student.getId() + " Courses: " + student.getCourses());
        }
    }
    
    private boolean idExists(String id) {
        return students.stream().anyMatch(s -> s.getId().equals(id));
    }

    private boolean usernameExists(String username) {
        return students.stream().anyMatch(s -> s.getUsername().equals(username));
    }
    
    private void addStudent(Scanner scanner) {

        System.out.println("Please enter the student's name, or type 'q' to end:");
        String name = scanner.nextLine();
        if ("q".equalsIgnoreCase(name)) return;
        name = capitalizeName(name);


        
        // Loop for ID input
        String id = "";
        while (true) {
            System.out.println("Please enter the student ID, or type 'q' to end:");
            id = scanner.nextLine();
            if ("q".equalsIgnoreCase(id)) return;
            if (!idExists(id)) break; // Break the loop if the ID does not exist
            System.out.println("A student with this ID already exists. Please try a different ID.");
        }

        // Loop for username input
        String username = "";
        while (true) {
            System.out.println("Please enter the student's username, or type 'q' to end:");
            username = scanner.nextLine();
            if ("q".equalsIgnoreCase(username)) return;
            if (!usernameExists(username)) break; // Break the loop if the username does not exist
            System.out.println("A student with this username already exists. Please try a different username.");
        }

        System.out.println("Please enter the student's password, or type 'q' to end:");
        String password = scanner.nextLine();
        if ("q".equalsIgnoreCase(password)) return;

        Map<String, String> studentCourses = new HashMap<>();
        printAllStudentsCourses();
        while (true) {
            System.out.println("Enter a course ID to add for the student (or type 'q' to finish adding student):");
            String courseId = scanner.nextLine();
            if ("q".equalsIgnoreCase(courseId)) break;

            System.out.println("Enter the grade for the course (or type 'q' to finish adding student):");
            String grade = scanner.nextLine();
            if ("q".equalsIgnoreCase(grade)) break;

            studentCourses.put(courseId.trim(), grade.trim());
        }
        
        // Deep copy of courses map
        Map<String, String> deepCopiedCourses = new HashMap<>();
        for (Map.Entry<String, String> entry : studentCourses.entrySet()) {
            deepCopiedCourses.put(entry.getKey(), entry.getValue());
        }

        Student newStudent = new Student(id.trim(), name.trim(), username.trim(), password.trim(), deepCopiedCourses);
        this.students.add(newStudent);
        printAllStudentsCourses();
        saveStudentsToFile("src/studentinfo.txt");
        System.out.println("Student added successfully.");
    }



    public void editStudent(Scanner scanner) {
        System.out.println("Enter Student ID to edit:");
        String studentId = scanner.nextLine().trim(); // Changed to nextLine for consistency
        Student student = findStudentById(studentId);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        // Edit name
        System.out.println("Current Name: " + student.getName());
        System.out.println("Enter new name, or press enter to keep current:");
        String newName = scanner.nextLine();
        if (!newName.trim().isEmpty()) {
            student.setName(capitalizeName(newName.trim()));
        }

        // Edit username
        System.out.println("Current Username: " + student.getUsername());
        System.out.println("Enter new username, or press enter to keep current:");
        String newUsername = scanner.nextLine();
        if (!newUsername.trim().isEmpty()) {
            student.setUsername(newUsername.trim());
        }

        // Edit password
        System.out.println("Enter new password, or press enter to keep current:");
        String newPassword = scanner.nextLine();
        if (!newPassword.trim().isEmpty()) {
            student.setPassword(newPassword.trim());
        }

        // Edit grades
        System.out.println("Do you want to edit grades? (yes/no)");
        String response = scanner.nextLine().trim();
        if (response.equalsIgnoreCase("yes")) {
            Map<String, String> studentCourses = student.getCourses();
            studentCourses.forEach((courseId, grade) -> {
                System.out.println("Course ID: " + courseId + ", Current Grade: " + grade);
                System.out.println("Enter new grade for this course, or press enter to keep current:");
                String newGrade = scanner.nextLine().trim();
                if (!newGrade.isEmpty()) {
                    studentCourses.put(courseId, newGrade);
                }
            });
            student.setCourses(studentCourses); // Update the student's courses map
        }
        // Save changes to file
        saveStudentsToFile("src/studentinfo.txt");
        System.out.println("Student updated successfully.");
    }


 // ... [existing code]

    private void loadStudentsFromFile(String filePath) {
        // Reset the students list
        this.students.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 5) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String username = parts[2].trim();
                    String password = parts[3].trim();

                    // Create a map for courses and grades
                    Map<String, String> courses = new HashMap<>();

                    // Assuming courses and grades are stored in the format "CourseID1:Grade1,CourseID2:Grade2,..."
                    String[] courseGradePairs = parts[4].trim().split(",");
                    for (String pair : courseGradePairs) {
                        String[] courseGrade = pair.split(":");
                        if (courseGrade.length == 2) {
                            String courseId = courseGrade[0].trim();
                            String grade = courseGrade[1].trim();
                            courses.put(courseId, grade);
                        }
                    }

                    // Create a Student object and add it to the list
                    Student student = new Student(id, name, username, password, courses);
                    students.add(student);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }


    private Student findStudentById(String studentId) {
        System.out.println("Searching for Student ID: " + studentId); // Debugging
        return this.students.stream()
                            .filter(student -> student.getId().equals(studentId))
                            .findFirst()
                            .orElse(null);
    }

    public void deleteStudent(Scanner scanner) {
        System.out.println("Enter Student ID to delete:");
        String studentId = scanner.nextLine().trim();
        
        loadStudentsFromFile("src/studentinfo.txt");
        
        Student studentToDelete = findStudentById(studentId);
        if (studentToDelete != null) {
            this.students.remove(studentToDelete);
            saveStudentsToFile("src/studentinfo.txt");
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    // ... [rest of your existing code]





    
    // Methods for managing professors
    // Method to manage professors
    public void saveProfessorsToFile(String filePath) {
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




    

    public void manageProfessors(Scanner scanner) {
        System.out.println("Select operation: 1. Add Professor 2. Edit Professor 3. Delete Professor");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                addProfessor(scanner); // Use the instance variable 'professors' within this method
                break;
            case 2:
                editProfessor(scanner); // Use the instance variable 'professors' within this method
                break;
            case 3:
                deleteProfessor(scanner); // Use the instance variable 'professors' within this method
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    
 // ... [previous code]

    private void addProfessor(Scanner scanner) {
        System.out.println("Please enter the professor ID, or type 'q' to end:");
        String id = scanner.nextLine();
        if ("q".equalsIgnoreCase(id)) {
            System.out.println("Operation cancelled.");
            return;
        }

        System.out.println("Please enter the professor's name, or type 'q' to end:");
        String name = scanner.nextLine();
        if ("q".equalsIgnoreCase(name)) {
            System.out.println("Operation cancelled.");
            return;
        }
        name = capitalizeName(name);
        
        System.out.println("Please enter the professor's username, or type 'q' to end:");
        String username = scanner.nextLine();
        if ("q".equalsIgnoreCase(username)) {
            System.out.println("Operation cancelled.");
            return;
        }
        username = capitalizeName(username);
        
        System.out.println("Please enter the professor's password, or type 'q' to end:");
        String password = scanner.nextLine();
        if ("q".equalsIgnoreCase(password)) {
            System.out.println("Operation cancelled.");
            return;
        }

        if (this.professorMap.containsKey(id)) {
            System.out.println("Professor ID already exists.");
            return;
        }

        Professor newProfessor = new Professor(id.trim(), name.trim(), username.trim(), password.trim(), new ArrayList<>());
        this.professors.add(newProfessor);
        this.professorMap.put(id, newProfessor);

        saveProfessorsToFile("src/profinfo.txt");
        System.out.println("Professor added successfully.");
    }


    private String capitalizeName(String name) {
        return Arrays.stream(name.split("\\s+"))
                     .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                     .collect(Collectors.joining(" "));
    }




    // ... [rest of the class]







    public void editProfessor(Scanner scanner) {
        System.out.println("Enter Professor ID to edit:");
        String professorId = scanner.nextLine();
        Professor professor = findProfessorById(professorId);

        if (professor == null) {
            System.out.println("Professor not found.");
            return;
        }

        System.out.println("Current Name: " + professor.getName());
        System.out.println("Enter new name, or press enter to keep current:");
        String newName = scanner.nextLine();
        if (!newName.trim().isEmpty()) {
            professor.setName(capitalizeName(newName.trim()));
        }

        System.out.println("Current Username: " + professor.getUsername());
        System.out.println("Enter new username, or press enter to keep current:");
        String newUsername = scanner.nextLine();
        if (!newUsername.trim().isEmpty()) {
            professor.setUsername(newUsername.trim());
        }

        System.out.println("Enter new password, or press enter to keep current:");
        String newPassword = scanner.nextLine();
        if (!newPassword.trim().isEmpty()) {
            professor.setPassword(newPassword.trim());
        }

        System.out.println("Professor updated successfully.");
        saveProfessorsToFile("src/profinfo.txt"); // Save the updated list
    }





    public void setProfessors(List<Professor> professors) {
        this.professors = new ArrayList<>(professors);
        this.professorMap.clear();
        for (Professor p : professors) {
            this.professorMap.put(p.getId(), p);
        }
    }

    private Professor findProfessorById(String professorId) {
        return professorMap.get(professorId);
    }

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



    // Additional methods can be implemented as needed
}
