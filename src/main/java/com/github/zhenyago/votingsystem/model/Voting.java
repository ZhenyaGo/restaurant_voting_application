package com.github.zhenyago.votingsystem.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"voting_date", "user_id"}, name = "voting_user_unique_voting_date_idx")})
public class Voting extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Restaurant restaurant;

    @Column(name = "voting_date")
    private LocalDate votingDate;

    @Column(name = "voting_time")
    private LocalTime votingTime;

   public Voting(User user, Restaurant restaurant) {
       this(null, user, restaurant);
   }

    public Voting(Integer id, User user, Restaurant restaurant) {
        this(id, user, restaurant, LocalDate.now(), LocalTime.now());
    }

    public Voting(Integer id, User user, Restaurant restaurant, LocalDate votingDate, LocalTime votingTime) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.votingDate = votingDate;
        this.votingTime = votingTime;
    }

    @Override
    public String toString() {
        return "Voting{" +
                "user=" + user +
                ", restaurant=" + restaurant +
                ", votingDate=" + votingDate +
                ", votingTime=" + votingTime +
                '}';
    }
}

