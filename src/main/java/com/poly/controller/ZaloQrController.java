package com.poly.controller;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Map;

import javax.imageio.ImageIO;

@RestController
public class ZaloQrController {

    private static final String OA_LINK = "https://zalo.me/726422128057206879";

    @GetMapping(value = "/qrzaloCustom", produces = MediaType.IMAGE_PNG_VALUE)
    public void qrZaloCustom(HttpServletResponse resp) throws Exception {
        // 1) Tạo QR code payload = link chat OA
        String payload = OA_LINK;

        Map<EncodeHintType,Object> hints = Map.of(
          EncodeHintType.CHARACTER_SET, "UTF-8",
          EncodeHintType.MARGIN,         Integer.valueOf(1)
        );
        BitMatrix matrix = new MultiFormatWriter()
            .encode(payload, BarcodeFormat.QR_CODE, 300, 300, hints);

        // 2) Chuyển thành BufferedImage
        BufferedImage qrImg = MatrixToImageWriter.toBufferedImage(matrix);

        // 3) Đọc logo Zalo (kích thước nhỏ ~ 60×60)
        //    Thả file zalo-logo.png trong /src/main/resources/static/images/
        InputStream logoIs = 
            getClass().getResourceAsStream("/static/images/zalo-logo.png");
        BufferedImage logo = ImageIO.read(logoIs);

        // 4) Vẽ nền trắng tròn + overlay logo
        Graphics2D g = qrImg.createGraphics();
        int logoSize = 60;
        int x = (qrImg.getWidth() - logoSize) / 2;
        int y = (qrImg.getHeight() - logoSize) / 2;
        // vẽ nền tròn trắng
        g.setColor(Color.WHITE);
        g.fillOval(x - 4, y - 4, logoSize + 8, logoSize + 8);
        // vẽ logo lên
        g.drawImage(logo, x, y, logoSize, logoSize, null);
        g.dispose();

        // 5) Trả về PNG
        resp.setContentType(MediaType.IMAGE_PNG_VALUE);
        ImageIO.write(qrImg, "PNG", resp.getOutputStream());
    }
}
