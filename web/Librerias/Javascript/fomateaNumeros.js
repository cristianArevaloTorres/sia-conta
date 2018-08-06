function eliminaComas(valor){//elimina las comas de una cantidad 
  var numero= "" ;
  len= valor.length; 
  for(x=0;len>x;++x){ 
    if(valor.charAt(x)!=","){
      numero=numero+valor.charAt(x);
    } 
   } 
   return numero; 
}//eliminarComas
      
function poneComas(_a){   
  var _ini=_a.toString().split(".");   
  var _b=_ini[0].toString().split("");   
  var _c="";   var _d=0;   
  for(var i=_b.length-1;i>=0;i--){     
    _d++;         
    _c=_b[i]+_c;     
    if(_d%3==0){        
      if((i!=0)){        
        if((_b[i-1]!="-"))
          _c=","+_c;        
       }      
    }    
  }//for   
  if(_ini[1]!=null)  
    return(_c+"."+_ini[1]);  
  else  
    return(_c); 
}//function pooneComas      

function moneyFormat(amount) { 
  var val = parseFloat(amount); 
  if (isNaN(val)) { 
    return "0.00"; 
  } 
  if (val == 0 ){ 
    return "0.00"; 
  } 
  val += ""; 
  // Next two lines remove anything beyond 2 decimal places 
  if (val.indexOf('.') == -1){ 
    return val+".00"; 
  } 
  else{ 
    val = val.substring(0,val.indexOf('.')+3); } 
    val = (val == Math.floor(val)) ? val + '.00' : ((val*10 == Math.floor(val*10)) ? val + '0' : val); 
    return val; 
} 