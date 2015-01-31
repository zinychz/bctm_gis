package bctm.gis.mvc;

import gis.orm.Division;
import gis.orm.DivisionDao;
import gis.orm.Inventories;
import gis.orm.InventoriesDao;
import gis.orm.Person;
import gis.orm.PersonDao;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("specFind")
//@SessionScoped
@ViewScoped
public class SpecFindBean implements Serializable
{
	@Inject
	@Named("invDao")
	private InventoriesDao invDao;

	@Inject
	@Named("divDao")
	private DivisionDao divDao;

	@Inject
	@Named("persDao")
	private PersonDao persDao;

	@Inject
	@Named("mapFind")
	private MapFindBean mapFindBean;
	
	@Inject
	@Named("routeProperty")
	private RoutePropertyBean routePropertyBean;
	
	@Inject
	@Named("boilerProperty")
	private BoilerPropertyBean boilerPropertyBean;

	private List<String> allDivisionList;
	private List<String> allPersonList;

	private List<Inventories> inventories;

	private long currentInventoryId;
	private Inventories currentInventory = null;

	private DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("uk", "UA"));
	private boolean synchronizedMap = true;

	public List<String> getAllPersonList()
	{
		if (allPersonList == null)
		{
			allPersonList = new ArrayList<String>();
			allPersonList.add("");
			for (Person p : persDao.getAll())
			{
				if (p.getName() != null)
					allPersonList.add(p.getName());
			}
			Collections.sort(allPersonList, String.CASE_INSENSITIVE_ORDER);
		}

		return allPersonList;
	}

	public List<String> getAllDivisionList()
	{
		if (allDivisionList == null)
		{
			allDivisionList = new ArrayList<String>();
			allDivisionList.add("");
			for (Division d : divDao.getAll())
			{
				if (d.getName() != null)
					allDivisionList.add(d.getName());
			}
			Collections.sort(allDivisionList, String.CASE_INSENSITIVE_ORDER);
		}

		return allDivisionList;
	}

	public boolean isSynchronizedMap()
	{
		return synchronizedMap;
	}

	public void setSynchronizedMap(boolean synchronizedMap)
	{
		this.synchronizedMap = synchronizedMap;
	}

	public long getCurrentInventoryId()
	{
		return currentInventoryId;
	}

	public String clearCurrent()
	{
		currentInventoryId = -1;
		currentInventory = null;

		return null;
	}

	public void setCurrentInventoryId(long currentInventoryId)
	{
		this.currentInventoryId = currentInventoryId;
		currentInventory = invDao.getById(currentInventoryId);
		if (synchronizedMap)
		{
			mapFindBean.focusOnShape(currentInventory.getGisgraph());
		}
		
		routePropertyBean.setCurrentInventory(currentInventory);
		boilerPropertyBean.setCurrentInventory(currentInventory);
	}

	public Inventories getCurrentInventory()
	{
		return currentInventory;
	}

	public void setCurrentInventory(Inventories currentInventory)
	{
		routePropertyBean.setCurrentInventory(currentInventory);
		boilerPropertyBean.setCurrentInventory(currentInventory);
		
		if (currentInventory == null)
		{
			clearCurrent();
			return;
		}

		this.currentInventory = currentInventory;
		this.currentInventoryId = currentInventory.getId();
	}

	public List<Inventories> getInventories()
	{
		if (inventories == null)
		{
			inventories = invDao.getAll();
		}

		return inventories;
	}

	public void setInventories(List<Inventories> inventories)
	{
		this.inventories = inventories;
	}

	public int getClientRows()
	{
		if (inventories != null)
		{
			return (int) inventories.size() / 3;
		}

		return 0;
	}

	public String getDateupdateStr()
	{
		if (currentInventory != null && currentInventory.getDateupdate() != null)
		{
			return "(данные на " + df.format(currentInventory.getDateupdate()) + ")";
		}

		return "";
	}

	public String formatDate(Date date)
	{
		if (date == null)
			return "";
		return df.format(date);
	}

	public SpecFindBean()
	{
	}

	public SpecFindBean(InventoriesDao invDao)
	{
		this.invDao = invDao;
	}
}
