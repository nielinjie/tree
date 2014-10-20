
(function(){


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
                $(this).closest("li").find("span.chevron").toggleClass("glyphicon-chevron-right glyphicon-chevron-down")
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


    $(".setCenter").click(function(){
        window.map.setCenter(window.ma,window.self.position)
    })
})
