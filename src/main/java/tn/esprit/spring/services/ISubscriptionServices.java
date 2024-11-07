package tn.esprit.spring.services;

import tn.esprit.spring.dtos.SubscriptionDTO;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ISubscriptionServices {

	SubscriptionDTO addSubscription(Subscription subscriptionDTO);

	SubscriptionDTO updateSubscription(SubscriptionDTO subscriptionDTO);

	SubscriptionDTO retrieveSubscriptionById(Long numSubscription);

	Set<SubscriptionDTO> getSubscriptionByType(TypeSubscription type);

	List<SubscriptionDTO> retrieveSubscriptionsByDates(LocalDate startDate, LocalDate endDate);

	void retrieveSubscriptions();

	void showMonthlyRecurringRevenue();
}
