package main;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfazReceptorMensajes extends Remote {
	//Este es el metodo que implementara el servidor
	boolean ingresarAlSistema(String dni, String clave) throws RemoteException;
	String consultarDinero(String dni) throws RemoteException;
	String depositarDinero(String dni, Float dinero) throws RemoteException;
	String depositarDineroCuenta(String dni, Float dinero, Integer nroCuenta) throws RemoteException;
	String extraerDinero(String dni, Float dinero) throws RemoteException;
	String transferirDinero(String dni, Float dinero, String cbu) throws RemoteException;
	
	//test
	void test() throws RemoteException;
	
	
}
