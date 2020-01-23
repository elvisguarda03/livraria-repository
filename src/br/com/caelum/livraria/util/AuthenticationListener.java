package br.com.caelum.livraria.util;

import java.util.Objects;

import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import br.com.caelum.livraria.modelo.Usuario;

public class AuthenticationListener implements PhaseListener {
	private static final long serialVersionUID = 3382991069312186471L;
	
	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		
		UIViewRoot root = context.getViewRoot();
		
		String namePage = root.getViewId();
		System.out.println(namePage);
		
		if (namePage.equals("/login.xhtml")) {
			return;
		}
		
		Usuario user = (Usuario) context.getExternalContext()
				.getSessionMap().get("userLogged");
		
		if (!Objects.isNull(user)) {
			return;
		}
		
		NavigationHandler handler = context.getApplication().getNavigationHandler();
		handler.handleNavigation(context, null, "/login?faces-redirect=true");
		
		context.renderResponse();
	}

	@Override
	public void beforePhase(PhaseEvent event) {
	}

	@Override
	public PhaseId getPhaseId() {
		//É necessário verificar na primeira fase se o usuário está autenticado.
		return PhaseId.RESTORE_VIEW;
	}

}
