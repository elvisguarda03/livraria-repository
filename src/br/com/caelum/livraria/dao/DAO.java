package br.com.caelum.livraria.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class DAO<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Class<T> classe;
	private EntityManager manager;

	public DAO(EntityManager manager, Class<T> classe) {
		this.manager = manager;
		this.classe = classe;
	}

	public void adiciona(T t) {
		// abre transacao
		manager.getTransaction().begin();

		// persiste o objeto
		manager.persist(t);

		// commita a transacao
		manager.getTransaction().commit();

		// fecha a entity manager
	}

	public void remove(T t) {
		manager.getTransaction().begin();

		manager.remove(manager.merge(t));

		manager.getTransaction().commit();
	}

	public void atualiza(T t) {
		manager.getTransaction().begin();

		manager.merge(t);

		manager.getTransaction().commit();
	}

	public List<T> listaTodos() {
		List<T> lista = manager.createQuery("FROM " + classe.getName(), classe)
				.getResultList();
//		CriteriaQuery<T> query = manager.getCriteriaBuilder().createQuery(classe);
//		query.select(query.from(classe));

//		List<T> lista = manager.createQuery(query).getResultList();

		return lista;
	}

	public T buscaPorId(Integer id) {
		T instancia = manager.find(classe, id);
		
		return instancia;
	}

	public int contaTodos() {
		long result = (Long) manager.createQuery("select count(n) from Livro n")
				.getSingleResult();

		return (int) result;
	}

	public List<T> listaTodosPaginada(int firstResult, int maxResults, String coluna, String valor) {
		CriteriaQuery<T> query = manager.getCriteriaBuilder().createQuery(classe);
		Root<T> root = query.from(classe);
		
		if (!Objects.isNull(valor)) {
			query = query.where(manager.getCriteriaBuilder().like(root.<String>get(coluna), valor + "%"));
		}

		List<T> lista = manager.createQuery(query).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();

		return lista;
	}
}