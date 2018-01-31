package bctm.gis.mvc;

import java.util.Enumeration;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.inject.Inject;
import javax.inject.Named;

import org.richfaces.event.DropEvent;
import org.richfaces.event.DropListener;
import org.springframework.context.annotation.Scope;

import gis.factory.ShapeNodeFactory;
import gis.orm.Gisgraphs;
import gis.orm.GisgraphsDao;
import gis.tree.AbstractGroupNode;
import gis.tree.GisgraphsNode;
import gis.tree.GroupNode;
import gis.tree.ObjectInGroupNode;

@Named("dndeventtree")
@Scope("request")
public class DnDEventTreeBean implements DropListener {

	@Inject
	@Named("shapeNodeFactory")
	private ShapeNodeFactory shapeNodeFactory;

	@Inject
	@Named("gisDao")
	private GisgraphsDao gisDao;

	@Inject
	@Named("mapFind")
	private MapFindBean mapFind;

	@Override
	public void processDrop(DropEvent event) {

		boolean accepted = false;
		GisgraphsNode shapeGisgraphsNode = null;
		GisgraphsNode groupGisgraphsNode = null;

		if (event.getDragValue() instanceof GisgraphsNode) {

			if (event.getDropValue() instanceof ObjectInGroupNode) {
				shapeGisgraphsNode = (GisgraphsNode) event.getDragValue();
				groupGisgraphsNode = ((ObjectInGroupNode) event.getDropValue()).getParentNode();
				accepted = true;
			} else if (event.getDropValue() instanceof GroupNode) {
				groupGisgraphsNode = (GisgraphsNode) event.getDragValue();
				shapeGisgraphsNode = ((GroupNode) event.getDropValue()).getParentNode();
				accepted = true;
			}
		} else if (event.getDragValue() instanceof GroupNode) {

			if (event.getDropValue() instanceof GisgraphsNode) {
				groupGisgraphsNode = (GisgraphsNode) event.getDropValue();
				shapeGisgraphsNode = ((GroupNode) event.getDragValue()).getParentNode();
				accepted = true;
			}
		} else if (event.getDragValue() instanceof ObjectInGroupNode) {

			if (event.getDropValue() instanceof GisgraphsNode) {
				shapeGisgraphsNode = (GisgraphsNode) event.getDropValue();
				groupGisgraphsNode = ((ObjectInGroupNode) event.getDragValue()).getParentNode();
				accepted = true;
			}
		}

		if (accepted && groupGisgraphsNode != null && shapeGisgraphsNode != null
				&& (groupGisgraphsNode.getGisgraphId() != shapeGisgraphsNode.getGisgraphId())) {

			List<Gisgraphs> objectsInGroup = shapeNodeFactory.getObjectsInGroup(groupGisgraphsNode.getGisgraphId());
			Gisgraphs shapeGisgraphs = gisDao.getById(shapeGisgraphsNode.getGisgraphId());
			Gisgraphs groupGisgraphs = gisDao.getById(groupGisgraphsNode.getGisgraphId());
			if (shapeGisgraphs != null && !objectsInGroup.contains(shapeGisgraphs)) {
				boolean success = false;
				try {
					success = gisDao.addChild(shapeGisgraphs, groupGisgraphs);
					if (success) {
						shapeNodeFactory.removeFromCache(shapeGisgraphsNode.getGisgraphId());
						shapeNodeFactory.removeFromCache(groupGisgraphsNode.getGisgraphId());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			Enumeration enumeration = shapeGisgraphsNode.children();
			while (enumeration.hasMoreElements()) {
				((AbstractGroupNode) enumeration.nextElement()).setNoInit(true);
			}
			enumeration = groupGisgraphsNode.children();
			while (enumeration.hasMoreElements()) {
				((AbstractGroupNode) enumeration.nextElement()).setNoInit(true);
			}
		}
	}

	public DnDEventTreeBean() {
	}
}
