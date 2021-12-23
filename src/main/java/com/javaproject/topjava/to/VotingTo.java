package com.javaproject.topjava.to;

import java.time.LocalDate;

public class VotingTo extends BaseTo {


    Integer user_id;

    Integer restaurant_id;

    LocalDate votingDate;

    public VotingTo() {

    }

    public VotingTo(Integer id, Integer user_id, Integer restaurant_id, LocalDate votingDate) {
        super(id);
        this.user_id = user_id;
        this.restaurant_id = restaurant_id;
        this.votingDate = votingDate;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(Integer restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public LocalDate getVotingDate() {
        return votingDate;
    }

    public void setVotingDate(LocalDate votingDate) {
        this.votingDate = votingDate;
    }

    @Override
    public String toString() {
        return "VotingTo{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", restaurant_id=" + restaurant_id +
                ", votingDate=" + votingDate +
                '}';
    }
}
