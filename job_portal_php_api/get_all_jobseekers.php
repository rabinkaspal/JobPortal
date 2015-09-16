<?php 

include_once('includes/db_connect.php');

$response= array();

$q = "select * from jobseeker";
$r = mysql_query($q);
$jobseeker = array();
while($row = mysql_fetch_array($r)){
	$jobseeker['jobseeker_id'] = $row['jobseeker_id'];
	$jobseeker['js_name'] = $row['js_name'];
	$jobseeker['js_dob'] = $row['js_dob'];
	$jobseeker['phone'] = $row['phone'];
	$jobseeker['address'] = $row['address'];
	$jobseeker['email'] = $row['email'];
	$jobseeker['alt_email'] = $row['alt_email'];
	$jobseeker['educational_degrees'] = $row['educational_degrees'];
	$jobseeker['duration'] = $row['duration'];
	$jobseeker['expected_salary'] = $row['expected_salary'];
	$jobseeker['functional_area'] = $row['functional_area'];
	$jobseeker['relevant_info'] = $row['relevant_info'];
	$jobseeker['username'] = $row['username'];
	$jobseeker['password'] = $row['password'];
	

	
	array_push($response, $jobseeker);
}

echo json_encode($response);


?>