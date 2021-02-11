package com.hui.service.impl;

import com.hui.dao.UserFileDao;
import com.hui.entity.UserFile;
import com.hui.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.SimpleFormatter;

@Service
@Transactional
public class UserFileServiceImpl implements UserFileService {

    @Autowired
    private UserFileDao userFileDao;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<UserFile> findAllById(Integer id) {
        return userFileDao.findAllById(id);
    }

    @Override
    public void save(UserFile userFile) {
        //是否是图片
        String isImage = userFile.getType().startsWith("image") ? "是" :"否";
        userFile.setIsImg(isImage);
        userFile.setDownCounts(0);
        userFile.setUploadTime(new Date());
        userFileDao.save(userFile);
    }

    @Override
    public UserFile findFileById(Integer id) {
        return userFileDao.findFileById(id);
    }

    @Override
    public void update(UserFile file) {
        userFileDao.update(file);
    }

    @Override
    public void delete(Integer id) {
        userFileDao.delete(id);
    }
}
