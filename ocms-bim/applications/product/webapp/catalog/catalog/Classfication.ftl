<link href="/commondefine_css/FacnyTable/ui.fancytree.css"rel="stylesheet">
<link href="/commondefine_css/product_info.css" rel="stylesheet">
<link href="/commondefine_css/yw_main.css" rel="stylesheet">

</head>
<div id="create_product">
    <div class="create_product_content_wap">
        <div class="create_product_content">
            <div class="create_product_detail">
                <div class="create_product_detail_zIndex">
                    <a onclick="clickAll()" class="checkAll">Select All</a>
                    <a onclick="refresh()" class="checkAll">Refresh</a>
                    <a onclick="closeClassfication()" class="expandTab">Collapse All</a>
                </div>
                <table id="tree" class="treeTable">
                    <thead>
                        <tr>
                            <th></th>
                            <th>Category ID</th>
                            <th>Category Name</th>
                            <th>Move</th>
                            <th>Created Date</th>
                            <th>Operation</th>
                            <th>Feature Relation</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td></td>
                            <td></td>
                            <td class="alignLeft"></td>
                            <td>
                                <span></span>
                                <a class="moveUp" href="javascript:void(0);"></a>
                                <a class="moveDown" href="javascript:void(0);"></a>
                            </td>
                            <td><span></span></td>
                            <td>
                                <a href="javascript:void(0);" class="editclass">Edit</a>&nbsp;&nbsp;
                                <a href="javascript:void(0);" class="deleteclass">Delete</a>
                            </td>
                            <td>
                                <a href="javascript:void(0);" class="operatorClass">View or Edit</a>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div class="viewOrEditTpl">
	<div class="viewOrEditScreen"></div>
	<div class="viewOrEditContent" id="viewOrEditContent">
		<div class="viewOrEditContentIcon"></div>
		<div class="currentCategoryCondition">
			<div class="currentCategoryTitle">Current Category Details</div>
			<div class="chooseCategory">
				<select class="selectFirstCategory">
					<option value="">Please Select</option>
				</select>
				<select class="selectSecondCategory">
					<option value="">Please Select</option>
				</select>
				<select class="selectThirdCategory">
					<option value="">Please Select</option>
				</select>
			</div>
		</div>
		<div class="currentCategoryContent">
			<div class="categoryFeatureTitle">Product Category Feature</div>
			<div class="categoryFeatureContent">
				<div class="categoryFeatureBox firstCategoryFeatureBox">
					<span class="categoryFeatureBoxTitle">First Category Feature : </span>
					<div class="firstCategoryFeature">
					</div>
				</div>
				<div class="categoryFeatureBox secondCategoryFeatureBox">
					<span class="categoryFeatureBoxTitle">Second Category Feature : </span>
					<div class="secondCategoryFeature">
					</div>
				</div>
				<div class="categoryFeatureBox thirdCategoryFeatureBox">
					<span class="categoryFeatureBoxTitle">Third Category Feature : </span>
					<div class="thirdCategoryFeature">
					</div>
				</div>
			</div>
			<div style="clear:both;"></div>
		</div>
		<div class="currentCategoryOperator">
			<a href="javascript:void(0);" class="confirmBtn">Confirm</a>
		</div>
		<div style="clear:both;"></div>
	</div>
</div>
<div class="error_tips">
    <div class="fixed_screen"></div>
    <div class="error_tips_box">
        <div class="error_tips_title">Tip Message</div>
        <div class="error_tips_content">
            <span></span>
        </div>
        <div class="error_tips_operator">
            <a href="javascript:void(0);" class="confirm">Confirm</a>
        </div>
    </div>
</div>
<script src="/commondefine_js/jQuery/jquery-ui.min.js"></script>
<script src="/commondefine_js/FacnyTable/jquery.fancytree-all.min.js"></script>
<script src="/commondefine_js/jQuery/jquery.ui-contextmenu.js"></script>
<script type="text/javascript">
var CLIPBOARD = null;
var childOrSibling;
var clickAllFlag = false;
var moveFlag=true;
var flag= false;

//全选
function clickAll(){
	if(clickAllFlag==true){
		clickAllFlag=false;
	}else if(clickAllFlag==false){
		clickAllFlag = true;
	}
	
	jQuery("#tree").fancytree("getRootNode").visit(function(node) {				
       node.setSelected(flag=clickAllFlag);                
	});
}

//更新source
function initSource(data){
   var source = new Array();
   
	 //获得第一层目录    	        	
   for(var firstIndex in data){
       var temp1 = data[firstIndex];
       if(temp1.parentCategoryId=="DefaultRootCategory"){
	        source.push(temp1);
       }			  
	}

	for(var i=0;i<source.length;i++){
	    var firstCategoryId = source[i].categoryId;
	    var secondCategory = new Array();
	    source[i].children = secondCategory;
	    //把二级类目放入相应的一级
	    for(var eachCategory1 in data){
	    	var temp2 = data[eachCategory1];
// 	    	temp2.lazy=true;
	    	if(temp2.parentCategoryId==firstCategoryId){
	    		secondCategory.push(temp2);
	    	}
	    }
	}
	     
	for(var m=0;m<source.length;m++){
	    var second = source[m].children;
	    //循环所有的二级菜单
		for(var n=0;n<second.length;n++){
		  var secondCategoryId = second[n].categoryId;
		  var thirdCategory = new Array();
		  second[n].children = thirdCategory;
		  //把三级类目放入相应的二级
		  for(var eachCategory2 in data){
		     var temp3 = data[eachCategory2];		    				    		
		     if(temp3.parentCategoryId==secondCategoryId){
		    	thirdCategory.push(temp3);
		    	}
		  }
    	}
	}
	
	for(var m in data){
	   	data[m].title= data[m].categoryName;
	   	data[m].key= data[m].categoryId;
   }

	return source;
}

function sortTable() {
    jQuery("#tree").fancytree("getRootNode").sortChildren(function(a,b){
    	return a.data.sequenceNum-b.data.sequenceNum;
    }, deep=true);
}

//创建新结点时，该节点所属的本层数组
function createNewCategory(nodeArr,nodeParentCategoryId){
	var categoryArr = new Array();//本级节点类目
// 	categoryArr.splice(0,categoryArr.length);
	var index=nodeArr.length;
	
	for(var k=0;k<index;k++){
		var childNodeData = nodeArr[k].data;
		console.log(nodeArr[k].data, k);
		var categoryName = nodeArr[k].title;
		if(/(&lt;(.*)&gt;)|(&lt;\/(.*)&gt;)/.test(categoryName)){
			flag = true;
			nodeArr[k].remove();
			index =index-1;
		}

		if(!childNodeData.hasOwnProperty("categoryId")){
			childNodeData.categoryId = null;
			childNodeData.categoryName = categoryName;
			childNodeData.parentCategoryId = nodeParentCategoryId; 
		}
		childNodeData.sequenceNum = k+1;
		categoryArr.push(childNodeData);
	}
	return categoryArr;
}

//新建节点，包括兄弟节点和子类节点
function createNewNode(data){
	var nodeParentCategoryId;//新创建的节点的父节点属性					
	var categoryArr = new Array();
	var nodeArr = new Array();
	var newNode = data.node;
	
	newNode.data.createTime= new Date().getTime();

	var title = newNode.title;
	
	if(childOrSibling=='child'){
		nodeParentCategoryId = newNode.parent.data.categoryId;
		nodeArr =newNode.parent.children;		
	}	
	if(childOrSibling=='sibling'){
		nodeParentCategoryId = newNode.parent.data.categoryId;
		nodeArr = newNode.parent.children
		
		if(nodeParentCategoryId==null){
			nodeParentCategoryId="DefaultRootCategory";
			nodeArr = jQuery("#tree").fancytree("getRootNode").children		
		}
	}
	
	for(var l=0;l<nodeArr.length;l++){
		var titleCount =0;
// 		console.log(l+":"+nodeArr[l].title);
		$.each(nodeArr,function(index,elem){
			if(nodeArr[index].title==title){
				titleCount++
			}
// 			console.log(index); 
		})		
		if(titleCount>=2){
			alert("Create failed, new category with the same name is already exist in this category level!");
			newNode.remove();
			return;
		}
	}

	categoryArr = createNewCategory(nodeArr,nodeParentCategoryId);
	
	if(flag == true){
		alert('Cannot enter labels or scripts!');
		flag = false;
		return false;
	}
	
	jQuery.ajax({
		dataType:'json',
		type: 'post',
		url: '/catalog/control/CreateClassfication',
		contentType: "application/json;charset=utf-8",
		data:JSON.stringify({							
			categoryJSONArr:categoryArr
		}),
		success: function (data) {
			if(data.responseMessage=="success"){
	 			var newId =data.newCategoryId;
// 	 			console.log(newId)
	 			newNode.data.categoryId = newId;	
				$tdList = jQuery(newNode.tr).find(">td");
	 			
				$tdList.eq(1).text(newId);
				$tdList.eq(4).find("span").text(formatDate(new Date(newNode.data.createTime)));
 				console.log(newNode);
			}
			else if(data.responseMessage=="error"){			
				alert(data.errorMessage);
				newNode.remove();
				return;
			}
		}      
	});	
}

//初始化Table
function initTable(source){
		
	jQuery("#tree").fancytree({
		checkbox: true,
// 		titlesTabbable: true,     // Add all node titles to TAB chain
// 		quicksearch: true,        // Jump to nodes when pressing first character	
		source: source,
	
		extensions: ["edit", "table", "gridnav"],
		edit: {
			close: function(event, data1) {
				//创建新节点
				if( data1.save && data1.isNew ){					
					createNewNode(data1);					
				}else{

				  if(/(&lt;(.*)&gt;)|(&lt;\/(.*)&gt;)/.test(data1.node.title)){
					  alert('Cannot enter labels or scripts!');
						data1.node.data.categoryName=data1.orgTitle;
						data1.node.title=data1.orgTitle;
						data1.node.renderTitle();
					  return; 
				  }
				  
				  if((data1.node!=null) && (data1.node.title!=data1.orgTitle)){		
					    jQuery.ajax({
							dataType:'json',
							type: 'post',
							contentType : 'application/json;charset=utf-8',
							url: '/catalog/control/EditClassfication',
							data:JSON.stringify({
								categoryId:data1.node.key,
								categoryName:data1.node.title,
								parentCategoryId:data1.node.data.parentCategoryId
							}),
							success: function (data) {
								if(!data.success){
// 						 			var newId =data.newCategoryId;
// //					 	 			console.log(newId)
// 						 			newNode.data.categoryId = newId;	
// 									$tdList = jQuery(data1.tr).find(">td");
						 			
// 									$tdList.eq(1).text(newId);
// 									jQuery(data1.tr).find(">td");.eq(4).find("span").text(formatDate(new Date(newNode.data.createTime)));
									
									data1.node.data.categoryName=data1.orgTitle
									data1.node.title=data1.orgTitle
									data1.node.renderTitle()
									alert(data.message);
								}else{
									data1.node.data.categoryName = data1.node.title;
								}
							}
						});
// 					    data1.node.data.categoryName = data1.node.title;
					}
				}
			}
		},			
			
		table: {
			indentation: 30,
			nodeColumnIdx: 2,
			checkboxColumnIdx: 0
		},
		gridnav: {
			autofocusInput: false,
			handleCursorKeys: true
		},
		createNode: function(event, data) {
			var node = data.node,
				$tdList = jQuery(node.tr).find(">td");
			// Span the remaining columns if it's a folder.
			// We can do this in createNode instead of renderColumns, because
			// the `isFolder` status is unlikely to change later
			if( node.isFolder() ) {
				$tdList.eq(2)
					.prop("colspan", 6)
					.nextAll().remove();
			}
		},
		//渲染行
		renderColumns: function(event, data) {
			var node = data.node,
			$trList = jQuery(node.tr);
			$tdList = jQuery(node.tr).find(">td");
			// (Index #0 is rendered by fancytree by adding the checkbox)
			// Set column #1 info from node data:
// 			$tdList.eq(1).text(node.getIndexHier());
			
			$trList.attr('data-id', node.data.categoryId);
			$trList.attr('data-parentCategoryId', node.data.parentCategoryId);
			
			$tdList.eq(1).text(node.data.categoryId);
			if(node.getLevel()==3){
				$tdList.eq(3).prepend("<span style='margin-left:6em;'></span>");
			}else if(node.getLevel()==2){
				$tdList.eq(3).prepend("<span style='margin-left:3em;'></span>");
			}
			$tdList.eq(4).find("span").text(formatDate(new Date(node.data.createTime)));
		}
	}).on("nodeCommand", function(event, data){
		// Custom event handler that is triggered by keydown-handler and
		// context menu:
		var refNode, moveMode,
			tree = jQuery(this).fancytree("getTree"),
			node = tree.getActiveNode();
 		
		switch( data.cmd ) {
		case "moveUp":
			refNode = node.getPrevSibling();
			if( refNode ) {
				node.moveTo(refNode, "before");
				node.setActive();
			}
			break;
		case "moveDown":
			refNode = node.getNextSibling();
			if( refNode ) {
				node.moveTo(refNode, "after");
				node.setActive();
			}
			break;
		case "rename":
			node.editStart();
			break;
// 		case "remove":
// 			refNode = node.getNextSibling() || node.getPrevSibling() || node.getParent();
// 			node.remove();
// 			if( refNode ) {
// 				refNode.setActive();
// 			}
// 			break;
		case "addChild":
			childOrSibling = "child";
			node.editCreateNode("child", "");
			break;
		case "addSibling":
			childOrSibling = "sibling";
			node.editCreateNode("after", "");
			break;
		default:
			alert("Unhandled command: " + data.cmd);
			return;
		}	
	})	
	sortTable();
}


function initMenu(){
	jQuery("#tree").contextmenu({
		delegate: "span.fancytree-node",
		menu: [
// 			{title: "Edit classfication<kbd>Rename&nbsp;</kbd>", cmd: "rename", uiIcon: "ui-icon-pencil" },
// 			{title: "----"},
			{title: "Create sibling-classfication<kbd>Sibling</kbd>", cmd: "addSibling", uiIcon: "ui-icon-plus" },
			{title: "----"},
			{title: "Create child-classfication<kbd>Child&nbsp;&nbsp;</kbd>", cmd: "addChild", uiIcon: "ui-icon-arrowreturn-1-e" }
			],
		beforeOpen: function(event, ui) {
			var node = jQuery.ui.fancytree.getNode(ui.target);

				jQuery("#tree").contextmenu("enableEntry", "paste", !!CLIPBOARD);
				node.setActive();

		},
		select: function(event, ui) {
			var node = jQuery.ui.fancytree.getNode(ui.target);
			if(node.getLevel()!=3||ui.cmd!="addChild"){
				var that = this;
				// delay the event, so the menu can close and the click event does
				// not interfere with the edit control
				setTimeout(function(){
					jQuery(that).trigger("nodeCommand", {cmd: ui.cmd});
				}, 100);
			}else{
				alert("Cannot create child-classfication in three-level category")
			}
		}
	});
}

//刷新按钮事件
function refresh(){
	jQuery.ajax({
	    type: 'post',
	    dataType: 'json',
	    url: '/catalog/control/ListDefaultClassfication',
		
	    success: function(data){
	    	if(data.success){
	    		var productCategoryBeanMap = data.data.productCategoryBeanMap;
		    	var source = initSource(productCategoryBeanMap);
		    	jQuery("#tree").fancytree("getTree").reload(source);
		    	sortTable();
	    	}
	    }
	});
}
	
window.onload = function(){ 	
	
	jQuery.ajax({
	    type: 'post',
	    dataType: 'json',
	    url: '/catalog/control/ListDefaultClassfication',
	
	    success: function(res){
	    	var dataResult = res.data.productCategoryBeanMap;
	    	if(res.success){
	    	console.log(dataResult);
		    	var source = initSource(dataResult);
		    	initTable(source);
		        
		        initMenu();
	    	}
	    }
    });
	
	
	/* Handle custom checkbox clicks */
	jQuery("#tree").delegate(".deleteclass", "click", function(e){
    	if(confirm("Do you want to delete this category?")){
			var node = jQuery.ui.fancytree.getNode(e);
    		
			jQuery.ajax({
			    type: 'post',
			    dataType: 'json',
			    contentType : 'application/json;charset=utf-8',
			    url: '/catalog/control/DeleteClassfication',
			    data:JSON.stringify({
					categoryId:node.data.categoryId,
					parentProductCategoryId:node.data.parentCategoryId
				}),
			    success: function(data){
			    	if(data.success){
			    		 node.remove();
			    		 alert("Delete Successful")
			    	}else{
			    		 alert(data.message);
			    	}
			    }
		    });
    	}
	});
	
	/* Handle custom rename clicks */
	jQuery("#tree").delegate(".editclass", "click", function(e){
		var that = this;
			var node = jQuery.ui.fancytree.getNode(e);			
			node.setActive();
			setTimeout(function(){
				jQuery(that).trigger("nodeCommand", {cmd: "rename"});
			}, 100);    	
	});

	/* Handle custom moveUp clicks */
	jQuery("#tree").delegate(".moveUp", "click", function(e){
		var that = this;
			var node = jQuery.ui.fancytree.getNode(e);
			jQuery(that).trigger("nodeCommand", {cmd: "moveUp"});
			setMoveTimer(node)
    	
	});
	
	/* Handle custom moveDown clicks */
	jQuery("#tree").delegate(".moveDown", "click", function(e){
		var that = this;
			var node = jQuery.ui.fancytree.getNode(e);
			jQuery(that).trigger("nodeCommand", {cmd: "moveDown"});
			setMoveTimer(node)  	
	});
}

//设置1秒发送延迟
function setMoveTimer(node){
	if(moveFlag==false){
		window.clearTimeout(timer);
	}
	var arr = chanegClassficationSeq(node);
	
	timer = window.setTimeout(function(){
		jQuery.ajax({
			dataType:'json',
			type: 'post',
			url: '/catalog/control/MoveClassfication',
			contentType: "application/json;charset=utf-8",
			data:JSON.stringify({							
				categoryJSONArr: arr
			}),
			success: function (data) {
				if(data.success){
					moveFlag = true;
				}else{
					alert(data.errorMessage);
				}
			}      
		});		
	},1000);
	
	moveFlag =false;
}	

//创建新结点时，该节点所属的本层数组Json
function chanegClassficationSeq(node){
	var categoryArr = new Array();//本级节点类目

	nodeArr =node.parent.children;
	var index=nodeArr.length;
	
	for(var k=0;k<index;k++){
		var childNodeData = nodeArr[k].data;
		childNodeData.sequenceNum = k+1;
		categoryArr.push(childNodeData);
	}
	
	return categoryArr;
}
//收缩目录
function closeClassfication(){
	jQuery("#tree").fancytree("getRootNode").visit(function(node) {
        node.setExpanded(false);
});
}
</script>

<!--  create by zouzhijie  -->
<script>
	$(document).ready(function(){
		
        function showErrorMessage(message, callBack){
            var errorTips = jQuery('.error_tips');
            errorTips.find('.error_tips_content span').html('').html(message);
            errorTips.show();

	        errorTips.find('.error_tips_operator').off('click','a').on('click','a',function () {
		        $('.error_tips').hide();
		        callBack && callBack();
	        })
        }
        
	    function sentAjax(options, callBack){
            $.ajax({
                url:"/catalog/control/listCategoryFeatureTree",
                type: 'post',
                dataType: 'json',
                contentType: "application/json;charset=utf-8",
                data:JSON.stringify(options),
                success: function(dataResult){
                    if(dataResult.success){
                    	callBack(dataResult.data);
                    }else{
                    	showErrorMessage(dataResult.msg);
                    }
                }
            });
	    }
	    
		function getFirstCatalogues(callBack){
			var cataloguesFirst = $(".selectFirstCategory");
			var responseData,optionContent='';
			
			$.ajax({
			    type: 'post',
			    dataType: 'json',
			    contentType : 'application/json;charset=utf-8',
			    url: '/catalog/control/ListGivenClassfication',
			    data:JSON.stringify({
					categoryId : 'DefaultRootCategory'
				}),
				
			    success: function(res){
				    if(res.success){
			    		responseData = (res.data.categoryList).sort(function(a, b){
	 		    			if(a.sequenceNum == b.sequenceNum){
	 		    				return a.categoryId - b.categoryId;
	 		    			}else{
	 		    				return a.sequenceNum - b.sequenceNum;
	 		    			}
			    		});
			    		jQuery.each(responseData, function(index, elem){
			    			optionContent+='<option value="'+ elem['categoryId']  +'">'+ elem['categoryName'] +'</option>';
			    		});
			    		cataloguesFirst.children().not(':first-child').remove();
			    		cataloguesFirst.append(optionContent);
			    		callBack && callBack();
			    	}else{
			    		showErrorMessage(res.msg);
			    	}
			    }
			});
		}
	
		function getSecondCatalogues(categoryId, callBack){
			var cataloguesSecond = $(".selectSecondCategory");
	        if(!categoryId){
	            cataloguesSecond.children().not(':first-child').remove();
	
	            return false;
	        }
	
			var responseData,optionContent='';;
			$.ajax({
			    type: 'post',
			    dataType: 'json',
			    contentType : 'application/json;charset=utf-8',
			    url: '/catalog/control/ListGivenClassfication',
			    data:JSON.stringify({
					categoryId : categoryId
				}),
			    success: function(res){
				    if(res.success){
			    		responseData = (res.data.categoryList).sort(function(a, b){
	 		    			if(a.sequenceNum == b.sequenceNum){
	 		    				return a.categoryId - b.categoryId;
	 		    			}else{
	 		    				return a.sequenceNum - b.sequenceNum;
	 		    			}
			    		});
			    		$.each(responseData, function(index, elem){
			    			optionContent+='<option value="'+ elem['categoryId']  +'">'+ elem['categoryName'] +'</a></li>';
			    		});
			    		cataloguesSecond.children().not(':first-child').remove();
			    		cataloguesSecond.append(optionContent);
	
			    		callBack && callBack();
			    	}else{
			    		showErrorMessage(res.msg);
			    	}
			    }
			});
		}
	
		function getThirdCatalogues(categoryId, callBack){
			var cataloguesThird = $(".selectThirdCategory");
	        if(!categoryId){
	            cataloguesThird.children().not(':first-child').remove();
	
	            return false;
	        }
			var responseData,optionContent='';
			$.ajax({
			    type: 'post',
			    dataType: 'json',
			    contentType : 'application/json;charset=utf-8',
			    url: '/catalog/control/ListGivenClassfication',
			    data:JSON.stringify({
					categoryId : categoryId
				}),
			    success: function(res){
				    if(res.success){
			    		responseData = (res.data.categoryList).sort(function(a, b){
	 		    			if(a.sequenceNum == b.sequenceNum){
	 		    				return a.categoryId - b.categoryId;
	 		    			}else{
	 		    				return a.sequenceNum - b.sequenceNum;
	 		    			}
			    		});
			    		$.each(responseData, function(index, elem){
			    			optionContent+='<option value="'+ elem['categoryId']  +'">'+ elem['categoryName'] +'</option>';
			    		});
			    		cataloguesThird.children().not(':first-child').remove();
			    		cataloguesThird.append(optionContent);
	
			    		callBack && callBack();
			    	}else{
			    		showErrorMessage(res.message);
			    	}
			    }
			});
		}
		
	    function selectedList(options){
	    	$(".selectFirstCategory").prop("disabled",true).css("background",'#dbdbdb');
	    	$(".selectSecondCategory").prop("disabled",true).css("background",'#dbdbdb');
	    	$(".selectThirdCategory").prop("disabled",true).css("background",'#dbdbdb');
	    	
            $(".selectFirstCategory").find('option[value="'+ options.firstId +'"]').prop("selected","selected");
            getSecondCatalogues(options.firstId, function(){
                $(".selectSecondCategory").find('option[value="'+ options.secondId +'"]').prop("selected","selected");
                getThirdCatalogues(options.secondId, function(){
                    $(".selectThirdCategory").find('option[value="'+ options.thirdId +'"]').prop("selected","selected");
                });
            });
	    }
		
		$('.treeTable').on('click', '.operatorClass', function(e){
			var flag = false;
			var currentId = $(e.currentTarget).closest('tr').attr('data-id');
			var parentcategoryid = $(e.currentTarget).closest('tr').attr('data-parentcategoryid');
			
 			//sent ajax
			sentAjax({
				categoryId : currentId,
				parentCategoryId : parentcategoryid
			}, function(data){
				var dataResult = data.reverse();
				//get category
				getFirstCatalogues(function(){
                    selectedList({
                        firstId : data[0] ? data[0].categoryId : '',
                        secondId : data[1] ? data[1].categoryId : '',
                        thirdId : data[2] ? data[2].categoryId : ''
                    })
               	}); 
				
				//get feature
				if(data[0]){
					var htmlContent = '';
					if(data[1]){
						if(data[0] && data[0].featureTypeAndFeatureVO && data[0].featureTypeAndFeatureVO.length){
							$.each(data[0].featureTypeAndFeatureVO, function(index, elem){
								htmlContent += '<span class="featureParent" data-id="'+ elem.productFeatureTypeId +'">'+ elem.productFeatureName +'</span>'
							})
						}
					}else{
						$.each(data[0].wholeFeatureType, function(index, elem){
 							var featureTypeAndFeatureVO=[];
 							data[0].featureTypeAndFeatureVO && data[0].featureTypeAndFeatureVO.length && (data[0].featureTypeAndFeatureVO).map(function(elem) {
 								featureTypeAndFeatureVO.push(elem.productFeatureTypeId);
							});
								
							if(featureTypeAndFeatureVO.indexOf(elem.productFeatureTypeId) != -1){
								htmlContent += '<a href="javascript:void(0);" data-id="'+ elem.productFeatureTypeId +'"class="featureSpan active">'+ elem.description +'</a>';
							}else{
								htmlContent += '<a href="javascript:void(0);" data-id="'+ elem.productFeatureTypeId +'"class="featureSpan">'+ elem.description +'</a>';
							}
						})
					}
					$('.firstCategoryFeature').html(htmlContent);
				}else{
					$('.firstCategoryFeature').html('');
				}
				
				if(data[1]){
					var htmlContent = '';
					if(data[2]){
						if(data[1] && data[1].featureTypeAndFeatureVO && data[1].featureTypeAndFeatureVO.length){
							$.each(data[1].featureTypeAndFeatureVO, function(index, elem){
								htmlContent += '<span class="featureParent" data-id="'+ elem.productFeatureTypeId +'">'+ elem.productFeatureName +'</span>'
							})
						}
					}else{
						$.each(data[1].wholeFeatureType, function(index, elem){
 							var featureTypeAndFeatureVO=[];
 							data[1].featureTypeAndFeatureVO && data[1].featureTypeAndFeatureVO.length && (data[1].featureTypeAndFeatureVO).map(function(elem) {
 								featureTypeAndFeatureVO.push(elem.productFeatureTypeId);
							});
								
							if(featureTypeAndFeatureVO.indexOf(elem.productFeatureTypeId) != -1){
								htmlContent += '<a href="javascript:void(0);" data-id="'+ elem.productFeatureTypeId +'"class="featureSpan active">'+ elem.description +'</a>';
							}else{
								htmlContent += '<a href="javascript:void(0);" data-id="'+ elem.productFeatureTypeId +'"class="featureSpan">'+ elem.description +'</a>';
							}
						})
					}
					$('.secondCategoryFeature').html(htmlContent);
				}else{
					$('.secondCategoryFeature').html('');
				}
				
				if(data[2]){
					var htmlContent = '';
					$.each(data[2].wholeFeatureType, function(index, elem){
							var featureTypeAndFeatureVO=[];
							data[2].featureTypeAndFeatureVO && data[2].featureTypeAndFeatureVO.length && (data[2].featureTypeAndFeatureVO).map(function(elem) {
								featureTypeAndFeatureVO.push(elem.productFeatureTypeId);
						});
							
						if(featureTypeAndFeatureVO.indexOf(elem.productFeatureTypeId) != -1){
							htmlContent += '<a href="javascript:void(0);" data-id="'+ elem.productFeatureTypeId +'"class="featureSpan active">'+ elem.description +'</a>';
						}else{
							htmlContent += '<a href="javascript:void(0);" data-id="'+ elem.productFeatureTypeId +'"class="featureSpan">'+ elem.description +'</a>';
						}
					})
					$('.thirdCategoryFeature').html(htmlContent);
				}else{
					$('.thirdCategoryFeature').html('');
				}
				
				// show Dialog
				$('.viewOrEditTpl').css("display","table-cell");
				$('body').addClass('overflowhidden');
				if($('.currentCategoryContent').height() >= 350){
					$('.currentCategoryContent').css({
						"max-height":"350px",
						"overflow-y":"scroll"
					});
				}else{
					$('.currentCategoryContent').css({
						"max-height":"none",
						"overflow-y":"auto"
					});
				}
				
				$('.viewOrEditTpl').off('click','.confirmBtn').on('click', '.confirmBtn', function(e){
					var featureArr = [];
					
					var firstId = $('.selectFirstCategory').val();
					var secondId = $('.selectSecondCategory').val();
					var thirdId = $('.selectThirdCategory').val();
					
					var categoryFeatureList = $('.categoryFeatureContent').find('a.active');
					if(categoryFeatureList && categoryFeatureList.length > 0){
						$.each(categoryFeatureList, function(index, elem){
							featureArr.push($(elem).attr('data-id'));
						});
					}
					
					$.showLoading(e.currentTarget);
					$.ajax({
		                url:"/catalog/control/editFeatureTypeCategory",
		                type: 'post',
		                dataType: 'json',
		                contentType: "application/json;charset=utf-8",
		                data:JSON.stringify({
					    	categoryId : currentId,
					    	selectProductTypeID : featureArr
						}),
					    success: function(res){
					    	$.hideLoading(e.currentTarget);
						    if(res.success){
						    	$('.viewOrEditTpl').css("display","none");
						    	$('body').removeClass('overflowhidden');
						    }else{
						    	showErrorMessage(res.message);
						    }
					    },
			            error: function(){
			            	$.hideLoading(e.currentTarget);
			            }
					});
				});
				
				$('.viewOrEditTpl').off('click','.featureSpan').on('click', '.featureSpan', function(e){
					if($(e.currentTarget).hasClass('active')){
						$(e.currentTarget).removeClass('active');
					}else{
						$(e.currentTarget).addClass('active');
					}
				});
				
				$('.viewOrEditTpl').off('click','.viewOrEditContentIcon').on('click', '.viewOrEditContentIcon', function(){
					$('.viewOrEditTpl').css("display","none");
					$('body').removeClass('overflowhidden');
				});
			});
		});
	});
</script>