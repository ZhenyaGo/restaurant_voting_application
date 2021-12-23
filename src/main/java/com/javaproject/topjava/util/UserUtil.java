package com.javaproject.topjava.util;

import com.javaproject.topjava.model.Role;
import com.javaproject.topjava.model.User;
import com.javaproject.topjava.to.UserTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserUtil {

//    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

//    public static User prepareToSave(User user) {
//        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
//        user.setEmail(user.getEmail().toLowerCase());
//        return user;
//    }
}
