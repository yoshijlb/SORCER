package sorcer.sml.jobs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sorcer.test.ProjectContext;
import org.sorcer.test.SorcerTestRunner;
import sorcer.arithmetic.provider.impl.AdderImpl;
import sorcer.arithmetic.provider.impl.AveragerImpl;
import sorcer.arithmetic.provider.impl.MultiplierImpl;
import sorcer.arithmetic.provider.impl.SubtractorImpl;
import sorcer.core.SorcerConstants;
import sorcer.core.provider.rendezvous.ServiceJobber;
import sorcer.service.Context;
import sorcer.service.Job;
import sorcer.service.Signature;
import sorcer.service.Task;

import java.util.logging.Logger;

import static org.junit.Assert.*;
import static sorcer.co.operator.*;
import static sorcer.eo.operator.*;


/**
 * @author Mike Sobolewski
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@RunWith(SorcerTestRunner.class)
@ProjectContext("examples/sml")
public class LocalJobExertions implements SorcerConstants {

	private final static Logger logger = Logger.getLogger(LocalJobExertions.class.getName());
	
	@Test
	public void exertAdderProvider() throws Exception {

		Task t5 = task("t5",
				sig("add", AdderImpl.class),
				context("add", inEnt("arg, x1", 20.0),
						inEnt("arg, x2", 80.0), result("result/y")));
		
		t5 = exert(t5);
		logger.info("t5 context: " + context(t5));
		assertEquals(value(context(t5), "result/y"), 100.0);

	}
	
	@Test
	public void evaluateAdderProvider() throws Exception {

		Task t5 = task("t5",
				sig("add", AdderImpl.class),
				context("add", inEnt("arg, x1", 20.0),
						inEnt("arg, x2", 80.0), result("result/y")));
		
		assertEquals(value(t5), 100.0);

	}
	
	@Test
	public void exertAveragerProvider() throws Exception {

		Task t5 = task(
				"t5",
				sig("average", AveragerImpl.class),
				context("average", inEnt("arg, x1", 20.0),
						inEnt("arg, x2", 80.0), result("result/y")));
		t5 = exert(t5);
		logger.info("t5 context: " + context(t5));
		assertEquals(value(context(t5), "result/y"), 50.0);

	}
	
	@Test
	public void taskConcatenation() throws Exception {

		Task t3 = task(
				"t3",
				sig("subtract", SubtractorImpl.class),
				context("subtract", inEnt("arg/x1"), inEnt("arg/x2"),
						outEnt("result/y")));

		Task t4 = task(
				"t4",
				sig("multiply", MultiplierImpl.class),
				context("multiply", inEnt("arg/x1", 10.0), inEnt("arg/x2", 50.0),
						outEnt("result/y")));

		Task t5 = task(
				"t5",
				sig("add", AdderImpl.class),
				context("add", inEnt("arg/x1", 20.0), inEnt("arg/x2", 80.0),
						outEnt("result/y")));

		Job job = job(sig("service", ServiceJobber.class),
				"j1", t4, t5, t3,
				pipe(out(t4, "result/y"), in(t3, "arg/x1")),
				pipe(out(t5, "result/y"), in(t3, "arg/x2")));
		
		Context context = serviceContext(exert(job));
		logger.info("job context: " + context);
		assertEquals(get(context, "j1/t3/result/y"), 400.0);

	}

	@Test
	public void nestingJobComposition() throws Exception {

		Task t3 = task(
				"t3",
				sig("subtract", SubtractorImpl.class),
				context("subtract", inEnt("arg/x1", null), inEnt("arg/x2", null),
						outEnt("result/y", null)));

		Task t4 = task(
				"t4",
				sig("multiply", MultiplierImpl.class),
				context("multiply", inEnt("arg/x1", 10.0), inEnt("arg/x2", 50.0),
						outEnt("result/y", null)));

		Task t5 = task(
				"t5",
				sig("add", AdderImpl.class),
				context("add", inEnt("arg/x1", 20.0), inEnt("arg/x2", 80.0),
						outEnt("result/y", null)));

		// Service Composition j1(j2(t4(x1, x2), t5(x1, x2)), t3(x1, x2))
		Job job = job(
				"j1", sig("service", ServiceJobber.class),
				job("j2", t4, t5), t3,
				pipe(out(t4, "result/y"), in(t3, "arg/x1")),
				pipe(out(t5, "result/y"), in(t3, "arg/x2")));

		Context context = serviceContext(exert(job));
		logger.info("job context: " + context);
		assertEquals(get(context, "j1/t3/result/y"), 400.0);

	}

	@Test
	public void contexterTest() throws Exception {

		// get a context for the template context in the task
		Task cxtt = task("addContext", sig("getContext", NetJobExertions.createContext()),
				context("add", input("arg/x1"), input("arg/x2")));

		Context result = context(exert(cxtt));
//		logger.info("contexter context: " + result);
		assertEquals(get(result, "arg/x1"), 20.0);
		assertEquals(get(result, "arg/x2"), 80.0);

	}
	
	@Test
	public void objectContexterTaskTest() throws Exception {

		Task t5 = task("t5", sig("add", AdderImpl.class), 
					type(sig("getContext", NetJobExertions.createContext()), Signature.APD),
					context("add", inEnt("arg/x1"), inEnt("arg/x2"),
						result("result/y")));
		
		Context result = context(exert(t5));
//		logger.info("task context: " + result);
		assertEquals(get(result, "result/y"), 100.0);

	}
	
	@Test
	public void serviceJob() throws Exception {

		Task t3 = srv("t3", sig("subtract", SubtractorImpl.class),
				cxt("subtract", inEnt("arg/x1"), inEnt("arg/x2"), outEnt("result/y")));

		Task t4 = srv("t4",
				sig("multiply", MultiplierImpl.class),
				// cxt("multiply", in("super/arg/x1"), in("arg/x2", 50.0),
				cxt("multiply", inEnt("arg/x1", 10.0), inEnt("arg/x2", 50.0),
						outEnt("result/y")));

		Task t5 = srv(
				"t5",
				sig("add", AdderImpl.class),
				cxt("add", inEnt("arg/x1", 20.0), inEnt("arg/x2", 80.0),
						outEnt("result/y")));

		// Service Composition j1(j2(t4(x1, x2), t5(x1, x2)), t3(x1, x2))
		Job job = srv(
				"j1",
				sig("execute", ServiceJobber.class),
				cxt(inEnt("arg/x1", 10.0),
						result("job/result", from("j1/t3/result/y"))),
				srv("j2", sig("execute", ServiceJobber.class), t4, t5), t3,
				pipe(out(t4, "result/y"), in(t3, "arg/x1")),
				pipe(out(t5, "result/y"), in(t3, "arg/x2")));

		Context context = serviceContext(exert(job));
		logger.info("job context: " + context);
		get(context, "j1/t3/result/y");
		assertEquals(get(context, "j1/t3/arg/x1"), 500.0);
		assertEquals(get(context, "j1/t3/arg/x2"), 100.0);
		assertEquals(get(context, "j1/t3/result/y"), 400.0);

	}
	
}
