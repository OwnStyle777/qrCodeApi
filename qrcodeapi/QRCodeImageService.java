package qrcodeapi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class QRCodeImageService {
    public BufferedImage createImage(String contents, int size, ErrorCorrectionLevel correctionLevel) {
        QRCodeWriter writer = new QRCodeWriter();
        BufferedImage bufferedImage = null;
        Map<EncodeHintType, Object> hints = Map.of(EncodeHintType.ERROR_CORRECTION, correctionLevel);

        try {
            BitMatrix bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, size, size, hints);
            bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bufferedImage;
    }

    public ResponseEntity createHttpResponse(ByteArrayResource resource, String type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("image/" + type.toLowerCase()));
        headers.setContentLength(resource.contentLength());

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    public ByteArrayResource bufferedImageToByteArray(BufferedImage bufferedImage, String type) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, type, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayResource(outputStream.toByteArray());

    }
}
