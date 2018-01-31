package bctm.gis.mvc;

import gis.factory.Graph;
import gis.factory.ShapeFactory;
import gis.factory.ShapeNodeFactory;
import gis.orm.Gisgraphs;
import gis.orm.GisgraphsDao;
import gis.shapes.AbstractShape;
import gis.shapes.Ellipse;
import gis.shapes.Line;
import gis.shapes.Polygon;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

@Named("liner")
@Scope("session")
public class LinerBean implements Serializable {

	private String currentLiner = "DEFAULT";
	private Map arcGisMap = new HashMap<String, String>() {
		{
			// "streets" | "satellite" | "hybrid" | "topo" | "gray" |
			// "dark-gray" | "oceans" | "national-geographic" | "terrain" |
			// "osm"
			put("satellite", "Satellite");
			put("streets", "Streets");
			put("osm", "Osm");
			put("topo", "Topo");
			put("hybrid", "Hybrid");
			put("gray", "Gray");
			put("dark-gray", "Dark-gray");
			put("oceans", "Oceans");
			put("national-geographic", "National-geographic");
			put("terrain", "Terrain");
		}
	};
	private Map googleMap = new HashMap<String, String>() {
		{
			put("SATELLITE", "Satellite");
			put("ROADMAP", "Roadmap");
			put("HYBRID", "Hybrid");
			put("TERRAIN", "Terrain");
		}
	};

	private String currentArcGisMap = "satellite";
	private String currentGoogleMap = "SATELLITE";
	private int opacitySliderValue = 0;

	public int getOpacitySliderValue() {
		return opacitySliderValue;
	}

	public void setOpacitySliderValue(int opacitySliderValue) {
		this.opacitySliderValue = opacitySliderValue;
	}

	public String getCurrentGoogleMap() {
		return currentGoogleMap;
	}

	public void setCurrentGoogleMap(String currentGoogleMap) {
		this.currentGoogleMap = currentGoogleMap;
	}

	public String getCurrentLiner() {
		return currentLiner;
	}

	public void setCurrentLiner(String currentLiner) {
		this.currentLiner = currentLiner;
	}

	public Map getArcGisMap() {
		return arcGisMap;
	}
	
	public String getCurrentArcGisMap() {
		return currentArcGisMap;
	}

	public void setCurrentArcGisMap(String currentArcGisMap) {
		this.currentArcGisMap = currentArcGisMap;
	}
	
	public Map getGoogleMap() {
		return googleMap;
	}

	public LinerBean() {
	}
}
