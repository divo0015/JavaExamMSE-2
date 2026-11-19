package com.kiet.studentmanagement.service;

import com.kiet.studentmanagement.model.Student;
import com.kiet.studentmanagement.repository.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // 1. Calculate average score
    public double calculateAverage(Student student) {
        return (student.getProgrammingScore()
                + student.getMathematicsScore()) / 2.0;
    }

    // 2. Assign performance category
    public String getPerformanceCategory(Student student) {

        double average = calculateAverage(student);

        if (average >= 85)
            return "Excellent";
        else if (average >= 70)
            return "Good";
        else if (average >= 50)
            return "Satisfactory";
        else
            return "Needs Improvement";
    }

    // 3. Identify stronger subject
    public String getStrongerSubject(Student student) {

        if (student.getProgrammingScore()
                > student.getMathematicsScore()) {
            return "Programming";
        }

        if (student.getMathematicsScore()
                > student.getProgrammingScore()) {
            return "Mathematics";
        }

        return "Both Equal";
    }

    // 4. Search students

    public Optional<Student> searchById(int id) {
        return repository.findById(id);
    }

    public Optional<Student> searchByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<Student> searchByCourse(String course) {
        return repository.findByCourse(course);
    }

    // 5. Generate merit list
    public List<Student> getMeritList() {

        List<Student> students = repository.findAll();

        students.sort(
                Comparator.comparingDouble(
                        this::calculateAverage
                ).reversed()
        );

        return students;
    }

    // 6. Identify students sharing technical skills
    public Map<String, List<Student>> getCommonSkills() {

        Map<String, List<Student>> skillMap = new HashMap<>();

        for (Student student : repository.findAll()) {

            for (String skill : new HashSet<>(student.getSkills())) {

                skillMap.computeIfAbsent(
                        skill, k -> new ArrayList<>()
                ).add(student);
            }
        }

        // Keep only skills shared by multiple students
        skillMap.entrySet().removeIf(
                entry -> entry.getValue().size() < 2
        );

        return skillMap;
    }

    // 7. Weak programming but strong mathematics
    public List<Student> getStudentsNeedingProgrammingImprovement() {

        return repository.findAll()
                .stream()
                .filter(student ->
                        student.getProgrammingScore() < 50
                                && student.getMathematicsScore() >= 75
                )
                .collect(Collectors.toList());
    }

    // 8. Prevent duplicate IDs and invalid scores
    public Student addStudent(Student student) {

        if (repository.existsById(student.getStudentId())) {
            throw new IllegalArgumentException(
                    "Student ID already exists"
            );
        }

        validateScore(student.getProgrammingScore());
        validateScore(student.getMathematicsScore());

        return repository.save(student);
    }

    private void validateScore(double score) {

        if (!Double.isFinite(score)
                || score < 0 || score > 100) {
            throw new IllegalArgumentException(
                    "Score must be between 0 and 100"
            );
        }
    }
    public List<Student> getAllStudents() {
        return repository.findAll();
    }
    public Student updateStudent(int id, Student updatedStudent) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Student not found"));

        // Validate scores
        validateScore(updatedStudent.getProgrammingScore());
        validateScore(updatedStudent.getMathematicsScore());

        // Update scores
        student.setProgrammingScore(
                updatedStudent.getProgrammingScore()
        );

        student.setMathematicsScore(
                updatedStudent.getMathematicsScore()
        );

        // Update skills
        student.setSkills(updatedStudent.getSkills());

        return repository.save(student);
    }
    public void deleteStudent(int id) {

        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Student not found");
        }

        repository.deleteById(id);
    }
}