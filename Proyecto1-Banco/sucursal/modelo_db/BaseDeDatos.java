package modelo_db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que permite conectar con la base de datos.
 *
 */
public final class BaseDeDatos {
	/** Nombre del driver de base de datos. */
	private static final String DRIVER_NAME = "postgresql";
	/** Nombre del esquema. */
	private final static String DB_NAME = "bancoDB";
	/** Ip de la base de datos. */
	private final static String DB_IP = "localhost";
	/** Puerto de la base de datos. */
	private final static String DB_PORT = "5433";
	/** Nombre de usuario. */
	private final static String USER_NAME = "postgres";
	/** Password de usuario. */
	private final static String USER_PASSWORD = "root";
	/** Url de conexion completo. */
	private final static String CONNECTION_URL = "jdbc:" + DRIVER_NAME + "://" + DB_IP + ":" + DB_PORT + "/" + DB_NAME;

	/**
	 * Crea una nueva conexion a la base de datos con los parametros estaticos
	 * configurados en esta clase.
	 * 
	 * @return conexion nueva con la base de datos.
	 * @throws SQLException
	 *             en caso de que falle la conexion.
	 * @throws ClassNotFoundException
	 */
	public static final Connection newConnection() throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(CONNECTION_URL, USER_NAME, USER_PASSWORD);
	}

}