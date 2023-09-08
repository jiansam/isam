/*展開下一層及從上層全選下層*/
$(function() {
	 $( document ).tooltip({
		 track:true
	 });
	$(".opener").click(function(){
		var x=$(this);
		toggleAdd(x);
	});
	$(".topBox").click(function(){
		var tester=$(this).prop("checked");
		$(this).siblings(".closed").find($(":checkbox")).each(function(){
			$(this).prop("checked",tester);
		});
	});
});
function toggleAdd(nowItem){
	var flag=nowItem.attr("alt");
	if(flag==="open"){
		var url = nowItem.attr("src").replace("action_add","action_remove");
		nowItem.attr({"alt":"close","src":url});
	}else{
		var url = nowItem.attr("src").replace("action_remove","action_add");
		nowItem.attr({"alt":"open","src":url});
	}
	nowItem.siblings(".closed").toggle();
}
function resettoggle($Mitem){
	$Mitem.find("div").not('.noUsed').not('.fit').each(function(){
		$(this).show();
	});
	$Mitem.find(".opener").each(function(){
		var url = $(this).attr("src").replace("action_remove","action_add");
		$(this).attr({"alt":"open","src":url});
		$(this).siblings(".closed").hide();
	});
}
function opentoggle($Mitem){
	$Mitem.find(".opener").each(function(){
		var url = $(this).attr("src").replace("action_add","action_remove");
		$(this).attr({"alt":"close","src":url});
		$(this).siblings(".closed").show();
	});
}
/*filter MItem checkboxVesion*/
$(function() {
	$(".filterMItem").focus(function(){
		resettoggle($(this).parent().parent().siblings(".MItem"));
		$(this).val("");
	});
	$(".filterMItem").keyup(function(){
		var x = $(this).val();
		var $filterMsg=$(this).parent().siblings(".filterMsg");
		var $Mitem=$(this).parent().parent().siblings(".MItem");
		$filterMsg.text("");
		resettoggle($Mitem);
		$Mitem.find(".fit").each(function(){
			$(this).removeClass("fit");
		});
		opentoggle($Mitem);
		var ty="div:contains('"+x+"')";
		var y=$Mitem.find(ty);
		if($(y).length){
			$(y).each(function(){
				$(this).addClass("fit");
			});
			$Mitem.find("div").not('.fit').each(function(){
				$(this).hide();
			});
//			console.log($Mitem.find("div:visible").length);
			if($Mitem.find("div:visible").length==0){
				$filterMsg.text('查無符合資料');
				resettoggle($Mitem);
			}
		}else{
			if(x.length){
				$filterMsg.text('查無符合資料');
				resettoggle($Mitem);
			}else{
				$filterMsg.text("");
				resettoggle($Mitem);
			}
		}
	});
});
$(function() {
	/*清空所選*/
	$(".clearMItem").click(function(){
		$(this).closest("fieldset").find(":checkbox").each(function(){
			$(this).prop("checked",false);
		});
		resettoggle($(this).closest("fieldset").find(".MItem"));
	});
	$(".clearMSelect").click(function(){
		$(this).closest("div").siblings().find(".selectBox option").each(function(){
			$(this).prop("selected",false);
		});
	});
	/*檢視所選*/
	$(".testCheckm").click(function(){
		showSelectedCheckbox($(this));
	});
	$(".checkMSelect").click(function(){
		var selectedStr="";
		$(this).closest("div").siblings().find(".selectBox option:selected").each(function(){
			selectedStr+=$(this).val()+"\r\n";
		});
		if(selectedStr.length==0){
			alert("您未勾選任何選項");
		}else{
			alert("您已選擇下面的選項：\r\n"+selectedStr);
		}
	});
	
	/*確保上下層全選、部分選擇、全不選時的checked狀態，總共就三層，沒寫遞迴，超過要改寫*/
	$(".MItem :checkbox").change(function(){
		var $close=$(this).closest("div .closed");
		if($close.length){
			var $xtopBox=$close.siblings(".topBox");
			if($xtopBox.prop("checked")){
				if(!$(this).prop("checked")){
					$xtopBox.prop("checked",false);
					if($close.parents("div .closed").length){
						$close.parents("div .closed").siblings(".topBox").prop("checked",false);
					}
				}
			}else{
				var tester=true;
				$close.find(":checkbox").each(function(){
					if($(this).prop("checked")!=tester){
						tester=false;
					}
				});
				if(tester){
					$xtopBox.prop("checked",true);
					if($close.parents("div .closed").length){
						var testertop=true;
						$close.parents("div .closed").find(":checkbox").each(function(){
							if($(this).prop("checked")!=testertop){
								testertop=false;
							}
						});
						if(testertop){
							$close.parents("div .closed").siblings(".topBox").prop("checked",true);
						}
					}
				}
			}
		}
	});
});

function showSelectedCheckbox($testCheckm){
	var chooseStr="";
	$testCheckm.closest("fieldset").find(".MItem").children("div").children(".topBox").each(function(){
		if($(this).prop("checked")){
			chooseStr+=$(this).siblings('span').text()+"\r\n";
		}else{
			var $level2=$(this).siblings('div').children("div");
			if($level2.children(".topBox").length){
				$level2.children(".topBox").each(function(){
					if($(this).prop("checked")){
						chooseStr+=$(this).siblings('span').text()+"\r\n";
					}else{
						var $level3=$(this).siblings('div').children("div");
						if($level3.children(".topBox").length){
							$level3.children(".topBox").each(function(){
								if($(this).prop("checked")){
									chooseStr+=$(this).siblings('span').text()+"\r\n";
								}
							});					
						}
						$level3.children(":checkbox:checked:not(.topBox)").each(function(){
							chooseStr+=$(this).siblings('span').text()+"\r\n";
						});
					}
				});					
			}
			$level2.children(":checkbox:checked:not(.topBox)").each(function(){
				chooseStr+=$(this).siblings('span').text()+"\r\n";
			});
		}
	});
	if(chooseStr.length==0){
		alert("您未勾選任何選項");
	}else{
		alert("您已選擇下面的選項：\r\n"+chooseStr);
	}
}
function showSelectedConfirm(){
	var infoStr="";
	$(".testCheckm").each(function(){
		var chooseStr="";
		$(this).closest("fieldset").find(".MItem").children("div").children(".topBox").each(function(){
			if($(this).prop("checked")){
				chooseStr+=$(this).siblings('span').text()+"、";
			}else{
				var $level2=$(this).siblings('div').children("div");
				if($level2.children(".topBox").length){
					$level2.children(".topBox").each(function(){
						if($(this).prop("checked")){
							chooseStr+=$(this).siblings('span').text()+"、";
						}else{
							var $level3=$(this).siblings('div').children("div");
							if($level3.children(".topBox").length){
								$level3.children(".topBox").each(function(){
									if($(this).prop("checked")){
										chooseStr+=$(this).siblings('span').text()+"、";
									}
								});					
							}
							$level3.children(":checkbox:checked:not(.topBox)").each(function(){
								chooseStr+=$(this).siblings('span').text()+"、";
							});
						}
					});					
				}
				$level2.children(":checkbox:checked:not(.topBox)").each(function(){
					chooseStr+=$(this).siblings('span').text()+"、";
				});
			}
		});
		if(chooseStr.length==0){
			chooseStr=$(this).prop("alt")+"：不拘<br/>";
		}else{
			chooseStr=$(this).prop("alt")+"："+chooseStr.substring(0, chooseStr.length-1)+"<br/>";
		}
		infoStr+=chooseStr;
	});
	return infoStr;
}

/*datatable用*/
$(function() {
	$(".selectThispage").click(function(){
		var num = $(this).closest("fieldset").find(":checkbox:not(:checked)").length;
		if(isInRange(num)){
			$(this).closest("fieldset").find(":checkbox").each(function(){
				$(this).prop("checked",true);
			});
			setSelectedCount();
		}else{
			alert("本頁全選會超過查詢限制上限("+$(".myLimitCount").text()+"家)，謝謝！");
		}
	});
	$(".unselectThispage").click(function(){
		$(this).closest("fieldset").find(":checkbox").each(function(){
			$(this).prop("checked",false);
		});
		setSelectedCount();
	});
	$(".selectedCompany").click(function(){
		var chooseItem=getSelectedName();
		alert(chooseItem);
	});
	$(".companyClear").click(function(){
		checkSelectedCount($(this));
	});
});
function checkSelectedCount($checkedItem){
	var myLimit = $(".myLimitCount").text();
	var count=getSelectedCount();
	if(count>myLimit){
		$checkedItem.prop("checked",false);
		alert("您已經超過查詢限制上限("+myLimit+"家)，謝謝！");
	}
	setSelectedCount();
}
function isInRange(num){
	var myLimit = $(".myLimitCount").text();
	var count=getSelectedCount();
	return myLimit-(count+num)>=0;
}
function setSelectedCount(){
	var count=getSelectedCount();
	$(".myCount").text(count);
}
function getSelectedCount(){
	var count=0;
	var oTable = $('#example').dataTable();
	var nNodes = oTable.fnGetNodes();
	 $('input:checkbox:checked', nNodes).each(function() {
		 count++;
	 });
	return count;
}
function getSelectedName(){
	var chooseItem="";
	var oTable = $('#example').dataTable();
	var nNodes = oTable.fnGetNodes();
	 $('input:checkbox:checked', nNodes).each(function() {
		 chooseItem+=$(this).parent().next().text()+"\r\n";
	 });
	 if(chooseItem.length==0){
		 chooseItem="您未勾選任何選項";
	 }else{
		 chooseItem="您已選擇下面的選項：\r\n"+chooseItem;
	 }
	 return chooseItem;
}
