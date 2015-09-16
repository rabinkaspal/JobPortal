<?php

// response json
$json = array();

/**
 * Registering a user device
 * Store reg id in users table
 */
if (isset($_POST["js_id"]) && isset($_POST["regId"])) {
    $js_id = $_POST["js_id"];
    $regId = $_POST["regId"]; // GCM Registration ID
    
	
	// Store user details in db
    include_once './db_functions.php';
    include_once './GCM.php';

    $db = new DB_Functions();
    $gcm = new GCM();

    //$res = $db->storeUser($name, $email, $gcm_regid);
    $res = $db->storeUser2($js_id, $regId);

//  $registatoin_ids = array($regId);
//   $message = array("price" => "{\"job_id\":\"531\",\"job_title\":\"IT Assistant\",\"company\":\"Acharya Software Solutions\"}");
//  $result = $gcm->send_notification($registatoin_ids, $message);
//  echo $result;
} else {
    // user details missing
}
?>