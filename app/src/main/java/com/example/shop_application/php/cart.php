<?php
	spl_autoload_register(function($class) {
		include_once $class . ".php";
	});
	
	$server = new SQLServer();
	$server->connectToDatabase();

	// Return all the items from user's cart to the application.
    $sql_query = "SELECT store.item_id, store.item_name, store.category, store.description, shopping_cart.quantity, store.unit_price, store.image FROM store JOIN shopping_cart ON store.item_id = shopping_cart.item_id WHERE shopping_cart.user_id = 1";
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