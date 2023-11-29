package courses;

import java.util.HashSet;
import java.util.Set;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Course {
    private String courseId;
    private String courseName;
    private String professorName; 
    private String professorId; 
    private String days;
    private String startTime;
    private String endTime;
    private int capacity;
    private Set<String> enrolledStudents; // Store student IDs

    public Course(String courseId, String courseName, String professorName, String professorId, String days, String startTime, String endTime, int capacity) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.professorName = professorName;
        this.professorId = professorId; // Updated
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.enrolledStudents = new HashSet<>();
    }
    // Override the toString method
    @Override
    public String toString() {
        return "Course{" +
               "courseId='" + courseId.trim() + '\'' +
               ", professorName='" + professorName.trim() + '\'' +
               ", days='" + days.trim() + '\'' +
               ", startTime='" + startTime.trim() + '\'' +
               ", endTime='" + endTime.trim() + '\'' +
               '}';
    }
    public boolean addStudent(String studentId) {
        if (studentId == null || studentId.isEmpty()) {
            return false; // Return false for invalid IDs
        }

        if (enrolledStudents.size() < capacity) {
            return enrolledStudents.add(studentId);
        }

        return false;
    }

    public boolean removeStudent(String studentId) {
        return enrolledStudents.remove(studentId);
    }
    
    public void enrollStudent(String studentId) {
        if (enrolledStudents.size() < capacity) {
            enrolledStudents.add(studentId);
            // Additional logic if needed
        } else {
            System.out.println("Course is full. Cannot enroll student.");
        }
    }

    public void unenrollStudent(String studentId) {
        if (enrolledStudents.contains(studentId)) {
            enrolledStudents.remove(studentId);
            // Additional logic if needed
        } else {
            System.out.println("Student not enrolled in this course.");
        }
    }


    public Set<String> getEnrolledStudents() {
        return new HashSet<>(enrolledStudents); // Return a copy to preserve encapsulation
    }
    
    public boolean hasTimeConflict(Course otherCourse) {
        // Check if courses are on the same day
        if (!this.days.equals(otherCourse.days)) {
            return false; // No conflict if courses are on different days
        }

        // Parse the start and end times of both courses
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime thisStartTime = LocalTime.parse(this.startTime, formatter);
        LocalTime thisEndTime = LocalTime.parse(this.endTime, formatter);
        LocalTime otherStartTime = LocalTime.parse(otherCourse.startTime, formatter);
        LocalTime otherEndTime = LocalTime.parse(otherCourse.endTime, formatter);

        // Check for time overlap
        return !thisStartTime.isAfter(otherEndTime) && !otherStartTime.isAfter(thisEndTime);
    }

    // Check if a student is enrolled in the course
    public boolean isStudentEnrolled(String studentId) {
        return enrolledStudents.contains(studentId);
    }

    // Getters and setters for all fields

    // Example getter
    public String getCourseId() {
        return courseId;
    }

    // Example setter
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    // Getter for professorName
    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    // Getter for professorId
    public String getProfessorId() {
        return professorId;
    }

    
    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentEnrollment() {
        return enrolledStudents.size();
    }
}



