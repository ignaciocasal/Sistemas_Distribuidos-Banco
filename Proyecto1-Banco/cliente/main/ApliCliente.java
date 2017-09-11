package main;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class ApliCliente {

	

	public static void main(String[] args) throws RemoteException, NotBoundException, SQLException {  
        
	Cliente cliente1 = null, cliente2 = null, cliente3 = null, cliente4 = null, cliente5 = null;
	
		try {
			 cliente1 = new Cliente("192.168.1.109",7559);
			 cliente2 = new Cliente("192.168.1.109",7559);
			 cliente3 = new Cliente("192.168.1.109",7559);
			 cliente4 = new Cliente("192.168.1.109",7559);
			 cliente5 = new Cliente("192.168.1.109",7559);

		} catch (RemoteException e) {
			try { //Redireccionamiento a replica
				 cliente1 = new Cliente("192.168.1.110",7559);
				 cliente2 = new Cliente("192.168.1.110",7559);
				 cliente3 = new Cliente("192.168.1.110",7559);
				 cliente4 = new Cliente("192.168.1.110",7559);
				 cliente5 = new Cliente("192.168.1.110",7559);
			} catch (RemoteException e1) {
				System.out.println("Fallo al conectarse a réplica.");
			}
		}
		
		cliente1.start();
		cliente2.start();
		cliente3.start();
		cliente4.start();
		cliente5.start();
		
	}
}
