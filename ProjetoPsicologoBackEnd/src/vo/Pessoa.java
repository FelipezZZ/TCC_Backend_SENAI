package vo;

public class Pessoa {

	private int cod_pessoa;
	private String login;
	private String nickname;
	private  String senha;
	private int tipoPerf;
	private boolean Verificado;
	private String identidade;
	private boolean anonimo;
	private boolean logado;
	
	public boolean isLogado() {
		return logado;
	}
	public void setLogado(boolean logado) {
		this.logado = logado;
	}
	public boolean isAnonimo() {
		return anonimo;
	}
	public void setAnonimo(boolean anonimo) {
		this.anonimo = anonimo;
	}
	public Pessoa(int cod_pessoa,String pessoa,String nickname,String senha,int tipoPerf,boolean perfVerificado,String identidade,String login) {
		
	}
	public Pessoa() {
		
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	public int getCod_pessoa() {
		return cod_pessoa;
	}
	public void setCod_pessoa(int cod_pessoa) {
		this.cod_pessoa = cod_pessoa;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public int getTipoPerf() {
		return tipoPerf;
	}
	public void setTipoPerf(int tipoPerf) {
		this.tipoPerf = tipoPerf;
	}
	
	public boolean isVerificado() {
		return Verificado;
	}
	public void setVerificado(boolean verificado) {
		Verificado = verificado;
	}
	public String getIdentidade() {
		return identidade;
	}
	public void setIdentidade(String identidade) {
		this.identidade = identidade;
	}


	
	
	
}


