<html>
  <head>
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  </head>
  <body>
    <script>
    	
    	$(document).ready(function() {
    		
    		$("#joinButton").click(function() {
    			joinGame();
    		});
    		
    		$("#textName").keyup(function() {
    			var name = $("#textName").val();
    			console.log(name);
    			if (name != "")	
    				$('#joinButton').removeAttr("disabled");
    			else
    				$('#joinButton').attr("disabled", "disabled");
    		});
    
			function joinGame() {
				var name = $("#textName").val();
				
				$.ajax({type: "POST",
						url: "./match/join",
						data: $("#nameForm").serialize(),
						success: function(result){
				   			alert(result);
				  		}
				});
				
		    	setInterval(function() {
				    			$.get( "./match/players", function(data) {
				    				  //clear the unordered list
				    				  $("li").remove();
				    				  var playersData = data;
				    				  for (var i = 0; i < playersData.playersArray.length; ++i) {
				    					  var playerContent = playersData.playersArray[i].nickname + ", " +
				    					  				playersData.playersArray[i].color;
				    					  $("#playersUnorderedList").append("<li>" + playerContent + "</li>");
				    				  }
		    			        });
			 	}, 3000);
		    }
		});
	</script>
	<b>Players who joined the game:</b>
	<hr>
	<ul id="playersUnorderedList">
	</ul>
	<hr>
	<br>
	<form id="nameForm">
		<input id="textName" name="name" type="text" placeholder="Enter your name here...">
	</form>
	<button id="joinButton" disabled="disabled" type="button">Join game</button>

  </body>
</html>
