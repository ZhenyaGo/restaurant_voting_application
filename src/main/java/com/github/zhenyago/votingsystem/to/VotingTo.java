package com.github.zhenyago.votingsystem.to;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VotingTo extends BaseTo {

    @NotNull
    Integer restaurant_id;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    LocalDate votingDate;

    public VotingTo(Integer id, Integer restaurant_id, LocalDate votingDate) {
        super(id);
        this.restaurant_id = restaurant_id;
        this.votingDate = votingDate;
    }
}
