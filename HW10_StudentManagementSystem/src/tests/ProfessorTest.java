package tests;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import roles.Professor;
import roles.Student;
import courses.Course;

class ProfessorTest {
    private Professor professor;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private ByteArrayInputStream testIn;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
        List<String> courseIds = Arrays.asList("CIS101", "CIS102");
        professor = new Professor("prof01", "Professor", "professor", "password", courseIds);
    }

    @AfterEach
    void restoreSystemProperties() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testManageOperationsMenuDisplay() {
        String simulatedUserInput = "3\n"; // Choice to exit the operation
        testIn = new ByteArrayInputStream(simulatedUserInput.getBytes());
        System.setIn(testIn);
        Scanner scanner = new Scanner(System.in);

        List<Course> mockCourses = createMockCourses();
        List<Student> mockStudents = createMockStudents();

        try {
            professor.manageOperations(mockCourses, mockStudents, scanner);
        } catch (Exception e) {
            e.printStackTrace(); // Print any exceptions for debugging
        }

        String capturedOutput = output.toString();
        Assertions.assertTrue(capturedOutput.contains("Professor Operations"));
        Assertions.assertTrue(capturedOutput.contains("1 -- View My Courses"));
        Assertions.assertTrue(capturedOutput.contains("2 -- View Student List for a Course"));
        Assertions.assertTrue(capturedOutput.contains("3 -- Return to Main Menu"));
    }

    @Test
    void testViewProfessorCourses() {
        professor.viewProfessorCourses(createMockCourses());

        String capturedOutput = output.toString();
        Assertions.assertTrue(capturedOutput.contains("Your Course IDs: [CIS101, CIS102]"));
        Assertions.assertTrue(capturedOutput.contains("Course ID: CIS101, Course Name: Introduction to Computer Science"));
        Assertions.assertTrue(capturedOutput.contains("Course ID: CIS102, Course Name: Data Structures"));
    }

    @Test
    void testViewStudentsInCourse() {
        List<Course> courses = createMockCourses();
        List<Student> students = createMockStudents();

        professor.viewStudentsInCourse("CIS101", courses, students);

        String capturedOutput = output.toString();
        Assertions.assertTrue(capturedOutput.contains("Students in CIS101:"));
        Assertions.assertTrue(capturedOutput.contains("Student ID: S001, Name: John Student"));
        Assertions.assertTrue(capturedOutput.contains("Student ID: S002, Name: Jane Learner"));
    }

    @Test
    void testViewStudentsInNonTaughtCourse() {
        List<Course> courses = createMockCourses();
        List<Student> students = createMockStudents();

        professor.viewStudentsInCourse("CIS103", courses, students);

        String capturedOutput = output.toString();
        Assertions.assertTrue(capturedOutput.contains("You do not teach this course."));
    }

    private List<Course> createMockCourses() {
        Course course1 = new Course("CIS101", "Introduction to Computer Science", "Professor", "prof01", "MW", "09:00", "10:30", 30);
        course1.addStudent("S001");
        course1.addStudent("S002");

        Course course2 = new Course("CIS102", "Data Structures", "Professor", "prof01", "TR", "11:00", "12:30", 25);

        return Arrays.asList(course1, course2);
    }

    private List<Student> createMockStudents() {
        Student student1 = new Student("S001", "John Student", "studentj", "pass123", new HashMap<>());
        Student student2 = new Student("S002", "Jane Learner", "learnerj", "pass456", new HashMap<>());

        return Arrays.asList(student1, student2);
    }

}
