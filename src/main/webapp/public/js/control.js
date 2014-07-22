
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

    window.menu={}
    window.menu.refresh=function(){

        var self=$("#menu ul.fixed [template]")
        $(self).html(window.template("self.detail")(window.self))


        var list=window.template("items.list")
        var ul=$("#menu ul.items")
        _(window.items).each(function(item){
            ul.append($(list(item)))
        })
        var items=$("#menu ul.items [template]")
        _(items).each(function(jq){
            var tn=$(jq).attr("template")
            var id=$(jq).attr("data-id")
            $(jq).html(window.template(tn)(_(window.items).find(function(item){
                return item.id==id
            })))
        })
        $("#menu img").closest("div").click(function(){
                var id=$(this).closest("li").attr("data-id")
                $("#menu .detail[data-id="+id+"]").toggleClass("hide")
                $(this).closest("li").find("span.glyphicon").toggleClass("glyphicon-chevron-right glyphicon-chevron-down")
        })

        window.setupArt($("#menu"))
        window.setupInterface($("#menu"))
        window.setupAction($("#menu"))
    }


})()


$(function(){
    window.menu.refresh()
    window.setupArt()
    window.setupInterface()
})
