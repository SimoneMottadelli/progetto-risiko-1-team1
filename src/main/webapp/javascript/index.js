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
						alert(result);
						$("#notReadyButton").hide();
						$("#readyButton").show();
					}
				});
			}

			function joinGame() {

				$.ajax({
					type : "POST",
					url : "./match/join",
					data : $("#nameForm").serialize(),
					success : function(result) {
						alert(result);
					}
				});

				source = new EventSource("./match/players");
				source.onmessage = function(evt) {
					var objResponse = JSON.parse(evt.data);
					$("tr").remove();// clear the table
					$("#playersTable").append("<tr><th>Name</th><th>Color</th></tr>");
					for (var i = 0; i < objResponse.playersArray.length; ++i) {
						$("#playersTable").append(
								"<tr><td>" + objResponse.playersArray[i].nickname + "</td>" +
									"<td>" + objResponse.playersArray[i].color + "</td></tr>"
						);
					}
				};

				$("#joinButton").hide();
				$("#textName").hide();
				$("#exitButton").show();
				$("#readyButton").show();
				$("#playersDiv").show();
			}

			function exitGame() {
				$.ajax({
					type : "POST",
					url : "./match/exit",
					data : $("#nameForm").serialize(),
					success : function(result) {
						alert(result);
						source.close();
						$("li").remove();// clear the unordered list
						$("#joinButton").show();
						$("#textName").show();
						$("#exitButton").hide();
						$("#readyButton").hide();
						$("#notReadyButton").hide();
						$("#readyButton").hide();
						$("#playersDiv").hide();
					}
				});
			}
		});