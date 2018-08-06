// JavaScript Document

function camb(tis)
{
	tis.T_DELAY=1; 
	tis.T_BORDERCOLOR='#006699'; 
	tis.T_SHADOWCOLOR='#aab0cc'; 
	tis.T_TITLE='Requerimiento Fecha Extraordinaria'; 
	return escape('Para esta fecha será necesario presentar un documento de Justificación.');
}
function selTodos(chec, forma, ide)
{
	if (chec.checked==true)
	bol=true; else bol=false;
	for (var i=0;i < forma.elements.length;i++)
	{
		var elemento = document.forms[0].elements[i];
		if (elemento.type == "checkbox")
		{
			if (ide!='null')
				{if (elemento.id==ide)
			elemento.checked = bol;}
			else
			{elemento.checked = bol;}
		}
	}
}

function nueva(pag) 
{ 
	window.open(pag,'jav','width=700,height=400,resizable=yes'); 
} 
function grupocon(tis)
{
ide=tis.id;
//alert(doc.getElementsByTagName("tr").length);
	for (x=1; x<document.getElementsByTagName("tr").length; x++)
	{
		//alert(document.getElementsByTagName("tr")[x].id);
		if (document.getElementsByTagName("tr")[x].id==ide)
		{
			tis=document.getElementsByTagName("tr")[x];
			coant=tis.style.color;
			tis.style.background='#EEEEEE';
			tis.style.cursor='pointer';
			tis.style.color='#006699';
			tis.style.font.weight='bold';           
		}
	}
}
function gruposin(tis)
{
ide=tis.id;
//alert(doc.getElementsByTagName("tr").length);
	for (x=1; x<document.getElementsByTagName("tr").length; x++)
	{
		//alert(document.getElementsByTagName("tr")[x].id);
		if (document.getElementsByTagName("tr")[x].id==ide)
		{
			tis=document.getElementsByTagName("tr")[x];
			tis.style.background='';
			tis.style.color=coant;
			tis.style.font.weight='normal';
		}
	}
}

var coant;
function conr(tis)
{
coant=tis.style.color;
tis.style.background='#EEEEEE';
tis.style.cursor='pointer';
tis.style.color='#006699';
tis.style.font.weight='bold';
}
function hand(tis)
{
tis.style.cursor='pointer';
}
function sinr(tis)
{
tis.style.background='';
tis.style.color=coant;
tis.style.font.weight='normal';
}
var combo='';
var pagm='';
function combox()
{
	combo='1';
}
function paginam(pag,tis)
{
	pagm=pag;
	tis.checked=false;
}
function ira(forma, pagina)
{
	if (combo=='1')
		combo='0';
	else
	{
		if (pagm!='')
			{forma.action=pagm;}
		else
			{forma.action=pagina;}
		forma.submit(); 
	}
}
function conf()
{
	if (confirm('Desea Eliminar estos Registros ?'))
	alert ('Se han eliminado los registros con exito');
}

      
var ant;
function over(tis) {
  ant = tis.className;
  tis.className = 'resGrisOscuroOver';
}

function out(tis) {
  tis.className = ant;
}
      
