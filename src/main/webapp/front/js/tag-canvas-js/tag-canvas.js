
var sps = document.getElementById('exa').children, sul = document.getElementById('suggests');
var mc = parseInt(Math.random()*11), ce = function(){
    if(mc>=sps.length)
        mc = 0;
    var es = document.getElementById('mes');
    var qs = sps[mc].innerText||sps[mc].textContent;
    es.innerHTML = "• "+qs;
    es.setAttribute("href","/i?q="+qs);
    mc++;
};
ce();
setInterval(ce,1000);

// Init samples while empty ...
for(sl = 0;sl < sps.length;sl++){
    var o = document.createElement('option');
    o.setAttribute('value',sps[sl].innerText||sps[sl].textContent);
    sul.appendChild(o);
}

window.onload = function () {
    try {
        TagCanvas.Start('ex', null, {
            textFont: 'Verdana, Geneva, sans-serif',
            textColour: '#494949',
            textHeight: 18,
            wheelZoom: false
        });
    } catch (e) {
        document.getElementById('ec').style.display = 'none';
    }
};
document.getElementById("i").focus();
document.getElementById('rf').id = "f";
document.getElementById('rfh').id = "fh";
document.getElementById('rfc').id = "fc";
document.getElementById('rfi').id = "fi";
