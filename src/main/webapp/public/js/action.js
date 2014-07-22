
(function(){

    window.setupAction=function($$){
        $$=$$|| $("body")
        $("*[data-action]",$$).each(function(i,v){
            $(v).click(function(){
                var action=$(v).attr("data-action")
                var id=$(v).closest("[data-id]").attr("data-id")
                window.actions[action](id);
            })
        })
    }
})()

window.actions={
        bless:function(sub){
            window.message.message($("<div/>").text("Will you bless? "+window.self.id+" -> "+sub)
                            ,window.message.confirm(function(ok,div){
                                if(ok){
                                    if(true){//post action
                                        window.message.message("Bless finished: "+window.self.id+" -> "+sub)
                                    }else{
                                        window.message.message("Bless failed: "+window.self.id+" -> "+sub,window.message.removeButton())
                                    }
                                }else{
                                }
                            }))
        },
        curse:function(sub){
            window.message.message($("<div/>").text("Curse failed: "+window.self.id+" -> "+sub)
                ,window.message.removeButton())
        }
    }