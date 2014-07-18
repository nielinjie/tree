window.geo={};

(function() {


    window.geo.watchPosition=function(callback,err) {
        if (navigator.geolocation) {
                navigator.geolocation.watchPosition(callback);
            } else {
                err("Geolocation is not supported by this browser.");
            }
    }

    window.geo.getPosition=function(callback,err){
       if (navigator.geolocation) {
                   navigator.geolocation.getCurrentPosition(callback);
               } else {
                   err("Geolocation is not supported by this browser.");
               }
    }

    window.geo.gpsToBaidu=function(gpsPoint,callback){
        BMap.Convertor.translate(gpsPoint,0,callback);
    }

    window.geo.getBaiduLocation=function(callback,err){
            window.geo.getLocation(function(gpsP){
                window.geo.gpsToBaidu(gpsP,callback)
            },err)
        }



})()

