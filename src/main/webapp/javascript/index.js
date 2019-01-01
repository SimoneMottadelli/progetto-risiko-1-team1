var source = null; // used to open Server Sent Event connection

$(document).ready(
		function() {

			$("#joinButton").click(function() {
				joinGame();
			});

			$("#exitButton").click(function() {
				exitGame();
			});

			$("#readyButton").click(function() {
				setReady();
			});

			$("#notReadyButton").click(function() {
				setNotReady();
			});

			$("#textName").keyup(function() {
				var name = $("#textName").val();
				if (name.trim() != "")
					$('#joinButton').removeAttr("disabled");
				else
					$('#joinButton').attr("disabled", "disabled");
			});

			function setReady() {
				$.ajax({
					type : "POST",
					url : "./match/ready",
					data : $("#nameForm").serialize(),
					success : function(result) {
						alert(result);
						$("#readyButton").hide();
						$("#notReadyButton").show();
					}
				});
			}

			function setNotReady() {
				$.ajax({
					type : "POST",
					url : "./match/notready",
					data : $("#nameForm").serialize(),
					success : function(result) {
						$("#notReadyButton").hide();
						$("#readyButton").show();
					}
				});
			}

			function joinGame() {
				var matchStarted = true;
				$.ajax({
					type : "POST",
					url : "./match/join",
					data : $("#nameForm").serialize(),
					success : function(result) {
						alert(result);
						if (result != "The match has already started!") {
							matchStarted = false;
							source = new EventSource("./match/players");
							source.onmessage = function(evt) {
								refreshPlayersTable(JSON.parse(evt.data).playersArray);
							};	
						}
						if (!matchStarted) {
							$("#joinButton").hide();
							$("#textName").hide();
							$("#exitButton").show();
							$("#readyButton").show();
							$("#playersDiv").show();
						}
					}
				});				
			}
			
			function refreshPlayersTable(playersArray) {
				$("tr").remove();
				$("#playersTable").append("<tr><th>Name</th><th>Color</th><th>Ready</th></tr>");
				for (var i = 0; i < playersArray.length; ++i) {
					$("#playersTable").append(
							"<tr><td>" + playersArray[i].nickname + "</td>" +
							"<td>" + playersArray[i].color + "</td>" +
							"<td>" + playersArray[i].ready + "</td></tr>"
					);
				}
			}

			function exitGame() {
				$.ajax({
					type : "POST",
					url : "./match/exit",
					data : $("#nameForm").serialize(),
				});
				if (source != null)
					source.close();
				$("tr").remove(); //clear players table
				$("#joinButton").show();
				$("#textName").show();
				$("#exitButton").hide();
				$("#readyButton").hide();
				$("#notReadyButton").hide();
				$("#readyButton").hide();
				$("#playersDiv").hide();
			}
		});