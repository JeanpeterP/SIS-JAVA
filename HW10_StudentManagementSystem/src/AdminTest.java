import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import courses.Course;
import roles.Admin;
import roles.Professor;

class AdminTest {
    private Admin admin;

    private  ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
    private  PrintStream originalOut = System.out;
    @BeforeEach
    void setUp() {
        // Initialize mock data
        List<Course> mockCourses = new ArrayList<>();
        List<Professor> mockProfessors = new ArrayList<>();
        mockProfessors.add(new Professor("025", "Professor Name 1", "username1", "password1", new ArrayList<>()));
        mockProfessors.add(new Professor("026", "Professor Name 2", "username2", "password2", new ArrayList<>()));
        mockCourses.add(new Course("CIS700", "Existing Course", "Existing Professor", "025", "MWF", "09:00", "10:00", 30));

        // Initialize Admin with mock data
        admin = new Admin("admin01", "Admin", "admin", "password");
        admin.setCourses(mockCourses);
        admin.setProfessors(mockProfessors);

        // Initialize professor map
        Map<String, Professor> professorMap = new HashMap<>();
        for (Professor professor : mockProfessors) {
            professorMap.put(professor.getId(), professor);
        }
        admin.initializeProfessorMap(professorMap);

        // Set up the captured output stream
        capturedOut = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(capturedOut));
    }
    @BeforeEach
    void setUpOutput() {
        System.setOut(new PrintStream(capturedOut));
    }

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
//	Course handling part of add courses
    @Test
    void testAddCourse() {
        // New course ID that doesn't exist in mock data
        String newCourseId = "CIS699";
        String simulatedUserInput = newCourseId + "\nNew Course\n09:00\n10:00\nMWF\n25\n025\n";
        InputStream originalIn = System.in;

        try {
            System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
            Scanner scanner = new Scanner(System.in);
            admin.addCourse(scanner);

            String output = capturedOut.toString();
            assertTrue(output.contains("Course added successfully."));
        } finally {
            System.setIn(originalIn);
        }
    }


    @Test
    void testAddCourseWithExistingID() {
        String existingCourseId = "CIS548";
        admin.getCourses().add(new Course(existingCourseId, "Existing Course", "Existing Professor", "MW", "08:00", "10:00", "025", 30));

        String simulatedUserInput = existingCourseId + "\nNew Course\n09:00\n10:00\nMWF\n25\n025\n";
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
            Scanner scanner = new Scanner(System.in);
            admin.addCourse(scanner);

            String output = capturedOut.toString();
            assertTrue(output.contains("Course with this ID already exists."));
        } finally {
            System.setIn(originalIn);
        }
    }
    
    @Test
    void testAddCourseWithNonExistentProfessor() {
        String simulatedUserInput = "CIS700\nNew Course\n09:00\n10:00\nMWF\n25\n948\n";
        InputStream originalIn = System.in;

        try {
            System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
            Scanner scanner = new Scanner(System.in);
            admin.addCourse(scanner);

            // Capture and assert the output
            String output = capturedOut.toString();
            assertTrue(output.contains("Professor not found. Adding a new professor: "));
            assertFalse(output.contains("Course added successfully."), "Course should not be added with an invalid professor ID");
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    void testEditCourse() {
        // Use the existing course ID from mock data
        String courseId = "CIS700";
        String newCourseName = "Updated Course Name";
        String newProfessorId = "025"; // Existing professor ID in mock data
        String newDays = "MF"; // New days
        String newStartTime = "10:00"; // New start time
        String newEndTime = "11:00"; // New end time
        String newCapacity = "35"; // New capacity

        String simulatedUserInput = String.format("%s\n%s\n%s\n%s\n%s\n%s\n%s\n", 
            courseId, newCourseName, newProfessorId, newDays, newStartTime, newEndTime, newCapacity);

        InputStream originalIn = System.in;

        try {
            System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
            Scanner scanner = new Scanner(System.in);
            admin.editCourse(scanner);

            String output = capturedOut.toString();
            assertTrue(output.contains("Course updated successfully."));
        } finally {
            System.setIn(originalIn);
        }
    }






//    Professor handling part of Admin class
    @Test
    void testAddProfessor() {
        // Simulate user input: first for selecting the operation, then for adding the professor details
        String simulatedUserInput = "1\n699\nNew Professor\nnewprof\npassword\n";
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
            Scanner scanner = new Scanner(System.in);
            admin.manageProfessors(scanner);

            String output = capturedOut.toString();
            System.out.println("Debug output: " + output); // Debugging line to print output
            assertTrue(output.contains("Professor added successfully."));
        } catch (Exception e) {
            e.printStackTrace(); // Print any exception that might occur
        } finally {
            System.setIn(originalIn);
        }
    }
    
    @Test
    void testAddProfessorWithExistingId() {
        // Adding an existing professor to the list
        Professor existingProfessor = new Professor("999", "Existing Professor", "existingprof", "password", null);
        admin.getProfessors().add(existingProfessor);
        admin.initializeProfessorMap(Map.of(existingProfessor.getId(), existingProfessor));

        String simulatedUserInput = "1\n999\nNew Professor\nnewprof\npassword\n";
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
            Scanner scanner = new Scanner(System.in);
            admin.manageProfessors(scanner);

            String output = capturedOut.toString();
            assertTrue(output.contains("Professor ID already exists."));
            assertFalse(output.contains("Professor added successfully."));
        } finally {
            System.setIn(originalIn);
        }
    }


    @Test
    void testCancelAddingProfessorMidway() {
        String simulatedUserInput = "1\n700\nq"; // User decides to quit after entering the ID
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
            Scanner scanner = new Scanner(System.in);
            admin.manageProfessors(scanner);

            String output = capturedOut.toString();
            assertFalse(output.contains("Professor added successfully."));
            assertTrue(output.contains("Operation cancelled") || output.contains("Invalid input")); // Assuming the system has a cancellation or error message
        } finally {
            System.setIn(originalIn);
        }
    }

}


