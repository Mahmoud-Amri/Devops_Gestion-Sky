package tn.esprit.spring.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.spring.entities.Color;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PisteDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    Long numPiste;

    @NotNull(message = "Piste name cannot be null")
    String namePiste;

    Color color;

    @Min(value = 0, message = "Length must be a non-negative value")
    Integer length;

    @Min(value = 0, message = "Slope must be a non-negative value")
    Integer slope;
}
