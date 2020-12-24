<?php
	spl_autoload_register(function($class) {
		include_once $class . ".php";
	});
	
	$id = $_POST["id"];
	$name = $_POST["name"];
	$category = $_POST["category"];
	$quantity = $_POST["quantity"];
	$price = $_POST["price"];
	//$total_price = $_POST["total_price"];
	
	/*if (!isset($id) or !isset($name) or !isset($category) or !isset($quantity) or !isset(price) or !isset(total_price)) {
		exit("Missing one or more parameters.");
	}*/
	
	$server = new SQLServer();
	$server->connectToDatabase();
	
	// Check if the newly added item is already in the cart.
	$sql_query = "SELECT quantity FROM cart WHERE id = '$id'";
	$result = mysqli_query($server->getConnection(), $sql_query);
	$row = mysqli_fetch_array($result);
	
	// If the item is already in the cart, update its quantity and the total price.
	if (!empty($row)) {
		$quantity = $row["quantity"] + $quantity;
	}
	$total_price = number_format($quantity * $price, 2);
	$sql_query = "INSERT INTO cart (id, name, category, quantity, price, total_price) VALUES ((SELECT id from store WHERE name = '$name'), '$name', '$category', '$quantity', '$price', '$total_price') ON DUPLICATE KEY UPDATE quantity = '$quantity', total_price = '$total_price'";
	mysqli_query($server->getConnection(), $sql_query);
	
	$sql_query = "SELECT * FROM cart";
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