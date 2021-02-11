package com.hui.service;

import com.hui.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lcv8
 */

public interface UserService {
    User login(User user);
}
