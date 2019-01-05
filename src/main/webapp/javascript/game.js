$(document).ready(function(){
	
	var myColor = null;
	var myTerritories = null;
	var availableTanks = null;
	showOrHidePlaceTanksDiv(false);
	findMyColor();
	
	$('#placeTanksButton').click(function() {
		placeTanks()
	});
	
	function showOrHidePlaceTanksDiv(val){
		if(val == false)
			$('#placeTanksDiv').hide();
		else
			$('#placeTanksDiv').show();
	}
	
	function findMyColor() {
		$.getJSON('../game/getColorFromSession', function(data) {
			if(data.responseCode != -1) {
				myColor = data.responseMessage;
				loadMap();
				showOrHidePlaceTanksDiv(true);
				playerTurnRequest();
			}
		});
	}
	
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
	
	function placeTanks() {
		var territory = $('#from').val();
		$('#from').val('');
		var numOfTanks = parseInt($('#howMuch').val(), 10);
		$('#howMuch').val(0);
		if(isMyTerritory(territory)) {
			$.ajax({
				type : 'POST',
				url : '../game/placeTanks',
				contentType: 'application/json',
				dataType: 'json',
				data: JSON.stringify(placeTanksJson(territory, numOfTanks)), 
				success: function(data) {
					if(data.responseCode == -1)
						alert(data.responseMessage);
				}
			});
		}
		else
			alert(territory + ' is not yours');
	}
	
	function placeTanksJson(territory, numOfTanks) {
		return {'where' : territory, 'numOfTanks' : numOfTanks};
	}
	
	
	function isMyTerritory(territoryName) {
		var i = 0;
		while(i < myTerritories.length) {
			if(myTerritories[i].name == territoryName && myTerritories[i].owner == myColor)
				return true;
			++i;
		}
		return false;
	}
	
	
	function showMap(continents, territories, membership, neighbourhood) {
		$('#continentsDiv').html(continents);
		$('#territoriesDiv').html(territories);
		$('#membershipDiv').html(membership);
		$('#neighbourhoodDiv').html(neighbourhood);
	}
	
	function playerTurnRequest() {
		var source = new EventSource('../game/turnStatus');
		source.onmessage = function(event) {
			if(!JSON.parse(event.data).hasOwnProperty('currentPlayer') && availableTanks != 0) {
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
	}
	
});