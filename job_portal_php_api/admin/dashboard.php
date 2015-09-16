<?php include_once('includes/top.php');

$emp_id = $_SESSION['emp_id'];

if(isset($_GET['setsession'])){
      $s=1;
}else{$s=2;}


if($s==1){
?>

<script>
      
     // onload = alert('asdfas');
     setSession(); 
      
</script>
<?php }?>
      
    <div class="container">
		<h2 style="color:#999; margin-top:40px;">Posts</h2>
		<hr style="border-top:1px solid #ccc; margin-top:5px; margin-bottom:30px;">
        
		<form role=form method="get" action="deleteJob.php">
			<input type="hidden" value="" name="chkid[]"/>
			<table style="margin-top:40px;" class="table table-bordered table-striped table-responsive">
			<tr>
				<th>S.N</th>
				<th>Job Title</th>
				<th>Category</th>
				<th>Posted Date</th>
				<th>Check</th>
				<th>Action</th>
                  <th style="text-align:center;">No.</th>
                 <th style="text-align:center;">Applications</th>
			</tr>
				
				<?php 
				$i=1;
				$q = "select * from jobs where employer_id={$emp_id} order by job_id desc";
				$r = mysql_query($q) or die(mysql_error());
				while($row = mysql_fetch_array($r)){
				echo '
				<tr>
					<td style="vertical-align:middle;">'.$i.'</td>
					<td style="vertical-align:middle;"><a href="viewJob.php?id='.$row['job_id'].'">'.$row['title'].'</a></td>
					<td style="vertical-align:middle;">'.$row['category'].'</td>
					<td align="center" style="vertical-align:middle;">'.$row['date_posted'].'</td>
					<td align="center" style="vertical-align:middle;"><input type="checkbox" name="chkid[]" value="'.$row['job_id'].'"/></td>
					<td><a href="add_jobs.php?edit=1&job_id='.$row['job_id'].'">Edit</a> &nbsp;&nbsp; / &nbsp;&nbsp; <a href="deleteJob.php?id='.$row['job_id'].'">Delete</a></td>';
                    
                      $thisJobId = $row['job_id'];
                        $count = mysql_result(mysql_query("select count(*) from jobseeker where applied_jobs like '%$thisJobId%'"),0);
                        echo '<td align="center" style="vertical-align:middle;">'.$count.'</td>';
                    
                   echo '<td align="center"  style="vertical-align:middle;"><a target="_blank" href="view_applications.php?job_id='.$row['job_id'].'" class="btn-block">View</a></td>
				</tr>
				
				';
				$i++;
				}
					
				
				?>
							
          </table>
			<button type="submit" name="submit" value="submit" class="btn-link">Delete Checked</button>
		</form>
        </div> <!-- /container -->
<?php include_once('includes/footer.php');?>