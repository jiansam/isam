$(function(){
	setReadOnly();
	/*改善財務*/
		$("#addRow").click(function(){
			if(checkAddRowInput()){
				$("#insertShow").show();
				addRow($(this).parents('tr'));
				clearRowInput();
				setReadOnly();
				addClassNewNextFocus();
			}
		});
		/*國內相對投資*/
		$("#addRowCalculate").click(function(){
			if(checkAddRowInput()){
				$("#insertShow").show();
				addRow($(this).parents('tr'));
				clearRowInput();
				sumRowByTRsType();
				setReadOnly();
				addClassNewNextFocus();
			}
		});
		/*資金回饋*/
		$("#addRowCount").click(function(){
			if(checkAddRowInput()){
				$("#insertShow").show();
				addRowForCapital($(this).parents('tr'));
				clearRowInput();
				sumRowRs03();
				setReadOnly();
				addClassNewNextFocus();
			}
		});
		$(".insertTable input[name^='aRsType01']").blur(function(){
			updateTRsTypeSumRS($(this));
		});
		$(".insertTable input[name='aRsType0301']").blur(function(){
			sumRowRs03();
			updateAccRS0302();
		});
		$("select[name='from'],select[name='to']").change(function(){
			var name = $(this).prop("name");
			if($(".insertTable input[name='atypeYear']").length>0&&checkTableYearOnChange()){
				alert("分年資料須維持在期間之內，請重新選取年度");
				var thisVal=$(".insertTable input[name='atypeYear']:first").val();
				var fromVal=$(".insertTable input[name='atypeYear']:last").val();
				if(name==="from"){
					setYearOption(name,thisVal);
					changeYearEditOption(thisVal);
				}else{
					setYearOption(name,thisVal);
					changeYearEditOption(fromVal);
				}
			}
		});
	});
	function addRowForCapital($aTr){
		var rs0302='<td><input type="text" name="aRsType0302" style="width: 95%;" class="trRight" value=""/></td>';
		var x = '<td><input type="button" onclick="delRowRs03Input(this);" class="btn_class_opener" style="color: #777777;text-align: center;font-size: 12px;" value="刪除" /></td>';
		var tr= $("<tr style='text-align: center;'></tr>").append(x).append($aTr.children("td:has(input:text)").clone()).append(rs0302);
		var year = $aTr.find("input[name='atypeYear']").val();
		if($(".insertTable").find("tr").length===1){
			$(".insertTable").append(tr);
		}else if(year===$("select[name='from']").val()){
			$(".insertTable tr:first").after(tr);
		}else if(year===$("select[name='to']").val()){
			$(".insertTable tr:last").after(tr);
		}else{
			var ary= new Array();
			$(".insertTable input[name='atypeYear']").each(function(){
				ary.push(parseInt($(this).val(),10));
			});
			ary.push(year);
			ary.sort();
			var num = parseInt($.inArray(year,ary),10);
			$(".insertTable tr:eq("+num+")").after(tr);
		}
		updateAccRS0302();
		$(".insertTable input[name='aRsType0301']").blur(function(){
			sumRowRs03();
			updateAccRS0302();
		});
	}
	function addRow($aTr){
		var x = '<td><input type="button" onclick="delRowInput(this);" class="btn_class_opener" style="color: #777777;text-align: center;font-size: 12px;" value="刪除" /></td>';
		var tr= $("<tr style='text-align: center;'></tr>").append(x).append($aTr.children("td:has(input:text)").clone());
		var year = $aTr.find("input[name='atypeYear']").val();
		if($(".insertTable").find("tr").length===1){
			$(".insertTable").append(tr);
		}else if(year===$("select[name='from']").val()){
			$(".insertTable tr:first").after(tr);
		}else if(year===$("select[name='to']").val()){
			$(".insertTable tr:last").after(tr);
		}else{
			var ary= new Array();
			$(".insertTable input[name='atypeYear']").each(function(){
				ary.push(parseInt($(this).val(),10));
			});
			ary.push(year);
			ary.sort();
			var num = parseInt($.inArray(year,ary),10);
			$(".insertTable tr:eq("+num+")").after(tr);
		}
		$(".insertTable input[name^='aRsType01']").blur(function(){
			updateTRsTypeSumRS($(this));
		});
	}
	function getAccRS0302ByYear(year){
		var countTmp=0;
		$(".insertTable input[name='aRsType0301']").each(function(){
			if($(this).parents("tr").find("input[name='atypeYear']").val()<=year){
				countTmp+=parseFloat(getRemoveCommaVal($(this).val()),10);
			}
		});
		countTmp = getInsertComma(countTmp);
		return countTmp;
	}
	function updateAccRS0302(){
		var countTmp=0;
		$(".insertTable input[name='aRsType0301']").each(function(){
			var year =$(this).parents("tr").find("input[name='atypeYear']").val();
			countTmp=getAccRS0302ByYear(year);
			$(this).parents("tr").find("input[name='aRsType0302']").val(countTmp);
		});
	}
	function sumRowRs03(){
		var sumValue=sumRowByTypeCode("0301");
		if(sumValue===0){
			$(".formProj input[name='RsType03']").val("");
		}else{
			$(".formProj input[name='RsType03']").val(getInsertComma(sumValue));
		}
	}
	function sumRowByTRsType(){
		$(".formProj input[name^='TRsType']").each(function(i){
			var typeCode=$(this).prop("name").replace("TRsType","");
			var sumValue=sumRowByTypeCode(typeCode);
			if(sumValue===0){
				$(this).val("");
			}else{
				$(this).val(getInsertComma(sumValue));
			}
		});
	}
	function sumRowByTypeCode(typeCode){
		var sumValue=0;
		$(".insertTable input[name='aRsType"+typeCode+"']").each(function(i){
			var aRsTypeVal=$(this).val();
			if(aRsTypeVal.length>0){
				sumValue+=parseFloat(getRemoveCommaVal($(this).val()),10);
			}
		});
		return sumValue;
	}
	function updateTRsTypeSumRS($input){
		var typeCode=$input.prop("name").replace("aRsType","");
		var sumValue=sumRowByTypeCode(typeCode);
		var $sumRsInput=$(".formProj input[name='TRsType"+typeCode+"']");
		if(sumValue===0){
			$sumRsInput.val("");
		}else{
			$sumRsInput.val(getInsertComma(sumValue));
		}
	}
	function addClassNewNextFocus(){
		$(".insertTable input[name^='aRsType']").not(".insertTable input[name='aRsType0302']").each(function(i){
			$(this).addClass("NewNextFocus");
		});
		setNewAddFormatInput(".NewNextFocus");
	}
	function setNewAddFormatInput(className){
		$(className).formatInput();
		$(className).focus(function(event){
			var x = $(this).getCursorPosition()-1;
			var result=getRemoveCommaVal($(this).val());
			$(this).val(result);
			$(this).setCursorPosition(x);
		});
		$(className).keyup(function(event){
			if(event.keyCode===13){
				if($(this).parent().next().has(className).length){
					$(this).parent().next().children(className).focus();
				}else{
					$(this).blur();
				}
			}
		});
	}
	function setReadOnly(){
		$(".insertTable input[name='atypeYear']").each(function(i){
			$(this).prop("readonly",true);
		});
		$(".insertTable input[name='atypeYear']").focus(function(){
			alert("年度不可修改，如需修改請先刪除本筆資料後，再重新新增，謝謝！");
			$(this).blur();
		});
		$(".insertTable input[name='aRsType0302']").focus(function(){
			alert("累計承諾金額由系統產出，不可修改");
			$(this).blur();
		});
	}
	function checkTableYear(year){
		var flag=false;
		$(".insertTable input[name='atypeYear']").each(function(i){
			if(year===$(this).val()){
				flag=true;
				return false;
			}
		});
		return flag;
	}
	function checkTableYearOnChange(){
		var result=false;
		var fromVal=parseInt($("select[name='from']").val(),10);
		var toVal=parseInt($("select[name='to']").val(),10);
		$(".insertTable input[name='atypeYear']").each(function(i){
			var ckVal=parseInt($(this).val(),10);
			if(ckVal>toVal||ckVal<fromVal){
				result=true;
				return false;
			}
		});
		return result;
	}
	function deleteRowInput($input){
		$(".insertTable tr").has($input).remove();
		if($(".insertTable").find("tr").length===1){
			$("#insertShow").hide();
		}
	}
	function delRowInput($input){
		$(".insertTable tr").has($input).remove();
		if($(".insertTable").find("tr").length===1){
			$("#insertShow").hide();
		}
		sumRowByTRsType();
	}
	function delRowRs03Input($input){
		$(".insertTable tr").has($input).remove();
		if($(".insertTable").find("tr").length===1){
			$("#insertShow").hide();
		}
		sumRowRs03();
		updateAccRS0302();
	}
	function clearRowInput(){
		$(".addtable").find("input:text").each(function(i){
			$(this).val("");
		});
	}
	function checkAddRowInput(){
		var textCheck="";
		var isOK=true;
		$(".addtable").find("input:text").each(function(i){
			var thisVal=$.trim($(this).val()).replace(/,/g, "");
			if(i===0&&thisVal.length===0){
				alert("年度不得為空值");
				isOK=false;
				return false;
			}else if(i===0){
				if(checkTableYear(thisVal)){
					alert("年度不可重複");
					isOK=false;
					return false;
				}
				var convertInt=parseInt(thisVal,10);
				if(convertInt<parseInt($("select[name='from']").val(),10)||convertInt>parseInt($("select[name='to']").val(),10)){
					alert("年度必須在所選期間內");
					isOK=false;
					return false;
				}
			}else if(i>0){
				if(isNaN(thisVal)){
					alert("請填寫數值");
					textCheck+="no";
					$(this).val("").focus();
					isOK=false;
					return false;
				}else{
					textCheck+=thisVal;
				}
			}
		});
		if(textCheck.length===0&&isOK===true){
			alert("請至少填寫一個分年欄位");
			isOK=false;
			return false;
		}
		return isOK;
	}