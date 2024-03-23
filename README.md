# TP2DPBO2024C1
Tugas Praktikum 2 Mata Kuliah DPBO (CRUD di Intellij IDEA)

/_ Saya Asyqari NIM 2102204 mengerjakan soal TP 2 dalam mata kuliah DPBO untuk keberkahanNya
maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin. _/

**Penjelasan Alur Program**
Program ini merupakan program CRUD sederhana untuk memasukan data mahasiswa ke dalam database mySQL

Tampilan awal program merupakan suatu form yang berisi beberapa field text yang bisa di isi, lalu ada beberapa tombol aksi yang bisa dilakukan

![Screenshot 2024-03-23 at 12 08 53](https://github.com/asyqari/TP2DPBO2024C1/assets/90365732/9b63f6fd-5cfa-42a9-8417-718196567370)

**Insert**
1. Insert hanya bisa dilakukan bila nim yang dimasukan tidak ada yang sama didalam data base
   a. Hal ini bisa dilakukan karena ada pengecekan di fungsi insertData dengan cara mencoba searching nim       yang dimasukan ke database menggunakan query sql 'SELECT COUNT(*)...'
   ![Screenshot 2024-03-23 at 12 13 11](https://github.com/asyqari/TP2DPBO2024C1/assets/90365732/d6afb193-1080-460d-a74f-91c3711d6545)
3. Insert hanya bisa dilakukan bila semua field telah diisi tanpa ada yang kosong
   a. Hal ini dilakukan dengan cara melakukan pengecekan dengan cara mengecek apakah setiap field               isNotEmpty
   ![Screenshot 2024-03-23 at 12 03 21](https://github.com/asyqari/TP2DPBO2024C1/assets/90365732/7256e8f6-0702-4481-8af7-6ae1aea0c542)

5. Jika kedua kondisi diatas tidak terpenuhi maka akan ada pesan gagal jika sudah terpenuhi maka akan ada pesan berhasil dan data masuk ke database, lalu dilakukan refresh untuk mengambil data baru

**Update dan Delete**
1. Update dan Delete menggunakan metode yang sama untuk _meng specify_ data mana yang mau di hapus/update berdasarkan id yang ada di database
  a. Hal ini menggunakan fungsi getIdFromDatabase dimana dilakukan query ke database untuk mengambil data id di urutan yang sudah ditentukan yaitu lewat parameter _selectedindex_ asumsi nya urutan data di aplikasi dan database sama
2. Untuk update hanya bisa dilakukan bila semua field terisi semua tidak ada yang kosong (seperti insert)
3. jika sudah berhasil maka akan muncul pop up berhasil dan data akan di refresh untuk ditampilkan kembali
