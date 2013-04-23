function updateMarkers(markersArray){
    infowindow.close();
    for( var i=0; i<markersArray.length; i++ ){
        marker = markersArray[i];
        showMarker=true;
        if(document.getElementById("dogcheckbox").checked && marker.bv_dog !="1"){
            showMarker=false;
        }
        if(document.getElementById("biocheckbox").checked && marker.bv_bio !="1"){
            showMarker=false;
        }
        if(document.getElementById("wlancheckbox").checked && marker.bv_wlan !="1"){
            showMarker=false;
        }
        if(document.getElementById("glutenfreecheckbox").checked && marker.bv_glutenFree !="1"){
            showMarker=false;
        }
        if(document.getElementById("rollstuhlcheckbox").checked && marker.bv_rollstuhl !="1"){
            showMarker=false;
        }
        if(document.getElementById("vegandeclarecheckbox")){
            if(document.getElementById("vegandeclarecheckbox").checked && (marker.bv_vegan =="1" || marker.bv_vegan =="3" )){
                showMarker=false;
            }
        }
        if(document.getElementById("opencheckbox").checked){
            var currentDate = new Date();
            var currentDay = currentDate.getDay()-1;
            if(currentDay == -1) currentDay = 6; // sonntag
	    var afterMidnight = false;
	    if(currentDate.getHours()<6) afterMidnight = true; // vor 6 Uhr werden oeffnungszeiten vom Vortag genommen            
	    var currentDayMinute = currentDate.getHours() * 60 + currentDate.getMinutes(); // aktuelle Minute im Tag
	    
	    if(afterMidnight){
	      currentDay--;
	      if(currentDay == -1) currentDay = 6; // vorg�nger vom montag ist sonntag	      
	    }
            timeStr = marker.bv_open[currentDay];
            timeStr = timeStr.split(" ").join(""); // remove spaces
            if(timeStr ==""){
                showMarker=false;
            }else if (!afterMidnight){
                var timeArray = timeStr.split("-");
                var openMinute = getDayMinute(timeArray[0]);
                var closeMinute = 23*60+59; //open end  = bis 0 uhr
                if(timeArray.length>1){
		    if(openMinute < getDayMinute(timeArray[1])){ // nur wenn open < close, setze close, ansonsten ist 0 uhr defautl, ist false z.B. bei 12:00 - 03:00 Uhr ab
		      closeMinute = getDayMinute(timeArray[1]);
		    }
                }
                if(currentDayMinute<openMinute || currentDayMinute>closeMinute){
                    showMarker=false;
                }
            }else{ // nach Mitternacht
		var timeArray = timeStr.split("-");                
                if(timeArray.length>1){ // es gibt eine enduhrzeit
		  if(getDayMinute(timeArray[0])<getDayMinute(timeArray[1])){ // wenn open<close, dann nicht �ber mitternacht auf, daher geschlossen
		    showMarker = false;  
		  }
		  if(currentDayMinute > getDayMinute(timeArray[1])){ // wenn aktuelle stunde, gr��er als enduhrzeit -> geschlossen
		    showMarker = false;
		  }
		}else{ // keine enduhrzeit also geschlossen
		    showMarker = false;
		}
	    }

        }
        if(showMarker){
            if( marker.getMap() == null){// avoid flicker
                marker.setMap(map)
            }
        }else{
            marker.setMap(null)
        }
    }
}

function getDayMinute(time){
    var timeArray = time.split(":"); // z.B: 12:20 Uhr
    dayMinute = timeArray[0] * 60;
    if(timeArray.length>1){
        dayMinute = dayMinute + parseInt(timeArray[1]);
    }
    return dayMinute;
}
//create onDomReady Event
window.onDomReady = initReady;
 
// Initialize event depending on browser
function initReady(fn)
{
    //W3C-compliant browser
    if(document.addEventListener) {
        document.addEventListener("DOMContentLoaded", fn, false);
    }
    //IE
    else {
        document.onreadystatechange = function(){readyState(fn)}
    }
}
 
//IE execute function
function readyState(func)
{
    // DOM is ready
    if(document.readyState == "interactive" || document.readyState == "complete")
    {
        func();
    }
}
//execute as soon as DOM is loaded
window.onDomReady(onReady);
 
//do when DOM is ready
function onReady()
{
    initialize();
}

