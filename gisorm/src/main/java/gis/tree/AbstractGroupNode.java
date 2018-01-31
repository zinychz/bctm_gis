package gis.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.google.common.collect.Iterators;

public abstract class AbstractGroupNode extends ShapeNode implements TreeNode {
	protected List<GisgraphsNode> childNodes = new ArrayList<GisgraphsNode>();
	protected GisgraphsNode parentNode;
	protected boolean noInit = true;

	public boolean isNoInit() {
		return noInit;
	}

	public void setNoInit(boolean noInit) {
		this.noInit = noInit;
	}

	public String getToggleType() {
		return noInit ? "ajax" : "client";
	}

	public abstract List<GisgraphsNode> getInitChildNodes();

	@Override
	public String getName() {
		return "(" + childNodes.size() + ") " + super.getName();
	}

	public TreeNode getParent() {
		return parentNode;
	}

	public GisgraphsNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(GisgraphsNode parentNode) {
		this.parentNode = parentNode;
	}

	public List<GisgraphsNode> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<GisgraphsNode> childNodes) {
		this.childNodes = childNodes;
	}

	public TreeNode getChildAt(int childIndex) {
		if (noInit) {
			childNodes = getInitChildNodes();
		}

		return childNodes.get(childIndex);
	}

	public int getChildCount() {
		if (noInit) {
			childNodes = getInitChildNodes();
		}

		return childNodes.size();
	}

	public int getIndex(TreeNode node) {
		if (noInit) {
			childNodes = getInitChildNodes();
		}

		return childNodes.indexOf(node);
	}

	public boolean getAllowsChildren() {
		return true;
	}

	public boolean isLeaf() {
		if (noInit) {
			childNodes = getInitChildNodes();
		}

		return childNodes.size() <= 0;
	}

	public Enumeration children() {
		return Iterators.asEnumeration(childNodes.iterator());
	}
}
