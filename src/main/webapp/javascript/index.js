var source = null; // used to open Server Sent Event connection
var warningAlreadyDisplayed = false;

$(document).ready(
		function() {

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

			$("#textName").keyup(function() {
				var name = $("#textName").val();
				if (name.trim() != "")
					$('#joinButton').removeAttr("disabled");
				else
					$('#joinButton').attr("disabled", "disabled");
			});

			function setReady() {
				$.get("./match/ready", function(result) {
					showModalWindow(result.responseMessage);
						$("#readyButton").attr("class", "hidden");
						$("#notReadyButton").attr("class", "shown");
					}
				);
			}
			
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
				
				console.log(jsonRequestPayload);

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

			function setNotReady() {
				$.get("./match/notready", function(result) {
					$("#notReadyButton").attr("class", "hidden");
					$("#readyButton").attr("class", "shown");
				});
				warningAlreadyDisplayed = false;
			}

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
								if (isEveryoneReady(playersArray) && isMapReady && areThereTwoPlayers(playersArray)) {
									source.close();
									var serverIp = JSON.parse(evt.data).serverIp;
									var serverPort = JSON.parse(evt.data).serverPort
									location.replace("http://" + serverIp + ":" + serverPort + "/drisk/pages/game.html");
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
			
			function updateMapStatus(ready) {
				if (ready == true)
					$("#mapStatusLabel").html(" ready");
				else
					$("#mapStatusLabel").html(" not ready");
			}
			
			function showModalWindow(message) {
				$("div.modal-body").html("<h2>" + message + "</h2>");
				$("#modalWindow").css("display", "block");
			}
			
			function isEveryoneReady(playersArray) {
				var ready = true;
				for (var i = 0; i < playersArray.length; ++i)
					if (!playersArray[i].ready)
						ready = false;
				return ready;
			}
			
			function areThereTwoPlayers(playersArray) {
				return playersArray.length >= 2;
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
		});