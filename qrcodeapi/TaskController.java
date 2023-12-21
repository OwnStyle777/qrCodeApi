package qrcodeapi;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.awt.image.BufferedImage;



@RestController
public class TaskController implements ErrorMessages {
    private final QRCodeImageService qrcodeService = new QRCodeImageService();

    @GetMapping("/api/health")
    public ResponseEntity healthStatus() {
        return ResponseEntity.status(200).build();

    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<?> getImage(@RequestParam(required = false) String contents,
                                      @RequestParam(required = false, defaultValue = "250") int size,
                                      @RequestParam(required = false, defaultValue = "png") String type,
                                      @RequestParam(required = false, defaultValue = "L") String correction) {

        if (contents.isBlank()) {
            return nullOrBlank();
        }

        if (size > 350 || size < 150) {
            return wrongSize();
        }

        ErrorCorrectionLevel errorCorrectionLevel;
        try {
            errorCorrectionLevel = ErrorCorrectionLevel.valueOf(correction);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Permitted error correction levels are L, M, Q, H";
            return ResponseEntity.badRequest().body("{\"error\": \"" + errorMessage + "\"}");
        }

        if (!type.equals("png") && !type.equals("jpeg") && !type.equals("gif")) {
            return wrongType();
        }

        BufferedImage bufferedImage = qrcodeService.createImage(contents, size, errorCorrectionLevel);


        ByteArrayResource resource = qrcodeService.bufferedImageToByteArray(bufferedImage, type);

        return qrcodeService.createHttpResponse(resource, type);
    }

}
