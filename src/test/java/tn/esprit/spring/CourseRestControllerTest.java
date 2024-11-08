package tn.esprit.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tn.esprit.spring.controllers.CourseRestController;
import tn.esprit.spring.dto.CourseDTO;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.services.ICourseServices;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.BDDMockito.given;


@WebMvcTest(controllers = CourseRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CourseRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICourseServices services;

    @Autowired
    private ObjectMapper objectMapper;

    private CourseDTO courseDTO;
    Course course;

    @BeforeEach
    public void init(){
        courseDTO = new CourseDTO();
        Set<Registration> registrations = new HashSet<>();
        course = new Course(0L,5, TypeCourse.INDIVIDUAL, Support.SKI, 150.0f, 3, registrations);
        courseDTO = courseDTO.convertToDTO(course);
    }

    @Test
    void addCourse() throws Exception{

        given(services.addCourse(ArgumentMatchers.any())).willReturn(courseDTO);

        mockMvc.perform(post("/course/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void getAllCourses() throws Exception {
        given(services.addCourse(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(get("/course/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void updateCourse() throws Exception {
        given(services.addCourse(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(MockMvcResultMatchers.status().is(200));

    }

    @Test
    void getById() throws Exception {
        given(services.addCourse(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        when(services.retrieveCourse(0L)).thenReturn(courseDTO);

        ResultActions response = mockMvc.perform(get("/course/get/0")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().is(200));
    }
}