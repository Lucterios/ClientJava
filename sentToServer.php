<?
#!/bin/php
### Launch Script for Client Lucterios

function sendModuleToUpdateServer($UpdateServerUrl,$project,$pass,$module,$arcFile)
{
	$txt="";
	require_once("HTTP/Request.php");
	$req =& new HTTP_Request($UpdateServerUrl."/actions/up.php");
	$req->setMethod(HTTP_REQUEST_METHOD_POST);
	$req->addPostData("MAX_FILE_SIZE", "10000000");
	$req->addPostData("project", $project);
	$req->addPostData("pass", $pass);
	$req->addPostData("module", $module);
	$req->addFile("file", $arcFile);

	$response = $req->sendRequest();
	if (PEAR::isError($response))
        	$txt=$response->getMessage()."{[newline]}";
	else
        {
		$txt=trim($req->getResponseBody());
		if (strpos($txt,'<HTML>')!==false)
			$txt=str_replace(array('<','>'),array('{[',']}'),$txt);
                if ($txt=='')
                    $txt="Module '$module' envoyé.";
		$txt.="{[newline]}";
        }
	return $txt;
}

$txt=sendModuleToUpdateServer("http://pov.lucterios.org/lucteriosUpdates/","lucterios","abc123","java","java.lpk");
//$txt=sendModuleToUpdateServer("http://ffss.38.free.fr/updates/","lucterios","abc123","JavaClient","JavaClient.lpk");

echo $txt;

?>
