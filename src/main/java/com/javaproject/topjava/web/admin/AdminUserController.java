package com.javaproject.topjava.web.admin;

import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.javaproject.topjava.model.User;
import com.javaproject.topjava.repository.UserRepository;

import static com.javaproject.topjava.util.validation.ValidationUtil.*;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserController {

    static final String REST_URL = "/api/admin/users";


    private final UserRepository repository;

    public AdminUserController(UserRepository repository) {
        this.repository = repository;
    }


    @GetMapping(value = "/{id}")
    public User get(@PathVariable int id) {
        return repository.findById(id).orElse(null);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        repository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@RequestBody User user) {
        checkNew(user);
        User created = repository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody User user, @PathVariable int id) {
        assureIdConsistent(user, id);
        checkNotFoundWithId(repository.save(user), user.id());
    }


    @GetMapping("/by-email")
    public ResponseEntity<User> getByEmail(@RequestParam String email) {
        return ResponseEntity.of(
                checkNotFound(repository.getByEmail(email), "email=" + email));
    }


    @GetMapping
    public List<User> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

}
