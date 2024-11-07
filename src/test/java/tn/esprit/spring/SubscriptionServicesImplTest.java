package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;

@SpringBootTest
public class SubscriptionServicesImplTest {

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddSubscription() {
        Subscription subscription = new Subscription();
        subscription.setStartDate(LocalDate.now());
        subscription.setTypeSub(TypeSubscription.ANNUAL);

        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription result = subscriptionServices.addSubscription(subscription);

        assertNotNull(result);
        assertEquals(subscription.getStartDate().plusYears(1), result.getEndDate());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void testUpdateSubscription() {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setPrice(200.0f);

        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        Subscription result = subscriptionServices.updateSubscription(subscription);

        assertNotNull(result);
        assertEquals(200.0f, result.getPrice());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void testRetrieveSubscriptionById() {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Subscription result = subscriptionServices.retrieveSubscriptionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getNumSub());
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetSubscriptionByType() {
        TypeSubscription type = TypeSubscription.ANNUAL;
        Set<Subscription> subscriptions = Set.of(new Subscription());
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(type)).thenReturn(subscriptions);

        Set<Subscription> result = subscriptionServices.getSubscriptionByType(type);

        assertNotNull(result);
        assertEquals(subscriptions, result);
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(type);
    }

    @Test
    public void testRetrieveSubscriptionsByDates() {
        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        List<Subscription> subscriptions = List.of(new Subscription());
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(subscriptions);

        List<Subscription> result = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);

        assertNotNull(result);
        assertEquals(subscriptions, result);
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(startDate, endDate);
    }

    @Test
    public void testRetrieveSubscriptions() {
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setEndDate(LocalDate.now().plusMonths(1));

        Skier skier = new Skier();
        skier.setFirstName("John");
        skier.setLastName("Doe");

        when(subscriptionRepository.findDistinctOrderByEndDateAsc()).thenReturn(List.of(subscription));
        when(skierRepository.findBySubscription(subscription)).thenReturn(skier);

        subscriptionServices.retrieveSubscriptions();

        verify(subscriptionRepository, times(1)).findDistinctOrderByEndDateAsc();
        verify(skierRepository, times(1)).findBySubscription(subscription);
    }

    @Test
    public void testShowMonthlyRecurringRevenue() {
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(1000f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(6000f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(12000f);

        subscriptionServices.showMonthlyRecurringRevenue();

        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);
    }
}
