$(document).ready(function(){
	
	// variables that hold important information of the game
	var myColor = null;
	var numberOfAvailableTanks = null;
	var map = {'territories': [], 'neighbourhood': []};
	
	
	$("#placeTanksButton").click(function() {
		placeTanks();
	});
	

	setColorAndFirstAvailableTanks();
	fillSVGMapDiv();
	requestMapLoading();
	
	
	function setColorAndFirstAvailableTanks() {
		$.getJSON('../game/playerInfo', function(result) {
			if(result.responseCode != -1) {
				myColor = result.color;
				numberOfAvailableTanks = result.availableTanks;
			}
			else
				showModalWindow(result.responseMessage);
		});
	}
	
	// fillSVGMapDiv is used to show the map return by the server on the web page.
	function fillSVGMapDiv(){
		$.getJSON('../game/mapImage', function(result) {
			if(result.responseCode != -1) {
				$("#mapDiv").html(result.mapSVG);
				addMouseoverEventToTerritories();
			}
			else
				showModalWindow(data.responseMessage);
		});
	}
	
	// requestMapLoading creates a link to connect the client to the server, so that the
	// server can send map updates using Server Send Event mechanism.
	// It also starts the initial tank placement phase
	function requestMapLoading() {
		var initialTankPlacementPhaseStarted = false;
		var source = new EventSource('../game/map');
		source.onmessage = function(event) {
			map.territories = JSON.parse(event.data).territories;
			map.neighbourhood = JSON.parse(event.data).neighbourhood;
			if (!initialTankPlacementPhaseStarted && myColor != null) {
				initialTankPlacementPhaseStarted = true;
				updateMyTerritoriesSelect();
				startTankPlacementPhase();
			}	
			updateMap();
		}
	}
	
	function updateMap() {
		$("path.country").each(function() {
			for (var i = 0; i < map.territories.length; ++i) {
				if (map.territories[i].name == $(this).attr("id")) {
					$(this).removeClass().attr("class", "country").addClass(map.territories[i].owner);
					// modifica contatore carri sul territorio
				}
			}
		});
	}
	
	function updateMyTerritoriesSelect() {
		$("#myTerritoriesSelect").html("");
		for (var i = 0; i < map.territories.length; ++i) {
			if (map.territories[i].owner == myColor)
				$("#myTerritoriesSelect").append('<option value="' + map.territories[i].name + '">' 
					+ map.territories[i].name + "</option>");
		}
	}
	
	function startTankPlacementPhase() {
		$("#availableTanksLabel").html(numberOfAvailableTanks);
		$("#placeTanksDiv").show();
	}

	
	function getPlayerInfo() {
		$.getJSON('../game/playerInfo', function(data) {
			if(data.responseCode != -1) {
				
			}
			else
				showModalWindow(data.responseMessage);
		});
	}
	
	
	// function used to place tanks to a territory
	function placeTanks() {
		var territory = $("#myTerritoriesSelect").val();
		var numOfTanks = parseInt($('#howMany').val(), 10);
		$('#howMany').val(0);
		$.ajax({
			type : 'POST',
			url : '../game/initialTanksPlacement',
			contentType: 'application/json',
			dataType: 'json',
			data: JSON.stringify(placeTanksJson(territory, numOfTanks)), 
			success: function(data) {
				if (data.responseCode == -1)
					showModalWindow(data.responseMessage);
				else {
					numberOfAvailableTanks -= numOfTanks;
					$("#availableTanksLabel").html(numberOfAvailableTanks);
				}
			}
		});
	}
	
	// helper function that return json object
	function placeTanksJson(territory, numOfTanks) {
		return {'where' : territory, 'numOfTanks' : numOfTanks};
	}
	
	// used to get information of turn status from the server, invoked only one time
	// sse emitter was used by the server to send the json response
	function playerTurnRequest() {
		var source = new EventSource('../game/turnStatus');
		source.onmessage = function(event) {
			if(!JSON.parse(event.data).hasOwnProperty('currentPlayer')) {
				if(availableTanks != 0) {
					var players = JSON.parse(event.data).players;
					var i = 0;
					while(i < players.length && availableTanks != 0) {
						if(players[i].color == myColor) {
							availableTanks = players[i].availableTanks
							if(availableTanks == 0) {
								$("#gameDiv").attr("class", "hidden");
								showModalWindow("all tanks placed, wait that all players place their tanks");
								return;
							}
						}
						++i;
					}
				}
			}
			else {
				var currentPlayer = JSON.parse(event.data).currentPlayer;
				if(myColor != currentPlayer)
					$("#gameDiv").attr("class", "hidden");
			}
		}
	}
	
	function addMouseoverEventToTerritories() {
		$("path.country").each(function() {
			$(this).hover(
					function() {
						$("#territoryNameLabel").html($(this).attr("id"));
					}, 
					function() {
						$("#territoryNameLabel").html("");
					});
		});
	}
	
	function showModalWindow(message) {
		$("div.modal-body").html("<h2>" + message + "</h2>");
		$("#modalWindow").css("display", "block");
	}
	
	$("span.close").click(function() {
		$("#modalWindow").css("display", "none");
	});
	
});