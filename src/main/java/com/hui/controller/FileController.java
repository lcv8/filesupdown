package com.hui.controller;

import com.hui.entity.User;
import com.hui.entity.UserFile;
import com.hui.service.UserFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author lcv8
 */
@Controller
@RequestMapping("file")
@Slf4j
public class FileController {

    @Autowired
    private UserFileService userFileService;

    @GetMapping("findAll")
    public String findAll(HttpSession session , Model model){
        User user = (User) session.getAttribute("user");
        log.info("user----->"+user.toString());
        List<UserFile> list = userFileService.findAllById(user.getId());
        model.addAttribute("list",list);
        return "files/showAll";
    }

    //JSON
    @GetMapping("findAllByJson")
    @ResponseBody
    public List<UserFile> findAllByJson(HttpSession session){
        User user = (User) session.getAttribute("user");
        List<UserFile> userFile = userFileService.findAllById(user.getId());
        return userFile;
    }

    @PostMapping("/upload")
    public String upload(MultipartFile youFile , HttpSession session){
        //获取用户ID
        User user = (User) session.getAttribute("user");
        Integer userId = user.getId();
        //获取文件原始名称
        String originalFilename = youFile.getOriginalFilename();
        //文件后缀
        String extension = FilenameUtils.getExtension(originalFilename);
        //生成新的文件名称
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + UUID.randomUUID().toString().replace("-","") +"."+ extension;
        //文件大小
        long size = youFile.getSize();
        //文件类型
        String contentType = youFile.getContentType();
        //根据日期生成目录
        String realPath = null;
        String dataPath = null;
        try {
            realPath = ResourceUtils.getURL("classpath:").getPath() + "/static/files";
            dataPath = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            File file = new File(realPath, dataPath);
            if(!file.exists()){
                file.mkdirs();
            }
            //上传
            youFile.transferTo(new File(file,newFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserFile userFile = new UserFile();
        userFile.setOldFileName(originalFilename).setNewFileName(newFileName).
                setExt(extension).setSize(String.valueOf(size)).setType(contentType).setPath("/files/"+dataPath).setUserId(userId);
        userFileService.save(userFile);
        return "redirect:/file/findAll";
    }

    //文件下载
    @GetMapping("download")
    public void download(String openStyle, Integer id , HttpServletResponse response) {
        UserFile file = userFileService.findFileById(id);
        //打开方式
        String style = "attachment";
        openStyle = openStyle == null ? style :openStyle;
        //根据文件信息中文件名字 和 文件存储路径获取文件输入流
        String path;
        FileInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            path = ResourceUtils.getURL("classpath:").getPath() + "/static" + file.getPath();
            log.info("realpath------>" + path);
            log.info("filename------>" + file.getNewFileName());
            //获得文件输出流
            inputStream = new FileInputStream(new File(path, file.getNewFileName()));
            //响应流
            outputStream = response.getOutputStream();
            response.setHeader("content-disposition",openStyle+";fileName="+ URLEncoder.encode(file.getOldFileName(),"UTF-8"));
            IOUtils.copy(inputStream,outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
        //更新下载次数
        if(style.equals(openStyle)){
            file.setDownCounts(file.getDownCounts()+1);
            userFileService.update(file);
        }
    }

    //删除
    @GetMapping("delete")
    public String delete(Integer id) throws FileNotFoundException {
        //服务器删除
        UserFile userFile = userFileService.findFileById(id);
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static" + userFile.getPath();
        File file = new File(realPath, userFile.getNewFileName());
        if(file.exists()){
            file.delete();
        }
        //删除数据库
        userFileService.delete(id);
        return "redirect:/file/findAll";
    }
}
