package gis.orm;

import gis.factory.Graph;
import gis.factory.ShapeFactory;
import gis.factory.ShapeNodeFactory;
import gis.shapes.AbstractShape;
import gis.shapes.Ellipse;
import gis.shapes.Line;
import gis.shapes.Polygon;
import gis.tree.GisgraphsNode;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HibernateTestMain extends JFrame
{
	public HibernateTestMain(String title)
	{
		super(title);
	}

	public static void main(String[] args) throws SQLException
	{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]
		{ "ormContext.xml" }, true);

		// BoilerItemDao boilerTypeDao = (BoilerItemDao)
		// context.getBean("boilerItemDao");
		//
		// // System.out.println(boilerTypeDao.getAll());
		// for (BoilerItem p : boilerTypeDao.getAll())
		// {
		// System.out.println(p.getId() + "; " + p.getCheckPeriod().getPeriod()
		// + "; " + p.getMaterial().getName() + "; " +
		// p.getMaterial().getMeasure().getName());
		// }

		// RouteItemDao routeItemDao = (RouteItemDao)
		// context.getBean("routeItemDao");
		// //
		// // // System.out.println(routeItemDao.getByInvId(33).size());
		// // for (Material p : materialDao.getAll())
		// // {
		// // System.out.println(p.getName() + "; " + p.getMeasure().getName());
		// // }
		//
		// System.out.println("RouteItemDao = " + routeItemDao);
		//
		// // System.out.println(routeItemDao.getByInvId(33030));
		// for(RouteItem r:routeItemDao.getByInvId(33030))
		// {
		// System.out.println(r.getQuantity() + "; " +
		// r.getItemPk().getMaterial().getName());
		// }

		// System.out.println(routeItemDao.getAll().get(0).getItemPk().getMaterial().getName());
		// List<RouteItem> pList = routeItemDao.getAll();
		// System.out.println("pList = " + pList.size());
		// for (RouteItem p : pList)
		// {
		// System.out.println(p.getQuantity());
		// // System.out.println(p.getMaterialid() + "; " + p.getQuantity());
		//
		// // if (p.getRemark() != null)
		// // {
		// // System.out.println("------------------");
		// // System.out.println(p.getRemark());
		// // System.out.println(p.getTray().getName());
		// // }
		// }

		// TrayDao routePropertyDao = (TrayDao) context.getBean("trayDao");
		// System.out.println("routePropertyDao = " + routePropertyDao);
		// List<Tray> pList = routePropertyDao.getAll();
		// System.out.println("pList = " + pList.size());
		// for (Tray p : pList)
		// {
		// System.out.println(p.getName());
		//
		// // if (p.getRemark() != null)
		// // {
		// // System.out.println("------------------");
		// // System.out.println(p.getRemark());
		// // System.out.println(p.getTray().getName());
		// // }
		// }

		// RoutePropertyDao routePropertyDao = (RoutePropertyDao)
		// context.getBean("routePropertyDao");
		// System.out.println("routePropertyDao = " + routePropertyDao);
		// List<RouteProperty> pList = routePropertyDao.getAll();
		// System.out.println("pList = " + pList.size());
		// for (RouteProperty p : pList)
		// {
		// if (p.getRemark() != null)
		// {
		// System.out.println("------------------");
		// System.out.println(p.getRemark());
		// System.out.println(p.getTray().getName());
		// }
		// }

		// PersonDao personDao = (PersonDao) context.getBean("personDao");
		// // System.out.println("personDao = " + personDao);
		// List<Person> pList = personDao.getAll();
		// System.out.println("pList = " + pList.size());
		// for(Person p: pList)
		// {
		// System.out.println(p.getName());
		// }

		// InventoriesDao invDao = (InventoriesDao)
		// context.getBean("inventoriesDao");
		// // Inventories inventory = invDao.getById(33030);
		// // System.out.println(inventory.getRouteItems().size());
		// // for (RouteItem p : inventory.getRouteItems())
		// // {
		// // System.out.println(p.getInvid() + "; " + p.getMaterialid() + "; "
		// + p.getQuantity());
		// // }
		// List<Inventories> invList = invDao.getAll();
		// for (Inventories i : invList)
		// {
		// // if(i.getRouteItems().size()>0)
		// //// System.out.println(i.getInvid() + "; " + i.getMaterialid() +
		// "; " + i.getQuantity());
		// // System.out.println(i.getRouteItems().size());
		//
		// // if (i.getCommissioning() != null)
		// // {
		// // System.out.println(i.getId());
		// // }
		//
		// // if(i.getGisgraph() != null)
		// // {
		// // System.out.println("i = " + i.getId() + "\tg=" +
		// // i.getGisgraph().getId() + "\tii=" +
		// // i.getGisgraph().getInventory().getId());
		// // }
		//
		// // if(i.getPerson() !=null)
		// // {
		// // System.out.println("i = " + i.getCommissioning() + "\tp=" +
		// // i.getPerson().getName());
		// // }
		//
		// // if (i.getDivision() != null)
		// // {
		// // System.out.println("i = " + i.getCommissioning() + "\tp="
		// // + i.getDivision().getName());
		// // }
		// System.out.println(i.getName());
		// }

		// GisgraphsDao gis = (GisgraphsDao) context.getBean("gisGraphsDao");
		// 2020
		// Gisgraphs g = gis.getById(2020);
		// System.out.println(g.getName());
		// System.out.println(gis.getChildren(2020));
		// System.out.println(g.getName() + "\t" +
		// invDao.getById(g.getInventory().getId()).getName());
		// List<Gisgraphs> listAll = gis.getAll();
		// for (Gisgraphs g : listAll)
		// {
		// // if (g.getInventory() != null)
		// // {
		// // System.out.println("g = " + g.getId() + "\ti=" +
		// // g.getInventory().getId() + "\tgg="
		// // + g.getInventory().getGisgraph().getId());
		// // }
		//
		// // try
		// // {
		// // System.out.println("g = " + g.getInventory());
		// // } catch (Exception e)
		// // {
		// // System.out.println(g.getId());
		// // }
		//
		// System.out.println(g.getName());
		// }

		// Inventories i = invDao.getById(29);
		// // System.out.println(i==null);
		// System.out.println(i.getGisgraphs() == null);

		// Gisgraphs g = i.getGisgraphs();
		// System.out.println(i.getName() + "\ni = " + g.getName());
		// System.out.println("-----------------------------");
		// System.out.println(g.getInventories().getName());

		// List<Inventories> list = invDao.getAll();
		// for (Inventories i : list)
		// {
		// System.out.println("i = " + i.getName());
		// }

		// HibernateTestMain frame = new HibernateTestMain("Test");
		//
		GisgraphsDao gis = (GisgraphsDao) context.getBean("gisGraphsDao");
		// List<Gisgraphs> listAll = gis.getAll();
		//
		// ShapeNodeFactory nodeFactory = new ShapeNodeFactory(gis);
		// TreeModel treeModel = nodeFactory.getTreeModel(listAll);
		//
		// JTree tree = new JTree(treeModel);
		//
		// frame.add(new JScrollPane(tree));
		//
		// frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// frame.pack();
		// frame.setVisible(true);

		// Gisgraphs g = gis.getById(2240);
		// System.out.println("------------------------------------------------------------------");
		// System.out.println(gis.getGroups(2240).size());

		// System.out.println(gis.getChildren(2349).size());
		// System.out.println(gis.getChildren(2349).iterator().next().getName());

		// System.out.println(gis.getById(2240).getGroups().size());
		// System.out.println(g.getChildren().size());

		// for(Gisgraphs g: gis.getByTypeByLikeName("zzzgrp:", "Ï", false))
		// System.out.println(g.getName());

		// System.out.println("gis = " + gis.getById(1).getName());

		// System.out.println(gis.getByLikeName(null, false));
		//
		List<Gisgraphs> listAll = gis.getAll();

		Map<String, AbstractShape> mapping = new HashMap<String, AbstractShape>();

		Line l = new Line();
		// l.setShowLabelMode(3);
		// l.setBeginStrLabel("");
		// l.setEndStrLabel("");
		// // l.setIncludeBeginStrLabel(false);
		// l.setFontSize("3");
		// l.setFill("yellow");
		// l.setStrokeWidth("1.5");
		// l.setStroke("red");
		// l.setRx(3);
		// l.setRy(3);
		
		l.setFillText("none");
		l.setFontWeight("bold");
		l.setStrokeText("black");
		l.setStrokeWidthText("0.5");
		mapping.put("aamavn:", l);
		//
		ShapeFactory shapeFactory = new ShapeFactory(mapping);
		//
		// // ShapeFactory shapeFactory = (ShapeFactory)
		// // context.getBean("shapeFactory");
		//
		for (Gisgraphs g : listAll)
		{
			Graph shape = shapeFactory.createShape(g.getId(), g.getType(), g.getPriority(),
					g.getName(), g.getCoord());

			if (shape != null && g.getType().equals("aamavn:"))
			{
				// System.out.println(shape.getLabel() + " = " +
				// shape.getBody());
				System.out.println(shape.getLabel());
			}

		}
		//
		// for (Gisgraphs g : listAll)
		// {
		// Graph shape = shapeFactory.createShape(g.getId(), g.getType(),
		// g.getPriority(),
		// g.getName(), g.getCoord());
		//
		// if (shape != null && shape instanceof gis.shapes.Line)
		// {
		// System.out.println(shape.getLabel());
		// }
		// }
	}
}
