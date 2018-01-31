package gis.shapes;

import java.awt.geom.Point2D;
import java.util.Map;

public class Ellipse extends Line {
	protected double rx = 1.5;
	protected double ry = 1.5;

	@Override
	protected AbstractShape newShape(long id) {
		Ellipse shape = new Ellipse();
		shape.id = id;
		initParam(shape);

		shape.rx = rx;
		shape.ry = ry;

		return shape;
	}

	protected String createBody(String _strokeWidth) {

		Point2D p = null;

		if (coordinates != null && coordinates.size() > 0) {
			p = coordinates.get(0);
		}

		// StringBuilder sb = new StringBuilder("<ellipse
		// id='").append(typeId).append(id).append("' cx='")
		// .append(p == null ? "" : p.getX()).append("' cy='").append(p == null
		// ? "" : p.getY()).append("' rx='")
		// .append(rx).append("' ry='").append(ry).append("'
		// style='").append("fill:" + fill)
		// .append(";stroke:" + stroke).append(";stroke-width:" +
		// _strokeWidth).append("'");
		StringBuilder sb = new StringBuilder("<ellipse id='").append(typeId).append(id).append("' cx='")
				.append(p == null ? "" : p.getX()).append("' cy='").append(p == null ? "" : p.getY()).append("' rx='")
				.append(rx).append("' ry='").append(ry).append("' fill='").append(fill).append("' stroke='")
				.append(stroke).append("' stroke-width='").append(_strokeWidth).append("'");

		if (customAttributes != null) {
			for (Map.Entry<String, String> entry : customAttributes.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (key != null && !key.equals("") && value != null && !value.equals("")) {
					sb.append(" ").append(key).append("=").append(value);
				}
			}
		}

		sb.append(" />");

		return sb.toString();
	}

	protected String createBody(String _strokeWidth, double _rx, double _ry) {

		Point2D p = null;

		if (coordinates != null && coordinates.size() > 0) {
			p = coordinates.get(0);
		}

		// StringBuilder sb = new StringBuilder("<ellipse
		// id='").append(typeId).append(id).append("' cx='")
		// .append(p == null ? "" : p.getX()).append("' cy='").append(p == null
		// ? "" : p.getY()).append("' rx='")
		// .append(_rx).append("' ry='").append(_ry).append("'
		// style='").append("fill:" + fill)
		// .append(";stroke:" + stroke).append(";stroke-width:" +
		// _strokeWidth).append("'");
		StringBuilder sb = new StringBuilder("<ellipse id='").append(typeId).append(id).append("' cx='")
				.append(p == null ? "" : p.getX()).append("' cy='").append(p == null ? "" : p.getY()).append("' rx='")
				.append(_rx).append("' ry='").append(_ry).append("' fill='").append(fill).append("' stroke='")
				.append(stroke).append("' stroke-width='").append(_strokeWidth).append("'");

		if (customAttributes != null) {
			for (Map.Entry<String, String> entry : customAttributes.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (key != null && !key.equals("") && value != null && !value.equals("")) {
					sb.append(" ").append(key).append("=").append(value);
				}
			}
		}

		sb.append(" />");

		return sb.toString();
	}

	@Override
	public String getBody(double scale) {
		if (coordinates != null && coordinates.size() > 0) {
			if (propertiesKeepingNaturalScale != null) {
				return createBody(
						propertiesKeepingNaturalScale.contains("strokeWidth") ? strokeWidth
								: Double.parseDouble(strokeWidth) / scale + "",
						propertiesKeepingNaturalScale.contains("rx") ? rx : rx / scale,
						propertiesKeepingNaturalScale.contains("ry") ? ry : ry / scale);
			}
			return createBody(Double.parseDouble(strokeWidth) / scale + "", rx / scale, ry / scale);
		}
		return "";
	}

	public double getRx() {
		return rx;
	}

	public void setRx(double rx) {
		this.rx = rx;
	}

	public double getRy() {
		return ry;
	}

	public void setRy(double ry) {
		this.ry = ry;
	}

	@Override
	public Point2D getLtPoint() {
		return new Point2D.Double(ltPoint.x - rx, ltPoint.y - ry);
	}

	@Override
	public Point2D getRbPoint() {
		return new Point2D.Double(rbPoint.x + rx, rbPoint.y + ry);
	}

}
