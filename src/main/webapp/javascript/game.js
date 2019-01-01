
$(document).ready(function() {
	var cy = cytoscape({
		
		container: document.getElementById('cy'), // container to render in
		
		elements: [ // list of graph elements to start with
						{ // node a
							data: { id: 'Italy' }
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
						}
				   ],
			
			style: [ // the stylesheet for the graph
						{
							selector: 'node',
							style: {
								'width': 120,
								'height': 120,
								'text-halign': 'center',
								'text-valign': 'center'
							}
						},
						
						{
							selector: '#France',
							style: {
								'background-color': '#666',
								'label': 'France 17',
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
							selector: 'edge',
							style: {
								'width': 3,
								'line-color': '#000',
								'target-arrow-color': '#000',
								'target-arrow-shape': 'triangle'
							}
						}
				   ],
				
			layout: {
						name: 'grid',
						rows: 1
					}
	
	});
	cy.boxSelectionEnabled(false);
	cy.zoomingEnabled( true );
});
