package bctm.gis.mvc;

import gis.orm.Insulation;
import gis.orm.InsulationDao;
import gis.orm.Inventories;
import gis.orm.Material;
import gis.orm.MaterialDao;
import gis.orm.Measure;
import gis.orm.MeasureDao;
import gis.orm.RouteItem;
import gis.orm.RouteItemDao;
import gis.orm.RouteProperty;
import gis.orm.RoutePropertyDao;
import gis.orm.RouteType;
import gis.orm.RouteTypeDao;
import gis.orm.Tray;
import gis.orm.TrayDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

@Named("routeProperty")
//@SessionScoped
//@ViewScoped
@Scope("session") //need this, JSR-330 in Spring context is singleton by default
public class RoutePropertyBean implements Serializable
{
	@Inject
	@Named("routItmDao")
	private RouteItemDao routItmDao;

	@Inject
	@Named("measDao")
	private MeasureDao measDao;

	@Inject
	@Named("materDao")
	private MaterialDao materDao;

	@Inject
	@Named("routPropDao")
	private RoutePropertyDao routPropDao;

	@Inject
	@Named("routTypeDao")
	private RouteTypeDao routTypeDao;

	@Inject
	@Named("insulDao")
	private InsulationDao insulDao;

	@Inject
	@Named("trDao")
	private TrayDao trDao;

	private boolean dirtyData = false;

	private Inventories currentInventory;
	private RouteProperty currentRouteProperty;

	private List<RouteItem> routeItems = new ArrayList<RouteItem>();

	private List<String> allRouteTypeList;
	private List<String> allInsulationList;
	private List<String> allTrayList;

	private List<String> allMeasureList;
	private List<String> allMaterialList;

	public List<RouteItem> getRouteItems()
	{
		if (dirtyData)
		{
			refreshDate();
		}
		return routeItems;
	}

	public RouteProperty getCurrentRouteProperty()
	{
		if (dirtyData)
		{
			refreshDate();
		}
		return currentRouteProperty;
	}

	public List<String> getAllMeasureList()
	{
		if (allMeasureList == null)
		{
			allMeasureList = new ArrayList<String>();
			allMeasureList.add("");
			for (Measure p : measDao.getAll())
			{
				if (p.getName() != null)
					allMeasureList.add(p.getName());
			}
			Collections.sort(allMeasureList, String.CASE_INSENSITIVE_ORDER);
		}

		return allMeasureList;
	}

	public List<String> getAllMaterialList()
	{
		if (allMaterialList == null)
		{
			allMaterialList = new ArrayList<String>();
			allMaterialList.add("");
			for (Material p : materDao.getAll())
			{
				if (p.getName() != null)
					allMaterialList.add(p.getName());
			}
			Collections.sort(allMaterialList, String.CASE_INSENSITIVE_ORDER);
		}

		return allMaterialList;
	}

	public List<String> getAllRouteTypeList()
	{
		if (allRouteTypeList == null)
		{
			allRouteTypeList = new ArrayList<String>();
			allRouteTypeList.add("");
			for (RouteType p : routTypeDao.getAll())
			{
				if (p.getName() != null)
					allRouteTypeList.add(p.getName());
			}
			Collections.sort(allRouteTypeList, String.CASE_INSENSITIVE_ORDER);
		}

		return allRouteTypeList;
	}

	public List<String> getAllInsulationList()
	{
		if (allInsulationList == null)
		{
			allInsulationList = new ArrayList<String>();
			allInsulationList.add("");
			for (Insulation p : insulDao.getAll())
			{
				if (p.getName() != null)
					allInsulationList.add(p.getName());
			}
			Collections.sort(allInsulationList, String.CASE_INSENSITIVE_ORDER);
		}

		return allInsulationList;
	}

	public List<String> getAllTrayList()
	{
		if (allTrayList == null)
		{
			allTrayList = new ArrayList<String>();
			allTrayList.add("");
			for (Tray p : trDao.getAll())
			{
				if (p.getName() != null)
					allTrayList.add(p.getName());
			}
			Collections.sort(allTrayList, String.CASE_INSENSITIVE_ORDER);
		}

		return allTrayList;
	}

	// ------------------------------------------------

	public Inventories getCurrentInventory()
	{
		return currentInventory;
	}

	public void setCurrentInventory(Inventories currentInventory)
	{
		if (this.currentInventory != currentInventory)
		{
			setDirtyData(true);
		}

		this.currentInventory = currentInventory;
	}

	// ------------------------------------------------

	private void refreshDate()
	{
		if (currentInventory != null)
		{
			currentRouteProperty = routPropDao.getById(currentInventory.getId());
			routeItems = routItmDao.getByInvId(currentInventory.getId());
		}

		setDirtyData(false);
	}

	public boolean isDirtyData()
	{
		return dirtyData;
	}

	public void setDirtyData(boolean dirtyData)
	{
		if (dirtyData)
		{
			currentRouteProperty = null;
			routeItems.clear();
		}

		this.dirtyData = dirtyData;
	}

	public RoutePropertyBean()
	{
	}
}
