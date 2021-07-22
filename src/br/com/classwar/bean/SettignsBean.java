package br.com.classwar.bean;

import java.io.File;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

@ManagedBean
public class SettignsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4126964094735625549L;
	@ManagedProperty(value="#{engineBean}")
	protected EngineBean engineBean;

	public void save() {
		try {
			String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "WEB-INF/";
			File fileSettings = new File(path + "settings.json");
			Gson gson = new Gson();
			FileUtils.writeStringToFile(fileSettings, gson.toJson(engineBean.getSettings().getListUnits()), "UTF-8");
			addMessage("Salvo...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	public EngineBean getEngineBean() {
		return engineBean;
	}

	public void setEngineBean(EngineBean engineBean) {
		this.engineBean = engineBean;
	}	

}
