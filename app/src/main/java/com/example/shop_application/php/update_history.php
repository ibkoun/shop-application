<?php
	spl_autoload_register(function($class) {
		include_once $class . ".php";
	});

	$server = new SQLServer();
	$server->connectToDatabase();
	
	$json = file_get_contents('php://input');
	$data = json_decode($json, true);
	
	// Iterate through each order.
	foreach($data as $order) {
		// Insert a new record into the orders table.
		$user_id = $order['user_id'];
		$total_price = $order['total_price'];
		$sql_query = "INSERT INTO orders (user_id, total_price) VALUES ('$user_id', '$total_price')";
		mysqli_query($server->getConnection(), $sql_query);
		
		// Get the the id of the last inserted order.
		$sql_query = "SELECT LAST_INSERT_ID()";
		$result = mysqli_query($server->getConnection(), $sql_query);
		$last_order = mysqli_fetch_array($result);
		$order_id = $last_order[0];
		$order_items = $order['order_items'];
		
		// Iterate through each items of the last inserted order.
		foreach($order_items as $item) {
			$item_id = $item['item_id'];
			$item_name = $item['item_name'];
			$quantity = $item['quantity'];
			$unit_price = $item['unit_price'];
			$sql_query = "INSERT INTO order_items (order_id, item_id, item_name, quantity, unit_price) VALUES ('$order_id', '$item_id', '$item_name', '$quantity', '$unit_price')";
			mysqli_query($server->getConnection(), $sql_query);
		}
	}
	
	// Return the last inserted order (including the purchased items) to the application.
	$sql_query = "SELECT * FROM orders WHERE order_id = '$order_id'";
	$result = mysqli_query($server->getConnection(), $sql_query);
	$result_array = array();
	while ($order_row = mysqli_fetch_array($result)) {
		$order = $order_row;
		$order_id = $order['order_id'];
		$order['order_items'] = array();
		$sql_query = "SELECT items.* FROM (SELECT order_items.order_id, store.item_id, store.item_name, store.category, order_items.quantity, store.description, order_items.unit_price, store.image FROM order_items JOIN store ON order_items.item_id = store.item_id WHERE order_items.order_id = '$order_id') items";
		$result = mysqli_query($server->getConnection(), $sql_query);
		while ($order_item_row = mysqli_fetch_array($result)) {
			$order_item = $order_item_row;
			array_push($order['order_items'], $order_item);
		}
		array_push($result_array, $order);
	}
	
    echo json_encode($result_array);

    mysqli_close($server->getConnection());
?>