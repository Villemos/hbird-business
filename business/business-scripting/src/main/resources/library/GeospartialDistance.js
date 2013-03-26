// This uses the "haversine" formula to calculate the great-circle distance between two
// points – that is, the shortest distance over the earth's surface – giving an "as-the-crow-flies"
// distance between the points (ignoring any hills, of course!).
//
// Source: http://www.movable-type.co.uk/scripts/latlong.html

var R = 6371000; // Mean earth radius in meters

var dLat = (location2.geoLocation.p1 - location1.geoLocation.p1) * Math.PI / 180;
var dLon = (location2.geoLocation.p2 - location2.geoLocation.p2) * Math.PI / 180;

var lat1 = location1.geoLocation.p1 * Math.PI / 180;
var lat2 = location2.geoLocation.p1 * Math.PI / 180;

var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);

var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

var d = R * c;

output.setValue(d);
