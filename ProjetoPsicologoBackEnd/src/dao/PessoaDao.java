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

	
	//       MANIPULA��O DE USUARIOS COMUNS//
	
	
					//CADASTRA//
	public boolean cadastraPessoa(Pessoa p) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
		
		String sql = "INSERT INTO PESSOA(cod_pessoa,nickname,senha,tipoperfil,verificado,ident,login,anonimo,registro,primeiroAcesso)values(0,?,?,?,?,?,?,?,now(),true)";
		
		con = ConnectionDB.getConnection();
		
		//criptografia ???
		String senha = p.getSenha();
		
		
				//CRIPTOGRAFIA DO LOGIN 
		
		 
         // Create key and cipher
         
         // encrypt the text

    
         /* decrypt the text
         cipher.init(Cipher.DECRYPT_MODE, aesKey);
         String decrypted = new String(cipher.doFinal(encrypted));
         System.err.println(decrypted);
		*/
         
         //GERA��O DE HASH DA SENHA
		
         MessageDigest algorithm = MessageDigest.getInstance("MD5");
		byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
		
		
		//prepara o sql
		ps = con.prepareStatement(sql);
		
		//filtragem de possiveis erros devido a usuarios anonimos//

		
		ps.setString(1, p.getNickname());
		ps.setBytes(2, messageDigestSenha);
		ps.setInt(3, p.getTipoPerf());
		if(p.isVerificado()) {
		ps.setBoolean(4, true);
		}else {
			ps.setBoolean(4, false);
		}
		ps.setString(5, p.getIdentidade());
		ps.setString(6,p.getLogin());	
		if(p.isAnonimo()) {
		ps.setBoolean(7, true);
		}else {
			ps.setBoolean(7, false);
		}
		
		
		
		
		return ps.executeUpdate() > 0;
	}
	
				//LISTA Pessoas
	public List<Pessoa> listarTodosUsuarios() throws SQLException{
		
		String sql = "SELECT * FROM pessoa WHERE tipoperfil = '1' ";
		
	con = ConnectionDB.getConnection();
		
		ps = con.prepareStatement(sql);
		
		List<Pessoa> pessoas = new ArrayList<>();
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			Pessoa p = new Pessoa();
			p.setCod_pessoa(rs.getInt("cod_pessoa"));
			p.setIdentidade(rs.getString("ident"));
			p.setLogin(rs.getString("login"));
			p.setNickname(rs.getString("nickname"));
			p.setSenha(rs.getString("senha"));
			p.setVerificado(rs.getBoolean("verificado"));
			p.setTipoPerf(rs.getInt("tipoperfil"));
			
			pessoas.add(p);
		}
		
		return pessoas;
	}		
				
				
	
					//LOGA//
	public Boolean tentaLogin(Pessoa p) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		//STRING DO SQL
		String sql = "SELECT * FROM pessoa WHERE login = ? AND senha = ?   ";
		
		
			//Declara cifra baseado na chave global
		
		
		  
		  //Encripta o login pra comparar com o banco
		
		//gera o hash da senha
		String senha = p.getSenha();
		MessageDigest algorithm = MessageDigest.getInstance("MD5");	
		byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
		
		
		
		
		
			//decripta o login
		//cipher.init(Cipher.DECRYPT_MODE, aesKey);
       // String decrypted = new String(cipher.doFinal(encrypted));
       // System.err.println(decrypted);
	         
        
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		ps.setString(1,p.getLogin());
		ps.setBytes(2, messageDigestSenha);

		
		ResultSet rs = ps.executeQuery();
			
		return rs.next();
		
	}
	

	
	0
				//ELEMENTO EXCLUSIVO DE USUARIOS PSICOLOGOS,O USUARIO DEVE ESTAR VERIFICADO ANTES DE PODER
				//AGIR COMO PSICOLOGO NO APP
	public boolean verificaPessoa(int i) throws SQLException {
		
		String sql = " UPDATE pessoa SET verificado=true WHERE cod_pessoa = ?";
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		
		ps.setInt(1, i);

		
		return ps.executeUpdate() > 0;
	}
	
		public String pegaNickname(String login,String senha ) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
			
			
			
			String sql = "SELECT nickname FROM pessoa WHERE login = ? AND senha = ? ";
			
			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			
			  
			  
			  //Pega o login e senha

			
			
			//gera o hash da senha
			
			MessageDigest algorithm = MessageDigest.getInstance("MD5");	
			byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
			
			
			ps.setString(1,login);
			ps.setBytes(2, messageDigestSenha);
			
			

			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return rs.getString(1);
			
		}
	/*
		public boolean pegaAcesso(String login,String senha) throws NoSuchAlgorithmException, SQLException, UnsupportedEncodingException {
			
			String sql = "SELECT primeiroAcesso FROM pessoa WHERE login = ? AND senha = ? ";
			
			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			
			  
			  
			  //Pega o login e senha

			
			
			//gera o hash da senha
			
			MessageDigest algorithm = MessageDigest.getInstance("MD5");	
			byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
			
			
			ps.setString(1,login);
			ps.setBytes(2, messageDigestSenha);
			
			

			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return rs.getBoolean(1);
			
			
			
		}
		*/
		
		
		//VERIFICA SE � O PRIMEIRO ACESSO E RETORNA
		
		
	public boolean primeiroAcesso(String login,String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException {
		
	String sql = "SELECT primeiroAcesso FROM pessoa WHERE login = ? and senha = ?";
	
	con = ConnectionDB.getConnection();
	ps = con.prepareStatement(sql);
	

	
	//gera o hash da senha
	
	MessageDigest algorithm = MessageDigest.getInstance("MD5");	
	byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
	
	
	ps.setString(1,login);
	ps.setBytes(2,messageDigestSenha);
	
	
	

	ResultSet rs = ps.executeQuery();
	
	rs.next();
	
	return rs.getBoolean(1);
	
		
	}
			
	
		//EDITA PERFIL
	
	public int pegaChave(String login,String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException {
		
		String sql = "SELECT cod_pessoa FROM pessoa WHERE login= ? and senha = ?";
		
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		

		
		//gera o hash da senha
		
		MessageDigest algorithm = MessageDigest.getInstance("MD5");	
		byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
		
		
		ps.setString(1,login);
		ps.setBytes(2,messageDigestSenha);
		
		
		

		ResultSet rs = ps.executeQuery();
		
		rs.next();
		
		return rs.getInt(1);
		
		
	}
	
	public boolean editaPerfil(String edit,int cod_pessoa,String dado) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
		
		String sql = "";
		switch (edit){
		
		case "nickname":
		sql = "UPDATE nickname SET = ? WHERE cod_pessoa = ? ";

		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		
		
		ps.setString(1,dado);
		ps.setInt(2, cod_pessoa);
	
		
			break;
			
		case "senha":
			sql = "UPDATE senha SET = ?  WHERE cod_pessoa = ? ";
			MessageDigest algorithm = MessageDigest.getInstance("MD5");	
			byte messageDigestSenha[] = algorithm.digest(dado.getBytes("UTF-8"));
			

			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setBytes(1,messageDigestSenha);
			ps.setInt(2,cod_pessoa);
			
			break;
			
		case "ident":
			sql = "UPDATE ident SET = ? WHERE cod_pessoa = ? ";
	
			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setString(1,dado);
			ps.setInt(2, cod_pessoa);
			
		break;
		
		case "tipoPerfil":
			sql = "UPDATE tipoPerfil SET = ? WHERE cod_pessoa = ? ";
			

			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			ps.setBoolean(1, Boolean.parseBoolean(dado));
			ps.setInt(2, cod_pessoa);
			
			break;
		
		}
	
		return ps.executeUpdate() > 0;
		
	}
		//LISTA N�O VERIFICADOS
		
		public List<Pessoa> listarNaoVerificados() throws SQLException{
			
			String sql = "SELECT * FROM pessoa WHERE tipoPerfil = '1' and verificado = '0' ";
			
		con = ConnectionDB.getConnection();
			
			ps = con.prepareStatement(sql);
			
			List<Pessoa> pessoas = new ArrayList<>();
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Pessoa p = new Pessoa();
				p.setCod_pessoa(rs.getInt("cod_pessoa"));
				p.setIdentidade(rs.getString("ident"));
				p.setLogin(rs.getString("login"));
				p.setNickname(rs.getString("nickname"));
				p.setSenha(rs.getString("senha"));
				p.setVerificado(rs.getBoolean("verificado"));
				p.setTipoPerf(rs.getInt("tipoperfil"));
				p.setRegistro(rs.getDate("registro"));
				
				pessoas.add(p);
			}
			
			return pessoas;
		}		
		
		
		
	
		
		
	
		
	
		//FIM DA MANIPULA��O DE USUARIOS COMUNS
	
	
	
			//MANIPULA��O DE USUARIOS ADMNISTRADORES//
	
				
				//CADASTRA ADMIN//
public boolean cadastraAdmin(Admin a) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		
		String sql = "INSERT INTO PESSOA(cod_pessoa,login,senha)values(0,?,?)";
		
		con = ConnectionDB.getConnection();
		
		//criptografia ???
			String senha = a.getSenha();
		
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
		
		
		//prepara o sql
		ps = con.prepareStatement(sql);
		ps.setString(1, a.getLogin());		
		ps.setBytes(2, messageDigestSenha);
		
		return ps.executeUpdate() > 0;
	}
	
	
				//LOGA ADMIN//
	public boolean tentaLoginAdmin(Admin a) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		
		String sql = "SELECT * FROM admins ";
		sql+= "WHERE login =  ? ";
		sql+= "AND senha = ?   ";
		

		String senha = a.getSenha();
		
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			
			byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
			
		
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		ps.setString(1, a.getLogin());
		ps.setBytes(2, messageDigestSenha);
		ps.executeQuery();
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			return true;
		}else {
			return false;
		}
	}
	
	
	}
		
		

	


