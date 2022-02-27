package com.github.zhenyago.votingsystem.web.user;

import com.github.zhenyago.votingsystem.model.User;
import com.github.zhenyago.votingsystem.to.UserTo;
import com.github.zhenyago.votingsystem.util.UserUtil;
import com.github.zhenyago.votingsystem.web.admin.AbstractUserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;

import static com.github.zhenyago.votingsystem.web.SecurityUtil.*;
import static com.github.zhenyago.votingsystem.util.validation.ValidationUtil.*;

@RestController
@Slf4j
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController extends AbstractUserController {

    static final String REST_URL = "/api/profile";

    @GetMapping
    public ResponseEntity<User> get() {
        return super.get(authId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        super.delete(authId());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        checkNew(userTo);
        User created = prepareAndSave(UserUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@RequestBody @Valid UserTo userTo) {
        assureIdConsistent(userTo, authId());
        User user = authUser();
        prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }
}
