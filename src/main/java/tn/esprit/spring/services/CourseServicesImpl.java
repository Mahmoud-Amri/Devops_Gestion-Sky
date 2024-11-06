package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.dtos.CourseDTO;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.List;
@AllArgsConstructor
@Service
public class CourseServicesImpl implements ICourseServices{

    private ICourseRepository courseRepository;

    @Override
    public List<CourseDTO> retrieveAllCourses() {
        return new CourseDTO().convertToDTOs(courseRepository.findAll());
    }

    @Override
    public CourseDTO addCourse(Course course) {
        courseRepository.save(course);
        CourseDTO newCourseDTO = new CourseDTO();
        return newCourseDTO.convertToDTO(course);
    }

    @Override
    public CourseDTO updateCourse(Course course) {
        return new CourseDTO().convertToDTO(courseRepository.save(course));
    }

    @Override
    public CourseDTO retrieveCourse(Long numCourse) {
        return new CourseDTO().convertToDTO(courseRepository.findById(numCourse).orElse(new Course()));
    }


}
