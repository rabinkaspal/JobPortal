<?php include_once('includes/top.php');

$emp_id = $_SESSION['emp_id'];

$job_id = (isset($_GET['job_id']) && !empty($_GET['job_id'])) ? $_GET['job_id'] : 0;


?>


      
    <div class="container">
		<h2 style="color:#999; margin-top:40px;">Applications</h2>
		<hr style="border-top:1px solid #ccc; margin-top:5px; margin-bottom:30px;">
       
       <?php if(!isset($_GET['job_id'])){ ?>
                   <table align="center"  class="table table-responsive table-striped table-bordered" style="max-width:800px;">
                   <thead>
                      <th>S.N</th>
                      <th>Job Title</th>
                      <th>Category</th>
                      <th style="text-align:center; max-width:100px;">No. of Applications</th>
                </thead>
             
             <?php
                  $i=0;
                   $q = "select * from jobs where employer_id={$emp_id}";
                   $r = mysql_query($q) or die(mysql_error());
            
                  while($row = mysql_fetch_array($r)){
                  ?>
                                
                  <tr>
                        <td align="center"><?php echo $i;?></td>
                  <td><a href="view_applications.php?job_id=<?php echo $row['job_id'];?>" target="_blank" style="display:block; width:100%; height:100%;"><?php echo $row['title'];?></a></td>
                        <td><?php echo $row['category'];?></td>
                  <?php
                        $thisJobId = $row['job_id'];
                        $count = mysql_result(mysql_query("select count(*) from jobseeker where applied_jobs like '%$thisJobId%'"),0);
                        echo '<td align="center">'.$count.'</td>';
                        ?>
            </tr>
               
                         <?php $i++; }?>
           </table>
          <?php }else{
            if($job_id == 0) header('Location:view_applications.php');
          ?>
          
          <?php 
            $date_posted = mysql_result(mysql_query("select date_posted from jobs where job_id={$job_id}"),0);
            $title = mysql_result(mysql_query("select title from jobs where job_id={$job_id}"),0);
      
          $a = "select * from jobseeker where applied_jobs like '%$job_id%'";
      
      $res = mysql_query($a) or die(mysql_error());
      
      if(mysql_num_rows($res)>0){
          ?>
            
      <h4><strong><?php echo $date_posted . " : " . $title ?></strong></h4>
          <table class="table table-responsive table-striped table-bordered">
                         <thead>
                            <th>Applicant's Name</th>
                            <th  style="text-align:center;">Accept</th>
                            <th style="text-align:center;">Decline</th>
                               <th  style="text-align:center;">Status</th>
                      </thead>
      <?php
      
      while($rows = mysql_fetch_array($res)){
      ?>
		
          
                   
                            
                  <tr>
                  <td><a class="btn" href="jobseeker.php?js_id=<?php echo $rows['jobseeker_id'];?>&job_id=<?php echo $job_id;?>" style="display:block; width:100%; height:100%; text-align:left;"><?php echo $rows['js_name'];?></a></td>
                  <td align="center"><a class="btn" href="accept_application.php?job_id=<?php echo $job_id;?>&js_id=<?php echo $rows['jobseeker_id'];?>" style="display:block; width:100%; height:100%;">Accept</a></td>
                  <td align="center"><a class="btn" href="decline_application.php?job_id=<?php echo $job_id;?>&js_id=<?php echo $rows['jobseeker_id'];?>" style="display:block; width:100%; height:100%;">Decline</a></td>
                        
      <?php    
      $status_query = "select status from job_acceptance where job_id={$job_id} and js_id={$rows['jobseeker_id']}";
      $status_result = mysql_query($status_query) or die(mysql_error());
      
                if($res){
      $result_row=mysql_fetch_array($status_result);
            $status = $result_row[0];
      }
          if($status==""){
                $statusMsg = "Pending";
              echo '<td align="center"><button type="button" class="btn btn-primary">'.$statusMsg,'</button></td>';    
            }
                else if($status==0){
                      $statusMsg = "Declined";
                       echo '<td align="center"><button type="button" class="btn btn-danger">'.$statusMsg,'</button></td>';              }
                else if($status==1){
                      $statusMsg = "Accepted";
                 echo '<td align="center"><button type="button" class="btn btn-success ">'.$statusMsg,'</button></td>'; }
      
           ?>
                        
                        
                  </tr>
                        
                <?php }}else{ echo "No applications yet.";} ?>
                </table>
	     <hr style="border-top:1px solid #ccc; margin-top:5px; margin-bottom:30px;">  
          <?php } ?>
          
        </div> <!-- /container -->
<?php include_once('includes/footer.php');?>