<?php 
include_once("includes/db_connect.php");


$page = (isset($_GET['page']))? $_GET['page'] : 1;
$item_per_page = 20;

$q="";
$job_ids="";
if(isset($_GET['js_id']) && !empty($_GET['js_id'])){
$js_id = $_GET['js_id'];
	
	$js_likes = mysql_result(mysql_query("select likes from jobseeker where jobseeker_id=$js_id"),0);
      //echo $js_likes;
      //echo '<br/>';
      //echo substr_count($js_likes, ",");
      
      if(substr_count($js_likes, ",")>0){
            $q = "select  distinct * from jobs,employers where jobs.job_cat_id in ($js_likes) and jobs.employer_id=employers.emp_id  order by job_id asc limit ". (($page-1) * $item_per_page ) .",".$item_per_page;
      }
      else{
      // $js_likes = str_replace("'", "",$js_likes);
      // echo $js_likes;
      $q = "select  * from jobs,employers where jobs.job_cat_id = 1 and jobs.employer_id=employers.emp_id order by job_id asc limit ". (($page-1) * $item_per_page ) .",".$item_per_page;

      }
}else{
   
$q = "select * from jobs,employers where jobs.employer_id=employers.emp_id order by title asc limit ". (($page-1) * $item_per_page ) .",".$item_per_page;

}



$result = mysql_query($q) or die("asd" . mysql_error());

$response = array();



if(mysql_num_rows($result) > 1){
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


?>