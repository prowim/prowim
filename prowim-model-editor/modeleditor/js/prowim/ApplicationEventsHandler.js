/**
 An event handler object that implements methods to handle events on the PorWim model editor application.	
 @author Saad Wardi. 
 
 This file is part of ProWim.

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


function ApplicationEventsHandler() {}

/** the editor object. */
ApplicationEventsHandler.editor;

/** the save succeeded message returned to the user. */
ApplicationEventsHandler.SAVE_SUCCESSFULL_MSG = "The model was successfully saved.";

/** the save failed message returned to the user. */
ApplicationEventsHandler.SAVE_FAIL_MSG = "ProWim Application Error: Saving the model has failed!";

/** the on exit text. */
ApplicationEventsHandler.ON_EXIT_MSG = "You are about to close the ProWim model editor, do you want to continue?";

ApplicationEventsHandler.SAVE_FLAG;

ApplicationEventsHandler.oldSource;
ApplicationEventsHandler.oldTarget;
ApplicationEventsHandler.edge;


//////////// Public Methods  //////////////////////////////////////

/** Sets the editor attribute. */
ApplicationEventsHandler.setEditor = function(theEditor){
	ApplicationEventsHandler.editor = theEditor;
};

/** Gets the editor attribute. */
ApplicationEventsHandler.getEditor = function(theEditor){
	return ApplicationEventsHandler.editor;
};

ApplicationEventsHandler.setSaveFlag = function(flag){
	ApplicationEventsHandler.SAVE_FLAG = flag;
};
ApplicationEventsHandler.getSaveFlag = function(){
	return ApplicationEventsHandler.SAVE_FLAG;
};

/** the on exit event. */
ApplicationEventsHandler.onExit = function()
{
	ApplicationEventsHandler.save();
	return ApplicationEventsHandler.ON_EXIT_MSG;
};



/** Call the action to save the changes. */
ApplicationEventsHandler.save = function(){
	ApplicationEventsHandler.editor.execute('saveGraph');
};

/****************************************** bind key events. *******************************/
/** DOWN ARROW. */
ApplicationEventsHandler.bindDownArrow = function(editor, keyHandler){
	keyHandler.bindKey(40, function(evt)
			{
				var cell = editor.graph.getSelectionCell();
				if (cell != null) {

                 	var mxg = cell.getGeometry();
					var tra = mxg.translate(0, 50);
					cell.setGeometry(tra);						
					editor.graph.refresh(cell);
					ApplicationEventsHandler.refresh(editor, cell);
			     }
			});
};


/** UP ARROW. */
ApplicationEventsHandler.bindUpArrow = function(editor, keyHandler){
	// up
	keyHandler.bindKey(38, function(evt)
			{
				var cell = editor.graph.getSelectionCell();
				if (cell != null) {
                 	var mxg = cell.getGeometry();
					var tra = mxg.translate(0, -50);
					cell.setGeometry(tra);						
					editor.graph.refresh(cell);
					ApplicationEventsHandler.refresh(editor, cell);
			     }
			});
};


/** LEFT ARROW. */
ApplicationEventsHandler.bindLeftArrow = function(editor, keyHandler){
    // LEFT
    keyHandler.bindKey(37, function(evt) {
				var cell = editor.graph.getSelectionCell();
				if (cell != null) {
				 	var mxg = cell.getGeometry();
					var tra = mxg.translate(-50, 0);
					cell.setGeometry(tra);						
					editor.graph.refresh(cell);
					ApplicationEventsHandler.refresh(editor, cell);
			    }
	});
};

/** RIGHT ARROW. */
ApplicationEventsHandler.bindRightArrow = function(editor, keyHandler){
	// RIGHT
	keyHandler.bindKey(39, function(evt){
		var cell = editor.graph.getSelectionCell();
				if (cell != null) {
				 	var mxg = cell.getGeometry();
					var tra = mxg.translate(50, 0);
					cell.setGeometry(tra);
					editor.graph.refresh(cell);
					ApplicationEventsHandler.refresh(editor, cell);
			    }
	});
	
};



ApplicationEventsHandler.refresh = function(editor, cell){
	var edgeCount = cell.getEdgeCount();
	if (edgeCount > 0) {
          for (var i = 0; i < edgeCount; i++){
	             editor.graph.refresh(cell.getEdgeAt(i));
	      }
    }
};


ApplicationEventsHandler.bendEdge = function(editor){
	// TODO : Implements bend of the other part of the connections
	
	var selectedCell = editor.graph.getSelectionCell();
	if (selectedCell != undefined && selectedCell.getAttribute('prowimtype') != undefined){
	   if (selectedCell.isEdge()){
		   
		   
	        if (selectedCell.getAttribute('prowimtype') != undefined){
	        	var prowimtype = selectedCell.getAttribute('prowimtype');
	        	
	        	if (prowimtype == "RELATION"){
	        		var source = selectedCell.source.getAttribute('prowimid');
	        		var target = selectedCell.target.getAttribute('prowimid');
	        		
	        		if (target != ApplicationEventsHandler.oldTarget) {
	        		   ApplicationEventsHandler.removeRelation(source, ApplicationEventsHandler.edge, target);
	        		   ApplicationEventsHandler.addRelation(editor,source, ApplicationEventsHandler.edge, target);
	        		} else {
	        			// TODO : Implements bend of the other part of the connections
	        		}
	        	} else if (prowimtype == "CONTROLFLOW"){
	        		var source = selectedCell.source.getAttribute('prowimid');
	        		var target = selectedCell.target.getAttribute('prowimid');
	        		
	        		ApplicationEventsHandler.bendControlFlow(selectedCell,editor,ApplicationEventsHandler.edge, ApplicationEventsHandler.oldSource, ApplicationEventsHandler.oldTarget, source, target);
	        	} else if (prowimtype == "PRODUCT"){
	        		var source = selectedCell.source.getAttribute('prowimid');
	        		var target = selectedCell.target.getAttribute('prowimid');
	        	}
	        }
	    }
	}
};

ApplicationEventsHandler.setSelectedItem = function(edge,oldSource, oldTarget){
	
	ApplicationEventsHandler.oldTarget = oldTarget;
	ApplicationEventsHandler.oldSource = oldSource;
	ApplicationEventsHandler.edge = edge;
};

ApplicationEventsHandler.removeRelation = function(source, relation, target){
	if (source!= undefined && relation != undefined && target!= undefined){
		try {
	           EditorProxy.removeRelationValue(source, relation);
		} catch(exception) {
			editor.execute('undoBend');
		}
	} else {
	}
};

ApplicationEventsHandler.addRelation = function(editor,source, relation, target){
	if (source!= undefined && relation != undefined && target!= undefined){
	     try{
	       EditorProxy.setRelationValue(source, relation, target);
	     } catch(exception) {
	    	 editor.execute('undoBend');
	     }
	}else{ 
	}
};

ApplicationEventsHandler.bendControlFlow = function(cell,editor,edge,oldSource, oldTarget, newSource, newTarget){
	try {
		if (edge != undefined && newSource!= undefined && newTarget!= undefined){
	       EditorProxy.bendControlFlow(edge, oldSource, oldTarget, newSource, newTarget);
		}
	} catch(exception){
		editor.execute('undoBend');
	} 
};

ApplicationEventsHandler.bendProduct = function(cell,editor,edge,oldSource, oldTarget, newSource, newTarget){
	try {
	  EditorProxy.bendControlFlow(edge, oldSource, oldTarget, newSource, newTarget);
	} catch(exception){
		editor.execute('deleteCell');
	} 
};

