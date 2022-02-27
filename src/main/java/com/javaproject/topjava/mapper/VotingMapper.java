package com.javaproject.topjava.mapper;

import com.javaproject.topjava.model.Voting;
import com.javaproject.topjava.repository.RestaurantRepository;
import com.javaproject.topjava.to.VotingTo;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class VotingMapper {

    ModelMapper mapper;

    RestaurantRepository repository;

    @Autowired
    public VotingMapper(ModelMapper mapper, RestaurantRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Voting.class, VotingTo.class)
                .addMappings(m -> m.skip(VotingTo::setRestaurant_id)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(VotingTo.class, Voting.class)
                .addMappings(m -> m.skip(Voting::setRestaurant)).setPostConverter(toEntityConverter());
    }


    Converter<Voting, VotingTo> toDtoConverter() {
        return context -> {
            Voting source = context.getSource();
            VotingTo destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    Converter<VotingTo, Voting> toEntityConverter() {
        return context -> {
            VotingTo source = context.getSource();
            Voting destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private Integer getId(Voting source) {
        return Objects.isNull(source) ? null : source.getRestaurant().getId();
    }

    void mapSpecificFields(Voting source, VotingTo destination) {
        destination.setRestaurant_id(getId(source));
    }

    void mapSpecificFields(VotingTo source, Voting destination) {
        destination.setRestaurant(repository.findById(source.getRestaurant_id()).orElse(null));
    }

    public Voting toEntity(VotingTo dto) {
        return Objects.isNull(dto)
                ? null
                : mapper.map(dto, Voting.class);
    }

    public VotingTo toDto(Voting entity) {
        return Objects.isNull(entity)
                ? null
                : mapper.map(entity, VotingTo.class);
    }
}
