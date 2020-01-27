package br.com.caelum.livraria.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.caelum.livraria.modelo.Usuario;

public class UsuarioDao {
	
	public boolean existsByEmailAndPass(String email, String senha) {
		EntityManager em = new JPAUtil().getEntityManager();
		
		TypedQuery<Usuario> typedQuery = em.createQuery(
				"FROM Usuario u where u.email = :pEmail and u.senha = :pSenha", Usuario.class);
		typedQuery.setParameter("pEmail", email);
		typedQuery.setParameter("pSenha", senha);
		
		try {
			typedQuery.getSingleResult();
		} catch (NoResultException nre) {
			return false;
		}
		
		return true;
	}
}