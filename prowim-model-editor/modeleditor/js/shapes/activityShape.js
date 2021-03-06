/**
 * $Id: mxActivity.js,v 1.6 2009/07/10 14:29:21 Maziar Khodaei $ Copyright (c)
 * 2009, Ebcot GmbH
 * 
 * Class: ActivityShape
 * 
 * Extends <mxShape> to implement a activity shape. This shape is registered
 * under <mxConstants.SHAPE_ACTIVITY> in <mxCellRenderer>.
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
 */
{
	/**
	 * Constructor: ActivityShape
	 * 
	 * Constructs a new rectangle shape.
	 */
	function ActivityShape(bounds, fill, stroke, strokewidth) {
		this.bounds = bounds;
		this.fill = fill;
		this.stroke = stroke;
		this.strokewidth = strokewidth || 1;
	}
	;

	/**
	 * Extends mxShape.
	 */
	ActivityShape.prototype = new mxActor();
	ActivityShape.prototype.constructor = ActivityShape;

	/**
	 * Function: redrawPath
	 * 
	 * Draws the path for this shape. This method uses the <mxPath> abstraction
	 * to paint the shape for VML and SVG.
	 */
	ActivityShape.prototype.redrawPath = function(path, x, y, w, h) {
		path.moveTo(0 * w, 0);
		path.lineTo(0.75 * w, 0);
		path.lineTo(w, 0.5 * h);
		path.lineTo(0.75 * w, h);
		path.lineTo(0 * w, h);
		path.lineTo(w * 0.25, 0.5 * h);
		path.lineTo(0.25 * w, 0.5 * h);

		path.close();
	};

	mxCellRenderer.prototype.defaultShapes['activityShape'] = ActivityShape;
}
