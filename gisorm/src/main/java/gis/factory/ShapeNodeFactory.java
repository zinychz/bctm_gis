package gis.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.google.common.collect.Iterators;

import gis.orm.Gisgraphs;
import gis.orm.GisgraphsDao;
import gis.tree.AbstractGroupNode;
import gis.tree.GisgraphsNode;

public class ShapeNodeFactory
{
	private GisgraphsDao gisDao;

	private HashMap<Long, List<Gisgraphs>> objectsInGroupCache = new HashMap<Long, List<Gisgraphs>>();
	private HashMap<Long, List<Gisgraphs>> groupsCache = new HashMap<Long, List<Gisgraphs>>();
	private HashMap<Long, GisgraphsNode> nodesCache = new HashMap<Long, GisgraphsNode>();

	// ----------------------------------------MainMethods----------------------------------------

	public GisgraphsNode getGisgraphsNode(String name, long gisgraphId, ShapeNodeFactory factory,
			TreeNode parent)
	{
		GisgraphsNode node = nodesCache.get(gisgraphId);

		if (node == null)
		{
			node = new GisgraphsNode(name, gisgraphId, factory, parent);
			nodesCache.put(gisgraphId, node);
		}

		return node;
	}

	public List<Gisgraphs> getObjectsInGroup(long id)
	{
		List<Gisgraphs> list = objectsInGroupCache.get(id);

		if (list == null)
		{
			Set<Gisgraphs> gisgraphs = gisDao.getChildren(id);
			list = Arrays.asList(gisgraphs.toArray(new Gisgraphs[0]));

			Collections.sort(list, new Comparator<Gisgraphs>()
			{
				public int compare(Gisgraphs o1, Gisgraphs o2)
				{
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
			});

			objectsInGroupCache.put(id, list);
		}

		return list;
	}

	public List<Gisgraphs> getGroups(long id)
	{
		List<Gisgraphs> list = groupsCache.get(id);

		if (list == null)
		{
			Set<Gisgraphs> gisgraphs = gisDao.getGroups(id);
			list = Arrays.asList(gisgraphs.toArray(new Gisgraphs[0]));

			Collections.sort(list, new Comparator<Gisgraphs>()
			{
				public int compare(Gisgraphs o1, Gisgraphs o2)
				{
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
			});

			groupsCache.put(id, list);
		}

		return list;
	}

	// ----------------------------------------TreeModel----------------------------------------

	public TreeModel getTreeModel(List<Gisgraphs> list)
	{
		List<TreeNode> nodesList = new ArrayList<TreeNode>();

		for (Gisgraphs g : list)
		{
			nodesList.add(getGisgraphsNode(g.getName(), g.getId(), this, null));
		}

		RootNode root = new RootNode(nodesList);

		return new ShapeTreeModel(root);
	}

	private class ShapeTreeModel implements TreeModel
	{
		private EventListenerList listenerList = new EventListenerList();
		private Object root;

		public ShapeTreeModel()
		{
		}

		public ShapeTreeModel(Object root)
		{
			this.root = root;
		}

		public void addTreeModelListener(TreeModelListener l)
		{
			listenerList.add(TreeModelListener.class, l);
		}

		public void removeTreeModelListener(TreeModelListener l)
		{
			listenerList.remove(TreeModelListener.class, l);
		}

		public Object getChild(Object parent, int index)
		{
			if (parent instanceof TreeNode)
			{
				return ((TreeNode) parent).getChildAt(index);
			}

			return null;
		}

		public int getChildCount(Object parent)
		{
			if (parent instanceof TreeNode)
			{
				return ((TreeNode) parent).getChildCount();
			}

			return 0;
		}

		public int getIndexOfChild(Object parent, Object child)
		{
			if (parent instanceof TreeNode && child instanceof TreeNode)
			{
				return ((TreeNode) parent).getIndex((TreeNode) child);
			}

			return -1;
		}

		public Object getRoot()
		{
			return root;
		}

		public void setRoot(Object root)
		{
			this.root = root;
		}

		public boolean isLeaf(Object node)
		{
			if (node instanceof TreeNode)
			{
				return ((TreeNode) node).isLeaf();
			}

			return true;
		}

		public void valueForPathChanged(TreePath path, Object newValue)
		{
		}
	}

	private class RootNode implements TreeNode
	{
		private List<TreeNode> children;

		public RootNode(List<TreeNode> children)
		{
			this.children = children;
		}

		public Enumeration children()
		{
			return Iterators.asEnumeration(children.iterator());
		}

		public boolean getAllowsChildren()
		{
			return true;
		}

		public TreeNode getChildAt(int childIndex)
		{
			return children.get(childIndex);
		}

		public int getChildCount()
		{
			return children.size();
		}

		public int getIndex(TreeNode node)
		{
			return children.indexOf(node);
		}

		public TreeNode getParent()
		{
			return null;
		}

		public boolean isLeaf()
		{
			return children.size() <= 0;
		}

	}

	// ----------------------------------------Constructors----------------------------------------

	public ShapeNodeFactory(GisgraphsDao gisDao)
	{
		this.gisDao = gisDao;
	}

	public ShapeNodeFactory()
	{
	}

	// ----------------------------------------Getters&Setters----------------------------------------
	public GisgraphsDao getGisDao()
	{
		return gisDao;
	}

	public void setGisDao(GisgraphsDao gisDao)
	{
		this.gisDao = gisDao;
	}
}