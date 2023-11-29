package tests;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import roles.Student;
import courses.Course;

class StudentTest {
    private Student student;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ByteArrayInputStream testIn;
    private List<Course> mockCourses;
    private List<Student> mockStudents;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
        mockCourses = createMockCourses();
        mockStudents = createMockStudents();
        student = new Student("S001", "John Student", "studentj", "pass123", new HashMap<>());
    }

    @AfterEach
    void restoreSystemProperties() {
        System.setOut(originalOut);
    }

    @Test
    void testViewCourses() {
        String simulatedUserInput = "1\n5\n"; // 1 to view courses, 5 to exit
        testIn = new ByteArrayInputStream(simulatedUserInput.getBytes());
        System.setIn(testIn);

        student.manageOperations(mockCourses, mockStudents, new Scanner(System.in));

        String capturedOutput = output.toString();
        Assertions.assertTrue(capturedOutput.contains("Student Operations"));
        Assertions.assertTrue(capturedOutput.contains("1 -- View Courses"));
        Assertions.assertTrue(capturedOutput.contains("5 -- Return to Main Menu"));
    }


    private List<Course> createMockCourses() {
        Course course1 = new Course("CIS101", "Introduction to Computer Science", "Professor", "prof01", "MW", "09:00", "10:30", 30);
        Course course2 = new Course("CIS102", "Data Structures", "Professor", "prof02", "TR", "11:00", "12:30", 25);
        return Arrays.asList(course1, course2);
    }

    private List<Student> createMockStudents() {
        return Arrays.asList(student); // Include only the test student
    }

    // Additional tests can be added for other scenarios
}
