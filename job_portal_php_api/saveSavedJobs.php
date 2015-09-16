<?php 
include_once("includes/db_connect.php");

$jobs = $_GET['jobs'];
$js_id = $_GET['js_id'];


$jobsToPost = substr($jobs,1);
$response = array();

$q = "update jobseeker set saved_jobs='$jobsToPost' where jobseeker_id={$js_id}";

$result = mysql_query($q);

if(mysql_affected_rows() == 1){
      $response["success"]=1;
}else{
      $response["success"]=0;
}


echo json_encode($response, JSON_PRETTY_PRINT);

?>