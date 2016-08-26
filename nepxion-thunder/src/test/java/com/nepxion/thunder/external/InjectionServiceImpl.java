package com.nepxion.thunder.external;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import com.nepxion.thunder.test.service.User;
import com.nepxion.thunder.test.service.UserService;

public class InjectionServiceImpl implements InjectionService {
    private UserService userService;

    @Override
    public User getUser(String name) {
        return userService.getUser(name);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}