package gis.shapes;

import java.awt.geom.Point2D;
import java.util.Map;

public class Polygon extends Line {
	@Override
	protected AbstractShape newShape(long id) {
		Polygon shape = new Polygon();
		shape.id = id;
		initParam(shape);

		return shape;
	}

	protected String createBody(String _strokeWidth) {

		StringBuilder sb = new StringBuilder();
		sb.append("<polygon id='").append(typeId).append(id).append("' points='");

		if (coordinates != null && coordinates.size() > 0) {
			for (Point2D p : coordinates) {
				sb.append(p.getX()).append(",").append(p.getY()).append(" ");
			}
		}

		// sb.append("' style='").append("fill:" + fill).append(";stroke:" +
		// stroke)
		// .append(";stroke-width:" + _strokeWidth).append("'");
		sb.append("' fill='").append(fill).append("' stroke='").append(stroke).append("' stroke-width='")
				.append(_strokeWidth).append("'");

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
}
