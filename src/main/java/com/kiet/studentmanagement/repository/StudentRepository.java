package com.kiet.studentmanagement.repository;

import com.kiet.studentmanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository
        extends JpaRepository<Student, Integer> {

    // Search student by email
    Optional<Student> findByEmail(String email);

    // Search students by course
    List<Student> findByCourse(String course);

    // Search students by technical skill
    List<Student> findBySkillsContaining(String skill);
}