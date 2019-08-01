package com.mm.servlet;
import com.mm.utils.HDFSTools;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
public class FileUploadAction extends BaseAction {
    public String upload(HttpServletRequest request, HttpServletResponse response) {
            //创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //核心API—-ServletFileUpload , 负责处理上传的文件数据，并将表单中每个输入项封装成一个FileItem 对象中
            ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
            //解决上传文件名的中文乱码
            servletFileUpload.setHeaderEncoding("utf-8");
            //使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            try {
                List<FileItem> fileItems = servletFileUpload.parseRequest(request);
                if (fileItems != null && fileItems.size() > 0) {
                    String fileName = null;
                    InputStream is = null;

                    for (FileItem fileItem : fileItems) {
                        if (!fileItem.isFormField()) {
                            fileName = fileItem.getName();
                            is = fileItem.getInputStream();
                        }
                    }
                    String destPath = "/upload/" + fileName;

                    HDFSTools hdfsTools = new HDFSTools();

                    boolean upload = hdfsTools.putByIOUtils(is, destPath);

                    System.out.println(destPath + "是否上传成功:" + upload);

                    if (upload) {
                        return "/fileoperation?action=fileList";
                    } else {
                        request.setAttribute("msg", "文件上传不成功");
                        return "/upload.jsp";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return "";
    }
}
