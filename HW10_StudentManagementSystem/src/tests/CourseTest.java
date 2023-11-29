package tests;
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
        course = new Course("CIS101", "Intro to Computer Science", "Dr. Smith", "", "MWF", "10:00", "11:00", 30);
    }
    
    @Test
    void testToString() {
        // Initialize a Course object with known values
        Course course = new Course("CIS101", "Intro to Computer Science", "Dr. Smith", "P001", "MWF", "09:00", "10:30", 30);

        // Expected string based on the Course's toString format
        String expected = "Course{" +
                          "courseId='CIS101'" +
                          ", professorName='Dr. Smith'" +
                          ", days='MWF'" +
                          ", startTime='09:00'" +
                          ", endTime='10:30'" +
                          '}';

        // Check if the actual toString output matches the expected string
        assertEquals(expected, course.toString(), "toString should return the correctly formatted string");
    }

    @Test
    void testAddStudent() {
        String studentId = "12345";
        boolean result = course.addStudent(studentId);
        assertTrue(result, "Adding a student should return true");
        assertEquals(1, course.getCurrentEnrollment(), "Current enrollment should be 1 after adding a student");
    }
    
    @Test
    void testAddStudentWhenCourseIsFull() {
        // Fill the course to its capacity
        for (int i = 0; i < course.getCapacity(); i++) {
            course.addStudent("Student" + i);
        }
        // Try adding another student
        assertFalse(course.addStudent("OverloadStudent"), "Should return false when adding a student to a full course");
    }

    @Test
    void testAddSameStudentTwice() {
        String studentId = "12345";
        course.addStudent(studentId);
        assertFalse(course.addStudent(studentId), "Should return false when adding the same student again");
    }

    @Test
    void testAddStudentWithInvalidId() {
        assertFalse(course.addStudent(""), "Should return false when adding a student with an empty ID");
        assertFalse(course.addStudent(null), "Should return false when adding a student with a null ID");
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


    @Test
    void testHasTimeConflict() {
        Course otherCourse = new Course("CIS102", "Advanced CS", "Dr. Johnson", "002", "MWF", "10:30", "11:30", 25);
        assertTrue(course.hasTimeConflict(otherCourse.getStartTime(), otherCourse.getEndTime(), otherCourse.getDays()), "Courses should have a time conflict");
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

    @Test
    void testEnrollStudentInFullCourse() {
        // Fill the course to capacity first
        for (int i = 0; i < course.getCapacity(); i++) {
            course.enrollStudent(String.valueOf(i));
        }
        String newStudentId = "12346";
        course.enrollStudent(newStudentId);
        assertFalse(course.isStudentEnrolled(newStudentId), "New student should not be enrolled if the course is full");
    }

    @Test
    void testUnenrollNonexistentStudent() {
        String nonExistentStudentId = "54321";
        course.unenrollStudent(nonExistentStudentId);
        assertFalse(course.isStudentEnrolled(nonExistentStudentId), "Unenrolling a non-existent student should not affect the course");
    }

    @Test
    void testGetEnrolledStudents() {
        String studentId = "12345";
        course.enrollStudent(studentId);
        HashSet<String> enrolledStudents = (HashSet<String>) course.getEnrolledStudents();
        assertTrue(enrolledStudents.contains(studentId), "Enrolled students should contain the added student ID");
    }

    @Test
    void testIsStudentEnrolled() {
        String studentId = "12345";
        course.enrollStudent(studentId);
        assertTrue(course.isStudentEnrolled(studentId), "isStudentEnrolled should return true for an enrolled student");
    }

    @Test
    void testIsStudentNotEnrolled() {
        String studentId = "54321";
        assertFalse(course.isStudentEnrolled(studentId), "isStudentEnrolled should return false for a non-enrolled student");
    }

    @Test
    void testSetAndGetCourseName() {
        String newName = "Advanced Computer Science";
        course.setCourseName(newName);
        assertEquals(newName, course.getCourseName(), "getCourseName should return the new course name");
    }

    @Test
    void testSetAndGetProfessorName() {
        String newProfessorName = "Dr. Johnson";
        course.setProfessorName(newProfessorName);
        assertEquals(newProfessorName, course.getProfessorName(), "getProfessorName should return the new professor name");
    }

    @Test
    void testSetAndGetProfessorId() {
        String newProfessorId = "prof002";
        course.setProfessorId(newProfessorId);
        assertEquals(newProfessorId, course.getProfessorId(), "getProfessorId should return the new professor ID");
    }

    @Test
    void testSetAndGetDays() {
        String newDays = "TTh";
        course.setDays(newDays);
        assertEquals(newDays, course.getDays(), "getDays should return the new days");
    }

    @Test
    void testSetAndGetStartTime() {
        String newStartTime = "14:00";
        course.setStartTime(newStartTime);
        assertEquals(newStartTime, course.getStartTime(), "getStartTime should return the new start time");
    }

    @Test
    void testSetAndGetEndTime() {
        String newEndTime = "15:30";
        course.setEndTime(newEndTime);
        assertEquals(newEndTime, course.getEndTime(), "getEndTime should return the new end time");
    }

    @Test
    void testSetAndGetCapacity() {
        int newCapacity = 40;
        course.setCapacity(newCapacity);
        assertEquals(newCapacity, course.getCapacity(), "getCapacity should return the new capacity");
    }

    @Test
    void testGetCurrentEnrollment() {
        assertEquals(0, course.getCurrentEnrollment(), "getCurrentEnrollment should return 0 for a new course");
        course.enrollStudent("12345");
        assertEquals(1, course.getCurrentEnrollment(), "getCurrentEnrollment should reflect the number of enrolled students");
    }

    // Add more tests as needed to cover different scenarios and edge cases

}
