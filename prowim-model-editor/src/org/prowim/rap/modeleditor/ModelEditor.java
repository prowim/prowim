/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 *------------------------------------------------------------------------------
 * (c) 07.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *This file is part of ProWim.

ProWim is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ProWim is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ProWim.  If not, see <http://www.gnu.org/licenses/>.

Diese Datei ist Teil von ProWim.

ProWim ist Freie Software: Sie können es unter den Bedingungen
der GNU General Public License, wie von der Free Software Foundation,
Version 3 der Lizenz oder (nach Ihrer Option) jeder späteren
veröffentlichten Version, weiterverbreiten und/oder modifizieren.

ProWim wird in der Hoffnung, dass es nützlich sein wird, aber
OHNE JEDE GEWÄHELEISTUNG, bereitgestellt; sogar ohne die implizite
Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
Siehe die GNU General Public License für weitere Details.

Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 *
 */

package org.prowim.rap.modeleditor;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.ebcot.tools.logging.Logger;


/**
 * 
 * A RAP-Widget to display the ModelEditor
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0.a9
 */
public class ModelEditor extends Browser
{

    @SuppressWarnings("unused")
    private static final Logger LOG                   = Logger.getLogger(ModelEditor.class);

    /**
     * The Base URL to build the URL .
     */
    private static final String MODELLEDITOR_BASE_URL = ModelEditorActivator.MODELEDITOR_BASE_URL;

    /**
     * The basic URL to start the model editor.
     */
    private static final String MODELEDITOR_URL       = MODELLEDITOR_BASE_URL + "/prowim-model-editor.html";

    /**
     * The basic URL to start the model viewer.
     */
    private static final String MODELVIEWER_URL       = MODELLEDITOR_BASE_URL + "/prowim-model-viewer.html";

    /**
     * The URL to show a white page.
     */
    private static final String CLEAN_PAGE_URL        = MODELLEDITOR_BASE_URL + "/clean.html";

    /**
     * The id of the model to show
     */
    private String              modelId;

    /**
     * the current mode
     */
    private EditorMode          mode;

    /**
     * The name of the current model
     */
    private String              modelName;

    private String              modelXML;

    /**
     * Just a hack to reload the editor without changing the {@link #modelId} ({@link #modelReloadTimestamp} is added to the URL)
     */
    private long                modelReloadTimestamp  = System.currentTimeMillis();

    private boolean             saveOnClose           = true;

    private final Dialog        loadingOverlay;

    private boolean             isLoading             = false;

    /**
     * 
     * Create a new {@link ModelEditor} on the given parent
     * 
     * @param parent the parent component
     */
    public ModelEditor(final Composite parent)
    {
        super(parent, SWT.NO_SCROLL);
        this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        this.loadingOverlay = new OverlayDialog(parent.getShell());
    }

    /**
     * 
     * The current id of the model.
     * 
     * @return may be null
     */
    public String getModelId()
    {
        return modelId;
    }

    /**
     * 
     * set the id of the current model
     * 
     * @param modelId must not be null
     */
    private void setModelId(String modelId)
    {
        Validate.notNull(modelId);

        this.modelId = modelId;
    }

    /**
     * Generate the URL of the editor depending of current mode is {@link EditorMode#EDITOR} or {@link EditorMode#VIEWER}
     */
    @Override
    public String getUrl()
    {

        String generatedUrl = null;
        String contextpath = RWT.getRequest().getContextPath();

        if (this.mode != null)
        {
            switch (this.mode)
            {
                case EDITOR:
                    generatedUrl = contextpath + buildModelEditorUrl();
                    break;
                case VIEWER:
                    generatedUrl = contextpath + buildModelViewerUrl();
                    break;
                default:
                    throw new IllegalStateException("Unknown mode. Cannot generate URL");
            }

            // Use this to fire a LocationChangeEvent
            if ( !generatedUrl.equals(super.getUrl()))
            {
                super.setUrl(generatedUrl);

                isLoading = true;

                // Loading modelEditor. Show overlay
                final Display display = Display.getCurrent();
                display.asyncExec(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        LOG.debug("Showing overlay");
                        loadingOverlay.open();
                    }
                });
            }
        }
        else
        {
            generatedUrl = contextpath + CLEAN_PAGE_URL;
        }

        return generatedUrl;
    }

    /**
     * 
     * check if the modeleditor is currently loading.
     * 
     * @return loading state
     */
    public boolean isLoading()
    {
        return isLoading;
    }

    /**
     * Do not use this method
     * 
     * @param url No URL necessary
     */
    @Override
    public boolean setUrl(String url)
    {
        throw new UnsupportedOperationException("URL is generated by class");
    }

    /**
     * Show the given model in viewer mode
     * 
     * @param modelId the modelId to show in the viewer
     */
    public void setViewerMode(String modelId)
    {
        setMode(EditorMode.VIEWER, modelId);
    }

    /**
     * Show the given model in editor mode
     * 
     * @param modelId the modelId to show in the editor
     */
    public void setEditorMode(String modelId)
    {
        setMode(EditorMode.EDITOR, modelId);
    }

    /**
     * Sets the mode of the editor
     * 
     * @param mode the mode to set
     * @param modelID the model ID to use
     * @see EditorMode#EDITOR
     * @see EditorMode#VIEWER
     */
    private void setMode(EditorMode mode, String modelID)
    {
        Validate.notNull(mode);
        Validate.notNull(modelID);
        Validate.isTrue( !isLoading, "Cannot set a new model while last one is still loading");

        this.setModelId(modelID);
        this.mode = mode;
    }

    /**
     * Create The URL to open the model editor with the given id.
     * 
     * @param id the id of the model to open
     * 
     * @return a non-null url
     */
    private String buildModelEditorUrl()
    {
        return MODELEDITOR_URL + "?prowimid=" + getModelId() + "&reloader=" + modelReloadTimestamp;
    }

    /**
     * Create The URL to open the model viewer with the given id.
     * 
     * @param id the id of the model to open
     * 
     * @return a non-null url
     */
    private String buildModelViewerUrl()
    {
        return MODELVIEWER_URL + "?prowimid=" + getModelId() + "&reloader=" + modelReloadTimestamp;
    }

    /**
     * 
     * Set the name of the current model
     * 
     * @param modelName never null
     */
    public void setModelName(String modelName)
    {
        Validate.notNull(modelName);

        this.modelName = modelName;
    }

    /**
     * 
     * Get the name of the current model.
     * 
     * @return not null
     */
    public String getModelName()
    {
        return modelName;
    }

    /**
     * 
     * Reload the modelEditor using the current settings
     */
    public void reload()
    {
        this.modelReloadTimestamp = System.currentTimeMillis();
    }

    /**
     * 
     * Reload the modelEditor using the current settings
     */
    public void clear()
    {
        this.mode = null;
        this.modelId = null;
        this.modelName = null;
        this.modelXML = null;
    }

    /**
     * 
     * Get the current operation mode
     * 
     * @return the mode. May be null if not model is selected
     */
    public EditorMode getMode()
    {
        return mode;
    }

    /**
     * 
     * Get the XML model
     * 
     * @return the modelXML
     */
    public String getModelXML()
    {
        return modelXML;
    }

    /**
     * 
     * Set the XML Model
     * 
     * @param modelXML the modelXML to set- Must not be null
     */
    public void setModelXML(String modelXML)
    {
        Validate.notNull(modelXML);

        this.modelXML = modelXML;
    }

    /**
     * 
     * The mode of the ModelEditor
     * 
     * @author Philipp Leusmann
     * @version $Revision$
     * @since 2.0.0a8
     */
    public static enum EditorMode
    {
        /**
         * Edit an existing Model
         */
        EDITOR,

        /**
         * View an existing Model
         */
        VIEWER
    }

    /**
     * Sets the flag wether to save the model on closing the view of the model editor, default is true.
     * 
     * Is used by the ModelEditorView.
     * 
     * @param saveOnClose if true, model will bes saved when closing
     */
    public void setSaveOnClose(boolean saveOnClose)
    {
        this.saveOnClose = saveOnClose;
    }

    /**
     * Returns true, if the model should be saved when closing the view.
     * 
     * @return true, when model should be saved on close, default is true
     */
    public boolean getSaveOnClose()
    {
        return saveOnClose;
    }

    /**
     * 
     * Hide the overlay dialog.
     */
    public void hideOverlay()
    {
        final Display display = Display.getCurrent();
        display.asyncExec(new Runnable()
        {

            @Override
            public void run()
            {
                LOG.debug("Removing overlay");
                ModelEditor.this.loadingOverlay.close();
            }
        });
        this.isLoading = false;
    }

    /**
     * 
     * A modal invisible dialog to prevent users from clicking while editor is loading
     * 
     * @author Philipp Leusmann
     * @version $Revision$
     * @since 2.0.0
     */
    private class OverlayDialog extends Dialog
    {

        /**
         * Description.
         * 
         * @param parentShell
         */
        protected OverlayDialog(Shell parentShell)
        {
            super(parentShell);
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.jface.window.Window#getShellStyle()
         */
        @Override
        protected int getShellStyle()
        {
            return SWT.APPLICATION_MODAL;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.jface.window.Window#getConstrainedShellBounds(org.eclipse.swt.graphics.Rectangle)
         */
        @Override
        protected Rectangle getConstrainedShellBounds(Rectangle preferredSize)
        {
            return new Rectangle(1, 1, 1, 1);
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
         */
        @Override
        protected Control createDialogArea(Composite parent)
        {
            return null;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.jface.dialogs.Dialog#createButtonBar(org.eclipse.swt.widgets.Composite)
         */
        @Override
        protected Control createButtonBar(Composite parent)
        {
            return null;
        }
    }
}
