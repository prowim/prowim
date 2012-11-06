/*
 * $Id: editorMain.js ,v 1.0 18/10/2009 Maziar Khodaei$
 * Copyright (c) 2009, Ebcot GmbH
 *
 * Defines the startup sequence of the application.
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
 */
{

	/**
	 * Constructs a new application
	 */
	function editorMain(config)
	{
		var hideSplash = function()
		{
			// Fades-out the splash screen
			var splash = $('#splash').get(0);
			
			if (splash != null)
			{
				try
				{
					mxEvent.release(splash);
					mxUtils.fadeOut(splash, 100, true);
				}
				catch (e)
				{
					splash.parentNode.removeChild(splash);
				}
			} else {
				alert("unable to remove splash overlay");
			}
		};
		
		try
		{
			if (!mxClient.isBrowserSupported())
			{
				mxUtils.error('Browser is not supported!', 200, false);
			}
			else
			{
				var modelID = '';
				var processCreated = false;
				var node = mxUtils.load(config).getDocumentElement();
				var editor = new mxEditor(node);
				var model =editor.graph.getModel();
				// Updates the window title after opening new files
				var title = document.title;
				var funct = function(sender)
				{
					document.title = title + ' - ' + sender.getTitle();
				};
				
				
				editor.addListener(mxEvent.OPEN, funct);
				
				// Prints the current root in the window title if the
				// current root of the graph changes (drilling).
				editor.addListener(mxEvent.ROOT, funct);
				funct(editor);
				
				// Defines an icon for creating new connections in the connection handler.
				// This will automatically disable the highlighting of the source vertex.
				mxConnectionHandler.prototype.connectImage = new mxImage('images/connector.gif', 16, 16);
				
				// Enables connections in the graph and disables
				// reset of zoom and translate on root change
				// (ie. switch between XML and graphical mode).
				editor.graph.setConnectable(true);
				editor.graph.setTooltips(true);
				editor.graph.setAllowDanglingEdges(false);
				editor.graph.setMultigraph(true);
				
				// Disable the orientation change by double click false
				mxElbowEdgeHandler.prototype.flipEnabled = false;
				
				// Validate the characteristics of shapes and their connections
				EditorFunctions.validation(editor.graph);
				
				// Enables rubberband selection
				new mxRubberband(editor.graph);
				
				RegisterViewerActions(editor);
				RegisterEditorActions(editor);
				editor.graph.vertexLabelsMovable = true;
				
				// Installs automatic validation (use editor.validation = true
				// if you are using an mxEditor instance)
				var listener = function(sender, evt)
				{
					//var selected
					editor.graph.validateGraph();
					editorMain.prototype.modified=true;
				};
				
				editor.graph.getModel().addListener(mxEvent.CHANGE, listener);

				// register the key handler
				var keyHandler = new mxKeyHandler(editor.graph);
				ApplicationEventsHandler.bindDownArrow(editor, keyHandler);
				ApplicationEventsHandler.bindUpArrow(editor, keyHandler);
				ApplicationEventsHandler.bindLeftArrow(editor, keyHandler);
				ApplicationEventsHandler.bindRightArrow(editor, keyHandler);
				
				editor.graph.htmlLabels=true; 
				
				// Set Wrapping by edit a label in one cell
				editor.graph.isWrapping = function(cell) {
						return true;
				}; 

				modelID = getQXModelEditor().getModelId();
				if (modelID == '')
				{
					throw("Creating a new process is not supported anymore");
				}
				else
				{
					var outXml = getQXModelEditor().getModelXML();
					var doc = mxUtils.parseXml(outXml); 
					var codec = new mxCodec(doc); 

					//Update the process Shape
					var processShape = codec.getElementById(modelID, "prowimid");
					if (processShape == null)
					{
						throw("Could not find model ID " + modelID + " in XML.");
					}
					
					mxCodec.prototype.setAttribute(processShape, "label", EditorProxy.getName(modelID));
					mxCodec.prototype.setAttribute(processShape, "description", EditorProxy.getDescription(modelID));

					codec.decode(doc.documentElement, editor.graph.getModel());	

					// Rewrite the model with new values
					EditorFunctions.initModel(editor, codec, modelID);
					
					editor.graph.getView().refresh();
					editorMain.prototype.modified=false;
				}
				
				// Add listener to create objects in ontology
				EditorListener.addCells(editor, modelID);
				// Add listener to rename, double click, move cells
				EditorListener.renameCell(editor);
				// Add listener to double click of a cell
				EditorListener.dblClick(editor);

				EditorListener.click(editor);
				EditorListener.change(editor);
				
				EditorListener.cellsRemoved(editor);
				
				EditorListener.addUpdateListener(editor);
				
				EditorFunctions(editor);
				
				// Shows the application
				hideSplash();
			}
		}
		catch (e)
		{
			hideSplash();
			throw e; // for debugging
		}
								
		return editor;
	}
	
	
	
	

}
