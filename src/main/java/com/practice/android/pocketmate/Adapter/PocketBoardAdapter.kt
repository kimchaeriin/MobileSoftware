package com.practice.android.pocketmate.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.practice.android.pocketmate.Model.BoardModel
import com.practice.android.pocketmate.Pocket.PocketActivity
import com.practice.android.pocketmate.R
import com.practice.android.pocketmate.databinding.ItemBoardBinding

class PocketBoardAdapter(context: Context,
                         itemList: MutableList<BoardModel>,
                         keyList: MutableList<String>)
    : BoardAdapter(context, itemList, keyList) {

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val binding = holder.binding
        val key = keyList[position]
        val tip = itemList[position]
        bindItems(binding, tip, key)
        binding.root.setOnClickListener {
            switchScreenWithKey(key)
        }

        binding.statisticsBtn.visibility = View.VISIBLE
        binding.statisticsBtn.setOnClickListener{
            val agree = binding.agreeNumber.text.toString().toDouble()
            val disagree = binding.disagreeNumber.text.toString().toDouble()
            val agreeRatio : Double
            val disagreeRatio : Double

            if(disagree != 0.0) {
                agreeRatio = agree / (agree + disagree)
            }
            else
                agreeRatio = 100.0

            if(agree != 0.0) {
                disagreeRatio = disagree / (agree + disagree)
            }
            else
                disagreeRatio = 100.0

            showDialog(context,agreeRatio,disagreeRatio)
        }
    }

    private fun switchScreenWithKey(key: String) {
        val intent = Intent(context, PocketActivity::class.java).apply {
            putExtra("key", key)
        }
        context.startActivity(intent)
    }

    private fun bindItems(binding: ItemBoardBinding, tip: BoardModel, key: String) {
        binding.boardTitle.text = tip.title
        binding.boardContent.text = tip.content
        if (tip.image == 0) {
            binding.boardImage.visibility = View.GONE
        }
        else {
            binding.boardImage.setImageResource(tip.image)
        }
        binding.bookmarkBtn.visibility = View.GONE

        showCommentCount(key, binding)
        showPostReaction(key, binding)

    }

    private fun showDialog(context: Context,agree:Double,disagree: Double){
        val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_pie,null)
        val dialog = AlertDialog.Builder(context).setView(dialogLayout).create()

        dialog.show()

        val webView : WebView = dialogLayout.findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        val htmlCode = loadPie(agree, disagree)
        webView.loadDataWithBaseURL(null, htmlCode,"text/html","UTF-8",null)
    }

    private fun loadPie(agree: Double,disagree:Double): String {
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
                      title: '유저 의견',
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
        return htmlCode
    }
}
