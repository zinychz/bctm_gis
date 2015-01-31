package gis.shapes;

import java.awt.geom.Point2D;

public class Polygon extends Line
{
	@Override
	protected AbstractShape newShape(long id)
	{
		Polygon shape = new Polygon();
		shape.id = id;
		initParam(shape);

		return shape;
	}
	
	@Override
	public String getBody()
	{
		if (coordinates != null && coordinates.size() > 0)
		{
			StringBuilder sb = new StringBuilder();

			sb.append("<polygon id='shp");
			sb.append(id);
			sb.append("' points='");

			for (Point2D p : coordinates)
			{
				sb.append(p.getX() + "," + p.getY() + " ");
			}

			sb.append("' style='");

			sb.append("fill:" + fill);
			sb.append(";stroke:" + stroke);
			sb.append(";stroke-width:" + strokeWidth);

			sb.append("' />");

			return sb.toString();
		}

		return "";
	}
}
