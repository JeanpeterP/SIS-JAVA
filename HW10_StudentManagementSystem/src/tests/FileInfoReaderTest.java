package tests;

import files.FileInfoReader;
import roles.Admin;
import courses.Course;
import roles.Professor;
import roles.Student;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileInfoReaderTest {

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary directory for mock files
        tempDir = Files.createTempDirectory("testFileInfoReader");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Cleanup the temporary directory
        Files.walk(tempDir)
             .sorted(Comparator.reverseOrder())
             .map(Path::toFile)
             .forEach(File::delete);
    }

    @Test
    void testReadCourseInfo() throws IOException {
        // Create mock files with course and professor information
        Path courseInfoPath = Files.createFile(tempDir.resolve("courseInfo.txt"));
        Path profInfoPath = Files.createFile(tempDir.resolve("profInfo.txt"));

        // Mock course data
        Files.write(courseInfoPath, List.of(
                "CIS101;Introduction to Computer Science;John Doe;MW;09:00;10:30;30",
                "CIS102;Data Structures;Jane Smith;TR;11:00;12:30;25"
        ));

        // Mock professor data (Mapping names to IDs)
        Files.write(profInfoPath, List.of(
                "John Doe;P001",
                "Jane Smith;P002"
        ));

        FileInfoReader fileInfoReader = new FileInfoReader(
                courseInfoPath.toString(),
                "", // Empty path for other files
                profInfoPath.toString(),
                ""
        );

        // Read course information
        List<Course> courses = fileInfoReader.readCourseInfo();
     // Debug print the size of courses list
        System.out.println("Courses list size: " + courses.size());

        // Assertions
        assertEquals(2, courses.size());
        assertEquals("CIS101", courses.get(0).getCourseId());
        assertEquals("Introduction to Computer Science", courses.get(0).getCourseName());
        assertEquals("P001", courses.get(0).getProfessorId()); // Asserting Professor ID
        // ... additional assertions for the first course
        assertEquals("CIS102", courses.get(1).getCourseId());
        assertEquals("P002", courses.get(1).getProfessorId()); // Asserting Professor ID
        // ... additional assertions for the second course
    }

    
    @Test
    void testReadProfessorInfo() throws IOException {
        // Create a mock file with professor information
        Path profInfoPath = Files.createFile(tempDir.resolve("profInfo.txt"));
        Files.write(profInfoPath, List.of(
                "001;Clayton Greenberg;greenbergc;password123",
                "002;Harry Smith;smithh;password456"
        ));

        FileInfoReader fileInfoReader = new FileInfoReader(
                "", // Empty path for other files
                "",
                profInfoPath.toString(),
                ""
        );

        // Read professor information
        List<Professor> professors = fileInfoReader.readProfInfo();

        // Debug print the entire list of professors
        for (Professor professor : professors) {
            System.out.println("Professor ID: " + professor.getId() + ", Name: " + professor.getName());
        }

        // Assertions
        assertEquals(2, professors.size());
        assertEquals("001", professors.get(0).getName());
        assertEquals("Clayton Greenberg", professors.get(0).getId());
        assertEquals("002", professors.get(1).getName());
        assertEquals("Harry Smith", professors.get(1).getId());
    }




    @Test
    void testReadStudentInfo() throws IOException {
        // Create a mock file with student information
        Path studentInfoPath = Files.createFile(tempDir.resolve("studentInfo.txt"));
        Files.write(studentInfoPath, List.of(
                "S001;John Student;studentj;pass123;CIS101:A,CIS102:B",
                "S002;Jane Learner;learnerj;pass456;CIS101:B"
        ));

        FileInfoReader fileInfoReader = new FileInfoReader(
                "",
                studentInfoPath.toString(),
                "",
                ""
        );

        // Read student information
        List<Student> students = fileInfoReader.readStudentInfo();

        // Assertions
        assertEquals(2, students.size());
        assertEquals("S001", students.get(0).getId());
    }

    @Test
    void testReadAdminInfo() throws IOException {
        // Create a mock file with admin information
        Path adminInfoPath = Files.createFile(tempDir.resolve("adminInfo.txt"));
        Files.write(adminInfoPath, List.of(
                "A001;Admin One;admin1;adminpass1",
                "A002;Admin Two;admin2;adminpass2"
        ));

        FileInfoReader fileInfoReader = new FileInfoReader(
                "",
                "",
                "",
                adminInfoPath.toString()
        );

        // Read admin information
        List<Admin> admins = fileInfoReader.readAdminInfo();

        // Assertions
        assertEquals(2, admins.size());
        assertEquals("A001", admins.get(0).getId());
    }

    // Additional tests for readStudentInfo, readProfInfo, readAdminInfo
}
