package com.javaproject.topjava.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "restaurants_unique_name_idx")})
public class Restaurant extends NamedEntity {

    @Column(name = "address", nullable = false)
    @Size(max = 50)
    @NotBlank
    private String address;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonManagedReference
    @JsonIgnore
    private List<Dish> menu;

    public Restaurant() {

    }

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void setMenu(List<Dish> menu) {
        this.menu = menu;
    }
//
//    public Set<Voting> getVoteSet() {
//        return voteSet;
//    }
//
//    public void setVoteSet(Set<Voting> voteSet) {
//        this.voteSet = voteSet;
//    }
//
//    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
//    private Set<Voting> voteSet = new HashSet<>();

}
