

<?php include_once('includes/top.php');?>

<?php 
	checkLogin();
?>

<?php


$title = $location=$category=$salary=$experience=$keySkills=$qualifications=$description=$tags=$extras=$expires="";

$titleErr=$locationErr=$salaryErr=$experienceErr=$keySkillsErr=$qualificationsErr=$descriptionErr=$tagsErr="";

$job_id="";

$employer_id="";

if(isset($_GET['edit']) && isset($_GET['job_id']) && !empty($_GET['job_id']) && $_GET['edit']==1){

$job_id = $_GET['job_id'];
	
	
	
	$q = "select * from jobs where job_id={$job_id} limit 1";
	$result = mysql_query($q);
	while($row = mysql_fetch_array($result)){
			$employer_id = $_SESSION['emp_id'];
			$title = $row['title'];
			$location= $row['location'];
			$category= $row['category'];
			$salary= $row['salary'];
			$experience= $row['experience'];
			$keySkills= $row['key_skills'];
			$qualifications= $row['qualifications'];
			$description= $row['body'];
			$tags= $row['tags'];
			$extras= $row['extra'];
			$expires= $row['date_ending'];

	}
	
//echo 	$title.'<br/>'.$location.'<br/>'.$employer_id.'<br/>'.$job_id.'<br/>'.$category.'<br/>'.$salary.'<br/>'.$experience.'<br/>'.$keySkills.'<br/>'.$qualifications.'<br/>'.$description.'<br/>'.$tags.'<br/>'.$extras.'<br/>'.$expires;

	
	

}



	if(isset($_POST['submit'])){
		$title = test_input($_POST['title']);
		$location = test_input($_POST['location']);
		$category = test_input($_POST['industry']);
		$salary = test_input($_POST['salary']);
		$experience = test_input($_POST['experience']);
		$keySkills = test_input($_POST['keyskills']);
		$qualifications = test_input($_POST['qualifications']);
		$description = $_POST['description'];
		$tags = test_input($_POST['tags']);
		$extras = test_input($_POST['extras']);
		$expires = test_input($_POST['date_ending']);

			if(empty($title)){
				$titleErr = "Title is Required.";
			}elseif(strlen($title) < 5){
				$titleErr = "Too short title";
			}

			if(empty($location)){
				$locationErr = "Location is Required.";
			}
		
			
			if(empty($salary)){
				$salaryErr = "Salary is Required.";
			}else{
				if(!preg_match('/^[1-9][0-9]*$/', $salary)){
					$salaryErr = "Only numbers are allowed.";
				}
				
			}

			if(empty($experience)){
				$experienceErr = "Experience is Required.";
			}else if(!preg_match('/^[1-9][0-9]*$/', $experience)){
				$experienceErr = "Only numbers are allowed.";
			}

			if(empty($keySkills)){
				$keySkillsErr = "KeySkills is Required.";
			}

			if(empty(trim($qualifications))){
				$qualificationsErr = "Qualification is Required.";
			}

			if(empty($description)){
				$descriptionErr = "Description is Required.";
			}

			if(empty($tags)){
				$tagsErr = "Tags is Required.";
			}
		
		
		if(hasNoErrors()){
$employer_id = $_SESSION['emp_id'];
$category_name = mysql_result(mysql_query('select cat_name from categories where cat_id='.$category),0);
$date = date('d/m/Y');

			
		if($_POST['submit']=="add"){
			
	$q = "insert into jobs(employer_id,title,location,job_cat_id,category,functional_area,
	salary,experience,key_skills,qualifications,body,extra,tags,date_posted,date_ending)
	values($employer_id,'$title','$location','$category','$category_name','$category_name',
	'$salary','$experience','$keySkills','$qualifications',\"$description\",'$extras','$tags','$date','$expires')";
	
	$r = mysql_query($q) or die('<br/><br/><br/><br/>'.mysql_error());
	
		$insert_id = mysql_insert_id();
	
		if(mysql_affected_rows()==1){
		header('Location:gcm_server_php/notify_list.php?job_id='.$insert_id);
		exit(0);
		}
	
	}else if($_POST['submit']=="edit"){
		
		$job_id_posted = $_POST['job_id'];
						
			echo $job_id_posted;
			
			$q = "update  jobs set
		title='$title',location='$location',job_cat_id='$category',
		category='$category_name',functional_area='$category_name',
		salary='$salary',experience='$experience',key_skills='$keySkills',
		qualifications='$qualifications',body=\"$description\",extra='$extras',tags='$tags',
		date_modified='$date',date_ending='$expires'
		where job_id={$job_id_posted} and employer_id={$employer_id} limit 1";
			$r = mysql_query($q) or die('<br/><br/><br/><br/>'.mysql_error());
		}
			
	if(mysql_affected_rows()==1){
		header('Location:dashboard.php');
		exit(0);
	}
			

	
}
	
		
		}

	
	
function test_input($data){
   $data = trim($data);
   $data = stripslashes($data);
   $data = htmlspecialchars($data);
   return $data;
}

/*echo $titleErr .'<br/>'. $locationErr .'<br/>'. $salaryErr .'<br/>'. $experienceErr .'<br/>'. $keySkillsErr .'<br/>'. $qualificationsErr .'<br/>'. $descriptionErr .'<br/>'. $tagsErr;
*/

function hasNoErrors(){
	global $titleErr,$locationErr,$salaryErr,$experienceErr,$keySkillsErr,$qualificationsErr,$descriptionErr,$tagsErr;
			if(strlen($titleErr)==0 && strlen($locationErr)==0 && strlen($salaryErr)==0 && strlen($experienceErr)==0 && strlen($keySkillsErr)==0 && strlen($qualificationsErr)==0 && strlen($descriptionErr)==0 && strlen($tagsErr)==0){
			return true;
		}else{
			return false;
		}
	}
	

//echo (hasNoErrors())? 'no errors' : ' errors ';


//echo '<br>'.mysql_result(mysql_query('select cat_name from categories where cat_id=9'),0);


?>
      
      
    <div class="container">
		
          <form class="form-register form-horizontal" role="form" method="post" id="addJobs" action="<?php echo $_SERVER['PHP_SELF']; ?>">
            <h2 style="color:#999; margin-top:40px;">Post Job</h2>
              <hr style="border-top:1px solid #ccc; margin-bottom:30px;">
        <input type="hidden" name="job_id" value="<?php echo $job_id; ?>"/>
        <h4 class="form-signin-heading" style="color:#333399;"><strong>Job Details</strong></h4>
              <hr style="border-top:1px solid #ccc; margin-top:10px; margin-bottom:20px;">
              
          <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($titleErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="jobTitle" >Title</label>
            <div class="col-sm-6">
              <input type="text" id="jobTitle" name="title" class="form-control" value="<?php echo $title; ?>" placeholder="Job Title" required autofocus> 
				<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($titleErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>
            </div>
			  <?php if(isset($_POST['submit'])) echo (!empty($titleErr)) ? '<p class="col-sm-4 formError">'.$titleErr.'</p>':'';?>
          </div>
              
         <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($locationErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="location" >Location:</label>
            <div class="col-sm-4">
              <input type="text" id="location" name="location" class="form-control" value="<?php echo $location; ?>" placeholder="Address" required>
				<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($locationErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>
            </div><?php if(isset($_POST['submit'])) echo (!empty($locationErr)) ? '<p class="col-sm-4 formError">'.$locationErr.'</p>':'';?>
          </div>
              
         <div class="form-group ">
            <label class="control-label col-sm-2" for="industry" >Category:</label>
            <div class="col-sm-4">
                 <select class="form-control" name="industry" id="industry">
                  
                  <?php 
                       $q = "select * from categories order by cat_name";
                       $r = mysql_query($q);

                    if(mysql_num_rows($r)>0){
                     while($row = mysql_fetch_array($r)){
                      echo '<option value="'.$row['cat_id'].' "'; echo($category==$row['cat_id']) ? 'selected' : ''; echo '>'.$row['cat_name'].'</option>';
                     }}
                  ?>
                  </select>
            </div>
          </div>
              
                  
        <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($salaryErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="salary" >Salary:</label>
            <div class="col-sm-2">
              <input type="text" id="salary" name="salary" class="form-control" value="<?php echo $salary; ?>" placeholder="salary per month" required>
				<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($salaryErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>
            </div><?php if(isset($_POST['submit'])) echo (!empty($salaryErr)) ? '<p class="col-sm-4 formError">'.$salaryErr.'</p>':'';?>
          </div>
              
         <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($experienceErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="experience" >Experience:</label>
            <div class="col-sm-2">
              <input type="text number" id="experience" name="experience" class="form-control" value="<?php echo $experience; ?>" placeholder="in years" required>
				<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($experienceErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>
            </div><?php if(isset($_POST['submit'])) echo (!empty($experienceErr)) ? '<p class="col-sm-4 formError">'.$experienceErr.'</p>':'';?>
          </div>
              
        <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($keySkillsErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="keySkills" >Key Skills:</label>
            <div class="col-sm-6">
              <input type="text" id="keySkills" name="keyskills" class="form-control" value="<?php echo $keySkills; ?>" placeholder="separate each skills by commas" required>
				<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($keySkillsErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>
            </div><?php if(isset($_POST['submit'])) echo (!empty($keySkillsErr)) ? '<p class="col-sm-4 formError">'.$keySkillsErr.'</p>':'';?>
          </div>
              
              
          <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($qualificationsErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="qualifications" >Qualifications:</label>
            <div class="col-sm-4">
              <input type="text" id="qualifications" name="qualifications" class="form-control" value="<?php echo $qualifications; ?>" placeholder="qualifications" required>
				<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($qualificationsErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>
            </div><?php if(isset($_POST['submit'])) echo (!empty($qualificationsErr)) ? '<p class="col-sm-4 formError">'.$qualificationsErr.'</p>':'';?>
          </div>
                     
              
        <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($descriptionErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="description" >Job Description:</label>
            <div class="col-sm-6">
				<textarea class="form-control ckeditor" rows="5" id="description" name="description" placeholder="job description" required><?php echo $description; ?></textarea>
				<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($descriptionErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>
            </div><?php if(isset($_POST['submit'])) echo (!empty($descriptionErr)) ? '<p class="col-sm-4 formError">'.$descriptionErr.'</p>':'';?>
          </div>
              
         <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($tagsErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="jobTags" >Job Tags:</label>
            <div class="col-sm-6">
               <input type="text" name="tags" id="jobTags" class="form-control"  value="<?php echo $tags; ?>" placeholder="comma separated tags" required>
				<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($tagsErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>
            </div><?php if(isset($_POST['submit'])) echo (!empty($tagsErr)) ? '<p class="col-sm-4 formError">'.$tagsErr.'</p>':'';?>
          </div>
              
             
              
        <div class="form-group">
            <label class="control-label col-sm-2" for="extras" >Extras:</label>
            <div class="col-sm-6">
               <textarea class="form-control" rows="5" id="extras" name="extras" placeholder="Apply Instruction" required><?php echo $extras; ?></textarea>
            </div>
          </div>
              
         <div class="form-group">
            <label class="control-label col-sm-2" for="companyLogo" >Job Expires in:</label>
            <div class="col-sm-3">
              <select class="form-control" id="date_ending" name="date_ending">
                    <option value="0" <?php echo ($expires==0)?'selected':''; ?>>1 week</option>
                    <option value="1" <?php echo ($expires==1)?'selected':''; ?>>2 weeks</option>
                    <option value="2" <?php echo ($expires==2)?'selected':''; ?>>3 weeks</option>
                    <option value="3" <?php echo ($expires==3)?'selected':''; ?>>1 month</option>
                    <option value="4" <?php echo ($expires==4)?'selected':''; ?>>2 months</option>
                    <option value="5" <?php echo ($expires==5)?'selected':''; ?>>1 year</option>
                    <option value="6" <?php echo ($expires==6)?'selected':''; ?>>2 years</option>
                </select>
             </div>         
          </div>      
              
        
              
              <button class="btn btn-md btn-primary col-md-offset-2" type="submit" name="submit" value="<?php echo isset($_GET['edit'])? 'edit' : 'add'; ?>"><?php echo isset($_GET['edit'])? 'Update' : 'Add'; ?> Job</button>
          </form>
        </div> <!-- /container -->

<?php
include_once('includes/footer.php');

?>