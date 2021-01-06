<?php
	spl_autoload_register(function($class) {
		include_once $class . ".php";
	});
	
	class DatabaseManager {
		private $server;
		
		public function __construct() {
			$this->server = new SQLServer();
			$database = $this->server->getDatabase();
			$this->server->connectToServer();
			$sql_query = "CREATE DATABASE IF NOT EXISTS $database";
			$output;
			if (mysqli_query($this->server->getConnection(), $sql_query)) {
				$this->server->connectToDatabase();
				$output = sprintf("Database '%s' created successfully.\r\n", $database);
			}
			else {
				$output = sprintf("Failed to create database '%s': %s\r\n", $database, mysqli_error($this->$connection));
			}
			echo nl2br($output);
		}
		
		public function createTable($table, $query) {
			$output;
			if (mysqli_query($this->server->getConnection(), $query)) {
				$output = sprintf("Table '%s' created successfully.\r\n", $table);
			}
			else {
				$output = sprintf("Failed to create table '%s'.\r\n", $table);
			}
			echo nl2br($output);
		}
		
		public function insertIntoTable($table, $query) {
			$output;
			if (mysqli_query($this->server->getConnection(), $query)) {
				$output = sprintf("Values inserted successfully into table '%s'.\r\n", $table);
			}
			else {
				$output = sprintf("Failed to insert values into table '%s'.\r\n", $table);
			}
			echo nl2br($output);
		}
		
		public function disconnect() {
			$this->server->disconnect();
		}
	}
	
	// Create the database if it doesn't exists.
	$db_manager = new DatabaseManager();
	$createTable = "createTable";
	$insertIntoTable = "insertIntoTable";
	$disconnect = "disconnect";
	
	// Create a table named 'users'.
	$table = "users";
	$sql_query = "CREATE TABLE IF NOT EXISTS $table (user_id INT AUTO_INCREMENT, username VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL, created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY(user_id), UNIQUE(email))";
	$db_manager->$createTable($table, $sql_query);
	
	// Add a user.
	$sql_query = "INSERT INTO $table (username, password, email) VALUES ('foo', 'bar', 'hello_world@gmail.com')";
	$db_manager->insertIntoTable($table, $sql_query);
	
	// Create a table named 'store'.
	$table = "store";
	$sql_query = "CREATE TABLE IF NOT EXISTS $table (item_id INT AUTO_INCREMENT, item_name VARCHAR(255) NOT NULL, category VARCHAR(255) NOT NULL, quantity INT NOT NULL, description VARCHAR(255), unit_price DECIMAL(6, 2) NOT NULL, image VARCHAR(255), PRIMARY KEY(item_id), UNIQUE(item_name))";
	$db_manager->createTable($table, $sql_query);
	
	// Add items to the store.
	$image = "http://your local address/phpmyadmin/any directory/c5f0fa5ac14327b8330fde1c621ffa8a.jpg"; // Hard coded
	for ($i = 0; $i <= 30; $i++) {
		$item_name = "item" . $i;
		$unit_price = rand(100, 2000) / 100;
		$sql_query = "INSERT INTO $table (item_name, category, quantity, description, unit_price, image) VALUES ('$item_name', 'consumable', '100', 'None', '$unit_price', '$image')";
		$db_manager->insertIntoTable($table, $sql_query);
	}
	

	// Create a table named 'shopping_carts'.
	$table = "shopping_cart";
	$sql_query = "CREATE TABLE IF NOT EXISTS $table (cart_items_id INT AUTO_INCREMENT, user_id INT, item_id INT, item_name VARCHAR(255) NOT NULL, quantity INT NOT NULL, unit_price DECIMAL(6, 2) NOT NULL, PRIMARY KEY (cart_items_id), FOREIGN KEY (user_id) REFERENCES users (user_id), FOREIGN KEY (item_id) REFERENCES store (item_id))";
	$db_manager->createTable($table, $sql_query);
	
	// Create a table named 'orders'.
	$table = "orders";
	$sql_query = "CREATE TABLE IF NOT EXISTS $table (order_id INT AUTO_INCREMENT, user_id INT, total_price DECIMAL(6, 2) NOT NULL, purchase_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY(order_id), FOREIGN KEY(user_id) REFERENCES users(user_id))";
	$db_manager->createTable($table, $sql_query);
	
	// Create a table named 'order_items'.
	$table = "order_items";
	$sql_query = "CREATE TABLE IF NOT EXISTS $table (order_items_id INT AUTO_INCREMENT, order_id INT NOT NULL, item_id INT NOT NULL, item_name VARCHAR(255) NOT NULL, quantity INT NOT NULL, unit_price DECIMAL(6, 2) NOT NULL, PRIMARY KEY (order_items_id), FOREIGN KEY(order_id) REFERENCES orders(order_id), FOREIGN KEY(item_id) REFERENCES store(item_id))";
	$db_manager->createTable($table, $sql_query);
	
	// Close the database connection.
	$db_manager->$disconnect();
?>