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

public class FileInfoReader {
    private String courseInfoPath;
    private String studentInfoPath;
    private String profInfoPath;
    private String adminInfoPath;

    public FileInfoReader(String courseInfoPath, String studentInfoPath, String profInfoPath, String adminInfoPath) {
        this.courseInfoPath = courseInfoPath;
        this.studentInfoPath = studentInfoPath;
        this.profInfoPath = profInfoPath;
        this.adminInfoPath = adminInfoPath;
    }

    public List<Course> readCourseInfo() throws IOException {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(courseInfoPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 7) {
                    int capacity = Integer.parseInt(parts[6].trim()); // Trim the string before parsing
                    Course course = new Course(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], capacity);
                    courses.add(course);
                }
            }
        }
        return courses;
    }


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

    public List<Professor> readProfInfo() throws IOException {
        List<Professor> professors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(profInfoPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    String name = parts[0].trim(); // Trim the parts to remove whitespace;
                    String id = parts[1].trim(); // Trim the parts to remove whitespace;
                    String username = parts[2].trim(); // Trim the parts to remove whitespace;
                    String password = parts[3].trim(); // Trim the parts to remove whitespace;
                    Professor professor = new Professor(id, name, username, password, new ArrayList<>());
                    professors.add(professor);
                }
            }
        }
        return professors;
    }


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
                    System.out.println("Loaded admin: " + username); // Debug print
                }
            }
        }
        return admins;
    }



    // Additional helper methods if needed
}
