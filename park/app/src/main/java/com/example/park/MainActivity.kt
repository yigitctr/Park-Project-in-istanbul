package com.example.park

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.park.databinding.ActivityMainBinding
import com.google.android.gms.maps.OnMapReadyCallback

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.findViewById(R.id.mapView).also { mapView = it }
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
            // Harita üzerinde gerekli işlemleri yapabilirsiniz.
        }
    }
}

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        val htmlContent = """
            <html>
            <head>
                <title>My WebView</title>
                <script>
                    var ibbMAP = new SehirHaritasiAPI({mapFrame:"mapFrame",apiKey:"..."},function(){
                        ...
                    });
                </script>
            </head>
            <body>
                <div id="mapFrame" style="width:100%;height:500px;"></div>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        // harita yüklenmesi tamamlandıktan sonra çalışması istenilen fonksiyonlar
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        // JavaScript ile iletişim için bir arayüz oluşturuluyor
        webView.addJavascriptInterface(WebAppInterface(), "Android")

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.Tiklaninca(function (lat, lon, zoom) {
                            Android.showLocation(lat, lon, zoom);
                        });
                    });

                    // Android tarafından çağrılacak fonksiyon
                    function showLocation(lat, lon, zoom) {
                        // Android tarafına konum bilgisini gönderme
                        Android.showToast(lat + "," + lon + ", " + zoom);
                    }
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }

    // JavaScript ile etkileşim için bir arayüz oluşturuluyor
    inner class WebAppInterface {
        @JavascriptInterface
        fun showToast(location: String) {
            // JavaScript kodundan gelen konum bilgisini kullanma
            // Burada location değişkeni, JavaScript kodundan gelen lat, lon ve zoom bilgilerini içerir
            // Bu bilgileri kullanarak istediğiniz işlemleri gerçekleştirebilirsiniz
            runOnUiThread {
                // Android cihazda bir Toast mesajı gösterme
                Toast.makeText(applicationContext, location, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.TiklamaKaldir();
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.SonTiklananNokta(function (lat, lon, zoom) {
                            if (arguments.length == 3) {
                                alert("lat:" + lat + " lon:" + lon + " zoom:" + zoom);
                            } else
                                alert("Henüz tıklama yapılmadı !");
                        });
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.ZoomSeviyesi(function (zoom) { 
                            alert(zoom); 
                        }); 
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.HaritaOrtaNokta(function (lat, lon) {
                            alert(lat + ", " + lon);
                        }); 
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.KonumaGit("41.01371789571470","28.95547972584920", 19, true);
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.SadeceHarita();
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.Wgs84ItrfDonusumu('41.01371789571470','28.95547972584920',function (lat, lon) {
                            alert(lat + "," + lon );
                        });
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.ItrfWgs84Donusumu('392760.29334524315','4557526.423340271',function (lat, lon) {
                            alert(lat + "," + lon );
                        });
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.AltlikKatmanListele(function (katmanlar) {
                            alert(katmanlar);
                        });
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.AltlikKatmanGetir(function (name) {
                            alert(name);
                        });
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.AltlikKatmanAyarla('2013');
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.KatmanEkle('http://SUNUCU/arcgis/rest/services/KATMAN/MapServer/export?','ÖRNEK',true, 'REST');
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.KatmanKaldir();
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = WebViewClient()

        val htmlContent = """
            <html>
            <head>
                <script src="http://sehirharitasi.ibb.gov.tr/api/map2.js"></script>
            </head>
            <body>
                <div id="harita" style="width:100%; height:100%">
                    <iframe id="mapFrame" src="http://sehirharitasi.ibb.gov.tr/">
                        <p>Tarayıcınız iframe özelliklerini desteklemiyor !</p>
                    </iframe>
                </div>

                <script>
                    var ibbMAP = new SehirHaritasiAPI("mapFrame", function(){
                        ibbMAP.KatmanYoneticisiGetir();
                    });
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }
}
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    fun onMapReady(p0: GoogleMap) {
        // Harita hazır olduğunda burada gerekli işlemleri yapabilirsiniz
    }
}
