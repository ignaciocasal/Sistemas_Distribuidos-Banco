package modelo_db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import modelo.Producto;

public final class ProductoDataAccess extends DataAccess {

	private ProductoDataAccess() {

	}

	public static DefaultTableModel bajarTabla() {
		String datos[][] = {};
		String cabecera[] = { "Nombre", "Marca", "ID", "Precio", "Stock", "Activo", "Punto Pedido" };

		DefaultTableModel modelo = new DefaultTableModel(datos, cabecera);
		final String consulta = "SELECT * FROM producto";
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			ResultSet res = statement.executeQuery();
			while (res.next()) {
				Object[] fila = new Object[7];
				fila[0] = res.getString("nombreProd");
				fila[1] = res.getString("marcaProd");
				fila[2] = res.getString("idProducto");
				fila[3] = String.format("%.2f", Float.valueOf(res.getString("precioProd")));
				fila[4] = res.getString("cantidadProd");
				fila[5] = res.getString("isActivo");
				fila[6] = res.getString("puntoPedido");
				// fila[6] = res.getString("requierePedido");
				modelo.addRow(fila);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return modelo;
	}

	public static DefaultTableModel bajarTablaActivos() {
		String datos[][] = {};
		String cabecera[] = { "Nombre", "Marca", "ID", "Precio", "Stock", "Punto Pedido" };

		DefaultTableModel modelo = new DefaultTableModel(datos, cabecera);

		final String consulta = "SELECT * FROM producto WHERE" + '"' + "isActivo" + '"' + " = TRUE";

		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			ResultSet res = statement.executeQuery();
			while (res.next()) {
				Object[] fila = new Object[6];
				fila[0] = res.getString("nombreProd");
				fila[1] = res.getString("marcaProd");
				fila[2] = res.getString("idProducto");
				fila[3] = String.format("%.2f", Float.valueOf(res.getString("precioProd")));
				fila[4] = res.getString("cantidadProd");
				fila[5] = res.getString("puntoPedido");
				modelo.addRow(fila);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return modelo;

	}

	public static DefaultTableModel bajarTablaReqPedido() {
		String datos[][] = {};
		String cabecera[] = { "Nombre", "Marca", "ID", "Precio", "Stock", "Activo", "Punto Pedido" };

		DefaultTableModel modelo = new DefaultTableModel(datos, cabecera);
		final String consulta = "SELECT * FROM producto WHERE " + '"' + "cantidadProd" + '"' + '<' + '"' + "puntoPedido"
				+ '"';
		try (Connection c = BaseDeDatos.newConnection()) {
			PreparedStatement statement = c.prepareStatement(consulta);
			ResultSet res = statement.executeQuery();
			while (res.next()) {
				Object[] fila = new Object[7];
				fila[0] = res.getString("nombreProd");
				fila[1] = res.getString("marcaProd");
				fila[2] = res.getString("idProducto");
				fila[3] = res.getString("precioProd");
				fila[4] = res.getString("cantidadProd");
				fila[5] = res.getString("isActivo");
				fila[6] = res.getString("puntoPedido");
				modelo.addRow(fila);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return modelo;

	}

	public static void registrarProducto(Producto prod) {
		try (Connection c = BaseDeDatos.newConnection()) {
			registrarProducto(c, prod);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void registrarProducto(Connection conn, Producto prod) {
		try (Statement statement = conn.createStatement()) {
			statement.executeUpdate("INSERT INTO producto VALUES ('" + prod.getIdProducto() + "', '" + prod.getNombre()
					+ "', '" + prod.getMarcaProd() + "', '" + prod.getCantidadProd() + "', '" + prod.getPrecioProd()
					+ "', '" + prod.getIsActivo() + "', '" + prod.getPuntoPedido() + "')");
//			JOptionPane.showMessageDialog(null, "El producto se ha añadido exitosamente", "Información",
//					JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null, "Ha ocurrido un problema al añadir el producto");
		}
	}

	public static void modificarProducto(Producto prod, Integer id) {
		try (Connection c = BaseDeDatos.newConnection()) {
			modificarProducto(c, prod, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void modificarProducto(Connection conn, Producto prod, Integer id) {
		String consulta = "UPDATE producto SET " + '"' + "nombreProd" + '"' + '=' + '?' + ',' + '"' + "marcaProd" + '"'
				+ '=' + '?' + ',' + '"' + "idProducto" + '"' + '=' + '?' + ',' + '"' + "cantidadProd" + '"' + '=' + '?'
				+ ',' + '"' + "precioProd" + '"' + '=' + '?' + ',' + '"' + "isActivo" + '"' + '=' + '?' + ',' + '"'
				+ "puntoPedido" + '"' + '=' + '?' + " WHERE " + '"' + "idProducto" + '"' + '=' + id;
		try (PreparedStatement statement = conn.prepareStatement(consulta)) {

			statement.setString(1, prod.getNombre());
			statement.setString(2, prod.getMarcaProd());
			statement.setInt(3, prod.getIdProducto());
			statement.setInt(4, prod.getCantidadProd());
			statement.setDouble(5, prod.getPrecioProd());
			statement.setBoolean(6, prod.getIsActivo());
			statement.setInt(7, prod.getPuntoPedido());
			statement.executeUpdate();

//			JOptionPane.showMessageDialog(null, " Se ha Modificado Correctamente ", "Confirmación",
//					JOptionPane.INFORMATION_MESSAGE);

		} catch (SQLException e) {

			System.out.println(e);
			JOptionPane.showMessageDialog(null, "Error al Modificar", "Error", JOptionPane.ERROR_MESSAGE);

		}
	}

	public static void eliminarProducto(Integer codigo) {
		try (Connection c = BaseDeDatos.newConnection()) {
			eliminarProducto(c, codigo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void eliminarProducto(Connection conn, Integer codigo) {
		try (Statement statement = conn.createStatement()) {
			statement.executeUpdate("UPDATE producto SET " + '"' + "isActivo" + '"' + " = " + false + " WHERE " + '"'
					+ "idProducto" + '"' + '=' + codigo);
//			JOptionPane.showMessageDialog(null, " Se ha Eliminado Correctamente", "Información",
//					JOptionPane.INFORMATION_MESSAGE);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null, "Ha ocurrido un error al eliminar el producto");
		}
	}

	// POR ID
	public static String[] buscarProductoID(int codigo) {
		try (Connection c = BaseDeDatos.newConnection()) {
			return buscarProductoID(c, codigo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fallo.
		return null;
	}

	public static String[] buscarProductoID(Connection conn, int codigo) {
		final String consulta = "SELECT * FROM producto WHERE " + '"' + "idProducto" + '"' + '=' + "'" + codigo + "'";

		try (PreparedStatement statement = conn.prepareStatement(consulta)) {
			// statement.setInt(3, codigo);
			try (ResultSet res = statement.executeQuery()) {
				while (res.next()) {
					// if (res != null) {
					String prod[] = new String[7];
					prod[0] = res.getString("nombreProd");
					prod[1] = res.getString("marcaProd");
					prod[2] = res.getString("idProducto");
					prod[3] = res.getString("precioProd");
					prod[4] = res.getString("cantidadProd");
					prod[5] = res.getString("isActivo");
					prod[6] = res.getString("puntoPedido");
					return prod;
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error, no se conecto");
			System.out.println(e);
		}
		// Fallo.
		return null;
	}

	// POR NOMBRE
	public static ArrayList<String[]> buscarProductoNombre(String codigo) {
		try (Connection c = BaseDeDatos.newConnection()) {
			return buscarProductoNombre(c, codigo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fallo.
		return null;
	}

	private static ArrayList<String[]> buscarProductoNombre(Connection conn, String codigo) {
		final String consulta = "SELECT * FROM producto WHERE " + '"' + "nombreProd" + '"' + '=' + "'" + codigo + "'";

		try (PreparedStatement statement = conn.prepareStatement(consulta)) {
			// statement.setString(1, codigo);
			try (ResultSet res = statement.executeQuery()) {
				ArrayList<String[]> arrayListUsers = new ArrayList<String[]>();
				while (res.next()) {
					String prod[] = new String[7];
					prod[0] = res.getString("nombreProd");
					prod[1] = res.getString("marcaProd");
					prod[2] = res.getString("idProducto");
					prod[3] = res.getString("precioProd");
					prod[4] = res.getString("cantidadProd");
					prod[5] = res.getString("isActivo");
					prod[6] = res.getString("puntoPedido");
					arrayListUsers.add(prod);
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

	// POR PRECIO
	public static ArrayList<String[]> buscarProductoPrecio(String codigo) {
		try (Connection c = BaseDeDatos.newConnection()) {
			return buscarProductoPrecio(c, codigo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Fallo.
		return null;
	}

	private static ArrayList<String[]> buscarProductoPrecio(Connection conn, String codigo) {
		final String consulta = "SELECT * FROM producto WHERE " + '"' + "precioProd" + '"' + '=' + "'" + codigo + "'";

		try (PreparedStatement statement = conn.prepareStatement(consulta)) {
			// statement.setString(5, codigo);
			try (ResultSet res = statement.executeQuery()) {
				ArrayList<String[]> arrayListUsers = new ArrayList<String[]>();
				while (res.next()) {
					String prod[] = new String[7];
					prod[0] = res.getString("nombreProd");
					prod[1] = res.getString("marcaProd");
					prod[2] = res.getString("idProducto");
					prod[3] = res.getString("precioProd");
					prod[4] = res.getString("cantidadProd");
					prod[5] = res.getString("isActivo");
					prod[6] = res.getString("puntoPedido");
					arrayListUsers.add(prod);
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
