package com.javaproject.topjava.model;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "voting", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"voting_date", "voting_time", "user_id"}, name = "voting_user_unique_voting_date_idx")})
public class Voting extends BaseEntity {


    public Voting() {
    }

    public Voting(User user, Restaurant restaurant, LocalDate votingDate, LocalTime votingTime) {
        super();
        this.user = user;
        this.restaurant = restaurant;
        this.votingDate = votingDate;
        this.votingTime = votingTime;
    }


    public Voting(Integer id, User user, Restaurant restaurant, LocalDate votingDate, LocalTime votingTime) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.votingDate = votingDate;
        this.votingTime = votingTime;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Restaurant restaurant;


    @Column(name = "voting_date")
    private LocalDate votingDate = LocalDate.now();

    @Column(name = "voting_time")
    private LocalTime votingTime = LocalTime.now();


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getVotingDate() {
        return votingDate;
    }

    public void setVotingDate(LocalDate votingDate) {
        this.votingDate = votingDate;
    }

    public LocalTime getVotingTime() {
        return votingTime;
    }

    public void setVotingTime(LocalTime votingTime) {
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

