package com.hui.service;

import com.hui.entity.User;
import com.hui.entity.UserFile;

import java.util.List;

/**
 * @author lcv8
 */

public interface UserFileService {
    List<UserFile> findAllById(Integer id);
    void save(UserFile userFile);

    UserFile findFileById(Integer id);

    void update(UserFile file);

    void delete(Integer id);
}
