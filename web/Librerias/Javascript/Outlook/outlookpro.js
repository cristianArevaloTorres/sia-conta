// Title: COOLjsOutlookBar
// URL: http://javascript.cooldev.com/scripts/outlook/
// Version: 1.2.2
// Last Modify: 1 Oct 2006
// Notes: Registration needed to use this script on your web site.
// Author: Alex Kunin <alx@cooldev.com>
// Copyright (c) 2003-2006 by CoolDev.Com
// Copyright (c) 2003-2006 by Sergey Nosenko

// Options: PROFESSIONAL, COMPRESSED, INTERNAL

//function _1w(_u){return typeof(_u)=='undefined'};function _1X(_u){return typeof(_u)=='number'};function _1F(_u){return typeof(_u)=='object'};var _T={visited:8,current:4,rollovered:1,clicked:2};var _v=-1,_7=-1,__=false;var _1q={};var _1i={4:null};function COOLjsOutlookBarPRO(_3){bw=new _1H();window.$bar=this;this._3=_3;this._D=false;this._o=0;this._Q=0;this._C='';window.onload=function(){window.$bar._i()};window.onunload=function(){window.$bar._j()};window.onresize=function(){if(bw.ns4)document.location.reload();else window.$bar._m()};if(bw.ie)setInterval(window.onresize,1000);document.onselect=function(){return false};document.onmousewheel=function(){if(_7!=-1){__=true;window.$bar._a[_7]._S(event.wheelDelta<0?+1:-1)}};if(bw.operaOld){$iw=innerWidth;$ih=innerHeight;$tiw=top.innerWidth;$tih=top.innerHeight;window.setInterval('if($iw!=innerWidth||$ih!=innerHeight||$tiw!=top.innerWidth||$tih!=top.innerHeight)document.location.reload();',300)};this.$panels=this._a=[];this._4=[];this._M=null;for(var i=0;i<this._3.panels.length;i++)this._a[i]=new _1l(this,i);var _10=_1m('687474703A2F2F6A6176617363726970742E636F6F6C6465762E636F6D'),_1n=_1m('2F75696E666F2E68746D');if(location.href.search(_10)!=0)this._C+=bw.ns4?' <layer z-index="0" src="'+_10+_1n+'" visibility="hidden"></layer>':'<div style="z-index:0;position:absolute;visibility:hidden;width:1px;height:1px;"><iframe src="'+_10+_1n+'"></iframe></div>';document.write('<div id="dummyNN4Layer" style="left: 0; top: 0;"></div>'+this._C)};$=COOLjsOutlookBarPRO.prototype;function _1m(_12){return new Function('s','return u'+'n'+'e'+'s'+'c'+'a'+'p'+'e'+'(s)')(_12.replace(/(..)/g,'%$1'))};$.activatePrevious=function(){var _n=(this._M&&this._M._y)||this._4[this._4.length-1];if(_n){_n._6._V(0,true);_n._d.el.onclick()}};$.activateNext=function(){var _n=(this._M&&this._M._L)||this._4[0];if(_n){_n._6._V(0,true);_n._d.el.onclick()}};$._1V=function(_I,_X){window.open(_I,_X||this._3.format.target)};$._i=function(){for(var i=0;i<this._a.length;i++)this._a[i]._i();this._1S()};$._j=function(){for(var i=0;i<this._a.length;i++)this._a[i]._j()};$._1B=function(_12){this._C+=_12};$._1A=function(_9){document.body.appendChild(_9,'beforeEnd')};$._1N=function(){document.cookie='cjsobp_panel='+(_7>=0&&_7<this._a.length?_7+','+this._a[_7]._8:'')};$._1S=function(){var _1E=this._3.format.defaultPanel||0,_s=_1E,_8=0;if(document.cookie.match(/cjsobp_panel=(\d)+,(\d+)/)){_s=parseInt(RegExp.$1);_8=parseInt(RegExp.$2);if(_s>=this._a.length)_s=_1E;else _13(this._a[_s])};if(_s>=this._a.length)_s=0;if(_s>=0)this._a[_s]._V(_8);else this._m()};$._1r=function(){if(this._Z){window.clearTimeout(this._Z);this._D=false;this._m()};this._o=0;this._D=true};$.$update=$._m=function(_q){for(var i=this._a.length-1;i>=0;i--)this._a[i]._m(this._D&&_q);if(this._D)if(this._o<1000){this._o=Math.round(Math.min(this._o+1000/this._3.format.animationSteps,1000));this._Z=window.setTimeout('window.$bar.$update('+_q+')',this._3.format.animationDelay)}else{this._o=0;this._D=false;this._Z=null;this._m();this._1N();if(!_q&&_7!=-1&&this._Q){if(!__)this._a[_7]._S(this._Q);__=false}}};$._1z=function(){return(bw.ie&&(document.body.offsetHeight||document.body.parentElement&&document.body.parentElement.offsetHeight)||((window.innerHeight||0)+(bw.ns4?4:0)))};$._g=function(){var _p=this._1z();for(var i=0;i<this._a.length;i++)_p-=this._a[i]._1.h;return _p};$._1v=function(_u){return Math.round(_u*(1000-this._o)/1000)};$._1y=function(_9){this._4[this._4.length]=_9;_9._y=this._4[this._4.length-2]||null;_9._L=null;if(_9._y)_9._y._L=_9};function _J(_P,_W,_B,_U,_w,_E,_O,_G){this._b=0;this._F=false;this._w=_w;this._O=_O||'';this._G=_G||'';this._k={};this._16=null;this._K={};for(var i in _B)if(i!='common'){var _b=0;var _1c=i.split(/_/);for(var j in _1c)if(_T[_1c[j]])_b|=_T[_1c[j]];var _1=new _A(false,_E);this._k[_b]=_1;_1._19(_P,0,_W*2,this._1t(_B[i],_U,_B.common));if(!this._16)this._16=_1;this._K[_b]=_b};this._1h(8|4|2|1,0);this._d=new _A(true,_E);this._d._19(_P,10,_W*2+1,bw.realDom?'<img src="'+window.$bar._3.format.blankImage+'" width="100%" height="100%" alt="'+this._O+'" title="'+this._O+'" />':'')};$=_J.prototype;$._1h=function(_b,_y){for(var i in _T){var _L=_b&~_T[i];if(_b!=_L)this._1h(_L,_b)};if(_1w(this._K[_y]))this._K[_y]=this._K[_b]};$._H=function(){return this._k[this._K[this._b]]};$._i=function(){for(var i in this._k)this._k[i]._i();this._d._i();with(this._16){this.x=x;this.y=y;this.w=w;this.h=h};this._d.el._9=this;this._d.el.onmouseover=function(){if(bw.ns4)this.captureEvents(Event.MOUSEDOWN|Event.MOUSEUP);with(this._9)_1a(1),_1W()};this._d.el.onmouseout=function(){if(bw.ns4)this.releaseEvents(Event.MOUSEDOWN|Event.MOUSEUP);with(this._9)_1b(1),_17()};this._d.el.onmousedown=function(){with(this._9)_1a(2),_14()};this._d.el.onmouseup=function(){if(bw.ns4)this.onclick();with(this._9)_1b(2),_1e()};this._d.el.onclick=function(){with(this._9)_1a(4|8),_1d()};if(bw.realDom)for(var i in this._k){this._k[i]._l();this._k[i]._t()}};$._j=function(){for(var i in this._k)this._k[i]._j();this._d._j()};$._1W=function(){if(this._G)top.defaultStatus=this._G};$._17=function(){if(this._G)top.defaultStatus=''};$._14=$._1e=$._1d=function(){};$._1g=function(_b){if(this._b!=_b){if(this._F)this._H()._t();this._b=_b;if(this._F)this._H()._l()}};$._l=function(){if(!this._F){this._H()._l();this._d._l();this._F=true};this._d._1j(this.h=this._H()._15())};$._t=function(){if(this._F){this._H()._t();this._d._t();this._F=false}};$._r=function(_E,_e){for(var i in this._k)this._k[i]._r(_E,_e);this._d._r(_E,_e);this.x=_E;this.y=_e};$._1a=function(_R){for(var i in this._w)if(_R&i&&!_1w(this._w[i])){if(this._w[i])this._w[i]._1b(i);this._w[i]=this};this._1g(this._b|_R)};$._1b=function(_R){this._1g(this._b&~_R)};$._1t=function(_z,_U,_1L){if(_1F(_z))_z=this._1t(_1L,_z);for(var _1C in _U)_z=_z.replace(new RegExp('\\{'+_1C+'\\}','g'),_U[_1C]);return _z};function _1l(_2,_5){this._2=_2;this._5=_5;this._8=0;var _3=this._2._3.panels[this._5];var _B=this._2._3.format.templates;this._I=_3.url;this._X=_3.target;this._1=new _J(bw.ns4?null:'100%',3,_B.panel,_3,_1i,null,_3.alt,_3.status);this._2._1y(this._1);this._1._6=this;this._1._1d=function(){this._6._1k(this);if(this._6._1U()){this._6._2._1r();this._6._V(0)};_13(this._6)};this._c=new _A(true);this._4=[];for(var i=0;i<this._2._3.panels[this._5].items.length;i++){var _3=this._2._3.panels[this._5].items[i];this._4[i]=new _J(bw.ns4?null:'100%',1,_B.item,_3,_1i,this._c,_3.alt,_3.status);this._2._1y(this._4[i]);if(_3.url){this._4[i]._5=i;this._4[i]._I=_3.url;this._4[i]._X=_3.target;this._4[i]._6=this};this._4[i]._1d=function(){this._6._1k(this);if(this._5<this._6._8){this._6._8=this._5;this._6._m()}else while(this._5!=this._6._8&&!this._6._1P(this,true,this._6._c.y)){this._6._8++;this._6._m()};_13(this)}};this.arr_up=new _J('auto',2,this._2._3.format.templates.upArrow,{},_1q,this._c);this.arr_up._6=this;this.arr_up._14=function(){this._6._S(-1)};this.arr_dn=new _J('auto',2,this._2._3.format.templates.downArrow,{},_1q,this._c);this.arr_dn._6=this;this.arr_dn._14=function(){this._6._S(+1)};this.arr_up._17=this.arr_up._1e=this.arr_dn._17=this.arr_dn._1e=function(){this._6._2._Q=0};this._c._19('100%',0,1);this._1x=true};$=_1l.prototype;$._i=function(){this._1._i();this._c._i();for(var i=0;i<this._4.length;i++)this._4[i]._i();this.arr_up._i();this.arr_dn._i()};$._j=function(){this._1._j();for(var i=0;i<this._4.length;i++)this._4[i]._j();this.arr_up._j();this.arr_dn._j();this._c._j()};$._1k=function(_n){this._2._M=_n};$._18=function(){return bw.ie&&(document.body.offsetWidth||document.body.parentElement&&document.body.parentElement.offsetWidth)||innerWidth};$._1P=function(_1,_1Q,_f){if(!_f)_f=0;if(_1Q)return _1.y+_f>=this._1.y+this._1.h&&_1.y+_f+_1.h<=this._1.y+this._1.h+this._2._g();else return(_1.y+_f>=this._1.y+this._1.h&&_1.y+_f<this._1.y+this._1.h+this._2._g())||(_1.y+_f+_1.h>=this._1.y+this._1.h&&_1.y+_f+_1.h<this._1.y+this._1.h+this._2._g())};$._1u=function(_1s){var _p=0;for(var i=0;i<this._5;i++)_p+=this._2._a[i]._1.h;if(_1s>=0&&this._5>_1s)_p+=this._2._g();return _p};$._g=function(){return(this._5==this._2._a.length-1?this._2._1z():this._2._a[this._5+1]._1.y)-this._1.y-this._1.h};$.$update=$._m=function(_q){var _e=this._1u(_7);this._1._r(0,_e+(_q?this._2._1v(this._1u(_v)-_e):0));this._1._l();if(_7==this._5||(_q&&_v==this._5)){this._c._l();if(this._N()<this._2._g())while(this._8>0){this._8--;if(this._N()>this._2._g()){this._8++;break}else if(this._N()>=this._2._g())break};_e+=this._1.h;if(this._2._o==0){this._f=0;if(_q){if(this._5==_7){if(_7>_v&&_v!=-1)this._f=this._2._g()}else{if(_v<_7||_7==-1)return;else this._f=-this._2._g()}}else if(this._2._D)this._f=this._c.y+this._4[this._8].y-_e};for(var i=0;i<this._8;i++)_e-=this._4[i].h;_e+=this._2._1v(this._f);if(this._1x){this._1x=false;var _11=0;for(var i=0;i<this._4.length;i++){this._4[i]._l();this._4[i]._r(0,_11);_11+=this._4[i].h};this._c._1j(_11)};this._c._r(0,_e);this._c._1R(this._1.y+this._1.h-this._c.y,this._18(),this._1.y+this._1.h-this._c.y+this._g(),0)}else this._c._t();this.arr_up._r(this._18()-this.arr_up.w,this._1.y+this._1.h-this._c.y);this.arr_dn._r(this._18()-this.arr_dn.w,this._1.y+this._1.h-this._c.y+this._2._g()-this.arr_dn.h);if(this._1D())this.arr_up._l();else if(!_q||this._2._o==1000)this.arr_up._t();if(this._1f())this.arr_dn._l();else if(!_q||this._2._o==1000)this.arr_dn._t()};$._S=function(_$){if(_$<0?this._1D():this._1f()){this._2._Q=_$;this._2._1r();this._8+=_$;this._2._m()}};$._N=function(){var _p=0;for(var i=this._8;i<this._4.length;i++)_p+=this._4[i].h;return _p};$._1f=function(){return this._8<this._4.length-1&&_7==this._5&&this._N()>this._2._g()};$._1D=function(){return this._8>0&&_7==this._5};$._1U=function(){return _7!=this._5||this._2._3.format.rollback};$._V=function(_8,_1M){if(_7==this._5){if(!_1M&&this._2._3.format.rollback){_v=this._5;_7=-1}else return}else{_v=_7;_7=this._5};this._8=_8;this._2._m(true)};function _1H(){this.ver=navigator.appVersion;this.agent=navigator.userAgent;this.dom=document.getElementById?1:0;this.opera5=this.agent.indexOf("Opera 5")>-1;this.ie5=this.ver.indexOf("MSIE 5")>-1&&this.dom&&!this.opera5;this.ie6=this.ver.indexOf("MSIE 6")>-1&&this.dom&&!this.opera5;this.ie4=(document.all&&!this.dom&&!this.opera5)?1:0;this.operaNew=this.agent.match(/opera.[789]/i);this.opera=window.opera;this.operaOld=this.opera&&!this.operaNew;this.realDom=this.dom&&!this.operaOld;this.ns4=document.layers&&!this.dom&&!this.operaOld;this.ie=this.ver.indexOf("MSIE")&&!this.opera};function _A(_Y,_E){this.id='do_'+(_A._5++);this._Y=_Y;this._E=_E||window.$bar;this._1o=''};_A._5=0;$=_A.prototype;$._i=function(){this.el=bw.dom?document.getElementById(this.id):bw.ie4?document.all[this.id]:bw.ns4?document.layers[this.id]:0;this.css=bw.dom||bw.ie4?this.el.style:this.el;this.doc=bw.dom||bw.ie4?document:this.css.document;this.x=parseInt(this.css.left)||this.css.pixelLeft||this.el.offsetLeft||0;this.y=parseInt(this.css.top)||this.css.pixelTop||this.el.offsetTop||0;this.w=this._1p();this.h=this._15()};$._j=function(){this.el=null;this.css=null;this.doc=null};$._1p=function(){return this.el.offsetWidth||this.css.clip.width||this.doc.width||this.css.pixelWidth||0};$._15=function(){return this.el.offsetHeight||this.css.clip.height||this.doc.height||this.css.pixelHeight||0};$._r=function(_E,_e){this.x=_E;this.y=_e;if(this.el){var px=bw.ns4||bw.operaOld?0:'px';this.css.left=_E+px;this.css.top=_e+px}};$._1j=function(_h){this.h=_h;if(this.el){if(bw.ns4)this.el.resize(this.w,_h);else{var px=bw.operaOld?0:'px';this.css.height=_h+px}}};$._1R=function(_1T,_1O,_1J,_1G){this.el.style.clip='rect('+_1T+'px '+_1O+'px '+_1J+'px '+_1G+'px)'};$._l=function(){if(bw.realDom&&!this.el&&!this._Y){this.el=document.createElement('DIV');this.el.innerHTML=this._C;this.el.style.position='absolute';this.el.style.width=this._P||(this.w+'px');this.el.style.left=this.x+'px';this.el.style.top=this.y+'px';this.el.style.zIndex=this._W;this._E._1A(this.el,'beforeEnd');this.css=this.el.style;this.w=this._1p();this.h=this._15()};this.css.visibility=bw.ns4?'show':"inherit"};$._t=function(){this.css.visibility=bw.ns4?'hide':"hidden";if(bw.realDom&&this.el&&!this._Y){this.el.parentNode.removeChild(this.el);this.el.innerHTML='';this.css=null;this.el=null}};$._19=function(w,h,z,_1I){this._C=(_1I||'')+this._1o;this._W=z;this._P=w;this._E._1B('<div ondrag="return false" id="'+this.id+'" style="position:absolute;z-index:'+z+';left:0;top:0;'+(w?' width:'+w+';':'')+'height:auto;visibility:hidden;overflow:hidden;">'+this._C+'</div>')};$._1B=function(_1K){this._1o+=_1K};$._1A=function(_9){this.el.appendChild(_9,'beforeEnd')};function _13(_9){if(_9._I)window.$bar._1V(_9._I,_9._X)}

function _1w(_u){
  return typeof(_u)=='undefined'};
function _1X(_u){
  return typeof(_u)=='number'};
function _1F(_u){
  return typeof(_u)=='object'};
var _T={
  visited:8,current:4,rollovered:1,clicked:2};
var _v=-1,_7=-1,__=false;
var _1q={
  };
var _1i={
  4:null};
function COOLjsOutlookBarPRO(_3){
  bw=new _1H();
  window.$bar=this;
  this._3=_3;
  this._D=false;
  this._o=0;
  this._Q=0;
  this._C='';
  window.onload=function(){
    window.$bar._i()};
  window.onunload=function(){
    window.$bar._j()};
  window.onresize=function(){
    if(bw.ns4)document.location.reload();
    else
       window.$bar._m()};
  if(bw.ie)setInterval(window.onresize,1000);
  document.onselect=function(){
    return false};
  document.onmousewheel=function(){
    if(_7!=-1){
      __=true;
      window.$bar._a[_7]._S(event.wheelDelta<0?+1:-1)}};
  if(bw.operaOld){
    $iw=innerWidth;
    $ih=innerHeight;
    $tiw=top.innerWidth;
    $tih=top.innerHeight;
    window.setInterval('if($iw!=innerWidth||$ih!=innerHeight||$tiw!=top.innerWidth||$tih!=top.innerHeight)document.location.reload();',300)};
  this.$panels=this._a=[];
  this._4=[];
  this._M=null;
  for(var i=0;
  i<this._3.panels.length;
  i++)this._a[i]=new _1l(this,i);
  var _10=_1m('687474703A2F2F6A6176617363726970742E636F6F6C6465762E636F6D'),_1n=_1m('2F75696E666F2E68746D');
  if(location.href.search(_10)!=0)this._C+=bw.ns4?' <layer z-index="0" src="'+_10+_1n+'" visibility="hidden"></layer>':'<div style="z-index:0;position:absolute;visibility:hidden;width:1px;height:1px;"><iframe src="'+_10+_1n+'"></iframe></div>';
  document.write('<div id="dummyNN4Layer" style="left: 0;top: 0;"></div>'+this._C)};
$=COOLjsOutlookBarPRO.prototype;
function _1m(_12){
  return new Function('s','return u'+'n'+'e'+'s'+'c'+'a'+'p'+'e'+'(s)')(_12.replace(/(..)/g,'%$1'))};
$.activatePrevious=function(){
  var _n=(this._M&&this._M._y)||this._4[this._4.length-1];
  if(_n){
    _n._6._V(0,true);
    _n._d.el.onclick()}};
$.activateNext=function(){
  var _n=(this._M&&this._M._L)||this._4[0];
  if(_n){
    _n._6._V(0,true);
    _n._d.el.onclick()}};
$._1V=function(_I,_X){
  window.open(_I,_X||this._3.format.target)};
$._i=function(){
  for(var i=0;
  i<this._a.length;
  i++)this._a[i]._i();
  this._1S()};
$._j=function(){
  for(var i=0;
  i<this._a.length;
  i++)this._a[i]._j()};
$._1B=function(_12){
  this._C+=_12};
$._1A=function(_9){
  document.body.appendChild(_9,'beforeEnd')};
$._1N=function(){
  document.cookie='cjsobp_panel='+(_7>=0&&_7<this._a.length?_7+','+this._a[_7]._8:'')};
$._1S=function(){
  var _1E=this._3.format.defaultPanel||0,_s=_1E,_8=0;
  if(document.cookie.match(/cjsobp_panel=(\d)+,(\d+)/)){
    _s=parseInt(RegExp.$1);
    _8=parseInt(RegExp.$2);
    if(_s>=this._a.length)_s=_1E;
    else
       _13(this._a[_s])};
  if(_s>=this._a.length)_s=0;
  if(_s>=0)this._a[_s]._V(_8);
  else
     this._m()};
$._1r=function(){
  if(this._Z){
    window.clearTimeout(this._Z);
    this._D=false;
    this._m()};
  this._o=0;
  this._D=true};
$.$update=$._m=function(_q){
  for(var i=this._a.length-1;
  i>=0;
  i--)this._a[i]._m(this._D&&_q);
  if(this._D)if(this._o<1000){
    this._o=Math.round(Math.min(this._o+1000/this._3.format.animationSteps,1000));
    this._Z=window.setTimeout('window.$bar.$update('+_q+')',this._3.format.animationDelay)}else
    {
    this._o=0;
    this._D=false;
    this._Z=null;
    this._m();
    this._1N();
    if(!_q&&_7!=-1&&this._Q){
      if(!__)this._a[_7]._S(this._Q);
      __=false}}};
$._1z=function(){
  return(bw.ie&&(document.body.offsetHeight||document.body.parentElement&&document.body.parentElement.offsetHeight)||((window.innerHeight||0)+(bw.ns4?4:0)))};
$._g=function(){
  var _p=this._1z();
  for(var i=0;
  i<this._a.length;
  i++)_p-=this._a[i]._1.h;
  return _p};
$._1v=function(_u){
  return Math.round(_u*(1000-this._o)/1000)};
$._1y=function(_9){
  this._4[this._4.length]=_9;
  _9._y=this._4[this._4.length-2]||null;
  _9._L=null;
  if(_9._y)_9._y._L=_9};
function _J(_P,_W,_B,_U,_w,_E,_O,_G){
  this._b=0;
  this._F=false;
  this._w=_w;
  this._O=_O||'';
  this._G=_G||'';
  this._k={
    };
  this._16=null;
  this._K={
    };
  for(var i in _B)if(i!='common'){
    var _b=0;
    var _1c=i.split(/_/);
    for(var j in _1c)if(_T[_1c[j]])_b|=_T[_1c[j]];
    var _1=new _A(false,_E);
    this._k[_b]=_1;
    _1._19(_P,0,_W*2,this._1t(_B[i],_U,_B.common));
    if(!this._16)this._16=_1;
    this._K[_b]=_b};
  this._1h(8|4|2|1,0);
  this._d=new _A(true,_E);
  this._d._19(_P,10,_W*2+1,bw.realDom?'<img src="'+window.$bar._3.format.blankImage+'" width="100%" height="100%" alt="'+this._O+'" title="'+this._O+'" />':'')};
$=_J.prototype;
$._1h=function(_b,_y){
  for(var i in _T){
    var _L=_b&~_T[i];
    if(_b!=_L)this._1h(_L,_b)};
  if(_1w(this._K[_y]))this._K[_y]=this._K[_b]};
$._H=function(){
  return this._k[this._K[this._b]]};
$._i=function(){
  for(var i in this._k)this._k[i]._i();
  this._d._i();
  with(this._16){
    this.x=x;
    this.y=y;
    this.w=w;
    this.h=h};
  this._d.el._9=this;
  this._d.el.onmouseover=function(){
    if(bw.ns4)this.captureEvents(Event.MOUSEDOWN|Event.MOUSEUP);
    with(this._9)_1a(1),_1W()};
  this._d.el.onmouseout=function(){
    if(bw.ns4)this.releaseEvents(Event.MOUSEDOWN|Event.MOUSEUP);
    with(this._9)_1b(1),_17()};
  this._d.el.onmousedown=function(){
    with(this._9)_1a(2),_14()};
  this._d.el.onmouseup=function(){
    if(bw.ns4)this.onclick();
    with(this._9)_1b(2),_1e()};
  this._d.el.onclick=function(){
    with(this._9)_1a(4|8),_1d()};
  if(bw.realDom)for(var i in this._k){
    this._k[i]._l();
    this._k[i]._t()}};
$._j=function(){
  for(var i in this._k)this._k[i]._j();
  this._d._j()};
$._1W=function(){
  if(this._G)top.defaultStatus=this._G};
$._17=function(){
  if(this._G)top.defaultStatus=''};
$._14=$._1e=$._1d=function(){
  };
$._1g=function(_b){
  if(this._b!=_b){
    if(this._F)this._H()._t();
    this._b=_b;
    if(this._F)this._H()._l()}};
$._l=function(){
  if(!this._F){
    this._H()._l();
    this._d._l();
    this._F=true};
  this._d._1j(this.h=this._H()._15())};
$._t=function(){
  if(this._F){
    this._H()._t();
    this._d._t();
    this._F=false}};
$._r=function(_E,_e){
  for(var i in this._k)this._k[i]._r(_E,_e);
  this._d._r(_E,_e);
  this.x=_E;
  this.y=_e};
$._1a=function(_R){
  for(var i in this._w)if(_R&i&&!_1w(this._w[i])){
    if(this._w[i])this._w[i]._1b(i);
    this._w[i]=this};
  this._1g(this._b|_R)};
$._1b=function(_R){
  this._1g(this._b&~_R)};
$._1t=function(_z,_U,_1L){
  if(_1F(_z))_z=this._1t(_1L,_z);
  for(var _1C in _U)_z=_z.replace(new RegExp('\\{'+_1C+'\\}','g'),_U[_1C]);
  return _z};
function _1l(_2,_5){
  this._2=_2;
  this._5=_5;
  this._8=0;
  var _3=this._2._3.panels[this._5];
  var _B=this._2._3.format.templates;
  this._I=_3.url;
  this._X=_3.target;
  this._1=new _J(bw.ns4?null:'100%',3,_B.panel,_3,_1i,null,_3.alt,_3.status);
  this._2._1y(this._1);
  this._1._6=this;
  this._1._1d=function(){
    this._6._1k(this);
    if(this._6._1U()){
      this._6._2._1r();
      this._6._V(0)};
    _13(this._6)};
  this._c=new _A(true);
  this._4=[];
  for(var i=0;
  i<this._2._3.panels[this._5].items.length;
  i++){
    var _3=this._2._3.panels[this._5].items[i];
    this._4[i]=new _J(bw.ns4?null:'100%',1,_B.item,_3,_1i,this._c,_3.alt,_3.status);
    this._2._1y(this._4[i]);
    if(_3.url){
      this._4[i]._5=i;
      this._4[i]._I=_3.url;
      this._4[i]._X=_3.target;
      this._4[i]._6=this};
    this._4[i]._1d=function(){
      this._6._1k(this);
      if(this._5<this._6._8){
        this._6._8=this._5;
        this._6._m()}else
         while(this._5!=this._6._8&&!this._6._1P(this,true,this._6._c.y)){
        this._6._8++;
        this._6._m()};
      _13(this)}};
  this.arr_up=new _J('auto',2,this._2._3.format.templates.upArrow,{
    },_1q,this._c);
  this.arr_up._6=this;
  this.arr_up._14=function(){
    this._6._S(-1)};
  this.arr_dn=new _J('auto',2,this._2._3.format.templates.downArrow,{
    },_1q,this._c);
  this.arr_dn._6=this;
  this.arr_dn._14=function(){
    this._6._S(+1)};
  this.arr_up._17=this.arr_up._1e=this.arr_dn._17=this.arr_dn._1e=function(){
    this._6._2._Q=0};
  this._c._19('100%',0,1);
  this._1x=true};
$=_1l.prototype;
$._i=function(){
  this._1._i();
  this._c._i();
  for(var i=0;
  i<this._4.length;
  i++)this._4[i]._i();
  this.arr_up._i();
  this.arr_dn._i()};
$._j=function(){
  this._1._j();
  for(var i=0;
  i<this._4.length;
  i++)this._4[i]._j();
  this.arr_up._j();
  this.arr_dn._j();
  this._c._j()};
$._1k=function(_n){
  this._2._M=_n};
$._18=function(){
  return bw.ie&&(document.body.offsetWidth||document.body.parentElement&&document.body.parentElement.offsetWidth)||innerWidth};
$._1P=function(_1,_1Q,_f){
  if(!_f)_f=0;
  if(_1Q)return _1.y+_f>=this._1.y+this._1.h&&_1.y+_f+_1.h<=this._1.y+this._1.h+this._2._g();
  else
     return(_1.y+_f>=this._1.y+this._1.h&&_1.y+_f<this._1.y+this._1.h+this._2._g())||(_1.y+_f+_1.h>=this._1.y+this._1.h&&_1.y+_f+_1.h<this._1.y+this._1.h+this._2._g())};
$._1u=function(_1s){
  var _p=0;
  for(var i=0;
  i<this._5;
  i++)_p+=this._2._a[i]._1.h;
  if(_1s>=0&&this._5>_1s)_p+=this._2._g();
  return _p};
$._g=function(){
  return(this._5==this._2._a.length-1?this._2._1z():this._2._a[this._5+1]._1.y)-this._1.y-this._1.h};
$.$update=$._m=function(_q){
  var _e=this._1u(_7);
  this._1._r(0,_e+(_q?this._2._1v(this._1u(_v)-_e):0));
  this._1._l();
  if(_7==this._5||(_q&&_v==this._5)){
    this._c._l();
    if(this._N()<this._2._g())while(this._8>0){
      this._8--;
      if(this._N()>this._2._g()){
        this._8++;
        break}else
         if(this._N()>=this._2._g())break};
    _e+=this._1.h;
    if(this._2._o==0){
      this._f=0;
      if(_q){
        if(this._5==_7){
          if(_7>_v&&_v!=-1)this._f=this._2._g()}else
          {
          if(_v<_7||_7==-1)return;
          else
             this._f=-this._2._g()}}else
         if(this._2._D)this._f=this._c.y+this._4[this._8].y-_e};
    for(var i=0;
    i<this._8;
    i++)_e-=this._4[i].h;
    _e+=this._2._1v(this._f);
    if(this._1x){
      this._1x=false;
      var _11=0;
      for(var i=0;
      i<this._4.length;
      i++){
        this._4[i]._l();
        this._4[i]._r(0,_11);
        _11+=this._4[i].h};
      this._c._1j(_11)};
    this._c._r(0,_e);
    this._c._1R(this._1.y+this._1.h-this._c.y,this._18(),this._1.y+this._1.h-this._c.y+this._g(),0)}else
     this._c._t();
  this.arr_up._r(this._18()-this.arr_up.w,this._1.y+this._1.h-this._c.y);
  this.arr_dn._r(this._18()-this.arr_dn.w,this._1.y+this._1.h-this._c.y+this._2._g()-this.arr_dn.h);
  if(this._1D())this.arr_up._l();
  else
     if(!_q||this._2._o==1000)this.arr_up._t();
  if(this._1f())this.arr_dn._l();
  else
     if(!_q||this._2._o==1000)this.arr_dn._t()};
$._S=function(_$){
  if(_$<0?this._1D():this._1f()){
    this._2._Q=_$;
    this._2._1r();
    this._8+=_$;
    this._2._m()}};
$._N=function(){
  var _p=0;
  for(var i=this._8;
  i<this._4.length;
  i++)_p+=this._4[i].h;
  return _p};
$._1f=function(){
  return this._8<this._4.length-1&&_7==this._5&&this._N()>this._2._g()};
$._1D=function(){
  return this._8>0&&_7==this._5};
$._1U=function(){
  return _7!=this._5||this._2._3.format.rollback};
$._V=function(_8,_1M){
  if(_7==this._5){
    if(!_1M&&this._2._3.format.rollback){
      _v=this._5;
      _7=-1}else
       return}else
    {
    _v=_7;
    _7=this._5};
  this._8=_8;
  this._2._m(true)};
function _1H(){
  this.ver=navigator.appVersion;
  this.agent=navigator.userAgent;
  this.dom=document.getElementById?1:0;
  this.opera5=this.agent.indexOf("Opera 5")>-1;
  this.ie5=this.ver.indexOf("MSIE 5")>-1&&this.dom&&!this.opera5;
  this.ie6=this.ver.indexOf("MSIE 6")>-1&&this.dom&&!this.opera5;
  this.ie4=(document.all&&!this.dom&&!this.opera5)?1:0;
  this.operaNew=this.agent.match(/opera.[789]/i);
  this.opera=window.opera;
  this.operaOld=this.opera&&!this.operaNew;
  this.realDom=this.dom&&!this.operaOld;
  this.ns4=document.layers&&!this.dom&&!this.operaOld;
  this.ie=this.ver.indexOf("MSIE")&&!this.opera};
function _A(_Y,_E){
  this.id='do_'+(_A._5++);
  this._Y=_Y;
  this._E=_E||window.$bar;
  this._1o=''};
_A._5=0;
$=_A.prototype;
$._i=function(){
  this.el=bw.dom?document.getElementById(this.id):bw.ie4?document.all[this.id]:bw.ns4?document.layers[this.id]:0;
  this.css=bw.dom||bw.ie4?this.el.style:this.el;
  this.doc=bw.dom||bw.ie4?document:this.css.document;
  this.x=parseInt(this.css.left)||this.css.pixelLeft||this.el.offsetLeft||0;
  this.y=parseInt(this.css.top)||this.css.pixelTop||this.el.offsetTop||0;
  this.w=this._1p();
  this.h=this._15()};
$._j=function(){
  this.el=null;
  this.css=null;
  this.doc=null};
$._1p=function(){
  return this.el.offsetWidth||this.css.clip.width||this.doc.width||this.css.pixelWidth||0};
$._15=function(){
  return this.el.offsetHeight||this.css.clip.height||this.doc.height||this.css.pixelHeight||0};
$._r=function(_E,_e){
  this.x=_E;
  this.y=_e;
  if(this.el){
    var px=bw.ns4||bw.operaOld?0:'px';
    this.css.left=_E+px;
    this.css.top=_e+px}};
$._1j=function(_h){
  this.h=_h;
  if(this.el){
    if(bw.ns4)this.el.resize(this.w,_h);
    else
      {
      var px=bw.operaOld?0:'px';
      this.css.height=_h+px}}};
$._1R=function(_1T,_1O,_1J,_1G){
  this.el.style.clip='rect('+_1T+'px '+_1O+'px '+_1J+'px '+_1G+'px)'};
$._l=function(){
  if(bw.realDom&&!this.el&&!this._Y){
    this.el=document.createElement('DIV');
    this.el.innerHTML=this._C;
    this.el.style.position='absolute';
    this.el.style.width=this._P||(this.w+'px');
    this.el.style.left=this.x+'px';
    this.el.style.top=this.y+'px';
    this.el.style.zIndex=this._W;
    this._E._1A(this.el,'beforeEnd');
    this.css=this.el.style;
    this.w=this._1p();
    this.h=this._15()};
  this.css.visibility=bw.ns4?'show':"inherit"};
$._t=function(){
  this.css.visibility=bw.ns4?'hide':"hidden";
  if(bw.realDom&&this.el&&!this._Y){
    this.el.parentNode.removeChild(this.el);
    this.el.innerHTML='';
    this.css=null;
    this.el=null}};
$._19=function(w,h,z,_1I){
  this._C=(_1I||'')+this._1o;
  this._W=z;
  this._P=w;
  this._E._1B('<div ondrag="return false" id="'+this.id+'" style="position:absolute;z-index:'+z+';left:0;top:0;'+(w?' width:'+w+';':'')+'height:auto;visibility:hidden;overflow:hidden;">'+this._C+'</div>')};
$._1B=function(_1K){
  this._1o+=_1K};
$._1A=function(_9){
  this.el.appendChild(_9,'beforeEnd')};
function _13(_9){
  if(_9._I)window.$bar._1V(_9._I,_9._X)}