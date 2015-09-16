<?php 

include_once('includes/db_connect.php');

$response= array();

$q = "select * from employers order by company_name asc";
$r = mysql_query($q);

if($r){
$response['success']=1;
$response['companies'] = array();
$cat = array();
while($row = mysql_fetch_array($r)){
	$cat['emp_id'] = $row['emp_id'];
	$cat['company_name'] = $row['company_name'];
	$cat['location'] = $row['address'];
	$cat['image_url'] = $row['logo'];
	
	
	
	array_push($response['companies'], $cat);
}

echo json_encode($response);
}
else{
$response['success']=0;
$response['message']="cannot find companies";
}

?>