package sorcer.provider.ssu.impl;

import sorcer.core.context.PositionalContext;
import sorcer.core.context.ServiceContext;
import sorcer.core.provider.Provider;
import sorcer.provider.ssu.Ssu;
import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("rawtypes")
public class SsuImpl implements Ssu {
	public static final String RESULT_PATH = "result/value";
	private Provider provider;
	private static Logger logger = Logger.getLogger(SsuImpl.class.getName());
	
	public void init(Provider provider) {
		this.provider = provider;
		try {
			logger = provider.getLogger();
		} catch (RemoteException e) {
			// ignore it, local call
		}
	}
	public Double silnia(Double i) 
  {
    if (i <= 0.0) 
      return 1.0;
    else 
      return i * silnia(i - 1);
  }
	public Context factorial(Context context) throws RemoteException, ContextException {
		// get inputs and outputs from the service context
		PositionalContext cxt = (PositionalContext) context;
		List<Double> inputs = cxt.getInValues();
		logger.info("inputs: " + inputs);
		List<String> outpaths = cxt.getOutPaths();
		logger.info("outpaths: " + outpaths);

		// calculate the result
		Double result = 0.0;
		Double value = inputs.get(0);
		result = silnia(value);
		logger.info("result: " + result);
		
		
		// update the service context
		if (provider != null)
			cxt.putValue("calculated/provider", provider.getProviderName());
		else
			cxt.putValue("calculated/provider", getClass().getName());
		if (((ServiceContext)context).getReturnPath() != null) {
			((ServiceContext)context).setReturnValue(result);
		} else if (outpaths.size() == 1) {
			// put the result in the existing output path
			cxt.putValue(outpaths.get(0), result);
		} else {
			cxt.putValue(RESULT_PATH, result);
		}

		// get a custom provider property
		if (provider != null) {
			try {
				int st = new Integer(provider.getProperty("provider.sleep.time"));
				if (st > 0) {
					Thread.sleep(st);
					logger.info("slept for: " + st);
					cxt.putValue("provider/slept/ms", st);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return cxt;
	}

}
