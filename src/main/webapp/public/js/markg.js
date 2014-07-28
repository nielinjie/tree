window.marks={
    "type.self":function(self){
        var marker = new AMap.Marker()
        var icon=new AMap.Icon({
            image:"./img/"+window.art["type.self"],
            size:new AMap.Size(30,30),
            imageSize:new AMap.Size(30,30),
//            imageOffset:new AMap.Pixel(15,30)
        })
//        icon.setAnchor(new AMap.Size(15,30))
        marker.setIcon(icon)
        window.map.markMap(window.ma,"self",marker,self.position)
        var range = new AMap.Circle({
            radius:self.visualRange,
            strokeWeight:1,
            strokeColor:"#FA8072",
            strokeOpacity:0.5,
            fillColor:"#FA8072",
            fillOpacity:0.5});
        window.map.markMap(window.ma,"self_range",range,self.position,false)
    },
    "type.pole":function(pole){
        var marker = new AMap.Marker()
                var icon=new AMap.Icon({
                    image:"./img/"+window.art["type.pole"],
                    size:new AMap.Size(30,30),
                    imageSize:new AMap.Size(30,30),
        //            imageOffset:new AMap.Pixel(15,30)
                })
        //        icon.setAnchor(new AMap.Size(15,30))
                marker.setIcon(icon)
                window.map.markMap(window.ma,"pole-"+pole.id,marker,pole.position)
                var range = new AMap.Circle({
                    radius:pole.range,
                    strokeWeight:1,
                    strokeColor:"#FA8072",
                    strokeOpacity:0.5,
                    fillColor:"#FA8072",
                    fillOpacity:0.5});
                window.map.markMap(window.ma,"pole_range-"+pole.id,range,pole.position,false)
    },
    "type.gold":function(obj){
        var marker = new AMap.Marker()
                var icon=new AMap.Icon({
                    image:"./img/"+window.art["type."+obj.type],
                    size:new AMap.Size(30,30),
                    imageSize:new AMap.Size(30,30),
        //            imageOffset:new AMap.Pixel(15,30)
                })
        //        icon.setAnchor(new AMap.Size(15,30))
                marker.setIcon(icon)
                window.map.markMap(window.ma,"obj-"+obj.id,marker,obj.position)

    },
}