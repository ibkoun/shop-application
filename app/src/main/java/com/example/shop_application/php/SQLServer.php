<?php
	// https://www.journaldev.com/12607/android-login-registration-php-mysql
	// https://supunkavinda.blog/php/autoload-classes-namespaces
	include('shop_application_config.php');
	
	class SQLServer {
		private $host, $username, $password, $database, $connection;
		
		public function __construct() {
			$this->host = host;
			$this->username = username;
			$this->password = password;
			$this->database = database;
		}
		
		public function connectToServer() {
			$this->connection = mysqli_connect($this->host, $this->username, $this->password);
			if (mysqli_connect_errno($this->connection)) {
				die("Failed to connect to the server '$host': " . mysqli_connect_error());
			}
		}
		
		public function connectToDatabase() {
			$this->connection = mysqli_connect($this->host, $this->username, $this->password, $this->database);
			if (mysqli_connect_errno($this->connection)) {
				die("Failed to connect to the database '$database': " . mysqli_connect_error());
			}
		}
		
		public function getConnection() {
			return $this->connection;
		}
		
		public function getDatabase() {
			return $this->database;
		}
		
		public function disconnect() {
			mysqli_close($this->connection);
		}
	}
?>