package qrcodeapi;

import org.apache.catalina.LifecycleState;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


@RestController
public class TaskController {


    @GetMapping("/api/health")
        public ResponseEntity healthStatus() {
          return ResponseEntity.status(200).build();

        }

//    @GetMapping("/api/qrcode")
//    public ResponseEntity qrcodeStatus() {
//        return ResponseEntity.status(501).build();
//
//    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<?> getImage(int size, String type) {

        if (size <= 350 && size >= 150) {

            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            image.createGraphics().fillRect(0, 0, size, size);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if(type.equals("png") || type.equals("gif") || type.equals("jpeg")){
            try {
                ImageIO.write(image, type, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("image/" + type.toLowerCase()));
            headers.setContentLength(resource.contentLength());

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);}
            else{
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
}
