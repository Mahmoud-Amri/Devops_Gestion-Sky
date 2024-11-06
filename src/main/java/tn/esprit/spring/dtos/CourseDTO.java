package tn.esprit.spring.dtos;
import lombok.Data;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class CourseDTO {
    private Long numCourse;
    private int level;
    private TypeCourse typeCourse;
    private Support support;
    private Float price;
    private int timeSlot;
    private Set<Registration> registrations;

    public CourseDTO convertToDTO(Course course){
        CourseDTO dto = new CourseDTO();

        dto.setNumCourse(course.getNumCourse());
        dto.setLevel(course.getLevel());
        dto.setTypeCourse(course.getTypeCourse());
        dto.setSupport(course.getSupport());
        dto.setPrice(course.getPrice());
        dto.setTimeSlot(course.getTimeSlot());
        dto.setRegistrations(course.getRegistrations());

        return dto;
    }

    public List<CourseDTO> convertToDTOs(List<Course> courses){

        List<CourseDTO> listDto = new ArrayList<>();


        for(int i=0;i<courses.size();i++){
            CourseDTO dto = new CourseDTO();

            dto.setNumCourse(courses.get(i).getNumCourse());
            dto.setLevel(courses.get(i).getLevel());
            dto.setTypeCourse(courses.get(i).getTypeCourse());
            dto.setSupport(courses.get(i).getSupport());
            dto.setPrice(courses.get(i).getPrice());
            dto.setTimeSlot(courses.get(i).getTimeSlot());
            dto.setRegistrations(courses.get(i).getRegistrations());

            listDto.add(dto);
        }


        return listDto;
    }

    public Course convertToObject(CourseDTO dto){
        Course course = new Course();

        course.setNumCourse(dto.getNumCourse());
        course.setLevel(dto.getLevel());
        course.setTypeCourse(dto.getTypeCourse());
        course.setSupport(dto.getSupport());
        course.setPrice(dto.getPrice());
        course.setTimeSlot(dto.getTimeSlot());
        course.setRegistrations(dto.getRegistrations());

        return course;
    }
}
