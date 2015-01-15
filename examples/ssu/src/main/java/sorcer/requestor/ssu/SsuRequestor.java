package sorcer.requestor.ssu;

import sorcer.core.requestor.ServiceRequestor;
import sorcer.provider.ssu.Ssu;
import sorcer.service.*;

import java.io.File;
import java.io.IOException;

import static sorcer.co.operator.inEnt;
import static sorcer.eo.operator.*;

public class SsuRequestor extends ServiceRequestor {

    public Exertion getExertion(String... args)
            throws ExertionException, ContextException, SignatureException, IOException {


        if (args[1].equals("netlet")) {
            return (Exertion) evaluate(new File("src/main/netlets/ssu-netlet.groovy"));
        } else if (args[1].equals("dynamic")) {
            return (Exertion) evaluate(new File("src/main/netlets/dynamic-ssu-netlet.groovy"));
        }
        Class serviceType =  Ssu.class;

        Double v1 = new Double(getProperty("arg/x1"));
        Double v2 = new Double(getProperty("arg/x2"));
        
        return task("hello ssu", sig("factorial", serviceType),
                    context("ssu", inEnt("arg/x1", v1), inEnt("arg/x2", v2),
                            result("out/y")));
    }

    @Override
    public void postprocess(String... args) throws ExertionException, ContextException {
        super.postprocess();
        logger.info("<<<<<<<<<< factorial task: \n" + exertion);
    }
}
