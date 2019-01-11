$(document).ready(function() {
	
	// +++++++++++++++ VARIABLES +++++++++++++++
	var myColor = null;
	var numberOfAvailableTanks = null;
	var map = {'territories': [], 'neighbourhood': [], 'membership': [], 'continents': []};
	var cards = [];
	var jsonResponsePlayPhase = null;
	
	init();	
	
	// this function initializes the web page in order to start the game
	function init() {
		initMap();
		initPlayersInfo();
	}
	
	// this function initializes the variable MAP, visualizes the map on the web page and
	// starts the initial tank placement phase.
	function initMap() {
		
		$.ajax({
			type : 'GET',
			url : '../game/map',
			success: function(result) {
				if(result.responseCode != -1) {
					map.territories = JSON.parse(result.responseMessage).territories;
					map.neighbourhood = JSON.parse(result.responseMessage).neighbourhood;
					map.membership = JSON.parse(result.responseMessage).membership;
					map.continents = JSON.parse(result.responseMessage).continents;
					fillSVGMapDiv(JSON.parse(result.responseMessage).mapSVG);
					startRequestingMapLoading();
					updateMyTerritoriesSelect();
					startTankPlacementPhase();
				}
				else
					showModalWindow(result.responseMessage);
			}
		});
	}
	
	function initPlayersInfo() {
		$.ajax({
			type : 'GET',
			url : '../game/playerInfo',
			success: function(result) {
				if(result.responseCode != -1) {
					myColor = result.color;
					$("#myColorLabel").html(myColor);
					numberOfAvailableTanks = result.availableTanks;
					$("#availableTanksLabel").html(numberOfAvailableTanks);
					$("#missionCardLabel").html(result.missionCard.mission);
				}
				else
					showModalWindow(result.responseMessage);
			}
		});
	}
	
	// fillSVGMapDiv is used to show the map return by the server on the web page.
	function fillSVGMapDiv(mapSVG){
		$("#mapDiv").html(mapSVG);
		updateSVGMap();
		addMouseoverEventToTerritories();
	}
	
	
	function updateMyTerritoriesSelect() {
		$("#wherePlacementSelect").html("");
		for (var i = 0; i < map.territories.length; ++i) {
			if (map.territories[i].owner == myColor)
				$("#wherePlacementSelect").append('<option value="' + map.territories[i].name + '">' 
					+ map.territories[i].name + "</option>");
		}
	}
	
	function startTankPlacementPhase() {
		$("#availableTanksLabel").html(numberOfAvailableTanks);
		$("#tankPlacementPhaseDiv").show();
	}
	
	// this function colors the map and updates tanks numbers
	function updateSVGMap() {
		$("path.country").each(function() {
			for (var i = 0; i < map.territories.length; ++i) {
				if (map.territories[i].name == $(this).attr("id")) {
					$(this).removeClass().attr("class", "country").addClass(map.territories[i].owner);
					$("#" + map.territories[i].name + "_text").text(map.territories[i].numberOfTanks);
				}
			}
		});
	}
	
	// requestMapLoading creates a link to connect the client to the server, so that the
	// server can send map updates using Server Send Event mechanism.
	function startRequestingMapLoading() {
		var initialTankPlacementPhaseStarted = false;
		var source = new EventSource('../game/territories');
		source.onmessage = function(event) {
			map.territories = JSON.parse(event.data).territories;
			initialTankPlacementPhaseStarted = true;			
			updateSVGMap();
		}
	}
	
	// function used to place tanks to a territory
	function placeInitialTanks() {
		var territory = $("#wherePlacementSelect").val();
		var numOfTanks = parseInt($('#howManyPlacement').val(), 10);
		$('#howManyPlacement').val(0);
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
					if (numberOfAvailableTanks <= 0) {
						$("#tankPlacementPhaseDiv").hide();
						playerTurnRequest();
						$("#placeTanksButton").unbind();
						$("#placeTanksButton").click(function() {
							placeTanks();
						});
					}
				}
			}
		});
	}
	
	function placeTanks() {
		var territory = $("#wherePlacementSelect").val();
		var numOfTanks = parseInt($('#howManyPlacement').val(), 10);
		$('#howManyPlacement').val(0);
		
		$.ajax({
			type : 'POST',
			url : '../game/playPhase',
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
		var allTanksPlacedWarningShown = false;
		var source = new EventSource('../game/turnStatus');
		source.onmessage = function(event) {
			console.log(JSON.parse(event.data));
			if(!JSON.parse(event.data).hasOwnProperty('currentPlayer')) {
				if (!allTanksPlacedWarningShown) {
					allTanksPlacedWarningShown = true;
					showModalWindow("All tanks have been placed! Wait for other players to finish this phase too");
				}
			}
			else {
				var currentPlayer = JSON.parse(event.data).currentPlayer;
				$("#playersTurnLabel").html(currentPlayer);
				if(myColor != currentPlayer) 
					$("#gameDiv").hide();
				else {
					var phaseId = JSON.parse(event.data).currentPhaseId;
					playPhase(phaseId);
				}
			}
		}
	}
	
	function playPhase(phaseId) {
		switch (phaseId) {
		case 1:
			$("#tankMovementPhaseDiv").hide();
			$("#useTrisButton").show();
			showCardsToSelect();
			break;
		case 2:
			$("#useTrisButton").hide();
			$("#tankPlacementPhaseDiv").show();
			break;
		case 3:
			$("#tankPlacementPhaseDiv").hide();
			$("#attackPhasePhaseDiv").show();
			break;
		case 4:
			$("#attackPhasePhaseDiv").hide();
			$("#tankMovementPhaseDiv").show();
			break;
		}
	}
	
	function showCardsToSelect() {
		for (var i = 0; i < cards.length; ++i) {
			$("#cardsDiv").append('<input type="checkbox" id="' + cards[i].territory + '" name="'  + cards[i].territory + '" >');
		}
	}
	
	function showModalWindow(message) {
		$("div.modal-body").html("<h2>" + message + "</h2>");
		$("#modalWindow").css("display", "block");
	}
	
	
	function sendPhaseData(jsonRequestObject) {
		$.ajax({
			type : 'POST',
			url : '../game/playPhase',
			contentType: 'application/json',
			dataType: 'json',
			data: JSON.stringify(jsonRequestObject), 
			success: function(data) {
				if (data.responseCode == -1)
					showModalWindow(data.responseMessage);
				else {
					numberOfAvailableTanks = JSON.parse(data.responseMessage).availableTanks;
					updateCards(JSON.parse(data.responseMessage).cards);
				}
			}
		});
	}
	
	// this function is used to updated the gloabal variable CARDS, which is an array of territory names.
	function updateCards(cardsArray) {
		for (var i = 0; i < cardsArray.length; ++i)
			cards[i] = {"territory": cardsArray[i].territory, "symbol": cardsArray[i].symbol};
	}
	
	
	// +++++++++++++++ EVENTS +++++++++++++++
	
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
	
	$("#nextPhaseButton").click(function() {
		$.getJSON('../game/nextPhase', function(result) {
			if (result.responseCode == -1)
				showModalWindow(result.responseMessage);
		});
	});
	
	$("#useTrisButton").click(function() {
		var selectedCards = [];
		var i = 0;
		$("input:checkbox").each(function () {
			if (this.checked) 
				selectedCards[i++] = $(this).attr("id");
		  });
		var jsonRequestObject = {"cards": selectedCards};
		sendPhaseData(jsonRequestObject);
	});
	
	$("span.close").click(function() {
		$("#modalWindow").css("display", "none");
	});
	
	$("#placeTanksButton").click(function() {
		placeInitialTanks();
	});
	
});