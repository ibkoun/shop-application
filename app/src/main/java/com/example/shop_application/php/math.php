<?php
	function compute_operation($a, $b, $op) {
		$result = 0;
		switch($op) {
			case '+':
				$result = $a + $b;
				break;
			case '-':
				$result = $a - $b;
				break;
			case '*':
				$result = $a * $b;
				break;
			case '/':
				$result = $a / $b;
				break;
		}
		return $result;
	}
	
	function compute_expression($sequence) {
		
	}
?>