
$(document).ready(function() {
	
	// --------------------
	// + GLOBAL VARIABLES +
	// --------------------
	var myColor = null;
	var numberOfAvailableTanks = null;
	var map = {'territories': [], 'neighbourhood': [], 'membership': [], 'continents': []};
	var cards = []; // cards drawn after conquering a territory
	var historyMessages = []; // messages of what's happening in the game
	var attackPhaseAlreadyInitialized = false;
	var movementPhaseAlreadyInitialized = false;
	var tanksAssignmentPhaseAlreadyInitialized = false;
	var tanksPlacementPhaseAlreadyInitialized = false;
	var attackPhaseAlreadyRefreshed = false;
	var tanksPlacementPhaseAlreadyRefreshed = false;
	var isThereAWinner = false;
	
	
	// ----------------------------------------------------
	// + INIT FUNCTIONS CALLED ONCE THE DOCUMENT IS READY +
	// ----------------------------------------------------
	
	init();	
	
	// this function initializes the map and the player info
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
					startInitialTanksPlacementPhase();
					updateMyTerritoriesSelect($("#wherePlacementSelect"));
				}
				else
					showModalWindow(result.responseMessage);
			}
		});
	}
	
	// this function is used to show the map return by the server on the web page.
	function fillSVGMapDiv(mapSVG){
		$("#mapDiv").html(mapSVG);
		updateSVGMap();
		addMouseoverEventToTerritories();
	}
	
	// this function is used to get the player's information
	function getPlayerInfo() {
		$.ajax({
			type : 'GET',
			url : '../game/playerInfo',
			success: function(result) {
				if(result.responseCode != -1) {
					result = JSON.parse(result.responseMessage);
					updatePlayerStatus(result);
					myColor = result.color;
					$("#myColorLabel").html(myColor);
				}
				else
					showModalWindow(result.responseMessage);
			}
		});
	}
	
	// this function is called when the initial tanks placement phase must begin
	function startInitialTanksPlacementPhase() {
		$("#availableTanksLabel").html(numberOfAvailableTanks);
		$("#tanksPlacementPhaseDiv").show();
	}
	
	
	
	
	// -------------------------------------------------------------------
	// + FUNCTIONS CALLED PERIODICALLY USING SERVER SEND EVENT MECHANISM +
	// -------------------------------------------------------------------
	
	// this function creates a link that connects the client to the server, so that the
	// server can send map updates using Server Send Event mechanism.
	function startRequestingMapLoading() {
		var initialTankPlacementPhaseStarted = false;
		var source = new EventSource('../game/territories');
		source.onmessage = function(event) {
			if(isThereAWinner)
				source.close();
			else {	
				map.territories = JSON.parse(event.data);
				initialTankPlacementPhaseStarted = true;			
				updateSVGMap();
				
				if (!attackPhaseAlreadyRefreshed) {
					updateMyTerritoriesSelect($("#fromAttackSelect"));
					attackPhaseAlreadyRefreshed = true;
				}
				
				if (!tanksPlacementPhaseAlreadyRefreshed) {
					updateMyTerritoriesSelect($("#wherePlacementSelect"));
					tanksPlacementPhaseAlreadyRefreshed = true;
				}
			}
		}
	}
	
	// this function colors the map territories and it also updates the tanks numbers printed on them
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
	
	
	// this function is used to get information of the turn status from the server, using Server Send Event
	// mechanism. It also shows or hides the GAME DIV, so that the user can play his phase turn.
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
			else if(!JSON.parse(event.data).hasOwnProperty('winner')) {
				updateConsoleText(JSON.parse(event.data).history);
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
			else {
				var winner = JSON.parse(event.data).winner;
				showModalWindow(winner + " has won the game!");
				isThereAWinner = true;
				source.close();
				$("#gameDiv").hide();
				$("#returnToHomeDiv").show();
			}
		}
	}
	
	
	// this function is called every time the client receives information about the turn status and it's actually his turn.
	// It shows or hides the PHASE DIVS, corresponding to the PHASEID received from the server, as well as updating these PHASE DIVS.
	function playPhase(phaseId) {
		hideAllPhaseDivs();
		switch (phaseId) {
		case 1:
			movementPhaseAlreadyInitialized = false;
			if (!tanksAssignmentPhaseAlreadyInitialized) {
				getPlayerInfo();
				tanksAssignmentPhaseAlreadyInitialized = true;
			}
			$("#tanksAssignmentPhaseDiv").show();
			break;
		case 2:
			tanksAssignmentPhaseAlreadyInitialized = false;
			if (!tanksPlacementPhaseAlreadyInitialized) {
				updateMyTerritoriesSelect($("#wherePlacementSelect"));
				tanksPlacementPhaseAlreadyInitialized = true;
			}
			$("#tanksPlacementPhaseDiv").show();
			break;
		case 3:
			tanksPlacementPhaseAlreadyInitialized = false;
			if (!attackPhaseAlreadyInitialized) {
				updateMyTerritoriesSelect($("#fromAttackSelect"));
				updateToAttackSelect();
				attackPhaseAlreadyInitialized = true;
			}
			$("#attackPhaseDiv").show();
			break;
		case 4:
			attackPhaseAlreadyInitialized = false;
			if (!movementPhaseAlreadyInitialized) {
				updateMyTerritoriesSelect($("#fromMovementSelect"));
				updateToMovementSelect();
				movementPhaseAlreadyInitialized = true;
			}
			$("#tanksMovementPhaseDiv").show();
			break;
		}
	}
	
	
	// --------------------
	// + UPDATE FUNCTIONS +
	// --------------------
	
	
	// this function is used to render the new game history messages on the console
	function updateConsoleText(newHistoryMessages) {
		for (var i = historyMessages.length; i < newHistoryMessages.length; ++i)
			$("#consoleText").val(newHistoryMessages[i] + "\n" + $("#consoleText").val());
		historyMessages = newHistoryMessages;
	}
	
	// this function is used to show all the player's territories.
	// the parameter SELECT can be: wherePlacementSelect, fromMovementSelect or fromAttackSelect.
	function updateMyTerritoriesSelect(select) {
		select.html("");
		for (var i = 0; i < map.territories.length; ++i) 
			if (map.territories[i].owner == myColor)
				select.append('<option value="' + map.territories[i].name + '">' 
					+ map.territories[i].name + "</option>");
	}
	
	// this function updates the cardsDiv with the player's cards
	function updateCardsCheckboxes() {
		$("#cardsDiv").html("");
		for (var i = 0; i < cards.length; ++i) 
			$("#cardsDiv").append('<input type="checkbox" id="' + i + '"> (' + cards[i].territory + ', ' + cards[i].symbol +') </input>');	
	}
	
	// this function shows a modal window to communicate important information to the player.
	function showModalWindow(message) {
		$("div.modal-body").html("<h2>" + message + "</h2>");
		$("#modalWindow").css("display", "block");
	}
	
	// this function is used to updated the global variable CARDS, which is an array of territories names.
	function updateCards(cardsArray) {
		cards = [];
		for (var i = 0; i < cardsArray.length; ++i)
			cards[i] = {"territory": cardsArray[i].territory, "symbol": cardsArray[i].symbol};
	}
	
	// this function updates the player status: mission card, available tanks and his cards.
	function updatePlayerStatus(status) {
		$("#missionCardLabel").html(status.missionCard.mission);
		numberOfAvailableTanks = status.availableTanks;
		$("#availableTanksLabel").html(numberOfAvailableTanks);
		updateCards(status.cards);
		updateCardsCheckboxes();
	}
	
	// this function is used to update the select that shows the territories names that can be attacked
	function updateToAttackSelect() {
		$("#toAttackSelect").html("");
		var neighbours = findNeighboursOf($("#fromAttackSelect"));
		for (var i = 0; i < neighbours.length; ++i) 
			if (neighbours[i].owner != myColor)
				$("#toAttackSelect").append('<option value="' + neighbours[i].name + '">' 
						+ neighbours[i].name + "</option>");	
	}
	
	// this function is used to update the select that shows the territories that the player can select to move
	// his tanks to
	function updateToMovementSelect() {
		$("#toMovementSelect").html("");
		var neighbours = findNeighboursOf($("#fromMovementSelect"));
		for (var i = 0; i < neighbours.length; ++i) {
			if (neighbours[i].owner == myColor) 
				$("#toMovementSelect").append('<option value="' + neighbours[i].name + '">' 
						+ neighbours[i].name + "</option>");
			
		}
	}
		
	// helper function that returns the list of neighbours of a territory.
	// the parameter FROMSELECT contains the select in which there is the territory name of which we want to know
	// its neighbours.
	function findNeighboursOf(fromSelect) {
		var fromTerritory = fromSelect.val();
		var fromTerritoryNeighboursFound = false;
		var neighboursNames = [];
		var i = 0;
		while (!fromTerritoryNeighboursFound) {
			if (map.neighbourhood[i].name == fromTerritory) {
				fromTerritoryNeighboursFound = true;
				neighboursNames = map.neighbourhood[i].territories;
			}
			++i;
		}
		var neighbours = [];
		for (var j = 0, k = 0; j < map.territories.length && k < neighboursNames.length; ++j) 
			if (map.territories[j].name == neighboursNames[k]) {
				neighbours[k++] = map.territories[j];
				j = -1;
			}
		return neighbours;
	}

	
	// this function hides all phase divs
	function hideAllPhaseDivs() {
		$("#tanksAssignmentPhaseDiv").hide();
		$("#tanksMovementPhaseDiv").hide();
		$("#tanksPlacementPhaseDiv").hide();
		$("#attackPhaseDiv").hide();
	}
	

	// ------------------------------------
	// + FUNCTIONS CALLED BY CLICK EVENTS +
	// ------------------------------------
	
	// this function is called whenever the player wants to play a certain phase.
	// the phase can be the attack phase, the tanks movement phase, etc...
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
					updatePlayerStatus(JSON.parse(data.responseMessage));
					attackPhaseAlreadyRefreshed = false; // in order to refresh "fromAttackSelect" in case of conquest of a new territory
					tanksPlacementPhaseAlreadyRefreshed = false; // in order to refresh "fromAttackSelect" in case of conquest of a new territory
					updateCards(JSON.parse(data.responseMessage).cards);
					updateCardsCheckboxes();
				}
			}
		});
	}
	
	// function used to return to home when there is a winner
	function endGame() {
		$.getJSON('../match/lobbyPage', function(result) {
			if (result.responseCode == -1)
				showModalWindow(result.responseMessage);
			else {
				location.replace(result.responseMessage);
			}
		});
	}
	
	// function used to place tanks on a territory in the INITIAL TANKS PLACEMENT PHASE
	function placeInitialTanks() {
		var territory = $("#wherePlacementSelect").val();
		if (territory == undefined) {
			updateMyTerritoriesSelect($("#wherePlacementSelect"));
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
					if (numOfTanks == numberOfAvailableTanks) {
						$("#tanksPlacementPhaseDiv").hide();
						playerTurnRequest();
						$("#placeTanksButton").unbind();
						$("#placeTanksButton").click(function() {
							placeTanks();
						});						
					}
					numberOfAvailableTanks = JSON.parse(data.responseMessage).availableTanks;
					$("#availableTanksLabel").html(numberOfAvailableTanks);
				}
			}
		});
	}
	
	// function used to make the player get bonus tanks from cards
	function useTris() {
		var selectedCards = [];
		var i = 0;
		$("input:checkbox").each(function () {
			if (this.checked) 
				selectedCards[i++] = cards[$(this).attr("id")];
		});
		var jsonRequestObject = {"cards": selectedCards};
		sendPhaseData(jsonRequestObject);
	}
	
	// function used to place tanks in TANKS PLACEMENT PHASE
	function placeTanks() {
		var territory = $("#wherePlacementSelect").val();
		var numOfTanks = parseInt($('#howManyPlacement').val(), 10);
		$('#howManyPlacement').val(0);
		sendPhaseData(placeTanksJson(territory, numOfTanks));
	}
	
	// function called when the player attacks a territory
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
		sendPhaseData(attackJson(fromTerritory, toTerritory, numOfTanks));
	}
	
	// function called when the player moves his tanks between two territories
	function moveTanks() {
		var fromTerritory = $("#fromMovementSelect").val();
		var toTerritory = $("#toMovementSelect").val();
		var numOfTanks = parseInt($('#howManyMovement').val(), 10);
		$('#howManyMovement').val(0);
		
		if (toTerritory == undefined) {
			showModalWindow("Select a valid territory to move your tanks to!");
			return;
		}
		sendPhaseData(moveJson(fromTerritory, toTerritory, numOfTanks));
	}
	
	// function called when the player wants to change the turn phase
	function nextPhase() {
		$.getJSON('../game/nextPhase', function(result) {
			if (result.responseCode == -1)
				showModalWindow(result.responseMessage);
			else {
				updatePlayerStatus(JSON.parse(result.responseMessage));
				updateCardsCheckboxes();
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
	

	// ----------------
	// + CLICK EVENTS +
	// ----------------
	
	$("#nextPhaseButton").click(function() {
		nextPhase();
	});
	
	$("#useTrisButton").click(function() {
		useTris();
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
	
	$("#returnToHomeButton").click(function() {
		endGame();
	});
	
	$("#moveButton").click(function() {
		moveTanks();
	});
	
	
	// ----------------
	// + OTHER EVENTS +
	// ----------------
	
	$("#fromAttackSelect").change(function() {
		updateToAttackSelect();
	});
	
	$("#fromMovementSelect").change(function() {
		updateToMovementSelect();
	});
	
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
	
});