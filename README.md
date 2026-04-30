# 🎬 Film, Dizi ve Oyun Keşif Uygulaması

Bu proje, kullanıcıların güncel filmleri, dizileri ve video oyunlarını keşfedebileceği, detaylarını inceleyebileceği ve favorilerini bulut ortamında saklayabileceği kapsamlı bir Android uygulamasıdır. Tamamen dinamik verilerle çalışan bu uygulama, modern bir kullanıcı deneyimi sunmayı hedefler.

## 🚀 Özellikler

* **Geniş İçerik Ağı:** TMDB API ile popüler filmler/diziler ve RAWG API ile trend oyunlar.
* **Gelişmiş Arama:** Film/Dizi ve Oyunlar için ayrılmış, yazarken anında sonuç getiren (Live Search) özel arama sekmeleri.
* **Bulut Tabanlı Favoriler:** Firebase Firestore entegrasyonu ile cihazdan bağımsız, kullanıcıya özel favori listesi.
* **Güvenli Kimlik Doğrulama:** Firebase Auth ile Kayıt Ol, Giriş Yap, Şifre Sıfırlama ve E-posta Onay kontrolü içeren güvenlik duvarı.
* **Detaylı İnceleme:** Diziler için otomatik hesaplanan sezon ve bölüm listeleri, filmler için IMDb puanları, oyunlar için RAWG puanları.
* **Dahili Oynatıcı:** Custom WebView tabanlı entegre oynatıcı ile uygulama dışına çıkmadan anında içerik izleme deneyimi.
* **Modern Kullanıcı Arayüzü:** Karanlık tema (Dark Theme) odaklı, Bottom Navigation ile desteklenmiş akıcı geçişlere sahip şık tasarım.

## 🛠️ Kullanılan Teknolojiler & Kütüphaneler

* **Dil:** Java
* **Ağ İstekleri & API:** Retrofit2 & Gson (JSON Parsing)
* **Görsel Yönetimi:** Glide (Dinamik afiş ve kapak fotoğrafları için)
* **Backend & Veritabanı:** Firebase Authentication, Firebase Cloud Firestore
* **Kullanılan API'ler:** The Movie Database (TMDB) API, RAWG Video Games API
* **Mimari Bileşenler:** RecyclerView, GridLayoutManager, Custom Adapters, Fragment tabanlı navigasyon.

## ⚙️ Kurulum ve Çalıştırma

Projeyi kendi ortamınızda test etmek veya geliştirmek için aşağıdaki adımları izleyebilirsiniz:

1. Projeyi bilgisayarınıza klonlayın:
   ```bash
   git clone [https://github.com/](https://github.com/)<KULLANICI_ADINIZ>/<PROJE_ADI>.git

2.Android Studio ile projeyi açın.

3.Firebase Kurulumu:

Kendi Firebase projenizi oluşturun.

google-services.json dosyasını indirip projenizdeki app/ dizininin içine yerleştirin.

Firebase Console üzerinden Authentication (Email/Password) ve Firestore Database hizmetlerini aktifleştirin.

4.API Anahtarları:

Projede TMDB ve RAWG API'leri kullanılmıştır. Kendi geliştirici hesaplarınızı açarak API anahtarlarınızı alın.

Proje içerisindeki Java sınıflarında (HomeFragment, SearchFragment, DetailActivity) bulunan API_KEY ve RAWG_API_KEY değişkenlerine kendi anahtarlarınızı yerleştirin.

5.Sağ üst köşedeki Sync Project with Gradle Files butonuna basın ve projeyi çalıştırın.

👨‍💻 Geliştirici
Eren Kaya
