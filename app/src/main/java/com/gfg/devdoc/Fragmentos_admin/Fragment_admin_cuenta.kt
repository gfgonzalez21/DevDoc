package com.gfg.devdoc.Fragmentos_admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gfg.devdoc.Elegir_rol
import com.gfg.devdoc.R
import com.gfg.devdoc.databinding.ActivityRegistrarAdminBinding
import com.gfg.devdoc.databinding.FragmentAdminCuentaBinding
import com.google.firebase.auth.FirebaseAuth


class Fragment_admin_cuenta : Fragment() {

private lateinit var binding: FragmentAdminCuentaBinding
private lateinit var firebaseAuth: FirebaseAuth
private lateinit var mcontext: Context

    override fun onAttach(context: Context) {
        mcontext=context
        super.onAttach(context)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentAdminCuentaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth=FirebaseAuth.getInstance()
        binding.CerrarSesionAdmin.setOnClickListener {
            firebaseAuth.signOut()
            firebaseAuth.signOut()
            startActivity(Intent(context,Elegir_rol::class.java))
            activity?.finishAffinity()
        }
    }
}