package tests;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import roles.Admin;
import roles.Student;


class AdminTest {
    private Admin admin;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private ByteArrayInputStream testIn;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
        // Initialize Admin with mock data
        admin = new Admin("admin01", "Admin", "admin", "password");
        // Mock data initialization here...
    }

    @AfterEach
    void restoreSystemProperties() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testManageProfessorsMenuDisplay() {
        String simulatedUserInput = "4\n"; // Exiting the operation
        testIn = new ByteArrayInputStream(simulatedUserInput.getBytes());
        System.setIn(testIn);
        Scanner scanner = new Scanner(System.in);

        try {
            admin.manageProfessors(scanner);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String capturedOutput = output.toString();
        Assertions.assertTrue(capturedOutput.contains("Professor Management"));
        Assertions.assertTrue(capturedOutput.contains("1 -- Add Professor"));
        Assertions.assertTrue(capturedOutput.contains("2 -- Edit Professor"));
        Assertions.assertTrue(capturedOutput.contains("3 -- Delete Professor"));
        Assertions.assertTrue(capturedOutput.contains("4 -- Return to Admin Operations Menu"));
    }

    @Test
    void testManageCoursesMenuDisplay() {
        String simulatedUserInput = "4\n"; // Exiting the operation
        testIn = new ByteArrayInputStream(simulatedUserInput.getBytes());
        System.setIn(testIn);
        Scanner scanner = new Scanner(System.in);

        try {
            admin.manageCourses(scanner);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String capturedOutput = output.toString();
        Assertions.assertTrue(capturedOutput.contains("Course Management"));
        Assertions.assertTrue(capturedOutput.contains("1 -- Add Course"));
        Assertions.assertTrue(capturedOutput.contains("2 -- Edit Course"));
        Assertions.assertTrue(capturedOutput.contains("3 -- Delete Course"));
        Assertions.assertTrue(capturedOutput.contains("4 -- Return to Admin Operations Menu"));
    }


    @Test
    void testManageStudentsMenuDisplay() {
        String simulatedUserInput = "4\n"; // Exiting the operation
        testIn = new ByteArrayInputStream(simulatedUserInput.getBytes());
        System.setIn(testIn);
        Scanner scanner = new Scanner(System.in);

        try {
            List<Student> emptyStudentsList = new ArrayList<>();
            admin.manageStudents(scanner, emptyStudentsList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String capturedOutput = output.toString();
        Assertions.assertTrue(capturedOutput.contains("Student Management"));
        Assertions.assertTrue(capturedOutput.contains("1 -- Add Student"));
        Assertions.assertTrue(capturedOutput.contains("2 -- Edit Student"));
        Assertions.assertTrue(capturedOutput.contains("3 -- Delete Student"));
        Assertions.assertTrue(capturedOutput.contains("4 -- Return to Admin Operations Menu"));
    }



}


