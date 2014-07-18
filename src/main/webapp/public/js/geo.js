window.geo={};

(function() {




    window.geo.getLocation=function(callback,err){
       if (navigator.geolocation) {
                   navigator.geolocation.getCurrentPosition(callback);
               } else {
                   err("Geolocation is not supported by this browser.");
               }
    }






})()

