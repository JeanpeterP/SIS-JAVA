import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import courses.Course;

import java.util.HashSet;

class CourseTest {

    private Course course;

    @BeforeEach
    void setUp() {
        // Initialize a Course object before each test
        course = new Course("CIS101", "Intro to Computer Science", "Dr. Smith", "001", "MWF", "10:00", "11:00", 30);
    }

    @Test
    void testAddStudent() {
        String studentId = "12345";
        boolean result = course.addStudent(studentId);
        assertTrue(result, "Adding a student should return true");
        assertEquals(1, course.getCurrentEnrollment(), "Current enrollment should be 1 after adding a student");
    }

    @Test
    void testRemoveStudent() {
        String studentId = "12345";
        course.addStudent(studentId);
        boolean result = course.removeStudent(studentId);
        assertTrue(result, "Removing a student should return true");
        assertEquals(0, course.getCurrentEnrollment(), "Current enrollment should be 0 after removing a student");
    }

    @Test
    void testGetCourseId() {
        assertEquals("CIS101", course.getCourseId(), "Course ID should be CIS101");
    }

    @Test
    void testSetCourseId() {
        course.setCourseId("CIS102");
        assertEquals("CIS102", course.getCourseId(), "Course ID should be updated to CIS102");
    }

    // Additional tests can be written for other getters and setters

    @Test
    void testHasTimeConflict() {
        Course otherCourse = new Course("CIS102", "Advanced CS", "Dr. Johnson", "002", "MWF", "10:30", "11:30", 25);
        assertTrue(course.hasTimeConflict(otherCourse), "Courses should have a time conflict");
    }

    @Test
    void testEnrollStudent() {
        String studentId = "12345";
        course.enrollStudent(studentId);
        assertTrue(course.isStudentEnrolled(studentId), "Student should be enrolled in the course");
    }

    @Test
    void testUnenrollStudent() {
        String studentId = "12345";
        course.enrollStudent(studentId);
        course.unenrollStudent(studentId);
        assertFalse(course.isStudentEnrolled(studentId), "Student should be unenrolled from the course");
    }

    // You can add more test cases to cover all scenarios and methods in your Course class
}
