<script type="text/javascript">
////A set of counters, each of which is specific to a type of link, which is first calculated 
////when the dataset is generated/imported and should be well maintained.
// var extendCounter = 0;
// var formalCounter = 0;
// var evalCounter = 0;
// 
// // global pairs: <edge_type_name, edge_counter>   
// var type_counter_pair = {
//		    "extend": extendCounter,
//		    "formal": formalCounter,
//		    "eval": evalCounter
// };
// 
// // global pairs: <number, edge_type_name>   
// var number_type_pair = {
//		    1: "extend",
//		    2: "formal",
//		    3: "eval"
// };
// 
// 
// // the number of nodes and edges
// var node_number = 5;
// var edge_number = 20;
 
  /**
	 * a set of functions that meet our modeling requirements
	 */
  function showEdge() {
// var newColor = '#' + Math.floor((Math.random() * 255 * 255 *
// 255)).toString(16);
	  // collect all check boxes
	  var types = document.getElementsByName("check_type");
	  
	  
	  // pairs: <edge_type_checkbox_name, edge_counter>
	   var checkbox_counter_pair = {
			    "check_extend": extendCounter,
			    "check_formal": formalCounter,
			    "check_eval": evalCounter
	   };
	  
	  
	   // traverse all
	  for (var i = 0; i < types.length; i++) {
		  var hide = !types[i].checked;
		  for(var p = 1; p <= checkbox_counter_pair[types[i].id]; p++) {
			  // get the later part of the id string to
			  var type_name = types[i].id.substring(6);
			  edges.update([{id:type_name+p, hidden: hide}]);
		  }
	  }
	  
// var view = new vis.DataView(data, {
// filter: function (item) {
// return item.id > "check_formal";
// }
// });
	  
	  
// var type_name_set = {
// "check_extend": "extend",
// "check_formal": "formal",
// "check_eval": "eval"
// };
// type_name = type_name_set[types[i].id];
	  
	  

// var i,j;
// for (i = 0; i < types.length; i++) {
// if (types[i].type == "checkbox" && types[i].id == "check_extend") {
// var hide = !types[i].checked;
// for(j = 1; j <= extendCounter; j++) {
// edges.update([{id:'extend'+j, hidden: hide}]);
// }
// }
// else if (types[i].type == "checkbox" && types[i].id == "check_formal") {
// var hide = !types[i].checked;
// for(j = 1; j <= formalCounter; j++) {
// edges.update([{id:'formal'+j, hidden: hide}]);
// }
// }
// else if (types[i].type == "checkbox" && types[i].id == "check_eval") {
// var hide = !types[i].checked;
// for(j = 1; j <= evalCounter; j++) {
// edges.update([{id:'eval'+j, hidden: hide}]);
// }
// }
// else{
//	    	  
// }
// }
	  
// for(i = 1; i <= extensionNumber; i++) {
// edges.update([{id:'extend'+i, hidden: true}]);
// }
// nodes.add({id:11, label:"I'm new!"});
// nodeIds.push(newId);
  }
  
  
  
// Using Math.round() will give you a non-uniform distribution!
  function generateNodes(nodeset, number) {
	  for (var i = 1; i <= number; i++) {
		  addNode(nodeset, i, i);
	  }
	  return nodeset;
  }

  
  // add a single node to a nodeset
 function addNode(nodeset, id_value, label_value) {
	 try {
		 nodeset.add({
		 id: id_value,
		 label: label_value
		 });
	 }
	 catch (err) {
		 alert(err);
	 }
	 return nodeset;
 }
 
 
 function generateRandomEdges(edgeset, e_number, n_number) {
	 var edge_type;
	 var edge_id;
	 var from, to;
	  for (var i = 1; i <= e_number; i++) {
		  edge_type = number_type_pair[getRandomInt(1,3)];
		  from = getRandomInt(1,n_number+1);
		  to = getRandomInt(1,n_number+1);
		  
// edge_type = getRandomInt(1,3);
		  if(edge_type == 'extend'){
			  extendCounter++;
			  edge_id = edge_type+extendCounter; 
		  }
		  else if (edge_type == 'formal'){
			  formalCounter++;
			  edge_id = edge_type+formalCounter;
		  }
		  else if (edge_type == 'eval'){
			  evalCounter++;
			  edge_id = edge_type+evalCounter;
		  }
		  else{
			  window.alert("egde type generation error");
		  }
		  addEdge(edgeset, edge_id, edge_type, from, to);
// window.alert(edge_id);
	  }
	  return edgeset;
 }
 
 // add individual edge
 function addEdge(edgeset, id_value, label_value, from_value, to_value) {
	 try {
		 edgeset.add({
			 id: id_value,
			 label: label_value,
			 from: from_value,
			 to: to_value
		 });
	 }
	 catch (err) {
		 alert(err);
	 }
	 return edgeset;
 }
 
 
// Returns a random integer between min (included) and max (excluded)
// Using Math.round() will give you a non-uniform distribution!
 function getRandomInt(min, max) {
	  // Math.floor(Math.random() * (max - min)) + min;
	 // change the original function to increase the probability of obtaining the value 'max'
	  var random = Math.floor(Math.random() * (max + 1 - min)) + min;
	  // if we luckly obtain the value 'max+1', we set it back to max
	  if (random == max+1){
		  random = max;
	  }
	  return random;
 }
 
  </script>
