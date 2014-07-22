window.marks={
    "type.self":function(self){
        var marker = new BMap.Marker()
        var icon=new BMap.Icon("./img/"+window.art["type.self"],new BMap.Size(30,30))
        icon.imageSize=new BMap.Size(30,30)
        icon.setAnchor(new BMap.Size(15,30))
        marker.setIcon(icon)
        window.map.markMap(window.ma,"self",marker,self.position)

        var range = new BMap.Circle(null,self.visualRange,{strokeWeight:1,fillColor:"MediumAquaMarine",fillOpacity:0.5});
        window.map.markMap(window.ma,"self-range",range,self.position)
    },
    "type.pole":function(pole){
        var marker = new BMap.Marker()
        var icon=new BMap.Icon("./img/"+window.art["type.pole"],new BMap.Size(30,30))
        icon.imageSize=new BMap.Size(30,30)
        icon.setAnchor(new BMap.Size(15,30))
        marker.setIcon(icon)
        window.map.markMap(window.ma,"pole-"+pole.id,marker,pole.position)

        var range = new BMap.Circle(null,pole.range,{strokeWeight:1,fillColor:"MediumAquaMarine",fillOpacity:0.5});
        window.map.markMap(window.ma,"pole-range-"+pole.id,range,pole.position)
    },
    "type.gold":function(obj){
        var marker = new BMap.Marker()
        var icon=new BMap.Icon("./img/"+window.art["type."+obj.type],new BMap.Size(30,30))
        icon.imageSize=new BMap.Size(30,30)
        icon.setAnchor(new BMap.Size(15,30))
        marker.setIcon(icon)
        window.map.markMap(window.ma,obj.id,marker,obj.position)
    },
}