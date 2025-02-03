package kz.talipovsn.retrofit_demo;

import android.annotation.SuppressLint;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

// КЛАСС ПОДКЛЮЧЕНИЯ БЕЗ ПРОВЕРКИ СЕРТИФИКАТОВ САЙТА
class UnsafeOkHttpClient {
    // Метод для создания объекта для работы с сайтом без проверки сертификатов
    static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Создаем объект для работы с SSL
            final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
            };

            // Создаем контекст SSL и устанавливаем наш SSL-сокет
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Создаем объект для работы с сайтом
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @SuppressLint("BadHostnameVerifier")
                @Override // Проверка имени хоста
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                } // Всегда возвращаем true
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
