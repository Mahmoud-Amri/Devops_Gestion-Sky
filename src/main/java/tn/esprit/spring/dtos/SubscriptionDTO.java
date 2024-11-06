package tn.esprit.spring.dtos;

import lombok.Data;
import tn.esprit.spring.entities.TypeSubscription;

import java.time.LocalDate;

@Data
public class SubscriptionDTO {
    private Long numSub;
    private LocalDate startDate;
    private LocalDate endDate;
    private Float price;
    private TypeSubscription typeSub;
}
