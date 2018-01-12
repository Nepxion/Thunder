package com.nepxion.thunder.test.external;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.thunder.test.core.User;
import com.nepxion.thunder.test.core.UserService;

public class AutowireServiceImpl implements AutowireService {
    @Autowired
    private UserService userService;

    @Override
    public User getUser(String name) {
        return userService.getUser(name);
    }
}