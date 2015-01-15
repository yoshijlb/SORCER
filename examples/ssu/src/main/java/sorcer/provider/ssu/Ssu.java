package sorcer.provider.ssu;

import sorcer.service.Context;
import sorcer.service.ContextException;

import java.rmi.RemoteException;

@SuppressWarnings("rawtypes")
public interface Ssu {

	public Context factorial(Context context) throws RemoteException, ContextException;
}
