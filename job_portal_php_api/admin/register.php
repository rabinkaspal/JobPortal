<?php ini_set("memory_limit", "200000000"); // for large images so that we do not get "Allowed memory exhausted"
ob_start();
session_start();

?>
<?php include_once('../includes/db_connect.php');?> 
<?php
$companyName=$address=$phone=$industry=$ownership=$numEmployees=$url=$email=$password=$confirmPassword=$companyInfo=$instruction="";

$companyNameErr=$addressErr=$phoneErr=$ownershipErr=$numEmployeesErr=$urlErr=$emailErr=$passwordErr=$confirmPasswordErr=$companyInfoErr=$companyLogoErr="";

	if(isset($_POST['submit'])){
		$companyName= test_input($_POST['companyName']);
		$address= test_input($_POST['address']);
		$phone= test_input($_POST['phone']);
		$industry= test_input($_POST['industry']);
		$ownership= test_input($_POST['ownership']);
		$numEmployees= test_input($_POST['numEmployees']);
		$url= test_input($_POST['url']);
		$email= test_input($_POST['email']);
		$password= test_input($_POST['password']);
		$confirmPassword= test_input($_POST['confirmPassword']);
		$companyInfo= test_input($_POST['companyInfo']);
		$instruction= test_input($_POST['instruction']);
		
		$companyLogo = $_FILES["companyLogo"];
		
		 if (empty($companyName)) {
			 $companyNameErr = "Company name is required";
		   } elseif(strlen($companyName) < 5){
				$companyNameErr = "Company name is too short.";
			}else {
			 // check if name only contains letters and whitespace
			 if (!preg_match("/^[a-zA-Z. ]*$/",$companyName)) {
			   $companyNameErr = "Only letters and white space allowed";
			 }
		   }
		
		if (empty($address)) {
			 $addressErr = "Address is required";
		   }
		
		if(empty($phone)){
				$phoneErr = "phone is Required.";
			}else if(strlen($phone)>10 || strlen($phone)<7){
				$phoneErr = "Enter valid phone number.";
			}else{
				if(!preg_match('/^[1-9][0-9]*$/', $phone)){
					$phoneErr = "Only numeric phone are allowed.";
				}
				
			}
		
		if(empty($numEmployees)){
				$numEmployeesErr = "Number of Employees is Required.";
			}else{
				if(!preg_match('/^[1-9][0-9]*$/', $numEmployees)){
					$numEmployeesErr = "Only numbers are allowed.";
				}
				
			}
		
		if (empty($url)) {
			 $urlErr = "URL is required.";
		   } else {
			 // check if URL address syntax is valid (this regular expression also allows dashes in the URL)
			 if (!preg_match("/\b(?:(?:https?|ftp):\/\/|www\.)[-a-z0-9+&@#\/%?=~_|!:,.;]*[-a-z0-9+&@#\/%=~_|]/i",$url)){
			   $urlErr = "Invalid URL";
			 }
		   }
		
		
		
		if (empty($email)) {
		 $emailErr = "Email is required";
	   }else if(!empty($email)){
			$countEmp = mysql_result(mysql_query("select count(emp_id) from employers where email='$email'"),0);
			if($countEmp>0){
				$emailErr = "Email already exists.";
			}
			
		}else {
		 // check if e-mail address is well-formed
		 if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
		   $emailErr = "Invalid email format";
		 }
	   }
		
		if (empty($password)) {
			 $passwordErr = "Password is required";
		  }else{
			if(strlen($password)<6){
				$passwordErr = "Password must be at least 6 characters long.";
			}
		}
		
		if(empty($confirmPassword)){
			$confirmPasswordErr = "Enter password again.";
		}else{
			if(strcmp($password,$confirmPassword)!=0){
				$confirmPasswordErr = "Passwords do not match.";
			}
		}
		
		if (empty($companyInfo)) {
			 $companyInfoErr = "Company Info is required";
		   }
	
		if(empty($companyLogo)){
			 $companyLogoErr = "Company Logo is required";
		}
		
function hasNoErrors(){
	global $companyNameErr, $addressErr, $phoneErr, $ownershipErr, $numEmployeesErr,
			$urlErr, $emailErr, $passwordErr, $confirmPasswordErr, $companyInfoErr;
	
			if(strlen($companyNameErr)==0 && strlen($addressErr)==0 && strlen($phoneErr)==0 && strlen($ownershipErr)==0 && strlen($numEmployeesErr)==0 && strlen($urlErr)==0 && strlen($emailErr)==0 && strlen($passwordErr)==0 && strlen($confirmPasswordErr)==0 && strlen($companyInfoErr)==0){
			return true;
		}else{
			return false;
		}
	}
		
		
if (($_FILES["companyLogo"]["type"] == "image/jpeg" || $_FILES["companyLogo"]["type"] == "image/pjpeg" || $_FILES["companyLogo"]["type"] == "image/gif" || $_FILES["companyLogo"]["type"] == "image/png") && ($_FILES["companyLogo"]["size"] < 4000000))
	{
		
		// some settings: how big image can user upload
		$max_upload_width = 200;
		$max_upload_height = 200;
		  
			
		// if uploaded image was JPG/JPEG
		if($_FILES["companyLogo"]["type"] == "image/jpeg" || $_FILES["companyLogo"]["type"] == "image/pjpeg"){	
			$image_source = imagecreatefromjpeg($_FILES["companyLogo"]["tmp_name"]);
		}		
		// if uploaded image was GIF
		if($_FILES["companyLogo"]["type"] == "image/gif"){	
			$image_source = imagecreatefromgif($_FILES["companyLogo"]["tmp_name"]);
		}	
		// BMP doesn't seem to be supported so remove it form above image type test (reject bmps)	
		// if uploaded image was BMP
		if($_FILES["companyLogo"]["type"] == "image/bmp"){	
			$image_source = imagecreatefromwbmp($_FILES["companyLogo"]["tmp_name"]);
		}			
		// if uploaded image was PNG
		if($_FILES["companyLogo"]["type"] == "image/png"){
			$image_source = imagecreatefrompng($_FILES["companyLogo"]["tmp_name"]);
		}
		

		$remote_file = "uploaded_images/".$companyName.'_'.md5(date('i s')).'.jpg';//$_FILES["companyLogo"]["name"];
		imagejpeg($image_source,$remote_file,100);
		chmod($remote_file,0644);
	
	

		// get width and height of original image
		list($image_width, $image_height) = getimagesize($remote_file);
	
		if($image_width>$max_upload_width || $image_height >$max_upload_height){
			$companyLogoErr = "file dimension error.";
			unlink($remote_file);
		}
	
		
			
		if(empty($companyLogoErr) && hasNoErrors()){
//			$proportions = $image_width/$image_height;
//			
//			if($image_width>$image_height){
//				$new_width = $max_upload_width;
//				$new_height = round($max_upload_width/$proportions);
//			}		
//			else{
//				$new_height = $max_upload_height;
//				$new_width = round($max_upload_height*$proportions);
//			}		
			
			echo 'asdfasdfasdfasdf';
			$new_width = 150;
			$new_height = 120;
			
			$new_image = imagecreatetruecolor($new_width , $new_height);
			$image_source = imagecreatefromjpeg($remote_file);
			
			
			
			
			imagecopyresampled($new_image, $image_source, 0, 0, 0, 0, $new_width, $new_height, $image_width, $image_height);
			imagejpeg($new_image,$remote_file,100);
			
			imagedestroy($new_image);
			imagedestroy($image_source);
		
		
		//image upload ok,, form contents ok... insert into database now

			$industry_name = mysql_result(mysql_query('select cat_name from categories where cat_id='.$industry),0);

			$q = "insert into employers(cat_id,email,industry,ownership,num_employee,password,
				company_name,address,phone,url,logo,company_info,instruction)
				values('$industry','$email','$industry_name','$ownership','$numEmployees','". md5($password)."',
				'$companyName','$address','$phone','$url',\"$remote_file\",'$companyInfo','$instruction')";

			$r = mysql_query($q) or die('<br/><br/><br/><br/>'.mysql_error());

			$id = mysql_insert_id();
			if(mysql_affected_rows()==1){
				
				$_SESSION['loggedIn'] = true;
				$_SESSION['emp_id'] = $id;
				$_SESSION['emp_email'] = $email;
				
				ob_flush();
				header('Location:add_jobs.php');
				exit(0);


			}
		
		} else{unlink($remote_file);}
		}
		
//		header("Location: image_upload.php?upload_message=image uploaded&upload_message_type=success&show_image=".$_FILES["image_upload_box"]["name"]);
//		exit;
	else{
		$companyLogoErr="file type error. jpg png gif allowed.";
		//header("Location: image_upload.php?upload_message=make sure the file is jpg, gif or png and that is smaller than 4MB&upload_message_type=error");
		//exit;
	}
}
		
		



//echo $companyNameErr.'<br/>'. $addressErr.'<br/>'.$phoneErr.'<br/>'.$ownershipErr.'<br/>'.$numEmployeesErr.'<br/>'.$urlErr.'<br/>'.$emailErr.'<br/>'.$passwordErr.'<br/>'.$confirmPasswordErr.'<br/>'.$companyInfoErr.'<br/>'.$companyLogoErr;

	
	
function test_input($data){
   $data = trim($data);
   $data = stripslashes($data);
   $data = htmlspecialchars($data);
   return $data;
}


?>



<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- the above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Register</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
	  <link href="css/style_admin.css" rel="stylesheet">
	   <script src="js/style_admin.js"></script>
    </head>
  <body>
    
      <nav class="navbar navbar-inverse navbar-fixed-top">
         <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Job Finder</a>
        </div>
          <div id="navbar" class="navbar-collapse collapse">
              <ul class="nav navbar-nav">
                  <li><a href="register.php">Register</a></li>
                  <li><a href="signin.php">Sign In</a></li>
              </ul>
      <ul class=" nav navbar-nav navbar-right">    
	   <li><a href="#" onclick="AndroidGoBack()"><span class="glyphicon glyphicon-off"></span>&nbsp;&nbsp;Exit</a></li>
      </ul>

		  </div>

	  
	  </nav>
		  
		  
		  
		  
	 
      
    <div class="container">
          <form class="form-register form-horizontal" role="form" method="post" action="register.php" enctype="multipart/form-data">
            <h2 class="form-signin-heading">Register</h2>
              <hr style="border-top:1px solid #ccc; margin-bottom:30px;">
        
        <h4 class="form-signin-heading" style="color:#333399;"><strong>Company Details</strong></h4>
              <hr style="border-top:1px solid #ccc; margin-top:10px; margin-bottom:20px;">
              
          <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($companyNameErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="companyName" >Company Name:</label>
            <div class="col-sm-4">
              <input type="text" name="companyName" class="form-control" value="<?php echo $companyName; ?>" placeholder="Company Name" required autofocus>
            <?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($companyNameErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>
			  </div>
			  <?php if(isset($_POST['submit'])) echo (!empty($companyNameErr)) ? '<p class="col-sm-4 formError">'.$companyNameErr.'</p>':'';?>
          </div>
              
         <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($addressErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="address" >Address:</label>
            <div class="col-sm-4">
              <input type="text" name="address" class="form-control"  value="<?php echo $address; ?>"placeholder="Address" required>
           <?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($addressErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>
			 </div>
			  <?php if(isset($_POST['submit'])) echo (!empty($addressErr)) ? '<p class="col-sm-4 formError">'.$addressErr.'</p>':'';?>
          </div>
              
        <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($phoneErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="phone" >Phone:</label>
            <div class="col-sm-4">
              <input type="text" name="phone" class="form-control"  value="<?php echo $phone; ?>"placeholder="Phone number" required>
				<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($phoneErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>
            </div>
			 <?php if(isset($_POST['submit'])) echo (!empty($phoneErr)) ? '<p class="col-sm-4 formError">'.$phoneErr.'</p>':'';?>
          </div>
              
         <div class="form-group">
            <label class="control-label col-sm-2" for="industry" >Industry:</label>
            <div class="col-sm-4">
                 <select class="form-control" name="industry">
                    <?php 
                       $q = "select * from categories order by cat_name";
                       $r = mysql_query($q);

                    if(mysql_num_rows($r)>0){
                     while($row = mysql_fetch_array($r)){
                      echo '<option value="'.$row['cat_id'].' "'; echo($industry==$row['cat_id']) ? 'selected' : ''; echo '>'.$row['cat_name'].'</option>';
                     }}
                  ?>
                  </select>
            </div>
          </div>
              
        <div class="form-group">
            <label class="control-label col-sm-2" for="ownership" >Ownership:</label>
            <div class="col-sm-4">
                 <select class="form-control" name="ownership">
                    <option value="Private" <?php ($ownership=="Private")?'selected':'';?>>Private</option>
                    <option value="Public"<?php ($ownership=="Public")?'selected':'';?>>Public</option>
                    <option value="Government"<?php ($ownership=="Government")?'selected':'';?>>Government</option>
                  </select>
            </div>
          </div>
              
        <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($numEmployeesErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="numEmployees" >Number of Employees:</label>
            <div class="col-sm-4">
              <input type="text" name="numEmployees" class="form-control" value="<?php echo $numEmployees; ?>"placeholder="Current number of employees" required>
			<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($numEmployeesErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>

			</div>
			 <?php if(isset($_POST['submit'])) echo (!empty($numEmployeesErr)) ? '<p class="col-sm-4 formError">'.$numEmployeesErr.'</p>':'';?>
          </div>
              
         <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($urlErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="url" >URL:</label>
            <div class="col-sm-4">
              <input type="url" name="url" class="form-control"  value="<?php echo $url; ?>"placeholder="Company url" required>
		<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($urlErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>

            </div>
			 <?php if(isset($_POST['submit'])) echo (!empty($urlErr)) ? '<p class="col-sm-4 formError">'.$urlErr.'</p>':'';?>
          </div>
              
        <hr style="border-top:1px solid #ccc; margin-top:10px; margin-bottom:20px;">
        <h4 class="form-signin-heading"  style="color:#333399;"><strong>Company Sign In Info</strong></h4>
        <hr style="border-top:1px solid #ccc; margin-top:10px; margin-bottom:20px;">
              
        <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($emailErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="email" >Email:</label>
            <div class="col-sm-4">
              <input type="email" name="email" class="form-control"  value="<?php echo $email; ?>"placeholder="Primary Email" required>
		<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($emailErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>

            </div>
			<?php if(isset($_POST['submit'])) echo (!empty($emailErr)) ? '<p class="col-sm-4 formError">'.$emailErr.'</p>':'';?>
          </div>
              
          <div class="form-group  <?php if(isset($_POST['submit'])) echo (!empty($passwordErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="password" >Password:</label>
            <div class="col-sm-4">
              <input type="password" name="password" class="form-control" value="<?php echo $password;?>"
																				 placeholder="Password" required>
		<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($passwordErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>

			  </div>
			  <?php if(isset($_POST['submit'])) echo (!empty($passwordErr)) ? '<p class="col-sm-4 formError">'.$passwordErr.'</p>':'';?>
          </div>
              
         <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($confirmPasswordErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="confirmPassword" >Confirm Password:</label>
            <div class="col-sm-4">
              <input type="password" name="confirmPassword" class="form-control" value="<?php echo $confirmPassword;?>" placeholder="Confirm Password" required>
			<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($confirmPasswordErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>

			 </div>
			 <?php if(isset($_POST['submit'])) echo (!empty($confirmPasswordErr)) ? '<p class="col-sm-4 formError">'.$confirmPasswordErr.'</p>':'';?>
          </div>
              
              
        <hr style="border-top:1px solid #ccc; margin-top:10px; margin-bottom:20px;">
        <h4 class="form-signin-heading" style="color:#333399;" ><strong>More Details</strong></h4>
        <hr style="border-top:1px solid #ccc; margin-top:10px; margin-bottom:20px;">
              
        <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($companyInfoErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="companyInfo" >Company Info:</label>
            <div class="col-sm-6">
               <textarea class="form-control" rows="5" name="companyInfo" placeholder="Company Info" required>
				   <?php echo $companyInfo; ?>
				</textarea>
			<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($companyInfoErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>

			</div>
			 <?php if(isset($_POST['submit'])) echo (!empty($companyInfoErr)) ? '<p class="col-sm-4 formError">'.$companyInfoErr.'</p>':'';?>
          </div>
              
         <div class="form-group">
            <label class="control-label col-sm-2" for="instruction" >Apply Instruction:</label>
            <div class="col-sm-6">
               <textarea class="form-control" rows="5" name="instruction" placeholder="Apply Instruction">
				<?php echo $instruction; ?>
				</textarea>
            </div>
          </div>
              
         <div class="form-group <?php if(isset($_POST['submit'])) echo (!empty($companyLogoErr)) ? 'has-error' : 'has-success'; ?>">
            <label class="control-label col-sm-2" for="companyLogo" >Upload Logo:</label>
            <div class="col-sm-4">
              <input type="file" name="companyLogo" class="form-control" required>
                <p class="help-block">Logo size must not be more that 4MB.(150X150)</p>
             
		<?php if(isset($_POST['submit'])) { echo '<span class="glyphicon ';if(!empty($companyLogoErr)) {echo 'glyphicon-remove';}else {echo 'glyphicon-ok';} echo ' form-control-feedback"></span>';} ?>

			 </div>
			 <?php if(isset($_POST['submit'])) echo (!empty($companyLogoErr)) ? '<p class="col-sm-4 formError">'.$companyLogoErr.'</p>':'';?>
          </div>      
              
              
              
              <button class="btn btn-md btn-primary col-md-offset-2" type="submit" name="submit">Register Now</button>
          </form>
        </div> <!-- /container -->

      
      <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>