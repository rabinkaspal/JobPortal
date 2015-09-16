<?php
include_once('includes/db_connect.php');

$js_id = !empty($_GET['js_id'])?$_GET['js_id']:0;


$response = array();

$q = "select * from job_alerts where js_id={$js_id}";

$result = mysql_query($q) or die(mysql_error());

      if($result){
            if(mysql_num_rows($result)==1){
                  
                  $row = mysql_fetch_array($result);
                  $job_desc = $row['job_desc'];
                //  echo $job_desc;
                  
                  $a = explode("/", $job_desc);
                
                  $k = $a[0];
                  $k = str_replace('  ',' ',$k);
                  $k = str_replace(' ', "-", $k);
                 
//                  echo $k;
                  $response['success']=1;
                  $response['keyword']= $k;
                  $response['minSal']=$a[1];
                  $response['maxSal']=$a[2];
                  $response['minExp']=$a[3];
                  $response['maxExp']=$a[4];
                  echo json_encode($response, JSON_PRETTY_PRINT);
                  
                  
            }else{
                  $response['success']=0;
                  $response['message']="No job alerts set Yet";
                  echo json_encode($response);
            }
      }else{
            $response['success']=0;
            $response['message']="no rresult";
            echo json_encode($response);
      
      }




?>