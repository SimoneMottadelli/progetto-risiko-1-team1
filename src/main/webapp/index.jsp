<html>
<body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script>
	function prova() {
		$.ajax({dataType: "json", url: "./Contr/prova", success: function(result){
		    $("#mydiv").append(result);
		  }});
	}
</script>
<div id="mydiv"></div>
<form action="./Contr/prova">
<input type="button" onClick="prova()">
</form>
</body>
</html>
