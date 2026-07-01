package com.example.newsapp.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.newsapp.databinding.ActivityDefinicoesBinding
import com.example.newsapp.model.Constantes

/**
 * Activity de Definições.
 *
 * PARTE DE: Gabi
 * Cobre: Switch modo escuro, Checkbox notificações, SharedPreferences,
 *        criação do canal de notificações, envio de notificação de teste
 */
class DefinicoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDefinicoesBinding

    private val prefs by lazy {
        getSharedPreferences(Constantes.PREFS_NOME, Context.MODE_PRIVATE)
    }

    // Pedir permissão de notificações (Android 13+)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permissão concedida", Toast.LENGTH_SHORT).show()
        } else {
            binding.checkboxNotificacoes.isChecked = false
            prefs.edit().putBoolean(Constantes.PREFS_NOTIFICACOES, false).apply()
            Toast.makeText(this, "Permissão negada. Não verás notificações.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDefinicoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Definições"

        // Criar canal de notificações (obrigatório Android 8+)
        criarCanalNotificacao()

        // Carregar preferências guardadas
        carregarPreferencias()

        // Switch modo escuro
        binding.switchModoEscuro.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(Constantes.PREFS_MODO_ESCURO, isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // Checkbox notificações
        binding.checkboxNotificacoes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                verificarPermissaoNotificacoes()
            }
            prefs.edit().putBoolean(Constantes.PREFS_NOTIFICACOES, isChecked).apply()
            val msg = if (isChecked) "Notificações ativadas" else "Notificações desativadas"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // Botão de teste de notificação
        binding.btnTestarNotificacao.setOnClickListener {
            if (prefs.getBoolean(Constantes.PREFS_NOTIFICACOES, true)) {
                enviarNotificacao(
                    titulo = "NewsApp",
                    mensagem = "Tens novas notícias para ler!"
                )
            } else {
                Toast.makeText(this, "Notificações estão desativadas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Carrega as preferências guardadas e aplica na UI.
     */
    private fun carregarPreferencias() {
        binding.switchModoEscuro.isChecked = prefs.getBoolean(Constantes.PREFS_MODO_ESCURO, false)
        binding.checkboxNotificacoes.isChecked = prefs.getBoolean(Constantes.PREFS_NOTIFICACOES, true)
    }

    /**
     * Cria o canal de notificações (obrigatório no Android 8+).
     */
    private fun criarCanalNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                Constantes.CANAL_NOTIFICACOES,
                "Notícias",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificações de novas notícias"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    /**
     * Envia uma notificação local.
     */
    fun enviarNotificacao(titulo: String, mensagem: String) {
        val notificacao = NotificationCompat.Builder(this, Constantes.CANAL_NOTIFICACOES)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(mensagem)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notificacao)
    }

    private fun verificarPermissaoNotificacoes() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
