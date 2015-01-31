package bctm.gis.mvc;

import gis.factory.Graph;
import gis.factory.ShapeFactory;
import gis.orm.Gisgraphs;
import gis.orm.GisgraphsDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GisController
{
	private GisgraphsDao gisDao;
	private ShapeFactory shapeFactory;

	@Autowired
	public GisController(GisgraphsDao gisDao, ShapeFactory shapeFactory)
	{
		this.gisDao = gisDao;
		this.shapeFactory = shapeFactory;
	}

	@RequestMapping(
	{ "/" })
	public String showCityMap(Map<String, Object> model)
	{
		List<Gisgraphs> listAll = gisDao.getAll();
		ArrayList<String> shapes = new ArrayList<String>();
		ArrayList<String> labels = new ArrayList<String>();

		for (Gisgraphs g : listAll)
		{
			Graph shape = shapeFactory.createShape(g.getId(), g.getType(), g.getPriority(),
					g.getName(), g.getCoord());

			if (shape != null)
			{
				shapes.add(shape.getBody());
				labels.add(shape.getLabel());
			}
		}

		model.put("shapes", shapes);
		model.put("labels", labels);

		return "test";
	}
}
