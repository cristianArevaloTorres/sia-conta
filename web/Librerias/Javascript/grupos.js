	function refrescarCapa(op, cve_grupo, nuevo_grupo) {
    loadSource('contenido',null,'grupos_perfiles.jsp','?buscar='+op+'&'+'cve_grupo='+cve_grupo+'&'+'nuevo_grupo='+nuevo_grupo);
  }

  function VerificaChecks(CB){
    Clave=CB.alt;
    Checked = CB.checked;
    var cve = new Array();
    cve[1] = Clave.substring(0,2);
    cve[2] = Clave.substring(2,4);
    cve[3] = Clave.substring(4,6);
    cve[4] = Clave.substring(6,8);
    cve[5] = Clave.substring(8,10);
    cve[6] = Clave.substring(10,12);
    var x=6;
    while (cve[x]=='00') {x--;}
    x--;
    var Objeto;		
    if (Checked){
      for(var indiceObj=0; indiceObj<forma.elements.length; indiceObj++){
        Objeto= forma.elements[indiceObj];
        cve_padre="";
        for (y=1;y<=x;y++){
          cve_padre+=cve[y];
          cve_padre_ceros=cve_padre;
          while (cve_padre_ceros.length<10) { cve_padre_ceros+="0";}
          if ( (Objeto.type=="checkbox") && (Objeto.alt==cve_padre_ceros) )	Objeto.checked=true;			
        }
        cve_padre+=cve[x+1];
        if ( (Objeto.type=="checkbox") && (Objeto.alt.indexOf(cve_padre)==0)) Objeto.checked=true;
      }
    }else{
      x++;
      cve_hijo="";
      for (y=1;y<=x;y++){cve_hijo+=cve[y]}
      for(var indiceObj=0; indiceObj<forma.elements.length; indiceObj++){
        Objeto= forma.elements[indiceObj];
        if ( (Objeto.type=="checkbox") && (Objeto.alt.indexOf(cve_hijo)==0) )
          {Objeto.checked=false;}
      }
    }	
  }

  function Busqueda(Formulario, Pagina, orden) {
    var valor= prompt("Por favor teclee la palabra de búsqueda", "[Teclee aquí su texto]");
    var signo = "";
    if (valor!= null) {
      if (Pagina.indexOf("?")<0) 
        signo="?";  
      else 
        signo="&";  
//      alert(Pagina+signo+"txt_busqueda="+ valor+ "&orden="+ orden);  
      Formulario.action=Pagina+signo+"txt_busqueda="+ valor+ "&orden="+ orden;  
      Formulario.submit();

    };
  }

  function EnviarDatos(Formulario, Pagina){
 		Formulario.action=Pagina;
		Formulario.submit();
  }

	function EnviarDatosConfirmar(Formulario, pagina, mensaje) {
		if (confirm(mensaje)) {
			Formulario.action=pagina;
 			Formulario.submit();	    
		}		
	}
	
  function EnviarDatosModulos(Formulario,pagina) {
    var opciones = Formulario.CBArbol.length;
		var c=false;
		for(var x=0;x<opciones;x++)  {
      if(Formulario.CBArbol[x].checked)
        c=true;
		}
		if (confirm("úmero seguro de actualizar las opciones?")) {
			if (c) {
	  		Formulario.action=pagina;
  	 		Formulario.submit();	
			}	else {
				alert("No puede actualizar un grupo o perfil sin opciones seleccionadas");
			}	
    }
  }
	 
  function EnviarDatosValor(Formulario,pagina, mensaje, objeto) {
		if (confirm(mensaje)) {
			if (objeto.value.length<1) {
				alert("Debe introducir un valor v&aacute;lido.");
			}			
			else {
	    	Formulario.action=pagina;
  	  	Formulario.submit();	    
			}	
		}
  }
	 
  function cambiarImagen(tabla, imagen) {
    if(tabla.style.display=='none') {
      tabla.style.display='';
      imagen.title= 'Ocultar opciones'; 
      imagen.src= '../Icons/previous.gif';   
    }  
    else {
      tabla.style.display='none';
      imagen.title= 'Ver opciones'; 
      imagen.src   = '../Icons/next.gif';   
    }; 
  }; 

  function cambiarImagenFolder(tabla, imagen) {
    if(tabla.style.display=='none') {
      tabla.style.display='';
      imagen.title='Ocultar opciones'; 
      imagen.src='../Icons/folderopen.gif';   
    }  
    else {
      tabla.style.display='none';
      imagen.title='Ver opciones'; 
      imagen.src='../Icons/folder.gif';   
    }; 
  }; 

  function EnviarDatosFinal(Formulario,pagina) {
    if (confirm("úmero seguro de actualizar el grupo?")) {
      if (Formulario.TextNombre.value.length<1) {
				alert("Debe incluir un nuevo nombre para el perfil");
			}			
			else {
	    	Formulario.action=pagina;
  	  	Formulario.submit();	
			}	
		}
  }

  function EnviarDatosPassword(Formulario,pagina) {
    if (Formulario.password_new.value.length<1 || Formulario.password_new2.value.length<1) {
      alert("Debe incluir una nueva contraseña para el usuario.");
		}	else {
      if ( Formulario.password_new.value != Formulario.password_new2.value ) {
        alert("Error al escribir la contraseña.");
				Formulario.password_new.focus();
			}	else
        if (confirm("úmero seguro de actualizar la contraseña del usuario?")) {
					Formulario.action=pagina;
					Formulario.submit();
				}
    }
  }

  function EnviarDatosGuardarComo(Formulario,pagina) {
    if (document.forma.selectPerfiles.length<2) {
      alert("Debe incluir al menos dos perfiles.");
    } else {
      if (document.forma.grupo_ultimo.value=="" && document.forma.TextGrupoNuevo.value=="") {
        alert("Debe incluir un nuevo nombre para el nuevo grupo.");
      } else {
        if (Formulario.TextNombre.value.length<1) {
          alert("Debe incluir un nuevo nombre para el perfil.");
        } else {
          if (confirm("úmero seguro de actualizar el perfil?")) {
            for (i=0; i < document.forma.selectPerfiles.length; i++) {
              document.forma.selectPerfiles.options[i].selected=true;
            }
            Formulario.action=pagina;
            Formulario.submit();	
          }
        }
			}	
		}
  }

  function agregarPerfiles(grupo_original, nuevo_grupo_bnd) {
    var grupos = new Array(document.forma.selectPerfilesPorGrupo.length);
    for (i=0;i < document.forma.selectPerfilesPorGrupo.length; i++) {
      if (document.forma.selectPerfilesPorGrupo.options[i].selected) {
        var bnd = true;
        for (j=0; j < document.forma.selectPerfiles.length; j++) {
          if (document.forma.selectPerfilesPorGrupo.options[i].value==document.forma.selectPerfiles.options[j].value) {
            bnd=false;
            break;
          }
        }
        if (bnd) {
           grupos[cnt]=grupo;
           document.forma.selectPerfiles.options[document.forma.selectPerfiles.length]=new Option(document.forma.selectPerfilesPorGrupo.options[i].text,document.forma.selectPerfilesPorGrupo.options[i].value,false, false);
           cnt++;
        }
      }
    }
    var cnt = 0;
    for (i=0; i < document.forma.selectPerfiles.length; i++) {
      var grupo = (document.forma.selectPerfiles.options[i].value).substring(0,document.forma.selectPerfiles.options[i].value.indexOf("~"));
      grupos[cnt] = grupo;
      cnt++;
    }

    var nuevo_grupo = "";
    var total_grupos = new Array(document.forma.selectPerfiles.length);
    var cnt2=0;
    for (j=0; j < cnt; j++) {
      if (nuevo_grupo!=grupos[j]) {
         nuevo_grupo=grupos[j];
         total_grupos[cnt2]=nuevo_grupo;
         cnt2++;
      }
    }
    var grupo_ultimo="";
    var bnd_calcular_grupo=false;
    if (cnt2<=1) {
      if (grupo_original!=total_grupos[0]) {
        grupo_ultimo=total_grupos[0];
      }
      else {
        grupo_ultimo = grupo_original;
      }
      bnd_calcular_grupo=false;
    }
    else {
      grupo_ultimo="";
      bnd_calcular_grupo=true;
    }
    if (!bnd_calcular_grupo) {
      TGrupoNuevo.style.display="none";
    }
    else {
      TGrupoNuevo.style.display='';
    }
    document.forma.grupo_ultimo.value=grupo_ultimo;
    document.forma.bnd_calcular_grupo.value=bnd_calcular_grupo;
  }

  function eliminarPerfiles(grupo_original, nuevo_grupo_bnd) {
    for (i=0;i < document.forma.selectPerfiles.length; i++) {
      if (document.forma.selectPerfiles.options[i].selected) {
           document.forma.selectPerfiles.options[i]=null;
      }
    }
    var cnt = 0;
    var grupos = new Array(document.forma.selectPerfiles.length);
    for (i=0; i < document.forma.selectPerfiles.length; i++) {
      var grupo = (document.forma.selectPerfiles.options[i].value).substring(0,document.forma.selectPerfiles.options[i].value.indexOf("~"));
      grupos[cnt] = grupo;
      cnt++;
    }

    var nuevo_grupo = "";
    var total_grupos = new Array(document.forma.selectPerfiles.length);
    var cnt2=0;
    for (j=0; j < cnt; j++) {
      if (nuevo_grupo!=grupos[j]) {
         nuevo_grupo=grupos[j];
         total_grupos[cnt2]=nuevo_grupo;
         cnt2++;
      }
    }
    var grupo_ultimo="";
    var bnd_calcular_grupo=false;
    if (cnt2<=1) {
      if (grupo_original!=total_grupos[0]) {
        grupo_ultimo=total_grupos[0];
      }
      else {
        grupo_ultimo = grupo_original;
      }
      bnd_calcular_grupo=false;
    }
    else {
      grupo_ultimo="";
      bnd_calcular_grupo=true;
    }
    if (grupo_ultimo!='') {
      TGrupoNuevo.style.display="none";
    }
    else {
      TGrupoNuevo.style.display='';
    }
    if (nuevo_grupo_bnd=='1') {
      document.forma.grupo_ultimo.value=grupo_ultimo;
      document.forma.bnd_calcular_grupo.value=bnd_calcular_grupo;
    }
    else {
      document.forma.grupo_ultimo.value=grupo_original;
      document.forma.bnd_calcular_grupo.value=false;
    }
  }

	function EnviarDatosUsuario(Formulario, pagina, cveGpo, cve_perfil) {
    Formulario.cve_grupo.value = cveGpo;
    Formulario.cve_perfil.value = cve_perfil;
		EnviarDatos( Formulario, pagina );
  }

  function ValidarInformacion(Formulario, pagina){
    Error="";
    if (Formulario.num_empleado.value.length=0) Error="Debe introducir un empleado";
    if (Formulario.password2.value!=document.forma.password.value) Error="Error al confirmar la contraseña";
    if (Formulario.password.value.length==0) Error="Debe introducir una contraseña";
    if (Formulario.login.value.length==0) Error="Debe introducir una cuenta";
    if (Error=="")
			EnviarDatos(Formulario, pagina)
    else
	    alert(Error)
    }

  function RefrescarCombo(){
		var ElementoValores = window.document.forma.SelectNombre;
		var ValorCapturado=window.document.forma.TextNombre.value;
		ValorCapturado= ValorCapturado.toUpperCase();
		var ContOpciones;
		if (ElementoValores!=null){
			for (ContOpciones = 0; ContOpciones < ElementoValores.length; ContOpciones++) {
        var ValorLista= ElementoValores.options[ContOpciones].text;
        if (ValorLista.indexOf(ValorCapturado)==0){
          ElementoValores.selectedIndex=ContOpciones;
          break;
        }
      }
    }
  }

