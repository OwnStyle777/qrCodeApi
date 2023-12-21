package qrcodeapi;

import org.springframework.http.ResponseEntity;

public interface ErrorMessages {
    default ResponseEntity nullOrBlank(){
        String errorMessage = "Contents cannot be null or blank";
        return ResponseEntity
                .badRequest()
                .body("{\"error\": \"" + errorMessage + "\"}");
    }

    default ResponseEntity wrongSize(){
        String errorMessage = "Image size must be between 150 and 350 pixels";
        return ResponseEntity
                .badRequest()
                .body("{\"error\": \"" + errorMessage + "\"}");
    }

    default ResponseEntity wrongType(){
        String errorMessage = "Only png, jpeg and gif image types are supported";
        return ResponseEntity
                .badRequest()
                .body("{\"error\": \"" + errorMessage + "\"}");
    }
}
