<?php 
include_once("includes/db_connect.php");


$page = (isset($_GET['page']))? $_GET['page'] : 1;
$item_per_page = 20;

if(isset($_GET['js_id']) && !empty($_GET['js_id']))
$js_id = $_GET['js_id'];

$q = "";
	$applied_jobs = mysql_result(mysql_query("select applied_jobs from jobseeker where jobseeker_id=$js_id"),0);
    // echo $applied_jobs;
//      echo '<br/>';
      $itemCount =  substr_count($applied_jobs, ",");
 // echo $itemCount;


if(!empty($applied_jobs)){

      if($itemCount>0){
            $q = "select  distinct * from jobs,employers where jobs.job_id in ($applied_jobs) and jobs.employer_id=employers.emp_id  order by job_id asc limit ". (($page-1) * $item_per_page ) .",".$item_per_page;
      }else{
            $q = "select distinct * from jobs,employers where jobs.job_id=$applied_jobs and jobs.employer_id=employers.emp_id limit ". (($page-1) * $item_per_page ) .",".$item_per_page;
      }
   
$result = mysql_query($q) or die("Error in sql \n" . mysql_error());

$response = array();



if(mysql_num_rows($result) > 0){
$response["jobs"] = array();
$response["success"] = 1;
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