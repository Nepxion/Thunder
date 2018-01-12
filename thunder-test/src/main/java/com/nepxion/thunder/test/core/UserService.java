package com.nepxion.thunder.test.core;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;

public interface UserService {
    User getUser(String name);

    User getUser(String name, int age);

    List<User> getUsers();

    void refreshUsers();
}