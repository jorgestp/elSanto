package prueba.santo.prubaAccesoSanto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import prueba.santo.prubaAccesoSanto.entity.Amasijo;
import prueba.santo.prubaAccesoSanto.entity.Entrada_Materia_Prima;
import prueba.santo.prubaAccesoSanto.entity.Formula;

/**
 * Hello world!
 *
 */
public class App {
	static Connection conn = null;
	static String driver = "sun.jdbc.odbc.JdbcOdbcDriver";

	static String urlDocumentos = "jdbc:ucanaccess://C:/Users/Jorge/Documents/trazabilidad_Materia_Prima.mdb";

//	static String url = "jdbc:ucanaccess://C:\\Users\\Jorge\\Documents\\bbddPrueba.accdb";
//	private final static String urlRaiz = "jdbc:ucanaccess://C:\\elSanto.mdb";
//	private final static String urlDocumentos = "jdbc:ucanaccess://C:\\Users\\USUARIO\\Documents\\elSanto.mdb";

	private static final String AMASIJOS_ID = "id_amasijo";
	private static final String AMASIJOS_LOTE_AMASIJO = "lote";
	private static final String AMASIJOS_REF_AMASIJO = "referencia_amasijo";
	private static final String AMASIJOS_DESCRIPCION = "descripcion";
	private static final String AMASIJOS_PESO = "peso_unitario";
	private static final String AMASIJOS_CANTIDAD_AMASIJO = "cantidad_amasijo";
	private static final String AMASIJOS_PESO_TOTAL = "peso_total";
	private static final String AMASIJOS_FECHA_FINALIZADO = "finalizado";

	private static final String FORMULA_REF_AMASIJO = "referencia_amasijo";
	private static final String FORMULA_REF_MATERIA_PRIMA = "referencia_materia_prima";
	private static final String FORMULA_KILOS = "kilos";

	private static final String FABRICACION_ID = "id_fabricacion";
	private static final String FABRICACION_LOTE_PRODUCTO = "lote_fabricacion";
	private static final String FABRICACION_REF_PRODUCTO = "referencia_producto";
	private static final String FABRICACION_CANTIDAD_FABRICADA = "cantidad_fabricada";
	private static final String FABRICACION_CANTIDAD_DISPONIBLE = "cantidad_disponible";

	private static final String SELECT_AMASIJOS_SIN_FINALIZAR = "SELECT * FROM AMASIJO WHERE FINALIZADO = 0";
	private static final String SELECT_FORMULA_BY_REF_ARTICULO = "SELECT * FROM FORMULA WHERE REFERENCIA_AMASIJO = ?";
	private static final String SELECT_FABRICACIONESBYREF_PRODUCTO_AND_CANTIDAD_DISPONIBLE = "SELECT * FROM FABRICACIONES "
			+ "WHERE REFERENCIA_PRODUCTO = ? AND cantidad_disponible > 0";

	private static final String INSERT_RESULTADO = "INSERT INTO RESULTADOS (referencia_envasado, descripcion, lote_envasado, lote_fabricacion, cantidad_kg, producto) "
			+ "VALUES (?,?,?,?,?,?)";

	private static final String UPDATE_FABIRCACION = "UPDATE FABRICACIONES SET CANTIDAD_DISPONIBLE = ? WHERE ID_FABRICACION = ?";

	private static final String UPDATE_ENVASADO_FINALIZADO = "UPDATE ENVASADOS SET FINALIZADO = 1 WHERE ID_ENVASADO = ?";
	private static final String INSERT_RESULTADOS_AGRUPADOS = "INSERT INTO RESULTADOS_AGRUPADOS (lote, descripcion, peso, operacion_envase) VALUES (?,?,?,?)";

	private static final Object MENSAJE_INFORMATIVO = "BIENVENIDO a la aplicación de trazabilidad de Mantecados El Santo. Se va a ejecutar la aplicación y, para visualizar los resultados, "
			+ "dirígase a la base de datos y consulte las tablas RESULTADOS o RESULTADOS AGRUPADOS."
			+ "Pulse ACEPTAR para comenzar el proceso trazabilidad.";
	private static final String TRAZABILIDAD_EL_SANTO = "Trazabilidad El Santo";

	public static Connection ejecutarConexion() throws SQLException {
		Connection conn = DriverManager.getConnection(urlDocumentos);
		return conn;
	}

	public static void main(String[] args) throws Exception {

		JOptionPane.showMessageDialog(null, MENSAJE_INFORMATIVO, TRAZABILIDAD_EL_SANTO,
				JOptionPane.INFORMATION_MESSAGE);

		Connection connection = null;
		try {
			connection = App.ejecutarConexion();

			// vemos si hay algun envasado por ejecutar
			List<Amasijo> amasijosSinTerminar = App.getListaAmasijosSinTerminar(connection);

			if (CollectionUtils.isNotEmpty(amasijosSinTerminar)) {

				recorrerLista(amasijosSinTerminar, connection);

				if (App.getListaAmasijosSinTerminar(connection).size() == 0) {

					JOptionPane.showMessageDialog(null, "Proceso terminado CORRECTAMENTE.",
							"Proceso terminado correctamente", JOptionPane.INFORMATION_MESSAGE);
				} else {

					JOptionPane.showMessageDialog(null, "Proceso terminado CORRECTAMENTE.",
							"Proceso terminado con algun artículo sin terminar "
									+ "su trazabilidad. Asegurate que haya productos fabricados suficientes para terminar el proceso. Gracias",
							JOptionPane.INFORMATION_MESSAGE);
				}

			} else {

				JOptionPane.showMessageDialog(null,
						"Todos los masijos a procesar hasta la fecha estan finalizados. Asegúrese de introducir nuevos articulos a amasijos SIN FINALIZAR. Gracias",
						TRAZABILIDAD_EL_SANTO, JOptionPane.WARNING_MESSAGE);
			}

		} catch (Exception e) {

			if (connection == null) {
				JOptionPane.showMessageDialog(null,
						"No es posible encontrar la base de datos en el directorio DOCUMENTOS con el nombre elSanto. Por favor, revise que el archivo está en esa ruta con ese nombre.",
						TRAZABILIDAD_EL_SANTO, JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	private static void recorrerLista(List<Amasijo> amasijosSinTerminar, Connection connection) throws SQLException {
		List<String> resultadosAgrupados;
		for (Amasijo amasijo : amasijosSinTerminar) {
			resultadosAgrupados = new ArrayList<String>();
			List<Formula> formulaArticulo = buscarFormulaPorArticulo(connection, amasijo.getReferencia_amasijo());

			for (Formula formula : formulaArticulo) {

				List<Entrada_Materia_Prima> fabricacionesProducto = devuelveFabricaciones(connection,
						formula.getReferencia_amasijo());

				Double kgNecesarios = (amasijo.getPeso_total());

				Double totalFabricacionDisponible = fabricacionesProducto.stream()
						.mapToDouble(Entrada_Materia_Prima::getCantidad_disponible).sum();

				if (totalFabricacionDisponible >= kgNecesarios) {

					Double auxKgNecesarios = kgNecesarios;

					for (Entrada_Materia_Prima entrada_materia_prima : fabricacionesProducto) {

						if (auxKgNecesarios > 0.0) {

							auxKgNecesarios = procesar(connection, resultadosAgrupados, amasijo, formulaArticulo,
									auxKgNecesarios, entrada_materia_prima);
						}

					}

				}

			}

			if (CollectionUtils.isNotEmpty(resultadosAgrupados)) {

				insertarResultadosAgrupados(connection, new ResultadoAgrupado(amasijo.getLote().toString(),
						amasijo.getDescripcion(), amasijo.getCantidad_amasijo(), amasijo.getPeso_total(), 
						resultadosAgrupados));
			}

		}

	}

	private static Double procesar(Connection connection, List<String> resultadosAgrupados, Amasijo envasado,
			List<Formula> formulaArticulo, Double auxKgNecesarios, Entrada_Materia_Prima entrada_materia_prima) throws SQLException {
		if (entrada_materia_prima.getCantidad_disponible() >= auxKgNecesarios) {// Si cantidadDisponible es mayot que kgnecesarios

			rellenarListaOperacionEnvase(resultadosAgrupados, formulaArticulo, auxKgNecesarios, entrada_materia_prima);

			auxKgNecesarios = procesarResultadocantidadDisponibleMayorKGNecesarios(connection, auxKgNecesarios,
					entrada_materia_prima, envasado);
			terminarProductoEnvase(connection, envasado, auxKgNecesarios);
		}

		// Si cantidadDisponible es menor que kg necesarios
		if (entrada_materia_prima.getCantidad_disponible() < auxKgNecesarios) {

			rellenarListaOperacionEnvase(resultadosAgrupados, formulaArticulo, entrada_materia_prima.getCantidad_disponible(),
					entrada_materia_prima);

			auxKgNecesarios = procesarResultadocantidadDisponibleMenorKGNecesarios(connection, auxKgNecesarios,
					entrada_materia_prima, envasado);
			terminarProductoEnvase(connection, envasado, auxKgNecesarios);
		}
		return auxKgNecesarios;
	}

	private static void terminarProductoEnvase(Connection connection, Amasijo amasijo, Double auxKgNecesarios)
			throws SQLException {
		if (auxKgNecesarios == 0.0) {
			// poner a 1 el finalizado del envasado
			atualizarFinalizadoEnvasado(connection, amasijo.getId_amasijo());
		}
	}

	private static void rellenarListaOperacionEnvase(List<String> resultadosAgrupados, List<Formula> formulaArticulo,
			Double auxKgNecesarios, Entrada_Materia_Prima entrada_materia_prima) {

		auxKgNecesarios = (Math.round(auxKgNecesarios * 100.0) / 100.0);
		if (formulaArticulo.size() > 1) {
			resultadosAgrupados.add(auxKgNecesarios + "(" + entrada_materia_prima.getLote_fabricacion() + "-"
					+ entrada_materia_prima.getReferencia_producto() + ")");
		} else {

			resultadosAgrupados.add(auxKgNecesarios + "(" + entrada_materia_prima.getLote_fabricacion() + ")");
		}
	}

	private static void insertarResultadosAgrupados(Connection connection, ResultadoAgrupado resultadoAgrupado)
			throws SQLException {

		PreparedStatement prepared = connection.prepareStatement(INSERT_RESULTADOS_AGRUPADOS);

		String resultado = resultadoAgrupado.getResultados_agrupados().stream().filter(StringUtils::isNotEmpty)
				.collect(Collectors.joining(", "));

		prepared.setString(1, resultadoAgrupado.getLote());
		prepared.setString(2, resultadoAgrupado.getDescripcion_producto());
		prepared.setDouble(3, resultadoAgrupado.getPesoTotal());
		prepared.setString(4, resultado);

		prepared.executeUpdate();

	}

	private static Double procesarResultadocantidadDisponibleMenorKGNecesarios(Connection connection,
			Double kgNecesarios, Entrada_Materia_Prima entrada_materia_prima, Amasijo amasijo) throws SQLException {

		insertarResultado(connection, amasijo.getReferencia_amasijo(), amasijo.getDescripcion(),
				amasijo.getLote(), entrada_materia_prima.getLote_fabricacion(), entrada_materia_prima.getCantidad_disponible(),
				entrada_materia_prima.getReferencia_producto());

		// actualizar entrada_materia_prima
		actualizarFabricacion(connection, entrada_materia_prima.getId_fabricacion(), 0.0);

		return kgNecesarios - entrada_materia_prima.getCantidad_disponible();
	}

	private static Double procesarResultadocantidadDisponibleMayorKGNecesarios(Connection connection,
			Double kgNecesarios, Entrada_Materia_Prima entrada_materia_prima, Amasijo amasijo) throws SQLException {

		double cantidadDisponibleActualizada = entrada_materia_prima.getCantidad_disponible() - kgNecesarios;
		// Insertar en resultados;
		insertarResultado(connection, amasijo.getReferencia_amasijo(), amasijo.getDescripcion(),
				amasijo.getLote(), entrada_materia_prima.getLote_fabricacion(), kgNecesarios,
				entrada_materia_prima.getReferencia_producto());

		// actualizar entrada_materia_prima
		actualizarFabricacion(connection, entrada_materia_prima.getId_fabricacion(), cantidadDisponibleActualizada);

		return 0.0;
	}

	private static void atualizarFinalizadoEnvasado(Connection connection, Long id_envasado) throws SQLException {

		PreparedStatement prepared = connection.prepareStatement(UPDATE_ENVASADO_FINALIZADO);

		prepared.setLong(1, id_envasado);

		prepared.executeUpdate();
	}

	private static void actualizarFabricacion(Connection connection, Long id_fabricacion,
			double cantidadDisponibleActualizada) throws SQLException {
		PreparedStatement prepared = connection.prepareStatement(UPDATE_FABIRCACION);

		prepared.setDouble(1, cantidadDisponibleActualizada);
		prepared.setLong(2, id_fabricacion);

		prepared.executeUpdate();

	}

	private static void insertarResultado(Connection connection, String referencia_articulo, String descripcion,
			Integer lote_envasado, Integer lote_fabricacion, Double kgNecesarios, String refProducto)
			throws SQLException {

		PreparedStatement prepared = connection.prepareStatement(INSERT_RESULTADO);

		prepared.setString(1, referencia_articulo);
		prepared.setString(2, descripcion);
		prepared.setInt(3, lote_envasado);
		prepared.setInt(4, lote_fabricacion);
		prepared.setDouble(5, kgNecesarios);
		prepared.setString(6, refProducto);

		prepared.executeUpdate();
	}

	private static List<Entrada_Materia_Prima> devuelveFabricaciones(Connection connection, String referencia_producto)
			throws SQLException {

		PreparedStatement prepared = connection
				.prepareStatement(SELECT_FABRICACIONESBYREF_PRODUCTO_AND_CANTIDAD_DISPONIBLE);
		prepared.setString(1, referencia_producto);
		ResultSet rs = prepared.executeQuery();
		List<Entrada_Materia_Prima> lista_fabricacion = new ArrayList<Entrada_Materia_Prima>();

		while (rs.next()) {
			lista_fabricacion.add(new Entrada_Materia_Prima(rs.getLong(FABRICACION_ID), rs.getInt(FABRICACION_LOTE_PRODUCTO),
					rs.getString(FABRICACION_REF_PRODUCTO), rs.getInt(FABRICACION_CANTIDAD_FABRICADA),
					rs.getDouble(FABRICACION_CANTIDAD_DISPONIBLE)));

		}

		return lista_fabricacion;
	}

	private static List<Formula> buscarFormulaPorArticulo(Connection connection, String ref_amasijo)
			throws SQLException {
		PreparedStatement prepared = connection.prepareStatement(SELECT_FORMULA_BY_REF_ARTICULO);

		prepared.setString(1, ref_amasijo);
		ResultSet rs = prepared.executeQuery();
		List<Formula> formulaProducto = new ArrayList<Formula>();
		while (rs.next()) {
			formulaProducto.add(new Formula(rs.getNString(FORMULA_REF_AMASIJO),
					rs.getNString(FORMULA_REF_MATERIA_PRIMA), rs.getDouble(FORMULA_KILOS)));
		}
		rs.close();
		prepared.close();

		return formulaProducto;
	}

	private static List<Amasijo> getListaAmasijosSinTerminar(Connection connection) throws SQLException {
		List<Amasijo> envasados = new ArrayList<Amasijo>();
		Statement s = connection.createStatement();
		ResultSet rs = s.executeQuery(SELECT_AMASIJOS_SIN_FINALIZAR);

		while (rs.next()) {

			envasados.add(new Amasijo(rs.getLong(AMASIJOS_ID), rs.getInt(AMASIJOS_LOTE_AMASIJO),
					rs.getString(AMASIJOS_REF_AMASIJO), rs.getString(AMASIJOS_DESCRIPCION), rs.getDouble(AMASIJOS_PESO),
					rs.getInt(AMASIJOS_CANTIDAD_AMASIJO), rs.getDouble(AMASIJOS_PESO_TOTAL),
					rs.getBoolean(AMASIJOS_FECHA_FINALIZADO)));
		}

		rs.close();
		s.close();

		return envasados;

	}
}
