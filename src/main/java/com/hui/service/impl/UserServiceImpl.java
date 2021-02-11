package com.hui.service.impl;

import com.hui.dao.UserDao;
import com.hui.entity.User;
import com.hui.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User login(User user) {
        return userDao.login(user);
    }
}
