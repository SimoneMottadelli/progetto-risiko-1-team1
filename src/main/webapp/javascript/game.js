$(document).ready(function(){
	
	loadMap();
	playerTurnRequest();
	
	function loadMap() {
		var source = new EventSource("../game/map");
		source.onmessage = function(event) {
			console.log(event.data);
		}
	}
	
	function playerTurnRequest() {
		var source = new EventSource("../match/info");
		source.onmessage = function(event) {
			console.log(event.data);
		}
	}
	
	$("#button").click(function() {
		loadMap();
		playerTurnRequest();
	});
});