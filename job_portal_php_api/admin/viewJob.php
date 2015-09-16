<?php include_once('includes/top.php');

$emp_id = $_SESSION['emp_id'];
?>


      
    <div class="container"><br><br>
		<div class="well">
		
			
			<?php 
			if(isset($_GET['id']) && !empty($_GET['id']))
				$id = $_GET['id'];


		$q = "select * from jobs where job_id={$id}";
		$r = mysql_query($q);
	$ending_date="";
		while($row = mysql_fetch_array($r)){
			
			switch($row['date_ending']){
					case 0: $ending_date="1 week";
					break;
					case 1: $ending_date="2 weeks";
					break;
					case 2: $ending_date="3 weeks";
					break;
					case 3: $ending_date="1 month";
					break;
					case 4: $ending_date="2 months";
					break;  
					case 5: $ending_date="1 year";
					break;
					case 6: $ending_date="2 years";
					break;
										

			}
			
			?>
		
			<h3><?php echo $row['title']; ?></h3>
			<p><?php echo mysql_result(mysql_query("select company_name from employers where emp_id={$emp_id}"),0); ?></p>
			<div>
				<strong>Location:</strong> <?php echo $row['location']; ?> <br>
				<strong>Category:</strong> <?php echo $row['category']; ?> <br>
				<strong>Salary:</strong> Rs.<?php echo $row['salary']; ?> per month<br>
				<strong>Experience:</strong> <?php echo $row['experience']; ?>+ years<br>
				<strong>Key Skills:</strong> <?php echo $row['key_skills']; ?><br>
				<strong>Qualifications:</strong> <?php echo $row['qualifications']; ?><br>
				<strong>Description:</strong> <?php echo $row['body']; ?><br>
				<strong>Tags:</strong> <?php echo $row['tags']; ?><br>
				<strong>Extras:</strong> <?php echo $row['extra']; ?><br>
				<strong>Posted on:</strong> <?php echo $row['date_posted']; ?><br>
				<strong>Ends in:</strong> <?php echo $ending_date; ?>  from posted date. <br>

			</div>
		<?php } ?>
		
		</div>
	</div> <!-- /container -->
<?php include_once('includes/footer.php');?>