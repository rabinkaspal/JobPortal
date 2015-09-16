<?php 

include_once('includes/db_connect.php');

$response= array();

$q = "select * from categories";
$r = mysql_query($q);

if($r){
$response['success']=1;
$response['categories'] = array();
$cat = array();
while($row = mysql_fetch_array($r)){
	$cat['cat_id'] = $row['cat_id'];
	$cat['cat_name'] = $row['cat_name'];
	
	
	
	array_push($response['categories'], $cat);
}

echo json_encode($response);
}
else{
$response['success']=0;
$response['message']="cannot find categories";
}

?>