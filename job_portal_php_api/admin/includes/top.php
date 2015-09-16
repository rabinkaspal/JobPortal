 <?php 
   ob_start();
	session_start();
   include_once('../includes/db_connect.php');
   include_once('includes/functions.php');

if(isset($_GET['logout']) && $_GET['logout']==1){
	logout_employer();
}

checkLogin();

   ?>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- the above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Job Finder</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
	  
	    <script src="ckeditor/ckeditor.js"></script>
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
                    <li><a href="dashboard.php"><strong>Dashboard</strong></a></li>
                  <li><a href="add_jobs.php"><strong>Add Job</strong></a></li>
                  <li><a href="#<?php echo $_SESSION['emp_id'];?>"><strong>Profile</strong></a></li>
                  <li><a href="view_applications.php"><strong>Applications</strong></a></li>
              </ul>
			  
			             
                      
              
              
              <ul class=" nav navbar-nav navbar-right">
                     <li><a href="<?php echo $_SERVER['PHP_SELF'].'?logout=1';?>"><strong>Logout</strong></a></li>
                  <li class="dropdown"><a  class="dropdown-toggle" data-toggle="dropdown"  href="#"><?php echo $_SESSION['emp_email'];?><span class="caret"></span></a>
                   <ul class="dropdown-menu dropdown">
                    <li><a href="#"><span class="glyphicon glyphicon-retweet"></span> Edit Profile</a></li>
                       <li><a href="<?php echo $_SERVER['PHP_SELF'].'?logout=1';?>"><span class="glyphicon glyphicon-off"></span> Logout</a></li>
                  </ul>
                  </li>
				   <li><a href="#" onclick="AndroidGoBack(<?php echo (isLoggedIn())? 1:0; ?>)"><span class="glyphicon glyphicon-off"></span>&nbsp;&nbsp;Exit</a></li>
              </ul>
          </div>
          
      </nav>