package com.javaproject.topjava.web.admin;

import com.javaproject.topjava.model.User;
import com.javaproject.topjava.repository.UserRepository;
import com.javaproject.topjava.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@Slf4j
public class AbstractUserController {

    @Autowired
    protected UserRepository repository;

//    @Autowired
//    private UniqueMailValidator emailValidator;
//
//    @InitBinder
//    protected void initBinder(WebDataBinder binder) {
//        binder.addValidators(emailValidator);
//    }

    public ResponseEntity<User> get(int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }


    public void delete(int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

//    protected User prepareAndSave(User user) {
//        return repository.save(UserUtil.prepareToSave(user));
//    }
}
