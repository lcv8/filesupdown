package com.hui.dao;

import com.hui.entity.UserFile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFileDao {
    //id -> file
    List<UserFile> findAllById(Integer id);
    void save(UserFile userFile);
    UserFile findFileById(Integer id);
    void update(UserFile file);
    void delete(Integer id);
}
