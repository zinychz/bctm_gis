package bctm.gis.mvc;

import gis.factory.Graph;
import gis.factory.ShapeFactory;
import gis.factory.ShapeNodeFactory;
import gis.orm.Gisgraphs;
import gis.orm.GisgraphsDao;
import gis.orm.Inventories;
import gis.orm.InventoriesDao;
import gis.tree.AbstractGroupNode;
import gis.tree.GisgraphsNode;
import gis.tree.GroupNode;
import gis.tree.ObjectInGroupNode;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.tree.TreeNode;

import org.springframework.context.annotation.Scope;

@Named("mapFind")
// @SessionScoped
// @ViewScoped
@Scope("session") // need this, JSR-330 in Spring context is singleton by
					// default
public class MapFindBean implements Serializable {
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
	@Named("specFind")
	private SpecFindBean specFindBean;

	private EditorBean editorBean;

	// Resolve Circular Dependency
	@PostConstruct
	public void init() {
		specFindBean.setMapFindBean(this);
	}

	@Inject
	@Named("invDao")
	private InventoriesDao invDao;

	private String matchesName = "";
	private boolean caseSensitive;

	private boolean synchronizedSpec = true;

	// private final static String GROUP_TYPE_KEY = "zzzgrp:";
	private final String GROUP_TYPE_KEY = "zzzgrp:";

	private long currentId;
	private Point2D currentPoint;

	private String currentType = "";
	private String children = "";

	private TreeNode currentSelection = null;
	private List<TreeNode> rootNodes = null;

	private int elementsDataGridFoundTree = 5;
	private final int H20 = 692 - 172;
	private final int E20 = 21;

	// -------------------------------------------------------------

	public void setDocumentSizeInfo(String documentSizeInfo) {
		if (documentSizeInfo != null && !documentSizeInfo.equals("")) {
			String[] list = documentSizeInfo.split(",");
			if (list.length >= 2) {
				try {
					int h = Integer.parseInt(list[1]) - 172;
					elementsDataGridFoundTree = h * E20 / H20;
					if (elementsDataGridFoundTree < 1)
						elementsDataGridFoundTree = 1;
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void setUngroupInfo(String ungroupInfo) {
		if (currentSelection != null && ungroupInfo != null && !ungroupInfo.equals("")) {
			String[] ungroupInfoArray = ungroupInfo.split(",");
			if (ungroupInfoArray.length >= 2) {
				String parentRowNum = ungroupInfoArray[0];
				try {
					long gisgraphId = Long.parseLong(ungroupInfoArray[1]);

					if (parentRowNum.equals("0")) {
						if (gisDao.deleteChild(gisDao.getById(gisgraphId), gisDao.getById(currentId))) {
							((GisgraphsNode) currentSelection).getLabels().get(0).setNoInit(true);
							for (TreeNode n : rootNodes) {
								GisgraphsNode gN = (GisgraphsNode) n;
								if (gN.getGisgraphId() == gisgraphId) {
									gN.getLabels().get(1).setNoInit(true);
									break;
								}
							}
							shapeNodeFactory.removeFromCache(gisgraphId);
							shapeNodeFactory.removeFromCache(currentId);
						}
					} else if (parentRowNum.equals("1")) {
						if (gisDao.deleteGroup(gisDao.getById(gisgraphId), gisDao.getById(currentId))) {

							((GisgraphsNode) currentSelection).getLabels().get(1).setNoInit(true);
							for (TreeNode n : rootNodes) {
								GisgraphsNode gN = (GisgraphsNode) n;
								if (gN.getGisgraphId() == gisgraphId) {
									gN.getLabels().get(0).setNoInit(true);
									break;
								}
							}
							shapeNodeFactory.removeFromCache(gisgraphId);
							shapeNodeFactory.removeFromCache(currentId);
						}
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setRootNodes(List<TreeNode> rootNodes) {
		this.rootNodes = rootNodes;
	}

	public boolean isSynchronizedSpec() {
		return synchronizedSpec;
	}

	public void setSynchronizedSpec(boolean synchronizedSpec) {
		this.synchronizedSpec = synchronizedSpec;
	}

	public TreeNode getCurrentSelection() {
		return currentSelection;
	}

	public void setCurrentSelection(TreeNode currentSelection) {
		this.currentSelection = currentSelection;
	}

	public List<TreeNode> getRootNodes() {
		if (rootNodes == null) {
			rootNodes = new ArrayList<TreeNode>();
			for (Gisgraphs g : gisDao.getByLikeName(matchesName, caseSensitive)) {
				rootNodes.add(shapeNodeFactory.getGisgraphsNode(g.getName(), g.getId(), shapeNodeFactory, null));
			}
		}

		return rootNodes;
	}

	public String focusOnShape() {
		return focusOnShapeById(((GisgraphsNode) currentSelection).getGisgraphId());
	}

	// -------------------------------------------------------------

	public StringBuilder selectSimpleFromGroup(Set<Gisgraphs> childrenSet, StringBuilder sb) {
		if (childrenSet != null && childrenSet.size() > 0) {
			Iterator<Gisgraphs> it = childrenSet.iterator();

			while (it.hasNext()) {
				Gisgraphs next = it.next();
				if (next.getType().equals(GROUP_TYPE_KEY)) {
					selectSimpleFromGroup(gisDao.getChildren(next.getId()), sb);
				} else {
					sb.append(next.getId());
					sb.append(" ");
				}
			}
		}
		return sb;
	}

	public String clearCurrent() {
		currentId = -1;
		currentType = "";
		children = "";
		currentPoint = null;
		currentSelection = null;

		if (editorBean.getCurrentMode().equals(EditorBean.Mode.EDIT)
				|| editorBean.getCurrentMode().equals(EditorBean.Mode.DELETE)) {
			editorBean.setChangedFromMapFindBean(true);
			editorBean.setCurrentMode(editorBean.getCurrentMode());
		}

		return null;
	}

	public String focusOnShape(Gisgraphs g) {
		if (g == null) {
			return clearCurrent();
		}

		currentId = g.getId();
		currentType = g.getType();
		children = "";
		currentPoint = null;
		currentSelection = shapeNodeFactory.getGisgraphsNode(g.getName(), g.getId(), shapeNodeFactory, null);

		if (currentType.equals(GROUP_TYPE_KEY)) {
			children = selectSimpleFromGroup(gisDao.getChildren(g.getId()), new StringBuilder("")).toString().trim();

			if (!children.equals("")) {
				long idForPoint = Long.parseLong(children.split(" ")[0]);
				Gisgraphs gPoint = gisDao.getById(idForPoint);
				Graph shape = shapeFactory.createShape(gPoint.getId(), gPoint.getType(), gPoint.getPriority(),
						gPoint.getName(), gPoint.getCoord(), true);
				currentPoint = shape.getCoordinates().get(0);
			}
		} else {
			Graph shape = shapeFactory.createShape(g.getId(), g.getType(), g.getPriority(), g.getName(), g.getCoord(),
					true);
			if (shape.getCoordinates() != null && shape.getCoordinates().size() > 0) {
				currentPoint = shape.getCoordinates().get(0);
			}
		}

		if (editorBean.getCurrentMode().equals(EditorBean.Mode.EDIT)
				|| editorBean.getCurrentMode().equals(EditorBean.Mode.DELETE)
				|| editorBean.getCurrentMode().equals(EditorBean.Mode.COPY)) {
			editorBean.setChangedFromMapFindBean(true);
			editorBean.setCurrentMode(editorBean.getCurrentMode());
		}

		return null;
	}

	public String focusOnShapeById(long id) {
		Gisgraphs g = gisDao.getById(id);
		if (synchronizedSpec) {
			Inventories inventory = null;
			try {
				inventory = invDao.getById(g.getInventory().getId());
			} catch (Exception e) {
			}
			specFindBean.setCurrentInventory(inventory);
		}

		return focusOnShape(g);
	}

	public void setCurrentId(long currentId) {
		focusOnShapeById(currentId);
	}

	// public void setCurrentIdFromTree(long currentId) {
	// if (editorBean.getCurrentMode().equals(EditorBean.Mode.EDIT)) {
	// editorBean.setChangedFromMapFindBean(true);
	// }
	// setCurrentId(currentId);
	// }

	public List<Gisgraphs> getFoundGisgraphs() {
		List<Gisgraphs> foundGisgraphs = new LinkedList<Gisgraphs>();
		foundGisgraphs.addAll(gisDao.getByLikeName(matchesName, caseSensitive));
		return foundGisgraphs;
	}

	public List<String> getFoundNames(String query) {
		List<String> found = new ArrayList<String>();
		for (Gisgraphs g : gisDao.getByLikeName(query, caseSensitive)) {
			found.add(g.getName());
		}
		return found;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public Point2D getCurrentPoint() {
		return currentPoint;
	}

	public void setCurrentPoint(Point2D currentPoint) {
		this.currentPoint = currentPoint;
	}

	public long getCurrentId() {
		return currentId;
	}

	public String getMatchesName() {
		return matchesName;
	}

	public void setMatchesName(String matchesName) {
		this.matchesName = matchesName;
		rootNodes = null;
	}

	public String getCurrentType() {
		return currentType;
	}

	public void setCurrentType(String currentType) {
		this.currentType = currentType;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public EditorBean getEditorBean() {
		return editorBean;
	}

	public void setEditorBean(EditorBean editorBean) {
		this.editorBean = editorBean;
	}

	public String getUngroupInfo() {
		return "";
	}

	public String getDocumentSizeInfo() {
		return "";
	}

	public int getElementsDataGridFoundTree() {
		return elementsDataGridFoundTree;
	}

	public MapFindBean() {
	}

	public MapFindBean(GisgraphsDao gisDao, ShapeFactory shapeFactory, ShapeNodeFactory shapeNodeFactory) {
		this.gisDao = gisDao;
		this.shapeFactory = shapeFactory;
		this.shapeNodeFactory = shapeNodeFactory;
	}
}
