package br.com.caelum.livraria.bean;

import javax.faces.bean.ManagedBean;

import br.com.caelum.livraria.dao.UsuarioDao;
import br.com.caelum.livraria.modelo.Usuario;

@ManagedBean
public class LoginBean {
	private Usuario user = new Usuario();
	
	public Usuario getUser() {
		return user;
	}
	
	public String login() {
		UsuarioDao dao = new UsuarioDao();
		if (dao.existsByEmailAndPass(user.getEmail(), user.getSenha())) {
			return "livro?faces-redirect=true";
		}
		
		return "login?faces-redirect=true";
	}
}
