/*
 * $Id: editorMain.js ,v 1.0 18/10/2009 Maziar Khodaei$
 * Copyright (c) 2009, Ebcot GmbH
 *
 * Defines the startup sequence of the application.
 * 
 * This file is part of ProWim.

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
{

	/**
	 * Constructs a new application
	 */
	function editorMain(config) {
		var hideSplash = function() {
			// Fades-out the splash screen
			var splash = document.getElementById('splash');

			if (splash != null) {
				try {
					mxEvent.release(splash);
					mxUtils.fadeOut(splash, 100, true);
				} catch (e) {
					splash.parentNode.removeChild(splash);
				}
			}
		};

		try {
			if (!mxClient.isBrowserSupported()) {
				mxUtils.error('Browser is not supported!', 200, false);
			} else {
				var modelID;
				var processCreated = false;
				var node = mxUtils.load(config).getDocumentElement();
				var editor = new mxEditor(node);

				// Updates the window title after opening new files
				var title = document.title;
				var funct = function(sender) {
					document.title = title + ' - ' + sender.getTitle();
				};

				editor.addListener(mxEvent.OPEN, funct);

				// Prints the current root in the window title if the
				// current root of the graph changes (drilling).
				editor.addListener(mxEvent.ROOT, funct);
				funct(editor);

				// Defines an icon for creating new connections in the
				// connection handler.
				// This will automatically disable the highlighting of the
				// source vertex.
				mxConnectionHandler.prototype.connectImage = new mxImage(
						'images/connector.gif', 16, 16);

				// Enables connections in the graph and disables
				// reset of zoom and translate on root change
				// (ie. switch between XML and graphical mode).
				editor.graph.setConnectable(true);
				editor.graph.setTooltips(true);
				editor.graph.setAllowDanglingEdges(false);
				editor.graph.setMultigraph(true);

				// Validate the characteristics of shapes and their connections
				EditorFunctions.validation(editor.graph);

				RegisterViewerActions(editor);

				// Enables rubberband selection
				new mxRubberband(editor.graph);

				// Installs automatic validation (use editor.validation = true
				// if you are using an mxEditor instance)
				var listener = function(sender, evt) {
					editor.graph.validateGraph();
					editorMain.prototype.modified = true;
				};

				editor.graph.getModel().addListener(mxEvent.CHANGE, listener);

				// Removes cells when [DELETE] is pressed
				var keyHandler = new mxKeyHandler(editor.graph);
				keyHandler.bindKey(46, function(evt) {
					editor.execute('deleteCell');
				});

				editor.graph.htmlLabels = true;

				// Set Wrapping by edit a label in one cell
				editor.graph.isWrapping = function(cell) {
					return true;
				};

				var modelID = getQXModelEditor().getModelId();
				if (modelID != '') {
					var outXml = getQXModelEditor().getModelXML();

					var doc = mxUtils.parseXml(outXml);
					var codec = new mxCodec(doc);

					// Update the process Shape
					var processShape = codec
							.getElementById(modelID, "prowimid");
					if (processShape == null) {
						throw ("Could not find model ID " + modelID + " in XML.");
					}

					mxCodec.prototype.setAttribute(processShape, "label",
							EditorProxy.getName(modelID));
					mxCodec.prototype.setAttribute(processShape, "description",
							EditorProxy.getDescription(modelID));

					codec.decode(doc.documentElement, editor.graph.getModel());

					// Rewrite the model with new values
					EditorFunctions.initModel(editor, codec, modelID);

					editor.graph.getView().refresh();
					editorMain.prototype.modified = false;

					// connect to life cycle chein
					editor.graph.addListener(mxEvent.CLICK, function(sender,
							evt) {
						getQXModelEditor().handleEditorClick();
					});

					// Override some methods of the graph to make it read-only
					// Check this, if anything is missing:
					// http://api.ebcot.info/mxgraph/docs/js-api/files/view/mxGraph-js.html#mxGraph.Graph_behaviour
					// Concept is from
					// http://api.ebcot.info/mxgraph/javascript/examples/permissions.html
					editor.graph.isCellDisconnectable = function(cell,
							terminal, source) {
						return false;
					};
					editor.graph.isCellBendable = function(cell) {
						return false;
					};
					editor.graph.isLabelMovable = function(cell) {
						return false;
					};
					editor.graph.isCellMovable = function(cell) {
						return false;
					};
					editor.graph.isCellResizable = function(cell) {
						return false;
					};
					editor.graph.isCellEditable = function(cell) {
						return false;
					};
					editor.graph.isCellDeletable = function(cell) {
						return false;
					};
					editor.graph.isCellCloneable = function(cell) {
						return false;
					};
					editor.graph.isCellLocked = function(cell) {
						return true;
					};
					editor.graph.isValidSource = function(cell) {
						return false;
					};

				}

				// Action show infos about the cell
				ViewerListener.registerClickListener(editor);

				ApplicationEventsHandler.setEditor(editor);

				// Shows the application
				hideSplash();
			}
		} catch (e) {
			hideSplash();
			throw e; // for debugging
		}

		return editor;
	}

}
