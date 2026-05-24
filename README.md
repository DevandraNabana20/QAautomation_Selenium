# QAautomation_Selenium_Webdriver

## 📝 Review & Pembelajaran

Project ini merupakan implementasi otomatisasi UI menggunakan Selenium WebDriver dan TestNG dengan fokus pada *E-Commerce user journey - SauceDemo dan Bookstore* (dari Homepage hingga Checkout). Berikut adalah poin pembelajaran utamanya:

### 1. Handling Dynamic Elements
Aplikasi web modern sering memicu error seperti `ElementClickInterceptedException`. Solusi yang kita terapkan:
*   **Dynamic XPath:** Menggunakan variabel dalam XPath (contoh: `//td[normalize-space()='" + bookName + "']`) untuk memvalidasi elemen yang datanya dinamis tanpa perlu *hardcode*.
*   **Scroll & Wait:** Mewajibkan penggunaan `scrollToElement()` sebelum klik untuk memastikan elemen ada di dalam layar, dipadukan dengan `waitForElementToBeVisible()` agar eksekusi menunggu elemen siap.
*   **List & Regex:** Untuk fitur Sorting (ASC/DESC), kita mengambil kumpulan elemen harga ke dalam `List`, membersihkan simbol mata uang dengan Regex, lalu memvalidasi urutannya secara matematis menggunakan `Collections.sort()`.

### 2. Test Independence 
Test yang bergantung satu sama lain bisa menyebabkan *flaky test* (test gagal secara beruntun).
*   **Solusi:** Karena browser di-reset (tear down) di setiap akhir metode test, kita membuat *helper method* seperti `setupCartState()`. Ini memastikan setiap test (seperti Update Cart atau Delete Cart) melakukan persiapan *state* (Login -> Add Item -> Cart) secara mandiri dari awal, sehingga hasilnya akurat dan stabil.

### 3. Parallel Execution
Framework ini sudah siap untuk dieksekusi secara paralel guna mempercepat waktu testing.
*   **ThreadLocal WebDriver:** Menggunakan class `DriverManager` dengan `ThreadLocal<WebDriver>` memastikan setiap test/thread memiliki *instance* browsernya sendiri (terisolasi) sehingga tidak saling bertabrakan saat dijalankan bersamaan.

