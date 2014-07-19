window.map={markers:{}};
(function(){
    window.map.init=function(){
        window.ma = new BMap.Map("allmap");            // 创建Map实例
        var style={}
        style.style="light"
        style.features=["road","water","land","building"]
        window.ma.setMapStyle(style)
        var point = new BMap.Point(104.15701141457,30.63501858048);    // 创建点坐标
        window.ma.centerAndZoom(point,17);                     // 初始化地图,设置中心点坐标和地图级别。
        window.ma.enableScrollWheelZoom();                            //启用滚轮放大缩小
    }
    window.map.showPosition=function(position) {
        var x = document.getElementById("positionText");

        x.innerHTML=position.coords.longitude +" - "
        + position.coords.latitude  + " - "
        + new Date(position.timestamp).toTimeString()
    }


    window.map.markMap=function(ma,markName,position,setCenter){
        var xx=position.coords.longitude
        var yy=position.coords.latitude

        var gpsPoint = new BMap.Point(xx,yy);

        window.geo.gpsToBaidu(gpsPoint,function(point){
            window.map.markMapByBaiduPoint(ma,markName,point,position,setCenter)
        })
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
    window.map.markMapByBaiduPoint=function(ma,markName,baiduPoint,position,setCenter){//position is for
        if(window.map.markers[markName]){
            window.map.markers[markName].setPosition(baiduPoint)
            window.map.markers[markName+"-accuracy"].setCenter(baiduPoint)
        }else{
            var marker = new BMap.Marker(baiduPoint)
            var icon=new BMap.Icon('./img/stoneAxe.png',new BMap.Size(30,30))
            icon.imageSize=new BMap.Size(30,30)
            icon.setAnchor(new BMap.Size(15,30))
            marker.setIcon(icon)
            window.map.markers[markName]=marker
            ma.addOverlay(marker);
            if(position||position.coords.accuracy){
                var acc = new BMap.Circle(baiduPoint,position.coords.accuracy,{strokeWeight:2,fillColor:"lightBlue",fillOpacity:0.2});
                window.map.markers[markName+"-accuracy"]=acc
                ma.addOverlay(acc);
            }
        }
        if(setCenter){
            ma.setCenter(baiduPoint);
        }
    }



})()