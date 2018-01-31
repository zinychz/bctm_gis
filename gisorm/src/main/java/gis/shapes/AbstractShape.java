package gis.shapes;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gis.factory.Graph;

public abstract class AbstractShape implements Graph {

	protected long id;
	protected String type;
	protected String typeName;
	protected long priority;

	protected String name;

	protected Point2D.Double ltPoint = new Point2D.Double(Double.MIN_VALUE, Double.MIN_VALUE);
	protected Point2D.Double rbPoint = new Point2D.Double(Double.MAX_VALUE, Double.MAX_VALUE);

	protected List<Point2D> coordinates;

	protected final static int ALONG_LINE_LABEL_MODE = 1;
	protected final static int ON_LONGEST_LINE_LABEL_MODE = 2;
	protected final static int INSIDE_RECT_SHAPE_LABEL_MODE = 3;

	public AbstractShape newInstance(long id, String type, long priority, String name) {
		AbstractShape shape = newShape(id);

		// shape.id = id;
		shape.type = type;
		shape.priority = priority;
		shape.name = name;

		if (shape.typeName == null || shape.typeName.equals("")) {
			shape.typeName = shape.type + shape.getClass().getName();
		}

		return shape;
	}

	public Point2D getLtPoint() {
		return ltPoint;
	}

	public Point2D getRbPoint() {
		return rbPoint;
	}

	public List<Point2D> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Point2D> coordinates) {
		this.coordinates = coordinates;
	}

	public void parseCoordinates(String coord) {
		if (coordinates == null) {
			coordinates = new ArrayList<Point2D>();
		} else {
			coordinates.clear();
		}

		if (coord != null && !coord.equals("")) {
			String[] arrayXY = coord.split(",");
			double minX = ltPoint.x, minY = ltPoint.y, maxX = rbPoint.x, maxY = rbPoint.y;
			for (int i = 0; i < arrayXY.length; i += 2) {
				double x = Double.parseDouble(arrayXY[i]);
				double y = Double.parseDouble(arrayXY[i + 1]);
				if (i == 0) {
					minX = maxX = x;
					minY = maxY = y;
				} else {
					if (x < minX)
						minX = x;
					if (y < minY)
						minY = y;
					if (x > maxX)
						maxX = x;
					if (y > maxY)
						maxY = y;
				}
				coordinates.add(new Point2D.Double(x, y));
			}
			ltPoint.setLocation(minX, minY);
			rbPoint.setLocation(maxX, maxY);
		}
	}

	protected abstract AbstractShape newShape(long id);

	public abstract String getBody();

	public abstract String getLabel();

	public abstract String getBody(double scale);

	public abstract String getLabel(double scale);

	public abstract boolean isShowBodyForScale(double scale);

	public abstract boolean isShowLabelForScale(double scale);

	public abstract String getCreateTemplateBody(double scale);

	public String getWrapLinkBody() {
		if (getBody().equals("")) {
			return "";
		}

		return "<a xlink:href='#' onmousedown='setRequestShapeOnClick(this.firstChild.id); return false;' onclick='getShapeFromLink(this.firstChild.id); return false;'>"
				+ getBody() + "</a>";
	}

	public String getWrapLinkLabel() {
		if (getLabel().equals("")) {
			return "";
		}

		return "<a xlink:href='#' onmousedown='setRequestShapeOnClick(this.firstChild.id); return false;' onclick='getShapeFromLink(this.firstChild.id); return false;'>"
				+ getLabel() + "</a>";
	}

	public String getWrapLinkBody(double scale) {
		if (getBody(scale).equals("")) {
			return "";
		}

		return "<a xlink:href='#' onmousedown='setRequestShapeOnClick(this.firstChild.id); return false;' onclick='getShapeFromLink(this.firstChild.id); return false;'>"
				+ getBody(scale) + "</a>";
	}

	public String getWrapLinkLabel(double scale) {
		if (getLabel(scale).equals("")) {
			return "";
		}

		return "<a xlink:href='#' onmousedown='setRequestShapeOnClick(this.firstChild.id); return false;' onclick='getShapeFromLink(this.firstChild.id); return false;'>"
				+ getLabel(scale) + "</a>";
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getType() {
		return type;
	}
}
