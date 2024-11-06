package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.dtos.SubscriptionDTO;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServicesImplTest {

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    @Mock
    private ISubscriptionRepository subscriptionRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSubscription() {
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.MONTHLY);
        subscription.setStartDate(LocalDate.now());

        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        SubscriptionDTO result = subscriptionServices.addSubscription(subscription);

        assertNotNull(result);
        assertEquals(subscription.getEndDate(), subscription.getStartDate().plusMonths(1));
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void testRetrieveSubscriptionById() {
        Long id = 1L;
        Subscription subscription = new Subscription();
        subscription.setNumSub(id);

        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(subscription));

        SubscriptionDTO result = subscriptionServices.retrieveSubscriptionById(id);

        assertNotNull(result);
        assertEquals(id, result.getNumSub());
        verify(subscriptionRepository, times(1)).findById(id);
    }

    @Test
    void testGetSubscriptionByType() {
        TypeSubscription type = TypeSubscription.MONTHLY;
        Set<Subscription> subscriptions = Set.of(new Subscription());

        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(type)).thenReturn(subscriptions);

        Set<SubscriptionDTO> result = subscriptionServices.getSubscriptionByType(type);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(type);
    }

    @Test
    void testRetrieveSubscriptionsByDates() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();
        List<Subscription> subscriptions = List.of(new Subscription());

        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(subscriptions);

        List<SubscriptionDTO> result = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(startDate, endDate);
    }
}
