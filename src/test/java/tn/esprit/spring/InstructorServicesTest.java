package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.InstructorServicesImpl;

import java.time.LocalDate;
import java.util.*;

public class InstructorServicesTest {

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    private Instructor instructor;
    private Course course1;
    private Course course2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        instructor = new Instructor(1L, "John", "Doe", LocalDate.now(), new HashSet<>());
        course1 = new Course(1L, 1, TypeCourse.COLLECTIVE_CHILDREN, Support.SKI, 50.0f, 2, new HashSet<>());
        course2 = new Course(2L, 2, TypeCourse.COLLECTIVE_ADULT, Support.SNOWBOARD, 75.0f, 1, new HashSet<>());
    }

    @Test
    public void testAssignCoursesToInstructor() {
        List<Long> courseIds = Arrays.asList(course1.getNumCourse(), course2.getNumCourse());

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseRepository.findById(course1.getNumCourse())).thenReturn(Optional.of(course1));
        when(courseRepository.findById(course2.getNumCourse())).thenReturn(Optional.of(course2));

        Instructor updatedInstructor = instructorServices.assignCoursesToInstructor(1L, courseIds);

        assertEquals(2, updatedInstructor.getCourses().size());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    public void testAssignCoursesToNonExistingInstructor() {
        List<Long> courseIds = Arrays.asList(course1.getNumCourse());

        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            instructorServices.assignCoursesToInstructor(1L, courseIds);
        });

        assertEquals("Instructor not found", exception.getMessage());
    }
}
