package com.mm.utils;

import com.mm.pojo.FileEntity;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tourist
 * @version 1.0
 * @package com.mm.utils
 * @date 2019/7/11 15:46
 * @Description
 */
public class HDFSTools {
    private FileSystem fileSystem;
    private Logger logger = Logger.getLogger(HDFSTools.class);
    private Configuration configuration;
    private List<FileStatus> list=new ArrayList<FileStatus>();

    public HDFSTools() {
        configuration = new Configuration();
        configuration.set("dfs.replication", "2");
        try {
            //fileSystem = FileSystem.get(new URI("hdfs://linux11:9000"), configuration);
            fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), configuration, "root");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归遍历目录下的文件  只列出文件名
     * @param path
     * @return
     */
    public  List<FileEntity> list(String path){
        List<FileEntity> fileList=new ArrayList<FileEntity>();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        try {
            RemoteIterator<LocatedFileStatus> remoteIterator = fileSystem.listFiles(new Path(path), true);
            while(remoteIterator.hasNext()){
                LocatedFileStatus  fileStatus= remoteIterator.next();
                FileEntity file = new FileEntity();
                Path p = fileStatus.getPath();
                String fileName = p.getName();
                file.setFileName(fileName);
                long modificationTime = fileStatus.getModificationTime();
                file.setCreateTime(simpleDateFormat.format(modificationTime));
                file.setPath(p.toString());
                System.out.println(p.toString());
                fileList.add(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    /**
     * 获取文件输入流
     * @param dest
     * @return
     * @throws IOException
     */
    public  FSDataInputStream getFileInputStream(String dest) throws IOException {
        FSDataInputStream fsDataInputStream = fileSystem.open(new Path(dest));
        return fsDataInputStream;
    }


    /**
     * 递归删除
     * @param path
     * @return
     */
    public boolean delete(String path){
        boolean flag = false;
        try {
            flag = fileSystem.delete(new Path(path), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 递归遍历目录下的文件  只列出文件
     * @param path
     * @return
     * @throws Exception
     *//*
    public List<FileStatus> getFiles(String path) throws Exception {
        List<FileStatus> fileStatuses=new ArrayList<FileStatus>();
        RemoteIterator<LocatedFileStatus> remoteIterator = fileSystem.listFiles(new Path(path), true);
        while(remoteIterator.hasNext()){
            LocatedFileStatus  fileStatus= remoteIterator.next();
            fileStatuses.add(fileStatus);
        }
        return fileStatuses;
    }
*/

    /**
     * 递归遍历 可以列出目录名
     * @param path
     * @return
     */
    public List<FileStatus> getFileStatus(String path) throws Exception {
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(path));
        for (FileStatus fileStatus : fileStatuses) {
            if(fileStatus.isDirectory()){
                listAllFiles(fileSystem,fileStatus.getPath());
            }else {
                list.add(fileStatus);
            }
        }
        return list;
    }

    private void listAllFiles(FileSystem fileSystem,Path path) throws  Exception{
        FileStatus[] fileStatuses = fileSystem.listStatus(path);
        for (FileStatus fileStatus : fileStatuses) {
            if(fileStatus.isDirectory()){
                listAllFiles(fileSystem,fileStatus.getPath());
            }else{
                list.add(fileStatus);
            }
        }
    }

    /**
     * 文件上传
     *
     * @param src  源文件
     * @param dest 目标文件
     */
    public void put(String src, String dest) {
        try {

            fileSystem.copyFromLocalFile(new Path(src), new Path(dest));
            System.out.println("文件上传成功!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putByIOUtils(String src, String dest) {
        FileInputStream fileInputStream = null;
        FSDataOutputStream fsDataOutputStream = null;
        try {
            fileInputStream = new FileInputStream(src);
            fsDataOutputStream = fileSystem.create(new Path(dest));

            IOUtils.copyBytes(fileInputStream, fsDataOutputStream, configuration);

            logger.info("文件通过IO上传成功!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                fsDataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean putByIOUtils(InputStream inputStream, String dest) {

        FSDataOutputStream fsDataOutputStream = null;
        try {
            fsDataOutputStream = fileSystem.create(new Path(dest));

            IOUtils.copyBytes(inputStream, fsDataOutputStream, configuration);

            logger.info("文件通过IO上传成功!");

            return  true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                fsDataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
