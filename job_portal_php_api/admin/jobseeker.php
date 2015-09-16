<?php
 include_once('includes/top.php');

if(isset($_GET['js_id']) && !empty($_GET['js_id'])&& isset($_GET['job_id']) && !empty($_GET['job_id'])){
      $js_id = $_GET['js_id'];
      $job_id = $_GET['job_id'];
}else{
      header('Location: view_applications.php');
}
?>
  <div class="container"><br><br>
		<div class="well">
		
			
			<?php 
			if(isset($_GET['id']) && !empty($_GET['id']))
				$id = $_GET['id'];


              $q = "select * from jobseeker where jobseeker_id={$js_id}";
              $r = mysql_query($q);
                  while($row = mysql_fetch_array($r)){

             ?>


            <h2 style="margin-bottom:2px;"><?php echo ucfirst($row['js_name']); ?></h2>
              <?php echo $row['email']; ?> <br>
              <?php echo $row['address']; ?> <br>
              <?php echo $row['phone']; ?> <br><br>
			
			<div>
           <?php $emp_json = $row['previous_jobs'];
                  $emp = json_decode($emp_json, true);?>
                  <h3>Employments</h3>
                  <strong><?php echo $emp['company']; ?></strong> <br>
				<?php echo $emp['position']; ?> <br>
                  <?php echo $emp['fromYear']." ".$emp['fromMonth']." to Present"; ?> <br><br>
            </div>
			
			<div>
                   <?php $education_json = $row['educational_degrees'];
                  $edu = json_decode($education_json, true);?>
                  
                <h3>Educational Qualifications</h3>
                  <p>Graduation</p>
				<strong>Course:</strong> <?php echo $edu['course1']; ?> <br>
				<strong>Passed:</strong> <?php echo $edu['year1']." ".$edu['month1']; ?> <br>
				<strong>Institution:</strong> <?php echo $edu['institution1']; ?> <br><br>
                  
                    <p>Post Graduation</p>
				<strong>Course:</strong> <?php echo $edu['course2']; ?> <br>
				<strong>Passed:</strong> <?php echo $edu['year2']." ".$edu['month2']; ?> <br>
				<strong>Institution:</strong> <?php echo $edu['institution2']; ?> <br><br>
            </div>
              
		    <?php } ?>
              <div class="col-md-6">
                    <div class="col-md-6">
                        <a class="btn btn-success" href="accept_application.php?job_id=<?php echo $job_id;?>&js_id=<?php echo $js_id;?>">Accept Application</a>                        </div>
                    <div class="col-md-6">
                    <a class="btn btn-danger" href="decline_application.php?job_id=<?php echo $job_id;?>&js_id=<?php echo $js_id;?>">Decline Application</a>
                    </div>
              </div>
              <div class="col-md-6">
                  
              </div>
		<div class="row">
		</div>
	</div> <!-- /container -->
<?php include_once('includes/footer.php');?>