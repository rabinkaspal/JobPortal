<?php

include_once('includes/db_connect.php');


$js_id = $_GET['js_id'];
$job_desc = $_GET['desc'];

$q = "select * from job_alerts where js_id = {$js_id}";
$r = mysql_query($q);

$response = array();

if(mysql_num_rows($r)>0){
     $qq = "update job_alerts set job_desc = '{$job_desc}' where js_id={$js_id}";
}else{
      $qq = "insert into job_alerts(js_id,job_desc) values({$js_id},'{$job_desc}')";
}

      $result = mysql_query($qq) or die(mysql_error());
      if($result){
      if(mysql_affected_rows() == 1){
            $response['message']='saved';
            echo json_encode($response);
      }else{
            $response['message']='Already Saved';
            echo json_encode($response);
      }
      }else{
           $response['message']='Unknown Error. Try again.';
            echo json_encode($response);
      }


?>