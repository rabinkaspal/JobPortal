<?php
function logout_employer(){
	
	
	if(isset($_SESSION['loggedIn']))unset($_SESSION['loggedIn']);
	if(isset($_SESSION['emp_id']))unset($_SESSION['emp_id']);
	if(isset($_SESSION['emp_email']))unset($_SESSION['emp_email']);
	
	
	session_destroy();
	header('Location: signin.php?logged_out=1');
	exit;
	
}


function checkLogin(){
	if(!isset($_SESSION['loggedIn']) ||(isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']!= true)){
		header('Location:signin.php');
		exit;
	}
}

function isLoggedIn(){
	if(!isset($_SESSION['loggedIn']) ||(isset($_SESSION['loggedIn']) && $_SESSION['loggedIn'] != true))
		return false;
	else return true;
}

?>