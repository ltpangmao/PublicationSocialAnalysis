
<script type="text/javascript">

function require(script) {
    $.ajax({
        url: script,
        dataType: "script",
        async: false,           // <-- This is the key
        success: function () {
            // all good...
        },
        error: function () {
            throw new Error("Could not load script " + script);
        }
    });
}


  // add a single node to a nodeset
 function addNode(nodeset, id_value, label_value) {
	 require("/vis.js");
	 require("/googleAnalytics.js");

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
 
  
 // add individual edge
 function addEdge(edgeset, id_value, label_value, from_value, to_value) {
	 require("/vis.js");
	 require("/googleAnalytics.js");

	 try {
		 edgeset.add({
			 id: id_value,
			 label: label_value,
			 from: from_value,
			 to: to_value
		 });
		 return edgeset;
	 }
	 catch (err) {
		 alert(err);
	 }
	 
 }
 
 


  </script>
