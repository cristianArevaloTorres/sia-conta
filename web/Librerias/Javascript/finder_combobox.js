//         <input name="searchText" type="text" class='cajaTexto' id="searchText" onKeyUp="findText(this, document.forma.testigosDescargo);" size="60" maxlength="60">
<!--
/***************************************************
This function takes whatever is in the text box and
uses a regular expression to find it within the
select box.
***************************************************/

function findText(finder_textbox, finder_selectbox)
{
    var searchStr = finder_textbox.value;
	var myExp = new RegExp(("^" + searchStr), "i");
	var foundResult = false;
	var i=0;
        var tecla=event.keyCode;

	if(((tecla>=48 && tecla<=57) || (tecla >=65 && tecla<=90)) ||tecla==32||tecla==190||tecla==8||tecla==192)	{
	  window.status= "Buscar: "+ searchStr+ " Tecla: "+ tecla;
		while ((foundResult = false) || (i < finder_selectbox.options.length))
		{
			if( myExp.test(finder_selectbox.options[i].text))
			{
				finder_selectbox.options[i].selected = true;
				foundResult = true;
				break;//Este es el que tienes que insertar.
			}
			i++;
		}
	}
}
