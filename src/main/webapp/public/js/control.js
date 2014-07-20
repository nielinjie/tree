
(function(){
    window.setupInterface=function(){
        $("*[interface]").each(function(i,v){
            var key=$(v).attr("interface")
            if(key){
                $(v).text(window.interface[key])
            }
        })
    }
    window.setupArt=function(){
        $("*[art]").each(function(i,v){
            var key=$(v).attr("art")
            if(key){
                $(v).attr("src","./img/"+window.art[key])
            }
        })
    }
})()


$(function(){
    $("#menu img").click(function(){
        var id=$(this).closest("li").attr("data-id")
        $("#menu .detail[data-id="+id+"]").toggleClass("hide")
    })

    window.setupArt()
    window.setupInterface()
})
