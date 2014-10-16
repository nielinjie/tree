window.interface={
        "type.pole":"图腾柱",
        "type.person":"原始人",
        "type.self":"自己",
        "type.gold":"金子",

        "property.item":"物体",
        "property.power":"体力",
        "property.visualRange":"视野",
        "property.range":"范围",
        "property.gift":"神赋",
        "property.score":"神力", //pole's score

        "action.bless":"祝福",
        "action.curse":"诅咒",
        "action.pick":"捡拾",
        "action.create":"修建图腾",
    };

    window.art={
        "type.pole":"pole.png",
        "type.person":"villager.png",
        "type.self":"stoneAxe.png",
        "type.gold":"gold.png"
    };

(function(){

    window.setupInterface=function($$){
            $$=$$|| $("body")
            $("*[interface]",$$).each(function(i,v){
                var key=$(v).attr("interface")
                if(key){
                    $(v).text(window.interface[key])
                }
            })
        }
        window.setupArt=function($$){
            $$=$$|| $("body")
            $("*[art]",$$).each(function(i,v){
                var key=$(v).attr("art")
                if(key){
                    $(v).attr("src","./img/"+window.art[key])
                }
            })
        }


        window.template=function(name){
            //todo template cache?
            function getRemote(url) {
                    return $.ajax({
                        type: "GET",
                        url: url,
                        async: false
                    }).responseText;
                }
            var t=getRemote("./template/"+name+".template.html")
            return _.template(t)
        }
})()