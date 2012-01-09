package org.strategoxt.imp.editors.spoofax.command;

public interface ISpxlangCommand<T,R> extends ISpxlangBaseCommand {

	public R getResult();

	public void setContext( T context);
}
