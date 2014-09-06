<?php
require_once 'tcpdf/tcpdf.php';
$user = 'openuser';
$passwd = 'openerp';
$db = 'moleqla';
$port = 5432;
$host = 'localhost';
$strCnx = "host=$host port=$port dbname=$db user=$user password=$passwd";
$conn = pg_connect($strCnx) or die ("Error de conexion. ". pg_last_error());
echo "Conexion exitosa <hr>";

//"/home/rafael/NetBeansProjects/MoleQla_Web/build/web/WEB-INF/numeros";

echo "\n 0 ->".$argv[0];
echo "\n 1 ->".$argv[1];
echo "\n 2 ->".$argv[2];
echo "\n 3 ->".$argv[3];
        

$rutaDestino = $argv[1];
print $rutaDestino;

$resultEditores = pg_query($conn, "SELECT CONCAT(e.nombre, ' ' ,e.apellidos) as usuario, (SELECT s.nombre FROM seccion s WHERE e.seccion_id = s.id) as seccion FROM editor e WHERE e.seccion_id IN (
	SELECT art.seccion_id FROM articulo art WHERE art.numero_id = 
		(SELECT DISTINCT(numero_id) FROM articulo A WHERE 
			(SELECT state FROM numero N WHERE N.id = A.numero_id) = 'a_publicar'))");
if (!$resultEditores) {
  echo "An error occurred in editor.\n";
  exit;
}

$resultMaquetadores = pg_query($conn, "SELECT CONCAT(e.nombre, ' ' ,e.apellidos) as usuario, (SELECT s.nombre FROM seccion s WHERE e.seccion_id = s.id) as seccion FROM maquetador e WHERE e.seccion_id IN (
	SELECT art.seccion_id FROM articulo art WHERE art.numero_id = 
		(SELECT DISTINCT(numero_id) FROM articulo A WHERE 
			(SELECT state FROM numero N WHERE N.id = A.numero_id) = 'a_publicar'))");
if (!$resultMaquetadores) {
  echo "An error occurred in maquetador.\n";
  exit;
}


// Crear el documento
$pdf = new TCPDF(PDF_PAGE_ORIENTATION, PDF_UNIT, PDF_PAGE_FORMAT, true, "UTF-8", false);

//Font
$pdf->SetFont('times', '', 11);

//Linea header and footer
$pdf->SetPrintHeader(false);
$pdf->SetPrintFooter(false);

//Margin
$pdf->SetLeftMargin(20);
$pdf->SetRightMargin(20);

// Anadir pagina
$pdf->AddPage();


//Obtemos el html
$html = retornaHtml($conn, $resultEditores, $resultMaquetadores);

// output the HTML content
$pdf->writeHTML($html);

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

// reset pointer to the last page
$pdf->lastPage();

// ---------------------------------------------------------

//PERMISOS RUTA: chmod -R 0777 /yourdirectory
//Close and output PDF document
$pdf->Output($rutaDestino.'/participantes.pdf', 'F');

print "OK";

function retornaHtml($conn, $resultEditores, $resultMaquetadores){

//Aplicamos operaciones de modificacion
$html ="";

//Portada
$html .= "<div class=WordSection1>

<p><b><>Portada</b></p>
<p>Carmen Santisteban Trigo</p>
<p></p>";

//Logotipo y Título de la revista
$html .= "
<p><b>Logotipo y Título de la revista</b></p>
<p>Juan Manuel García Arcos, Rafael Hoyos Manchado y Rafael Iigo
Rocio Escudero Ávila, Inés Maldonado Lasunción y Javier Revello Sánchez</p>
<p></p>";

//Plantilla de la revista
$html .= "
<p><b>Plantilla de la revista</b></p>
<p>Norberto Díaz Díaz</p>
<p></p>";

//Responsables de las secciones que aparecen en este número
$html .= "
<p><b>Responsables de las secciones que aparecen en este número</b></p>";


$html .= "<dl>";
while ($row = pg_fetch_row($resultEditores)) {
  $editor = $row[0];
  $seccion = $row[1];

	$html .= "<dd><b><i>MoleQla $seccion: </i></b>$editor</dd>";
}
$html .= "</dl>";

$html .= "<p></p>";


//Responsables de maquetación de las secciones que aparecen en este número
$html .= "
<p><b>Responsables de maquetación de las secciones que aparecen en este número</b></p>";

$html .= "<dl>";
while ($row = pg_fetch_row($resultMaquetadores)) {
  $maquetador = $row[0];
  $seccion = $row[1];

	$html .= "<dd><b><i>MoleQla $seccion: </i></b>$maquetador</dd>";
}
$html .= "</dl>";

$html .= "<p></p>";

$html .= '<p>Información sobre todas las secciones de MoleQla en <a href="http://www.upo.es/MoleQla">MoleQla</a></p>';

$html .= "
<p><b>Editores</b></p>";

$html .= "<ul>
	<li>Sofía Calero Díaz</li>
	<li>Ana Paula Zaderenko Partida</li>
	<li>Juan Antonio Anta Montalvo</li>
	<li>Patrick J. Merkling</li>
</ul>";

$html .= "<p></p>";

$html .= '
<table boder="0">
	<tr>
		<td style="margin: 0"><img src="images/upo.png" alt="UPO" width="60" height="60" border="0"/></td>
		<td>ISSN 2173-0903<br />
			Editado el 21 de Junio de 2014<br />
			Universidad Pablo de Olavide, Sevilla, España</td>
	</tr>
</table>
';
//Fin div Section
$html .="</div>";

ob_end_clean();
return $html;
}
?>
