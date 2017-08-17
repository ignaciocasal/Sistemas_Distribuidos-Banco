package main;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ApliCliente {

	public static void main(String[] args) throws RemoteException, NotBoundException {  
        
		Cliente cliente1 = new Cliente("192.168.185.190",7559);
		
		cliente1.iniciarSesion("39203249", "1234");
		
	}
}
