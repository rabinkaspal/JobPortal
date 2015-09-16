<?php
include_once('includes/db_connect.php');

$response = array();

if(isset($_GET['username']) && isset($_GET['password']) && !empty($_GET['username']) && !empty($_GET['password'])){

$username = $_GET['username'];
$password=$_GET['password'];
	
	
	$q = "select * from jobseeker where username='$username' and password='".md5($password)."' limit 1";
	$r = mysql_query($q);
	
	if(mysql_num_rows($r)==1){
		$response['success']=1;
		$response['jobseeker'] = array();
		$jobseeker = array();
		while($row= mysql_fetch_array($r)){
			$jobseeker['jsId'] = $row['jobseeker_id'];
			$jobseeker['jsName'] = $row['js_name'];
			$jobseeker['jsEmail'] = $row['email'];
			$jobseeker['applied_jobs'] = $row['applied_jobs'];
			$jobseeker['saved_jobs'] = $row['saved_jobs'];
              
			
			array_push($response['jobseeker'], $jobseeker);
		}
		echo json_encode($response);
	}else{
		$response['success']=0;
		$response['message']="No such username and password combination.";
		echo json_encode($response);
	}
}else{
	$response['success']=0;
	$response['message']="username/password not set";
	echo json_encode($response);
}



?>