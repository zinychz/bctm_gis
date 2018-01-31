/**
 * 
 */

function debug(dataAjax) {
	alert(winInfo.value);
}

var winInfo = document.getElementById('windowInfo');

function refreshAjaxGis() {
	winInfo.value = -translateX / scale + "," + -translateY / scale + ","
			+ (-translateX / scale + userGisWindow.clientWidth / scale) + ","
			+ (-translateY / scale + userGisWindow.clientHeight / scale) + ","
			+ scale;
	document.getElementById('renderShapes').click();
}

var shiftStartX;
var shiftStartY;

window.onload = function() {

	translateX = userGisWindow.clientWidth / 2 - 2276.4375 * (scale);
	translateY = userGisWindow.clientHeight / 2 - 1419.1875 * (scale);

	shiftX = (translateX - (-userGisWindow.clientWidth / 2) * (scale - 1))
			/ scale;
	shiftY = (translateY - (-userGisWindow.clientHeight / 2) * (scale - 1))
			/ scale;

	shiftStartX = shiftX;
	shiftStartY = shiftY;

	$(window).bind('resize', function() {
		resizeWindow();
	});
	resizeWindow();

	userGisWindowWidth = userGisWindow.clientWidth;
	userGisWindowHeight = userGisWindow.clientHeight;

	refreshAjaxGis();
}

// function saveCreateEditShape(dataAjax) {
function saveCreateEditShape() {
	// if (dataAjax.status == "success") {
	winInfo.value = -translateX / scale + "," + -translateY / scale + ","
			+ (-translateX / scale + userGisWindow.clientWidth / scale) + ","
			+ (-translateY / scale + userGisWindow.clientHeight / scale) + ","
			+ scale;
	document.getElementById('saveCreateEditShape').click();
	// }
}

function errMessage(error) {
	alert("Error Description: " + error.description
			+ "\n-------------------\nError Name:" + error.errorName
			+ "\n-------------------\nError errorMessage:" + error.errorMessage
			+ "\n-------------------\nstatus:" + error.status
			+ "\n-------------------\ntype:" + error.type
			+ "\n-------------------\nsource:" + error.source
			+ "\n-------------------\nresponseXML:" + error.responseXML
			+ "\n-------------------\nresponseText:" + error.responseText
			+ "\n-------------------\nresponseCode:" + error.responseCode);
}

function restoreAfterRefreshAjaxGis(dataAjax) {
	if (dataAjax.status == "success") {
		groupScale = document.getElementById('groupScale');
		userGisWindow = document.getElementById('userGisWindow');

		groupScale.setAttribute("transform", "translate(" + translateX + ","
				+ translateY + ") scale(" + scale + ")");
		attachMousewheel();

		mode = document.getElementById('mapMode');

		setAnimate(dataAjax);

		createElement = document.getElementById('cr_ed_Shape');
		if (createElement) {
			createElement = createElement.firstElementChild;
		}
		type = null;
		if (createElement) {
			type = Object.prototype.toString.call(createElement).slice(8, -1);
		}
		points = null;
		if (type && type === "SVGEllipseElement") {
			var cx = createElement.getAttribute("cx");
			var cy = createElement.getAttribute("cy");
			if (cx && cx != "" && cy && cy != "") {
				points = [ parseFloat(createElement.getAttribute("cx")),
						parseFloat(createElement.getAttribute("cy")) ];
			} else {
				points = [];
			}
		} else if (type
				&& (type === "SVGPolylineElement" || type === "SVGPolygonElement")) {
			points = createElement.getAttribute("points").trim()
					.split(/[\s,]+/);
			if (points.length > 1) {
				points = points.join().replace(/^,+/, "").split(",").map(
						function(coord) {
							return parseFloat(coord);
						});
			} else {
				points = [];
			}
		}

		cr_ed_Points = document.getElementById('cr_ed_Points');
		linePhantomFly = cr_ed_Points.querySelector("[id^='fly']");

		createEditShapeInfo = document.getElementById('createEditShapeInfo');

		crossPoint = null;
		crossLine = null;

		refreshLiner();
		setOpacity('opacitySlider_input', 'userGisWindow');
		var currentArcGisMap = document.getElementById('currentArcGisMap');
		if (currentArcGisMap) {
			changeArcGisMap(currentArcGisMap.options[currentArcGisMap.selectedIndex].value);
		} else {
			var currentGoogleMap = document.getElementById('currentGoogleMap');
			if (currentGoogleMap && googleMap) {
				changeGoogleMap(currentGoogleMap.options[currentGoogleMap.selectedIndex].value);
			}
		}
	}
}

jQuery(function($) {
	$('#gisSlider').split({
		orientation : 'vertical',
		limit : 20,
		position : '75%'
		// position : '5%'
		/*
		 * ,onDragEnd: function() { console.log("onDragEnd: " +
		 * userGisWindow.clientWidth); }
		 */
		,
		onDragEnd : resizeSlider
	});
});

var arcgisItemChecked = document.getElementById('currentLiner:1').checked;
var googleItemChecked = document.getElementById('currentLiner:2').checked;

// var zoomRate = 1.5;
var zoomRate = 2;

// var translateX = -700;
// var translateY = -500;
var translateX = -160;
var translateY = -102;

// var scale =1;
var startScale = 1 / 1.5 / 1.5 / 1.5; // var scale =
// 0.2962962962962962962962962962963;
var scale = startScale;

var groupScale = document.getElementById('groupScale');
var userGisWindow = document.getElementById('userGisWindow');

var count = 0;
var timer;

function attachMousewheel() {
	if (userGisWindow.addEventListener) {
		// IE9, Chrome, Safari, Opera
		userGisWindow.addEventListener("mousewheel", mouseWheelHandler, false);
		// Firefox
		userGisWindow.addEventListener("DOMMouseScroll", mouseWheelHandler,
				false);
	}
	// IE 6/7/8
	else
		userGisWindow.attachEvent("onmousewheel", mouseWheelHandler);
}

function mouseWheelHandler(e) {
	// cross-browser wheel delta
	var e = window.event || e; // old IE support
	var delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));
	if (delta > 0) {
		zoom("zoomIn");
	} else if (delta < 0) {
		zoom("zoomOut");
	}
}

function setCurrentInventoryId(idInv) {

	var id = parseInt((idInv + "").replace(/\D/g, ''));

	if (id) {
		document.getElementById('currentInventoryId').value = id;
		document.getElementById('getInventoryInfo').click();
	}
}

function getShapeFromLink(idShape) {
	if (preClickOnShape) {
		var id = parseInt((idShape + "").replace(/\D/g, ''));

		if (id) {
			document.getElementById('currentId').value = id;
			document.getElementById('getShapeInfo').click();
		}
	}
	preClickOnShape = false;
}

function getShapeFromLinkAndFocus(idShape) {

	var id = parseInt((idShape + "").replace(/\D/g, ''));

	if (id) {
		document.getElementById('currentId').value = id;
		document.getElementById('getShapeInfoAndFocus').click();
	}
}

// function addClass(o, c) {
// var re = new RegExp("(^|\\s)" + c + "(\\s|$)", "g");
// if (re.test(o.className)) {
// // alert("ok");
// return o.className = (o.className + " " + c).replace(/\s+/g, " ")
// .replace(/(^ | $)/g, "");
// } else {
// // alert("No");
// o.className = o.className + " " + c;
// }
// }

function setCursor(element, cursor) {
	if (element.style)
		element.style.cursor = cursor;

	// var property = "cursor";
	// var important = true;
	// //remove previously defined property
	// if (element.style.setProperty)
	// element.style.setProperty(property, '');
	// else
	// element.style.setAttribute(property, '');
	//
	// //insert the new style with all the old rules
	// element.setAttribute('style', element.style.cssText +
	// property + ':' + cursor + ((important) ? ' !important' : '') + ';');

	// element.classList.add("processCursor");
	// addClass(element, "progressCursor");

	return element.parentNode;
}

function setCursorAllParents(element, cursor) {
	var el = element;
	while (el) {
		el = setCursor(el, cursor);
	}
}

function progressCursorAjax(dataAjax) {
	if (dataAjax.status == "begin") {
		setCursorAllParents(dataAjax.source, "progress");
	}

	if (dataAjax.status == "success") {
		setCursorAllParents(dataAjax.source, "default");
	}
}

function focusOnShape(dataAjax) {

	if (dataAjax.status == "begin") {
		setCursorAllParents(dataAjax.source, "progress");
	}

	if (dataAjax.status == "success") {

		setCursorAllParents(dataAjax.source, "default");

		if (document.getElementById('currentId').value > -1) {
			var currX = document.getElementById('currentX').value;
			var currY = document.getElementById('currentY').value;

			translateX = userGisWindow.clientWidth / 2 - currX * (scale);
			translateY = userGisWindow.clientHeight / 2 - currY * (scale);

			shiftStartX = shiftX;
			shiftStartY = shiftY;

			shiftX = userGisWindow.clientWidth / 2 - currX;
			shiftY = userGisWindow.clientHeight / 2 - currY;

			groupScale.setAttribute("transform", "translate(" + translateX
					+ "," + translateY + ") scale(" + scale + ")");

			difArcGisX = (shiftStartX - shiftX) * scale;
			difArcGisY = (shiftStartY - shiftY) * scale;
			shiftArcGisX += difArcGisX / (scale / startScale);
			shiftArcGisY += difArcGisY / (scale / startScale);
			if (arcgisItemChecked) {
				var center = arcGisMap.extent.getCenter();
				arcGisMap.centerAt(arcGisMap.toMap(arcGisMap.toScreen(center)
						.offset(difArcGisX, difArcGisY)));
			}

			difGoogleX = (shiftStartX - shiftX) * scale;
			difGoogleY = (shiftStartY - shiftY) * scale;
			shiftGoogleX += difGoogleX / (scale / startScale);
			shiftGoogleY += difGoogleY / (scale / startScale);
			if (googleItemChecked) {
				googleMap.panBy(difGoogleX, difGoogleY);
			}

			refreshAjaxGis();
		} else if (mode.value === "EDIT" || mode.value === "DELETE"
				|| mode.value === "COPY") {
			refreshAjaxGis();
		}

		// setAnimate(dataAjax);

	}
}

function emptyFun(dataAjax) {
}

function setAnimate(dataAjax) {

	if (timer) {
		clearInterval(timer);
	}

	var animate = document.getElementById('animate');
	var next = animate.firstElementChild;
	while (next) {
		animate.removeChild(next);
		next = animate.firstElementChild;
	}

	if (dataAjax.status == "success" && mode.value === "VIEW") {
		var typeShape = document.getElementById('currentType').value;
		var shape = document.getElementById('shp'
				+ document.getElementById('currentId').value);

		if (shape || typeShape === "zzzgrp:") {

			var width = 3 / scale;
			var fragment = document.createDocumentFragment();

			if (typeShape === "zzzgrp:") {

				var children = document.getElementById('children').value
						.split(" ");

				for (var i = 0; i < children.length; i++) {

					var child = document.getElementById('shp' + children[i]);
					if (child) {
						var newAnimate = child.cloneNode(true);
						newAnimate.id = "";
						newAnimate.setAttribute("style",
								"fill:none;stroke:black;stroke-width:" + width);
						fragment.appendChild(newAnimate);
					}
				}
			} else {
				var newAnimate = shape.cloneNode(true);
				newAnimate.setAttribute("style",
						"fill:none;stroke:black;stroke-width:" + width);
				fragment.appendChild(newAnimate);
			}

			animate.appendChild(fragment);

			count = 0;

			timer = setInterval(
					function() {

						var animate = document.getElementById('animate');
						var width = 3 / scale;

						var nextChild = animate.firstElementChild;
						while (nextChild) {
							if (count % 2 == 0) {
								nextChild.setAttribute("style",
										"fill:none;stroke:yellow;stroke-width:"
												+ width);
							} else {
								nextChild.setAttribute("style",
										"fill:none;stroke:black;stroke-width:"
												+ width);
							}

							nextChild = nextChild.nextElementSibling;
						}

						count++;

					}, 700);
		}
	}
}

function zoom(zoomType) {
	if (zoomType == 'zoomIn') {
		scale *= zoomRate;
		zoomArcGis++;
		zoomGoogle++;
	} else if (zoomType == 'zoomOut') {
		scale /= zoomRate;
		zoomArcGis--;
		zoomGoogle--;
	}

	translateX = (-userGisWindow.clientWidth / 2) * (scale - 1) + shiftX
			* (scale);
	translateY = (-userGisWindow.clientHeight / 2) * (scale - 1) + shiftY
			* (scale);

	groupScale.setAttribute("transform", "translate(" + translateX + ","
			+ translateY + ") scale(" + scale + ")");

	if (arcgisItemChecked) {
		arcGisMap.setZoom(zoomArcGis);
	}

	if (googleMap) {
		googleMap.setZoom(zoomGoogle);
	}

	refreshAjaxGis();
}

var moveFlg = false;
var mouseDownFlg = false;

var difX = 0;
var difY = 0;
var startPoint = {
	x : 0,
	y : 0
};
var currPoint = {
	x : 0,
	y : 0
};

var shiftX = -700;
var shiftY = -500;

var preClickOnShape = false;

var mode;

var isCaptured = false;
var capturedElement;

var createElement;
var type;
var linePhantomFly;
var cr_ed_Points;
var points;

var createEditShapeInfo;

var arcGisMap;
var difArcGisX = 0;
var difArcGisY = 0;
var shiftArcGisX = 0;
var shiftArcGisY = 0;
var zoomArcGis = 13;

var startArcGisPoint = {
	x : 0,
	y : 0
};
var currArcGisPoint = {
	x : 0,
	y : 0
};

var googleMap;
var difGoogleX = 0;
var difGoogleY = 0;
var shiftGoogleX = 0;
var shiftGoogleY = 0;
var zoomGoogle = 13;

var startGooglePoint = {
	x : 0,
	y : 0
};
var currGooglePoint = {
	x : 0,
	y : 0
};

function setRequestShapeOnClick(id) {
	preClickOnShape = true;
}

function clearPhantomsContainer() {
	var next = cr_ed_Points.firstElementChild;
	while (next) {
		cr_ed_Points.removeChild(next);
		next = cr_ed_Points.firstElementChild;
	}
	crossPoint = null;
	crossLine = null;
}

function setAttr(parent, attr, value) {
	var next = parent.firstElementChild;
	while (next) {
		next.setAttribute(attr, value);
		next = next.nextElementSibling;
	}
}

function setPointsCreateElement() {
	if (type === "SVGEllipseElement") {
		createElement.setAttribute("cx", points[0]);
		createElement.setAttribute("cy", points[1]);
	} else if (type === "SVGPolylineElement" || type === "SVGPolygonElement") {
		var pointsAttr = "";
		for (var j = 0; j < points.length - 1; j += 2) {
			pointsAttr += (points[j] + "," + points[j + 1] + " ");
		}
		createElement.setAttribute("points", pointsAttr);
	}
}

function fillPhantomLinesContainer() {
	var linePhantom;
	for (var k = 0; k < points.length; k += 2) {
		var linePhantom = null;
		if (points.length > 3 && (k + 3 < points.length)) {
			linePhantom = document.createElementNS(
					"http://www.w3.org/2000/svg", 'polyline');
			if (linePhantom) {
				linePhantom.setAttribute('points', points[k] + ","
						+ points[k + 1] + " " + points[k + 2] + ","
						+ points[k + 3]);

				linePhantom.setAttribute('id', "phl" + (1 + k / 2));
				linePhantom.setAttribute('fill', 'none');
				linePhantom.setAttribute('stroke', 'red');
				linePhantom.setAttribute('stroke-width', '' + (1 / scale));
				linePhantom.setAttribute('onmouseover',
						"showCrossPoint(evt.target);");

				/*
				 * var setElement =
				 * document.createElementNS("http://www.w3.org/2000/svg",
				 * 'set'); setElement.setAttribute('attributeName', 'stroke');
				 * setElement.setAttribute('to', 'yellow');
				 * setElement.setAttribute('begin',
				 * createElement.getAttribute("id") + ".mouseover");
				 * setElement.setAttribute('end',
				 * createElement.getAttribute("id") + ".mouseout");
				 * 
				 * linePhantom.appendChild(setElement);
				 */
				cr_ed_Points.appendChild(linePhantom);

			}
		}
	}
}

function fillPhantomFlyLinesContainer() {
	linePhantomFly = document.createElementNS("http://www.w3.org/2000/svg",
			'polyline');
	if (linePhantomFly) {
		if (type === "SVGPolylineElement") {
			linePhantomFly.setAttribute("points", points[0] + "," + points[1]);
		} else if (type === "SVGPolygonElement") {
			if (points.length > 2) {
				linePhantomFly.setAttribute("points", points[points.length - 2]
						+ "," + points[points.length - 1] + " " + points[0]
						+ "," + points[1]);
			} else {
				linePhantomFly.setAttribute("points", points[0] + ","
						+ points[1]);
			}
		}

		if (mode.value === "EDIT" || mode.value === "COPY") {
			linePhantomFly.setAttribute('id', "fly" + (points.length / 2));
			linePhantomFly.setAttribute('onmouseover',
					"showCrossPoint(evt.target);");
		} else {
			linePhantomFly.setAttribute('id', "fly1");
		}

		linePhantomFly.setAttribute('fill', 'none');
		linePhantomFly.setAttribute('stroke', 'red');
		linePhantomFly.setAttribute('stroke-width', '' + (1 / scale));

		/*
		 * var setElement =
		 * document.createElementNS("http://www.w3.org/2000/svg", 'set');
		 * setElement.setAttribute('attributeName', 'stroke');
		 * setElement.setAttribute('to', 'yellow');
		 * setElement.setAttribute('begin', createElement.getAttribute("id") +
		 * ".mouseover"); setElement.setAttribute('end',
		 * createElement.getAttribute("id") + ".mouseout");
		 * 
		 * linePhantomFly.appendChild(setElement);
		 */
		cr_ed_Points.appendChild(linePhantomFly);
	}
}

function fillPhantomPointsContainer() {
	for (var i = 0; i < points.length - 1; i += 2) {
		var pointShapeElement = document.createElementNS(
				"http://www.w3.org/2000/svg", 'ellipse');
		pointShapeElement.setAttribute('cx', points[i]);
		pointShapeElement.setAttribute('cy', points[i + 1]);
		pointShapeElement.setAttribute('stroke', "red");
		pointShapeElement.setAttribute('stroke-width', 1 / scale);
		pointShapeElement.setAttribute('fill', "red");
		pointShapeElement.setAttribute('rx', 2 / scale);
		pointShapeElement.setAttribute('ry', 2 / scale);

		pointShapeElement
				.setAttribute(
						'onmouseover',
						"evt.target.setAttribute('rx', '"
								+ 3
								/ scale
								+ "'); evt.target.setAttribute('ry', '"
								+ 3
								/ scale
								+ "'); evt.target.setAttribute('stroke', 'black');  evt.target.setAttribute('fill', 'yellow');");
		pointShapeElement
				.setAttribute(
						'onmouseout',
						"evt.target.setAttribute('rx', '"
								+ 2
								/ scale
								+ "'); evt.target.setAttribute('ry', '"
								+ 2
								/ scale
								+ "'); evt.target.setAttribute('stroke', 'red');  evt.target.setAttribute('fill', 'red');");

		pointShapeElement.setAttribute('opacity', "0.8");

		pointShapeElement.setAttribute('onmousedown',
				"captureFromEditElement(evt);");

		pointShapeElement.setAttribute('ondblclick', "requestForDelete(evt);");
		pointShapeElement.setAttribute('onclick', "requestForDelete(evt);");

		pointShapeElement.setAttribute('id', "php" + (1 + i / 2));

		cr_ed_Points.appendChild(pointShapeElement);
	}
}

function captureFromEditElement(evt) {
	if (!isCaptured) {
		capturedElement = evt.target;
		if (capturedElement) {
			if (capturedElement.getAttribute("id") === "crossPoint") {
				var index = 2 * parseInt((crossLine.getAttribute("id") + "")
						.replace(/\D/g, ''));
				points.splice(index, 0, parseFloat(crossPoint
						.getAttribute("cx")));
				points.splice(index + 1, 0, parseFloat(crossPoint
						.getAttribute("cy")));
				if (createEditShapeInfo) {
					createEditShapeInfo.value = points.join();
				}
			}
			isCaptured = true;
		}
	}
}

var crossPoint = null;
var crossLine = null;

function showCrossPoint(line) {
	if (!mouseDownFlg) {
		if (!crossPoint) {
			crossPoint = document.createElementNS("http://www.w3.org/2000/svg",
					'ellipse');
			crossPoint.setAttribute('stroke', "red");
			crossPoint.setAttribute('stroke-width', 1 / scale);
			crossPoint.setAttribute('fill', "yellow");
			crossPoint.setAttribute('rx', 3 / scale);
			crossPoint.setAttribute('ry', 3 / scale);
			crossPoint.setAttribute('onmouseover',
					"evt.target.setAttribute('opacity', '0.8');");
			crossPoint.setAttribute('onmouseout',
					"evt.target.setAttribute('opacity', '0');");
			crossPoint.setAttribute('id', "crossPoint");
			crossPoint.setAttribute('onmousedown',
					"captureFromEditElement(evt);");
			cr_ed_Points.appendChild(crossPoint);
		}

		crossLine = line;
		crossPoint
				.setAttribute("cx", -translateX / scale + currPoint.x / scale);
		crossPoint
				.setAttribute("cy", -translateY / scale + currPoint.y / scale);
	}
}

function requestForDelete(evt) {
	if (evt.detail == 2 || evt.type == "dblclick") {
		var index = 2 * parseInt((evt.target.getAttribute("id") + "").replace(
				/\D/g, '')) - 2;
		if (points.length > index + 1) {

			points.splice(index, 2);
			createEditShapeInfo.value = points.join();

			clearPhantomsContainer();
			setPointsCreateElement();
			fillPhantomLinesContainer();
			fillPhantomFlyLinesContainer();
			fillPhantomPointsContainer();
		}
	}
}

function moveMap(event) {
	if (event.type === 'mousedown') {

		startPoint.x = event.clientX;
		startPoint.y = event.clientY;

		startArcGisPoint.x = event.clientX;
		startArcGisPoint.y = event.clientY;

		startGooglePoint.x = event.clientX;
		startGooglePoint.y = event.clientY;

		mouseDownFlg = true;
	} else if (event.type === 'mousemove') {

		preClickOnShape = false;

		currPoint.x = event.clientX;
		currPoint.y = event.clientY;

		currArcGisPoint.x = event.clientX;
		currArcGisPoint.y = event.clientY;

		currGooglePoint.x = event.clientX;
		currGooglePoint.y = event.clientY;

		if (mouseDownFlg && isCaptured && createElement && type && points
				&& capturedElement !== createElement) {

			capturedElement.setAttribute("cx", -translateX / scale
					+ currPoint.x / scale);
			capturedElement.setAttribute("cy", -translateY / scale
					+ currPoint.y / scale);

			if (type === "SVGPolylineElement" || type === "SVGPolygonElement") {

				if (capturedElement.getAttribute("id") === "crossPoint") {
					var index = 2 * parseInt((crossLine.getAttribute("id") + "")
							.replace(/\D/g, ''));

					points[index] = -translateX / scale + currPoint.x / scale;
					points[index + 1] = -translateY / scale + currPoint.y
							/ scale;

					var crossLinePoints = crossLine.getAttribute("points")
							.trim().split(/[\s,]+/);
					if (crossLinePoints.length > 5) {
						crossLine
								.setAttribute("points", crossLinePoints[0]
										+ "," + crossLinePoints[1] + " "
										+ points[index] + ","
										+ points[index + 1] + " "
										+ crossLinePoints[4] + ","
										+ crossLinePoints[5]);
					} else {
						crossLine
								.setAttribute("points", crossLinePoints[0]
										+ "," + crossLinePoints[1] + " "
										+ points[index] + ","
										+ points[index + 1] + " "
										+ crossLinePoints[2] + ","
										+ crossLinePoints[3]);
					}

				} else {
					var index = 2 * parseInt((capturedElement
							.getAttribute("id") + "").replace(/\D/g, '')) - 2;
					points[index] = -translateX / scale + currPoint.x / scale;
					points[index + 1] = -translateY / scale + currPoint.y
							/ scale;

					var linePhantom = document.getElementById('phl'
							+ ((index + 2) / 2));
					if (linePhantom) {
						if (points.length > 2) {
							if ((index + 3) < points.length) {
								linePhantom.setAttribute("points",
										points[index] + "," + points[index + 1]
												+ " " + points[index + 2] + ","
												+ points[index + 3]);
							}

						} else {
							linePhantom.setAttribute("points", points[index]
									+ "," + points[index + 1]);
						}
					}

					linePhantom = document.getElementById('phl'
							+ ((index + 2) / 2 - 1));
					if (linePhantom) {
						if (points.length > 2) {
							if ((index - 2) >= 0) {
								linePhantom.setAttribute("points",
										points[index - 2] + ","
												+ points[index - 1] + " "
												+ points[index] + ","
												+ points[index + 1]);
							}
						}
					}
				}

				var needCorrectFly = (mode.value != "EDIT" && mode.value != "COPY")
						|| ((mode.value == "EDIT" || mode.value == "COPY") && capturedElement
								.getAttribute("id") != "crossPoint");
				if (needCorrectFly) {
					if (type === "SVGPolylineElement") {
						linePhantomFly.setAttribute("points",
								points[points.length - 2] + ","
										+ points[points.length - 1]);
					} else if (type === "SVGPolygonElement") {
						linePhantomFly.setAttribute("points",
								points[points.length - 2] + ","
										+ points[points.length - 1] + " "
										+ points[0] + "," + points[1]);
					}
				}

				if (createEditShapeInfo) {
					createEditShapeInfo.value = points.join();
				}

			} else if (type === "SVGEllipseElement") {

				points[0] = -translateX / scale + currPoint.x / scale;
				points[1] = -translateY / scale + currPoint.y / scale;

				if (createEditShapeInfo) {
					createEditShapeInfo.value = points.join();
				}
			}
		} else if (mouseDownFlg) {

			// moveFlg = true;

			difX = currPoint.x - startPoint.x;
			difY = startPoint.y - currPoint.y;

			difArcGisX = startArcGisPoint.x - currArcGisPoint.x;
			difArcGisY = startArcGisPoint.y - currArcGisPoint.y;

			difGoogleX = startGooglePoint.x - currGooglePoint.x;
			difGoogleY = startGooglePoint.y - currGooglePoint.y;

			// translateX += difX;
			// translateY -= difY;

			// shiftX = shiftX + difX / scale;
			// shiftY = shiftY - difY / scale;

			if (isCaptured && capturedElement === createElement) {
				for (var i = 0; i < points.length; i += 2) {
					points[i] = points[i] + difX / scale;
					points[i + 1] = points[i + 1] - difY / scale;
				}
				setPointsCreateElement();
			} else {
				moveFlg = true;
				translateX += difX;
				translateY -= difY;
				shiftX = shiftX + difX / scale;
				shiftY = shiftY - difY / scale;
				groupScale.setAttribute("transform", "translate(" + translateX
						+ "," + translateY + ") scale(" + scale + ")");

				if (linePhantomFly) {
					if (type === "SVGPolygonElement" && points
							&& points.length > 2) {
						linePhantomFly.setAttribute("points", points[0] + ","
								+ points[1] + " " + points[points.length - 2]
								+ "," + points[points.length - 1]);
					} else if (type === "SVGPolylineElement" && points
							&& points.length > 1) {
						linePhantomFly.setAttribute("points", points[0] + ","
								+ points[1]);
					}
				}
			}

			startPoint.x = currPoint.x;
			startPoint.y = currPoint.y;

		} else if (linePhantomFly && mode.value != "EDIT"
				&& mode.value != "COPY") {
			if (type === "SVGPolygonElement" && points) {
				if (points.length > 2) {
					linePhantomFly.setAttribute("points", points[0] + ","
							+ points[1] + " "
							+ (-translateX / scale + currPoint.x / scale) + ","
							+ (-translateY / scale + currPoint.y / scale) + " "
							+ points[points.length - 2] + ","
							+ points[points.length - 1]);
				} else {
					linePhantomFly.setAttribute("points", points[0] + ","
							+ points[1] + " "
							+ (-translateX / scale + currPoint.x / scale) + ","
							+ (-translateY / scale + currPoint.y / scale));
				}
			} else if (type === "SVGPolylineElement" && points) {
				if (points.length > 2) {
					linePhantomFly.setAttribute("points",
							points[points.length - 2]
									+ ","
									+ points[points.length - 1]
									+ " "
									+ (-translateX / scale + currPoint.x
											/ scale)
									+ ","
									+ (-translateY / scale + currPoint.y
											/ scale));
				} else {
					linePhantomFly.setAttribute("points", points[0] + ","
							+ points[1] + " "
							+ (-translateX / scale + currPoint.x / scale) + ","
							+ (-translateY / scale + currPoint.y / scale));
				}
			}
		}
	} else if (event.type === 'mouseup') {
		if (moveFlg) {

			refreshAjaxGis();

			if (arcgisItemChecked) {
				arcGisMap.centerAt(arcGisMap.toMap(arcGisMap.toScreen(
						arcGisMap.extent.getCenter()).offset(difArcGisX,
						difArcGisY)));
			}

			if (googleItemChecked) {
				googleMap.panBy(difGoogleX, difGoogleY);
			}

			shiftArcGisX += difArcGisX / (scale / startScale);
			shiftArcGisY += difArcGisY / (scale / startScale);

			shiftGoogleX += difGoogleX / (scale / startScale);
			shiftGoogleY += difGoogleY / (scale / startScale);

		} else if (mouseDownFlg
				&& isCaptured
				&& (mode.value === "NEW" || mode.value === "EDIT" || mode.value === "COPY")) {

			if (isCaptured && capturedElement === createElement) {

				clearPhantomsContainer();

				if (type === "SVGPolylineElement"
						|| type === "SVGPolygonElement") {
					fillPhantomLinesContainer();
					fillPhantomFlyLinesContainer();
					fillPhantomPointsContainer();
				} else if (type === "SVGEllipseElement") {
					fillPhantomPointsContainer();
				}

				createEditShapeInfo.value = points.join();
			} else {
				setPointsCreateElement();

				if (type === "SVGPolylineElement"
						|| type === "SVGPolygonElement") {
					if (capturedElement.getAttribute("id") === "crossPoint") {
						clearPhantomsContainer();
						fillPhantomLinesContainer();
						fillPhantomFlyLinesContainer();
						fillPhantomPointsContainer();
					}
				}
			}
		} else if (mouseDownFlg && (mode.value === "NEW")) {
			// http://stackoverflow.com/questions/31762733/ellipse-element-created-through-js-doesnt-appear-in-html

			clearPhantomsContainer();

			if (type === "SVGEllipseElement") {
				if (!points || points.length < 2) {
					points = [ , ];
				}
				points[0] = -translateX / scale + startPoint.x / scale;
				points[1] = -translateY / scale + startPoint.y / scale;
				setPointsCreateElement();
			} else if (type === "SVGPolylineElement"
					|| type === "SVGPolygonElement") {
				if (!points || points.length < 2) {
					points = [];
				}
				points.push(-translateX / scale + startPoint.x / scale);
				points.push(-translateY / scale + startPoint.y / scale);
				points = points.join().replace(/^,+/, "").split(",").map(
						function(coord) {
							return parseFloat(coord);
						});

				setPointsCreateElement();
				fillPhantomLinesContainer();
				fillPhantomFlyLinesContainer();
			}

			createEditShapeInfo.value = points.join();
			fillPhantomPointsContainer();

		} else {

		}

		moveFlg = false;
		mouseDownFlg = false;
		isCaptured = false;
	}
}

function cancelBubble(e) {
	var evt = e ? e : window.event;
	if (evt.stopPropagation)
		evt.stopPropagation();
	if (evt.cancelBubble != null)
		evt.cancelBubble = true;
}

// ---------------------------------------------

var typeArcGisMap;
var typeGoogleMap = null;
var arcGisMapValid = false;
var googleMapValid = false;

function refreshLiner() {
	arcgisItemChecked = document.getElementById('currentLiner:1').checked;
	googleItemChecked = document.getElementById('currentLiner:2').checked;

	if (arcgisItemChecked) {

		if (googleMapValid && googleMap) {
			googleMapValid = false;
			googleMap.setCenter({
				lat : 49.79345,
				lng : 30.14247
			});
			document.getElementById('googleMap').style.display = "none";
		}

		if (!arcGisMapValid) {
			require(
					[ // "esri/map" // , "dojo/domReady!"

					"esri/map", "esri/layers/FeatureLayer",
							"esri/layers/ArcGISTiledMapServiceLayer",
							"esri/tasks/query",
							"esri/symbols/SimpleFillSymbol",
							"esri/symbols/SimpleLineSymbol", "esri/graphic",
							"esri/dijit/Popup", "esri/dijit/PopupTemplate",
							"esri/urlUtils", "esri/graphicsUtils",
							"esri/Color", "dojo/on", "dojo/query",
							"dojo/parser", "dojo/dom-construct",

							"dijit/layout/BorderContainer",
							"dijit/layout/ContentPane", "dojo/domReady!",
							"esri/geometry/Point" ],
					function(Map, Point) {
						/*
						 * esriConfig.defaults.map.panDuration = 1; // time in
						 * milliseconds, default panDuration: 350
						 * esriConfig.defaults.map.panRate = 1; // default
						 * panRate: 25 esriConfig.defaults.map.zoomDuration =
						 * 100; // default zoomDuration: 500
						 * esriConfig.defaults.map.zoomRate = 1; // default
						 * zoomRate: 25
						 */

						var bsMap = "satellite";
						if (typeArcGisMap && typeArcGisMap.length > 0) {
							bsMap = typeArcGisMap;
						} else {
							var currentArcGisMap = document
									.getElementById('currentArcGisMap');
							if (currentArcGisMap) {
								typeArcGisMap = currentArcGisMap.options[currentArcGisMap.selectedIndex].value;
								bsMap = typeArcGisMap;
							}
						}

						arcGisMap = new Map("arcGisMap", { // basemap: "osm"
							basemap : bsMap
							// //,extend : extentZ, // center: [30.5238,
							// 50.45466 ]
							,
							center : [ 30.14247, 49.79345 ],
							zoom : zoomArcGis
						});

						arcGisMap
								.on(
										"load",
										function() {
											var center = arcGisMap.extent
													.getCenter();
											arcGisMap
													.centerAt(arcGisMap
															.toMap(arcGisMap
																	.toScreen(
																			center)
																	.offset(
																			shiftArcGisX
																					* (scale / startScale),
																			shiftArcGisY
																					* (scale / startScale))));

											// arcGisMap.disableMapNavigation();
											// arcGisMap.disableRubberBandZoom();
											// arcGisMap.hidePanArrows();
											// arcGisMap.disableKeyboardNavigation();
											// arcGisMap.disableShiftDoubleClickZoom();
											// arcGisMap.disableDoubleClickZoom();
											// arcGisMap.disableClickRecenter();
											// arcGisMap.disableScrollWheelZoom();
											// arcGisMap.disablePan();

											arcGisMap.hideZoomSlider();

										});

					});

			arcGisMapValid = true;
		}

	} else if (googleItemChecked) {
		if (arcGisMapValid) {
			arcGisMapValid = false;
			arcGisMap.destroy();
		}

		if (!googleMap) {
			loadScript(
					"http://maps.googleapis.com/maps/api/js?key=AIzaSyAt20ckLTpDbQpdO72rxZX0tutaPAudFfM&callback=initGoogleMap",
					null);
		} else if (!googleMapValid) {
			googleMapValid = true;
			document.getElementById('googleMap').style.display = "block";
			// must set Center for correct on resize
			googleMap.setCenter({
				lat : 49.79345,
				lng : 30.14247
			});
			var currCenter = googleMap.getCenter();
			google.maps.event.trigger(googleMap, 'resize');
			googleMap.setCenter(currCenter);
			googleMap.panBy(shiftGoogleX * (scale / startScale), shiftGoogleY
					* (scale / startScale));
		}
	} else {
		if (googleMapValid && googleMap) {
			googleMapValid = false;
			googleMap.setCenter({
				lat : 49.79345,
				lng : 30.14247
			});
			document.getElementById('googleMap').style.display = "none";
		}
		if (arcGisMapValid) {
			arcGisMapValid = false;
			arcGisMap.destroy();
		}
	}
}

function setOpacity(sliderInputId, targetId) {
	var alfa = (100 - document.getElementById(sliderInputId).value) / 100;
	// document.getElementById(targetId).style.backgroundColor = "rgba(245, 225,
	// 192, " + alfa + ")";
	document.getElementById(targetId).style.backgroundColor = "rgba(243, 206, 151, "
			+ alfa + ")";
}

function changeArcGisMap(valueArcGisMap) {
	typeArcGisMap = valueArcGisMap;
	if (arcGisMap && arcGisMapValid) {
		arcGisMap.setBasemap(valueArcGisMap);
	}
}

function changeGoogleMap(valueGoogleMap) {
	switch (valueGoogleMap) {
	case "SATELLITE":
		typeGoogleMap = google.maps.MapTypeId.SATELLITE;
		break;
	case "ROADMAP":
		typeGoogleMap = google.maps.MapTypeId.ROADMAP;
		break;
	case "HYBRID":
		typeGoogleMap = google.maps.MapTypeId.HYBRID;
		break;
	case "TERRAIN":
		typeGoogleMap = google.maps.MapTypeId.TERRAIN;
		break;
	default:
		typeGoogleMap = google.maps.MapTypeId.SATELLITE;
	}

	if (googleMap && googleMapValid) {
		googleMap.setMapTypeId(typeGoogleMap);
	}
}

function loadScript(url, callback) {
	// Adding the script tag to the head as suggested before
	var head = document.getElementsByTagName('head')[0];
	var script = document.createElement('script');
	script.type = 'text/javascript';
	script.src = url;

	// Then bind the event to the callback function.
	// There are several events for cross browser compatibility.
	// script.onreadystatechange = callback;
	// script.onload = callback;

	// Fire the loading
	head.appendChild(script);
}

function initGoogleMap() {

	var typeGoogleMapId = google.maps.MapTypeId.SATELLITE;
	var needChangeMapType = false;
	if (typeGoogleMap) {
		typeGoogleMapId = typeGoogleMap;
	} else {
		needChangeMapType = true;
	}

	googleMap = new google.maps.Map(document.getElementById('googleMap'), {
		center : {
			lat : 49.79345,
			lng : 30.14247
		},
		zoom : zoomGoogle,
		mapTypeId : typeGoogleMapId,
		disableDefaultUI : true,
		draggable : false
	});

	// var marker = new google.maps.Marker({
	// position : googleMap.getCenter(),
	// icon : {
	// path : google.maps.SymbolPath.CIRCLE,
	// scale : 3
	// },
	// draggable : true,
	// map : googleMap
	// });

	if (googleMap) {
		/*
		 * googleMap.addListener('center_changed', function() {
		 * 
		 * console.log("center_changed"); // 3 seconds after the center of the
		 * map has changed, pan back to the // marker.
		 * window.setTimeout(function() { map.panTo(marker.getPosition()); },
		 * 3000); });
		 */

		googleMap.panBy(shiftGoogleX * (scale / startScale), shiftGoogleY
				* (scale / startScale));
		googleMapValid = true;
		if (needChangeMapType) {
			var currentGoogleMap = document.getElementById('currentGoogleMap');
			if (currentGoogleMap) {
				changeGoogleMap(currentGoogleMap.options[currentGoogleMap.selectedIndex].value);
			}
		}
	}
}

function ungroup(sequenceRow, gisgraphId) {

	var listRowMask = sequenceRow.split(/[\s,]+/);
	if (listRowMask.length >= 3) {
		// var parentRowNum = parseInt(listRowMask[listRowMask.length - 2]);
		document.getElementById('ungroupInfo').value = listRowMask[listRowMask.length - 2]
				+ "," + gisgraphId;
		document.getElementById('ungroupButton').click();
	}
}

var findGroupTabWidth = 0;
var findGroupTabHeight = 0;
var userGisWindowWidth;
var userGisWindowHeight;
function resizeWindow() {
	var newHeight = document.getElementById("findGroupTab").clientHeight;
	var newWidth = document.getElementById("findGroupTab").clientWidth;
	if (newHeight != findGroupTabHeight && newHeight > 0) {
		document.getElementById('documentSizeInfo').value = newWidth + ","
				+ newHeight;
		document.getElementById('documentSizeButton').click();
	}
	if (newWidth != findGroupTabWidth) {
		// console.log("screen.width != screenWidth");
	}

	findGroupTabWidth = newWidth;
	findGroupTabHeight = newHeight;

	newHeight = userGisWindow.clientHeight;
	newWidth = userGisWindow.clientWidth;
	if (userGisWindowWidth && userGisWindowHeight) {
		if (newWidth != userGisWindowWidth || newHeight != userGisWindowHeight) {
			shiftX = (translateX - (-userGisWindow.clientWidth / 2)
					* (scale - 1))
					/ scale;
			shiftY = (translateY - (-userGisWindow.clientHeight / 2)
					* (scale - 1))
					/ scale;

			shiftArcGisX += (newWidth - userGisWindowWidth)
					/ (scale / startScale) / 2;
			shiftArcGisY += (newHeight - userGisWindowHeight)
					/ (scale / startScale) / 2;
			if (arcGisMap && arcGisMapValid) {
				arcGisMap.resize();
			}

			shiftGoogleX += (newWidth - userGisWindowWidth)
					/ (scale / startScale) / 2;
			shiftGoogleY += (newHeight - userGisWindowHeight)
					/ (scale / startScale) / 2;
			if (googleMap && googleMapValid) {
			}

			refreshAjaxGis();
		}
	}

	userGisWindowWidth = newWidth;
	userGisWindowHeight = newHeight;
}

function resizeSlider() {

	var newWidth = userGisWindow.clientWidth;
	if (newWidth != userGisWindowWidth) {
		shiftX = (translateX - (-userGisWindow.clientWidth / 2) * (scale - 1))
				/ scale;
		shiftArcGisX += (newWidth - userGisWindowWidth) / (scale / startScale)
				/ 2;

		shiftGoogleX += (newWidth - userGisWindowWidth) / (scale / startScale)
				/ 2;
		if (googleMap && googleMapValid) {
			google.maps.event.trigger(googleMap, 'resize');
		}

		if (arcGisMap && arcGisMapValid) {
			arcGisMap.resize();
		}
		refreshAjaxGis();
	}

	userGisWindowWidth = newWidth;
}
