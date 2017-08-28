package main;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ApliCliente {

	public static void main(String[] args) throws RemoteException, NotBoundException {  
        
		Cliente cliente1 = new Cliente("192.168.1.105",7559);
		
		cliente1.start();
		
		//Aca crear nuevos clientes y realizar operaciones aleatorias con ellos
		
		
		
	}
}
