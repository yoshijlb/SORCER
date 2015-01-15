package sorcer.provider.ssu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sorcer.test.ProjectContext;
import org.sorcer.test.SorcerTestRunner;
import sorcer.provider.ssu.impl.SsuImpl;
import sorcer.service.*;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static sorcer.co.operator.*;
import static sorcer.eo.operator.*;
import static sorcer.eo.operator.value;

/**
 * @author Mike Sobolewski
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@RunWith(SorcerTestRunner.class)
@ProjectContext("examples/service")
public class LocalTasks {
	private final static Logger logger = Logger.getLogger(LocalTasks.class.getName());
	
	@Test
	public void exertTask() throws Exception  {

		Service t5 = service("t5", sig("factorial", SsuImpl.class),
				cxt("factorial", inEnt("arg/x1", 4.0)));

		Service out = exert(t5);
		Context cxt = context(out);
		logger.info("out context: " + cxt);
		logger.info("context @ arg/x1: " + get(cxt, "arg/x1"));
		logger.info("context @ result/value: " + value(cxt, "result/value"));

		// get a single context argument
		assertEquals(24.0, value(cxt, "result/value"));

		// get the subcontext output from the context
		assertTrue(context(ent("arg/x1", 4.0), ent("result/value", 24.0)).equals(
				value(cxt, result("result/value", from("arg/x1", "result/value")))));

	}

	

}
	
	
