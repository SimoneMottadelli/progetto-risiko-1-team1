$(document).ready(function() {
	
	// +++++++++++++++ VARIABLES +++++++++++++++
	var myColor = null;
	var numberOfAvailableTanks = null;
	var map = {'territories': [], 'neighbourhood': [], 'membership': [], 'continents': []};
	var cards = [];
	var jsonResponsePlayPhase = null;
	
	var attackPhaseAlreadyInitialized = false;
	var movementPhaseAlreadyInitialized = false;
	
	init();	
	
	// this function initializes the web page in order to start the game
	function init() {
		initMap();
		getPlayerInfo();
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
					startTankPlacementPhase();
					updateMyTerritoriesSelect();
				}
				else
					showModalWindow(result.responseMessage);
			}
		});
	}
	
	function getPlayerInfo() {
		$.ajax({
			type : 'GET',
			url : '../game/playerInfo',
			success: function(result) {
				if(result.responseCode != -1) {
					result = JSON.parse(result.responseMessage);
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
			map.territories = JSON.parse(event.data);
			initialTankPlacementPhaseStarted = true;			
			updateSVGMap();
		}
	}
	
	// function used to place tanks to a territory
	function placeInitialTanks() {
		var territory = $("#wherePlacementSelect").val();
		if (territory == undefined) {
			updateMyTerritoriesSelect();
			showModalWindow("Something wrong happened... try again!");
			return;
		}
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
	
	function attack() {
		attackPhaseAlreadyInitialized = false;
		var fromTerritory = $("#fromAttackSelect").val();
		var toTerritory = $("#toAttackSelect").val();
		var numOfTanks = parseInt($('#howManyAttack').val(), 10);
		$('#howManyAttack').val(0);
		
		if (toTerritory == undefined) {
			showModalWindow("You have to select a territory to attack!");
			return;
		}
		
		$.ajax({
			type : 'POST',
			url : '../game/playPhase',
			contentType: 'application/json',
			dataType: 'json',
			data: JSON.stringify(attackJson(fromTerritory, toTerritory, numOfTanks)), 
			success: function(data) {
				if (data.responseCode == -1)
					showModalWindow(data.responseMessage);
			}
		});
	}
	
	function moveTanks() {
		var fromTerritory = $("#fromMovementSelect").val();
		var toTerritory = $("#toMovementSelect").val();
		var numOfTanks = parseInt($('#howManyMovement').val(), 10);
		$('#howManyMovement').val(0);
		
		if (toTerritory == undefined) {
			showModalWindow("Select a valid territory to move your tanks to!");
			return;
		}
		
		$.ajax({
			type : 'POST',
			url : '../game/playPhase',
			contentType: 'application/json',
			dataType: 'json',
			data: JSON.stringify(moveJson(fromTerritory, toTerritory, numOfTanks)), 
			success: function(data) {
				if (data.responseCode == -1)
					showModalWindow(data.responseMessage);
			}
		});
	}
	
	// helper function that return json object for placement phase
	function placeTanksJson(territory, numOfTanks) {
		return {'territory' : territory, 'numOfTanks' : numOfTanks};
	}
	
	// helper function that return json object from attacking phase
	function attackJson(fromTerritory, toTerritory, numOfTanks) {
		return {'from' : fromTerritory, 'to' : toTerritory, 'howMany' : numOfTanks};
	}
	
	// helper function that return json object from movement phase
	function moveJson(fromTerritory, toTerritory, numOfTanks) {
		return attackJson(fromTerritory, toTerritory, numOfTanks);
	}
	
	// used to get information of turn status from the server, invoked only one time
	// sse emitter was used by the server to send the json response
	function playerTurnRequest() {
		var allTanksPlacedWarningShown = false;
		var source = new EventSource('../game/turnStatus');
		source.onmessage = function(event) {
			if(!JSON.parse(event.data).hasOwnProperty('currentPlayerColor')) {
				if (!allTanksPlacedWarningShown) {
					allTanksPlacedWarningShown = true;
					showModalWindow("All tanks have been placed! Wait for other players to finish this phase too");
				}
			}
			else {
				var currentPlayer = JSON.parse(event.data).currentPlayerColor;
				$("#playersTurnLabel").html(currentPlayer);
				if(myColor != currentPlayer) 
					$("#gameDiv").hide();
				else {
					$("#gameDiv").show();
					$("#nextPhaseButton").show();
					var phaseId = JSON.parse(event.data).currentPhaseId;
					playPhase(phaseId);
				}
			}
		}
	}
	
	function playPhase(phaseId) {
		switch (phaseId) {
		case 1:
			movementPhaseAlreadyInitialized = false;
			getPlayerInfo();
			initCardsCheckboxes();
			$("#tankMovementPhaseDiv").hide();
			$("#assignTanksPhaseDiv").show();
			break;
		case 2:
			$("#assignTanksPhaseDiv").hide();
			$("#tankPlacementPhaseDiv").show();
			break;
		case 3:
			if (!attackPhaseAlreadyInitialized) {
				updateFromAttackSelect();
				updateToAttackSelect();
				attackPhaseAlreadyInitialized = true;
			}
			$("#tankPlacementPhaseDiv").hide();
			$("#attackPhaseDiv").show();
			break;
		case 4:
			attackPhaseAlreadyInitialized = false;
			if (!movementPhaseAlreadyInitialized) {
				updateFromMovementSelect();
				updateToMovementSelect();
				movementPhaseAlreadyInitialized = true;
			}
			$("#attackPhaseDiv").hide();
			$("#tankMovementPhaseDiv").show();
			break;
		}
	}
	
	function updateMyTerritoriesSelect() {
		$("#wherePlacementSelect").html("");
		for (var i = 0; i < map.territories.length; ++i) {
			if (map.territories[i].owner == myColor)
				$("#wherePlacementSelect").append('<option value="' + map.territories[i].name + '">' 
					+ map.territories[i].name + "</option>");
		}
	}
	
	function updateFromAttackSelect() {
		$("#fromAttackSelect").html("");
		for (var i = 0; i < map.territories.length; ++i)
			if (map.territories[i].owner == myColor)
				$("#fromAttackSelect").append('<option value="' + map.territories[i].name + '">' 
					+ map.territories[i].name + "</option>");
	}
	
	function updateToAttackSelect() {
		$("#toAttackSelect").html("");
		var fromTerritory = $("#fromAttackSelect").val();
		var fromTerritoryNeighboursFound = false;
		var neighbours = null;
		var i = 0;
		while (!fromTerritoryNeighboursFound) {
			if (map.neighbourhood[i].name == fromTerritory) {
				fromTerritoryNeighboursFound = true;
				neighbours = map.neighbourhood[i].territories;
			}
			++i;
		}
		
		for (var j = 0; j < neighbours.length; ++j) {
			$("#toAttackSelect").append('<option value="' + neighbours[j] + '">' 
					+ neighbours[j] + "</option>");
		}	
	}
	
	function updateFromMovementSelect() {
		$("#fromMovementSelect").html("");
		for (var i = 0; i < map.territories.length; ++i)
			if (map.territories[i].owner == myColor)
				$("#fromMovementSelect").append('<option value="' + map.territories[i].name + '">' 
						+ map.territories[i].name + "</option>");
	}
	
	function updateToMovementSelect() {
		$("#toMovementSelect").html("");
		var fromTerritory = $("#fromMovementSelect").val();
		var fromTerritoryNeighboursFound = false;
		var neighbours = null;
		var i = 0;
		while (!fromTerritoryNeighboursFound) {
			if (map.neighbourhood[i].name == fromTerritory) {
				fromTerritoryNeighboursFound = true;
				neighbours = map.neighbourhood[i].territories;
			}
			++i;
		}
		
		for (var j = 0; j < neighbours.length; ++j) {
			$("#toMovementSelect").append('<option value="' + neighbours[j] + '">' 
					+ neighbours[j] + "</option>");
		}	
	}
	
	function initCardsCheckboxes() {
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
					$("#availableTanksDiv").html(numberOfAvailableTanks);
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
	
	$("#attackButton").click(function() {
		attack();
	});
	
	$("#fromAttackSelect").change(function() {
		updateToAttackSelect();
	});
	
	$("#fromMovementSelect").change(function() {
		updateToMovementSelect();
	});
	
	$("#moveButton").click(function() {
		moveTanks();
	});
	
});