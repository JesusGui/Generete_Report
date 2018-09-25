package com.example.demo;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.Map;

@RestController
@CrossOrigin("/*")
public class ControllerReport {



    @PostMapping(value = "/report")
    @ResponseBody
    public void ResponsePDF(HttpServletResponse response , @RequestBody Map<String,Object> params) throws JRException,IOException{
        InputStream jasperStream = this.getClass().getResourceAsStream("/jasperreports/"+params.get("doc")+".jasper");
        params.remove("doc");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        String base = params.get("logo").toString().split(",")[1];
        params.put("logo",(Image)decodeToImage(base));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,params, new JREmptyDataSource());
        response.setContentType("application/x-pdf");
        response.setHeader("Content-disposition", "inline; filename=helloworldReport.pdf");
        final OutputStream outStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
    }

    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }



}
