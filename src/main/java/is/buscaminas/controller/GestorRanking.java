package is.buscaminas.controller;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import is.buscaminas.model.Usuario;
import is.buscaminas.model.db.GestorDB;
import is.buscaminas.model.db.ResultadoSQL;

import java.sql.SQLException;

public class GestorRanking {

    private static GestorRanking mGestorRanking;

    private GestorRanking(){}

    public static GestorRanking getGestorRanging(){
        if (mGestorRanking == null) {
            mGestorRanking = new GestorRanking();
        }
        return mGestorRanking;
    }

    public String obtenerRankingGlobal() throws SQLException {

        JsonObject ranking = new JsonObject();
        JsonObject fila = new JsonObject();
        JsonArray filas = new JsonArray();
        ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("Select nickname,nivel,puntuacion from Ranking order by puntuacion");
        while (resultado.next()) {
            String nickname = resultado.getString("nickname");
            int nivel = resultado.getInt("nivel");
            int puntuacion = resultado.getInt("puntuacion");
            fila.put("nickname",nickname);
            fila.put("nivel",nivel);
            fila.put("puntuacion",puntuacion);
            filas.add(fila);
        }
        ranking.put("ranking",filas);
        return ranking.toJson();
    }

    public String obtenerRankingPersonal() throws SQLException {

        JsonObject ranking = new JsonObject();
        JsonObject fila = new JsonObject();
        JsonArray filas = new JsonArray();
        Usuario usuarioActual = Usuario.getUsuario();
        String email = usuarioActual.getEmail();
        ResultadoSQL resultado = GestorDB.getGestorDB().execSELECT("Select nickname,nivel,puntuacion from Ranking where email = " + email + " order by puntuacion");
        while (resultado.next()) {
            String nickname = resultado.getString("nickname");
            int nivel = resultado.getInt("nivel");
            int puntuacion = resultado.getInt("puntuacion");
            fila.put("nickname",nickname);
            fila.put("nivel",nivel);
            fila.put("puntuacion",puntuacion);
            filas.add(fila);
        }
        ranking.put("ranking",filas);
        return ranking.toJson();
    }

    public String clasificarPorNivel() throws SQLException {

        ResultadoSQL niveles = GestorDB.getGestorDB().execSELECT("SELECT nivel FROM Nivel");
        ResultadoSQL ranking = GestorDB.getGestorDB().execSELECT("SELECT nivel,nickname,puntuacion FROM Ranking ORDER BY nivel ASC, puntuacion DESC");
        JsonObject rankingNiveles = new JsonObject();
        JsonArray arrayFinal = new JsonArray();
        JsonArray arrayPuntuaciones = new JsonArray();
        JsonObject nickYPuntuacion = new JsonObject();
        JsonObject nivelPuntuaciones = new JsonObject();
        while (niveles.next()) {
            int nivel = niveles.getInt("nivel");
            while (ranking.next() && nivel == ranking.getInt("nivel")) {
                String nickname = ranking.getString("nickname");
                int puntuacion = ranking.getInt("puntuacion");
                nickYPuntuacion.put("nickname",nickname);
                nickYPuntuacion.put("puntuacion",puntuacion);
                arrayPuntuaciones.add(nickYPuntuacion);
            }
            nivelPuntuaciones.put("nivel",nivel);
            nivelPuntuaciones.put("puntuaciones",arrayPuntuaciones);
            arrayFinal.add(nivelPuntuaciones);
        }
        rankingNiveles.put("rankingNiveles",arrayFinal);
        return rankingNiveles.toJson();
    }

    public void registrarPuntuacion(int pPuntuacion, int pNivel) throws SQLException {
        Usuario usuarioActual = Usuario.getUsuario();
        String nickname = usuarioActual.getNickname();
        String email = usuarioActual.getEmail();
        GestorDB.getGestorDB().execSQL("INSERT INTO Ranking (nivel, email, puntuacion, nickname) VALUES (" +pNivel+ "," +email+ "," +pPuntuacion+ "," +nickname+ ")");
    }
}

