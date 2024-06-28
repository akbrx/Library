<?php

include '../koneksi.php';
/**
 *  @var $connection PDO
 */

if ($_SERVER['REQUEST_METHOD'] != 'POST') {
    header('Content-Type: application/json');
    http_response_code(400);
    $reply['meta'] = [
        'message' => 'POST method required',
        'status' => false
    ];
    echo json_encode($reply);
    exit();
}

/**
 *  Input data
 */
$input = json_decode(file_get_contents('php://input'), true);

$member_id = $input['member_id'] ?? '';
$member_nama = $input['member_nama'] ?? '';
$book_id = $input['book_id'] ?? '';
$book_nama = $input['book_nama'] ?? '';
$peminjaman = $input['peminjaman'] ?? '';
$pengembalian = $input['pengembalian'] ?? '';

/**
 * Validation
 */
$status = false;
$message = "";
$code = 200;
$isValidate = true;

if (empty($member_id)) {
    $message = "Member ID harus diisi";
    $isValidate = false;
}
if (empty($member_nama)) {
    $message = "Nama member harus diisi";
    $isValidate = false;
}
if (empty($book_id)) {
    $message = "Book ID harus diisi";
    $isValidate = false;
}
if (empty($book_nama)) {
    $message = "Nama buku harus diisi";
    $isValidate = false;
}
if (empty($peminjaman)) {
    $message = "Tanggal peminjaman harus diisi";
    $isValidate = false;
}
if (empty($pengembalian)) {
    $message = "Tanggal pengembalian harus diisi";
    $isValidate = false;
}

if (!$isValidate) {
    http_response_code(400);
    $reply['meta'] = [
        'message' => $message,
        'status' => $isValidate
    ];
    echo json_encode($reply);
    exit();
}

try {
    // Insert into database
    $insertLoan = "INSERT INTO loans (member_id, member_nama, book_id, book_nama, peminjaman, pengembalian) 
                    VALUES (:member_id, :member_nama, :book_id, :book_nama, :peminjaman, :pengembalian)";
    $statement = $conn->prepare($insertLoan);

    // Bind values
    $statement->bindValue(":member_id", $member_id);
    $statement->bindValue(":member_nama", $member_nama);
    $statement->bindValue(":book_id", $book_id);
    $statement->bindValue(":book_nama", $book_nama);
    $statement->bindValue(":peminjaman", $peminjaman);
    $statement->bindValue(":pengembalian", $pengembalian);

    $execute = $statement->execute();
    if ($execute) {
        $message = "Data peminjaman berhasil ditambahkan";
        $status = true;
    } else {
        $message = $statement->errorInfo();
        $code = 400;
    }

} catch (Exception $exception) {
    $message = $exception->getMessage();
    $status = false;
    $code = 400;
}

// HEADER API
header('Content-Type: application/json');
$reply['meta'] = [
    'message' => $message,
    'status' => $status
];
http_response_code($code);
echo json_encode($reply);
