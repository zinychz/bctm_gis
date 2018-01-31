package bctm.gis.mvc;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import org.springframework.context.annotation.Scope;

@ManagedBean
//@ViewScoped
//@SessionScoped
@Scope("session") //need this, JSR-330 in Spring context is singleton by default
public class SpecFindFilteringBean implements Serializable
{
	private String inventoryIdFilter;
	private String inventoryNameFilter;
	private String inventoryDivisionFilter;
	private String inventoryPersonFilter;

	public String getInventoryPersonFilter()
	{
		return inventoryPersonFilter;
	}

	public void setInventoryPersonFilter(String inventoryPersonFilter)
	{
		this.inventoryPersonFilter = inventoryPersonFilter;
	}

	public String getInventoryDivisionFilter()
	{
		return inventoryDivisionFilter;
	}

	public void setInventoryDivisionFilter(String inventoryDivisionFilter)
	{
		this.inventoryDivisionFilter = inventoryDivisionFilter;
	}

	public String getInventoryIdFilter()
	{
		return inventoryIdFilter;
	}

	public void setInventoryIdFilter(String inventoryIdFilter)
	{
		this.inventoryIdFilter = inventoryIdFilter;
	}

	public String getInventoryNameFilter()
	{
		return inventoryNameFilter;
	}

	public void setInventoryNameFilter(String inventoryNameFilter)
	{
		this.inventoryNameFilter = inventoryNameFilter;
	}

}
