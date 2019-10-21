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
	private  String key = "bananabananamaca"; // 128 bit key
	private Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
	
	//       MANIPULA플O DE USUARIOS COMUNS//
	
	
					//CADASTRA//
	public boolean cadastraPessoa(Pessoa p) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
		
		String sql = "INSERT INTO PESSOA(cod_pessoa,nickname,senha,tipoperfil,verificado,ident,login,anonimo,logado)values(0,?,?,?,?,?,?,?,?)";
		
		con = ConnectionDB.getConnection();
		
		//criptografia ???
		String login = p.getLogin();
		String senha = p.getSenha();
		
		
				//CRIPTOGRAFIA DO LOGIN 
		
		 
         // Create key and cipher
		String key = "bananabananamaca"; // 128 bit key
		Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
         Cipher cipher = Cipher.getInstance("AES");
         
         // encrypt the text
         cipher.init(Cipher.ENCRYPT_MODE, aesKey);
         byte[] encrypted = cipher.doFinal(login.getBytes());
         System.err.println(new String(encrypted));
         
         /* decrypt the text
         cipher.init(Cipher.DECRYPT_MODE, aesKey);
         String decrypted = new String(cipher.doFinal(encrypted));
         System.err.println(decrypted);
		*/
         
         //GERA플O DE HASH DA SENHA
		
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
		ps.setBytes(6,encrypted);	
		if(p.isAnonimo()) {
		ps.setBoolean(7, true);
		}else {
			ps.setBoolean(7, false);
		}
		ps.setBoolean(8, false);
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
		String sql = "SELECT * FROM pessoa ";
		sql+= "WHERE login =  ?  ";
		sql+= "AND senha = ?   ";
		
		
			//Declara cifra baseado na chave global
		
		  Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);  
		  
		  //Encripta o login pra comparar com o banco
		String login = p.getLogin();
		byte[] encrypted = cipher.doFinal(login.getBytes());	
		
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
		ps.setBytes(1,encrypted);
		ps.setBytes(2, messageDigestSenha);
		ps.executeQuery();
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			return true;
		}else {
			return false;
		}
	}
	

	
	
				//ELEMENTO EXCLUSIVO DE USUARIOS PSICOLOGOS,O USUARIO DEVE ESTAR VERIFICADO ANTES DE PODER
				//AGIR COMO PSICOLOGO NO APP
	public boolean verificaPessoa(int i) throws SQLException {
		
		String sql = " UPDATE pessoa SET verificado='true' WHERE cod_user = ?";
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		
		ps.setInt(1, i);

		
		return ps.executeUpdate() > 0;
	}
	
		public String pegaNickname(String login,String senha ) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
			
			
			
			String sql = "SELECT nickname FROM pessoa WHERE login = ? AND senha = ? ";
			
			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			
			Cipher cipher =  Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);	  
			  
			  //Pega o login e senha

			byte[] encrypted = cipher.doFinal(login.getBytes());	
			
			//gera o hash da senha
			
			MessageDigest algorithm = MessageDigest.getInstance("MD5");	
			byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
			
			
			ps.setBytes(1,encrypted);
			ps.setBytes(2, messageDigestSenha);
			
			

			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return rs.getString(1);
			
		}
	
			
		public void logado(Pessoa p) throws SQLException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
			String sql = "UPDATE pessoa SET logado='true' WHERE login = ? AND senha = ?";
			
			con = ConnectionDB.getConnection();
			ps = con.prepareStatement(sql);
			
			String login = p.getLogin();
			String senha = p.getSenha();

			Cipher cipher =  Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);	  
			  
			  //Pega o login e senha

			byte[] encrypted = cipher.doFinal(login.getBytes());	
			
			//gera o hash da senha
			
			MessageDigest algorithm = MessageDigest.getInstance("MD5");	
			byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
			
			
			ps.setBytes(1,encrypted);
			ps.setBytes(2, messageDigestSenha);
			
			ps.executeQuery();
		
		}
		
	
		//FIM DA MANIPULA플O DE USUARIOS COMUNS
	
	
	
			//MANIPULA플O DE USUARIOS ADMNISTRADORES//
	
				
				//CADASTRA ADMIN//
public boolean cadastraAdmin(Admin a) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		
		String sql = "INSERT INTO PESSOA(cod_pessoa,login,senha)values(0,?,?)";
		
		con = ConnectionDB.getConnection();
		
		//criptografia ???
		String login = a.getLogin();
		String senha = a.getSenha();
		
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		byte messageDigestLogin[] = algorithm.digest(login.getBytes("UTF-8"));
		byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
		
		
		//prepara o sql
		ps = con.prepareStatement(sql);
		ps.setBytes(1, messageDigestLogin);		
		ps.setBytes(2, messageDigestSenha);
		
		return ps.executeUpdate() > 0;
	}
	
	
				//LOGA ADMIN//
	public boolean tentaLoginAdmin(Admin a) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		
		String sql = "SELECT * FROM admins ";
		sql+= "WHERE login =  ?  ";
		sql+= "AND senha = ?   ";
		
		String login = a.getLogin();
		String senha = a.getSenha();
		
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			byte messageDigestLogin[] = algorithm.digest(login.getBytes("UTF-8"));
			byte messageDigestSenha[] = algorithm.digest(senha.getBytes("UTF-8"));
			
		
		con = ConnectionDB.getConnection();
		ps = con.prepareStatement(sql);
		ps.setBytes(1, messageDigestLogin);
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
		
		

	


