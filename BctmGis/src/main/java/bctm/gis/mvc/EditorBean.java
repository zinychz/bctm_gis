package bctm.gis.mvc;

import gis.factory.Graph;
import gis.factory.ShapeFactory;
import gis.factory.ShapeNodeFactory;
import gis.orm.Gisgraphs;
import gis.orm.GisgraphsDao;
import gis.shapes.AbstractShape;
import gis.shapes.Ellipse;
import gis.shapes.Line;
import gis.shapes.Polygon;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

@Named("editor")
@Scope("session")
public class EditorBean implements Serializable {
	@Inject
	@Named("gisDao")
	private GisgraphsDao gisDao;

	@Inject
	@Named("shapeFactory")
	private ShapeFactory shapeFactory;

	@Inject
	@Named("shapeNodeFactory")
	private ShapeNodeFactory shapeNodeFactory;

	@Inject
	@Named("mapFind")
	private MapFindBean mapFindBean;

	private GisBean gisBean;

	public enum Mode {
		VIEW("Просмотр"), EDIT("Редактировать текущий"), DELETE("Удалить текущий"), NEW("Новый объект"), COPY(
				"Копия текущего");

		private final String mode;

		public String getMode() {
			return mode;
		}

		Mode(String mode) {
			this.mode = mode;
		}

		@Override
		public String toString() {
			return mode;
		}
	}

	private Mode currentMode = Mode.VIEW;

	private Graph createEditShape = null;
	private String createEditShapeType = "";
	private String createEditShapeName = "";
	private String createEditShapeInfo = "";

	private boolean isChangedFromMapFindBean = false;

	private Map<String, String> mappingTypes;

	public void setCreateEditShapeName(String createEditShapeName) {
		this.createEditShapeName = createEditShapeName;
	}

	public boolean getSaveButtonDisabled() {
		if (currentMode == null || currentMode.equals(Mode.VIEW))
			return true;
		if (createEditShapeType == null || createEditShapeType.trim().equals("")
				|| (createEditShape == null && !createEditShapeType.trim().equals("zzzgrp:")))
			return true;
		return false;
	}

	public void save(AjaxBehaviorEvent event) {
		if (currentMode.equals(Mode.NEW) || currentMode.equals(Mode.COPY)) {

			Gisgraphs gNew = new Gisgraphs();
			gNew.setCoord(createEditShapeInfo);
			gNew.setName(createEditShapeName);
			gNew.setType(createEditShapeType);
			gNew.setPriority(0l);

			Long idNew = gisDao.add(gNew);

			if (idNew != null) {
				if (createEditShapeName == null || createEditShapeName.trim().equals("")) {
					createEditShapeName = "Без имени" + idNew;
					gNew = gisDao.getById(idNew);
					gNew.setName(createEditShapeName);
					// if (!gisDao.update(gNew)) {
					// idNew = null;
					// }
					gisDao.update(gNew);
				}
			}
			if (idNew != null) {

				setCreateEditShapeType(createEditShapeType);
				createEditShapeInfo = "";
				gisBean.setListAll(null);

				mapFindBean.setCurrentId(idNew);

				isChangedFromMapFindBean = false;
			}
		} else if (currentMode.equals(Mode.EDIT)) {

			Gisgraphs gEdit = gisDao.getById(mapFindBean.getCurrentId());
			if (gEdit != null) {
				gEdit.setCoord(createEditShapeInfo);
				if (createEditShapeName == null || createEditShapeName.trim().equals("")) {
					createEditShapeName = "Без имени" + gEdit.getId();
				}
				gEdit.setName(createEditShapeName);
				gEdit.setType(createEditShapeType);
				// gEdit.setPriority(0l);

				if (gisDao.update(gEdit)) {
					shapeFactory.removeFromCache(mapFindBean.getCurrentId());
					shapeNodeFactory.removeFromCache(mapFindBean.getCurrentId());
					mapFindBean.focusOnShape(gEdit);

					isChangedFromMapFindBean = false;
				}
			}
		} else if (currentMode.equals(Mode.DELETE)) {

			Gisgraphs gDelete = gisDao.getById(mapFindBean.getCurrentId());
			if (gDelete != null) {
				if (gisDao.delete(gDelete)) {
					shapeFactory.removeFromCache(mapFindBean.getCurrentId());
					shapeNodeFactory.removeFromCache(mapFindBean.getCurrentId());
					createEditShapeInfo = "";
					createEditShape = null;
					gisBean.setListAll(null);
					mapFindBean.focusOnShape(null);

					isChangedFromMapFindBean = false;
				}
			}
		}
	}

	public void setCreateEditShapeType(String createEditShapeType) {

		this.createEditShapeType = createEditShapeType;

		if (currentMode.equals(Mode.VIEW))
			return;

		if (createEditShapeType == null || createEditShapeType.equals("")
				|| ((currentMode.equals(Mode.EDIT) || currentMode.equals(Mode.DELETE) || currentMode.equals(Mode.COPY))
						&& mapFindBean.getCurrentId() <= 0)) {
			createEditShapeInfo = "";
			this.createEditShapeType = "";
			createEditShape = null;
			return;
		}

		List<Point2D> coordinates = new ArrayList<Point2D>();

		if (currentMode.equals(Mode.NEW)) {

			createEditShape = shapeFactory.createShape(-1 * createEditShapeType.hashCode(), createEditShapeType, 0,
					"Creating", null, false);

		} else if ((currentMode.equals(Mode.EDIT) || currentMode.equals(Mode.DELETE) || currentMode.equals(Mode.COPY))
				&& mapFindBean.getCurrentId() > 0) {

			Gisgraphs g = gisDao.getById(mapFindBean.getCurrentId());
			createEditShape = shapeFactory.createShape(currentMode.equals(Mode.COPY) ? -g.getId() : g.getId(),
					createEditShapeType, g.getPriority(), g.getName(), g.getCoord(), false);

			createEditShapeInfo = "";

			if (createEditShape != null) {
				coordinates = createEditShape.getCoordinates();

				createEditShapeInfo = g.getCoord();
				if (createEditShapeInfo != null && !createEditShapeInfo.equals("")
						&& createEditShapeInfo.lastIndexOf(',') == createEditShapeInfo.length() - 1) {
					createEditShapeInfo = createEditShapeInfo.substring(0, createEditShapeInfo.length() - 1);
				}
			}
		}

		if (createEditShape != null) {

			final double scale;
			String windowInfo = gisBean.getWindowInfo();
			if (windowInfo != null && !windowInfo.equals("")) {
				String[] arrWindowInfo = windowInfo.split(",");
				if (arrWindowInfo.length > 4) {
					scale = Double.parseDouble(arrWindowInfo[4]);
				} else {
					scale = 1;
				}
			} else {
				scale = 1;
			}

			((Line) createEditShape).setCustomAttributes(new HashMap<String, String>() {
				{
					if (currentMode.equals(Mode.NEW) || currentMode.equals(Mode.EDIT)
							|| currentMode.equals(Mode.COPY)) {
						put("opacity", "0.5");
						put("onmouseover", "\"setAttr(cr_ed_Points,'stroke','black');\"");
						put("onmouseout", "\"setAttr(cr_ed_Points,'stroke','red');\"");
						put("onmousedown", "\"captureFromEditElement(evt);\"");
					} else if (currentMode.equals(Mode.DELETE)) {
						put("stroke-dasharray", "\"" + 5 / scale + "," + 5 / scale + "\"");
					}
				}
			});

			if ((createEditShape instanceof Ellipse) && coordinates.size() > 1) {
				Point2D last = coordinates.get(coordinates.size() - 1);
				coordinates.clear();
				coordinates.add(last);
			}

			((AbstractShape) createEditShape).setCoordinates(coordinates);
		}
	}

	private Line getLinePhantom(final long id, final String typeId, final List<Point2D> points) {
		return (Line) shapeFactory.createCustomShape(Line.class, new HashMap<String, Object>() {
			{
				put("id", id);
				put("typeId", typeId);
				put("strokeWidth", "1");
				put("stroke", "red");
				put("fill", "none");
				if (points != null) {
					put("coordinates", points);
				}

				if (typeId.equals("phl") || currentMode.equals(Mode.EDIT) || currentMode.equals(Mode.COPY)) {
					put("customAttributes", new HashMap<String, String>() {
						{
							put("onmouseover", "\"showCrossPoint(evt.target);\"");
						}
					});
				}
			}
		});
	}

	private Ellipse getEllipsePhantom(final long id, final String typeId, final List<Point2D> points) {

		final double scale;
		String windowInfo = gisBean.getWindowInfo();
		if (windowInfo != null && !windowInfo.equals("")) {
			String[] arrWindowInfo = windowInfo.split(",");
			if (arrWindowInfo.length > 4) {
				scale = Double.parseDouble(arrWindowInfo[4]);
			} else {
				scale = 1;
			}
		} else {
			scale = 1;
		}

		return (Ellipse) shapeFactory.createCustomShape(Ellipse.class, new HashMap<String, Object>() {
			{
				put("id", id);
				put("typeId", typeId);
				put("rx", 2);
				put("ry", 2);
				put("strokeWidth", "1");
				put("stroke", "red");
				put("fill", "red");
				if (points != null) {
					put("coordinates", points);
				}

				put("customAttributes", new HashMap<String, String>() {
					{
						put("onmouseover", "\"evt.target.setAttribute('rx', '" + 3 / scale
								+ "'); evt.target.setAttribute('ry', '" + 3 / scale
								+ "'); evt.target.setAttribute('stroke', 'black');  evt.target.setAttribute('fill', 'yellow');\"");
						put("onmouseout", "\"evt.target.setAttribute('rx', '" + 2 / scale
								+ "'); evt.target.setAttribute('ry', '" + 2 / scale
								+ "'); evt.target.setAttribute('stroke', 'red');  evt.target.setAttribute('fill', 'red');\"");

						put("opacity", "0.8");

						put("onmousedown", "\"captureFromEditElement(evt);\"");
						put("ondblclick", "\"requestForDelete(evt);\"");
						put("onclick", "\"requestForDelete(evt);\"");
					}
				});
			}
		});
	}

	@PostConstruct
	public void init() {
		mapFindBean.setEditorBean(this);

		mappingTypes = new HashMap<String, String>();
		for (Entry<String, AbstractShape> e : shapeFactory.getMapping().entrySet()) {
			mappingTypes.put(e.getKey(), e.getValue().getTypeName());
		}

		mappingTypes.put("zzzgrp:", "Группа объектов");
	}

	public String getCreateEditShapeBody() {
		double scale = 1;
		String windowInfo = gisBean.getWindowInfo();
		if (windowInfo != null && !windowInfo.equals("")) {
			String[] arrWindowInfo = windowInfo.split(",");
			if (arrWindowInfo.length > 4) {
				scale = Double.parseDouble(arrWindowInfo[4]);
			}
		}
		return createEditShape == null ? null
				: currentMode.equals(Mode.NEW) ? createEditShape.getCreateTemplateBody(scale)
						: currentMode.equals(Mode.EDIT) ? createEditShape.getBody(scale)
								: currentMode.equals(Mode.DELETE) ? createEditShape.getBody(scale)
										: currentMode.equals(Mode.COPY) ? createEditShape.getBody(scale) : null;
	}

	public String getCreateEditPhantoms() {

		if (currentMode.equals(Mode.VIEW) || currentMode.equals(Mode.DELETE) || createEditShape == null) {
			return "";
		}

		final List<Point2D> points = createEditShape.getCoordinates();
		StringBuilder sb = new StringBuilder("");

		double scale = 1;
		String windowInfo = gisBean.getWindowInfo();
		if (windowInfo != null && !windowInfo.equals("")) {
			String[] arrWindowInfo = windowInfo.split(",");
			if (arrWindowInfo.length > 4) {
				scale = Double.parseDouble(arrWindowInfo[4]);
			}
		}

		if (!(createEditShape instanceof Ellipse)) {
			for (long id = 0; id < points.size() - 1; id++) {
				final int index = (int) id;
				sb.append(getLinePhantom(id + 1, "phl", new ArrayList<Point2D>() {
					{
						add(points.get(index));
						if (index + 1 < points.size()) {
							add(points.get(index + 1));
						}
					}
				}).getBody(scale)).append("\n");
			}

			if (points.size() > 0) {

				long id = 1;
				if (currentMode.equals(Mode.EDIT) || currentMode.equals(Mode.COPY)) {
					id = points.size();
				}
				sb.append(getLinePhantom(id, "fly", new ArrayList<Point2D>() {
					{
						add(createEditShape.getCoordinates().get(createEditShape.getCoordinates().size() - 1));

						if (createEditShape instanceof Polygon) {
							add(createEditShape.getCoordinates().get(0));
						}
					}
				}).getBody(scale)).append("\n");
			}
		}

		long idCurrentPoint = 1;

		for (final Point2D point : points) {
			sb.append(getEllipsePhantom(idCurrentPoint, "php", new ArrayList<Point2D>() {
				{
					add(point);
				}
			}).getBody(scale)).append("\n");

			idCurrentPoint++;
		}

		return sb.toString();
	}

	public String getCreateEditShapeInfo() {
		return createEditShapeInfo;
	}

	public boolean isChangedFromMapFindBean() {
		return isChangedFromMapFindBean;
	}

	public void setChangedFromMapFindBean(boolean isChangedFromMapFindBean) {
		this.isChangedFromMapFindBean = isChangedFromMapFindBean;
	}

	public void setCreateEditShapeInfo(String createEditShapeInfo) {
		if (!isChangedFromMapFindBean) {
			this.createEditShapeInfo = createEditShapeInfo;
			if (createEditShape != null) {

				createEditShape.parseCoordinates(createEditShapeInfo);

			}
		}
		isChangedFromMapFindBean = false;
	}

	private List<Mode> modes = new ArrayList<Mode>() {
		{
			add(Mode.VIEW);
			add(Mode.EDIT);
			add(Mode.NEW);
			add(Mode.COPY);
			add(Mode.DELETE);
		}
	};

	public String getStyle() {
		return currentMode.equals(Mode.VIEW) ? "contentGis" : "contentGisEdit";
	}

	public String getCurrentModeCode() {
		return currentMode == null ? "" : currentMode.name();
	}

	// ---------------------------------------------------------------------

	public GisBean getGisBean() {
		return gisBean;
	}

	public void setGisBean(GisBean gisBean) {
		this.gisBean = gisBean;
	}

	public Graph getCreateEditShape() {
		return createEditShape;
	}

	public void setCreateEditShape(Graph createEditShape) {
		this.createEditShape = createEditShape;
	}

	public Mode getCurrentMode() {
		return currentMode;
	}

	public void setCurrentMode(Mode currentMode) {
		this.currentMode = currentMode;
		if (currentMode.equals(Mode.VIEW)) {
			createEditShape = null;
		} else
			// if ((currentMode.equals(Mode.EDIT) ||
			// currentMode.equals(Mode.DELETE)) && mapFindBean.getCurrentId() >
			// 0) {
			if ((currentMode.equals(Mode.EDIT) || currentMode.equals(Mode.DELETE) || currentMode.equals(Mode.COPY))) {
			setCreateEditShapeType(mapFindBean.getCurrentType());
			setCreateEditShapeName(
					mapFindBean.getCurrentSelection() == null ? "" : mapFindBean.getCurrentSelection().toString());
		} else if (currentMode.equals(Mode.NEW)) {
			setCreateEditShapeType(createEditShapeType);
		}
	}

	public List<Mode> getModes() {
		return modes;
	}

	public void setModes(List<Mode> modes) {
		this.modes = modes;
	}

	public String getCreateEditShapeType() {
		return createEditShapeType;
	}

	public String getCreateEditShapeName() {
		return createEditShapeName;
	}

	public Map<String, String> getMappingTypes() {
		return mappingTypes;
	}

	public EditorBean(GisgraphsDao gisDao, ShapeFactory shapeFactory) {
		this.gisDao = gisDao;
		this.shapeFactory = shapeFactory;
	}

	public EditorBean() {
	}
}
