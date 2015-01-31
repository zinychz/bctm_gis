package gis.shapes;

import java.awt.geom.Point2D;
import java.util.List;

public class Line extends AbstractShape
{
	protected String fill = "none";
	protected String fillText = "gray";
	protected String stroke = "black";
	protected String strokeText = "black";

	protected String strokeWidth = "1";
	protected String strokeWidthText = "0";

	protected boolean showLabel = true;
	protected boolean hideNoNameLabel = true;
	protected String noNameLabel = "Без имени";
	protected int showLabelMode = ALONG_LINE_LABEL_MODE;
	protected String beginStrLabel = "уч.";
	protected String endStrLabel = " ";
	protected boolean includeBeginStrLabel = true;
	protected boolean includeEndStrLabel = false;

	protected String placeholderBetween = "\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0";

	protected String fontSize = "12";
	protected String fontFamily = "Arial";
	protected String fontWeight = "normal";
	protected double widthWord = 7.0;
	protected double symbolShift = 2.0;

	@Override
	protected AbstractShape newShape(long id)
	{
		Line shape = new Line();
		shape.id = id;
		initParam(shape);

		return shape;
	}

	protected void initParam(Line shape)
	{
		shape.fill = fill;
		shape.stroke = stroke;
		shape.strokeWidth = strokeWidth;
		shape.fillText = fillText;

		shape.fontWeight = fontWeight;
		shape.strokeText = strokeText;
		shape.strokeWidthText = strokeWidthText;

		shape.showLabel = showLabel;
		shape.hideNoNameLabel = hideNoNameLabel;
		shape.noNameLabel = noNameLabel;
		shape.showLabelMode = showLabelMode;
		shape.beginStrLabel = beginStrLabel;
		shape.endStrLabel = endStrLabel;
		shape.includeBeginStrLabel = includeBeginStrLabel;
		shape.includeEndStrLabel = includeEndStrLabel;

		shape.placeholderBetween = placeholderBetween;

		shape.fontSize = fontSize;
		shape.fontFamily = fontFamily;
		shape.widthWord = widthWord;
		shape.symbolShift = symbolShift;
	}

	@Override
	public String getBody()
	{
		if (coordinates != null && coordinates.size() > 0)
		{
			StringBuilder sb = new StringBuilder();

			sb.append("<polyline id='shp");
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

	@Override
	public String getLabel()
	{
		if (showLabel && name != null && !name.equals(""))
		{
			if (hideNoNameLabel && name.startsWith(noNameLabel))
				return "";

			if (showLabelMode == ALONG_LINE_LABEL_MODE)
			{
				char[] symbols = (name + placeholderBetween).toCharArray();
				String[] labels = new String[coordinates.size() - 1];
				int pos = 0;

				for (int i = 0; i < coordinates.size() - 1; i++)
				{
					Point2D p1 = coordinates.get(i);
					Point2D p2 = coordinates.get(i + 1);

					double x1 = p1.getX();
					double y1 = p1.getY();
					double x2 = p2.getX();
					double y2 = p2.getY();

					double dX = (x2 - x1);
					double dY = (y2 - y1);

					double angle = 180 * Math.atan(dY / dX) / Math.PI;
					double deltaX = widthWord * Math.cos(Math.atan(dY / dX));
					double deltaY = widthWord * Math.sin(Math.atan(dY / dX));
					double lenMax = Math.sqrt(Math.pow(dY, 2) + Math.pow(dX, 2)) / widthWord - 1;

					StringBuilder x = new StringBuilder("");
					StringBuilder y = new StringBuilder("");
					StringBuilder lab = new StringBuilder("");
					double len = lenMax;

					x1 -= symbolShift * Math.sin(Math.atan(dY / dX));
					y1 += symbolShift * Math.cos(Math.atan(dY / dX));

					while (len > 0)
					{
						lab.append(symbols[pos]);

						x.append(x1);
						x.append(",");
						y.append(y1);
						y.append(",");

						x1 += deltaX;
						y1 += deltaY;
						pos = (pos + 1) % symbols.length;
						len--;
					}

					if (!x.toString().equals("") && !y.toString().equals(""))
					{
						labels[i] = "<text id='txt" + id + "' x='" + x.substring(0, x.length() - 1)
								+ "' y='" + y.substring(0, y.length() - 1) + "' rotate='" + angle
								+ "' font-size='" + fontSize + "' font-family='" + fontFamily
								+ "' fill='" + fillText + "' font-weight='" + fontWeight
								+ "' stroke='" + strokeText + "' stroke-width='" + strokeWidthText
								+ "'>" + lab + "</text>";
					}
				}

				String out = "";
				for (String s : labels)
				{
					if (s != null)
						out += s + "\n";
				}
				return out;

			} else if (showLabelMode == ON_LONGEST_LINE_LABEL_MODE)
			{
				if (coordinates.size() > 0)
				{
					int longestLineIndex = 0;
					double maxLen = 0;

					if (coordinates.size() > 2)
					{
						for (int i = 0; i < coordinates.size() - 1; i++)
						{
							Point2D p1 = coordinates.get(i);
							Point2D p2 = coordinates.get(i + 1);
							double len = Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2)
									+ Math.pow(p2.getY() - p1.getY(), 2));
							if (len > maxLen)
							{
								maxLen = len;
								longestLineIndex = i;
							}
						}
					}

					Point2D pCenter = coordinates.get(longestLineIndex);

					if (coordinates.size() > 1)
					{
						Point2D pNext = coordinates.get(longestLineIndex + 1);
						pCenter = new Point2D.Double((pNext.getX() + pCenter.getX()) / 2,
								(pNext.getY() + pCenter.getY()) / 2);
					}

					String lab = name;
					if (beginStrLabel != null && !beginStrLabel.equals(""))
					{
						int beginIndex = lab.indexOf(beginStrLabel);
						if (beginIndex > -1)
						{
							if (includeBeginStrLabel)
								lab = lab.substring(beginIndex);
							else
								lab = lab.substring(beginIndex + beginStrLabel.length());
						}
					}
					if (endStrLabel != null && !endStrLabel.equals(""))
					{
						int endIndex = lab.indexOf(endStrLabel);
						if (endIndex > -1)
						{
							if (includeEndStrLabel)
								lab = lab.substring(0, endIndex + endStrLabel.length());
							else
								lab = lab.substring(0, endIndex);
						}
					}

					if (!lab.equals(""))
					{
						pCenter = new Point2D.Double(pCenter.getX() + symbolShift, pCenter.getY()
								- symbolShift);

						return "<text id='txt" + id + "' x='" + pCenter.getX() + "' y='"
								+ pCenter.getY() + "' font-size='" + fontSize + "' font-family='"
								+ fontFamily + "' fill='" + fillText + "' font-weight='" + fontWeight
								+ "' stroke='" + strokeText + "' stroke-width='" + strokeWidthText
								+ "'>" + lab + "</text>";
					}
				}
			} else if (showLabelMode == INSIDE_RECT_SHAPE_LABEL_MODE)
			{
				if (coordinates.size() > 0)
				{
					double maxX = coordinates.get(0).getX();
					double maxY = coordinates.get(0).getY();
					double minX = maxX;
					double minY = maxY;

					for (int i = 1; i < coordinates.size(); i++)
					{
						Point2D p = coordinates.get(i);

						if (p.getX() > maxX)
							maxX = p.getX();
						if (p.getY() > maxY)
							maxY = p.getY();
						if (p.getX() < minX)
							minX = p.getX();
						if (p.getY() < minY)
							minY = p.getY();
					}

					Point2D pCenter = new Point2D.Double((maxX + minX) / 2, (maxY + minY) / 2);

					String lab = name;
					if (beginStrLabel != null && !beginStrLabel.equals(""))
					{
						int beginIndex = lab.indexOf(beginStrLabel);
						if (beginIndex > -1)
						{
							if (includeBeginStrLabel)
								lab = lab.substring(beginIndex);
							else
								lab = lab.substring(beginIndex + beginStrLabel.length());
						}
					}
					if (endStrLabel != null && !endStrLabel.equals(""))
					{
						int endIndex = lab.indexOf(endStrLabel);
						if (endIndex > -1)
						{
							if (includeEndStrLabel)
								lab = lab.substring(0, endIndex + endStrLabel.length());
							else
								lab = lab.substring(0, endIndex);
						}
					}

					if (!lab.equals(""))
					{
						pCenter = new Point2D.Double(pCenter.getX() + symbolShift, pCenter.getY()
								- symbolShift);

						return "<text id='txt" + id + "' x='" + pCenter.getX() + "' y='"
								+ pCenter.getY() + "' font-size='" + fontSize + "' font-family='"
								+ fontFamily + "' fill='" + fillText + "' font-weight='" + fontWeight
								+ "' stroke='" + strokeText + "' stroke-width='" + strokeWidthText
								+ "'>" + lab + "</text>";
					}
				}
			}
		}

		return "";
	}

	public boolean isShowLabel()
	{
		return showLabel;
	}

	public void setShowLabel(boolean showLabel)
	{
		this.showLabel = showLabel;
	}

	public boolean isHideNoNameLabel()
	{
		return hideNoNameLabel;
	}

	public void setHideNoNameLabel(boolean hideNoNameLabel)
	{
		this.hideNoNameLabel = hideNoNameLabel;
	}

	public String getPlaceholderBetween()
	{
		return placeholderBetween;
	}

	public void setPlaceholderBetween(String placeholderBetween)
	{
		this.placeholderBetween = placeholderBetween;
	}

	public String getFontSize()
	{
		return fontSize;
	}

	public void setFontSize(String fontSize)
	{
		this.fontSize = fontSize;
	}

	public String getFontFamily()
	{
		return fontFamily;
	}

	public void setFontFamily(String fontFamily)
	{
		this.fontFamily = fontFamily;
	}

	public double getWidthWord()
	{
		return widthWord;
	}

	public void setWidthWord(double widthWord)
	{
		this.widthWord = widthWord;
	}

	public double getSymbolShift()
	{
		return symbolShift;
	}

	public void setSymbolShift(double symbolShift)
	{
		this.symbolShift = symbolShift;
	}

	public Line()
	{
	}

	public Line(long id, String type, long priority, String name)
	{
		this.id = id;
		this.type = type;
		this.priority = priority;
		this.name = name;
	}

	public String getFill()
	{
		return fill;
	}

	public void setFill(String fill)
	{
		this.fill = fill;
	}

	public String getStroke()
	{
		return stroke;
	}

	public void setStroke(String stroke)
	{
		this.stroke = stroke;
	}

	public String getStrokeWidth()
	{
		return strokeWidth;
	}

	public void setStrokeWidth(String strokeWidth)
	{
		this.strokeWidth = strokeWidth;
	}

	public String getFillText()
	{
		return fillText;
	}

	public void setFillText(String fillText)
	{
		this.fillText = fillText;
	}

	public String getNoNameLabel()
	{
		return noNameLabel;
	}

	public void setNoNameLabel(String noNameLabel)
	{
		this.noNameLabel = noNameLabel;
	}

	public int getShowLabelMode()
	{
		return showLabelMode;
	}

	public void setShowLabelMode(int showLabelMode)
	{
		this.showLabelMode = showLabelMode;
	}

	public String getBeginStrLabel()
	{
		return beginStrLabel;
	}

	public void setBeginStrLabel(String beginStrLabel)
	{
		this.beginStrLabel = beginStrLabel;
	}

	public String getEndStrLabel()
	{
		return endStrLabel;
	}

	public void setEndStrLabel(String endStrLabel)
	{
		this.endStrLabel = endStrLabel;
	}

	public boolean isIncludeBeginStrLabel()
	{
		return includeBeginStrLabel;
	}

	public void setIncludeBeginStrLabel(boolean includeBeginStrLabel)
	{
		this.includeBeginStrLabel = includeBeginStrLabel;
	}

	public boolean isIncludeEndStrLabel()
	{
		return includeEndStrLabel;
	}

	public void setIncludeEndStrLabel(boolean includeEndStrLabel)
	{
		this.includeEndStrLabel = includeEndStrLabel;
	}

	public String getStrokeText()
	{
		return strokeText;
	}

	public void setStrokeText(String strokeText)
	{
		this.strokeText = strokeText;
	}

	public String getStrokeWidthText()
	{
		return strokeWidthText;
	}

	public void setStrokeWidthText(String strokeWidthText)
	{
		this.strokeWidthText = strokeWidthText;
	}

	public String getFontWeight()
	{
		return fontWeight;
	}

	public void setFontWeight(String fontWeight)
	{
		this.fontWeight = fontWeight;
	}
}
