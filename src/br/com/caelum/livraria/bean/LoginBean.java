package br.com.caelum.livraria.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.caelum.livraria.dao.UsuarioDao;
import br.com.caelum.livraria.modelo.Usuario;

@ManagedBean
public class LoginBean extends BaseBean {
	private Usuario user = new Usuario();
	
	public Usuario getUser() {
		return user;
	}
	
	public String login() {
		System.out.println("Fazendo login do usu�rio "
	            + this.user.getEmail());

		FacesContext context = getFacesContext();
		ExternalContext externalContext = context.getExternalContext();
		
		UsuarioDao dao = new UsuarioDao();
		if (!dao.existsByEmailAndPass(user.getEmail(), user.getSenha())) {
			//O FacesContext s� existe em uma requisi��o, e com o faces-redirect s�o feitas 2 requisi��es.
			//Existe um escopo chamado flash no ExternalContext que surgiu a partir do JSF 2, e este dura duas requisi��es.
			//Este m�todo mant�m a mensagem adicionada por duas requisi��es.
			externalContext.getFlash().setKeepMessages(true);
			context.addMessage(null, new FacesMessage("Usu�rio n�o encontrado"));
			
			return "login?faces-redirect=true";
		}
		
		externalContext.getSessionMap().put("userLogged", this.user);
		
		return "livro?faces-redirect=true";
	}
	
	public String logout() {
		FacesContext context = getFacesContext();
		context.getExternalContext().getSessionMap().remove("userLogged");
		
		return "login?faces-redirect=true";
	}
}