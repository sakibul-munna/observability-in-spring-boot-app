package tutorial.buildon.aws.o11y;

import java.util.Objects;

public class Response {
    Response(String message) {
        Objects.requireNonNull(message);
    }

    public boolean isValid() {
        return true;
    }
}
