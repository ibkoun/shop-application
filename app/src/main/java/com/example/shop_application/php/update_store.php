<?php
	spl_autoload_register(function($class) {
		include_once $class . ".php";
	});
	
	include_once "math.php";
	
	$server = new SQLServer();
	$server->connectToDatabase();
	
	$json = file_get_contents('php://input');
	$data = json_decode($json, TRUE);
	$updated_items = array();
	
	// Iterate through each item that needs to be updated in the store.
	foreach($data as $value) {
		$item_id = $value["item_id"];
		$item_name = $value["item_name"];
		$amount_purchased = $value["quantity"];
		array_push($updated_items, $item_id);
		$sql_query = "SELECT * FROM store WHERE item_id = '$item_id' AND item_name = '$item_name'";
		$result = mysqli_query($server->getConnection(), $sql_query);
		$row = mysqli_fetch_array($result);
		$amount_remaining = $row['quantity'];
		$item_price = $row['unit_price'];
		$quantity = compute_operation($amount_remaining, $amount_purchased, $value["operator"]);
		$total_cost = $amount_purchased * $item_price;
		$sql_query = "UPDATE store SET quantity = '$quantity' WHERE item_id = '$item_id' AND item_name = '$item_name'";
		mysqli_query($server->getConnection(), $sql_query);
	}
	
	// Return the items that were updated in the store to the application.
	$sql_query = "SELECT * FROM store WHERE item_id IN (" . implode(',', $updated_items) . ")";
	$result = mysqli_query($server->getConnection(), $sql_query);
	$resultArray = array();
	$tempArray = array();
	while ($row = mysqli_fetch_array($result)) {
		$tempArray = $row;
		array_push($resultArray, $tempArray);
	}
    echo json_encode($resultArray);

    mysqli_close($server->getConnection());
?>