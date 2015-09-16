<?php
ob_start();
session_start();
   include_once('../includes/db_connect.php');
   include_once('includes/functions.php');

checkLogin();

if(isset($_GET['submit'])){
	
	if(!empty($_GET['chkid'])){
		$i=0;
		foreach($_GET['chkid'] as $id){
			if($i!=0){
				mysql_query("delete from jobs where job_id={$id}");
				
				
			}
				$i++;
		}
		header('Location:dashboard.php');
	}
}


if(isset($_GET['id']) && !empty($_GET['id'])){

		$id = $_GET['id'];
	
		mysql_query("delete from jobs where job_id={$id}");
				header('Location:dashboard.php');
}


?>