package facsenacMaven.sistemaChat.infrastructure.exceptions;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder

public class RestError {

    private final String errorCode;
    private final String errorMessage;
    private final int status;
    private final String path;
    private final List<String> details;

}
