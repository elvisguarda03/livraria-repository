package br.com.caelum.livraria.bean;

import javax.faces.context.FacesContext;

public abstract class BaseBean {
	
	public final FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
}
