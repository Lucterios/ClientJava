<?

$appli_dir="../../extensions/applis";
if (!is_dir($appli_dir)) $appli_dir="./extensions/applis";

if (is_file($appli_dir."/application.inc.php")) {
	include $appli_dir."/application.inc.php";
	$copy_right=application_CopyRight();
}
else
	$copy_right='CopyRight Lucterios.org';

if (is_file($appli_dir."/setup.inc.php")) {
	$core_dir="../../CORE";
	if (!is_dir($core_dir)) $core_dir="./CORE";
	include $core_dir."/setup_param.inc.php";
	include $appli_dir."/setup.inc.php";
} else {
	$extention_description='Lucterios';
	$extention_appli='Lucterios';
}

function getConfigFile()
{
	global $extention_titre;
	global $extention_appli;
	$http_referer=$_SERVER["HTTP_REFERER"];
	$pos=strpos($http_referer,'://');
	$protocol=substr($http_referer,0,$pos);
	$protocol=($protocol='')?'http':$protocol;
	$mode=($protocol=='https')?1:0;		
	$server_name=$_SERVER["SERVER_NAME"];
	$server_port=$_SERVER["SERVER_PORT"];
	$server_dir=$_SERVER["PHP_SELF"];
	if ($server_dir[0]=='/')
		$server_dir=substr($server_dir,1);
	$pos=strpos($server_dir,'UpdateClients/java');
	if ($pos===false) $pos=strpos($server_dir,'java');
	$server_dir=substr($server_dir,0,$pos);
	
	$conf_content="";
	$conf_content.="<?xml version='1.0' encoding='ISO-8859-1'?>\n";
	$conf_content.="<CONFIG>\n";
	$conf_content.="	<TITLE>$extention_titre</TITLE>\n";
	$conf_content.="	<SERVER name='$extention_appli' host='$server_name' port='$server_port' dir='$server_dir' mode='$mode'/>\n";
	$conf_content.="</CONFIG>\n";
	return $conf_content;
}

if ((array_key_exists('act',$_GET)) && ($_GET['act']='conf')) {
	header("Content-Type: text/*");
	header('Content-Disposition: attachment; filename="LucteriosClient.conf"');
	$content=getConfigFile()
	echo $content;
}
elseif ((array_key_exists('act',$_GET)) && ($_GET['act']='zip')) {
	
	if (is_file('NewJavaClient.zip')) unlink('NewJavaClient.zip');
	if (copy('JavaClient.zip','NewJavaClient.zip')) {
		header("Content-Type: application/zip");
		header('Content-Disposition: attachment; filename="ClientJava.zip"');
		$za = new ZipArchive();
		$za->open(realpath('NewJavaClient.zip'));
		$za->addFromString("LucteriosClient.conf",getConfigFile());
		$za->addFile("LucteriosClient.bat");
		$za->addFile("LucteriosClient.sh");
		$za->addFile("LucteriosLogo.png");
		$za->addFile("LucteriosLogo.ico");
		$za->close();
		$content=file_get_contents('NewJavaClient.zip');
		unlink('NewJavaClient.zip');
		echo $content;
	}
	else echo "Error";
}
else
{
header('Content-Type: text/html; charset=ISO-8859-1');
?>
<html>
<head>
  <title><? echo $extention_description;?></title>
	<style type="text/css">
	<!--
		BODY {
		background-color: white;
		}
		
		h1 {
		font-family : Helvetica, serif;
		font-size : 10mm;
		font-weight : bold;
		text-align : center;
		vertical-align : middle;
		}
		
		h2 {
		font-size : 8mm;
		font-style : italic;
		font-weight : lighter;
		text-align : center;
		}
		
		h3 {
		font-size : 6mm;
		font-style : italic;
		text-decoration : underline;
		text-align : center;
		}
		
		img {
		border-style: none;
		}
		
		TABLE.main {
		width: 100%;
		height: 95%;
		background-color: rgb(18, 0, 130);
		}
		
		/* banniere */
		
		TR.banniere {
		}
		
		TD.banniere {
		width: 980px;
		height: 70px;
		border-style: solid;
		border-color: orange;
		border-width: 1px;
		}
		
		h1.banniere {
		color: orange;
		}

		TD.menu {
		width: 120px;
		vertical-align: top;
		color: white;
		}	
			
		/* corps */
		TR.corps {
		width: 980px;
		}
		
		TD.corps {
		width: 860px;
		vertical-align: top;
		background-color: white;
		}
		
		/* pied */
		TR.pied {
		width: 980px;
		height: 15px;
		background-color: rgb(100, 100, 200);
		}
		
		TD.pied {
		width: 980px;
		height: 15px;
		color: rgb(230, 230, 230);
		font-size: 9px;
		text-align: right;
		font-weight: bold;
		}	
		
		.go {
		font-family : verdana,helvetica;
		font-size : 10pt;
		font-style : oblique;
		text-decoration : none;
		text-indent : 2cm;
		}
		
		TD {
		font-size: 10pt;
		font-family: verdana,helvetica;
		}
		
		a.invisible {
		color: rgb(18, 0, 130);
		font-size: 0px;
		}
		
		li {
		}
		
		ul {
		font-style : italic;
		}
	-->
	</style>
</head>
<body>
<table class="main">
    <tr class="banniere">
        <td colspan="2" class="banniere">
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
                <tr>
                    <td>
                        <img src='<? echo $appli_dir."/images/logo.gif";?>' alt='logo' />
                    </td>
                    <td align="center">
                        <h1 class="banniere"><? echo $extention_description;?></h1>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr class="corps">	
        <td class="menu">
        </td>
        <td class="corps">
		<br>
		<h3>Page d'Installation de l'interface graphique</h3>
		<br>
		<br>
		Pour installer le client JAVA:<br>
		<ul>
			<li>Téléchargez cette archive <a href="index.php?act=zip">ClientJava.zip</a> (Version <?echo str_replace(' ','.',trim(file_get_contents('version.txt')));?>)</li>
			<li>Extractez la dans le répertoire de votre choix</li>
			<li>Créez un racourcis sur votre bureau avec le fichier <i>LucteriosClient.bat</i> (sous MS-Windows) ou <i>LucteriosClient.sh</i> (sous Apple-Macintosh, Linux ou Unix).</li>
			<li>Lancez l'application en cliquant sur le liens.</li>
		</ul>
		<i>Attention:</i> vérifiez que l'environnement JAVA est bien installé sur votre ordinateur (téléchargement <a href="http://www.java.com/fr/download">ici</a>).
		<br><br>
		Pour modifier la configuration de votre client JAVA:<br>
		<ul>
			<li>Téléchargez ce fichier <a href="index.php?act=conf">LucteriosClient.conf</a> </li>
			<li>Lancer votre client JAVA</li>
			<li>Dans l'écran de connexion, cliquer sur <i>Conf...</i>.</li>
			<li>Importer le fichier précédent.</li>
			<li>Valider.</li>
		</ul>
        </td>
    </tr>
    <tr class="pied">
        <td colspan="2" class="pied">
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
                <tr>
                    <td class="pied">
                        <? echo $copy_right;?> - Mise à jour <? echo date ("d/m/Y", filemtime("index.php")); ?>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>
 
<?
}
?>