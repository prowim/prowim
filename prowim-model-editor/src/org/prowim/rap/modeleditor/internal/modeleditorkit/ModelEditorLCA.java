/*******************************************************************************
 * Copyright (c) 2002, 2009 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 *     EclipseSource - ongoing development
 ******************************************************************************/
package org.prowim.rap.modeleditor.internal.modeleditorkit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.internal.service.IServiceStateInfo;
import org.eclipse.rwt.lifecycle.AbstractWidgetLCA;
import org.eclipse.rwt.lifecycle.ControlLCAUtil;
import org.eclipse.rwt.lifecycle.IWidgetAdapter;
import org.eclipse.rwt.lifecycle.JSVar;
import org.eclipse.rwt.lifecycle.JSWriter;
import org.eclipse.rwt.lifecycle.ProcessActionRunner;
import org.eclipse.rwt.lifecycle.WidgetLCAUtil;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.rwt.resources.IResourceManager;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.internal.browser.browserkit.BrowserLCA;
import org.eclipse.swt.internal.widgets.IBrowserAdapter;
import org.eclipse.swt.widgets.Widget;
import org.prowim.rap.modeleditor.ModelEditor;
import org.prowim.utils.JSONConvertible;



/**
 * 
 * Lifecycle adapter for the ModelEditor. Copied from {@link BrowserLCA}
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0a8
 */
@SuppressWarnings("restriction")
public final class ModelEditorLCA extends AbstractWidgetLCA
{

    /**
     * blank html code
     */
    static final String         BLANK_HTML               = "<html><script></script></html>";

    /**
     * JS function name
     */
    static final String         PARAM_EXECUTE_FUNCTION   = "executeFunction";

    /**
     * JS function name
     */
    static final String         PARAM_EXECUTE_ARGUMENTS  = "executeArguments";

    /**
     * id
     */
    static final String         EXECUTED_FUNCTION_NAME   = ModelEditor.class.getName() + "#executedFunctionName.";

    /**
     * id
     */
    static final String         EXECUTED_FUNCTION_RESULT = ModelEditor.class.getName() + "#executedFunctionResult.";

    /**
     * id
     */
    static final String         EXECUTED_FUNCTION_ERROR  = ModelEditor.class.getName() + "#executedFunctionError.";

    private static final String QX_FIELD_MODEL_XML       = "modelXML";

    private static final String QX_FIELD_MODEL_ID        = "modelId";

    private static final String QX_TYPE                  = "org.prowim.rap.modeleditor.ModelEditor";
    private static final String QX_FIELD_SOURCE          = "source";

    /**
     * A trigger field to clear the overlay.
     */
    private static final String QX_TRIGGER_HIDE_OVERLAY  = "hideOverlay";

    private static final String PARAM_EXECUTE_RESULT     = "executeResult";
    private static final String FUNCTIONS_TO_CREATE      = Browser.class.getName() + "#functionsToCreate.";
    private static final String FUNCTIONS_TO_DESTROY     = Browser.class.getName() + "#functionsToDestroy.";

    private static final String PROP_URL                 = "url";
    private static final String PROP_TEXT                = "text";

    @Override
    public void preserveValues(final Widget widget)
    {
        ModelEditor modelEditor = (ModelEditor) widget;
        ControlLCAUtil.preserveValues(modelEditor);
        IWidgetAdapter adapter = WidgetUtil.getAdapter(modelEditor);
        adapter.preserve(PROP_URL, modelEditor.getUrl());
        adapter.preserve(PROP_TEXT, getText(modelEditor));
        adapter.preserve(QX_FIELD_MODEL_XML, modelEditor.getModelXML());
        adapter.preserve(QX_FIELD_MODEL_ID, modelEditor.getModelId());
        WidgetLCAUtil.preserveCustomVariant(modelEditor);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.rwt.lifecycle.IWidgetLifeCycleAdapter#readData(org.eclipse.swt.widgets.Widget)
     */
    public void readData(final Widget widget)
    {
        ModelEditor modelEditor = (ModelEditor) widget;

        if (WidgetLCAUtil.readPropertyValue(modelEditor, QX_TRIGGER_HIDE_OVERLAY) != null)
        {
            modelEditor.hideOverlay();
        }

        String modelXML = WidgetLCAUtil.readPropertyValue(modelEditor, QX_FIELD_MODEL_XML);
        if (modelXML != null)
        {
            modelEditor.setModelXML(modelXML);
        }

        String value = WidgetLCAUtil.readPropertyValue(modelEditor, PARAM_EXECUTE_RESULT);
        if (value != null)
        {
            boolean executeResult = Boolean.valueOf(value).booleanValue();
            getAdapter(modelEditor).setExecuteResult(executeResult, executeResult);
        }
        executeFunction(modelEditor);
    }

    @Override
    public void renderInitialization(final Widget widget) throws IOException
    {
        Browser browser = (Browser) widget;
        JSWriter writer = JSWriter.getWriterFor(browser);
        writer.newWidget(QX_TYPE);
        ControlLCAUtil.writeStyleFlags(browser);
    }

    @Override
    public void renderChanges(final Widget widget) throws IOException
    {
        ModelEditor modelEditor = (ModelEditor) widget;
        // TODO [rh] though implemented in DefaultAppearanceTheme, setting
        // border
        // does not work
        ControlLCAUtil.writeChanges(modelEditor);
        destroyBrowserFunctions(modelEditor);
        writeUrl(modelEditor);

        JSWriter.getWriterFor(widget).set(QX_FIELD_MODEL_XML, modelEditor.getModelXML());
        JSWriter.getWriterFor(widget).set(QX_FIELD_MODEL_ID, modelEditor.getModelId());

        createBrowserFunctions(modelEditor);
        writeExecute(modelEditor);
        writeFunctionResult(modelEditor);
        WidgetLCAUtil.writeCustomVariant(modelEditor);
    }

    @Override
    public void renderDispose(final Widget widget) throws IOException
    {
        JSWriter writer = JSWriter.getWriterFor(widget);

        writer.dispose();
    }

    private static void writeUrl(final Browser browser) throws IOException
    {
        if (hasUrlChanged(browser))
        {
            JSWriter writer = JSWriter.getWriterFor(browser);
            writer.set(QX_FIELD_SOURCE, getUrl(browser));
        }
    }

    /**
     * 
     * Description.
     * 
     * @param browser browser
     * @return true or false
     */
    static boolean hasUrlChanged(final Browser browser)
    {
        boolean initialized = WidgetUtil.getAdapter(browser).isInitialized();
        return !initialized || WidgetLCAUtil.hasChanged(browser, PROP_TEXT, getText(browser))
                || WidgetLCAUtil.hasChanged(browser, PROP_URL, browser.getUrl());
    }

    /**
     * 
     * Description.
     * 
     * @param browser browser
     * @return the url
     * @throws IOException may happen
     */
    static String getUrl(final Browser browser) throws IOException
    {
        String text = getText(browser);
        String url = browser.getUrl();
        String result;
        if ( !"".equals(text.trim()))
        {
            result = registerHtml(text);
        }
        else if ( !"".equals(url.trim()))
        {
            result = url;
        }
        else
        {
            result = registerHtml(BLANK_HTML);
        }
        return result;
    }

    private static void writeExecute(final Browser browser) throws IOException
    {
        IBrowserAdapter adapter = getAdapter(browser);
        String executeScript = adapter.getExecuteScript();
        if (executeScript != null)
        {
            JSWriter writer = JSWriter.getWriterFor(browser);
            writer.call("execute", new Object[] { executeScript });
        }
    }

    private static String registerHtml(final String html) throws IOException
    {
        String name = createUrlFromHtml(html);
        byte[] bytes = html.getBytes("UTF-8");
        InputStream inputStream = new ByteArrayInputStream(bytes);
        IResourceManager resourceManager = RWT.getResourceManager();
        resourceManager.register(name, inputStream);
        return resourceManager.getLocation(name);
    }

    private static String createUrlFromHtml(final String html)
    {
        StringBuffer result = new StringBuffer();
        result.append("org.eclipse.swt.browser/text");
        result.append(String.valueOf(html.hashCode()));
        result.append(".html");
        return result.toString();
    }

    private static String getText(final Browser browser)
    {
        Object adapter = browser.getAdapter(IBrowserAdapter.class);
        IBrowserAdapter browserAdapter = (IBrowserAdapter) adapter;
        return browserAdapter.getText();
    }

    private static IBrowserAdapter getAdapter(final Browser browser)
    {
        return (IBrowserAdapter) browser.getAdapter(IBrowserAdapter.class);
    }

    // ////////////////////////////////////
    // Helping methods for BrowserFunction

    private static void createBrowserFunctions(final Browser browser) throws IOException
    {
        IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
        String id = WidgetUtil.getId(browser);
        String[] functions = (String[]) stateInfo.getAttribute(FUNCTIONS_TO_CREATE + id);
        if (functions != null)
        {
            for (int i = 0; i < functions.length; i++)
            {
                JSWriter writer = JSWriter.getWriterFor(browser);
                writer.call("createFunction", new Object[] { functions[i] });
            }
        }
    }

    private static void destroyBrowserFunctions(final Browser browser) throws IOException
    {
        IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
        String id = WidgetUtil.getId(browser);
        String[] functions = (String[]) stateInfo.getAttribute(FUNCTIONS_TO_DESTROY + id);
        if (functions != null)
        {
            for (int i = 0; i < functions.length; i++)
            {
                JSWriter writer = JSWriter.getWriterFor(browser);
                writer.call("destroyFunction", new Object[] { functions[i] });
            }
        }
    }

    private void executeFunction(final Browser browser)
    {
        String function = WidgetLCAUtil.readPropertyValue(browser, PARAM_EXECUTE_FUNCTION);
        String arguments = WidgetLCAUtil.readPropertyValue(browser, PARAM_EXECUTE_ARGUMENTS);
        if (function != null)
        {
            IBrowserAdapter adapter = getAdapter(browser);
            BrowserFunction[] functions = adapter.getBrowserFunctions();
            boolean found = false;
            for (int i = 0; i < functions.length && !found; i++)
            {
                final BrowserFunction current = functions[i];
                if (current.getName().equals(function))
                {
                    final Object[] args = parseArguments(arguments);
                    ProcessActionRunner.add(new Runnable()
                    {
                        public void run()
                        {
                            try
                            {
                                setExecutedFunctionName(browser, current.getName());
                                Object executedFunctionResult = current.function(args);
                                setExecutedFunctionResult(browser, executedFunctionResult);
                            }
                            catch (Exception e)
                            {
                                setExecutedFunctionError(browser, e.getMessage());
                            }
                        }
                    });
                    found = true;
                }
            }
        }
    }

    private static void writeFunctionResult(final Browser browser) throws IOException
    {
        IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
        String id = WidgetUtil.getId(browser);
        String function = (String) stateInfo.getAttribute(EXECUTED_FUNCTION_NAME + id);
        if (function != null)
        {
            Object result = stateInfo.getAttribute(EXECUTED_FUNCTION_RESULT + id);
            if (result != null)
            {
                result = new JSVar(toJson(result, true));
            }
            String error = (String) stateInfo.getAttribute(EXECUTED_FUNCTION_ERROR + id);
            JSWriter writer = JSWriter.getWriterFor(browser);
            writer.call("setFunctionResult", new Object[] { result, error });
        }
    }

    private static void setExecutedFunctionName(final Browser browser, final String name)
    {
        IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
        String id = WidgetUtil.getId(browser);
        stateInfo.setAttribute(EXECUTED_FUNCTION_NAME + id, name);
    }

    private static void setExecutedFunctionResult(final Browser browser, final Object result)
    {
        IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
        String id = WidgetUtil.getId(browser);
        stateInfo.setAttribute(EXECUTED_FUNCTION_RESULT + id, result);
    }

    private static void setExecutedFunctionError(final Browser browser, final String error)
    {
        IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
        String id = WidgetUtil.getId(browser);
        stateInfo.setAttribute(EXECUTED_FUNCTION_ERROR + id, error);
    }

    /**
     * 
     * parse parse.
     * 
     * @param arguments sdfg
     * @return stuff
     */
    @SuppressWarnings("unchecked")
    static Object[] parseArguments(final String arguments)
    {
        @SuppressWarnings("rawtypes")
        List result = new ArrayList();
        if (arguments.startsWith("[") && arguments.endsWith("]"))
        {
            // remove [ ] brackets
            String args = arguments.substring(1, arguments.length() - 1);
            int openQuotes = 0;
            int openBrackets = 0;
            String arg;
            StringBuffer argBuff = new StringBuffer();
            char prevChar = ' ';
            for (int i = 0; i < args.length(); i++)
            {
                char ch = args.charAt(i);
                if (ch == ',' && openQuotes == 0 && openBrackets == 0)
                {
                    arg = argBuff.toString();
                    if (arg.startsWith("["))
                    {
                        result.add(parseArguments(arg));
                    }
                    else
                    {
                        arg = arg.replaceAll("\\\\\"", "\"");
                        result.add(withType(arg));
                    }
                    argBuff.setLength(0);
                }
                else
                {
                    if (ch == '"' && prevChar != '\\')
                    {
                        if (openQuotes == 0)
                        {
                            openQuotes++;
                        }
                        else
                        {
                            openQuotes--;
                        }
                    }
                    else if (ch == '[' && openQuotes == 0)
                    {
                        openBrackets++;
                    }
                    else if (ch == ']' && openQuotes == 0)
                    {
                        openBrackets--;
                    }
                    argBuff.append(ch);
                }
                prevChar = ch;
            }
            // append last segment
            arg = argBuff.toString();
            if (arg.startsWith("["))
            {
                result.add(parseArguments(arg));
            }
            else if ( !arg.equals(""))
            {
                arg = arg.replaceAll("\\\\\"", "\"");
                result.add(withType(arg));
            }
        }
        return result.toArray();
    }

    /**
     * 
     * I don't know. ask RAP
     * 
     * @param argument xxx
     * @return xxx
     */
    static Object withType(final String argument)
    {
        Object result;
        if (argument.equals("null") || argument.equals("undefined"))
        {
            result = null;
        }
        else if (argument.equals("true") || argument.equals("false"))
        {
            result = new Boolean(argument);
        }
        else if (argument.startsWith("\""))
        {
            result = new String(argument.substring(1, argument.length() - 1));
        }
        else
        {
            try
            {
                result = Double.valueOf(argument);
            }
            catch (NumberFormatException nfe)
            {
                result = argument;
            }
        }
        return result;
    }

    /**
     * 
     * Convert Object to JSON
     * 
     * @param object o
     * @param deleteLastChar maybe?
     * @return a json string
     */
    static String toJson(final Object object, final boolean deleteLastChar)
    {
        StringBuilder result = new StringBuilder();
        if (object == null)
        {
            result.append("null");
            result.append(",");
        }
        else if (object instanceof String)
        {
            result.append("\"");
            result.append(StringEscapeUtils.escapeJavaScript((String) object));
            result.append("\"");
            result.append(",");
        }
        else if (object instanceof Boolean)
        {
            result.append(((Boolean) object).toString());
            result.append(",");
        }
        else if (object instanceof Number)
        {
            result.append(((Number) object).toString());
            result.append(",");
        }
        else if (object.getClass().isArray())
        {
            Object[] array = (Object[]) object;
            result.append("[");
            for (int i = 0; i < array.length; i++)
            {
                result.append(toJson(array[i], false));
            }
            if (',' == result.charAt(result.length() - 1))
            {
                // insert ']' before last ','
                result.insert(result.length() - 1, "]");
            }
            else
            {
                // special handling for empty array
                result.append("],");
            }
        }
        else if (object instanceof Collection)
        {
            result.append(toJson(((Collection) object).toArray(), false));
        }
        else if (object instanceof JSONConvertible)
        {
            result.append(((JSONConvertible) object).toJSON());
            result.append(",");
        }
        if (deleteLastChar)
        {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }
}
