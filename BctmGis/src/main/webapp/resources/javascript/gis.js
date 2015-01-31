/**
 * 
 */

function errMessage() {
	alert("Error");
}

jQuery(function($) {
	$('#gisSlider').split({
		orientation : 'vertical',
		limit : 20,
		position : '75%'
	});
});

var zoomRate = 1.5;
var translateX = -700;
var translateY = -500;
var scale = 1;

var groupScale = document.getElementById('groupScale');
var userGisWindow = document.getElementById('userGisWindow');

var count = 0;
var timer;

function setCurrentInventoryId(idInv) {

	var id = parseInt((idInv + "").replace(/\D/g, ''));

	if (id) {
		document.getElementById('currentInventoryId').value = id;
		document.getElementById('getInventoryInfo').click();
	}
}

function getShapeFromLink(idShape) {

	var id = parseInt((idShape + "").replace(/\D/g, ''));

	if (id) {
		document.getElementById('currentId').value = id;
		document.getElementById('getShapeInfo').click();
	}
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

			shiftX = userGisWindow.clientWidth / 2 - currX;
			shiftY = userGisWindow.clientHeight / 2 - currY;

			groupScale.setAttribute("transform", "translate(" + translateX
					+ "," + translateY + ") scale(" + scale + ")");
		}

		setAnimate(dataAjax);
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

	if (dataAjax.status == "success") {
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
	} else if (zoomType == 'zoomOut') {
		scale /= zoomRate;
	}

	translateX = (-userGisWindow.clientWidth / 2) * (scale - 1) + shiftX
			* (scale);
	translateY = (-userGisWindow.clientHeight / 2) * (scale - 1) + shiftY
			* (scale);

	groupScale.setAttribute("transform", "translate(" + translateX + ","
			+ translateY + ") scale(" + scale + ")");
}

var moveFlg = false;
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

function moveMap(event) {
	if (event.type === 'mousedown') {
		moveFlg = true;
		startPoint.x = event.clientX;
		startPoint.y = event.clientY;
	} else if (event.type === 'mousemove') {
		currPoint.x = event.clientX;
		currPoint.y = event.clientY;

		if (moveFlg) {
			difX = currPoint.x - startPoint.x;
			difY = startPoint.y - currPoint.y;

			translateX += difX;
			translateY -= difY;

			shiftX = shiftX + difX / scale;
			shiftY = shiftY - difY / scale;

			groupScale.setAttribute("transform", "translate(" + translateX
					+ "," + translateY + ") scale(" + scale + ")");

			startPoint.x = currPoint.x;
			startPoint.y = currPoint.y;
		}
	} else if (event.type === 'mouseup') {
		if (moveFlg) {
			moveFlg = false;
		}
	}
}