<?php 
error_reporting(E_ERROR | E_WARNING | E_PARSE | E_NOTICE);
$con = mysql_connect("localhost", "root", "") or die("cannot connect to database");

$db = mysql_select_db("jobportal", $con) or die("cannot select database");

$page = (isset($_GET['page']))? $_GET['page'] : 1;

$q = "select * from endless_demo limit ". (($page-1) * 20 ) .",20";


$result = mysql_query($q) or die("asd" . mysql_error());

$response = array();



if(mysql_num_rows($result) > 1){
$response["values"] = array();
$response["success"] = 1;
while($row = mysql_fetch_array($result)){
	$value = array();
	$value["id"] = $row['id'];
	$value["value"] = $row['value'];
	array_push($response["values"], $value);
}

echo json_encode($response);

}else{

$response["success"] = 0;

echo json_encode($response);

}


?>