package tn.esprit.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.dto.CourseDTO;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServicesImplTest{

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseService;

    private Course course;

    @BeforeEach
    void setUp() {
        // Initializes the @Mock annotations
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Service can show all Courses")
    void testReadALLCourse() {
        List<CourseDTO> result = courseService.retrieveAllCourses();
        Assertions.assertEquals(0,result.size());
    }

    @Test
    @DisplayName("Service can add Course")
    void testAddCourse(){
        Set<Registration> registrations = new HashSet<>();
        Course newCourse = new Course(0L,5, TypeCourse.INDIVIDUAL, Support.SKI, 150.0f, 3,registrations);
        when(courseRepository.save(newCourse)).thenReturn(course);
        CourseDTO savedCourse = courseService.addCourse(newCourse);

        assertNotNull(savedCourse);
        assertEquals(0L, savedCourse.getNumCourse());
        assertEquals(5, savedCourse.getLevel());
        assertEquals(TypeCourse.INDIVIDUAL, savedCourse.getTypeCourse());
        assertEquals(Support.SKI, savedCourse.getSupport());
        assertEquals(150.0f, savedCourse.getPrice());
        assertEquals(3, savedCourse.getTimeSlot());
        assertEquals(registrations, savedCourse.getRegistrations());

        List<Course> newCourseList = new ArrayList<>();
        newCourseList.add(savedCourse.convertToObject(savedCourse));

        when(courseRepository.findAll()).thenReturn(newCourseList);

        List<CourseDTO> result = courseService.retrieveAllCourses();
        Assertions.assertEquals(1,result.size());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(savedCourse, result.get(0));
    }

    @Test
    @DisplayName("Service can update Course")
    void testUpdateCourse(){
        Set<Registration> registrations = new HashSet<>();

        // Create an existing Course instance
        Course existingCourse = new Course(1L, 5, TypeCourse.INDIVIDUAL, Support.SKI, 150.0f, 3, registrations);

        // Mock the save behavior of the repository for the update
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);

        // Call the service method to update the course
        CourseDTO updatedCourse = courseService.updateCourse(existingCourse);

        // Verify that the repository's save method was called
        verify(courseRepository, times(1)).save(existingCourse);

        // Assertions to verify the update
        assertNotNull(updatedCourse);
        assertEquals(1L, updatedCourse.getNumCourse());
        assertEquals(5, updatedCourse.getLevel());
        assertEquals(TypeCourse.INDIVIDUAL, updatedCourse.getTypeCourse());
        assertEquals(Support.SKI, updatedCourse.getSupport());
        assertEquals(150.0f, updatedCourse.getPrice());
        assertEquals(3, updatedCourse.getTimeSlot());
        assertEquals(registrations, updatedCourse.getRegistrations());
    }

}