window.map={};
(function(){
window.map.showPosition=function(position) {
    var x = document.getElementById("demo");

        x.innerHTML="Latitude: " + position.coords.latitude +
        "<br>Longitude: " + position.coords.longitude +
        "<br>Time: "+ new Date().toTimeString()

        var xx=position.coords.longitude
        var yy=position.coords.latitude

        var gpsPoint = new BMap.Point(xx,yy);


        ma.centerAndZoom(gpsPoint,15)


        //添加谷歌marker和label
        var markergps = new BMap.Marker(gpsPoint);
        ma.addOverlay(markergps); //添加GPS标注
        var labelgps = new BMap.Label("我是GPS标注哦",{offset:new BMap.Size(20,-10)});
        markergps.setLabel(labelgps); //添加GPS标注

        //坐标转换完之后的回调函数
        translateCallback = function (point){
            var marker = new BMap.Marker(point);
            ma.addOverlay(marker);
            var label = new BMap.Label("我是百度标注哦",{offset:new BMap.Size(20,-10)});
            marker.setLabel(label); //添加百度label
            ma.setCenter(point);
        }

            BMap.Convertor.translate(gpsPoint,0,translateCallback);     //真实经纬度转成百度坐标
            }
})()