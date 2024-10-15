package tn.esprit.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorServicesImpl {

    @Autowired
    private IInstructorRepository instructorRepository;

    @Autowired
    private ICourseRepository courseRepository;

    public Instructor assignCoursesToInstructor(Long instructorId, List<Long> courseIds) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));

        for (Long courseId : courseIds) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Course not found"));
            instructor.getCourses().add(course);  // Ajoute le cours à l'instructeur
        }

        instructorRepository.save(instructor);  // Enregistre l'instructeur
        return instructor;  // Renvoie l'instructeur mis à jour
    }
}
