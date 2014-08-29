<?php
require_once 'tcpdf/tcpdf.php';
$user = 'openuser';
$passwd = 'openerp';
$db = 'moleqla';
$port = 5432;
$host = 'localhost';
$strCnx = "host=$host port=$port dbname=$db user=$user password=$passwd";
$conn = pg_connect($strCnx) or die ("Error de conexion. ". pg_last_error());
//echo "Conexion exitosa <br />";

//"/home/rafael/NetBeansProjects/MoleQla_Web/build/web/WEB-INF/numeros";

/*echo "\n 0 ->".$argv[0];
echo "\n 1 ->".$argv[1];
echo "\n 2 ->".$argv[2];
echo "\n 3 ->".$argv[3];*/
        

$rutaDestino = $argv[1];
print $rutaDestino;

$result = pg_query($conn, "SELECT A.nombre, (SELECT S.nombre FROM seccion S WHERE A.seccion_id = S.id) as seccion
 									FROM articulo A 
										WHERE (SELECT state FROM numero N WHERE N.id = A.numero_id) = 'a_publicar' 
											ORDER BY A.seccion_id");
if (!$result) {
  echo "An error occurred in indice.\n";
  exit;
}


$resultEntrevista = pg_query($conn, "SELECT N.entrevista FROM numero N WHERE N.state = 'a_publicar'");
if (!$resultEntrevista) {
  echo "An error occurred in entrevista.\n";
  exit;
}

// Crear el documento
$pdf = new TCPDF(PDF_PAGE_ORIENTATION, PDF_UNIT, PDF_PAGE_FORMAT, true, "UTF-8", false);

//Font
$pdf->SetFont('times', '', 11);

//Linea header and footer
$pdf->SetHeaderData('', '', 'ÍNDICE', '', array(0,64,255), array(0,64,128));
$pdf->SetHeaderMargin(PDF_MARGIN_HEADER);
$pdf->SetPrintFooter(false);

//Margin
$pdf->SetLeftMargin(30);
$pdf->SetRightMargin(30);

// Anadir pagina
$pdf->AddPage();


//Obtemos el html
$html = retornaHtml($result,$resultEntrevista);

// output the HTML content
$pdf->writeHTML($html);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

// reset pointer to the last page
$pdf->lastPage();

// ---------------------------------------------------------

//PERMISOS RUTA: chmod -R 0777 /yourdirectory
//Close and output PDF document
$pdf->Output($rutaDestino.'/indice.pdf', 'F');

//$pdf->Output('indice.pdf', 'I');


//-----------------------------------------------------------------------------------


function retornaHtml($result,$resultEntrevista){

$html = "<div>";
    
$entrevista = "";
while ($row = pg_fetch_row($resultEntrevista)) {
	$entrevista = $row[0];               
}
    
$secciones = array();
$indice = array();

//Si existe una entrevista se añade al indice
if(empty($entrevista)==false){
    $indice["Entrevista"][] = "Entrevista de MoleQla";
    $secciones[] = "Entrevista";
}


while ($row = pg_fetch_row($result)) {
	$articulo = $row[0];
	$seccion = $row[1];

	$indice[$seccion][] = $articulo;
	if (in_array($seccion, $secciones) == false) {
		$secciones[] = $seccion;
	}

	
}


$html .= "<dl>";
for($i = 0; $i < count($secciones); $i++){
	$seccion = $secciones[$i];
	$punto = $i+1;
	$html .= "<dt><b>$punto.  MoleQla $seccion</b></dt>";

	for($j = 0; $j < count($indice[$seccion]); $j++){
		$art = $indice[$seccion][$j];
		$sub = $j+1;
		$html .= "<dd>$punto.$sub $art </dd>";
	}
	$html .= "<br />";

}
$html .= "</dl>";
$html .= "</div>";

//ob_end_clean();
return $html;
}
?>
