package dao;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.sun.xml.internal.ws.message.EmptyMessageImpl;

import vo.Admin;
import vo.Pessoa;

public class PessoaDao {
	
	private Connection con;
	private PreparedStatement ps;
	
	//CADASTRA//
	public int cadastraPessoa(Pessoa p) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
		
		String sql = "INSERT INTO PESSOA(cod_pessoa,universidade,RA,nome,email,senha,tipoperfil,verificado,dataCadastro,primeiroAcesso,cadastroFB)"
				+ "values(0,?,?,?,?,?,?,false,now(),true,?)";

		con = ConnectionDB.getConnection();
		
		//criptografia ???
		String senha = p.getSenha();
		
		//CRIPTOGRAFIA DO LOGIN 
		
        // Create key and cipher/
         
        // encrypt the text

    
        /* decrypt the text
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted = new String(cipher.doFinal(encrypted));
        System.err.println(decrypted);
		*/
         
        //GERAÇÃO DE HASH DA SENHA
		
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
		byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
		
		
		//prepara o sql
		ps = con.prepareStatement(sql);
		
		//filtragem de possiveis erros devido a usuarios anonimos//

		ps.setString(1, p.getUniversidade());
		ps.setString(2, p.getRA());
		ps.setString(3, p.getNome());
		ps.setString(4, p.getEmail());
		ps.setBytes(5, messageDigestSenha);
		ps.setInt(6, p.getTipoPerf());
		ps.setBoolean(7, p.isCadastroFb());
		ps.executeUpdate();
		
		return pegaChave(p.getEmail()); 
	}
	
	//PEGA CHAVE PRIMAR
	public int pegaChave(String login) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException {
		
		String sql = "SELECT cod_pessoa FROM pessoa WHERE email = ?";
		
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		
		ps.setString(1,login);
		
		ResultSet rs = ps.executeQuery();
		
		rs.next();
		
		return rs.getInt(1);
		
	}
	
	public int verificarTipoUsuario(String cod_pessoa) throws SQLException {
		
		String sql = "SELECT tipoperfil FROM pessoa WHERE cod_pessoa = ?";
		
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		
		ps.setString(1,cod_pessoa);
		
		ResultSet rs = ps.executeQuery();
		
		rs.next();
		
		return rs.getInt(1);
	}
	
	public boolean verificarPrimeiroAcesso(String cod_pessoa) throws SQLException {
		
		String sql = "SELECT primeiroAcesso FROM pessoa WHERE cod_pessoa = ?";
		
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		
		ps.setString(1,cod_pessoa);
		
		ResultSet rs = ps.executeQuery();
		
		rs.next();
		
		return rs.getBoolean(1);
	}
	
	public void salvarAnamnese(String cod_pessoa, String a, String d, String s) throws SQLException {
		
		String sql = "INSERT INTO anamnese(cod_pessoa,a,d,s,dataAnamnese) values(?,?,?,?,now())";
		
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		
		ps.setString(1,cod_pessoa);
		ps.setString(2, a);
		ps.setString(3, d);
		ps.setString(4, s);
		
		ps.executeUpdate();
	}
	
	public void mudarAcesso(String cod_pessoa) throws SQLException {
		
		String sql = " UPDATE pessoa SET primeiroAcesso = false WHERE cod_pessoa = ?";
		
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		
		ps.setString(1,cod_pessoa);
		
		ps.executeUpdate();
		
	}

	public int verificaCadastroSQL(String email, String senha) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		
		String sql = " SELECT cod_pessoa FROM pessoa WHERE email = ? and senha = ? and cadastroFB = false ";
		
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
		byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
		
		System.out.println(messageDigestSenha);
		
		ps.setString(1, email);
		ps.setBytes(2, messageDigestSenha);
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			return rs.getInt(1);
		}else {
			return 0;
		}
	}
	
	public String pegarNome(String cod_pessoa) throws SQLException {
		
		String sql = " SELECT nome FROM pessoa WHERE cod_pessoa = ? ";
		
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		
		ps.setString(1, cod_pessoa);
		
		ResultSet rs = ps.executeQuery();
		
		rs.next();
		
		return rs.getString(1);
		
	}
	
	public void mudarCadastroFB(String cod_pessoa) throws SQLException {
		
		String sql = " UPDATE pessoa SET cadastroFB = true WHERE cod_pessoa = ?";
		
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		
		ps.setString(1,cod_pessoa);
		
		ps.executeUpdate();
		
	}
	
}
		
		

	


