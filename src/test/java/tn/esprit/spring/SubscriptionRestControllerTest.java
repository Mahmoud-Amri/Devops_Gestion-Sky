package tn.esprit.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.controllers.SubscriptionRestController;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISubscriptionServices;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionRestController.class) // Only load the controller layer for the test
public class SubscriptionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISubscriptionServices subscriptionServices;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddSubscription() throws Exception {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setTypeSub(TypeSubscription.ANNUAL);

        when(subscriptionServices.addSubscription(any(Subscription.class))).thenReturn(subscription);

        mockMvc.perform(post("/subscription/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscription)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(subscription.getNumSub()))
                .andExpect(jsonPath("$.typeSub").value(subscription.getTypeSub().toString()));

        verify(subscriptionServices, times(1)).addSubscription(any(Subscription.class));
    }

    @Test
    public void testGetById() throws Exception {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);

        when(subscriptionServices.retrieveSubscriptionById(1L)).thenReturn(subscription);

        mockMvc.perform(get("/subscription/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(subscription.getNumSub()));

        verify(subscriptionServices, times(1)).retrieveSubscriptionById(1L);
    }

    @Test
    public void testGetSubscriptionsByType() throws Exception {
        Set<Subscription> subscriptions = Set.of(new Subscription());
        TypeSubscription type = TypeSubscription.ANNUAL;

        when(subscriptionServices.getSubscriptionByType(type)).thenReturn(subscriptions);

        mockMvc.perform(get("/subscription/all/" + type))
                .andExpect(status().isOk());

        verify(subscriptionServices, times(1)).getSubscriptionByType(type);
    }

    @Test
    public void testUpdateSubscription() throws Exception {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setTypeSub(TypeSubscription.MONTHLY);

        when(subscriptionServices.updateSubscription(any(Subscription.class))).thenReturn(subscription);

        mockMvc.perform(put("/subscription/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscription)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(subscription.getNumSub()))
                .andExpect(jsonPath("$.typeSub").value(subscription.getTypeSub().toString()));

        verify(subscriptionServices, times(1)).updateSubscription(any(Subscription.class));
    }

    @Test
    public void testGetSubscriptionsByDates() throws Exception {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        List<Subscription> subscriptions = List.of(new Subscription());

        when(subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate)).thenReturn(subscriptions);

        mockMvc.perform(get("/subscription/all/" + startDate + "/" + endDate))
                .andExpect(status().isOk());

        verify(subscriptionServices, times(1)).retrieveSubscriptionsByDates(startDate, endDate);
    }
}
