
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
    window.briefAndAction=function(action,properties,okCall){
        var brief=$(window.template("actionBrief."+action)(properties))
        window.message.message(brief,window.message.confirm(function(ok,div){
            if(ok){
                okCall()
            }else{
                window.message.canceled("取消："+window.interface["action."+action])
            }
        }))
    }
})()

//TODO need keep sync with server side action.
window.actions={
        bless:function(obj){
            /*
            {
            "type":"bless",
            "obj":"pole_id",
            "sub":"person_id",
            "position":{"lat":13.13,"long":12.12},
            "time":1406471514137,
            "id":"action"}
            */

            window.briefAndAction("bless",{sub:window.self,obj:window.items[obj],rule:window.rules["bless"]},function(){
                            alert("send")
                        })
        },
        curse:function(obj){
            window.briefAndAction("curse",{sub:window.self,obj:window.items[obj],rule:window.rules["curse"]},function(){
                            alert("send")
                        })
        },
        create:function(){
            /*
            {
            "type":"create",
            "obj":"pole_id",
            "sub":"person_id",
            "name":"the pole",
            "poObj":{"lat":13.13,"long":12.12},
            "position":{"lat":11.11,"long":10.1},
            "time":1406464298193,
            "id":"(uuid)"}
            */

            window.briefAndAction("create",{sub:window.self,rule:window.rules["create"]},function(){
                alert("send")
            })
        },
        pick:function(obj){
                    window.briefAndAction("pick",{sub:window.self,obj:window.items[obj],rule:window.rules["pick"]},function(){
                        alert("send")
                    })
                }
    }
