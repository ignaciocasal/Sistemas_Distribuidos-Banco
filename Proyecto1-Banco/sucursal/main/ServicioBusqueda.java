package main;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioBusqueda extends Remote {
	
	
	//Este es el metodo que implementara el servidor
	boolean existeCuenta(Integer nroCuenta) throws RemoteException;
	boolean login(String dni, String clave) throws RemoteException;
	Float consultarDinero(String dni) throws RemoteException;
	Float depositarDinero(String dni, Float dinero) throws RemoteException;
	boolean depositarDineroCuenta(String dni, Float dinero, Integer nroCuenta) throws RemoteException;
	Respuesta extraerDinero(String dni, Float dinero) throws RemoteException;
	
	
	
}
