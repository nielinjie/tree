
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
//TODO need keep sync with server side action.
window.actions={
        bless:function(sub){
            window.message.message($("<div/>").text("Will you bless? "+window.self.id+" -> "+sub)
                            ,window.message.confirm(function(ok,div){
                                if(ok){
                                    if(true){//TODO post action
                                        window.message.success("Bless finished: "+window.self.id+" -> "+sub)
                                    }else{
                                        window.message.failed("Bless failed: "+window.self.id+" -> "+sub)
                                    }
                                }else{
                                }
                            }))
        },
        curse:function(sub){
            window.message.failed("Curse failed: "+window.self.id+" -> "+sub)
        }
    }