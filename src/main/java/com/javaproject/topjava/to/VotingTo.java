package com.javaproject.topjava.to;

import java.time.LocalDate;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VotingTo votingTo = (VotingTo) o;
        return  Objects.equals(id, votingTo.id) &&
                Objects.equals(user_id, votingTo.user_id) &&
                Objects.equals(restaurant_id, votingTo.restaurant_id) &&
                Objects.equals(votingDate, votingTo.votingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,user_id, restaurant_id, votingDate);
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
