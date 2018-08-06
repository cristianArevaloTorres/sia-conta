ns4 = (document.layers)? true:false
ie4 = (document.all)? true:false
function loadSource(id,nestref,url,bufferFrame) {
	if (ns4) {
		var lyr = (nestref)? eval('document.'+nestref+'.document.'+id) : document.layers[id]
		lyr.load(url,lyr.clip.width)
	}
	else if (ie4) {
	    bufferFrame.document.location = url
		//parent.bufferFrame.document.location = url
	}
}
function loadSourceFinish(id, bufferFrame) {
	//if (ie4) document.all[id].innerHTML = parent.bufferFrame.document.body.innerHTML
	if (ie4) document.all[id].innerHTML = bufferFrame.document.body.innerHTML
}
