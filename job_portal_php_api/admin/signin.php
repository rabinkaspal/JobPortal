<?php
ob_start();
session_start();
include_once('../includes/db_connect.php');
	

$loggedOutMsg = "";

if(isset($_GET['logged_out']) && $_GET['logged_out']== 1){
	$loggedOutMsg = "You are now logged out.";
}

$signinErr="";
$emailErr="";
$passwordErr="";

$email="";
$password="";

if(isset($_POST['submit'])){
	
	$email = $_POST['email'];
	$password=$_POST['password'];
	
	
	if (empty($password)) {
			 $passwordErr = "Password is required";
		  }else{
			if(strlen($password)<6){
				$passwordErr = "Password must be at least 6 characters long.";
			}
		}
	
		if (empty($email)) {
		 $emailErr = "Email is required";
	   }else {
			// check if e-mail address is well-formed
		 if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
		   $emailErr = "Invalid email format";
		 }
	   }
	
	if(strlen($emailErr)==0 && strlen($passwordErr)==0){
	
		
		$rowCount = mysql_result(mysql_query('select count(*) from employers where email="'.$email.'" and password="'. md5($password).'"'),0);
		
	if($rowCount==1){
		$emp_id = mysql_result(mysql_query('select emp_id from employers where email="'.$email.'"'),0);
		$_SESSION['loggedIn'] = true;
		$_SESSION['emp_id'] = $emp_id;
		$_SESSION['emp_email'] = $email;

		header('Location:dashboard.php?setsession=1');
		exit(0);

		}else{
			//echo '<br/><br/><br/>incorrect email password';
			$signinErr = "Incorrect Email/Password combination.";
		}
	}
		
}
//echo '<br/><br/><br/>'.$emailErr.'<br/>'.$passwordErr;
?>


<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- the above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Sign In</title>

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
        
        <?php  if(isset($_GET['logged_out']) && $_GET['logged_out']== 1){?>
        <script>logOut();</script>
        <?php }?>
    
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
          <form class="form-signin" method="post" action="signin.php">
            <h2 class="form-signin-heading">Sign In</h2>
			  <?php echo (!empty($loggedOutMsg)) ? '<p class="success">'.$loggedOutMsg.'</p>':''; ?>
			  <?php echo (!empty($signinErr)) ? '<p class="failure">'.$signinErr.'</p>':''; ?>
			  <?php echo (!empty($emailErr)) ? '<p class="failure">'.$emailErr.'</p>':''; ?>
			  <?php echo (!empty($passwordErr)) ? '<p class="failure">'.$passwordErr.'</p>':''; ?>
			  
	 
			  
            <label for="inputEmail" class="sr-only">Email address</label>
            <input type="email" id="email" name="email" class="form-control" placeholder="Email address" required autofocus>
           
			  
			<label for="inputPassword" class="sr-only">Password</label>
            <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>

			  
			  <button class="btn btn-lg btn-primary btn-block" type="submit" name="submit">Sign in</button>
          </form>
        </div> <!-- /container -->

      <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>