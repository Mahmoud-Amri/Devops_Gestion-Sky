package tn.esprit.spring.services;

import tn.esprit.spring.dtos.CourseDTO;
import tn.esprit.spring.entities.Course;

import java.util.List;

public interface ICourseServices {

    List<CourseDTO> retrieveAllCourses();

    CourseDTO  addCourse(Course  course);

    CourseDTO updateCourse(Course course);

    CourseDTO retrieveCourse(Long numCourse);


}
