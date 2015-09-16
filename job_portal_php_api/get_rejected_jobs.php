<?php
include_once('includes/db_connect.php');


$js_id = $_GET['js_id'];

$response=array();
$response['rejected_jobs']="";
$q = "select job_id from job_acceptance where js_id={$js_id} and status=0";

$result = mysql_query($q) or die(mysql_error());

if($result){
      while($row = mysql_fetch_array($result)){
           $response['rejected_jobs'] .= $row['job_id'] . ",";
      }
      
      echo json_encode($response);
      
}else{
      echo json_encode($response);
}
      


?>