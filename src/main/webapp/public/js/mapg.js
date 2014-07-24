window.map={markers:{},overlays:{}};
(function(){
    function getRemote(url) {
                    return $.ajax({
                        type: "GET",
                        url: url,
                        async: false
                    }).responseText;
                }
    var toGaode=function(gps){
        var url="http://ditujiupian.com/service/api.ashx?key=8df54dbbdc95420eb09bfe0dc1c70ea1&type=wgs2gcj&lng="+gps.getLng()+"&lat="+gps.getLat()
        var re= getRemote(url)
        return re
    }
    window.map.init=function(po){
        var xx=po.coords.longitude
        var yy=po.coords.latitude
        window.ma = new AMap.Map("allmap",{
            view: new AMap.View2D({
              center:new AMap.LngLat(xx,yy),//地图中心点
              zoom:16 //地图显示的缩放级别
            })
          });
    }
    window.map.showPosition=function(position) {
        var x = document.getElementById("positionText");

        x.innerHTML=position.coords.longitude +" - "
        + position.coords.latitude  + " - "
        + new Date(position.timestamp).toTimeString()
    }

    window.map.setCenter=function(ma,position){
        var xx=position.coords.longitude
        var yy=position.coords.latitude
        var gpsPoint = new AMap.LngLat(xx,yy);
        ma.setCenter(toGaode(gpsPoint))
    }
    window.map.markMap=function(ma,markName,marker,position,setCenter){
        var xx=position.coords.longitude
        var yy=position.coords.latitude

        var gpsPoint = new AMap.LngLat(xx,yy);


        if(m = window.map.markers[markName]){
            if(m.setPosition){
                m.setPosition(toGaode(gpsPoint))
            }else if(m.setCenter){
                m.setCenter(toGaode(gpsPoint))
            }
            m.draw()
            if(macc=window.map.markers[markName+"-accuracy"] ){
                macc.setCenter(toGaode(gpsPoint))
                macc.setRadius(position.coords.accuracy)
            }
        }else{
            //TODO support more mark
            if(marker.setPosition){
                marker.setPosition(toGaode(gpsPoint))
            }else if(marker.setCenter){
                marker.setCenter(toGaode(gpsPoint))
            }
            window.map.markers[markName]=marker
            marker.setMap(ma)
            if(position && position.coords.accuracy){
                var acc = new AMap.Circle({
                    center:toGaode(gpsPoint),
                    radius:position.coords.accuracy,
                    strokeWeight:1,
                    strokeColor:"#00FFFF",
                    strokeOpacity:0.5,
                    fillColor: "#00FFFF", //填充颜色
                       fillOpacity: 0.35//填充透明度
                    });
                window.map.markers[markName+"-accuracy"]=acc
                acc.setMap(ma)
            }
        }

        if(setCenter){
            ma.setCenter(toGaode(gpsPoint)); //setCenter will force overlays redraw
        }
    }


    /*
    position -
        coords: Coordinates
            accuracy: 65
            altitude: null
            altitudeAccuracy: null
            heading: null
            latitude: 30.631243985803238
            longitude: 104.14813821421157
            speed: null
        __proto__: CoordinatesPrototype
        timestamp: 427478092009
    */

    //TODO cache baidu point to save a call?
})()