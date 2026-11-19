package com.kiet;

import com.kiet.studentmanagement.model.Student;
import java.util.Arrays;

public class StudentTest {

    public static void main(String[] args) {

        Student s1 = new Student(
                101,
                "Rahul",
                "rahul@gmail.com",
                "B.Tech CSE",
                85,
                90,
                Arrays.asList("Java", "Python", "SQL")
        );

        System.out.println(s1);
    }
}