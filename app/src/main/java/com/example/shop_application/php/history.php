<?php
	spl_autoload_register(function($class) {
		include_once $class . ".php";
	});
	
	$server = new SQLServer();
	$server->connectToDatabase();

	// Return all the orders (including the purchased items) to the application.
    $sql_query = "SELECT * FROM orders";
	$result = mysqli_query($server->getConnection(), $sql_query);
	$result_array = array();
	while ($order_row = mysqli_fetch_array($result)) {
		$order = $order_row;
		$order_id = $order['order_id'];
		$order['order_items'] = array();
		$sql_query = "SELECT items.* FROM (SELECT order_items.order_id, store.item_id, store.item_name, store.category, order_items.quantity, store.description, order_items.unit_price FROM order_items JOIN store ON order_items.item_id = store.item_id WHERE order_items.order_id = '$order_id') items";
		$order_items = mysqli_query($server->getConnection(), $sql_query);
		while ($order_item_row = mysqli_fetch_array($order_items)) {
			$order_item = $order_item_row;
			array_push($order['order_items'], $order_item);
		}
		array_push($result_array, $order);
	}
	
    echo json_encode($result_array);

    $server->disconnect();
?>