<?php 
include_once("includes/db_connect.php");



/*
?strKeyword=strKeyword&strLocation=strLocation&strCategory=strCategory&minSal=minSal&maxSal=maxSal&minExp=minExp&maxExp=maxExp
*/

$page = (isset($_GET['page']))? $_GET['page'] : 1;
$item_per_page = 20;

$q="";
$job_ids="";


$strKeyword = $_GET['strKeyword'];
$minSal = $_GET['minSal'];
$maxSal = $_GET['maxSal'];
$minExp = $_GET['minExp'];
$maxExp = $_GET['maxExp'];

//echo $minSal . '<br/>' .$maxSal . '<br/>' .$minExp . '<br/>' .$maxExp; 

//$strKeyword = "factory manager Accounting Taxation Audit basantapur categories";
//$strSkillCat = "business IT Banking crying sleeping";
//$minSal = 1000;
//$maxSal = 20000;
//$minExp = 0;
//$maxExp = 7;

  
$rowCount = mysql_num_rows(mysql_query("select * from jobs,employers where match(title,body,location,key_skills,category) against('$strKeyword') and salary between $minSal and $maxSal and experience between $minExp and $maxExp and jobs.employer_id=employers.emp_id"));

$q = "select *, match(title,body,location,key_skills,category) against('$strKeyword') as score from jobs,employers where match(title,body,location,key_skills,category) against('$strKeyword') and salary between $minSal and $maxSal and experience between $minExp and $maxExp and jobs.employer_id=employers.emp_id order by score desc";  
//limit ". (($page-1) * $item_per_page ) .",".$item_per_page;


$result = mysql_query($q) or die("asd" . mysql_error());

$response = array();



if(mysql_num_rows($result) > 1){
      
      $response["rowCount"] = $rowCount;
      
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
      $response["message"] = "No jobs found.";

echo json_encode($response, JSON_PRETTY_PRINT);

}


?>