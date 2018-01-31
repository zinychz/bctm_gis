/**
 * 
 */

if (arcgisItemChecked) {
	require([
	// "esri/map"
	// , "dojo/domReady!"

	"esri/map", "esri/layers/FeatureLayer",
			"esri/layers/ArcGISTiledMapServiceLayer", "esri/tasks/query",
			"esri/symbols/SimpleFillSymbol", "esri/symbols/SimpleLineSymbol",
			"esri/graphic", "esri/dijit/Popup", "esri/dijit/PopupTemplate",
			"esri/urlUtils", "esri/graphicsUtils", "esri/Color", "dojo/on",
			"dojo/query", "dojo/parser", "dojo/dom-construct",

			"dijit/layout/BorderContainer", "dijit/layout/ContentPane",
			"dojo/domReady!"

			, "esri/geometry/Point"

	], function(Map, Point) {

		/*
		 * esriConfig.defaults.map.panDuration = 1; // time in milliseconds,
		 * default panDuration: 350 esriConfig.defaults.map.panRate = 1; //
		 * default panRate: 25 esriConfig.defaults.map.zoomDuration = 100; //
		 * default zoomDuration: 500 esriConfig.defaults.map.zoomRate = 1; //
		 * default zoomRate: 25
		 */

		arcGisMap = new Map("arcGisMap", {
			// basemap: "osm"
			basemap : "satellite"
			// ,extend: extentZ
			,
			// center: [30.5238, 50.45466 ]
			center : [ 30.14247, 49.79345 ],
			zoom : 13
		});

		arcGisMap.on("click", function(e) {
			console.log("getLatitude() = " + e.mapPoint.getLatitude()
					+ "; getLongitude() = " + e.mapPoint.getLongitude());
		});

	});
}