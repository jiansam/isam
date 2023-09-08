function deleteInterviewFile(o, id){
	if(confirm("確定刪除這個檔案？（刪除的檔案無法回復）")){
		$.post("download.jsp", {identifier: id, action: 'delete'});
		var tr = $(o).closest('tr');
		tr.remove();
	}else{
		return false;
	}		
}