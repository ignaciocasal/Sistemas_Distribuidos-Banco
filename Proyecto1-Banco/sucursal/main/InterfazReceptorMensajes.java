package main;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfazReceptorMensajes extends Remote {
	//Este es el metodo que implementara el servidor
	boolean ingresarAlSistema(String dni, String clave) throws RemoteException;
	String consultarDinero() throws RemoteException;
	String depositarDinero(Float dinero) throws RemoteException;
	String depositarDineroCuenta(Float dinero, Integer nroCuenta) throws RemoteException;
	String extraerDinero(Float dinero) throws RemoteException;
	String transferirDinero(Float dinero,String cbu) throws RemoteException;
	
	//test
	void test() throws RemoteException;
	
}
