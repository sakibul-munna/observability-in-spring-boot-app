package tutorial.buildon.aws.o11y;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloAppController {

    @Autowired
    private HelloAppService helloAppService;

    @Autowired
    private MetricsAndTracingService metricsAndTracingService;

    private static final Logger log = LoggerFactory.getLogger(HelloAppController.class);

    @RequestMapping(method= RequestMethod.GET, value="/hello")
    public Response hello() {
        log.info("Here inside controller");
        Response response = helloAppService.buildResponse();
        metricsAndTracingService.logAndTrace(response);
        log.info("Returning response");
        return response;
    }

    @RequestMapping(method= RequestMethod.GET, value="/exception")
    public Response exception() throws Exception {

        log.info("Here inside exception controller");
        Response response = helloAppService.buildResponse();

        if(response.isValid()){
            //metricsAndTracingService.logAndTrace(response);
            throw new Exception("Test Exception");
        }
        log.info("Returning exception response");
        return response;
    }

}
