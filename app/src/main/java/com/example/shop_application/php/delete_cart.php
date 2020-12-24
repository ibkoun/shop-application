<?php
	spl_autoload_register(function($class) {
		include_once $class . ".php";
	});
	
	$server = new SQLServer();
	$server->connectToDatabase();
	
	$json = file_get_contents('php://input');
	$data = json_decode($json, true);
	$updated_items = array();
	
	// Iterate through each item that will be removed from the cart.
	foreach($data as $value) {
		$user_id = $value["user_id"];
		$item_id = $value["item_id"];
		$item_name = $value["item_name"];
		$quantity = $value["quantity"];
		$unit_price = $value["unit_price"];
		array_push($updated_items, $item_id);
		$sql_query = "DELETE FROM shopping_cart WHERE user_id = '$user_id' AND item_id = '$item_id'";
		mysqli_query($server->getConnection(), $sql_query);
		
	}
	
	// Return the removed items back to the application.
	$sql_query = "SELECT item_id FROM store WHERE item_id IN (" . implode(',', $updated_items) . ")";
	$result = mysqli_query($server->getConnection(), $sql_query);
	$result_array = array();
	$row_array = array();
	while ($row = mysqli_fetch_array($result)) {
		$row_array = $row;
		array_push($result_array, $row_array);
	}
	
    echo json_encode($result_array);
	
	$server->disconnect();
?>