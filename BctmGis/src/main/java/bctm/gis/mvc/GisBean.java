package bctm.gis.mvc;

import gis.factory.Graph;
import gis.factory.ShapeFactory;
import gis.orm.Gisgraphs;
import gis.orm.GisgraphsDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("gis")
//@SessionScoped
@ViewScoped
public class GisBean implements Serializable
{
	@Inject
	@Named("gisDao")
	private GisgraphsDao gisDao;
	
	@Inject
	@Named("shapeFactory")
	private ShapeFactory shapeFactory;

	private List<String> shapes;
	private List<String> labels;

	public List<String> getShapes()
	{
		List<String> shapes = new ArrayList<String>();
		List<Gisgraphs> listAll = gisDao.getAll();

		for (Gisgraphs g : listAll)
		{
			Graph shape = shapeFactory.createShape(g.getId(), g.getType(), g.getPriority(),
					g.getName(), g.getCoord());

			if (shape != null)
			{
				shapes.add(shape.getWrapLinkBody());
			}
		}

		return shapes;
	}

	public List<String> getLabels()
	{
		List<String> labels = new ArrayList<String>();
		List<Gisgraphs> listAll = gisDao.getAll();

		for (Gisgraphs g : listAll)
		{
			Graph shape = shapeFactory.createShape(g.getId(), g.getType(), g.getPriority(),
					g.getName(), g.getCoord());

			if (shape != null)
			{
				labels.add(shape.getWrapLinkLabel());
			}
		}

		return labels;
	}

	// @Autowired
	public GisBean(GisgraphsDao gisDao, ShapeFactory shapeFactory)
	{
		this.gisDao = gisDao;
		this.shapeFactory = shapeFactory;
	}

	public GisBean()
	{
	}
}
