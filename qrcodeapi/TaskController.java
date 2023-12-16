package qrcodeapi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.catalina.LifecycleState;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


@RestController
public class TaskController {


    @GetMapping("/api/health")
    public ResponseEntity healthStatus() {
        return ResponseEntity.status(200).build();

    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<?> getImage(String contents, int size, String type) {

        if (contents.isBlank()) {

            String errorMessage = "Contents cannot be null or blank";
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\": \"" + errorMessage + "\"}");
        }

        if (size <= 350 && size >= 150) {

            BufferedImage bufferedImage = createImage(contents, size);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (type.equals("png") || type.equals("gif") || type.equals("jpeg")) {
                try {
                    ImageIO.write(bufferedImage, type, outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("image/" + type.toLowerCase()));
                headers.setContentLength(resource.contentLength());

                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                String errorMessage = "Only png, jpeg and gif image types are supported";
                return ResponseEntity
                        .badRequest()
                        .body("{\"error\": \"" + errorMessage + "\"}");
            }
        }
        String errorMessage = "Image size must be between 150 and 350 pixels";
        return ResponseEntity
                .badRequest()
                .body("{\"error\": \"" + errorMessage + "\"}");
    }

    public BufferedImage createImage(String contents, int size) {
        QRCodeWriter writer = new QRCodeWriter();
        BufferedImage bufferedImage = null;

        try {
            BitMatrix bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, size, size);
            bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bufferedImage;
    }
}
