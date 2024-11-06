package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ISubscriptionRepositoryTest {

    @Autowired
    private ISubscriptionRepository subscriptionRepository;

    private Subscription subscription1;
    private Subscription subscription2;

    @BeforeEach
    void setUp() {
        subscription1 = new Subscription();
        subscription1.setTypeSub(TypeSubscription.MONTHLY);
        subscription1.setStartDate(LocalDate.now().minusDays(10));
        subscription1.setEndDate(LocalDate.now().plusDays(20));
        subscription1.setPrice(100F);

        subscription2 = new Subscription();
        subscription2.setTypeSub(TypeSubscription.ANNUAL);
        subscription2.setStartDate(LocalDate.now().minusDays(30));
        subscription2.setEndDate(LocalDate.now().minusDays(5));
        subscription2.setPrice(200F);

        subscriptionRepository.save(subscription1);
        subscriptionRepository.save(subscription2);
    }

    @Test
    void testFindByTypeSubOrderByStartDateAsc() {
        Set<Subscription> subscriptions = subscriptionRepository.findByTypeSubOrderByStartDateAsc(TypeSubscription.MONTHLY);
        assertNotNull(subscriptions);
        assertEquals(1, subscriptions.size());
        assertEquals(subscription1.getStartDate(), subscriptions.iterator().next().getStartDate());
    }

    @Test
    void testGetSubscriptionsByStartDateBetween() {
        LocalDate startDate = LocalDate.now().minusDays(15);
        LocalDate endDate = LocalDate.now();
        List<Subscription> subscriptions = subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate);
        assertNotNull(subscriptions);
        assertEquals(1, subscriptions.size());
        assertEquals(subscription1.getStartDate(), subscriptions.get(0).getStartDate());
    }

    @Test
    void testFindDistinctOrderByEndDateAsc() {
        List<Subscription> subscriptions = subscriptionRepository.findDistinctOrderByEndDateAsc();
        assertNotNull(subscriptions);
        assertEquals(2, subscriptions.size());
        assertEquals(subscription2.getEndDate(), subscriptions.get(0).getEndDate());
    }

    @Test
    void testRecurringRevenueByTypeSubEquals() {
        Float revenue = subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        assertNotNull(revenue);
        assertEquals(100F, revenue);
    }
}
