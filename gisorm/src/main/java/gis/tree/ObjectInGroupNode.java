package gis.tree;

import gis.orm.Gisgraphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.swing.tree.TreeNode;

public class ObjectInGroupNode extends AbstractGroupNode
{
	public ObjectInGroupNode()
	{
		this.setType("objectInGroup");
	}

	public ObjectInGroupNode(String name, GisgraphsNode parent)
	{
		this.setType("objectInGroup");
		this.setName(name);
		this.setParentNode(parent);
	}

	@Override
	public List<GisgraphsNode> getInitChildNodes()
	{
		GisgraphsNode fromCache = parentNode.getFactory().getGisgraphsNode(parentNode.getName(),
				parentNode.getGisgraphId(), parentNode.getFactory(), parentNode);

		AbstractGroupNode groupNode = fromCache.getLabels().get(0);

		if (fromCache != null && !groupNode.noInit)
		{
			return fromCache.getLabels().get(0).getChildNodes();
		}

		List<Gisgraphs> gisgraphs = parentNode.getFactory().getObjectsInGroup(
				parentNode.getGisgraphId());
		List<GisgraphsNode> list = new ArrayList<GisgraphsNode>();
		for (Gisgraphs g : gisgraphs)
		{
			GisgraphsNode p = parentNode.getFactory().getGisgraphsNode(g.getName(), g.getId(),
					parentNode.getFactory(), this);
			list.add(p);
		}

		noInit = false;

		return list;
	}
}
