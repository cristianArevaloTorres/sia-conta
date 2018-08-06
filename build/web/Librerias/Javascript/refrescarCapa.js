ns4 = (document.layers)? true:false
ie4 = (document.all)? true:false
nn6 = (document.getElementById && !document.all);
ie5 = (document.all && document.getElementById);


function getObjeto( elemID )
{
    var obj
    if (document.all) {
        //alert( elemID + ' - ' + document.all(elemID) );
        obj = document.all(elemID)
    } else if (document.layers) {
        //alert( elemID + ' - ' + document.layers[elemID] );
        obj = document.layers[elemID]
    } else if (document.getElementById) {
        //alert( elemID + ' - ' + document.getElementById(elemID) );
        obj = document.getElementById(elemID)
    }
    
    return obj;
}

function getObjetoDoc( doc, elemID )
{
    var obj
    if (doc.all) {
        //alert( elemID + ' - ' + document.all(elemID) );
        obj = doc.all(elemID)
    } else if (doc.layers) {
        //alert( elemID + ' - ' + document.layers[elemID] );
        obj = doc.layers[elemID]
    } else if (doc.getElementById) {
        //alert( elemID + ' - ' + document.getElementById(elemID) );
        obj = doc.getElementById(elemID)
    }
    
    return obj;
}


function loadSource(id,nestref, url, parametros) {
  url= url+ parametros;
	if (ns4) {
          //alert( 'ns4' );
            var lyr = (nestref)? eval('document.'+nestref+'.document.'+id) : document.layers[id]
                lyr.load(url,lyr.clip.width);
	}
	else   
	  if (ie4) {  
            //alert( 'ie4' );
              bufferCurp.document.location = url;
          }
          else { 
            if(ie5 || nn6) {
              //alert( getObjeto('bufferCurp') );
               //document.getElementById('bufferCurp').src = url;
               getObjeto('bufferCurp').src = url;
            } //if ie4  
  	}; // if ns4
}//loadSource

function loadSourceFinish(id) {
	if (ie4) {
          //alert( 'ie4' );
          document.all[id].innerHTML = bufferCurp.document.body.innerHTML;
        }
        else  {
          if(ie5||nn6) {
            //alert( 'ie5 - nn6' );
            //document.getElementById(id).innerHTML = window.frames['bufferCurp'].document.getElementById('theBody').innerHTML;
            //alert( window.frames['bufferCurp'] );
            //alert( window.frames['bufferCurp'].document );
            
            //getObjeto(id).innerHTML = window.frames['bufferCurp'].document.body.innerHTML;
            getObjeto(id).innerHTML = document.getElementsByTagName( "iframe" )[ 0 ].contentDocument.documentElement.innerHTML;
          }
       }  //if ie4 
}//loadSourceFinish


