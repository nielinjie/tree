 $(function(){
        var token=$.cookie("access_token")
        $("#loginStatus").text(token?token:":-(")
    })