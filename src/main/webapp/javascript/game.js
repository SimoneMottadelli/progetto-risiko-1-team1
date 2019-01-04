$(document).ready(function(){
	
	var myColor;
	findMyColor();
	
	function findMyColor() {
		$.getJSON('../game/getColorFromSession', function(data) {
			if(data.responseCode != -1) {
				myColor = data.responseMessage;
				loadMap();
				//playerTurnRequest();
			}
		});
	}
	
	function loadMap() {
		var source = new EventSource('../game/map');
		source.onmessage = function(event) {
			var continents = JSON.parse(event.data).continents;
			continents = JSON.stringify(continents);
			var territories = JSON.parse(event.data).territories;
			territories = JSON.stringify(territories);
			var membership = JSON.parse(event.data).membership;
			membership = JSON.stringify(membership);
			var neighbourhood = JSON.parse(event.data).neighbourhood;
			neighbourhood = JSON.stringify(neighbourhood);
			showMap(continents, territories, membership, neighbourhood);
		}
	}
	
	function showMap(continents, territories, membership, neighbourhood) {
		$('#continentsDiv').html(continents);
		$('#territoriesDiv').html(territories);
		$('#membershipDiv').html(membership);
		$('#neighbourhoodDiv').html(neighbourhood);
	}
	
	function playerTurnRequest() {
		var source = new EventSource('../game/turn');
		source.onmessage = function(event) {
			console.log(event.data);
		}
	}
	
});