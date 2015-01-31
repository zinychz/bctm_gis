package gis.shapes;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gis.factory.Graph;

public abstract class AbstractShape implements Graph
{
	protected long id;
	protected String type;
	protected long priority;
	protected String name;

	protected List<Point2D> coordinates;

	protected final static int ALONG_LINE_LABEL_MODE = 1;
	protected final static int ON_LONGEST_LINE_LABEL_MODE = 2;
	protected final static int INSIDE_RECT_SHAPE_LABEL_MODE = 3;

	public AbstractShape newInstance(long id, String type, long priority, String name)
	{
		AbstractShape shape = newShape(id);

		// shape.id = id;
		shape.type = type;
		shape.priority = priority;
		shape.name = name;

		return shape;
	}

	protected abstract AbstractShape newShape(long id);

	public List<Point2D> getCoordinates()
	{
		return coordinates;
	}

	public void setCoordinates(List<Point2D> coordinates)
	{
		this.coordinates = coordinates;
	}

	public void parseCoordinates(String coord)
	{
		if (coordinates == null)
		{
			coordinates = new ArrayList<Point2D>();
		} else
		{
			coordinates.clear();
		}

		if (coord != null && !coord.equals(""))
		{
			String[] arrayXY = coord.split(",");
			for (int i = 0; i < arrayXY.length; i += 2)
			{
				coordinates.add(new Point2D.Double(Double.parseDouble(arrayXY[i]), Double
						.parseDouble(arrayXY[i + 1])));
			}
		}
	}

	public abstract String getBody();

	public abstract String getLabel();

	public String getWrapLinkBody()
	{
		if (getBody().equals(""))
		{
			return "";
		}

		return "<a xlink:href='#' onclick='getShapeFromLink(this.firstChild.id); return false'>"
				+ getBody() + "</a>";
	}

	public String getWrapLinkLabel()
	{
		if (getLabel().equals(""))
		{
			return "";
		}

		return "<a xlink:href='#' onclick='getShapeFromLink(this.firstChild.id); return false'>"
				+ getLabel() + "</a>";
	}
}
