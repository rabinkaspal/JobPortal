<?php
include_once('includes/db_connect.php');

$js_id = $_GET['js_id'];

$response=array();


$res = mysql_query("select applied_jobs from jobseeker where jobseeker_id={$js_id}");
if(mysql_num_rows($res)>0)
      $applied_jobs = mysql_result($res, 0);
else
      $applied_jobs=0;

if($applied_jobs!=0){

      //$a = explode(',', $applied_jobs);

      $q = "select * from job_acceptance where job_id in ($applied_jobs) and js_id={$js_id}";
      $result = mysql_query($q);
      if($result){
          //echo mysql_num_rows($result);
           if(mysql_num_rows($result) > 0){
                 $response['success']=1;
                 $response['job_response']=array();
                 while($row = mysql_fetch_array($result)){
                        $job_response = array();
                       
                       $job_response['job_id'] = $row['job_id'];
                       $job_response['job_title'] = mysql_result(mysql_query("select title from jobs where job_id={$row['job_id']}"),0);
                       $job_response['status'] = $row['status'];
                       $job_response['responseMsg'] = $row['message'];
                       

                       array_push($response['job_response'],$job_response); 
                 }

                  echo json_encode($response, JSON_PRETTY_PRINT);
      }else{
            $response['success']=0;
            $response['message']="No Job Responses yet.";
            echo json_encode($response, JSON_PRETTY_PRINT);
           }

      }else{
            $response['success']=0;
            $response['message']="No Job Responses yet.";
            echo json_encode($response, JSON_PRETTY_PRINT);
      }

}else{
      $response['success']=0;
      $response['message']="You have not applied for any job yet.";
      echo json_encode($response, JSON_PRETTY_PRINT);
}



?>