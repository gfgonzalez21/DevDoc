package com.gfg.devdoc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gfg.devdoc.Administrador.Registrar_admin
import com.gfg.devdoc.Cliente.Registro_cliente
import com.gfg.devdoc.databinding.ActivityElegirRolBinding

class Elegir_rol : AppCompatActivity() {

    private lateinit var binding:ActivityElegirRolBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityElegirRolBinding.inflate(layoutInflater)
        binding=ActivityElegirRolBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.BtnRolAdmin.setOnClickListener{
            //Toast.makeText(applicationContext,"rol admin",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@Elegir_rol,Registrar_admin::class.java))
        }
        binding.BtnRolCliente.setOnClickListener{
            startActivity(Intent(this@Elegir_rol,Registro_cliente::class.java))
            //Toast.makeText(applicationContext,"rol cliente",Toast.LENGTH_SHORT).show()

        }
    }
}