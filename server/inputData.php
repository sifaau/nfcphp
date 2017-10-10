<?php
	include "koneksi.php";
    
	//set parameter
	$uid 	= $_GET['uid'];
	$tanggal= 'CURRENT_TIMESTAMP';
	$status = 'mati';
	//inisialisasi sql
	$sql	= "INSERT INTO `tbl_uid` (`uid`, `tanggal`, `status`) VALUES ('$uid', '$tanggal', '$status')";
	//eksekusi sql
	$input 	= mysqli_query($koneksi, $sql); //sampai disini data BERHASIL di input
	
	//kalau data BERHASIL di input
	if($input) {
		//ambil data dari database
		$sql2 = "SELECT * FROM `tbl_uid`;";
		$result = mysqli_query($koneksi, $sql2) or die("Maaf, ada error" . mysqli_error($koneksi));

		while($row = mysqli_fetch_array($result))
		{
			//ambil data UID nya
			$uid_temp = $row['uid'];
			$tanggal_temp = $row['tanggal'];
			$status_temp = $row['status'];
			//cek status nya, inisialisasikan
			if($status_temp == 'mati'){
				$data_status = 'hidup';
			}
			if($status_temp == 'hidup'){
				$data_status = 'mati';
			}
			$sql3 = "DELETE FROM `tbl_uid` WHERE uid = ''";
			mysqli_query($koneksi, $sql3) or die("Maaf, ada error" . mysqli_error($koneksi));
			//update statusnya
			$sql4 = "UPDATE `tbl_uid` SET status = '$data_status' WHERE uid = '$uid_temp'";
			mysqli_query($koneksi, $sql4) or die("Maaf, ada error" . mysqli_error($koneksi));

		}
		
		echo "Data berhasil berubah";
	} else {
		//ambil data dari database
		$sql2 = "SELECT * FROM `tbl_uid`;";
		$result = mysqli_query($koneksi, $sql2) or die("Maaf, ada error" . mysqli_error($koneksi));

		while($row = mysqli_fetch_array($result))
		{
			//ambil data UID nya
			$uid_temp = $row['uid'];
			$tanggal_temp = $row['tanggal'];
			$status_temp = $row['status'];
			//cek status nya, inisialisasikan
			if($status_temp == 'mati'){
				$data_status = 'hidup';
			}
			if($status_temp == 'hidup'){
				$data_status = 'mati';
			}
			$sql3 = "DELETE FROM `tbl_uid` WHERE uid = ''";
			mysqli_query($koneksi, $sql3) or die("Maaf, ada error" . mysqli_error($koneksi));
			//update statusnya
			$sql4 = "UPDATE `tbl_uid` SET status = '$data_status' WHERE uid = '$uid_temp'";
			mysqli_query($koneksi, $sql4) or die("Maaf, ada error" . mysqli_error($koneksi));

		}
		
		echo "Data gagal di ubah";
	}
?>