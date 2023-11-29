package courses;

import java.util.HashSet;
import java.util.List;
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
    
    /**
     * Checks if this course has a time conflict with given course details.
     *
     * @param newStartTime The start time of the new course.
     * @param newEndTime   The end time of the new course.
     * @param newDays      The days the new course will take place.
     * @return true if there is a time conflict, false otherwise.
     */
    public boolean hasTimeConflict(String newStartTime, String newEndTime, String newDays) {
        // Check for day overlap first
        if (!daysOverlap(this.days, newDays)) {
            return false; // No day overlap, so no time conflict
        }

        // Check for time overlap
        for (char day : newDays.toCharArray()) {
            if (this.days.indexOf(day) != -1) {
                if (timePeriodsOverlap(this.startTime, this.endTime, newStartTime, newEndTime)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the days of two courses overlap.
     *
     * @param days1 The days of the first course.
     * @param days2 The days of the second course.
     * @return true if the days overlap, false otherwise.
     */
    private boolean daysOverlap(String days1, String days2) {
        for (char day : days2.toCharArray()) {
            if (days1.indexOf(day) != -1) {
                return true; // Found a common day
            }
        }
        return false;
    }

    /**
     * Determines if two time periods overlap.
     *
     * @param startTime1 First period start time.
     * @param endTime1   First period end time.
     * @param startTime2 Second period start time.
     * @param endTime2   Second period end time.
     * @return true if periods overlap, false otherwise.
     */
    private boolean timePeriodsOverlap(String startTime1, String endTime1, String startTime2, String endTime2) {
        int start1 = timeToInt(startTime1);
        int end1 = timeToInt(endTime1);
        int start2 = timeToInt(startTime2);
        int end2 = timeToInt(endTime2);

        // Check if one period starts during the other
        return (start1 <= end2 && end1 >= start2);
    }


    /**
     * Converts a time string in "HH:mm" format to an integer representing minutes since midnight.
     *
     * @param time The time string.
     * @return The number of minutes since midnight.
     */
    private int timeToInt(String time) {
        String[] parts = time.trim().split(":"); // Trim the time string before splitting
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes; // Convert time to minutes since midnight
    }
    
   
    private List<Course> allCourses;

    public void CourseService(List<Course> allCourses) {
        this.allCourses = allCourses;
    }

    public boolean doesCourseExist(String courseId) {
        return allCourses.stream().anyMatch(c -> c.getCourseId().equals(courseId));
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



