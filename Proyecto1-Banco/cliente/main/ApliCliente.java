package main;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class ApliCliente {

	

	public static void main(String[] args) throws RemoteException, NotBoundException, SQLException {  
        
	Cliente cliente1 = null;
		try {
			 cliente1 = new Cliente("10.15.5.26",7559);
		} catch (RemoteException e) {
			try {
				 cliente1 = new Cliente("10.15.5.238",7559);
			} catch (RemoteException e1) {
				System.out.println("Fallo al conectarse a replica.");
			}
		}
		
		cliente1.start();
		
		//Aca crear nuevos clientes y realizar operaciones aleatorias con ellos
		
		
		
	}
}
