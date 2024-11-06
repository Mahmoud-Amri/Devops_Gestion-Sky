package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dtos.SubscriptionDTO;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISubscriptionServices;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "\uD83D\uDC65 Subscription Management")
@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionRestController {

    private final ISubscriptionServices subscriptionServices;

    private SubscriptionDTO toDTO(SubscriptionDTO subscription) {
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

    @Operation(description = "Add Subscription")
    @PostMapping("/add")
    public SubscriptionDTO addSubscription(@RequestBody Subscription subscriptionDTO){
        Subscription subscription = toEntity(subscriptionDTO);
        SubscriptionDTO savedSubscription = subscriptionServices.addSubscription(subscription);
        return toDTO(savedSubscription);
    }

    @Operation(description = "Retrieve Subscription by Id")
    @GetMapping("/get/{id-subscription}")
    public SubscriptionDTO getById(@PathVariable("id-subscription") Long numSubscription){
        SubscriptionDTO subscription = subscriptionServices.retrieveSubscriptionById(numSubscription);
        return toDTO(subscription);
    }

    @Operation(description = "Retrieve Subscriptions by Type")
    @GetMapping("/all/{typeSub}")
    public Set<SubscriptionDTO> getSubscriptionsByType(@PathVariable("typeSub") TypeSubscription typeSubscription){
        return subscriptionServices.getSubscriptionByType(typeSubscription)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }

    @Operation(description = "Update Subscription")
    @PutMapping("/update")
    public SubscriptionDTO updateSubscription(@RequestBody SubscriptionDTO subscriptionDTO){
        SubscriptionDTO subscription = toEntity(subscriptionDTO);
        SubscriptionDTO updatedSubscription = subscriptionServices.updateSubscription(subscription);
        return toDTO(updatedSubscription);
    }

    @Operation(description = "Retrieve Subscriptions created between two dates")
    @GetMapping("/all/{date1}/{date2}")
    public List<SubscriptionDTO> getSubscriptionsByDates(@PathVariable("date1") LocalDate startDate,
                                                         @PathVariable("date2") LocalDate endDate){
        return subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
