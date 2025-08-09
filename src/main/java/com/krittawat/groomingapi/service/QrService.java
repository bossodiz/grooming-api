package com.krittawat.groomingapi.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrService {
    public String toPngBase64(String payload, int size) throws Exception {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix matrix = new MultiFormatWriter()
                .encode(payload, BarcodeFormat.QR_CODE, size, size, hints);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            javax.imageio.ImageIO.write(image, "png", baos);
            return java.util.Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }
}