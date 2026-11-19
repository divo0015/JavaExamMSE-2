package com.kiet.studentmanagement.controller;

import com.kiet.studentmanagement.model.Student;
import com.kiet.studentmanagement.service.StudentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    // Constructor Injection
    public StudentController(StudentService service) {
        this.service = service;
    }

    // 1. Add a student
    @PostMapping
    public ResponseEntity<Student> addStudent(
            @RequestBody Student student) {

        Student saved = service.addStudent(student);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    // 2. Retrieve all students
    @GetMapping
    public List<Student> getAllStudents() {

        return service.getAllStudents();
    }

    // 3. Search student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @PathVariable int id) {

        return service.searchById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    // 4. Update student scores and skills
    @PutMapping("/{id}")
    public Student updateStudent(
            @PathVariable int id,
            @RequestBody Student student) {

        return service.updateStudent(id, student);
    }

    // 5. Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable int id) {

        service.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }

    // 6. Display merit list
    @GetMapping("/merit")
    public List<Student> getMeritList() {

        return service.getMeritList();
    }

    // Handle duplicate IDs and invalid scores
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(
            IllegalArgumentException ex) {

        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }

    // Handle student not found
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(
            NoSuchElementException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
    // Search by email
    @GetMapping("/email")
    public ResponseEntity<Student> searchByEmail(
            @RequestParam String email) {

        return service.searchByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Search by course
    @GetMapping("/course")
    public List<Student> searchByCourse(
            @RequestParam String course) {

        return service.searchByCourse(course);
    }

    // Get students sharing technical skills
    @GetMapping("/common-skills")
    public Map<String, List<Student>> commonSkills() {

        return service.getCommonSkills();
    }

    // Students needing improvement in programming
    @GetMapping("/improvement")
    public List<Student> needsImprovement() {

        return service.getStudentsNeedingProgrammingImprovement();
    }

    // Get performance details of a student
    @GetMapping("/{id}/performance")
    public ResponseEntity<Map<String, Object>> getPerformance(
            @PathVariable int id) {

        return service.searchById(id).map(student -> {

            Map<String, Object> result = new LinkedHashMap<>();

            result.put("studentId", student.getStudentId());
            result.put("name", student.getName());
            result.put("average", service.calculateAverage(student));
            result.put("category", service.getPerformanceCategory(student));
            result.put("strongerSubject", service.getStrongerSubject(student));

            return ResponseEntity.ok(result);

        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}