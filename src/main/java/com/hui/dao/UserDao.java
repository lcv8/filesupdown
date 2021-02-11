package com.hui.dao;

import com.hui.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author lcv8
 */
@Repository
public interface UserDao {
    User login(User user);
}
