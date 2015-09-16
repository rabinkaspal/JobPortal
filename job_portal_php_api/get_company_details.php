<?php 

include_once('includes/db_connect.php');

$response= array();
$emp_id="1";
if(isset($_GET['emp_id']) && !empty($_GET['emp_id'])){
	$emp_id = $_GET['emp_id'];
}

$q = "select * from employers where emp_id={$emp_id}";
$r = mysql_query($q);

if(mysql_num_rows($r) == 1){
$response['company_details']= array();
$response['success']= 1;
$company_details = array();
while($row = mysql_fetch_array($r)){
	$company_details['emp_id'] = $row['emp_id'];
	$company_details['email'] = $row['email'];
	$company_details['industry'] = $row['industry'];
	$company_details['ownership'] = $row['ownership'];
	$company_details['numEmployees'] = $row['num_employee'];
	$company_details['company_name'] = $row['company_name'];
	$company_details['address'] = $row['address'];
	$company_details['phone'] = $row['phone'];
	$company_details['url'] = $row['url'];
	$company_details['logo'] = $row['logo'];
	$company_details['company_info'] = $row['company_info'];
	$company_details['instructions'] = $row['instruction'];

	
	array_push($response['company_details'], $company_details);
}

echo json_encode($response);
}else{

$response["success"] = 0;

echo json_encode($response);

}

?>