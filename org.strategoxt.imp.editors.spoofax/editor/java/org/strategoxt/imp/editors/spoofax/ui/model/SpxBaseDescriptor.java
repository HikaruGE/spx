package org.strategoxt.imp.editors.spoofax.ui.model;


public class SpxBaseDescriptor{
	private Object parent;
	
	protected void setParent(Object parent){
		this.parent = parent;
	}
	
	public  Object getParent(){
		return parent;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getTypedParent(){
		return (T)parent;
	}
}
