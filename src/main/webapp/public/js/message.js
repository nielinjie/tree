(function(){
    var addMessage=function(id,html){
        var div=$("<div/>").html(html).addClass("messageD").attr("data-id",id)

        $("#message").append(div)
        $("#message").removeClass("hide")
        return  div
    }
    var removeMessage=function(id){
        $("#message [data-id="+id+"]").remove()
        if($("#message [data-id]").length ==0){
            $("#message").addClass("hide")
        }
    }

    window.message.message=function(html,funDiv){
        funDiv=funDiv || function(div){
            var id=$(div).attr("data-id")
            window.setTimeout(function(){
                removeMessage(id)
            },2000)
        }
        var id=_.uniqueId("message")
        var div=addMessage(id,html)
        funDiv(div)
        return id

    }
    window.message.info=function(errMsg){
        window.message.message($("<div class='text-info'/>").text(errMsg))
    }
    window.message.err=function(errMsg){
        window.message.message($("<div class='text-danger'/>").text(errMsg),window.message.removeButton())
    }
    window.message.success=function(errMsg){
        window.message.message($("<div class='text-success'/>").text(errMsg))
    }
    window.message.failed=function(errMsg){
        window.message.message($("<div class='text-warning'/>").text(errMsg),window.message.removeButton())
    }

    window.message.removeButton=function(){
        return function(messageD){
            var div=messageD
            var id=$(div).attr("data-id")
            var rB=$("<span/>").addClass("glyphicon glyphicon-remove").click(function(){
                removeMessage(id)
            })
            div.append($("<button type='button' class='btn btn-default btn-xs'/>").append(rB))
        }
    }
    /*c
    callback=function(ok?,messageD)
    */
    window.message.confirm=function(callback){
        return function(messageD){
            var div=messageD
            var id=$(div).attr("data-id")
            var oB=$("<span/>").addClass("glyphicon glyphicon-ok").click(function(){
                removeMessage(id)
                callback(true,div)
            })
            var cB=$("<span/>").addClass("glyphicon glyphicon-remove").click(function(){
                removeMessage(id)
                callback(false,div)
            })
            div.append($("<button type='button' class='btn btn-default btn-sm'/>").append(oB))
            .append($("<button type='button' class='btn btn-default btn-sm'/>").append(cB))
        }
    }


})()