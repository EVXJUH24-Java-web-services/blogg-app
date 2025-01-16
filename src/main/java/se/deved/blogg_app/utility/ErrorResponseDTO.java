package se.deved.blogg_app.utility;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorResponseDTO {
    private final Date date;
    private final String message;

    public ErrorResponseDTO(String message) {
        this.date = new Date();
        this.message = message;
    }
}
