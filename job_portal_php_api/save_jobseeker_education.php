<?php 
include_once("includes/db_connect.php");


$detail = mysql_real_escape_string($_GET['detail_string']);
$js_id = $_GET['js_id'];

$q = "update jobseeker set educational_degrees='".$detail."' where jobseeker_id={$js_id}";

$result = mysql_query($q) or die ("some error" . mysql_error());

$response = array();

if(mysql_affected_rows()==1){
      $response["success"] = 1;
      $response['message'] = "Saved.";
            
      echo json_encode($response, JSON_PRETTY_PRINT);
}else{

      $response["success"] = 0;
      $response['message'] = "Error Saving Details.";
            
      echo json_encode($response, JSON_PRETTY_PRINT);
}

?>