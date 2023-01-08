package orestesk.multipop

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import javax.net.ssl.*


private const val BASE_URL = "https://98.10.243.234:8080"
class Instances {

    object RetrofitInstance {
        fun getInstance(): Retrofit {
            val mHttpLoggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            val mOkHttpClient = OkHttpClient
                .Builder()
                //.addInterceptor(mHttpLoggingInterceptor)
                .sslSocketFactory(
                    createCertificate(LoginActivity.appContext.resources.openRawResource(R.raw.cert)).socketFactory,
                    systemDefaultTrustManager()!!
                )
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(mOkHttpClient)
                .build()
        }

        private fun createCertificate(trustedCertificateIS: InputStream): SSLContext {
            val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
            val ca: Certificate = trustedCertificateIS.use { cert ->
                cf.generateCertificate(cert)
            }

            // creating a KeyStore containing our trusted CAs
            val keyStoreType: String = KeyStore.getDefaultType()
            val keyStore: KeyStore = KeyStore.getInstance(keyStoreType)
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)

            // creating a TrustManager that trusts the CAs in our KeyStore
            val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
            val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm)
            tmf.init(keyStore)

            // creating an SSLSocketFactory that uses our TrustManager
            val sslContext: SSLContext = SSLContext.getInstance("TLS")
            sslContext.init(null, tmf.trustManagers, null)
            return sslContext
        }

        private fun systemDefaultTrustManager(): X509TrustManager? {
            return try {
                val trustManagerFactory: TrustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
                check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                    "Unexpected default trust managers:" + trustManagers.contentToString()
                }
                trustManagers[0] as X509TrustManager
            } catch (e: GeneralSecurityException) {
                throw AssertionError() // no TLS,  give up.
            }
        }
    }
}