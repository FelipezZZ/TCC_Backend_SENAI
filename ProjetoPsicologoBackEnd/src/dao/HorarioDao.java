package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.protocol.Resultset;

public class HorarioDao {

	private Connection con;
	private PreparedStatement ps;

	public List<Integer> pesquisaHorario(String dia, String horario) throws SQLException {

		List<Integer> codEstagiarios = new ArrayList<Integer>();
		con = ConnectionDB.getConnection();
		String sql = "";
		switch (dia) {

		case "dom":
			sql = "SELECT cod_estagiario FROM horarios WHERE dom = ?";
			break;
		case "seg":
			sql = "SELECT cod_estagiario FROM horarios WHERE seg = ?";
			break;
		case "ter":
			sql = "SELECT cod_estagiario FROM horarios WHERE ter = ?";
			break;
		case "qua":
			sql = "SELECT cod_estagiario FROM horarios WHERE qua = ?";
			break;
		case "qui":
			sql = "SELECT cod_estagiario FROM horarios WHERE qui = ?";
			break;
		case "sex":
			sql = "SELECT cod_estagiario FROM horarios WHERE sex = ?";
			break;
		case "sab":
			sql = "SELECT cod_estagiario FROM horarios WHERE sab = ?";
			break;

		}

		ps = con.prepareStatement(sql);
		ps.setString(1, horario);

		System.out.println(ps);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			codEstagiarios.add(rs.getInt(1));

			return codEstagiarios;

		}

	}
}
