# Aplikasi Kasir TS MINIMARKET

Aplikasi Kasir berbasis JavaFX dan MySQL yang dirancang untuk efisiensi operasional minimarket. Sistem ini mendukung pengelolaan transaksi, penomoran nota otomatis, serta kalkulasi diskon bertingkat secara *real-time*.

## Fitur Utama
* **Transaksi Otomatis**: Generate nomor nota otomatis (`TS-XXX`) berbasis *sequence* database.
* **Data Lookup Cepat**: *Auto-populate* data pelanggan menggunakan `HashMap` untuk efisiensi akses memori.
* **Kalkulasi Diskon Bertingkat**: Sistem diskon otomatis (0%, 5%, 10%) berdasarkan total belanja pelanggan.
* **Manajemen Data Terintegrasi**: Sinkronisasi data antar tabel (`Nota` dan `Nota_Detail`) menggunakan teknik *transactional sync*.
* **Antarmuka GUI**: Desain antarmuka modern dan responsif dengan JavaFX.

## Arsitektur Sistem
* **Bahasa Pemrograman**: Java (JDK 25)
* **UI Framework**: JavaFX
* **Database**: MySQL
* **Paradigma**: Object-Oriented Programming (OOP) dengan desain *Controller-to-Controller Data Passing*.

## Struktur Proyek
Aplikasi ini dibangun menggunakan arsitektur MVC (Model-View-Controller) sederhana untuk memisahkan logika tampilan dan database:
- `Menu_Utama`: Pusat kendali dan manajemen nota.
- `Input`: Pengelola logika transaksi harian.
- `FormInput`: Modul penambahan item belanja.
- `Preview`: Modul pratinjau struk belanja spesifik.

## Cara Instalasi
1.  Pastikan JDK 25 atau yang lebih baru telah terinstal.
2.  Impor database MySQL yang diperlukan (pastikan tabel `Nota`, `Nota_Detail`, `Barang`, dan `Pelanggan` sudah dibuat).
3.  Konfigurasikan kelas `Koneksi.java` sesuai dengan pengaturan *database* lokal Anda.
4.  Jalankan melalui IDE (VS Code atau IntelliJ) menggunakan perintah `Proyek_UTS.java`.

## Kontributor
Proyek ini dikembangkan oleh **Wa Ode Nur Kumala Dewi** sebagai pemenuhan tugas UTS mata kuliah Praktikum Algoritma dan Pemrograman 2, Universitas Ibnu Sina.