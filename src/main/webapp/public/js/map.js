window.map={};
(function(){
    window.map.showPosition=function(position) {
        var x = document.getElementById("positionText");

        x.innerHTML=position.coords.latitude +" - "
        + position.coords.longitude + " - "
        + new Date(position.timestamp).toTimeString()
    }

    window.map.markMap=function(ma,position){
        var xx=position.coords.longitude
        var yy=position.coords.latitude

        var gpsPoint = new BMap.Point(xx,yy);

        window.geo.gpsToBaidu(gpsPoint,function(point){
            window.map.markMapByBaiduPoint(ma,point,position)
        })
    }

    window.map.markMapByBaiduPoint=function(ma,baiduPoint,position){
        var marker = new BMap.Marker(baiduPoint);
        ma.addOverlay(marker);
//        var label = new BMap.Label("我是百度标注哦",{offset:new BMap.Size(20,-10)});
//        marker.setLabel(label); //添加百度label
        ma.setCenter(baiduPoint);
    }
})()