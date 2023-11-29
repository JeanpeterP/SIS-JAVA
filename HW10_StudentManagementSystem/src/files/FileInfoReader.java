package files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roles.Admin;
import roles.Professor;
import roles.Student;
import courses.Course;

/**
 * Constructor for FileInfoReader.
 * Initializes paths for course, student, professor, and admin info and loads professor name to ID map.
 *
 * @param courseInfoPath  Path to the course information file.
 * @param studentInfoPath Path to the student information file.
 * @param profInfoPath    Path to the professor information file.
 * @param adminInfoPath   Path to the admin information file.
 */
public class FileInfoReader {
    private String courseInfoPath;
    private String studentInfoPath;
    private String profInfoPath;
    private String adminInfoPath;
    private Map<String, String> professorNameToIdMap = new HashMap<>();

    public FileInfoReader(String courseInfoPath, String studentInfoPath, String profInfoPath, String adminInfoPath) {
        this.courseInfoPath = courseInfoPath;
        this.studentInfoPath = studentInfoPath;
        this.profInfoPath = profInfoPath;
        this.adminInfoPath = adminInfoPath;
        try {
            loadProfessorNameToIdMap(); // Load mapping when instance is created
        } catch (IOException e) {
            // Handle the exception, for example, log it or print a message
            System.err.println("Error loading professor information: " + e.getMessage());
            // Depending on how critical this operation is, you might want to rethrow as a runtime exception
            // or handle it in a way that the application can safely continue.
        } // Load mapping when instance is created
    }
    
    /**
     * Loads a mapping of professor names to IDs from a file.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void loadProfessorNameToIdMap() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(profInfoPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) {
                    // Name as key, ID as value
                    professorNameToIdMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
    }

    /**
     * Reads course information from a file and returns a list of Course objects.
     *
     * @return A list of Course objects.
     * @throws IOException If an I/O error occurs.
     */
    public List<Course> readCourseInfo() throws IOException {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(courseInfoPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 7) {
                    int capacity = Integer.parseInt(parts[6].trim());
                    String professorName = parts[2].trim();
                    String professorId = professorNameToIdMap.get(professorName);

                    if (professorId == null) {
                        System.err.println("No matching ID found for professor: " + professorName);
                        continue; // Skip adding this course if the professor ID is not found
                    }

                    Course course = new Course(parts[0], parts[1], professorName, professorId, parts[3], parts[4], parts[5], capacity);
                    courses.add(course);
                }
            }
        }
        return courses;
    }



    /**
     * Reads student information from a file and returns a list of Student objects.
     *
     * @return A list of Student objects.
     * @throws IOException If an I/O error occurs.
     */
    public List<Student> readStudentInfo() throws IOException {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(studentInfoPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 5) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String username = parts[2].trim();
                    String password = parts[3].trim();
                    Map<String, String> courses = new HashMap<>();
                    String[] courseDetails = parts[4].split(",");
                    for (String courseDetail : courseDetails) {
                        String[] course = courseDetail.split(":");
                        if (course.length == 2) {
                            courses.put(course[0].trim(), course[1].trim());
                        }
                    }
                    Student student = new Student(id, name, username, password, courses);
                    students.add(student);
                }
            }
        }
        return students;
    }
    
    /**
     * Reads professor information from a file and returns a list of Professor objects.
     *
     * @return A list of Professor objects.
     * @throws IOException If an I/O error occurs.
     */
    public List<Professor> readProfInfo() throws IOException {
        List<Professor> professors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(profInfoPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    String id = parts[0].trim(); // Correctly assign the first part to id
                    String name = parts[1].trim(); // Correctly assign the second part to name
                    String username = parts[2].trim();
                    String password = parts[3].trim();
                    Professor professor = new Professor(name, id, username, password, new ArrayList<>());
                    professors.add(professor);
                }
            }
        }
        return professors;
    }

    /**
     * Reads admin information from a file and returns a list of Admin objects.
     *
     * @return A list of Admin objects.
     * @throws IOException If an I/O error occurs.
     */
    public List<Admin> readAdminInfo() throws IOException {
        List<Admin> admins = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(adminInfoPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    String id = parts[0].trim(); // Trim the parts to remove whitespace
                    String name = parts[1].trim();
                    String username = parts[2].trim();
                    String password = parts[3].trim();
                    Admin admin = new Admin(id, name, username, password);
                    admins.add(admin);
                }
            }
        }
        return admins;
    }

}
