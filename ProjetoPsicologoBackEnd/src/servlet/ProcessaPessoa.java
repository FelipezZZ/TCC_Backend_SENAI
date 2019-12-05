package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import dao.PessoaDao;
import vo.Admin;
import vo.Pessoa;

@WebServlet("/ProcessaPessoa")
public class ProcessaPessoa extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProcessaPessoa() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("conectou");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			//FUNCIONARIO COD = 1
			//PACIENTE COD = 2 
			//PELO AMOR DE JEOVÁ,ZEUS E VISHNU NÃO ESQUECER DISSO
		int v = 0;
		System.out.println("conectou no processa");
		
		PrintWriter out = response.getWriter();
		JSONObject obj = new JSONObject();
		PessoaDao pDao = new PessoaDao();
			
		//strings da preguiça	
		String sucesso = "sucesso";
		String falha = "falha";
		String erroSQL = "erro no sql";
		
		
		String acao = request.getParameter("acao");
		
		//Gerenciamento de pessoas
		
		
		//CADASTRAR PESSOA
		if (acao.equals("cadastrarPessoa")) {
			System.out.println("To no cadastra");
			
			Pessoa p = new Pessoa();
			
			String universidade = request.getParameter("universidade");
			String RA = request.getParameter("RA");
			
			
			String nome = request.getParameter("nome");
			String email = request.getParameter("email");
			String senha = request.getParameter("senha");
			int tipoPerf = Integer.valueOf(request.getParameter("tipoPerf"));
			String ScadastroFB = request.getParameter("cadastroFB");
		
			
			System.out.println(universidade);
			System.out.println(RA);
			
			System.out.println(nome);
			System.out.println(email);
			System.out.println(senha);
			System.out.println(tipoPerf);
			System.out.println("cadastroFB " + ScadastroFB);
			
			if(tipoPerf == 2) {
				p.setUniversidade(universidade);
				p.setRA(RA);
			}
			
			p.setNome(nome);
			p.setEmail(email);
			p.setSenha(senha);
			p.setTipoPerf(tipoPerf);
			p.setCadastroFb(Boolean.parseBoolean(ScadastroFB));
			
			try {
				int cod_pessoa = pDao.cadastraPessoa(p);
				out.print(cod_pessoa);
			} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException
					| NoSuchPaddingException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(acao.equals("verificarTipoUsuario")) {
			System.out.println("to no verificarTipoUsuario");
			
			String cod_pessoa = request.getParameter("codPessoa");
			
			try {
				int tipousuario = pDao.verificarTipoUsuario(cod_pessoa);
				out.print(tipousuario);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(acao.equals("verificarPrimeiroAcesso")) {
			System.out.println("to no verificarPrimeiroAcesso");
			
			String cod_pessoa = request.getParameter("codPessoa");
			
			try {
				if(pDao.verificarPrimeiroAcesso(cod_pessoa)) {
					out.print(true);
				}else {
					out.print(false);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		if(acao.equals("salvarAnamnese")) {
			
			System.out.println("to no salvarAnamnese");
			
			String cod_pessoa = request.getParameter("codPessoa");
			String a = request.getParameter("a");
			String d = request.getParameter("d");
			String s = request.getParameter("s");
			
			System.out.println(cod_pessoa);
			
			try {
				pDao.salvarAnamnese(cod_pessoa, a, d, s);
				out.print("success");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		if(acao.equals("mudarAcesso")) {
			
			System.out.println("to no mudarAcesso");
			
			String cod_pessoa = request.getParameter("codPessoa");
			
			try {
				pDao.mudarAcesso(cod_pessoa);
				out.print("success");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		if(acao.equals("verificaCadastroSQL")) {
			
			System.out.println("to no verificaCadastroSQL");
			
			String email = request.getParameter("email");
			String senha = request.getParameter("senha");
			
			System.out.println(email + " " + senha);
			
			try {
				int cod_pessoa = pDao.verificaCadastroSQL(email, senha);
				System.out.println(cod_pessoa);
				out.print(cod_pessoa);
			} catch (NoSuchAlgorithmException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(acao.equals("pegarNome")) {
			
			System.out.println("to no pegarNome");
			
			String cod_pessoa = request.getParameter("codPessoa");
			
			try {
				String nome = pDao.pegarNome(cod_pessoa);
				pDao.mudarCadastroFB(cod_pessoa);
				out.print(nome);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		if(acao.equals("loginWeb")) {
			System.out.println("ovo logar");
		}
		
	}	
}

