package gis.factory;

import java.awt.geom.Point2D;
import java.util.List;

public interface Graph
{
	public void parseCoordinates(String coord);
	public String getBody();
	public String getLabel();
	public String getWrapLinkBody();
	public String getWrapLinkLabel();
	public List<Point2D> getCoordinates();
}
