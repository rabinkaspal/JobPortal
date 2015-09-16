<?php 
include_once("includes/db_connect.php");


$username = $_GET['username'];

$response = array();

if(!empty($username)){
      $q = "select * from jobseeker where username='$username'";

      $result = mysql_query($q);

      if(mysql_num_rows($result)>0){
            $response["success"] = 0;
             $response["message"] = "username exists.";
            echo json_encode($response);
      }else{
            $response["success"] = 1;
             $response["message"] = "Username is available.";
            echo json_encode($response);
      }
}else{
      $response["success"] = 0;
      $response["message"] = "";
      echo json_encode($response);
}

?>