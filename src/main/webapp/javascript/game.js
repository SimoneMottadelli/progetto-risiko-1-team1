$(document).ready(function(){
	
	var myColor = null;
	var myTerritories = null;
	var availableTanks = null;
	showOrHidePlaceTanksDiv(false);
	findMyColor();
	
	$('#placeTanksButton').click(function() {
		initialTanksPlacement();
	});
	
	// used to show or hide a div - val = false -> hide, val = true -> show
	function showOrHidePlaceTanksDiv(val){
		var div = $('#placeTanksDiv')
		if(val == false)
			div.hide();
		else
			div.show();
	}
	
	function showOrHideGameDiv(val) {
		var div = $('#gameDiv');
		if(val == false)
			div.hide();
		else
			div.show();
	}
	
	// used to get this player's color and to identify it on this page
	function findMyColor() {
		$.getJSON('../game/getColorFromSession', function(data) {
			if(data.responseCode != -1) {
				myColor = data.responseMessage;
				getPlayerInfo();
				loadMap();
				showOrHidePlaceTanksDiv(true);
			}
			else
				alert(data.responseMessage);
		});
	}
	
	function getPlayerInfo() {
		$getJSON('../game/playerInfo', function(data) {
			if(data.responseCode != -1) {
				
			}
			else
				alert(data.responseMessage);
		});
	}
	
	// used to load map structure every time that the server send it
	function loadMap() {
		var source = new EventSource('../game/map');
		source.onmessage = function(event) {
			var continents = JSON.parse(event.data).continents;
			continents = JSON.stringify(continents);
			var territories = JSON.parse(event.data).territories;
			myTerritories = territories;
			territories = JSON.stringify(territories);
			var membership = JSON.parse(event.data).membership;
			membership = JSON.stringify(membership);
			var neighbourhood = JSON.parse(event.data).neighbourhood;
			neighbourhood = JSON.stringify(neighbourhood);
			showMap(continents, territories, membership, neighbourhood);
		}
	}
	
	// called only in initial game phase
	function initialTanksPlacement() {
		var territory = $('#from').val();
		$('#from').val('');
		var numOfTanks = parseInt($('#howMuch').val(), 10);
		$('#howMuch').val(0);
		if(isMyTerritory(territory)) {
			$.ajax({
				type : 'POST',
				url : '../game/initialTanksPlacement',
				contentType: 'application/json',
				dataType: 'json',
				data: JSON.stringify(placeTanksJson(territory, numOfTanks)), 
				success: function(data) {
					alert(data.responseMessage);
				}
			});
		}
		else
			alert(territory + ' is not yours');
	}
	
	// helper function that return json object
	function placeTanksJson(territory, numOfTanks) {
		return {'where' : territory, 'numOfTanks' : numOfTanks};
	}
	
	// helper function to avoid server further work
	function isMyTerritory(territoryName) {
		var i = 0;
		while(i < myTerritories.length) {
			if(myTerritories[i].name == territoryName && myTerritories[i].owner == myColor)
				return true;
			++i;
		}
		return false;
	}
	
	// used to refresh html page with che real structure of the map
	function showMap(continents, territories, membership, neighbourhood) {
		$('#continentsDiv').html(continents);
		$('#territoriesDiv').html(territories);
		$('#membershipDiv').html(membership);
		$('#neighbourhoodDiv').html(neighbourhood);
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
								showOrHidePlaceTanksDiv(false);
								alert("all tanks placed, wait that all players place their tanks");
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
					showOrHideGameDiv(false);
			}
		}
	}
	
});