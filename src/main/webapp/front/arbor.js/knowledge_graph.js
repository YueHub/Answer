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
    var dom_knowledge_graph = $(elt)
    var canvas_knowledge_graph = dom_knowledge_graph.get(0)
    var ctx_knowledge_graph = canvas_knowledge_graph.getContext("2d");
    var gfx_knowledge_graph = arbor.Graphics("#knowledge_graph")
    var sys_knowledge_graph = null

    var _vignette = null
    var selected = null,
        nearest = null,
        _mouseP = null;

    
    var that = {
      init:function(pSystem){
    	sys_knowledge_graph = pSystem
    	sys_knowledge_graph.screen({size:{width:dom_knowledge_graph.width(), height:dom_knowledge_graph.height()},
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
    	canvas_knowledge_graph.width = $(window).width()
        canvas_knowledge_graph.height = .75* $(window).height()
        sys_knowledge_graph.screen({size:{width:canvas_knowledge_graph.width, height:canvas_knowledge_graph.height}})
        _vignette = null
        that.redraw()
      },
      redraw:function(){
    	gfx_knowledge_graph.clear()
        sys_knowledge_graph.eachEdge(function(edge, p1, p2){
          if (edge.source.data.alpha * edge.target.data.alpha == 0) return
          gfx_knowledge_graph.line(p1, p2, {stroke:"#b2b19d", width:2, alpha:edge.target.data.alpha})
          // TODO 修改
          gfx_knowledge_graph.text(edge.data.name, (Math.abs(p2.x + p1.x))/2, (Math.abs(p2.y + p1.y))/2, {color:edge.data.color, align:"center", font:"Arial", size:edge.data.size})
        })
        // 结点标签
        sys_knowledge_graph.eachNode(function(node, pt){
          var w = Math.max(20, 20+gfx_knowledge_graph.textWidth(node.name) )
          if (node.data.alpha===0) return
          if (node.data.shape=='dot'){
        	gfx_knowledge_graph.oval(pt.x-w/2, pt.y-w/2, w, w, {fill:node.data.color, alpha:node.data.alpha})
            gfx_knowledge_graph.text(node.name, pt.x, pt.y+7, {color:"white", align:"center", font:"Arial", size:12})
            gfx_knowledge_graph.text(node.name, pt.x, pt.y+7, {color:"white", align:"center", font:"Arial", size:12})
          }else{
        	gfx_knowledge_graph.rect(pt.x-w/2, pt.y-8, w, 20, 4, {fill:node.data.color, alpha:node.data.alpha})
            gfx_knowledge_graph.text(node.name, pt.x, pt.y+9, {color:"white", align:"center", font:"Arial", size:12})
            gfx_knowledge_graph.text(node.name, pt.x, pt.y+9, {color:"white", align:"center", font:"Arial", size:12})
          }
        })
        // 连接线标签
        /*sys_knowledge_graph.eachEdge(function(edge, pt1, pt2){
          var w = Math.max(20, 20+gfx.textWidth(edge.name) )
          if (edge.data.alpha===0) return
            gfx.oval(pt.x-w/2, pt.y-w/2, w, w, {fill:edge.data.color, alpha:node.data.alpha})
            gfx.text(edge.name, Math.abs(pt1.x - pt2.x)/2, Math.abs(pt1.y - pt2.y)/2, {color:"white", align:"center", font:"Arial", size:12})
            gfx.text(edge.name, Math.abs(pt1.x - pt2.x)/2, Math.abs(pt1.y - pt2.y)/2, {color:"white", align:"center", font:"Arial", size:12})
        })*/
        that._drawVignette()
      },
      
      _drawVignette:function(){
        var w = canvas_knowledge_graph.width
        var h = canvas_knowledge_graph.height
    	/*var w = 700
        var h = 448*/
        var r = 20

        if (!_vignette){
          var top = ctx_knowledge_graph.createLinearGradient(0,0,0,r)
          top.addColorStop(0, "#e0e0e0")
          top.addColorStop(.7, "rgba(255,255,255,0)")

          var bot = ctx_knowledge_graph.createLinearGradient(0,h-r,0,h)
          bot.addColorStop(0, "rgba(255,255,255,0)")
          bot.addColorStop(1, "white")

          _vignette = {top:top, bot:bot}
        }
        
        // top
        ctx_knowledge_graph.fillStyle = _vignette.top
        ctx_knowledge_graph.fillRect(0,0, w,r)

        // bot
        ctx_knowledge_graph.fillStyle = _vignette.bot
        ctx_knowledge_graph.fillRect(0,h-r, w,r)
      },

      switchMode:function(e){
        if (e.mode=='hidden'){
        	dom_knowledge_graph.stop(true).fadeTo(e.dt,0, function(){
            if (sys_knowledge_graph) sys_knowledge_graph.stop()
            $(this).hide()
          })
        }else if (e.mode=='visible'){
        	dom_knowledge_graph.stop(true).css('opacity',0).show().fadeTo(e.dt,1,function(){
            that.resize()
          })
          if (sys_knowledge_graph) sys_knowledge_graph.start()
        }
      },
      
      switchSection:function(newSection){
        var parent = sys_knowledge_graph.getEdgesFrom(newSection)[0].source
        var children = $.map(sys_knowledge_graph.getEdgesFrom(newSection), function(edge){
          return edge.target
        })
        
        sys_knowledge_graph.eachNode(function(node){
          if (node.data.shape=='dot') return // skip all but leafnodes

          var nowVisible = ($.inArray(node, children)>=0)
          var newAlpha = (nowVisible) ? 1 : 0
          var dt = (nowVisible) ? .5 : .5
          sys_knowledge_graph.tweenNode(node, dt, {alpha:newAlpha})

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
        	// canvas修改成了"#knowledge_graph"
            //var pos = $(canvas).offset();
        	var pos = $("#knowledge_graph").offset();
        	  
            _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
            nearest = sys_knowledge_graph.nearest(_mouseP);

            if (!nearest.node) return false

            if (nearest.node.data.shape!='dot'){
              // 如果距离结点很近 代表选择了该节点 则将弹出连接
              selected = (nearest.distance < 20) ? nearest : null
            // TODO 将selected修改为propertyShow
             if (selected){
            	 dom_knowledge_graph.addClass('linkable')
                 window.status = selected.node.data.link.replace(/^\//,"http://"+window.location.host+"/").replace(/^#/,'')
              }
    		  /*if(propertyShow){
                  dom_knowledge_graph.addClass('linkable')
                  window.status = selected.node.data.link.replace(/^\//,"http://"+window.location.host+"/").replace(/^#/,'')
    		  }*/
              // 否则删除连接
              else{
            	 dom_knowledge_graph.removeClass('linkable')
                 window.status = ''
              }
            }else if ($.inArray(nearest.node.name, ['arbor.js','code','docs','demos']) >=0 ){
              if (nearest.node.name!=_section){
                _section = nearest.node.name
                that.switchSection(_section)
              }
              dom_knowledge_graph.removeClass('linkable')
              window.status = ''
            }
            
            return false
          },
          clicked:function(e){
            var pos = $("#knowledge_graph").offset();
            _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
            nearest = dragged = sys_knowledge_graph.nearest(_mouseP);
            
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
            $("#knowledge_graph").unbind('mousemove', handler.moved);
            $("#knowledge_graph").bind('mousemove', handler.dragged)
            $(window).bind('mouseup', handler.dropped)

            return false
          },
          dragged:function(e){
            var old_nearest = nearest && nearest.node._id
            var pos = $("#knowledge_graph").offset();
            var s = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)

            if (!nearest) return
            if (dragged !== null && dragged.node !== null){
              var p = sys_knowledge_graph.fromScreen(s)
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
            $("#knowledge_graph").unbind('mousemove', handler.dragged)
            $(window).unbind('mouseup', handler.dropped)
            $("#knowledge_graph").bind('mousemove', handler.moved);
            _mouseP = null
            return false
          }


        }
        $("#knowledge_graph").mousedown(handler.clicked);
        // 绑定鼠标移动事件的函数
        $("#knowledge_graph").mousemove(handler.moved);
        
      }
    }
    
    return that
  }
  
  
  var Nav = function(elt){
    var dom_knowledge_graph = $(elt)

    var _path = null
    
    var that = {
      init:function(){
        $(window).bind('popstate',that.navigate)
        dom_knowledge_graph.find('> a').click(that.back)
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
          dom_knowledge_graph.find('p').css('opacity',0).show().fadeTo('slow',1)
        }

        switch (_path){
          case '':
          case '/':
          dom_knowledge_graph.find('p').text('a graph visualization library using web workers and jQuery')
          dom_knowledge_graph.find('> a').removeClass('active').attr('href','#')

          $('#docs').fadeTo('fast',0, function(){
            $(this).hide()
            $(that).trigger({type:'mode', mode:'visible', dt:dt})
          })
          document.title = "arbor.js"
          break
          
          case 'introduction':
          case 'reference':
          $(that).trigger({type:'mode', mode:'hidden', dt:dt})
          dom_knowledge_graph.find('> p').text(_path)
          dom_knowledge_graph.find('> a').addClass('active').attr('href','#')
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
	  var targetURL = "developerAction!getKnowledgeGraph.action"
	  $.ajax({
          url : targetURL,
          type : "POST",
          async : true,
          dataType : "json",
          timeout : 10000 ,
          success : function (returnVal) {
        	  var CLR = {
        			  branch:"#b2b19d",
        		      code:"orange",
        		      doc:"#922E00",
        		      demo:"#a7af00"
        	  }
        	  if(returnVal.length == 0) {
        		  $('#knowledge').css('display','none');
        		  return;
        	  }
        	  
        	  // 初始化
        	  var sys_knowledge_graph = arbor.ParticleSystem()
        	  // 设置相关参数	stiffness:硬度	repulsion:排斥力	gravity:重力  
        	  sys_knowledge_graph.parameters({stiffness:900, repulsion:2000, gravity:true, dt:0.015})
        	  // 读取绘图区域
        	  sys_knowledge_graph.renderer = Renderer("#knowledge_graph")
        	  // 传入结点和连接
        	  // 添加结点
        	  
        	  for(var i = 0; i < returnVal.length; i++) {
        		  var knowledgeGraphStatements = returnVal[i].knowledgeGraphStatements;
        		  for(var j = 0; j < knowledgeGraphStatements.length; j++) {
            		  // 添加结点
            		  // 先添加普通结点 避免实体颜色不能正常显示
            		  var objectName = knowledgeGraphStatements[j].object.name
            		  var data = knowledgeGraphStatements[j].object
            		  sys_knowledge_graph.addNode(objectName, data)
            		  
            		  var subjectName = knowledgeGraphStatements[j].subject.name
            		  var data = knowledgeGraphStatements[j].subject
            		  sys_knowledge_graph.addNode(subjectName, data)
            		  // 添加连线
            		  var sourceName = knowledgeGraphStatements[j].subject.name
            		  var targetName = knowledgeGraphStatements[j].object.name
            		  var data = knowledgeGraphStatements[j].predicate
            		  sys_knowledge_graph.addEdge(sourceName, targetName, data)
            	  }
        	  }
				
        	  var nav = Nav("#nav")
        	  $(sys_knowledge_graph.renderer).bind('navigate', nav.navigate)
        	  $(nav).bind('mode', sys_knowledge_graph.renderer.switchMode)
        	  nav.init()
          },
          error : function (errorInfo , errorType) {
        	  alert('知识图谱获取失败')
              console.log(errorInfo);
          }
      });
  })
})(this.jQuery)