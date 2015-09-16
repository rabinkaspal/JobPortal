<?php 
include_once("includes/db_connect.php");



$response=array();
      
      $js_id = $_GET['js_id'];


$q = "select js_name,address,email,previous_jobs,educational_degrees from jobseeker where jobseeker_id={$js_id}";

$result = mysql_query($q);

if($result){

      if(mysql_num_rows($result)>0){
      $response["success"] = 1;
      
            while($row = mysql_fetch_array($result)){

                  $response["js_name"] = $row['js_name'];
                  $response["address"] = $row['address'];
                  $response["email"] = $row['email'];

            //      $response["previous_jobs"] = $row['previous_jobs'];
                  $emp_json = $row['previous_jobs'];
                  $emp = json_decode($emp_json, true);


                  $response["currCompany"] = $emp['company'];
                  $response["currPosition"] = $emp['position'];
                  $response['fromDate'] =  $emp['fromYear']." ".$emp['fromMonth'];
            //      $response["fromYear"] = $emp['fromYear'];
            //      $response["fromMonth"] = $emp['fromMonth'];

            //      $response["educational_degrees"] = stripslashes($row['educational_degrees']);
                  $education_json = $row['educational_degrees'];
                  $edu = json_decode($education_json, true);

                   $response["course1"] =  $edu['course1'];
                   $response["passed1"] = $edu['year1']." ".$edu['month1'];
                   $response["institution1"] = $edu['institution1'];

                   $response["course2"] = $edu['course2'];
                   $response["passed2"] =  $edu['year2']." ".$edu['month2'];
                   $response["institution2"] = $edu['institution2'];
            }
      
            echo json_encode($response, JSON_PRETTY_PRINT);
      }else{
       $response["success"] = 0;
       echo json_encode($response, JSON_PRETTY_PRINT);
      }
}else{
      $response["success"] = 0;
      echo json_encode($response, JSON_PRETTY_PRINT);
}

?>