package gis.shapes;

import java.awt.geom.Point2D;

public class Ellipse extends Line
{
	protected double rx = 1.5;
	protected double ry = 1.5;
	
	@Override
	protected AbstractShape newShape(long id)
	{
		Ellipse shape = new Ellipse();
		shape.id = id;
		initParam(shape);
		
		shape.rx=rx;
		shape.ry=ry;

		return shape;
	}
	
	@Override
	public String getBody()
	{
		if (coordinates != null && coordinates.size() > 0)
		{
			Point2D p = coordinates.get(0);
			StringBuilder sb = new StringBuilder();
			
			sb.append("<ellipse id='shp");
			sb.append(id);
			sb.append("' cx='");
			
			sb.append(p.getX());
			sb.append("' cy='");
			sb.append(p.getY());
			sb.append("' rx='");
			sb.append(rx);
			sb.append("' ry='");
			sb.append(ry);
			
			sb.append("' style='");
			sb.append("fill:" + fill);
			sb.append(";stroke:" + stroke);
			sb.append(";stroke-width:" + strokeWidth);

			sb.append("' />");
			
			return sb.toString();
		}

		return "";
	}

	public double getRx()
	{
		return rx;
	}

	public void setRx(double rx)
	{
		this.rx = rx;
	}

	public double getRy()
	{
		return ry;
	}

	public void setRy(double ry)
	{
		this.ry = ry;
	}
	
}
