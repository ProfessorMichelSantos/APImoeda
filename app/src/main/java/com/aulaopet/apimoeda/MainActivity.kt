package com.aulaopet.apimoeda

import android.app.DownloadManager
import android.app.DownloadManager.Request
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.provider.FontsContractCompat
import com.aulaopet.apimoeda.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) //substitui o set content view normal pelo binding

        buscadados().start()

        //setContentView(R.layout.activity_main)
    }

    private fun buscadados(): Thread {
        return Thread {
            val url = URL("https://open.er-api.com/v6/latest/brl")
            val connection = url.openConnection() as HttpsURLConnection

            if (connection.responseCode == 200) {
                val inputsystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputsystem, "UTF-8")
                val request = Gson().fromJson(inputStreamReader, requisicao::class.java)

                atualizaui(request)

                inputStreamReader.close()
                inputsystem.close()

            } else {
                binding.moeda.text = "Problema na conexão"
            }


        }

    }

    private fun atualizaui(request: requisicao)
    {
        runOnUiThread{
            kotlin.run{
                binding.atualizacao.text = request.ultima_atualizacao
                binding.moeda.text = String.format("R: %2f ", request.rates.BRL)
                binding.dolar.text = String.format("USD: %2f ", request.rates.USD)
            }
        }

    }

}