<?php
	spl_autoload_register(function($class) {
		include_once $class . ".php";
	});
	
	$server = new SQLServer();
	$server->connectToDatabase();

	// Return all the items from the store to the application.
    $sql_query = "SELECT * FROM store";
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