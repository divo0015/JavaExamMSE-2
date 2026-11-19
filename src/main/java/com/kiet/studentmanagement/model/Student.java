package com.kiet.studentmanagement.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    private int studentId;

    private String name;
    private String email;
    private String course;

    @Column(name = "programming_score", nullable = false)
    private double programmingScore;

    @Column(name = "mathematics_score", nullable = false)
    private double mathematicsScore;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "student_skills",
            joinColumns = @JoinColumn(name = "student_id")
    )
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    // Default Constructor
    public Student() {
    }

    // Parameterized Constructor
    public Student(int studentId, String name, String email,
                   String course, double programmingScore,
                   double mathematicsScore, List<String> skills) {

        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.course = course;
        setProgrammingScore(programmingScore);
        setMathematicsScore(mathematicsScore);
        setSkills(skills);
    }

    // Getters and Setters

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getProgrammingScore() {
        return programmingScore;
    }

    public void setProgrammingScore(double score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        this.programmingScore = score;
    }

    public double getMathematicsScore() {
        return mathematicsScore;
    }

    public void setMathematicsScore(double score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        this.mathematicsScore = score;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills == null
                ? new ArrayList<>()
                : new ArrayList<>(skills);
    }

    // Object Methods

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", course='" + course + '\'' +
                ", programmingScore=" + programmingScore +
                ", mathematicsScore=" + mathematicsScore +
                ", skills=" + skills +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;

        Student other = (Student) obj;
        return studentId == other.studentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}