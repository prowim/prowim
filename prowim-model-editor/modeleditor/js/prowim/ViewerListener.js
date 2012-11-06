/**
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
 */

function ViewerListener() {}

// action to rename a cell
ViewerListener.renameCell = function(editor) {
	editor.graph.addListener(mxEvent.LABEL_CHANGED, function(sender, evt) {
		// do nothing --> TODO disable editing in component
	});
};
	ViewerListener.registerClickListener  = function(editor)
	{
		editor.graph.addListener(mxEvent.CLICK, function(sender, evt)
		{
			var cell = editor.graph.getSelectionCell();
	
			// If process elements are selected shows the knowledge object of this. Other wise clean the view 
			// Clean the view if a swimlane or relation is selected.
			if (cell != undefined)
			{
				var prowimType = cell.getAttribute('prowimtype');
				if ((prowimType != "SWIMLANE") && (prowimType != "RELATION") && (prowimType != "PROCESSTYPE_LANDSPACE") && (prowimType != "PROCESS_LANDSPACE"))
				{
					BFShowKnowledge(cell.getAttribute('prowimtype'), cell.getAttribute('prowimid'));
				}
				else
				{
					// Clean the view. 
			    	BFShowKnowledge(cell.getAttribute('prowimtype'), '0');
				}
			}
		});
	};
	
		



