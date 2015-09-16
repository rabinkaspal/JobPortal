<?php 
include_once("includes/db_connect.php");

$job_id = $_GET['job_id'];

$q = "select * from jobs,employers where jobs.employer_id=employers.emp_id AND jobs.job_id={$job_id}";


$result = mysql_query($q) or die("asd" . mysql_error());

$response = array();



if(mysql_num_rows($result) > 0){
$response["jobs"] = array();
$response["success"] = 1;
$response["message"] = "Job Details";
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

echo json_encode($response,  JSON_PRETTY_PRINT);

}else{

$response["success"] = 0;
$response["message"] = "Cannot find Job Details.";

echo json_encode($response,  JSON_PRETTY_PRINT);

}


?>