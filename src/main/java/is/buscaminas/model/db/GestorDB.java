package is.buscaminas.model.db;


import java.sql.*;


public class GestorDB
{
	//IMPORTANTE!!! ESTA CLASE DEVUELVE TODAS LAS EXCEPCIONES. LAS EXCEPCIONES HAN DE TRATARSE EN LA CLASE INVOCADORA
	
	// Atributos
	private static GestorDB mGestorDB;
	
	
	// Constructora
	private GestorDB(){}
	
	
	// Patrón singleton
	public static GestorDB getGestorDB()
	{
		if (mGestorDB == null) mGestorDB = new GestorDB();
		return mGestorDB;
	}
	
	
	// Métodos
	
	private Connection connect() throws SQLException
	{
		// SQLite connection string
		return DriverManager.getConnection("jdbc:sqlite:src/main/resources/is/buscaminas/DB/dbBuscaminas.db");
	}
	
	public void execSQL(String pSentenciaSQL) throws SQLException
	{
		// No devuelve un resultado.
		// Empleado para INSERT/DELETE/UPDATE
		
		try (Connection dbCon = this.connect(); PreparedStatement pstmt = dbCon.prepareStatement(pSentenciaSQL)){
			// Se ejecuta el INSERT/DELETE/UPDATE
			pstmt.executeUpdate();
		}
	}
	
	public ResultadoSQL execSELECT(String pSentenciaSQL) throws SQLException
	{
		// Devuelve un resultado del tipo ResultadoSQL.
		// Empleado para SELECT
		Connection dbCon = connect();
		Statement stmt = dbCon.createStatement();
		ResultSet resultado = stmt.executeQuery(pSentenciaSQL);
		
		return new ResultadoSQL(dbCon, stmt, resultado);
	}
}
