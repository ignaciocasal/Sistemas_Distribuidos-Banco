package modelo_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import main.ClienteSv;

public final class UsuarioDataAccess extends DataAccess {

	public static ClienteSv usuarioActual = null;

	private UsuarioDataAccess() {

	}

	public static DefaultTableModel bajarTabla() {
		String datos[][] = {};
		String cabecera[] = { "Nombre", "Apellido", "ID", "Pass", "Admin", "Activo" };

		DefaultTableModel modelo = new DefaultTableModel(datos, cabecera);

		final String consulta = "SELECT * FROM usuario ";
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			ResultSet res = statement.executeQuery();
			while (res.next()) {
				Object[] fila = new Object[6];
				fila[0] = res.getString("nombreUser");
				fila[1] = res.getString("apellidoUser");
				fila[2] = res.getString("idUser");
				fila[3] = res.getString("passUser");
				fila[4] = res.getBoolean("isAdmin");
				fila[5] = res.getBoolean("isActivo");
				modelo.addRow(fila);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return modelo;

	}

	///////////

	public static void registrarUsuario(Usuario user) {
		try (Connection c = BaseDeDatos.newConnection()) {
			registrarUsuario(c, user);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void registrarUsuario(Connection conn, Usuario user) {
		try (Statement statement = conn.createStatement()) {
			statement.executeUpdate("INSERT INTO usuario VALUES ('" + user.getnombreUser() + "', '"
					+ user.getapellidoUser() + "', '" + user.getidUser() + "', '" + MD5.crypt(user.getpassUser())
					+ "', '" + user.getIsAdmin() + "')");
			JOptionPane.showMessageDialog(null, "El usuario se ha registrado exitosamente", "Informaciï¿½n",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null, "Ha ocurrido un problema al registrar el usuario");
		}
	}

	public static void modificarUsuario(Usuario user) {
		try (Connection c = BaseDeDatos.newConnection()) {
			modificarUsuario(c, user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void modificarUsuario(Connection conn, Usuario user) {
		String consulta = "UPDATE usuario SET " + '"' + "idUser" + '"' + '=' + '?' + ',' + '"' + "passUser" + '"' + '='
				+ '?' + ',' + '"' + "nombreUser" + '"' + '=' + '?' + ',' + '"' + "apellidoUser" + '"' + '=' + '?' + ','
				+ '"' + "isAdmin" + '"' + '=' + '?' + ',' + '"' + "isActivo" + '"' + '=' + '?' + " WHERE " + '"'
				+ "idUser" + '"' + '=' + '?';

		try (PreparedStatement statement = conn.prepareStatement(consulta)) {
			statement.setString(1, user.getnombreUser());
			statement.setString(2, user.getapellidoUser());
			statement.setString(3, user.getidUser());
			statement.setString(4, user.getpassUser());
			statement.setBoolean(5, user.getIsAdmin());
			statement.setBoolean(6, user.getIsActivo());
			statement.executeUpdate();

			JOptionPane.showMessageDialog(null, " Se ha Modificado Correctamente ", "Confirmaciï¿½n",
					JOptionPane.INFORMATION_MESSAGE);

		} catch (SQLException e) {

			System.out.println(e);
			JOptionPane.showMessageDialog(null, "Error al Modificar", "Error", JOptionPane.ERROR_MESSAGE);

		}
	}

	public static void eliminarUsuario(String codigo) {
		try (Connection c = BaseDeDatos.newConnection()) {
			eliminarUsuario(c, codigo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void eliminarUsuario(Connection conn, String codigo) {

		String consulta = "UPDATE usuario SET " + '"' + "isActivo" + '=' + '?' + " WHERE " + '"' + "isUser" + '='
				+ codigo + "'";
		try (PreparedStatement statement = conn.prepareStatement(consulta)) {
			statement.setBoolean(6, false);
			statement.executeUpdate();
			JOptionPane.showMessageDialog(null, " Se ha Elinado Correctamente", "Información",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null, "Ha ocurrido un error al eliminar el usuario");
		}
	}

	public static String[] buscarUsuarioID(String codigo) {
		try (Connection c = BaseDeDatos.newConnection()) {
			return buscarUsuarioID(c, codigo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fallo.
		return null;
	}

	private static String[] buscarUsuarioID(Connection conn, String codigo) {
		final String consulta = "SELECT * FROM usuario WHERE " + '"' + "idUser" + '"' + '=' + "'" + codigo + "'";
		try (PreparedStatement statement = conn.prepareStatement(consulta)) {
			// statement.setString(3, codigo);
			try (ResultSet res = statement.executeQuery()) {
				while (res.next()) {
					// if (res != null) {
					String user[] = new String[6];
					user[0] = res.getString("nombreUser");
					user[1] = res.getString("apellidoUser");
					user[2] = res.getString("idUser");
					user[3] = res.getString("passUser");
					user[4] = res.getString("isAdmin");
					user[5] = res.getString("isActivo");
					return user;
				}

				// }
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error, no se conecto");
			System.out.println(e);
		}
		// Fallo.
		return null;
	}

	public static ArrayList<String[]> buscarUsuarioApellido(String codigo) {
		try (Connection c = BaseDeDatos.newConnection()) {
			return buscarUsuarioApellido(c, codigo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fallo.
		return null;
	}

	private static ArrayList<String[]> buscarUsuarioApellido(Connection conn, String codigo) {
		final String consulta = "SELECT * FROM usuario WHERE " + '"' + "apellidoUser" + '"' + '=' + "'" + codigo + "'";
		try (PreparedStatement statement = conn.prepareStatement(consulta)) {
			// statement.setString(2, codigo);
			try (ResultSet res = statement.executeQuery()) {
				ArrayList<String[]> arrayListUsers = new ArrayList<String[]>();
				while (res.next()) {
					String user[] = new String[6];
					user[0] = res.getString("nombreUser");
					user[1] = res.getString("apellidoUser");
					user[2] = res.getString("idUser");
					user[3] = res.getString("passUser");
					user[4] = res.getString("isAdmin");
					user[5] = res.getString("isActivo");
					arrayListUsers.add(user);
				}
				return arrayListUsers;
			}

		} catch (SQLException e)

		{
			JOptionPane.showMessageDialog(null, "Error, no se conecto");
			System.out.println(e);
		}
		// Fallo.
		return null;

	}

}
