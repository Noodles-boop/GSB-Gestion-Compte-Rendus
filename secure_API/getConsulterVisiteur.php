<?php

$servername = 'localhost';
$username = 'root';
$password = '';
$databasename = 'gsb_doctor';


$conn = new mysqli($servername, $username, $password, $databasename);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$tab = array();

$sql = "SELECT titre, rdv, c.id, c.region FROM compte_rendu c INNER JOIN client c2 ON c2.id = c.id_visiteur";

$stmt = $conn->prepare($sql);
$stmt->execute();

$stmt->bind_result($titre, $rdv, $id, $region);

while ($stmt->fetch()) {
    $temp = [
        'titre' => $titre,
        'rdv' => $rdv,
        'id' => $id, 
        'region' => $region 
    ];

    array_push($tab, $temp);
}

echo json_encode($tab);
