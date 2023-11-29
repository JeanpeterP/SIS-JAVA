package tests;

import main.Controller;
import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import courses.Course;
import roles.Professor;
import roles.Student;

public class ControllerTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private List<Course> courses;
    private Map<String, Professor> professors;
    private Map<String, Student> students;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));

        // Set up mock data
        courses = new ArrayList<>();
        courses.add(new Course("CIT591", "Introduction to Software Development", "Arvind Bhusnurmath", "MW", "12:00", "13:30", null, 120));
        courses.add(new Course("CIT592", "Mathematical Foundations of Computer Science", "Clayton Greenberg", "TR", "10:00", "11:00", null, 72));

        professors = new HashMap<>();
        professors.put("001", new Professor("001", "Clayton Greenberg", "Greenberg", "password590", new ArrayList<>()));
        professors.put("002", new Professor("002", "Harry Smith", "Smith", "password590", new ArrayList<>()));

        students = new HashMap<>();
        students.put("001", new Student("001", "StudentName1", "testStudent01", "password590", new HashMap<>()));
        students.put("002", new Student("002", "StudentName2", "testStudent02", "password590", new HashMap<>()));

    }

    @AfterEach
    public void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }


    @Test
    void testMainMethod() {
        String input = "4\n"; // Input to exit the application
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Controller.main(new String[]{}); // Pass empty String array as an argument

        String output = outputStreamCaptor.toString().trim();
        Assertions.assertTrue(output.contains("Students Management System"));
        Assertions.assertTrue(output.contains("1 -- Login as a student"));
        Assertions.assertTrue(output.contains("2 -- Login as a professor"));
        Assertions.assertTrue(output.contains("3 -- Login as an admin"));
        Assertions.assertTrue(output.contains("4 -- Quit the system"));
        Assertions.assertTrue(output.contains("Exiting system."));
    }


}
