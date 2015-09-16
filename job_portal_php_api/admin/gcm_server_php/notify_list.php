<?php 
include_once('db_connect.php');


$db = new DB_Connect();
$db->connect();

$job_id = $_GET['job_id'];

$json = array();
$sql = "select title,company_name from jobs,employers where jobs.job_id={$job_id} and jobs.employer_id=employers.emp_id";
$res = mysql_query($sql);
while($rows = mysql_fetch_array($res)){
	$json['job_id'] = $job_id;
	$json['job_title'] = $rows['title'];
	$json['company'] = $rows['company_name'];
}

$msgToSend = json_encode($json);

echo $msgToSend . "<br/><br/><hr/><br/><br/>";

$js_ids = array();

$q = "select * from job_alerts";

$r = mysql_query($q);

if($r){
	while($row = mysql_fetch_array($r)){
		echo "<br/>";
		
		$alert = $row['job_desc'];
		echo $alert . "<br/>";
		
		$a = explode("/", $alert);
		print_r($a);
		
		$q1 = "select job_id from jobs where match(title,body,location,key_skills,category) against('$a[0]') and salary between $a[1] and $a[2] and experience between $a[3] and $a[4] order by job_id asc";
		$r1 = mysql_query($q1);
		if($r1){
			$ids="";
			while($row1 = mysql_fetch_array($r1)){
				if($ids=="") $ids .= $row1[0];
				else $ids .= "," . $row1[0];
			}
			echo "<br/>" . $ids;
		}
		
		if (strpos($ids, strval($job_id)) !== false) {
			$js_ids[] = $row['js_id'];
		}
		
		echo "<br/><br/><hr/>";
		
	}
	
	print_r($js_ids);
	
	echo "<br/><br/><hr/><hr/><hr/>";
	
	for($i=0; $i<sizeof($js_ids); $i++){
		echo $js_ids[$i] . "<br/>";
		
		//if($i == 0) continue;
		
		$regId = mysql_result(mysql_query("select regId from notification where js_id={$js_ids[$i]}"),0);
			echo $regId. "<br/>";
			
			 include_once './GCM.php';
			 $gcm = new GCM();
			
			$registatoin_ids = array($regId);
			$message = array("price" => $msgToSend);

			$result = $gcm->send_notification($registatoin_ids, $message);

			
			echo "<hr>";
		
	}
	
}
header('Location: ../dashboard.php');
exit(0);


//select job_id from jobs where match(title,body,location,key_skills,category) against('factory manager basantapur Accounting Taxation Audit') and salary between 12000 and 48000 and experience between 1 and 7 


 ?>