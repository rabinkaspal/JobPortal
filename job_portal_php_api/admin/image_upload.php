<?php ini_set("memory_limit", "200000000"); // for large images so that we do not get "Allowed memory exhausted"?>
<?php

$fileErr = "";
// upload the file
if ((isset($_POST["submitted_form"])) && ($_POST["submitted_form"] == "image_upload_form")) {
	
	// file needs to be jpg,gif,bmp,x-png and 4 MB max
if (($_FILES["image_upload_box"]["type"] == "image/jpeg" || $_FILES["image_upload_box"]["type"] == "image/pjpeg" || $_FILES["image_upload_box"]["type"] == "image/gif" || $_FILES["image_upload_box"]["type"] == "image/png") && ($_FILES["image_upload_box"]["size"] < 4000000))
	{
		
		// some settings: how big image can user upload
		$max_upload_width = 200;
		$max_upload_height = 200;
		  
			
		// if uploaded image was JPG/JPEG
		if($_FILES["image_upload_box"]["type"] == "image/jpeg" || $_FILES["image_upload_box"]["type"] == "image/pjpeg"){	
			$image_source = imagecreatefromjpeg($_FILES["image_upload_box"]["tmp_name"]);
		}		
		// if uploaded image was GIF
		if($_FILES["image_upload_box"]["type"] == "image/gif"){	
			$image_source = imagecreatefromgif($_FILES["image_upload_box"]["tmp_name"]);
		}	
		// BMP doesn't seem to be supported so remove it form above image type test (reject bmps)	
		// if uploaded image was BMP
		if($_FILES["image_upload_box"]["type"] == "image/bmp"){	
			$image_source = imagecreatefromwbmp($_FILES["image_upload_box"]["tmp_name"]);
		}			
		// if uploaded image was PNG
		if($_FILES["image_upload_box"]["type"] == "image/png"){
			$image_source = imagecreatefrompng($_FILES["image_upload_box"]["tmp_name"]);
		}
		

		$remote_file = "uploaded_images/".$_FILES["image_upload_box"]["name"];
		imagejpeg($image_source,$remote_file,100);
		chmod($remote_file,0644);
	
	

		// get width and height of original image
		list($image_width, $image_height) = getimagesize($remote_file);
		
		if($image_width >$max_upload_width || $image_height >$max_upload_height){
			$fileErr = "file dimension Err";
		}else{
	
		if($image_width>$max_upload_width || $image_height >$max_upload_height){
			$proportions = $image_width/$image_height;
			
			if($image_width>$image_height){
				$new_width = $max_upload_width;
				$new_height = round($max_upload_width/$proportions);
			}		
			else{
				$new_height = $max_upload_height;
				$new_width = round($max_upload_height*$proportions);
			}		
		$new_width = 150;
		$new_height = 120;
			
			$new_image = imagecreatetruecolor($new_width , $new_height);
			$image_source = imagecreatefromjpeg($remote_file);
			
			
			
			
			imagecopyresampled($new_image, $image_source, 0, 0, 0, 0, $new_width, $new_height, $image_width, $image_height);
			imagejpeg($new_image,$remote_file,100);
			
			imagedestroy($new_image);
		
		
		imagedestroy($image_source);
		} 
		}
		
		header("Location: image_upload.php?upload_message=image uploaded&upload_message_type=success&show_image=".$_FILES["image_upload_box"]["name"]);
		exit;
	}
	else{
		$fileErr="file type error. jpg png gif allowed.";
		//header("Location: image_upload.php?upload_message=make sure the file is jpg, gif or png and that is smaller than 4MB&upload_message_type=error");
		//exit;
	}
}
?>

<?php if(isset($_REQUEST['upload_message'])){?>
	<div class="upload_message_<?php echo $_REQUEST['upload_message_type'];?>">
	<?php echo htmlentities($_REQUEST['upload_message']);?>
	</div>
<?php }?>


<?php echo (!empty($fileErr))? $fileErr : '';?>

<form action="image_upload.php" method="post" enctype="multipart/form-data" name="image_upload_form" id="image_upload_form" style="margin-bottom:0px;">
<label>Image file, maximum 4MB. it can be jpg, gif,  png:</label><br />
          <input name="image_upload_box" type="file" id="image_upload_box" size="40" />
          <input type="submit" name="submit" value="Upload image" />     
    
<input name="submitted_form" type="hidden" id="submitted_form" value="image_upload_form" />
</form>




<?php if(isset($_REQUEST['show_image']) and $_REQUEST['show_image']!=''){
?>
<p><img src="uploaded_images/<?php echo $_REQUEST['show_image'];?>" /></p>
<?php }?>
