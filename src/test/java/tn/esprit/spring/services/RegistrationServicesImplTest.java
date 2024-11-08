package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import lombok.extern.slf4j.Slf4j;

import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

import java.util.List;
import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RegistrationServicesImplTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addRegistrationAndAssignToSkier() {
        // Mock data
        Skier skier = new Skier();
        skier.setNumSkier(1L);
        skier.setFirstName("John");
        skier.setLastName("Doe");
        skier.setDateOfBirth(LocalDate.of(1995, 1, 1));
        skier.setCity("New York");
        skier.setRegistrations(new HashSet<>());

        Registration registration = new Registration();
        registration.setNumRegistration(1L);
        registration.setNumWeek(2);

        // Mock repository behavior
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // Execute service method
        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, 1L);

        // Assertions
        assertNotNull(result);
        assertEquals(1L, result.getNumRegistration());
        verify(registrationRepository, times(1)).save(any(Registration.class));
        verify(skierRepository, times(1)).findById(1L);

        log.info("Registration added successfully with ID: {}", result.getNumRegistration());
    }

    @Test
    void assignRegistrationToCourse() {
        // Mock data
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.INDIVIDUAL);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(1);
        course.setRegistrations(new HashSet<>());

        Registration registration = new Registration();
        registration.setNumRegistration(1L);
        registration.setNumWeek(2);

        // Mock repository behavior
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));

        // Execute service method
        registrationServices.assignRegistrationToCourse(1L, 1L);

        // Verify interactions
        verify(courseRepository, times(1)).findById(1L);
        verify(registrationRepository, times(1)).findById(1L);
        verify(registrationRepository, times(1)).save(any(Registration.class));

        log.info("Registration {} assigned to course {}", registration.getNumRegistration(), course.getNumCourse());
    }

    @Test
    void addRegistrationAndAssignToSkierAndCourse() {
        // Mock data
        Skier skier = new Skier();
        skier.setNumSkier(1L);
        skier.setDateOfBirth(LocalDate.of(2005, 1, 1)); // Initialize dateOfBirth for age calculation
        skier.setRegistrations(new HashSet<>());

        Course course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.INDIVIDUAL); // Set course type for testing
        course.setRegistrations(new HashSet<>());

        Registration registration = new Registration();
        registration.setNumRegistration(1L);
        registration.setNumWeek(3);

        // Mock repository behavior
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // Execute service method
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Assertions
        assertNotNull(result);
        assertEquals(1L, result.getNumRegistration());
        verify(registrationRepository, times(1)).save(any(Registration.class));

        log.info("Registration added and assigned to skier and course: {}", result.getNumRegistration());
    }


    @Test
    void numWeeksCourseOfInstructorBySupport() {
        // Mock data
        Long instructorId = 1L;
        Support support = Support.SKI;
        List<Integer> weeksList = Arrays.asList(2, 3);  // Example weeks

        // Mock repository behavior
        when(registrationRepository.numWeeksCourseOfInstructorBySupport(instructorId, support))
                .thenReturn(weeksList);

        // Execute service method
        List<Integer> result = registrationServices.numWeeksCourseOfInstructorBySupport(instructorId, support);

        // Calculate total weeks
        int totalWeeks = result.stream().mapToInt(Integer::intValue).sum();

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(5, totalWeeks); // 2 + 3 = 5 weeks total
        verify(registrationRepository).numWeeksCourseOfInstructorBySupport(instructorId, support);

        log.info("Weeks list: {}", result);
        log.info("Total weeks for instructor courses: {}", totalWeeks);
    }
}