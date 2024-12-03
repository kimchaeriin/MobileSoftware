package com.practice.android.pocketmate

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.practice.android.pocketmate.databinding.ActivityStatisticsBinding

class StatisticsActivity : AppCompatActivity() {
    lateinit var binding: ActivityStatisticsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarStatistics)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.webViewClient = WebViewClient()

        loadPieChart(78,22) //이후 수정
    }

    private fun loadPieChart(agree:Int, disagree:Int){
        val htmlCode = """
            <!DOCTYPE html>
            <html>
              <head>
                <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
                <script type="text/javascript">
                  google.charts.load('current', {packages:['corechart']});
                  google.charts.setOnLoadCallback(drawChart);

                  function drawChart() {
                    var data = google.visualization.arrayToDataTable([
                      ['Category', 'Value'],
                      ['찬성', $agree],
                      ['반대', $disagree]                     
                    ]);

                    var options = {
                      title: '소비계획 통계',
                      is3D: false,
                    };

                    var chart = new google.visualization.PieChart(document.getElementById('piechart'));
                    chart.draw(data, options);
                  }
                </script>
              </head>
              <body>
                <div id="piechart" style="width: 100%; height: 100%;"></div>
              </body>
            </html>

        """.trimIndent()
        binding.webView.loadDataWithBaseURL(null, htmlCode,"text/html","UTF-8",null)
    }
}