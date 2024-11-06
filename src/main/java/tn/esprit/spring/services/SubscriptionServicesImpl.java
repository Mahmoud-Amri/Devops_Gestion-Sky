package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.spring.dtos.SubscriptionDTO;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class SubscriptionServicesImpl implements ISubscriptionServices {

    private final ISubscriptionRepository subscriptionRepository;
    private final ISkierRepository skierRepository;

    private SubscriptionDTO toDTO(Subscription subscription) {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setNumSub(subscription.getNumSub());
        dto.setStartDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        dto.setPrice(subscription.getPrice());
        dto.setTypeSub(subscription.getTypeSub());
        return dto;
    }

    private Subscription toEntity(SubscriptionDTO dto) {
        Subscription subscription = new Subscription();
        subscription.setNumSub(dto.getNumSub());
        subscription.setStartDate(dto.getStartDate());
        subscription.setEndDate(dto.getEndDate());
        subscription.setPrice(dto.getPrice());
        subscription.setTypeSub(dto.getTypeSub());
        return subscription;
    }

    @Override
    public SubscriptionDTO addSubscription(Subscription subscriptionDTO) {
        Subscription subscription = toEntity(subscriptionDTO);
        switch (subscription.getTypeSub()) {
            case ANNUAL:
                subscription.setEndDate(subscription.getStartDate().plusYears(1));
                break;
            case SEMESTRIEL:
                subscription.setEndDate(subscription.getStartDate().plusMonths(6));
                break;
            case MONTHLY:
                subscription.setEndDate(subscription.getStartDate().plusMonths(1));
                break;
        }
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return toDTO(savedSubscription);
    }

    @Override
    public SubscriptionDTO updateSubscription(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = toEntity(subscriptionDTO);
        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return toDTO(updatedSubscription);
    }

    @Override
    public SubscriptionDTO retrieveSubscriptionById(Long numSubscription) {
        Subscription subscription = subscriptionRepository.findById(numSubscription).orElse(null);
        return subscription != null ? toDTO(subscription) : null;
    }

    @Override
    public Set<SubscriptionDTO> getSubscriptionByType(TypeSubscription type) {
        return subscriptionRepository.findByTypeSubOrderByStartDateAsc(type)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public List<SubscriptionDTO> retrieveSubscriptionsByDates(LocalDate startDate, LocalDate endDate) {
        return subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Scheduled(cron = "*/30 * * * * *") // Runs every 30 seconds
    public void retrieveSubscriptions() {
        for (Subscription sub : subscriptionRepository.findDistinctOrderByEndDateAsc()) {
            Skier aSkier = skierRepository.findBySubscription(sub);
            log.info(sub.getNumSub().toString() + " | " + sub.getEndDate().toString()
                    + " | " + aSkier.getFirstName() + " " + aSkier.getLastName());
        }
    }

    @Override
    @Scheduled(cron = "*/30 * * * * *") // Runs every 30 seconds
    public void showMonthlyRecurringRevenue() {
        Float revenue = subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)
                + subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL) / 6
                + subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL) / 12;
        log.info("Monthly Revenue = " + revenue);
    }
}
