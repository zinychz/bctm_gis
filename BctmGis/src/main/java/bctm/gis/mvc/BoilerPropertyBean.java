package bctm.gis.mvc;

import gis.orm.BoilerItem;
import gis.orm.BoilerItemDao;
import gis.orm.BoilerProperty;
import gis.orm.BoilerPropertyDao;
import gis.orm.BoilerType;
import gis.orm.BoilerTypeDao;
import gis.orm.Inventories;
import gis.orm.Material;
import gis.orm.MaterialDao;
import gis.orm.Measure;
import gis.orm.MeasureDao;

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

import org.springframework.context.annotation.Scope;

@Named("boilerProperty")
//@SessionScoped
//@ViewScoped
@Scope("session") //need this, JSR-330 in Spring context is singleton by default
public class BoilerPropertyBean implements Serializable
{
	@Inject
	@Named("boilerItmDao")
	private BoilerItemDao boilerItmDao;

	@Inject
	@Named("measDao")
	private MeasureDao measDao;

	@Inject
	@Named("materDao")
	private MaterialDao materDao;

	@Inject
	@Named("boilerPropDao")
	private BoilerPropertyDao boilerPropDao;

	@Inject
	@Named("boilTypeDao")
	private BoilerTypeDao boilTypeDao;

//	private DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("uk", "UA"));

	private boolean dirtyData = false;

	private Inventories currentInventory;
	private BoilerProperty currentBoilerProperty;

	private List<BoilerItem> boilerItems = new ArrayList<BoilerItem>();

	private List<String> allBoilerTypeList;
	private List<String> allMeasureList;
	private List<String> allMaterialList;

	public List<BoilerItem> getBoilerItems()
	{
		if (dirtyData)
		{
			refreshDate();
		}
		return boilerItems;
	}

	public BoilerProperty getCurrentBoilerProperty()
	{
		if (dirtyData)
		{
			refreshDate();
		}
		return currentBoilerProperty;
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

	public List<String> getAllBoilerTypeList()
	{
		if (allBoilerTypeList == null)
		{
			allBoilerTypeList = new ArrayList<String>();
			allBoilerTypeList.add("");
			for (BoilerType p : boilTypeDao.getAll())
			{
				if (p.getName() != null)
					allBoilerTypeList.add(p.getName());
			}
			Collections.sort(allBoilerTypeList, String.CASE_INSENSITIVE_ORDER);
		}

		return allBoilerTypeList;
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
			currentBoilerProperty = boilerPropDao.getById(currentInventory.getId());
			boilerItems = boilerItmDao.getByInvId(currentInventory.getId());
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
			currentBoilerProperty = null;
			boilerItems.clear();
		}

		this.dirtyData = dirtyData;
	}
	
//	public String formatDate(java.sql.Timestamp date)
//	{
//		if (date == null)
//			return "";
//		return df.format(date);
//	}

	public BoilerPropertyBean()
	{
	}
}
