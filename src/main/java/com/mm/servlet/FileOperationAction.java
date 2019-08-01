package com.mm.servlet;

import com.mm.pojo.FileEntity;
import com.mm.utils.HDFSTools;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


public class FileOperationAction extends BaseAction {

    public String fileList(HttpServletRequest request, HttpServletResponse response) {

        HDFSTools hdfsTools = new HDFSTools();
        List<FileEntity> fileList = hdfsTools.list("/upload/");
        if (fileList.size() > 0) {
            request.setAttribute("fileList", fileList);
            return "/list.jsp";
        }
        return "";
    }

    public String delete(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("utf-8");
            String fileName = request.getParameter("fileName");
            HDFSTools hdfsTools = new HDFSTools();
            boolean delete = hdfsTools.delete("/upload/" + fileName);
            if(delete) {
                return "/fileoperation?action=fileList";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void download(HttpServletRequest request, HttpServletResponse response) {
        try {
            /**
             * Response的getWriter()方法连续两次输出流到页面的时候，第二次的流会包括第一次的流，
             * 所以可以使用将response.reset或者resetBuffer的方法
             */
            response.reset();
            request.setCharacterEncoding("utf-8");
            String fileName = request.getParameter("fileName");
            HDFSTools hdfsTools=new HDFSTools();
            FSDataInputStream fileInputStream = hdfsTools.getFileInputStream("/upload/" + fileName);

            //解决中文乱码问题
            //得到当前请求的浏览器的类型
            String agent = request.getHeader("User-Agent");

            //如果是火狐浏览器
            if(agent.contains("Firefox")) {
                //base64编码
                fileName = "=?UTF-8?B?"+
                        new BASE64Encoder().encode(fileName.getBytes("utf-8"))+"?=";
            } else {//其它浏览器
                fileName = URLEncoder.encode(fileName, "utf-8");
            }
            //得到文件的mime类型
            String type = getServletContext().getMimeType(fileName);
            //设置mime类型
            response.setContentType(type);
            //设置头信息 Content-Disposition：无论什么格式，都以下载方式打开
            response.setHeader("Content-Disposition", "attachment;filename="+fileName);
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            IOUtils.copy(fileInputStream,outputStream );
            IOUtils.closeQuietly(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
