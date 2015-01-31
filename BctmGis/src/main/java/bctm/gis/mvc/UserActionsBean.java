package bctm.gis.mvc;

import java.io.Serializable;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

@Named("actions")
//@SessionScoped
@ViewScoped
public class UserActionsBean implements Serializable
{
	private int outerPageIndex = 0;

	private int mapInfoIndex = 0;
	private int specInfoIndex = 0;
	
	public int getRenderOuter(){return outerPageIndex;}
	public void setRenderOuter(int index){renderOuter(index);}

	public String renderOuter(int index)
	{
		outerPageIndex = index;
		return null;
	}

	public String renderMap(int index)
	{
		mapInfoIndex = index;
		return null;
	}

	public String renderSpec(int index)
	{
		specInfoIndex = index;
		return null;
	}

	public String styleOuter(int index)
	{
		return index == outerPageIndex ? "tabbedSelected" : "tabbedUnSelected";
	}

	public String styleMap(int index)
	{
		return index == mapInfoIndex ? "tabbedSubSelected" : "tabbedSubUnSelected";
	}

	public String styleSpec(int index)
	{
		return index == specInfoIndex ? "tabbedSubSelected" : "tabbedSubUnSelected";
	}

	public boolean isOuterCurrent(int index)
	{
		return index == outerPageIndex;
	}

	public boolean isMapCurrent(int index)
	{
		return index == mapInfoIndex;
	}

	public boolean isSpecCurrent(int index)
	{
		return index == specInfoIndex;
	}

	public UserActionsBean()
	{
	}
}
