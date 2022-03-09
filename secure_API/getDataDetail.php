<?php

    $servername = 'localhost';
    $username = 'root';
    $password = '';
    $databasename = 'gsb_doctor';
    

    $conn = new mysqli($servername, $username, $password, $databasename);

    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    if (isset($_GET['id']))
        $id = $_GET['id'];
    else
        $id = null;

    $tab = array();
        
        $sql = "SELECT prenom, nom, rdv, id FROM compterendu where id = '$id'";

        $stmt = $conn->prepare($sql);
        $stmt->execute();


        $stmt->bind_result($prenom, $nom, $rdv, $id);
        while($stmt->fetch()) {
            $temp = [
                'prenom'=>$prenom,
                'nom'=>$nom,
                'rdv'=>$rdv,
                'id'=>$id
            ];

            array_push($tab, $temp);
        }

        echo json_encode($tab);