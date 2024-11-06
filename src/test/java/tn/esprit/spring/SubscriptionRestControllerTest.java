package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.controllers.SubscriptionRestController;
import tn.esprit.spring.dtos.SubscriptionDTO;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.services.ISubscriptionServices;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionRestControllerTest {

    @InjectMocks
    private SubscriptionRestController subscriptionRestController;

    @Mock
    private ISubscriptionServices subscriptionServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Example Test for `addSubscription`
    @Test
    void testAddSubscription() {
        Subscription subscription = new Subscription();
        SubscriptionDTO expectedDTO = new SubscriptionDTO(); // Mocked DTO conversion

        when(subscriptionServices.addSubscription(subscription)).thenReturn(expectedDTO);

        SubscriptionDTO result = subscriptionRestController.addSubscription(subscription);

        assertNotNull(result);
        assertEquals(expectedDTO, result);
        verify(subscriptionServices, times(1)).addSubscription(subscription);
    }

    // Example Test for `getById`
    @Test
    void testGetById() {
        Long id = 1L;
        Subscription subscription = new Subscription();
        subscription.setNumSub(id);
        SubscriptionDTO expectedDTO = new SubscriptionDTO();
        expectedDTO.setNumSub(id); // assuming DTO contains similar fields

        when(subscriptionServices.retrieveSubscriptionById(id)).thenReturn(expectedDTO);

        SubscriptionDTO result = subscriptionRestController.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getNumSub());
        verify(subscriptionServices, times(1)).retrieveSubscriptionById(id);
    }
}