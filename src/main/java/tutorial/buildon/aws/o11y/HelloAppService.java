package tutorial.buildon.aws.o11y;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.stereotype.Service;

@Service
public class HelloAppService {

    @WithSpan
    Response buildResponse() {
        return new Response("Hello World");
    }
}
