/**
 * $Id: mxActivity.js,v 1.6 2010/03/30 Maziar Khodaei $
 * Copyright (c) 2009-2010, Ebcot GmbH
 *
 * Class: ActivityNestingShape
 *
 * Extends <mxShape> to implement a sub process shape.
 * This shape is registered under <mxConstants.SHAPE_ACTIVITY>
 * in <mxCellRenderer>.
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
	 * Constructor: subProcessShape
	 *
	 * Constructs a new subProcessShape.
	 */
	function subProcessShape(bounds, fill, stroke, strokewidth)
	{
		this.bounds = bounds;
		this.fill = fill;
		this.stroke = stroke;
		this.strokewidth = strokewidth || 1;
	};

	/**
	 * Extends mxShape.
	 */
	subProcessShape.prototype = new mxActor();
	subProcessShape.prototype.constructor = subProcessShape;

	/**
	 * Function: redrawPath
	 *
	 * Draws the path for this shape. This method uses the <mxPath>
	 * abstraction to paint the shape for VML and SVG.
	 */
	subProcessShape.prototype.redrawPath = function(path, x, y, w, h)
	{
		
		path.moveTo(0* w, 0 *h);
		path.lineTo( w ,0 * h);
		path.lineTo( w , h);
	    path.lineTo(0.60 * w , h);
		path.lineTo(0.60 * w ,0.8* h);
		path.lineTo( 0.4*w ,0.8* h);
		path.lineTo( 0.4*w ,1* h);
		path.lineTo( 0*w ,1* h);
		path.moveTo( 0.58*w ,0.98* h);
		path.lineTo( 0.42*w ,0.82* h);
		path.moveTo( 0.58*w ,0.82* h);
		path.lineTo( 0.42*w ,0.98* h);
		path.moveTo( 0.4*w ,1* h);
		path.close();
	};
	
	mxCellRenderer.prototype.defaultShapes['subProcessShape']=subProcessShape;
}
