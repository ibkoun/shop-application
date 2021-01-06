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
	
	// Iterate through each item in the cart.
	foreach($data as $value) {
		$user_id = $value["user_id"];
		$item_id = $value["item_id"];
		$item_name = $value["item_name"];
		$quantity = $value["quantity"];
		$unit_price = $value["unit_price"];
		array_push($updated_items, $item_id);
		
		// Check if the newly added item is already in the cart.
		$sql_query = "SELECT quantity FROM shopping_cart WHERE item_id = '$item_id'";
		$result = mysqli_query($server->getConnection(), $sql_query);
		$row = mysqli_fetch_array($result);
		if (!empty($row)) {
			// Update the quantity of the item if its already in the cart.
			$quantity = compute_operation($row["quantity"], $quantity, $value["operator"]);
			$sql_query = "UPDATE shopping_cart SET quantity = '$quantity' WHERE user_id = '$user_id' AND item_id = '$item_id'";
		}
		else {
			// Add the item to the cart.
			$sql_query = "INSERT INTO shopping_cart (user_id, item_id, item_name, quantity, unit_price) VALUES ((SELECT user_id FROM users where user_id = '$user_id'), (SELECT item_id FROM store WHERE item_name = '$item_name'), '$item_name', '$quantity', '$unit_price')";
		}
		mysqli_query($server->getConnection(), $sql_query);
	}
	
	// Return the items that were updated in the cart to the application.
	$sql_query = "SELECT store.item_id, store.item_name, store.category, store.description, shopping_cart.quantity, store.unit_price, store.image FROM store JOIN shopping_cart ON store.item_id = shopping_cart.item_id WHERE shopping_cart.user_id = 1 AND shopping_cart.item_id IN (" . implode(',', $updated_items) . ")";
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