package com.github.zhenyago.votingsystem.to;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DishTo extends NamedTo {

    @Range(min = 50, max = 1000)
    int price;

    int restaurantId;

    @NotNull
    LocalDate menu_date;
}