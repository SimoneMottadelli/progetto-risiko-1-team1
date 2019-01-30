
$(document).ready(
		function() {
			
			// --------------------
			// + GLOBAL VARIABLES +
			// --------------------

			var source = null; // used to open Server Sent Event connection
			var warningAlreadyDisplayed = false;
	
			
			
			// ----------
			// + EVENTS +
			// ----------
			$("#joinButton").click(function() {
				joinGame();
			});
			
			$("span.close").click(function() {
				$("#modalWindow").css("display", "none");
			});
			
			$("#modifyButton").click(function() {
				applyChanges();
			});
			
			$("#customRadio").click(function() {
				$("#difficultyDiv").attr("class", "hidden");
				$("#mapCustomConfigDiv").attr("class", "show");
			});
			
			$("#defaultRadio").click(function() {
				$("#difficultyDiv").attr("class", "shown");
				$("#mapCustomConfigDiv").attr("class", "hidden");
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
			
			$(window).on("beforeunload", function(){  
		        exitGame();  
		    }); 
			
			window.addEventListener("beforeunload", function () {
				 exitGame();
			});

			$("#textName").keyup(function() {
				var name = $("#textName").val();
				if (name.trim() != "")
					$('#joinButton').removeAttr("disabled");
				else
					$('#joinButton').attr("disabled", "disabled");
			});
			
			
			// ------------------------------
			// + FUNCTIONS CALLED BY EVENTS +
			// ------------------------------
			
			// this function asks the server to set the player ready
			function setReady() {
				$.get("./match/ready", function(result) {
					showModalWindow(result.responseMessage);
						$("#readyButton").attr("class", "hidden");
						$("#notReadyButton").attr("class", "shown");
					}
				);
			}
			
			function setNotReady() {
				$.get("./match/notready", function(result) {
					$("#notReadyButton").attr("class", "hidden");
					$("#readyButton").attr("class", "shown");
				});
				warningAlreadyDisplayed = false;
			}
			
			// this function makes the server apply changes about the player's preferences 
			// about the map and about the game configuration.
			function applyChanges() {
				var isMapDefault = $('input[name=mapConfigRadio]:checked', '#mapForm').val() == "default";
				var objectiveType = $('input[name=modalityConfigRadio]:checked', '#modalityForm').val();
				var jsonRequestPayload = "{'objective': '" + objectiveType + "','difficulty': '";
				if (isMapDefault) {
					jsonRequestPayload += $("#difficultySelect").val() + "'}";
				} else {
					jsonRequestPayload += "custom', " + $("#jsonTextArea").val() + ", ";
					jsonRequestPayload += "'mapSVG': '" + $("#svgTextArea").val() + "'}";
				}
		
				$.ajax({
					type : "POST",
					url : "./match/gameConfig",
					contentType: 'application/json',
		            dataType: 'json',
					data: jsonRequestPayload,
					success: function(result) {
						showModalWindow(result.responseMessage);
					}
				});
			}		
			
			// this function is called whenever the player joins the match
			function joinGame() {
				var matchStarted = true;
				$.ajax({
					type : "POST",
					url : "./match/join",
					data : $("#nameForm").serialize(),
					success : function(result) {
						showModalWindow(result.responseMessage);
						if (result.responseCode != -1) {
							matchStarted = false;
							source = new EventSource("./match/info");
							source.onmessage = function(evt) {
								var playersArray = JSON.parse(evt.data).players;
								var isMapReady = JSON.parse(evt.data).mapReady;
								var gameStarted = JSON.parse(evt.data).gameStarted;
								if (gameStarted) {
									source.close();
									location.replace(JSON.parse(evt.data).gamePage);
								}
								else if (!warningAlreadyDisplayed && isEveryoneReady(playersArray) && !isMapReady){
									showModalWindow("Everyone is ready but the map hasn't been created yet");
									warningAlreadyDisplayed = true;
								}
								else if(!warningAlreadyDisplayed && isEveryoneReady(playersArray) && !areThereTwoPlayers(playersArray)) {
									showModalWindow("Waiting for another player to join...");
									warningAlreadyDisplayed = true;
								}
								refreshPlayersTable(playersArray);
								updateMapStatus(JSON.parse(evt.data).mapReady);
							};	
						}
						if (!matchStarted) {
							$("#joinButton").attr("class", "hidden");
							$("#exitButton").attr("class", "shown");
							$("#readyButton").attr("class", "shown");
							$("#textName").attr("class", "hidden");
							$("#playersTable").attr("class", "shown");
							$("#rightContentDiv").attr("class", "shown");
						}
					}
				});				
			}
			
			// this function is called whenever the player exits from the match
			function exitGame() {
				$.get("./match/exit");
				if (source != null)
					source.close();
				$("tr").remove(); //clear players table
				$("#joinButton").attr("class", "shown");
				$("#textName").attr("class", "shown");
				$("#exitButton").attr("class", "hidden");
				$("#readyButton").attr("class", "hidden");
				$("#notReadyButton").attr("class", "hidden");
				$("#readyButton").attr("class", "hidden");
				$("#playersTable").attr("class", "hidden");
				$("#rightContentDiv").attr("class", "hidden");
			}
			
			
			// --------------------
			// + HELPER FUNCTIONS +
			// --------------------
			
			// this function is used to update the player's ready status
			function updateMapStatus(ready) {
				if (ready == true)
					$("#mapStatusLabel").html(" ready");
				else
					$("#mapStatusLabel").html(" not ready");
			}
			
			// function used to communicate important messages to the player
			function showModalWindow(message) {
				$("div.modal-body").html("<h2>" + message + "</h2>");
				$("#modalWindow").css("display", "block");
			}
			
			// this function checks whether every player is ready or not
			function isEveryoneReady(playersArray) {
				var ready = true;
				for (var i = 0; i < playersArray.length; ++i)
					if (!playersArray[i].ready)
						ready = false;
				return ready;
			}
			
			// this function checks whether there are more than two players or not
			function areThereTwoPlayers(playersArray) {
				return playersArray.length >= 2;
			}
			
			// this function refreshes the table with the most recent changes
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
		});