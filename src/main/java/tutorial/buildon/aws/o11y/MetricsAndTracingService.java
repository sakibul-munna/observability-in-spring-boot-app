package tutorial.buildon.aws.o11y;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static java.lang.Runtime.getRuntime;
import static tutorial.buildon.aws.o11y.Constants.*;

@Service
public class MetricsAndTracingService {

    @Value("otel.traces.api.version")
    private String tracesApiVersion;

    @Value("otel.metrics.api.version")
    private String metricsApiVersion;

    private Tracer tracer;
    private Meter meter;
    private LongCounter numberOfExecutions;

    @PostConstruct
    public void init() {
        tracer = GlobalOpenTelemetry.getTracer("io.opentelemetry.traces.hello", tracesApiVersion);
        meter = GlobalOpenTelemetry.meterBuilder("io.opentelemetry.metrics.hello")
                .setInstrumentationVersion(metricsApiVersion)
                .build();
        createMetrics();
    }

    public void createMetrics() {
        //a synchronous metric
        numberOfExecutions =
                meter
                        .counterBuilder(NUMBER_OF_EXEC_NAME)
                        .setDescription(NUMBER_OF_EXEC_DESCRIPTION)
                        .setUnit("int")
                        .build();

        //an asynchronous metric
        meter
                .gaugeBuilder(HEAP_MEMORY_NAME)
                .setDescription(HEAP_MEMORY_DESCRIPTION)
                .setUnit("byte")
                .buildWithCallback(
                        r -> {
                            r.record(getRuntime().totalMemory() - getRuntime().freeMemory());
                        });
    }

    public void logAndTrace(Response response) {
        // Creating a custom span
        Span span = tracer.spanBuilder("mySpan").startSpan();
        try (Scope scope = span.makeCurrent()) {
            // Update the synchronous metric
            numberOfExecutions.add(1);
        } finally {
            span.end();
        }
    }
}

