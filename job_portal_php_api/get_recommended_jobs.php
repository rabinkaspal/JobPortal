<?php
include_once("includes/db_connect.php");


$js_id = $_GET['js_id'];

$response = array();
$job_ids = array();





//select all from analytics for js_id
//iterate through each row to get job_ids
//remove duplicate job_ids

//fetch jobs details for all ids


$r = mysql_query("select * from analytics where js_id={$js_id}");

if($r){
      if(mysql_num_rows($r)>0){
           while($row = mysql_fetch_array($r)){
                 $keyword = $row['common'];
                  
                 $res2 = mysql_query("select *, match(title,category) against('$keyword') as score from jobs,employers where match(title,category) against('$keyword')and jobs.employer_id=employers.emp_id order by score desc");
                 if($res2){
                  while($row2 = mysql_fetch_array($res2)){
                        //echo $row2['job_id']. "<br/>";
                        if(!in_array($row2['job_id'], $job_ids)){
                              $job_ids[] = $row2['job_id'];
                        }
                  }
                 }
                 
                 
                 
           }
           $response["jobs"] = array();
            
          //  echo sizeof($job_ids);
            
            for($i=0; $i<sizeof($job_ids); $i++){
                  
                  $j_id = $job_ids[$i];
                 
                  $query = "select * from jobs,employers where jobs.employer_id=employers.emp_id and jobs.job_id={$j_id}";
                  $result = mysql_query($query) or die(mysql_error());
                  if(mysql_num_rows($result) > 0){
                  while($row = mysql_fetch_array($result)){
                      $jobs = array();
                      $jobs["job_id"] = $row['job_id'];
                      $jobs["employer_id"] = $row['employer_id'];
                      $jobs["title"] = $row['title'];
                      $jobs["location"] = $row['location'];
                      $jobs["job_cat_id"] = $row['job_cat_id'];
                      $jobs["category"] = $row['category'];
                      $jobs["functional_area"] = $row['functional_area'];
                      $jobs["salary"] = $row['salary'];
                      $jobs["experience"] = $row['experience'];
                      $jobs["company_name"] = $row['company_name'];
                      $jobs["key_skills"] = $row['key_skills'];
                      $jobs["qualifications"] = $row['qualifications'];
                      $jobs["body"] = $row['body'];
                      $jobs["extra"] = $row['extra'];
                      $jobs["date_posted"] = $row['date_posted'];
                      $jobs["date_ending"] = $row['date_ending'];

                      $jobs["company_category"] = $row['industry'];
                      $jobs["company_address"] = $row['address'];
                      $jobs["telephone"] = $row['phone'];
                      $jobs["company_description"] = $row['company_info'];

                      array_push($response["jobs"], $jobs);
                  }
            }
            
            
            }
             $response["success"] = 1;
            echo json_encode($response, JSON_PRETTY_PRINT);
            
            
      }else{
           $response["success"]=0;
            $response["message"]="No data yet.";
            echo json_encode($response);
      }
}else{
$response["success"]=0;
$response["message"]="Error fetchin jobs.";
echo json_encode($response);
}

?>