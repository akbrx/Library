<?php

include '../koneksi.php';

if ($_POST) {

    //Data
    $email = $_POST['email'] ?? '';
    $password = $_POST['password'] ?? '';

    $response = []; //Data Response

    try {
        //Cek email didalam databse
        $userQuery = $conn->prepare("SELECT * FROM member where email = ? ");
        $userQuery->execute(array($email));
        $query = $userQuery->fetch();

        if ($userQuery->rowCount() == 0) {
            $response['status'] = false;
            $response['message'] = "email Tidak Terdaftar";
        } else {
            // Ambil password di db

            $passwordDB = $query['password'];

            if (strcmp(($password), $passwordDB) === 0) {
                $response['status'] = true;
                // $response['message'] = "Login Berhasil";
                $response['data'] = [
                    'member_id' => $query['id'],
                    'email' => $query['email'],
                    'username' => $query['username']
                ];
            } else {
                $response['status'] = false;
                $response['message'] = "Password anda salah";
            }
        }
    } catch (PDOException $e) {
        // Tangani kesalahan koneksi database
        $response['status'] = false;
        $response['message'] = "Kesalahan database: " . $e->getMessage();
    }

    //Jadikan data JSON
    $json = json_encode($response, JSON_PRETTY_PRINT);

    //Print
    echo $json;
}