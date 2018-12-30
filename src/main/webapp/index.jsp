<html>
  <head>
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  </head>
  <body>
    <script>
    
    	var playerName = null;
    	
    	$(document).ready(function() {
    		
    		$("#joinButton").click(function() {
    			joinGame();
    		});
    		
    		$("#exitButton").click(function() {
    			exitGame();
    		});
    		
    		$("#textName").keyup(function() {
    			var name = $("#textName").val();
    			if (name.trim() != "")	
    				$('#joinButton').removeAttr("disabled");
    			else
    				$('#joinButton').attr("disabled", "disabled");
    		});
    
			function joinGame() {
				playerName = $("#textName").val();
				
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
				    				  for (var i = 0; i < data.playersArray.length; ++i) {
				    					  var playerContent = data.playersArray[i].nickname + ", " +
				    					  				data.playersArray[i].color;
				    					  $("#playersUnorderedList").append("<li>" + playerContent + "</li>");
				    				  }
		    			        });
			 	}, 3000);
		    	
		    	$("#joinButton").hide();   
		    	$("#textName").hide();
		    	$("#exitButton").show();
		    	$("#readyButton").show();
		    }
			
			function exitGame() {
				$.ajax({type: "POST",
					url: "./match/exit",
					data: $("#nameForm").serialize(),
					success: function(result){
			   			alert(result);
			  		}
				});
				$("#joinButton").show();   
		    	$("#textName").show();
		    	$("#exitButton").hide();
		    	$("#readyButton").hide();
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
	<button id="exitButton" hidden="hidden" type="button">Exit</button>
	<button id="readyButton" hidden="hidden" type="button">Ready</button>
  </body>
</html>
