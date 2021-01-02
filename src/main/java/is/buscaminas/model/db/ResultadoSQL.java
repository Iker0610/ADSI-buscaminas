package is.buscaminas.model.db;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ResultadoSQL
{
	// Atributos
	
	private final Connection dbCon;
	private final Statement stmt;
	private final ResultSet resultado;
	
	
	// Constructora
	
	protected ResultadoSQL(Connection pDBCon, Statement pStmt, ResultSet pResultado)
	{
		dbCon = pDBCon;
		stmt = pStmt;
		resultado = pResultado;
	}
	
	
	// Métodos
	public boolean next() throws SQLException
	{
		return resultado.next();
	}
	
	public void close() throws SQLException
	{
		resultado.close();
		stmt.close();
		dbCon.close();
	}
	
	public int getInt(String pNombreCampo) throws SQLException
	{
		return resultado.getInt(pNombreCampo);
	}
	
	public String getString(String pNombreCampo) throws SQLException
	{
		return resultado.getString(pNombreCampo);
	}
	
	public Boolean getBoolean(String pNombreCampo) throws SQLException
	{
		return resultado.getInt(pNombreCampo) > 0;
	}
}
