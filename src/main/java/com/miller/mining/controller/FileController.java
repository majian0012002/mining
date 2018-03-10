package com.miller.mining.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping(value="file")
public class FileController {

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    public static final String downloadFilePath = "/opt/downloadFiles";

    @RequestMapping("download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
        logger.info("开始准备下载文件");

        String filename = request.getParameter("filename");
        File file = new File(FileController.downloadFilePath  + "/" + filename);
        if (file.exists()) {
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + filename);

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            OutputStream os = null;

            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bis.close();
                    fis.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            logger.info("文件下载结束");
        }
        return null;
    }
}
