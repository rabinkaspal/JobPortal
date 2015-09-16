<?php 
include_once("includes/db_connect.php");


//js_id,job_id,keywords,view_count

$js_id = $_GET['js_id'];
//$job_id = $_GET['job_id'];
$keyword = $_GET['keyword'];

//$js_id = 4;
//$job_id = 602;
//$keyword="asdfas asdf";

$q = "select * from analytics where js_id={$js_id}";

$res = mysql_query($q) or die(mysql_error());

      if($res){
            if(mysql_num_rows($res)>0){
                  
//                  $view_count = mysql_result(mysql_query("select views from analytics where js_id={$js_id} and job_id={$job_id}"),0);
//                  $view_count++;
                 

                  $kw = mysql_result(mysql_query("select keyword from analytics where js_id={$js_id}"),0);
                  $cm = mysql_result(mysql_query("select common from analytics where js_id={$js_id}"),0);
//                  echo $kw . "<br/>";
                   $words = array();
                  $words[0] = $kw;
                  $words[1] = $keyword;
                  $common = getCommon($words);
                  
                  $keyword_new = $keyword;
                  $keyword_new .= " " . $kw;
                  
                  $common_new = $common;
                  $common_new .= " ". $cm;
                  
                //  $keyword_new = implode(' ',array_unique(explode(' ', $keyword_new)));
                  $common_new = implode(' ',array_unique(explode(' ', $common_new)));
                  
      
//                  echo $keyword_new;
                  
                  mysql_query("update analytics set keyword='$keyword_new', common='$common_new' where  js_id={$js_id}");
                        
                  echo json_encode(array("message"=>"updated"));
                  
            }else{
                  mysql_query("insert into analytics(js_id, job_id, keyword,views) values({$js_id}, 0, '$keyword',1)") or die(mysql_error());
                 echo json_encode(array("message"=>"inserted"));
            }
      }


      

function getCommon($array,$occurance = 2){
    $array = array_reduce($array, function($a,$b) { $a = array_merge($a,explode(" ", $b)); return $a; },array());
    return implode(" ",array_keys(array_filter(array_count_values($array),function($var)use($occurance) {return $var > $occurance ;})));
}


?>