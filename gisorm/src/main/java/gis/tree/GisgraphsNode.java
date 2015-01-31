package gis.tree;

import gis.factory.ShapeNodeFactory;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.google.common.collect.Iterators;

public class GisgraphsNode extends ShapeNode implements TreeNode
{
	protected List<AbstractGroupNode> labels = new ArrayList<AbstractGroupNode>();
	protected TreeNode parent;
	protected long gisgraphId;
	protected ShapeNodeFactory factory;

	public String getToggleType()
	{
		boolean wasInit = labels.size() > 0
				&& (!labels.get(0).isNoInit() || !labels.get(0).isNoInit());
		return wasInit ? "client" : "ajax";
	}

	public GisgraphsNode()
	{
		this.type = "gisgraph";
		labels.add(new ObjectInGroupNode("Объекты в группе", this));
		labels.add(new GroupNode("Вхождения в группы", this));
	}

	public GisgraphsNode(String name, long gisgraphId, ShapeNodeFactory factory, TreeNode parent)
	{
		this.type = "gisgraph";
		this.name = name;
		labels.add(new ObjectInGroupNode("Объекты в группе", this));
		labels.add(new GroupNode("Вхождения в группы", this));

		this.gisgraphId = gisgraphId;
		this.factory = factory;
		this.parent = parent;
	}

	public long getGisgraphId()
	{
		return gisgraphId;
	}

	public ShapeNodeFactory getFactory()
	{
		return factory;
	}

	public List<AbstractGroupNode> getLabels()
	{
		return labels;
	}

	public TreeNode getChildAt(int childIndex)
	{
		return labels.get(childIndex);
	}

	public int getChildCount()
	{
		return labels.size();
	}

	public TreeNode getParent()
	{
		return parent;
	}

	public void setParent(GisgraphsNode parent)
	{
		this.parent = parent;
	}

	public int getIndex(TreeNode node)
	{
		return labels.indexOf(node);
	}

	public boolean getAllowsChildren()
	{
		return true;
	}

	public boolean isLeaf()
	{
		return labels.size() <= 0;
	}

	public Enumeration children()
	{
		return Iterators.asEnumeration(labels.iterator());
	}
}
