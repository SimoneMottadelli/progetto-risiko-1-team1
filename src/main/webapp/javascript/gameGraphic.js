
$(document).ready(function() {
	var cy = cytoscape({
		
		container: document.getElementById('cy'), // container to render in
		
		elements: [ // list of graph elements to start with
						{ // node a
							data: { id: 'Italy' }
						},
						{ // node a
							data: { id: 'Switzerland' }
						},
						{ // node a
							data: { id: 'Germany' }
						},
						{ // node b
							data: { id: 'France' }
						},
						{ // edge ab
							data: { id: 'Italy_France', source: 'Italy', target: 'France' }
						},
						{
							data: { id: 'Italy_Germany', source: 'Italy', target: 'Germany' }		
						},
						{
							data: { id: 'Italy_Switzerland', source: 'Italy', target: 'Switzerland' }		
						},
						{
							data: { id: 'Switzerland_Germany', source: 'Switzerland', target: 'Germany' }		
						}
				   ],
			
			style: [ // the stylesheet for the graph
						{
							selector: 'node',
							style: {
								'width': 90,
								'height': 90,
								'text-halign': 'center',
								'text-valign': 'center'
							}
						},
						
						{
							selector: '#France',
							style: {
								'background-color': '#666',
								'label': 'France ' + '17',
							}
						},
						
						{
							selector: '#Germany',
							style: {
								'background-color': '#666',
								'label': 'Germany 2'
							}
						},
						
						{
							selector: '#Italy',
							style: {
								'background-color': '#966',
								'label': 'Italy 3',
							}
						},
						
						{
							selector: '#Switzerland',
							style: {
								'background-color': '#666',
								'label': 'Switzerland 2'
							}
						}
				   ],
				
			layout: {
						name: 'grid',
						rows: 1
					},
	
	 // initial viewport state:
	  zoom: 1,
	  pan: { x: 0, y: 0 },

	  // interaction options:
	  minZoom: 1e-50,
	  maxZoom: 1e50,
	  zoomingEnabled: true,
	  userZoomingEnabled: true,
	  panningEnabled: true,
	  userPanningEnabled: true,
	  boxSelectionEnabled: false,
	  selectionType: 'single',
	  touchTapThreshold: 8,
	  desktopTapThreshold: 4,
	  autolock: false,
	  autoungrabify: false,
	  autounselectify: false,

	  // rendering options:
	  headless: false,
	  styleEnabled: true,
	  hideEdgesOnViewport: false,
	  hideLabelsOnViewport: false,
	  textureOnViewport: false,
	  motionBlur: false,
	  motionBlurOpacity: 0.2,
	  wheelSensitivity: 1,
	  pixelRatio: 'auto'
	
	});
	cy.boxSelectionEnabled(false);
	cy.zoomingEnabled(false);
	cy.$('#j').lock();
	cy.fit( cy.$(':selected'), 50 )
	
let options = {
  name: 'circle',

  fit: true, // whether to fit the viewport to the graph
  padding: 10, // the padding on fit
  boundingBox: undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
  avoidOverlap: true, // prevents node overlap, may overflow boundingBox and radius if not enough space
  nodeDimensionsIncludeLabels: false, // Excludes the label when calculating node bounding boxes for the layout algorithm
  spacingFactor: undefined, // Applies a multiplicative factor (>0) to expand or compress the overall area that the nodes take up
  radius: undefined, // the radius of the circle
  startAngle: 3 / 2 * Math.PI, // where nodes start in radians
  sweep: undefined, // how many radians should be between the first and last node (defaults to full circle)
  clockwise: true, // whether the layout should go clockwise (true) or counterclockwise/anticlockwise (false)
  sort: undefined, // a sorting function to order the nodes; e.g. function(a, b){ return a.data('weight') - b.data('weight') }
  animate: false, // whether to transition the node positions
  animationDuration: 500, // duration of animation in ms if enabled
  animationEasing: undefined, // easing of animation if enabled
  animateFilter: function ( node, i ){ return true; }, // a function that determines whether the node should be animated.  All nodes animated by default on animate enabled.  Non-animated nodes are positioned immediately when the layout starts
  ready: undefined, // callback on layoutready
  stop: undefined, // callback on layoutstop
  transform: function (node, position ){ return position; } // transform a given node position. Useful for changing flow direction in discrete layouts 

};


		cy.layout( options );
});
