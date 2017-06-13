//
// site.js
//
// the arbor.js website
//
(function($){
  // var trace = function(msg){
  //   if (typeof(window)=='undefined' || !window.console) return
  //   var len = arguments.length, args = [];
  //   for (var i=0; i<len; i++) args.push("arguments["+i+"]")
  //   eval("console.log("+args.join(",")+")")
  // }  
  
  var Renderer = function(elt){
    var dom = $(elt)
    var canvas = dom.get(0)
    var ctx = canvas.getContext("2d");
    var gfx_semantic_graph = arbor.Graphics("#semantic_graph")
    var sys_graph = null

    var _vignette = null
    var selected = null,
        nearest = null,
        _mouseP = null;

    
    var that = {
      init:function(pSystem){
    	sys_graph = pSystem
    	sys_graph.screen({size:{width:dom.width(), height:dom.height()},
                    padding:[36,60,36,60]})

       // TODO 设置画布的大小为Window的大小  为了适应屏幕。？   屏幕大小变化的适应问题
       /* $(window).resize(that.resize)
        that.resize()*/
        that._initMouseHandling()

        if (document.referrer.match(/echolalia|atlas|halfviz/)){
          // if we got here by hitting the back button in one of the demos, 
          // start with the demos section pre-selected
          that.switchSection('demos')
        }
      },
      resize:function(){
        canvas.width = $(window).width()
        canvas.height = .75* $(window).height()
        sys_graph.screen({size:{width:canvas.width, height:canvas.height}})
        _vignette = null
        that.redraw()
      },
      redraw:function(){
    	gfx_semantic_graph.clear()
        sys_graph.eachEdge(function(edge, p1, p2){
          if (edge.source.data.alpha * edge.target.data.alpha == 0) return
          gfx_semantic_graph.line(p1, p2, {stroke:"#b2b19d", width:2, alpha:edge.target.data.alpha})
           // TODO 修改
          gfx_semantic_graph.text(edge.data.name, (Math.abs(p2.x + p1.x))/2, (Math.abs(p2.y + p1.y))/2, {color:edge.data.color, align:"center", font:"Arial", size:edge.data.size})
        })
        sys_graph.eachNode(function(node, pt){
          var w = Math.max(20, 20+gfx_semantic_graph.textWidth(node.name) )
          if (node.data.alpha===0) return
          if (node.data.shape=='dot'){
        	gfx_semantic_graph.oval(pt.x-w/2, pt.y-w/2, w, w, {fill:node.data.color, alpha:node.data.alpha})
            gfx_semantic_graph.text(node.name, pt.x, pt.y+7, {color:"white", align:"center", font:"Arial", size:12})
            gfx_semantic_graph.text(node.name, pt.x, pt.y+7, {color:"white", align:"center", font:"Arial", size:12})
          }else{
        	gfx_semantic_graph.rect(pt.x-w/2, pt.y-8, w, 20, 4, {fill:node.data.color, alpha:node.data.alpha})
            gfx_semantic_graph.text(node.name, pt.x, pt.y+9, {color:"white", align:"center", font:"Arial", size:12})
            gfx_semantic_graph.text(node.name, pt.x, pt.y+9, {color:"white", align:"center", font:"Arial", size:12})
          }
        })
        that._drawVignette()
      },
      
      _drawVignette:function(){
        var w = canvas.width
        var h = canvas.height
    	/*var w = 700
        var h = 448*/
        var r = 20

        if (!_vignette){
          var top = ctx.createLinearGradient(0,0,0,r)
          top.addColorStop(0, "#e0e0e0")
          top.addColorStop(.7, "rgba(255,255,255,0)")

          var bot = ctx.createLinearGradient(0,h-r,0,h)
          bot.addColorStop(0, "rgba(255,255,255,0)")
          bot.addColorStop(1, "white")

          _vignette = {top:top, bot:bot}
        }
        
        // top
        ctx.fillStyle = _vignette.top
        ctx.fillRect(0,0, w,r)

        // bot
        ctx.fillStyle = _vignette.bot
        ctx.fillRect(0,h-r, w,r)
      },

      switchMode:function(e){
        if (e.mode=='hidden'){
          dom.stop(true).fadeTo(e.dt,0, function(){
            if (sys_graph) sys_graph.stop()
            $(this).hide()
          })
        }else if (e.mode=='visible'){
          dom.stop(true).css('opacity',0).show().fadeTo(e.dt,1,function(){
            that.resize()
          })
          if (sys_graph) sys_graph.start()
        }
      },
      
      switchSection:function(newSection){
        var parent = sys_graph.getEdgesFrom(newSection)[0].source
        var children = $.map(sys_graph.getEdgesFrom(newSection), function(edge){
          return edge.target
        })
        
        sys_graph.eachNode(function(node){
          if (node.data.shape=='dot') return // skip all but leafnodes

          var nowVisible = ($.inArray(node, children)>=0)
          var newAlpha = (nowVisible) ? 1 : 0
          var dt = (nowVisible) ? .5 : .5
          sys_graph.tweenNode(node, dt, {alpha:newAlpha})

          if (newAlpha==1){
            node.p.x = parent.p.x + .05*Math.random() - .025
            node.p.y = parent.p.y + .05*Math.random() - .025
            node.tempMass = .001
          }
        })
      },
      
      
      _initMouseHandling:function(){
        // no-nonsense drag and drop (thanks springy.js)
        selected = null;
        nearest = null;
        
        propertyShow = true;
        
        var dragged = null;
        var oldmass = 1

        var _section = null

        var handler = {
          moved:function(e){
        	  // 修改canvas修改成了"#semantic_graph"
            var pos = $("#semantic_graph").offset();
            _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
            nearest = sys_graph.nearest(_mouseP);

            if (!nearest.node) return false

            if (nearest.node.data.shape!='dot'){
              // 如果距离结点很近 代表选择了该节点 则将弹出连接
              selected = (nearest.distance < 20) ? nearest : null
            // TODO 将selected修改为propertyShow
             if (selected){
                 dom.addClass('linkable')
                 //window.status = selected.node.data.link.replace(/^\//,"http://"+window.location.host+"/").replace(/^#/,'')
              }
    		  /*if(propertyShow){
                  dom.addClass('linkable')
                  window.status = selected.node.data.link.replace(/^\//,"http://"+window.location.host+"/").replace(/^#/,'')
    		  }*/
              // 否则删除连接
              else{
                 dom.removeClass('linkable')
                 window.status = ''
              }
            }else if ($.inArray(nearest.node.name, ['星爷','code','docs','demos']) >=0 ){
              if (nearest.node.name!=_section){
                _section = nearest.node.name
                that.switchSection(_section)
              }
              dom.removeClass('linkable')
              window.status = ''
            }
            
            return false
          },
          clicked:function(e){
            var pos = $("#semantic_graph").offset();
            _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
            nearest = dragged = sys_graph.nearest(_mouseP);
            
            if (nearest && selected && nearest.node===selected.node){
              var link = selected.node.data.link
              if (link.match(/^#/)){
                 $(that).trigger({type:"navigate", path:link.substr(1)})
              }else{
                 window.location = link
              }
              return false
            }
            
            if (dragged && dragged.node !== null) dragged.node.fixed = true
            $("#semantic_graph").unbind('mousemove', handler.moved);
            $("#semantic_graph").bind('mousemove', handler.dragged)
            $(window).bind('mouseup', handler.dropped)

            return false
          },
          dragged:function(e){
            var old_nearest = nearest && nearest.node._id
            var pos = $("#semantic_graph").offset();
            var s = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)

            if (!nearest) return
            if (dragged !== null && dragged.node !== null){
              var p = sys_graph.fromScreen(s)
              dragged.node.p = p
            }

            return false
          },

          dropped:function(e){
            if (dragged===null || dragged.node===undefined) return
            if (dragged.node !== null) dragged.node.fixed = false
            dragged.node.tempMass = 1000
            dragged = null;
            // selected = null
            $("#semantic_graph").unbind('mousemove', handler.dragged)
            $(window).unbind('mouseup', handler.dropped)
            $("#semantic_graph").bind('mousemove', handler.moved);
            _mouseP = null
            return false
          }


        }
        $("#semantic_graph").mousedown(handler.clicked);
        // 绑定鼠标移动事件的函数
        $("#semantic_graph").mousemove(handler.moved);
        
      }
    }
    
    return that
  }
  
  
  var Nav = function(elt){
    var dom = $(elt)

    var _path = null
    
    var that = {
      init:function(){
        $(window).bind('popstate',that.navigate)
        dom.find('> a').click(that.back)
        $('.more').one('click',that.more)
        
        $('#docs dl:not(.datastructure) dt').click(that.reveal)
        that.update()
        return that
      },
      more:function(e){
        $(this).removeAttr('href').addClass('less').html('&nbsp;').siblings().fadeIn()
        $(this).next('h2').find('a').one('click', that.less)
        
        return false
      },
      less:function(e){
        var more = $(this).closest('h2').prev('a')
        $(this).closest('h2').prev('a')
        .nextAll().fadeOut(function(){
          $(more).text('creation & use').removeClass('less').attr('href','#')
        })
        $(this).closest('h2').prev('a').one('click',that.more)
        
        return false
      },
      reveal:function(e){
        $(this).next('dd').fadeToggle('fast')
        return false
      },
      back:function(){
        _path = "/"
        if (window.history && window.history.pushState){
          window.history.pushState({path:_path}, "", _path);
        }
        that.update()
        return false
      },
      navigate:function(e){
        var oldpath = _path
        if (e.type=='navigate'){
          _path = e.path
          if (window.history && window.history.pushState){
             window.history.pushState({path:_path}, "", _path);
          }else{
            that.update()
          }
        }else if (e.type=='popstate'){
          var state = e.originalEvent.state || {}
          _path = state.path || window.location.pathname.replace(/^\//,'')
        }
        if (_path != oldpath) that.update()
      },
      update:function(){
        var dt = 'fast'
        if (_path===null){
          // this is the original page load. don't animate anything just jump
          // to the proper state
          _path = window.location.pathname.replace(/^\//,'')
          dt = 0
          dom.find('p').css('opacity',0).show().fadeTo('slow',1)
        }

        switch (_path){
          case '':
          case '/':
          dom.find('p').text('a graph visualization library using web workers and jQuery')
          dom.find('> a').removeClass('active').attr('href','#')

          $('#docs').fadeTo('fast',0, function(){
            $(this).hide()
            $(that).trigger({type:'mode', mode:'visible', dt:dt})
          })
          document.title = "arbor.js"
          break
          
          case 'introduction':
          case 'reference':
          $(that).trigger({type:'mode', mode:'hidden', dt:dt})
          dom.find('> p').text(_path)
          dom.find('> a').addClass('active').attr('href','#')
          $('#docs').stop(true).css({opacity:0}).show().delay(333).fadeTo('fast',1)
                    
          $('#docs').find(">div").hide()
          $('#docs').find('#'+_path).show()
          document.title = "arbor.js » " + _path
          break
        }
        
      }
    }
    return that
  }
  
  $(document).ready(function(){
	  var targetURL = "developerAction!getSemanticGraph.action"
	  $.ajax({
          url : targetURL,
          type : "POST",
          async : true,
          dataType : "json", 
          timeout : 10000 ,
          success : function (returnVal) {
        	  var semanticGraphStatements = returnVal.semanticGraphStatements
        	  var CLR = {
        			  branch:"#b2b19d",
        		      code:"orange",
        		      doc:"#922E00",
        		      demo:"#a7af00"
        	  }
        	  
        	  if(semanticGraphStatements.length == 0) {
        		  $('#semantic').css('display','none');
        		  return;
        	  }
        	  
        	  // 初始化
        	  var sys_graph = arbor.ParticleSystem()
        	  // 设置相关参数	stiffness:硬度	repulsion:排斥力	gravity:重力  
        	  sys_graph.parameters({stiffness:900, repulsion:2000, gravity:true, dt:0.015})
        	  // 读取绘图区域
        	  sys_graph.renderer = Renderer("#semantic_graph")
        	  // 传入结点和连接
        	  // 添加结点
        	  for(var i = 0; i < semanticGraphStatements.length; i++) {
        		  // 添加结点 
        		  var subjectName = semanticGraphStatements[i].subject.name
        		  var data = semanticGraphStatements[i].subject
        		  sys_graph.addNode(subjectName, data)
        		  /*var predicateName = "-" + semanticGraphStatements[i].predicate.iD + ":" + semanticGraphStatements[i].predicate.name + "-"
        		  var data = semanticGraphStatements[i].predicate
        		  sys_graph.addNode(predicateName, data)*/
        		  var objectName = semanticGraphStatements[i].object.name
        		  var data = semanticGraphStatements[i].object
        		  sys_graph.addNode(objectName, data)
        		  // 添加连线
        		  var sourceName = semanticGraphStatements[i].subject.name
        		  //var predicateName = "-" + semanticGraphStatements[i].predicate.iD + ":" + semanticGraphStatements[i].predicate.name + "-"
        		  var targetName = semanticGraphStatements[i].object.name
        		  var data = semanticGraphStatements[i].predicate
        		  sys_graph.addEdge(sourceName, targetName, data)
        		  //sys_graph.addEdge(predicateName, targetName, data)
        	  }
				
        	  var nav = Nav("#nav")
        	  $(sys_graph.renderer).bind('navigate', nav.navigate)
        	  $(nav).bind('mode', sys_graph.renderer.switchMode)
        	  nav.init()
          },
          error : function (errorInfo , errorType) {
        	  alert('语义图获取失败')
              console.log(errorInfo);
          }
      });
  })
})(this.jQuery)