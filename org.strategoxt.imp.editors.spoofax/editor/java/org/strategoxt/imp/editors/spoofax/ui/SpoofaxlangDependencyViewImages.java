/*******************************************************************************
 * Copyright (c) 2009 EclipseSource Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     EclipseSource Corporation - initial API and implementation
 *  	Md. Adil Akhter <md.adilakhter@gmail.com>   
 *     
 *******************************************************************************/
package org.strategoxt.imp.editors.spoofax.ui;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.strategoxt.imp.editors.spoofax.Activator;

public class SpoofaxlangDependencyViewImages {

	private static ImageRegistry PLUGIN_REGISTRY;

	public final static String ICONS_PATH = "icons/"; //$NON-NLS-1$

	private static final String PATH_OBJ = ICONS_PATH + "obj16/"; //$NON-NLS-1$

	public static final String IMG_SNAPSHOT = "snapshot.gif"; //$NON-NLS-1$
	public static final String IMG_SAVEEDIT = "save_edit.gif"; //$NON-NLS-1$
	public static final String IMG_REQ_PLUGIN_OBJ ="req_plugins_obj.gif"; //$NON-NLS-1$
	public static final String IMG_SEARCH_CANCEL ="progress_rem.gif"; //$NON-NLS-1$
	public static final String IMG_CALLEES = "ch_callees.gif"; //$NON-NLS-1$
	public static final String IMG_CALLERS = "ch_callers.gif"; //$NON-NLS-1$
	public static final String IMG_FOCUS = "focus.gif"; //$NON-NLS-1$
	public static final String IMG_FOCUS_PROJECT = "search-icon.png"; //$NON-NLS-1$
	public static final String IMG_VIZ_LABEL = "packages.gif"; //$NON-NLS-1$
	public static final String IMG_SPX_PROJECT = "projects.gif"; 	
	public static final String IMG_SPX_PACKAGE = "package_obj.gif";
	public static final String IMG_SPX_EMPTY_PACKAGE = "empty_logical_package_obj.gif";
	public static final String IMG_SPX_MODULE = "public_co.gif";
	public static final String IMG_REFRESH = "refresh.gif";
	public static final String IMG_OPEN = "opentype.gif";
	
	
	
	public static final ImageDescriptor DESC_SNAPSHOT = create(PATH_OBJ, IMG_SNAPSHOT);
	public static final ImageDescriptor DESC_SAVEEDIT = create(PATH_OBJ, IMG_SAVEEDIT);
	public static final ImageDescriptor DESC_REQ_PLUGIN_OBJ = create(PATH_OBJ, IMG_REQ_PLUGIN_OBJ);
	public static final ImageDescriptor DESC_SEARCH_CANCEL = create(PATH_OBJ, IMG_SEARCH_CANCEL);
	public static final ImageDescriptor DESC_CALLEES = create(PATH_OBJ, IMG_CALLEES);
	public static final ImageDescriptor DESC_CALLERS = create(PATH_OBJ, IMG_CALLERS);
	public static final ImageDescriptor DESC_FOCUS = create(PATH_OBJ, IMG_FOCUS);
	public static final ImageDescriptor DESC_FOCUS_PROJECT = create(PATH_OBJ, IMG_FOCUS_PROJECT);
	public static final ImageDescriptor DESC_SPX_PROJECT = create(PATH_OBJ, IMG_SPX_PROJECT);
	public static final ImageDescriptor DESC_VIZ_LABEL = create(PATH_OBJ, IMG_VIZ_LABEL);
	public static final ImageDescriptor DESC_SPX_PACKAGE = create(PATH_OBJ, IMG_SPX_PACKAGE);
	public static final ImageDescriptor DESC_SPX_EMPTY_PACKAGE = create(PATH_OBJ, IMG_SPX_EMPTY_PACKAGE);
	public static final ImageDescriptor DESC_SPX_MODULE = create(PATH_OBJ, IMG_SPX_MODULE);
	public static final ImageDescriptor DESC_REFRESH = create(PATH_OBJ, IMG_REFRESH);
	public static final ImageDescriptor DESC_OPEN = create(PATH_OBJ, IMG_OPEN);
	
	private static final void initialize() {
		PLUGIN_REGISTRY = Activator.getInstance().getImageRegistry();
		manage(IMG_SNAPSHOT, DESC_SNAPSHOT);
		manage(IMG_SAVEEDIT, DESC_SAVEEDIT);
		manage(IMG_REQ_PLUGIN_OBJ, DESC_REQ_PLUGIN_OBJ);
		manage(IMG_SEARCH_CANCEL, DESC_SEARCH_CANCEL);
		manage(IMG_CALLEES, DESC_CALLEES);
		manage(IMG_CALLERS, DESC_CALLERS);
		manage(IMG_FOCUS, DESC_FOCUS);
		manage(IMG_FOCUS_PROJECT, DESC_FOCUS_PROJECT);
		manage(IMG_SPX_PROJECT, DESC_SPX_PROJECT);
		manage(IMG_VIZ_LABEL, DESC_VIZ_LABEL);
		manage(IMG_SPX_PACKAGE, DESC_SPX_PACKAGE);
		manage(IMG_SPX_MODULE, DESC_SPX_MODULE);
		manage(IMG_SPX_EMPTY_PACKAGE, DESC_SPX_EMPTY_PACKAGE);
		manage(IMG_REFRESH, DESC_REFRESH);
		manage(IMG_OPEN, DESC_OPEN);
	}

	private static ImageDescriptor create(String prefix, String name) {
		return ImageDescriptor.createFromURL(makeIconURL(prefix, name));
	}
	public static Image get(String key) {
		if (PLUGIN_REGISTRY == null)
			initialize();
		return PLUGIN_REGISTRY.get(key);
	}

	private static URL makeIconURL(String prefix, String name) {
		String path = "$nl$/" +prefix + name;
		return FileLocator.find(Activator.getInstance().getBundle(), new Path(path), null);
	}

	public static Image manage(String key, ImageDescriptor desc) {
		Image image = desc.createImage();
		PLUGIN_REGISTRY.put(key, image);
		return image;
	}
}
