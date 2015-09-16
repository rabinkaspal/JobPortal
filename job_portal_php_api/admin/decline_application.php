<?php 
include_once('includes/top.php');

$msgErr = "";
if(isset($_GET['job_id']) && !empty($_GET['job_id']) && isset($_GET['js_id']) && !empty($_GET['js_id']) ){
      $job_id = $_GET['job_id'];
      $js_id = $_GET['js_id'];
      
      if(isset($_GET['override'])){
            $override=1;
      }else{
            $override=0;
      }
     
      
      
      if(isset($_GET['submit']) && $_GET['submit']==1){
            $message = $_GET['message'];
            if(empty($message)){
                  $msgErr = "message cannot be empty.";
            }else{
                  
                  if($override==0){
                  $q = "insert into job_acceptance(job_id,js_id,status,message)
                        values({$job_id}, {$js_id}, 0, '{$message}')";
                  }else{
                  $q = "update job_acceptance set message='{$message}', status=0
                        where job_id={$job_id} and js_id={$js_id}";
                  }
                  $result = mysql_query($q) or die("<br/>".mysql_error());
                  if($result){
                        header('Location: view_applications.php?job_id='.$job_id);
                  }
                  
            }
      }
      
      
}else{
      header('Location: view_applications.php');
}
?>

   
    <div class="container">
		<h2 style="color:#999; margin-top:40px;">Decline Application</h2>
		<hr style="border-top:1px solid #ccc; margin-top:5px; margin-bottom:30px;">
        
 <?php 
 $q = "select status from job_acceptance where job_id={$job_id} and js_id={$js_id}";
      $res = mysql_query($q) or die(mysql_error());
      
       $date_posted = mysql_result(mysql_query("select date_posted from jobs where job_id={$job_id}"),0);
      $title = mysql_result(mysql_query("select title from jobs where job_id={$job_id}"),0);

      echo '<h3>'.$date_posted . ' : ' . $title.'</h3>';
      echo '<hr style="border-top:1px solid #ccc; margin-top:5px; margin-bottom:30px;">';


      if($res){
      $row=mysql_fetch_array($res);
            $status = $row[0];
      }
if($status=="" || $override==1){ 
       echo '
		<form role=form method="get" action="decline_application.php">';
            if($override==1) echo '<input type="hidden" name="override" value="1">';
            echo'<input type="hidden" name="job_id" value="'.$job_id.'">
              <input type="hidden" name="js_id" value="'.$js_id.'">
             <div class="row" style="width:450px; margin-left:0px;">';
       echo (!empty($msgErr))? $msgErr:""; 
       echo'<textarea class="form-control" cols="50" rows="6" name="message"></textarea><br/>
      <button type="submit" name="submit" value="1" class="btn btn-danger">Decline Application</button>
              
              </div>
          </form>';
            }
                else{
                  if($status==0){
                        echo '<p class="failure">The application from this candidate has been already declined.';
                      echo '<a style="margin-left:10%;" class="btn btn-success" href="accept_application.php?job_id='.$job_id.'&js_id='.$js_id.'&override=1">Accept Application</a></p>';  
                  }
                  else if($status==1){
                  echo '<p class="success">The application from this candidate has been already accepted.'; 
                  echo '<a style="margin-left:10%;" class="btn btn-danger" href="decline_application.php?job_id='.$job_id.'&js_id='.$js_id.'&override=1">Decline Application</a></p>';
                  }
                }
                ?>  
          
        </div> <!-- /container -->
<?php include_once('includes/footer.php');?>


