CanvasRenderingContext2D.prototype.roundRect = function(x,y,w,h,r){
    r = Math.min(w/2 , h/2 , r) ;
    this.moveTo(x+r , y) ;
    this.lineTo(x + w - r , y) ;
    this.arc(x+w-r , y+ r , r ,  Math.PI*1.5 , Math.PI*2 , false) ;
    this.lineTo(x+w , y+ h -r ) ;
    this.arc(x+w-r , y+h -r , r , 0 , Math.PI / 2 , false) ;
    this.lineTo(x+r , y+h ) ;
    this.arc(x+r , y + h - r , r , Math.PI/2 , Math.PI , false) ;
    this.lineTo(x , y+ r) ;
    this.arc(x+r , y+ r , r , Math.PI , Math.PI * 1.5 , false) ;
}

Array.prototype.hasContain = function(val){
    for(var i = 0 ; i < this.length ; ++i){
        if(this[i] == val) return true ;
    }
    return false ;
}
