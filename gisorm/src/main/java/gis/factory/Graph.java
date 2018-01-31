package gis.factory;

import java.awt.geom.Point2D;
import java.util.List;

public interface Graph
{
	public void parseCoordinates(String coord);
	public String getBody();
	public String getLabel();
	public String getBody(double scale);
	public String getLabel(double scale);
	public String getWrapLinkBody();
	public String getWrapLinkLabel();
	public String getWrapLinkBody(double scale);
	public String getWrapLinkLabel(double scale);
	public boolean isShowBodyForScale(double scale);
	public boolean isShowLabelForScale(double scale);
	public List<Point2D> getCoordinates();
	public Point2D getLtPoint();
	public Point2D getRbPoint();
	public String getCreateTemplateBody(double scale);
}
